package com.kycrm.syn.service.mongo.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import com.alibaba.fastjson.JSON;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.syn.UserSynData;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.syn.service.mongo.dao.TradeRepository;
import com.kycrm.syn.service.mongo.entity.OldOrdersDTO;
import com.kycrm.syn.service.mongo.entity.OldTradeDTO;
import com.kycrm.syn.service.syn.MongoSynService;
import com.kycrm.syn.service.syn.UserSynDataService;
import com.kycrm.syn.thread.OrderSynThread;
import com.kycrm.syn.thread.TradeSynThread;
import com.kycrm.syn.util.MyFixedThreadPool;
import com.kycrm.util.Constants;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.RedisConstant.RedisCacheGroup;
import com.kycrm.util.RedisConstant.RediskeyCacheGroup;
import com.kycrm.util.ValidateUtil;

/** 
* @author wy
* @version 创建时间：2018年2月5日 下午3:38:47
*/
public class SynTradeToMySqlService {
    
    @Autowired
    private TradeRepository  tradeRepository;
    
    @Autowired
    private UserSynDataService  userSynDataService;
    
    @Autowired
    private CacheService cacheService;
    
    @Autowired
    private ITradeDTOService tradeDTOService;
    
    @Autowired
    private IOrderDTOService orderDTOService;
    
    private static final String SYN_USER_LOCK ="SYN_TRADEMONGO_LOCK_UID_";
    
    private Logger logger = LoggerFactory.getLogger(SynTradeToMySqlService.class);
    
