package com.kycrm.member.service.traderate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.traderate.ITradeRatesDao;
import com.kycrm.member.domain.entity.other.TaskNode;
import com.kycrm.member.domain.entity.traderate.TradeRates;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.other.ITaskNodeService;
import com.kycrm.member.service.tradecenter.ITradeSetupService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.NumberUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.domain.TradeRate;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.TraderatesGetRequest;
import com.taobao.api.response.TraderatesGetResponse;
@MyDataSource(MyDataSourceAspect.MASTER)
@Service("tradeRatesService")
public class TradeRatesServiceImpl implements ITradeRatesService {
	
	private Logger logger = LoggerFactory.getLogger(TradeRatesServiceImpl.class);

	@Autowired
	private ITradeRatesDao tradeRatesDao;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	@Autowired
	private ITradeSetupService tradeSetupService;
	
	@Autowired
	private ITaskNodeService taskNodeService;
	
	/**
	 * 保存一条记录
	 */
	@Override
	public void saveTradeRates(TradeRates tradeRates) {
		tradeRatesDao.saveTradeRates(tradeRates);
	}

	/**
	 * 保存多条记录
	 */
	@Override
	public void saveTradeRatesList(List<TradeRates> tradeRates) {
		Map<String, Object> map = new HashMap<>();
		map.put("tradeRatesList", tradeRates);
		tradeRatesDao.saveTradeRatesList(map);
	}
	
