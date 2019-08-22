package com.kycrm.member.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.core.queue.LogAccessQueueService;
import com.kycrm.member.domain.entity.eco.log.LogAccessDTO;
import com.kycrm.member.domain.entity.eco.log.LogType;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.order.OrderVO;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.thread.MyFixedThreadPool;

@Controller
@RequestMapping(value = "/backstage")
public class OrderDTOController extends BaseController{

	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private IOrderDTOService orderDTOService;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	/**
	 * 店铺数据-订单列表
	 * @author HL
	 * @time 2018年5月22日 下午6:08:46 
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/findOrderDTO",produces="text/html;charset=UTF-8")
	public String findOrderDTO(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		OrderVO vo = new OrderVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, OrderVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		List<String> status = vo.getTradeStatus();
		if(status !=null && status.size()>0){
			List<String> newStatus = new ArrayList<String>();
			for (String s : status) {
				String[] split = s.split(",");
				for (String str : split) {
					if(str != null && !"".equals(str)){
						newStatus.add(str);
					}
				}
			}
			vo.setTradeStatus(newStatus);
		}
		
		vo.setUid(user.getId());
		String accessToken = userInfoService.findUserTokenById(user.getId());
		Map<String, Object> map = orderDTOService.findOrderDTOPage(user.getId(),vo,accessToken);
		map.put("pageNo", vo.getPageNo());
		this.logImport(request,map,user);
		
		return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}

	/**
	 * 日志上传
	 * @time 2018年10月10日 下午7:40:15
	 */
	private void logImport(HttpServletRequest request, Map<String, Object> map, UserInfo user) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				try {
					@SuppressWarnings("unchecked")
					List<TradeDTO> list = (List<TradeDTO>) map.get("list");
					if(list != null && list.size()>0){
						Set<Long> tidSet = new HashSet<Long>();
						for (TradeDTO tradeDTO : list) {
							tidSet.add(tradeDTO.getTid());
						}
						
						//日志上传
						LogAccessDTO log = new LogAccessDTO();
						log.setAti(RequestUtil.getAtiValue(request));
						log.setUserIp("118.178.185.208");
						log.setUserId(user.getTaobaoUserNick());
						log.setOperation("订单查询");
						log.setUrl("http://crm.kycrm.com/shopData/index#/backstageManagement/orderquery");
						log.setTid(StringUtils.join(tidSet, ","));
						log.setTradeIds(StringUtils.join(tidSet, ","));
						
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(LogType.class.getName(), LogType.ORDER_TYPE);
						map.put(LogAccessDTO.class.getName(), log);
						LogAccessQueueService.getQueue().put(map);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
