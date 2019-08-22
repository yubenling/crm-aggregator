package com.kycrm.syn.service.trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.dao.trade.ITradeDTODao;
import com.kycrm.syn.util.RedisLock;
import com.kycrm.util.Constants;
import com.kycrm.util.JayCommonUtil;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.TradeStatusCompareUtil;
import com.kycrm.util.ValidateUtil;

/** 
* @author wy
* @version 创建时间：2018年2月8日 下午3:39:27
*/
@Service
@MyDataSource
public class TradeDTOMongoService {
    
    @Autowired
    private ITradeDTODao tradeDao;
    

    @Resource(name = "redisTemplateLock")
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private TradeErrorMessageService tradeErrorMessageService;
    
    private Logger logger = LoggerFactory.getLogger(TradeDTOMongoService.class);
    
    
    public String findTradeStatusByTid(Long uid,Long tid){
        Map<String,Long> map = new HashMap<String,Long>(5);
        map.put("uid", uid);
        map.put("tid", tid);
        return this.tradeDao.findStatusByTid(map);
    }
    
    
    /**
     * 批量保存订单
     * @author: wy
     * @time: 2018年1月22日 下午4:31:37
     * @param uid 用户主键id
     * @param tradeList 要保持的订单集合
     */
    
    public void batchInsertTradeList(Long uid, List<TradeDTO> tradeList) {
        if (ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(tradeList)) {
            return;
        }
        long startTime = System.currentTimeMillis();
        this.logger.info("批量订单开始更新 uid:" + uid + " 数量：" + tradeList.size());
        int size = tradeList.size();
        if (size > Constants.TRADE_SAVE_MAX_LIMIT) {
            List<List<TradeDTO>> splitList = JayCommonUtil.splitList(tradeList, Constants.TRADE_SAVE_MAX_LIMIT);
            for (List<TradeDTO> list : splitList) {
                this.batchInsetTradeListByLimit(uid, list);
            }
        } else {
            this.batchInsetTradeListByLimit(uid, tradeList);
        }
        this.logger.info("全部更新完成  uid:" + uid + " 数量：" + tradeList.size() + " 花费时间："+(System.currentTimeMillis() - startTime));
    }
    