	/**
	 * 根据条件查询评价订单数
	 */
	@Override
	public long countTradeByParam(Long uid, Date beginTime,
			Date endTime, List<String> resultList) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("role", "buyer");
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		map.put("resultList", resultList);
		Long tradeNum = tradeRatesDao.countTradeByParam(map);
		return NumberUtils.getResult(tradeNum);
	}
	
	/**
	 * 定时同步评价数据到CRM数据库
	 * ZTK2017年3月20日下午5:28:50
	 */
	public void saveTradeRate(){
		try {
			int i = 0;
			// 调用taobao接口
			DefaultTaobaoClient client = new DefaultTaobaoClient(Constants.TAOBAO_URL, Constants.TOP_APP_KEY, Constants.TOP_APP_SECRET);
			TraderatesGetRequest neutralReq = new TraderatesGetRequest();
			TraderatesGetRequest badReq = new TraderatesGetRequest();
			Date date1 = new Date();
			// 查询userInfo表里的所有的token
			List<UserInfo> userInfoList = userInfoService.listAllUser();
			for (UserInfo userInfo : userInfoList) {
				String userId = userInfo.getTaobaoUserNick();
	//			if(!"哈数据库等哈".equals(userId)){
	//				continue;
	//			}
				// 全部的token
				Long uid = userInfo.getId();
				logger.debug("=====================评价同步：：取出userInfoId:" + uid);
				//查询此用户中差评监控是否开启。未开启，则不获得评价信息。
				boolean isOpen = tradeSetupService.queryStatusIsOpen(uid);
				if(isOpen){
				}else{
					continue;
				}
				String accessToken = userInfo.getAccessToken();
				if(accessToken == null || "".equals(accessToken)){
					continue;
				}
				TraderatesGetResponse neutralRsp = installReq(client, neutralReq, accessToken, "neutral");
				i ++;
				saveTradeRates(neutralRsp,userId);
				//拉取差评信息，使用该accessToken查询用户表是否过期，如果过期就通过userInfoId查询信的accessToken;
				UserInfo userIn = userInfoService.findUserBytoken(accessToken);
				if(userIn != null ){
					logger.debug("===================================================评价同步：：：：accessToken未过期");
				}else{
					accessToken = userInfoService.findUserTokenById(uid);
					logger.debug("=================评价同步：：：：accessToken过期啦！！取出新的accessToken:"+accessToken);
				}
				TraderatesGetResponse badRsp = installReq(client, badReq, accessToken, "bad");
				i ++;
				saveTradeRates(badRsp,userId);
			}
			Date date2 = new Date();
			TaskNode taskNodeParam = new TaskNode();
			taskNodeParam.setTaskEndTime(date2);
			taskNodeParam.setType("tradeRates");
			taskNodeParam.setTaskNode(i);
			logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!调用接口次数：" + i);
			TaskNode taskNode = taskNodeService.queryTaskNodeByType("tradeRates");
			if(taskNode != null){
				taskNodeService.updateTaskNode(taskNodeParam);
			}else {
				taskNodeService.saveTaskNode(taskNodeParam);
			}
			logger.debug("保存最后一次获取时间:" + date2);
			logger.debug("<<<<<<<<<<========<<<<<<<<<获取本次时间段评价完成,耗时:"+ (date2.getTime() - date1.getTime()) + ">>>>>>>>>>==========>>>>>>>>>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拉取评价信息设置拉取条件
	 * ZTK2017年8月22日上午11:56:53
	 */
	public TraderatesGetResponse installReq(DefaultTaobaoClient client,TraderatesGetRequest req,
			String accessToken,String result){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String taskNodeType = "tradeRates";//评价
		TaskNode taskNode = taskNodeService.queryTaskNodeByType("tradeRates");
		// 设置调用接口查询条件
		req.setFields("tid,oid,role,nick,result,created,rated_nick,item_title,item_price,content,reply,num_iid");
		req.setRateType("get");
		req.setRole("buyer");
		req.setPageSize(150l);
		req.setResult(result);
		// 根据评价时间查询，第一次查询需要注释掉，开始时间为定时任务开启的时间的24小时之前，结束时间为定时任务开启时间
		req.setEndDate(new Date());
		if(taskNode != null && taskNode.getTaskEndTime() != null){
			req.setStartDate(taskNode.getTaskEndTime());
		}else{
			req.setStartDate(StringUtils.parseDateTime(DateUtils.getTimeByHour(format.format(new Date()), -2)));
		}
		TraderatesGetResponse rsp = null;
		try {
			rsp = client.execute(req, accessToken);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return rsp;
	}
	
	/**
	 * 保存拉取的淘宝评价信息
	 * ZTK2017年8月22日下午12:03:42
	 */
	public void saveTradeRates(TraderatesGetResponse rsp,String userId){
		if(rsp != null && rsp.getTradeRates() != null && rsp.getTradeRates().size() > 0){
			logger.debug("~~~~~~~~~~~~~~" + "" + "搜索到的评价总条数数：" + rsp.getTotalResults());
			List<TradeRate> tradeRatesList = rsp.getTradeRates();
			// 循环获得的评价信息，填充到tradeRates中
			for (TradeRate tradeRate : tradeRatesList) {
				//更新会员是否为中差评
				/*boolean isSuccess = vipMemberService.updateMemberCommentStatus(tradeRate.getNick(), userId);
				if(isSuccess){
					logger.info("-------取出评价数据--更新会员中差评状态成功-------");
				}else{
					logger.info("-------取出评价数据--更新会员中差评状态失败-------");
				}*/
				logger.info("-------取出评价数据--评价类型是:"+tradeRate.getResult()+"------保存到crm数据库-------");
				logger.info("-------取出评价数据--list的数量:"+tradeRatesList.size()+"------tid："+tradeRate.getTid()+"-oid:"+tradeRate.getOid()+"--------");
			}
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("tradeRateList", tradeRatesList);
				map.put("createdDate", tradeRatesList.get(0).getCreated());
				map.put("createdBy", userId);
				map.put("lastModifiedDate", new Date());
				map.put("lastModifiedBy", userId);
				/*map.put("tid", tradeRate.getTid());
				map.put("oid", tradeRate.getOid());
				map.put("role", tradeRate.getRole());
				map.put("nick", tradeRate.getNick());
				map.put("result", tradeRate.getResult());
				map.put("created", tradeRate.getCreated());
				map.put("ratedNick", tradeRate.getRatedNick());
				map.put("itemTitle", tradeRate.getItemTitle());
				map.put("itemPrice", tradeRate.getItemPrice());
				map.put("content", tradeRate.getContent());
				map.put("reply", tradeRate.getReply());
				map.put("numIid", tradeRate.getNumIid());
				map.put("validScore", tradeRate.getValidScore());
				map.put("createdDate", tradeRate.getCreated());*/
				// 保存中评TradeRates实体到CRM数据库
				tradeRatesDao.saveTBTradeRateList(map);
				
			try {
				/*rateMonitoringPacifyService.doHandle(userId, tradeRatesList);*/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}



}
