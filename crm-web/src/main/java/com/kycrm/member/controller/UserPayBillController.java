package com.kycrm.member.controller;

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
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserPayBill;
import com.kycrm.member.domain.vo.user.UserPayBillVO;
import com.kycrm.member.service.user.IUserPayBillService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;

/**
 * 处理用户打款处理器
 * @author 余本领
 *
 */
@Controller
@RequestMapping(value = "/backstage")
public class UserPayBillController extends BaseController{
	
	
	private  static final Logger logger=LoggerFactory.getLogger(UserPayBillController.class);
    
	@Autowired
	private IUserPayBillService userPayBillService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@ResponseBody
	@RequestMapping(value="selectPayBillList")
	public String selectPayBillList(@RequestBody String params,
			HttpServletRequest request,HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
    	if(null == user){
    		return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
    	}
    	UserPayBillVO userPayBillVO=new UserPayBillVO();
    	try {
    		if(null!=params&&!"".equals(params)){
    			//转换对象
    			userPayBillVO = JsonUtil.paramsJsonToObject(params, UserPayBillVO.class);		
    		}
		} catch (Exception e) {
			logger.info("查询打款记录出错");
			e.printStackTrace();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
    	//转换时间
    	if(userPayBillVO!=null&&userPayBillVO.getPayStartTimeStr()!=null&&!"".equals(userPayBillVO.getPayStartTimeStr())){}
    	    userPayBillVO.setPayStartTime(
    	    		DateUtils.parseTime(userPayBillVO.getPayStartTimeStr(), DateUtils.DEFAULT_TIME_FORMAT));
    	if(userPayBillVO!=null&&userPayBillVO.getPayEndTimeStr()!=null&&!"".equals(userPayBillVO.getPayEndTimeStr())){
    		userPayBillVO.setPayEndTime(
    				DateUtils.parseTime(userPayBillVO.getPayEndTimeStr(), DateUtils.DEFAULT_TIME_FORMAT));
    	}
    	Map<String, Object> map=new HashMap<String, Object>();
    	//开始分页查询所有的打款记录
    	List<UserPayBill> payBillList=userPayBillService.selectPayBillList(userPayBillVO,user);
	    //查询总条数
    	Integer count=userPayBillService.selectPayBillListCount(userPayBillVO,user);
    	//当前页
    	Integer pageNo = userPayBillVO.getPageNo();
    	//总页数
    	Integer totalPage=count%userPayBillVO.getCurrentRows()==0?count/userPayBillVO.getCurrentRows():(count/userPayBillVO.getCurrentRows()+1);
    	map.put("list", payBillList);
    	map.put("count", count);
    	map.put("pageNo", pageNo);
    	map.put("totalPage", totalPage);
    	return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}

	
}