    /**
     * 批量保存订单
     * @author: wy
     * @time: 2018年1月22日 下午4:31:51
     * @param uid 用户主键id
     * @param tradeList 要保持的订单集合
     */
    private void batchInsetTradeListByLimit(Long uid, List<TradeDTO> tradeList) {
        try {
            long startTime = System.currentTimeMillis();
            List<TradeDTO> newTradeList = new ArrayList<TradeDTO>(Constants.TRADE_SAVE_MAX_LIMIT/2);
            List<TradeDTO> updateTradeList = new ArrayList<TradeDTO>(Constants.TRADE_SAVE_MAX_LIMIT);
            for (TradeDTO tradeDTO : tradeList) {
                String status = this.findTradeStatusByTid(uid, tradeDTO.getTid());
                int compareTo = TradeStatusCompareUtil.compareTo(tradeDTO.getStatus(), status);
                if (ValidateUtil.isEmpty(status)) {
                    newTradeList.add(tradeDTO);
                } else if(compareTo>0){
                    updateTradeList.add(tradeDTO);
                }
            }
            long startTime2 = System.currentTimeMillis();
            this.logger.info("区分保存还是更新  :"+uid+" 的  "+tradeList.size()+"个订单，花费了  " + (startTime2 - startTime)+"ms");
            saveBatchTradeDTOByList(uid, newTradeList);
            long startTime3 = System.currentTimeMillis();
            this.logger.info("保存订单     :"+uid+" 的  "+newTradeList.size()+"个订单，花费了  " + (startTime3 - startTime2)+"ms");
            this.updateBatchTradeDTOByList(uid, updateTradeList);
            this.logger.info("更新订单  :"+uid+" 的  "+updateTradeList.size()+"个订单，花费了  " + (System.currentTimeMillis()-startTime3)+"ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 批量保存订单，如果批处理失败，则改为轮询  单个订单保存
     * @author: wy
     * @time: 2018年1月22日 下午4:51:15
     */
    private void saveBatchTradeDTOByList(long uid, List<TradeDTO> tradeList) throws InterruptedException {
        if(ValidateUtil.isEmpty(tradeList)){
            return ;
        }
        long startTime = System.currentTimeMillis();
        RedisLock redisLock = new RedisLock(redisTemplate, RedisConstant.RediskeyCacheGroup.TRADEDTO_TABLE_LOCKE_KEY + uid,Constants.TRADE_SAVE_TIME_OUT,Constants.TRADE_SAVE_EXPIRE_TIME);
        try {
            if (redisLock.lock()) {
                try {
                    this.saveTradeDTOList(uid, tradeList);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.logger.error("批量保存订单异常！！！改为轮询单个保存" + e.getMessage());
                    long startTime2 = System.currentTimeMillis();
                    for (TradeDTO tradeDTO : tradeList) {
                        this.saveTradeDTOBySingle(uid, tradeDTO);
                    }
                    this.logger.error("保存异常，轮询单个订单保存花费的时间为 ： "+( System.currentTimeMillis()-startTime2)+"ms");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } finally {
            this.logger.info("批量保存订单花费时间为：  " + (System.currentTimeMillis() - startTime)+"ms");
            redisLock.unlock();
        }
    }
    
    /**
     * 批量更新订单信息
     * @author: wy
     * @time: 2018年1月23日 下午12:16:28
     * @param uid
     * @param tradeList
     */
    private void updateBatchTradeDTOByList(long uid, List<TradeDTO> tradeList) {
        if(ValidateUtil.isEmpty(tradeList)){
            return ;
        }
        long startTime = System.currentTimeMillis();
        try {
            this.doUpdateTradeDTOByList(uid, tradeList);
        } catch (Exception e) {
            e.printStackTrace();
            this.logger.error("批量更新订单异常！！！改为轮询单个更新" + e.getMessage());
            long startTime2 = System.currentTimeMillis();
            for (TradeDTO tradeDTO : tradeList) {
                this.doUpdateTradeDTOBySingle(uid, tradeDTO);
            }
            this.logger.error("更新异常，轮询单个订单更新花费的时间为 ： "+( System.currentTimeMillis()-startTime2)+"ms");
        }finally {
            this.logger.info("批量更新订单花费时间为：  " + (System.currentTimeMillis() - startTime)+"ms");
        }
    }
    
    /**
     * 批量保存订单
     * @author: wy
     * @time: 2018年1月22日 下午4:45:33
     * @param uid
     * @param tradeDTOList
     * @throws Exception 
     */
    private void saveTradeDTOList(Long uid,List<TradeDTO> tradeDTOList) throws Exception{
        if(ValidateUtil.isEmpty(tradeDTOList)){
            return ;
        }
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("uid", uid);
        map.put("list", tradeDTOList);
        this.tradeDao.doCreateTradeDTOByList(map);
    }
    
    /**
     * 保存单个订单内容
     * @author: wy
     * @time: 2018年1月23日 上午11:26:24
     * @param uid 用户表主键id
     * @param tradeDTO
     */
    private void saveTradeDTOBySingle(Long uid,TradeDTO tradeDTO){
        if(tradeDTO==null){
            return ;
        }
        try {
            String status = this.findTradeStatusByTid(uid, tradeDTO.getTid());
            if (ValidateUtil.isEmpty(status)) {
                tradeDTO.setUid(uid);
                this.tradeDao.doCreateTradeDTOByBySingle(tradeDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.tradeErrorMessageService.saveErrorMessage(e.getMessage(), TradeErrorMessageService.IS_TRADE,
                    TradeErrorMessageService.SAVE, JSON.toJSONString(tradeDTO));
        }
    }
    /**
     * 批量更新订单
     * @author: wy
     * @time: 2018年1月22日 下午4:45:33
     * @param uid
     * @param tradeDTOList
     */
    private void doUpdateTradeDTOByList(Long uid,List<TradeDTO> tradeDTOList){
        if(ValidateUtil.isEmpty(tradeDTOList)){
            return ;
        }
       /* BaseListEntity<TradeDTO> entityList = new BaseListEntity<TradeDTO>();
        entityList.setUid(uid);
        entityList.setEntityList(tradeDTOList);*/
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("list", tradeDTOList);
//        this.tradeDao.doUpdateTradeDTOByList(uid,tradeDTOList);
        this.tradeDao.doUpdateTradeDTOByList_bak(map);
    }
    /**
     * 更新单个订单内容
     * @author: wy
     * @time: 2018年1月23日 上午11:26:24
     * @param uid 用户表主键id
     * @param tradeDTO
     */
    private void doUpdateTradeDTOBySingle(Long uid,TradeDTO tradeDTO){
        try {
            if(tradeDTO==null){
                return ;
            }
            tradeDTO.setUid(uid);
            this.tradeDao.doUpdateTradeDTOBySingle(tradeDTO);
        } catch (Exception e) {
            e.printStackTrace();
            this.tradeErrorMessageService.saveErrorMessage(e.getMessage(), TradeErrorMessageService.IS_TRADE, 
                    TradeErrorMessageService.UPDATE, JSON.toJSONString(tradeDTO));
        }
    }
}
