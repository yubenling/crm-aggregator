package com.kycrm.member.service.tradecenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.tradecenter.ITradeSetupDao;
import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;
import com.kycrm.member.domain.vo.tradecenter.OrderReminderEffectVo;
import com.kycrm.member.domain.vo.tradecenter.TradeSetupVO;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.user.UserTmcListernService;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.ValidateUtil;
import com.kycrm.util.thread.MyFixedThreadPool;

@Service("tradeSetupService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class TradeSetupServiceImpl implements ITradeSetupService{

	private static final Log logger = LogFactory.getLog(TradeSetupServiceImpl.class);
	
	@Autowired
	private ITradeSetupDao tradeSetupDao;

	@Autowired
	private UserTmcListernService userTmcListernService;
	
	@Autowired
	private ICacheService cacheService;
	
	 /**
	 * 数据集合
	 * TradeSetupService实例化时创建
	 * 订单发送范围校验
	 * */
    private final List<String> BLOCK_LIST = new ArrayList<String>(){   
		private static final long serialVersionUID = -237244155080108106L;
		{  
			//"付款关怀", "发货提醒"
			add(OrderSettingInfo.PAYMENT_CINCERN);add(OrderSettingInfo.SHIPMENT_TO_REMIND);
			// "延时发货提醒", "到达同城提醒"
			add(OrderSettingInfo.DELAY_SEND_REMIND); add(OrderSettingInfo.ARRIVAL_LOCAL_REMIND);
			//"派件提醒", "签收提醒"
			add(OrderSettingInfo.SEND_GOODS_REMIND);add( OrderSettingInfo.REMIND_SIGNFOR);
			//"宝贝关怀", "回款提醒"
			add(OrderSettingInfo.COWRY_CARE);add(OrderSettingInfo.RETURNED_PAYEMNT); 
			//"买家申请退款", "同意退款"
			add(OrderSettingInfo.REFUND_CREATED);add(OrderSettingInfo.REFUND_AGREE);
			//"拒绝退款", "退款成功"
			add(OrderSettingInfo.REFUND_REFUSE); add(OrderSettingInfo.REFUND_SUCCESS);
			//"提醒好评"
			add(OrderSettingInfo.GOOD_VALUTION_REMIND);
        }  
    };

	/**
	 * 查询开启的类型   动态监听tmc
	 * @author: wy
	 * @time: 2017年9月20日 下午3:25:49
	 * @param sellerNick 卖家昵称
	 * @return 开启的设置类型集合
	 */
	public List<String> findTypeBySellerNickTmc(Long uid){
		if(ValidateUtil.isEmpty(uid)){
			return null;
		}
		List<String> list = tradeSetupDao.findTypeOpenBySellerNick(uid);
		return list;
	}
	
	/** 
	* @Description 展示各种订单中心的设置是否开启或者关闭<br/>
	* key:type(订单中心各种设置类型)<br/>
	* value:status(true:有开启;false:未设置或者未开启)
	* @param  uid
	* @param  set
	* @return Map<String,Boolean>    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 上午11:52:41
	*/
	public Map<String, Boolean> showSetupMenu(Long uid, Set<String> set) {
		Map<String, Object> hashMap = new HashMap<String,Object>();
		hashMap.put("uid", uid);
		//此数参数写死,值参数status为true的
		hashMap.put("status", true);
		//只返回设置为true的实体,通过类型分组
		List<TradeSetup> setups = tradeSetupDao.querySetupsByStatus(hashMap);
		Map<String, Boolean>  resultMap = null;
		if(setups!=null && !setups.isEmpty()){
			resultMap = new HashMap<String, Boolean>();
			for (TradeSetup tradeSetup : setups) {
				resultMap.put(tradeSetup.getType(), tradeSetup.getStatus());
			}
		}
		if(resultMap!=null && !resultMap.isEmpty())
			set.removeAll(resultMap.keySet());
		if(set.size()>0){
			if(resultMap ==null || resultMap.isEmpty()) resultMap = new HashMap<String, Boolean>();
			for (String str : set) {
				resultMap.put(str, false);
			}
		}
		return resultMap;
	}
	

	/**
	 * 保存之前:
	 * 查询当前改用设置的最大任务级别:a
	 * 对当前任务设置任务级别:a+1
	 * */
	public Integer queryMaxTaskLevelByType(TradeSetup tradeSetup){
		return tradeSetupDao.queryMaxTaskLevelByType(tradeSetup);
	}
	
	/** 
	* @Description 保存订单中心设置
	* @param  setup
	* @return TradeSetup    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午5:59:19
	*/
	@Transactional
	public TradeSetup saveTradeSetup(TradeSetup setup) {
		//添加功能:订单发送短信范围
		//保存数据(insert)时判断 判断是否开始  添加一个开启时间
		if(BLOCK_LIST.contains(setup.getType()) && setup.getTradeBlock().equals(true))
			setup.setChosenTime(new Date());
		tradeSetupDao.saveTradeSetup(setup);
		if(setup.getId()==null){
			logger.info("保存订单中心设置,无法返回主键  :"+setup.getUserId());
			throw new RuntimeException("tradeCenter:can not return primary key after saving a tradeSetup !");
		}
		//设置时开启时,放入redis
		if(setup.getStatus()) putDataToRedis(setup);
		addUserPermitByMySql(setup.getUid(), setup.getUserId());
		return setup;
	}

	/** 
	* @Description 根据类型查询用户该设置的条数
	* @param uid
	* @param type
	* @return long    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午4:49:31
	*/
	public long queryTradeSetupCount(Long uid,String type,Boolean status){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("uid", uid);
		map.put("type", type);
		map.put("status",status);
		return tradeSetupDao.queryTradeSetupCount(map);
	}

	/** 
	* @Description 根据设置的任务名称查询该类型的设置是否存在 
	* @param  sourceVO
	* @return TradeSetup    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午5:18:02
	*/
	public TradeSetup queryTradeSetupByTaskName(TradeSetupVO vo) {
		return tradeSetupDao.queryTradeSetupByTaskName(vo);
	}

	/**
	* @Title: queryTradeSetupTable
	* @Description: (筛选多任务订单中心相关设置集合)
	* @return List<TradeSetup>   返回类型
	* @author:jackstraw_yu
	*/
	public List<TradeSetup> queryTradeSetupTable(TradeSetupVO setup) {
		return tradeSetupDao.queryTradeSetupTable(setup);
	}

	/**
	* @Title: querySingleTradeSetup
	* @Description: (查询单条订单中心相关设置)
	* @return List<TradeSetup>   返回类型
	* @author:jackstraw_yu
	*/
	public TradeSetup querySingleTradeSetup(TradeSetupVO setup) {
		return tradeSetupDao.querySingleTradeSetup(setup);
	}
	
	/**
	 * 订单中心根据条件筛选任务名称列表
	 * ztk2017年9月25日下午4:08:36
	 */
	public List<TradeSetup> queryTradeSetupTaskNames(TradeCenterEffectVO tradeCenterEffectVO){
		return tradeSetupDao.queryTradeSetupTaskNames(tradeCenterEffectVO);
	}
	
	/**
	 * 订单中心根据任务名称查询任务Id
	 * ztk2017年10月30日下午11:11:26
	 */
	public Long queryTradeSetupId(OrderReminderEffectVo orderEffectVo){
		return tradeSetupDao.queryTradeSetupId(orderEffectVo);
	}
	
	/**
	* @Title: showSingleTradeSetup
	* @Description: (筛选单个订单中心相关设置;用于数据回填)
	* @return TradeSetup   返回类型
	* @author:jackstraw_yu
	*/
	public TradeSetup showSingleTradeSetup(TradeSetupVO setVo) {
		return tradeSetupDao.querySingleTradeSetup(setVo);
	}
	
	/**
	* @Title: deleteTradeSetup
	* @Description: (删除订单中心相关设置)
	* @return void   返回类型
	* @author:jackstraw_yu
	*/
	@Transactional
	public void deleteTradeSetup(TradeSetupVO setup){
		 tradeSetupDao.deleteTradeSetup(setup);
		/*if(delete!=1){
			throw new Exception("tradeCenter:delete tradeSetup more than one result!");
		}*/
		//删除该设置时,从redis取出
		removeDataFromRedis(setup.getUid(),setup.getType(),setup.getId());
		addUserPermitByMySql(setup.getUid(), setup.getUserId());
	}


	/**
	* @Title: switchTradeSetup
	* @Description: (打开或者关闭订单中心相关设置)
	* @return void   返回类型
	* @author:jackstraw_yu
	*/
	@Transactional
	public void switchTradeSetup(TradeSetupVO setupVO) {
		tradeSetupDao.switchTradeSetup(setupVO);
		//打开设置,放入redis/关闭设置,重redis取出
		if(setupVO.getStatus()){//打开
			TradeSetup setup = tradeSetupDao.querySingleTradeSetup(setupVO);
			putDataToRedis(setup);
		}else{//关闭
			removeDataFromRedis(setupVO.getUid(),setupVO.getType(),setupVO.getId());
		}
		addUserPermitByMySql(setupVO.getUid(), setupVO.getUserId());
	}


	/** 
	* @Description 更新订单中心设置
	* @param  setup    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 下午1:47:53
	*/
	@Transactional
	public void updateTradeSetup(TradeSetup setup){
		TradeSetup query = null;
		//更新之前判断需不需要对**的选中时间进行赋值
		if(BLOCK_LIST.contains(setup.getType()) && setup.getTradeBlock().equals(true)){
			//更新数据为真时先查询出就数据的状态是否为真
			//为真时对chonsenTime赋值为null,由sql判空更新;为假时,重新覆盖chonsenTime
			query = tradeSetupDao.queryTradeSetupById(setup.getId());
			if(query!=null){
				if(query.getTradeBlock() != null && !query.getTradeBlock()){
					setup.setChosenTime(new Date());
				}else{
					setup.setChosenTime(null);
				}
			}else{
				throw new RuntimeException("tradeCenter:updateTradeSetup Exception!");
			}
		}
		int excute = tradeSetupDao.updateTradeSetup(setup);
		if(excute != 1){
			logger.info("影响的行数不为1 :"+setup.getUserId());
			throw new RuntimeException("影响的行数不为1! :"+setup.getUserId());	
		}
		//修改设置后判断是否关闭,开启:重新覆盖redis中设置,关闭:从redis取出
		//打开
		query = tradeSetupDao.queryTradeSetupById(setup.getId());
		if(query!=null){
			if(query.getStatus()){
				putDataToRedis(query);
			}else{//关闭
				removeDataFromRedis(query.getUid(),query.getType(),query.getId());
			}
		}else{
			throw new RuntimeException("tradeCenter:updateTradeSetup Exception!");
		}
		addUserPermitByMySql(setup.getUid(), setup.getUserId());
	}


	/**
	* @Title: resetTradeSetupLevel
	* @Description: (修改订单中心设置的任务级别)
	* @return void   返回类型
	* @author:jackstraw_yu
	*/
	@Transactional
	public void resetTradeSetupLevel(TradeSetupVO setupVO) {
		tradeSetupDao.resetTradeSetupLevel(setupVO);
		/**
		 * 修改任务级别需要判断该任务是否是开启或者关闭
		 * 开启或者关闭的参数必须有web传入
		 * */
		TradeSetup tradeSetup = tradeSetupDao.querySingleTradeSetup(setupVO);
		if(tradeSetup!=null && tradeSetup.getStatus())
			putDataToRedis(tradeSetup);
		addUserPermitByMySql(setupVO.getUid(), setupVO.getUserId());
	}

	
	/** 
	* @Description 将设置放入redis
	* @param  tradeSetup    设定文件 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午6:14:10
	*/
	private void putDataToRedis(TradeSetup tradeSetup){
		cacheService.putNoTime(OrderSettingInfo.TRADE_SETUP+tradeSetup.getUid()+"_"+tradeSetup.getType(),  
							   tradeSetup.getId().toString(),tradeSetup,true);
	}
	
	/** 
	* @Description 将设置移出redis
	* @param  tradeSetup    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午6:15:01
	*/
	private void removeDataFromRedis(Long uid,String type,Long id){
		cacheService.remove(OrderSettingInfo.TRADE_SETUP+uid+"_"+type,id.toString(),true);
	}
		
	/**
	 * 查询taskName对应的id
	 * ztk2017年11月3日上午10:06:30
	 */
	public Long queryIdByTaskName(String userId,String type,String taskName){
		Map<String,Object> dataMap = new HashMap<String, Object>();
		if(userId == null || type == null || taskName == null){
			return null;
		}
		dataMap.put("userId", userId);
		dataMap.put("type", type);
		dataMap.put("taskName", taskName);
		Long taskId = tradeSetupDao.queryIdByTaskName(dataMap);
		return taskId;
	}	
		
	
	/**
	* @Title: addUserPermitByMySql
	* @return void   返回类型
	* @author:jackstraw_yu
	*/
	@Override
	public void addUserPermitByMySql(final Long uid,final String userName){
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
			@Override
			public void run() {
				try {
					//休息5秒，再授权
					Thread.sleep(5*1000);
					userTmcListernService.addUserPermitByMySql(uid,userName,null);
				} catch (Exception e) {
					logger.error("##################### addUserPermitByMySql() Exception:"+e.getMessage());
				}
			}
		});
	}

	/**
	 * 查询订单中心所有正在使用的taskId
	 */
	@Override
	public List<Long> queryTaskIdIsUse(Long uid, Boolean inUse) {
		TradeSetup tradeSetup = new TradeSetup();
		tradeSetup.setUid(uid);
		tradeSetup.setInUse(inUse);
		List<Long> taskIdList = tradeSetupDao.queryTaskIdInUse(tradeSetup);
		return taskIdList;
	}

	/**
	 * 查询用户是否开启中差评监控
	 */
	@Override
	public boolean queryStatusIsOpen(Long uid) {
		Boolean isOpen = tradeSetupDao.queryStatusIsOpen(uid);
		if(isOpen != null){
			return isOpen;
		}
		return false;
	}
    /**
     * 查询所有订单中心的设置
     */
	@Override
	public List<TradeSetup> listAllTradeStep() {
		return this.tradeSetupDao.listAllTradeStep();
	}
}