package com.kycrm.member.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.user.UserBillRecord;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserPayBill;
import com.kycrm.member.domain.vo.user.UserBillRecordVO;
import com.kycrm.member.service.user.IUserBillRecordService;
import com.kycrm.member.service.user.IUserPayBillService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;


@Controller
@RequestMapping(value = "/backstage")
public class UserBillRecordController extends BaseController{
	
	
	private static final Logger logger=LoggerFactory.getLogger(UserBillRecordController.class);
    
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private IUserBillRecordService userBillRecordService;
	
	@Autowired
	private IUserPayBillService userPayBillService;
	
	@ResponseBody
	@RequestMapping(value="selectBillRecordList")
    public String selectBillRecordList(@RequestBody String params,
    		HttpServletRequest request,
    		HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(user==null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		//转换对象
		UserBillRecordVO userBillRecordVo=new UserBillRecordVO();
		try {
			if(null!=params&&!"".equals(params)){
				userBillRecordVo=JsonUtil.paramsJsonToObject(params, UserBillRecordVO.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//转换时间
		if(userBillRecordVo!=null && userBillRecordVo.getApplyEndTimeStr()!=null){
			userBillRecordVo.setApplyEndTime(
			DateUtils.parseTime(userBillRecordVo.getApplyEndTimeStr(), DateUtils.DEFAULT_TIME_FORMAT));
		}
		if(userBillRecordVo!=null && userBillRecordVo.getApplyStartTimeStr()!=null){
			userBillRecordVo.setApplyStartTime(
					DateUtils.parseTime(userBillRecordVo.getApplyStartTimeStr(), DateUtils.DEFAULT_TIME_FORMAT));
		}
		Map<String,Object> map=new HashMap<String, Object>();
		List<UserBillRecordVO> userBillRecordList=userBillRecordService.selectBillRecordList(user,userBillRecordVo);
		//总条
		Integer count=userBillRecordService.selectBillRecordListCount(user,userBillRecordVo);
		//当前页
    	Integer pageNo = userBillRecordVo.getPageNo();
    	//总页数
    	Integer totalPage=count%userBillRecordVo.getCurrentRows()==0?count/userBillRecordVo.getCurrentRows():(count/userBillRecordVo.getCurrentRows()+1);
    	map.put("list", userBillRecordList);
    	map.put("count", count);
    	map.put("totalPage", totalPage);
    	map.put("pageNo", pageNo);
    	return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}

	@ResponseBody
	@RequestMapping(value="saveBillRecord")
	public String saveBillRecord(@RequestBody String params,
    		HttpServletRequest request,
    		HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null==user){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		//转换对象
		UserBillRecordVO userBillRecordVO=new UserBillRecordVO();
		try {
			if(null!=params&&!"".equals(params)){
				userBillRecordVO=JsonUtil.paramsJsonToObject(params, UserBillRecordVO.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return exceptionReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		}
		if(null==userBillRecordVO){
			return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}
		//添加uid 和 UserName
		userBillRecordVO.setUid(user.getId());
		userBillRecordVO.setUserName(user.getTaobaoUserNick());
		//判断是一键申请，还是单个申请
		String applyStyle = userBillRecordVO.getApplyStyle();
		List<UserPayBill> payBillList=null;
		if("1".equals(applyStyle)){ //一键申请
			payBillList=userPayBillService.findAllPayBillByStatus(user.getId(),"0");
			BigDecimal computerSum=userPayBillService.selectAllPayBillAmont(user);
			userBillRecordVO.setApplyPrice(computerSum);
		}else if("2".equals(applyStyle)){ //单个或多个申请
			String orderIdList = userBillRecordVO.getOrderIdList();
		    payBillList=userPayBillService.findPayBillByOrderId(user,orderIdList);
		}
		//返回发票记录id
		Long billRecordId=userBillRecordService.saveBillRecord(payBillList,userBillRecordVO);
		//修改打款记录中的打款信息状态
		for(UserPayBill upb:payBillList){
			//申请中
			upb.setBillStatus(1);
			upb.setBillRecordId(billRecordId);
		}
		userPayBillService.updatePayBillStatus(payBillList);
		return successReusltMap(ApiResult.SAVE_SUCCESS).toJson();
	}
	
	/**
	 * 撤销发票申请
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="cancleApplyRecord")
	public String cancleApplyRecord(@RequestBody String params,
    		HttpServletRequest request,
    		HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null==user){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		//转换对象
		UserBillRecord userBillRecord=new UserBillRecord();
		if(null!=params&&!"".equals(params)){
			userBillRecord=JsonUtil.paramsJsonToObject(params, UserBillRecord.class);	
		}
		if(userBillRecord==null||userBillRecord.getId()==null){
			return failureReusltMap(ApiResult.PARAM_LACK).toJson();
		}
		//删除发票记录
		Integer delSum=userBillRecordService.deleteBillRecordById(userBillRecord.getId());
		//如果发票记录删除成功，查询出所有的打款记录，修改状态为未申请
		if(delSum>0){
			userPayBillService.updatePayBillStatusByRecord(user,userBillRecord);
		}
		return successReusltMap(ApiResult.UPDATE_SUCCESS).toJson();
	}
	
	/**
	 * 发票记录查询订单明细
	 * @param params id 发票记录id
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="payBillListDetail")
	public String  selectPayBillByBillRecord(@RequestBody String params,
    		HttpServletRequest request,HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null==user){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		//转换对象
		UserBillRecordVO userBillRecord=new UserBillRecordVO();
		if(null!=params&&!"".equals(params)){
			userBillRecord=JsonUtil.paramsJsonToObject(params, UserBillRecordVO.class);	
		}
		if(userBillRecord==null||userBillRecord.getId()==null){
			return failureReusltMap(ApiResult.PARAM_LACK).toJson();
		}
		//查询出该发票记录
		UserBillRecord ubr = userBillRecordService.selectBillRecordById(userBillRecord.getId());
		//查询所有的打款记录
		Map<String, Object> map=new HashMap<String, Object>();
		List<UserPayBill> list=userPayBillService.selectPayBillByRecordId(user,userBillRecord);
		//查询总条数
		Integer count=userPayBillService.selectPayBillByRecordIdCount(user,userBillRecord);
		//当前页
    	Integer pageNo = userBillRecord.getPageNo();
    	//总页数
    	Integer totalPage=count%userBillRecord.getCurrentRows()==0?count/userBillRecord.getCurrentRows():(count/userBillRecord.getCurrentRows()+1);
    	map.put("list", list);
    	map.put("count", count);
    	map.put("totalPage", totalPage);
    	map.put("pageNo", pageNo);
		map.put("payBillAmount", ubr.getApplyPrice());
		return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}
	/**
	 * 查询发票记录中的发票信息
	 */
	@ResponseBody
	@RequestMapping(value="selectBillRecordById")
	public String selectBillRecordById(@RequestBody String params,
    		HttpServletRequest request,HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null==user){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		//转换对象
		UserBillRecordVO userBillRecord=new UserBillRecordVO();
		if(null!=params&&!"".equals(params)){
			userBillRecord=JsonUtil.paramsJsonToObject(params, UserBillRecordVO.class);	
		}
		if(userBillRecord==null||userBillRecord.getId()==null){
			return failureReusltMap(ApiResult.PARAM_LACK).toJson();
		}
		UserBillRecord ubr=userBillRecordService.selectBillRecordById(userBillRecord.getId());
		return successReusltMap(null).put(ApiResult.API_RESULT, ubr).toJson();
	}
	/**
	 * 修改发票信息中的发票信息
	 * @param params id  发票记录
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="updateRecordBillInfo")
	public String  updateRecordBillInfo(@RequestBody String params,
    		HttpServletRequest request,HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null==user){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		//转换对象
		UserBillRecordVO userBillRecord=new UserBillRecordVO();
		if(null!=params&&!"".equals(params)){
			userBillRecord=JsonUtil.paramsJsonToObject(params, UserBillRecordVO.class);	
		}
		if(userBillRecord==null||userBillRecord.getId()==null){
			return failureReusltMap(ApiResult.PARAM_LACK).toJson();
		}
		//修改发票记录中的发票信息
		userBillRecordService.updateRecordBillInfo(userBillRecord);
		return successReusltMap(ApiResult.UPDATE_SUCCESS).toJson();
	}
}