    public void doHandle(long uid){
        int pageSize = Constants.TRADE_SAVE_MAX_LIMIT;
        while(true){
            Long lock = this.cacheService.setnx(SynTradeToMySqlService.SYN_USER_LOCK+uid, SYN_USER_LOCK, 50000L);
            try {
                if(lock==1){
                    if(isSynEnd(uid, pageSize)){
                        return;
                    }
                    Thread.sleep(500L);
                }else{
                    Thread.sleep(40000L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                this.cacheService.delete(SynTradeToMySqlService.SYN_USER_LOCK+uid);
            }
        }
        
    }
    /**
     * 查询是否需要同步，如果需要同步则保存，不需要同步则返回true
     * @author: wy
     * @time: 2018年2月8日 下午2:27:20
     * @param uid 用户主键id
     * @param pageSize 一次分页查询数量
     * @return true 同步结束  false，同步未结束，还需继续循环
     */
    private Boolean isSynEnd(long uid,int pageSize){
        UserSynData userSynData = this.cacheService.get(RedisCacheGroup.SYN_DATA_USER_CACHE, RediskeyCacheGroup.SYN_DATA_USER_CACHE_KEY+uid, UserSynData.class);
        if(userSynData==null){
            this.logger.info(uid+"：查询不到该用户的同步信息");
            return true;
        }
        if(userSynData.getIsTradeEnd()==null){
            userSynData.setIsTradeEnd(false);
        }
        if(userSynData.getIsTradeEnd()){
            this.logger.info(uid+"：用户的同步已经结束,不需要再次同步");
            return true;
        }
        Long sratNum = userSynData.getTradeStartNum();
        Query query  = new Query();
        query.skip(sratNum.intValue()).limit(pageSize);
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "_id")));
        List<OldTradeDTO> oldTradeDTOList = tradeRepository.find(query ,userSynData.getUserNick());
        if(ValidateUtil.isEmpty(oldTradeDTOList)){
            userSynData.setIsTradeEnd(true);
            this.saveDataToRedis(userSynData);
            return true;
        }else if(oldTradeDTOList.size()<pageSize){
            userSynData.setIsTradeEnd(true);
        }
        OldTradeDTO oldTradeDTO = oldTradeDTOList.get(0);
        if(oldTradeDTO.getModified()>MongoSynService.START_TIME_LONG){
            userSynData.setIsTradeEnd(true);
        }
        doSaveData(uid,oldTradeDTOList);
        userSynData.setTradeStartNum(userSynData.getTradeStartNum()+oldTradeDTOList.size());
        this.saveDataToRedis(userSynData);
        return false;
    }
    /**
     * 保存数据，转换实体
     * @author: wy
     * @time: 2018年2月8日 下午2:28:48
     * @param oldTradeDTOList
     */
    private void doSaveData(long uid,List<OldTradeDTO> oldTradeDTOList){
        List<TradeDTO> tradeDTOList = new ArrayList<>(oldTradeDTOList.size());
        List<OrderDTO> orderDTOList = new ArrayList<>(oldTradeDTOList.size()*2);
        for (OldTradeDTO oldTradeDTO : oldTradeDTOList) {
            TradeDTO tradeDTO = this.packageTradeDTO(oldTradeDTO);
            if(tradeDTO==null){
                continue;
            }
            tradeDTOList.add(tradeDTO);
            List<OldOrdersDTO> oldOrdersDTOList = oldTradeDTO.getOrders();
            if(ValidateUtil.isEmpty(oldOrdersDTOList)){
                continue;
            }
            for (OldOrdersDTO oldOrdersDTO : oldOrdersDTOList) {
                OrderDTO orderDTO = this.packageToOrderDTO(oldOrdersDTO, tradeDTO);
                if(orderDTO!=null){
                    orderDTOList.add(orderDTO);
                }
            }
        }
        /*CountDownLatch countDownLatch = new CountDownLatch(2);*/
        MyFixedThreadPool.getTradeAndOrderFixedThreadPool().execute(new TradeSynThread(/*countDownLatch,*/ tradeDTOService, uid, tradeDTOList));
        MyFixedThreadPool.getTradeAndOrderFixedThreadPool().execute(new OrderSynThread(/*countDownLatch,*/ orderDTOService, uid, orderDTOList));
       /* try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
    /**
     * 转换实体
     * @author: wy
     * @time: 2018年2月6日 下午12:06:05
     * @param oldTradeDto
     * @return
     */
    private TradeDTO packageTradeDTO(OldTradeDTO oldTradeDto){
        if(oldTradeDto==null){
            return null;
        }
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setTid(Long.parseLong(oldTradeDto.getTid()));
        tradeDTO.setSellerNick(oldTradeDto.getSellerNick());
        tradeDTO.setPayment(new BigDecimal(oldTradeDto.getPayment()));
        tradeDTO.setSellerRate(oldTradeDto.getSellerRate());
        if(oldTradeDto.getPostFee()!=null){
            tradeDTO.setPostFee(new BigDecimal(oldTradeDto.getPostFee()));
        }
        tradeDTO.setReceiverName(oldTradeDto.getReceiverName());
        tradeDTO.setReceiverState(oldTradeDto.getReceiverState());
        tradeDTO.setReceiverZip(oldTradeDto.getReceiverZip());
        tradeDTO.setReceiverMobile(oldTradeDto.getReceiverMobile());
        tradeDTO.setReceiverPhone(oldTradeDto.getReceiverPhone());
        if(oldTradeDto.getConsignTime()!=null){
            tradeDTO.setConsignTime(new Date(oldTradeDto.getConsignTime()));
        }else{
            tradeDTO.setConsignTime(oldTradeDto.getConsignTimeUTC());
        }
        if(oldTradeDto.getReceivedPayment()!=null){
            tradeDTO.setReceivedPayment(new BigDecimal(oldTradeDto.getReceivedPayment()));
        }
        tradeDTO.setReceiverCountry(oldTradeDto.getReceiverCountry());
        tradeDTO.setReceiverTown(oldTradeDto.getReceiverTown());
        tradeDTO.setShopPick(oldTradeDto.getShopPick());
        tradeDTO.setNum(oldTradeDto.getNum());
        tradeDTO.setStatus(oldTradeDto.getStatus());
        tradeDTO.setTitle(oldTradeDto.getTitle());
        tradeDTO.setType(oldTradeDto.getType());
        if(oldTradeDto.getDiscountFee()!=null){
            tradeDTO.setDiscountFee(new BigDecimal(oldTradeDto.getDiscountFee()));
        }
        tradeDTO.setHasPostFee(oldTradeDto.getHasPostFee());
        if(oldTradeDto.getTotalFee()!=null){
            tradeDTO.setTotalFee(new BigDecimal(oldTradeDto.getTotalFee()));
        }
        if(oldTradeDto.getCreated()!=null){
            tradeDTO.setCreated(new Date(oldTradeDto.getCreated()));
        }else{
            tradeDTO.setCreated(oldTradeDto.getCreatedUTC());
        }
        if(oldTradeDto.getPayTime()!=null){
            tradeDTO.setPayTime(new Date(oldTradeDto.getPayTime()));
        }else{
            tradeDTO.setPayTime(oldTradeDto.getPayTimeUTC());
        }
        if(oldTradeDto.getModified()!=null){
            tradeDTO.setModified(new Date(oldTradeDto.getModified()));
        }else{
            tradeDTO.setModified(oldTradeDto.getModifiedUTC());
        }
        if(oldTradeDto.getEndTime()!=null){
            tradeDTO.setEndTime(new Date(oldTradeDto.getEndTime()));
        }else{
            tradeDTO.setEndTime(oldTradeDto.getEndTimeUTC());
        }
        if(oldTradeDto.getBuyerFlag()!=null){
            tradeDTO.setBuyerFlag(oldTradeDto.getBuyerFlag().longValue());
        }
        if(oldTradeDto.getSellerFlag()!=null){
            tradeDTO.setSellerFlag(Long.parseLong(oldTradeDto.getSellerFlag()));
        }
        tradeDTO.setBuyerNick(oldTradeDto.getBuyerNick());
        tradeDTO.setStepTradeStatus(oldTradeDto.getStepTradeStatus());
        tradeDTO.setStepPaidFee(oldTradeDto.getStepPaidFee());
        tradeDTO.setShippingType(oldTradeDto.getShippingType());
        tradeDTO.setBuyerCodFee(oldTradeDto.getBuyerCodFee());
        if(oldTradeDto.getAdjustFee()!=null){
            tradeDTO.setAdjustFee(new BigDecimal(oldTradeDto.getAdjustFee()));
        }
        tradeDTO.setTradeFrom(oldTradeDto.getTradeFrom());
        tradeDTO.setBuyerRate(oldTradeDto.getBuyerRate());
        tradeDTO.setReceiverCity(oldTradeDto.getReceiverCity());
        tradeDTO.setReceiverDistrict(oldTradeDto.getReceiverDistrict());
        tradeDTO.setBuyerEmail(oldTradeDto.getBuyerEmail());
        tradeDTO.setBuyerAlipayNo(oldTradeDto.getBuyer_alipay_no());
        tradeDTO.setAlipayId(oldTradeDto.getAlipayId());
        tradeDTO.setAlipayNo(oldTradeDto.getAlipayNo());
        tradeDTO.setBuyerArea(oldTradeDto.getBuyerArea());
        tradeDTO.setBuyerObtainPointFee(oldTradeDto.getBuyerObtainPointFee());
        tradeDTO.setMsgId(oldTradeDto.getMsgId());
        if(oldTradeDto.getLastSendSmsTime()!=null){
            tradeDTO.setLastSendSmsTime(new Date(oldTradeDto.getLastSendSmsTime()));
        }
        tradeDTO.setLastModifiedDate(oldTradeDto.getLastModifiedDate());
        tradeDTO.setCreatedDate(oldTradeDto.getCreatedDate());
        return tradeDTO;
    }
    /**
     * 转换成新的子订单实体
     * @author: wy
     * @time: 2018年2月7日 下午12:10:52
     * @param oldOrderDTO
     * @param oldTradeDTO
     * @return
     */
    private OrderDTO packageToOrderDTO(OldOrdersDTO oldOrderDTO,TradeDTO tradeDTO){
        if(oldOrderDTO==null){
            return null;
        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOid(Long.parseLong(oldOrderDTO.getOid()));
        orderDTO.setTid(Long.parseLong(oldOrderDTO.getTid()));
        orderDTO.setCid(oldOrderDTO.getCid());
        orderDTO.setNumIid(oldOrderDTO.getNumIid());
        orderDTO.setItemMealId(oldOrderDTO.getItemMealId());
        orderDTO.setSkuId(oldOrderDTO.getSkuId());
        orderDTO.setRefundId(oldOrderDTO.getRefundId());
        orderDTO.setBindOid(oldOrderDTO.getBindOid());
        orderDTO.setItemMealName(oldOrderDTO.getItemMealName());
        orderDTO.setPicPath(oldOrderDTO.getPicPath());
        orderDTO.setSellerNick(oldOrderDTO.getSellerNick());
        orderDTO.setBuyerNick(oldOrderDTO.getBuyerNick());
        orderDTO.setRefundStatus(oldOrderDTO.getRefundStatus());
        orderDTO.setOuterIid(oldOrderDTO.getOuteriid());
        orderDTO.setSnapshotUrl(oldOrderDTO.getSnapshotUrl());
        orderDTO.setSnapshot(oldOrderDTO.getSnapshot());
        if(null != oldOrderDTO.getTimeoutActionTime()){
            orderDTO.setTimeoutActionTime(new Date(oldOrderDTO.getTimeoutActionTime()));
        }else{
            orderDTO.setTimeoutActionTime(oldOrderDTO.getTimeoutActionTimeUTC());
        }
        orderDTO.setBuyerRate(oldOrderDTO.getBuyerRate());
        orderDTO.setSellerRate(oldOrderDTO.getSellerRate());
        orderDTO.setSellerType(oldOrderDTO.getSellerType());
        orderDTO.setSubOrderTaxFee(oldOrderDTO.getSubOrderTaxFee());
        orderDTO.setSubOrderTaxRate(oldOrderDTO.getSubOrderTaxRate());
        orderDTO.setStatus(oldOrderDTO.getStatus());
        orderDTO.setTitle(oldOrderDTO.getTitle());
        orderDTO.setType(oldOrderDTO.getType());
        orderDTO.setIid(oldOrderDTO.getIid());
        if(null != oldOrderDTO.getPrice()){
            orderDTO.setPrice(new BigDecimal(oldOrderDTO.getPrice()));
        }else if(null != oldOrderDTO.getPriceMoney()){
            orderDTO.setPrice(new BigDecimal(oldOrderDTO.getPriceMoney()));
        }
        orderDTO.setNum(oldOrderDTO.getNum());
        orderDTO.setOuterSkuId(oldOrderDTO.getOuterSkuId());
        orderDTO.setOrderFrom(oldOrderDTO.getOrderFrom());
        if(null!=oldOrderDTO.getTotalFee()){
            orderDTO.setTotalFee(new BigDecimal(oldOrderDTO.getTotalFee()));
        }else if(null != oldOrderDTO.getTotalFeeMoney()){
            orderDTO.setTotalFee(new BigDecimal(oldOrderDTO.getTotalFeeMoney()));
        }
        if(null!=oldOrderDTO.getTotalFee()){
            orderDTO.setPayment(new BigDecimal(oldOrderDTO.getTotalFee()));
        }else if(null != oldOrderDTO.getTotalFeeMoney()){
            orderDTO.setTotalFee(new BigDecimal(oldOrderDTO.getTotalFeeMoney()));
        }
        if(null != oldOrderDTO.getDiscountFee()){
            orderDTO.setDiscountFee(new BigDecimal(oldOrderDTO.getDiscountFee()));
        }
        if(null!=oldOrderDTO.getAdjustFee()){
            orderDTO.setAdjustFee(new BigDecimal(oldOrderDTO.getAdjustFee()));
        }
        if(null != oldOrderDTO.getModified()){
            orderDTO.setModified(new Date(oldOrderDTO.getModified()));
        }else{
            orderDTO.setModified(oldOrderDTO.getModifiedUTC());
        }
        orderDTO.setSkuPropertiesName(oldOrderDTO.getSkuPropertiesName());
        orderDTO.setIsOversold(oldOrderDTO.getIsOversold());
        orderDTO.setIsServiceOrder(oldOrderDTO.getIsServiceOrder());
        if(null!=oldOrderDTO.getEndTime()){
            orderDTO.setEndTime(new Date(oldOrderDTO.getEndTime()));
        }else{
            orderDTO.setEndTime(oldOrderDTO.getEndTimeUTC());
        }
        orderDTO.setConsignTime(oldOrderDTO.getConsignTime());
        orderDTO.setOrderAttr(oldOrderDTO.getOrderAttr());
        orderDTO.setShippingType(oldOrderDTO.getShippingType());
        orderDTO.setLogisticsCompany(oldOrderDTO.getLogisticsCompany());
        orderDTO.setInvoiceNo(oldOrderDTO.getInvoiceNo());
        orderDTO.setIsDaixiao(oldOrderDTO.getIsdaixiao());
        if(null!=oldOrderDTO.getDivideOrderFee()){
            orderDTO.setDivideOrderFee(new BigDecimal(oldOrderDTO.getDivideOrderFee()));
        }
        orderDTO.setPartMjzDiscount(oldOrderDTO.getPartMjzDiscount());
        orderDTO.setTicketOuterId(oldOrderDTO.getTicketOuterId());
        orderDTO.setTicketExpDateKey(oldOrderDTO.getTicketExpdateKey());
        orderDTO.setStoreCode(oldOrderDTO.getStoreCode());
        orderDTO.setIsWww(oldOrderDTO.getIsWww());
        orderDTO.setTmserSpuCode(oldOrderDTO.getTmserSpuCode());
        orderDTO.setBindOids(oldOrderDTO.getBindOids());
        orderDTO.setZhengjiStatus(oldOrderDTO.getZhengjiStatus());
        orderDTO.setMdQualification(oldOrderDTO.getMdQualification());
        if(null!=oldOrderDTO.getMdFee()){
            orderDTO.setMdFee(new BigDecimal(oldOrderDTO.getMdFee()));
        }
        orderDTO.setCustomization(oldOrderDTO.getCustomization());
        orderDTO.setInvType(oldOrderDTO.getInvType());
        orderDTO.setIsShShip(oldOrderDTO.getIsShShip());
        orderDTO.setShipper(oldOrderDTO.getShipper());
        orderDTO.setfType(oldOrderDTO.getfType());
        orderDTO.setfStatus(oldOrderDTO.getfStatus());
        orderDTO.setfTerm(oldOrderDTO.getFTERM());
        orderDTO.setComboId(oldOrderDTO.getComboId());
        orderDTO.setAssemblyRela(oldOrderDTO.getAssemblyRela());
        orderDTO.setAssemblyPrice(oldOrderDTO.getAssemblyPrice());
        orderDTO.setAssemblyItem(oldOrderDTO.getAssemblyItem());
        orderDTO.setReceiverDistrict(oldOrderDTO.getReceiverDistrict());
        orderDTO.setReceiverCity(oldOrderDTO.getReceiverCity());
        orderDTO.setStepTradeStatus(oldOrderDTO.getStepTradeStatus());
        orderDTO.setReceiverName(oldOrderDTO.getReceiverName());
        orderDTO.setReceiverMobile(oldOrderDTO.getReceiverMobile());
        if(null != oldOrderDTO.getBuyerFlag()){
            orderDTO.setBuyerFlag(oldOrderDTO.getBuyerFlag().longValue());
        }
        if(null != oldOrderDTO.getSellerFlag()){
            orderDTO.setSellerFlag(oldOrderDTO.getSellerFlag().longValue());
        }
        if(tradeDTO==null){
            return orderDTO;
        }
        orderDTO.setSellerNick(tradeDTO.getSellerNick());
        orderDTO.setBuyerNick(tradeDTO.getBuyerNick());
        orderDTO.setTradePayment(tradeDTO.getPayment());
        orderDTO.setTradeSellerRate(tradeDTO.getSellerRate());
        orderDTO.setReceiverName(tradeDTO.getReceiverName());
        orderDTO.setReceiverState(tradeDTO.getReceiverState());
        orderDTO.setReceiverAddress(tradeDTO.getReceiverAddress());
        orderDTO.setReceiverZip(tradeDTO.getReceiverZip());
        orderDTO.setReceiverMobile(tradeDTO.getReceiverMobile());
        orderDTO.setReceiverPhone(tradeDTO.getReceiverPhone());
        orderDTO.setTradeConsignTime(tradeDTO.getConsignTime());
        orderDTO.setTradePayment(tradeDTO.getReceivedPayment());
        orderDTO.setReceiverCountry(tradeDTO.getReceiverCountry());
        orderDTO.setReceiverTown(tradeDTO.getReceiverTown());
        orderDTO.setShopPick(tradeDTO.getShopPick());
        orderDTO.setTid(tradeDTO.getTid());
        orderDTO.setTradeNum(tradeDTO.getNum());
        orderDTO.setTradeStatus(tradeDTO.getStatus());
        orderDTO.setTradeTitle(tradeDTO.getTitle());
        orderDTO.setTradeType(tradeDTO.getType());
        orderDTO.setTradeTotalFee(tradeDTO.getTotalFee());
        orderDTO.setTradeCreated(tradeDTO.getCreated());
        orderDTO.setTradePayTime(tradeDTO.getPayTime());
        orderDTO.setTradeModified(tradeDTO.getModified());
        orderDTO.setTradeEndTime(tradeDTO.getEndTime());
        orderDTO.setBuyerFlag(tradeDTO.getBuyerFlag());
        orderDTO.setSellerFlag(tradeDTO.getSellerFlag());
        orderDTO.setStepTradeStatus(tradeDTO.getStepTradeStatus());
        if(null != tradeDTO.getStepPaidFee()){
            orderDTO.setStepPaidFee(new BigDecimal(tradeDTO.getStepPaidFee()));
        }
        orderDTO.setTradeShippingType(tradeDTO.getShippingType());
        if(null != tradeDTO.getBuyerCodFee()){
            orderDTO.setBuyerCodFee(new BigDecimal(tradeDTO.getBuyerCodFee()));
        }
        orderDTO.setTradeAdjustFee(tradeDTO.getAdjustFee());
        orderDTO.setTradeFrom(tradeDTO.getTradeFrom());
        orderDTO.setTradeBuyerRate(tradeDTO.getBuyerRate());
        orderDTO.setReceiverCity(tradeDTO.getReceiverCity());
        orderDTO.setReceiverDistrict(tradeDTO.getReceiverDistrict());
        return orderDTO;
    }
    
    /**
     * 保存数据到redis
     * @author: wy
     * @time: 2018年2月8日 上午11:51:34
     * @param userSynData
     */
    private void saveDataToRedis(UserSynData userSynData){
        this.cacheService.put(RedisConstant.RedisCacheGroup.SYN_DATA_USER_CACHE, 
                RedisConstant.RediskeyCacheGroup.SYN_DATA_USER_CACHE_KEY+userSynData.getUid(), 
                JSON.toJSONString(userSynData));
        this.userSynDataService.doUpdateTradeStatus(userSynData);
    }
}
