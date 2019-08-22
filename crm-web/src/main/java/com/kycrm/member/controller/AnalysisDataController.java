package com.kycrm.member.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.effect.EffectStandardRFM;
import com.kycrm.member.domain.entity.other.TaskNode;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.effect.EffectStandardRFMVO;
import com.kycrm.member.service.effect.IEffectStandardRFMService;
import com.kycrm.member.service.effect.IRFMDetailChartService;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.other.ITaskNodeService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;


/**
 * 数据分析的Controller
 * @ClassName: AnalysisDataController  
 * @author ztk
 * @date 2018年11月27日 下午3:12:59
 */
@Controller
@RequestMapping("/analysis")
public class AnalysisDataController extends BaseController {

	
private Logger logger = LoggerFactory.getLogger(AnalysisDataController.class);
	
	@Autowired
	private IEffectStandardRFMService standardRFMService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private ITradeDTOService tradeDTOService;
	
	@Autowired
	private IMemberDTOService memberDTOService;
	
	@Autowired
	private ITaskNodeService taskNodeService;
	
	@Autowired
	private IRFMDetailChartService rfmDetailChartService;
	
	@RequestMapping("/index")
	private String index(){
		return "/dataAnalysis/index";
	}
	
	/**
	 * standardRFM(RFM分析中的标准分析)
	 * @Title: standardRFM 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/standardRFM")
	@ResponseBody
	public String standardRFM(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params){
		EffectStandardRFM standardRFMVO = null;
		try {
			standardRFMVO = JsonUtil.paramsJsonToObject(params, EffectStandardRFM.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "参数转换异常，请联系系统管理员!").put("status", false).toJson();
		}
		if(standardRFMVO == null){
			return rsMap(101, "操作失败，请求参数为空!").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		EffectStandardRFMVO resultVO = standardRFMService.sumStandardRFM(userInfo.getId(), standardRFMVO.getEffectType());
		TaskNode taskNode = taskNodeService.queryTaskNodeByType("standardRFM");
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		if(taskNode != null){
			resultMap.put("updateTime", taskNode.getTaskEndTime());
		}else {
			resultMap.put("updateTime", new Date());
		}
		resultMap.put("data", resultVO);
		
		return resultMap.toJson();
	}
	
	/**
	 * detailRFM(RFM分析详情数据)
	 * @Title: detailRFM 
	 * @param @param request
	 * @param @param response
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/detailRFM")
	@ResponseBody
	public String detailRFM(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params){
		Long l1 = System.currentTimeMillis();
		EffectStandardRFMVO standardRFMVO = null;
		try {
			standardRFMVO = JsonUtil.paramsJsonToObject(params, EffectStandardRFMVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "参数转换异常，请联系系统管理员!").put("status", false).toJson();
		}
		if(standardRFMVO == null){
			return rsMap(101, "操作失败，请求参数为空!").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null){
			return rsMap(101, "操作失败,请重新登录!").put("status", false).toJson();
		}
		String dateType = standardRFMVO.getDateType();
		Integer days = standardRFMVO.getDays();
		Date eTime = new Date();
		Date bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(7, eTime));
		if("month".equals(dateType)){
			if(6 == days){
				bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(180, eTime));
			}else if(12 == days){
				bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(365, eTime));
			}else if(24 == days){
				bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(730, eTime));
			}
		}else if("day".equals(dateType)){
			if(7 == days){
				bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(6, eTime));	
			}else if(15 == days){
				bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(14, eTime));
			}else if(30 == days){
				bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(29, eTime));
			}
		}
		Long l2 = System.currentTimeMillis();
		//计算RFM详情的顶部数据
		EffectStandardRFMVO resultRFM = null;
		try {
			resultRFM = memberDTOService.aggregateRFMDetail(userInfo.getId(), standardRFMVO.getTradeNum());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Long l3 = System.currentTimeMillis();
		logger.info("//计算RFM详情的顶部数据耗时：" + (l3 - l2) + "ms");
		//计算RFM详情的图表数据
		Map<String, Object> dataMap = null;
		try {
//			dataMap = tradeDTOService.listRFMDetailChart(userInfo.getId(), standardRFMVO.getDateType(), days, bTime, eTime);
			dataMap = rfmDetailChartService.queryRFMChartMap(userInfo.getId(), standardRFMVO.getTradeNum(), days, standardRFMVO.getDateType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long l4 = System.currentTimeMillis();
		logger.info("//计算RFM详情的图表数据耗时：" + (l4 - l3) + "ms");
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("data", resultRFM);
		if(dataMap != null){
			resultMap.put("dataChart", dataMap);
		}
		logger.info("//计算RFM详情数据总耗时：" + (l4 - l1) + "ms");
		return resultMap.toJson();
	}
}
