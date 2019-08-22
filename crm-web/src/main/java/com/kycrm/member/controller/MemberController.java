package com.kycrm.member.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.to.RemarkInfo;
import com.kycrm.member.domain.to.SimplePage;
import com.kycrm.member.domain.vo.member.MemberCriteria;
import com.kycrm.member.domain.vo.member.MemberVO;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MyBeanUtils;

/** 
* @ClassName: MemberController 
* @Description 会员信息控制层<br/>
* 1:会员短信群发
* @author jackstraw_yu
* @date 2018年2月7日 上午10:59:00 
*/
//TODO 所有牵扯到加密解密的地方都还没有做!!!!!
//TODO 手机号   收货地址  收货人 会员昵称  都是加密的
@Controller
@RequestMapping("/member")
public class MemberController extends BaseController {
	private static final Log logger = LogFactory.getLog(MemberController.class);
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private IMemberDTOService memberDTOServiceImpl;
	
	@Autowired
	private ITradeDTOService tradeDTOService;
	
	@RequestMapping("/index")
	public String memberIndex(){
		return "ctManagement/index";
	}
	
	/** 
	* @Description: 客户信息页,查询会员分页信息 
	* @param  request
	* @param  response
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月8日 下午12:09:23
	*/
	@RequestMapping("/queryMemberPage")
	public String queryMemberPage(@RequestBody String params,HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo result = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		MemberCriteria criteria = JsonUtil.paramsJsonToObject(params, MemberCriteria.class);
		if(criteria==null) return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		//避免筛选条件冲突
		if( (criteria.getMinLastTradeTime()!=null || criteria.getMaxLastTradeTime()!=null)
			&& (criteria.getMinNoneTradeTime()!=null || criteria.getMaxNoneTradeTime()!=null)
		  ) return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		criteria.setUid(result.getId());criteria.setUserNick(result.getTaobaoUserNick());
		SimplePage page=null; 
		try {
			page = memberDTOServiceImpl.queryMemberPage(result.getId(),criteria);
		} catch (Exception e) {
			logger.error("##################### queryMemberPage() Exception:"+e.getMessage());
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		return successReusltMap(null).put(ApiResult.API_RESULT,page).toJson();
	}
	
	
	
	/** 
	* @Description 客户信息页,查询单个会员详情用于数据回填
	* @param  request
	* @param  response
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月8日 下午12:10:05
	*/
	@RequestMapping("/queryMembeInfo")
	public String queryMembeInfo(@RequestBody String params,HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo result = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		MemberCriteria criteria = JsonUtil.paramsJsonToObject(params, MemberCriteria.class);
		if(criteria==null) return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		criteria.setUid(result.getId());criteria.setUserNick(result.getTaobaoUserNick());
		MemberInfoDTO member=null; 
		try {
			member = memberDTOServiceImpl.queryMemberInfo(result.getId(),criteria);
		} catch (Exception e) {
			logger.error("##################### queryMemberInfo() Exception:"+e.getMessage());
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		return successReusltMap(null).put(ApiResult.API_RESULT,member).toJson();
	}
	
	
	/** 
	* @Description  修改保存会员信息
	* @param  request
	* @param  response
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月8日 下午4:48:31
	*/
	@RequestMapping("/updateMembeInfo")
	public String updateMembeInfo(@RequestBody String params,HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo result = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		MemberVO vo = JsonUtil.paramsJsonToObject(params, MemberVO.class);
		if(vo==null) return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		vo.setUid(result.getId());vo.setUserName(result.getTaobaoUserNick());
		MemberInfoDTO member=null; 
		try {
			//此处可修改的属性:生日/qq/年龄/职业/邮箱/性别/微信
			//单独提供接口:备注/收货信息
			member = this.convertMemberInfoDTO(vo,new MemberInfoDTO(),false);
			memberDTOServiceImpl.updateMembeInfo(result.getId(),member);
		} catch (Exception e) {
			logger.error("##################### updateMembeInfo() Exception:"+e.getMessage());
			if(e.getCause() instanceof TimeoutException)
				return exceptionReusltMap(ApiResult.TIMEOUT_EXCEPTION).toJson();
			if(e.getCause() instanceof RemotingException)
				return exceptionReusltMap(ApiResult.REMOTING_EXCEPTION).toJson();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		return successReusltMap(null).put(ApiResult.API_RESULT,member).toJson();
	}
	
	
	/** 
	* @Description 添加或者删除会员备注
	* @param  request
	* @param  response
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月27日 下午4:26:05
	*/
	@RequestMapping("/updateMembeRemarks")
	public String updateMembeRemarks(@RequestBody String params,HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo result = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		MemberVO vo = JsonUtil.paramsJsonToObject(params, MemberVO.class);
		if(vo==null) return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		vo.setUid(result.getId());vo.setUserName(result.getTaobaoUserNick());
		MemberInfoDTO member=null; 
		try {//属性赋值
			member = convertMemberInfoDTO(vo,new MemberInfoDTO(),true);
		} catch (Exception e) {
			logger.error("##################### convertMemberInfoDTO() Exception:"+e.getMessage());
			return exceptionReusltMap(ApiResult.PARAM_WRONG).toJson();
		}
		if(member==null || member.getId()==null || member.getUserName()==null)  
			return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		boolean check = checkMemberRemarks(member);
		if(!check) return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		try {
			memberDTOServiceImpl.updateMembeRemarks(result.getId(),member);
		} catch (Exception e) {
			logger.error("##################### updateMembeRemarks() Exception:"+e.getMessage());
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		return successReusltMap(null).put(ApiResult.API_RESULT,member).toJson();
	}
	
	
	/**
	 * 查询该客户的订单数据
	 */
	@RequestMapping(value="/queryMemberTrades",/*method=RequestMethod.POST,*/produces="text/html;charset=UTF-8")
	public String queryMemberTrades(@RequestBody String params,
									HttpServletRequest request,HttpServletResponse response,
									String buyerNick,Date minTradeTime,Date maxTradeTime,
									@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageNo,
									@RequestParam(value="pageNo",required=false,defaultValue="1")Integer pageSize) {
		UserInfo result = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("buyerNick", buyerNick);map.put("uid",result.getId());
		map.put("pageNo", pageNo);map.put("pageNo", pageSize);
		map.put("minTradeTime", minTradeTime);map.put("maxTradeTime", maxTradeTime);
		SimplePage page=null; 
		try {
			page = tradeDTOService.queryMemberTradePage(result.getId(),map);
		} catch (Exception e) {
			logger.error("##################### queryMemberPage() Exception:"+e.getMessage());
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		return successReusltMap(null).put(ApiResult.API_RESULT,page).toJson();
	}
	
	
	
	
//==================================私有方法	
	/** 
	* @Description: 将vo转换为dto
	* @param source
	* @param target
	* @param isUpdate  true值仅限于修改会员备注
	* @return MemberInfoDTO    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月8日 下午5:25:05
	* @throws Exception 
	*/
	private MemberInfoDTO convertMemberInfoDTO(MemberVO source,MemberInfoDTO target,boolean isUpdate) throws Exception{
		/*
		String receiveStr = source.getReceiverInfoStr();
		if(receiveStr!=null && !"".equals(receiveStr)){
			List<ReceiverInfo> receivers = JsonUtil.readValuesAsArrayList(receiveStr, ReceiverInfo.class);
			if(receivers !=null && !receivers.isEmpty()) 
				target.setReceiverList(receivers);
		}
		==========
		//开始校验 备注数据
		if(member.getReceiverInfoStr()==null || "".equals(member.getReceiverInfoStr()))
			return failureReusltMap(ApiResult.PARAM_WRONG).toJson();//不能为空
		if(member.getReceiverList()==null)
			return failureReusltMap(ApiResult.PARAM_WRONG).toJson();//不能为空
		if(member.getReceiverList()!=null && member.getReceiverList().size()>5)
			return failureReusltMap(ApiResult.PARAM_WRONG).toJson();//长度不能大于5个
		*/
		if(isUpdate)
			if(source.getRemarkStr()!=null && !"".equals(source.getRemarkStr())){//此处代码:为方便修改备注服务
				List<RemarkInfo> list = 
						JsonUtil.readValuesAsArrayList(source.getRemarkStr(), RemarkInfo.class);
				if(list!=null && !list.isEmpty())
					target.setRemarkList(list);
			}
		MyBeanUtils.copyProperties(target, source);
		return target;
	}
	
	/** 
	* @Description  用户修改备注信息,校验数据,备注个数不能长于5个
	* @param  member
	* @return boolean    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月28日 上午11:24:14
	*/
	private boolean checkMemberRemarks(MemberInfoDTO member){
		if(member.getRemarks()==null ||"".equals(member.getRemarks()))
			return false;
		if(member.getRemarkList()==null) return false;
		if(member.getRemarkList()!=null && member.getRemarkList().size()>5)
			return false;
		List<RemarkInfo> list = member.getRemarkList();
		for (RemarkInfo remark : list) {//备注信息不能为空
			//if(remark.getId()==null) return false;
			if(remark.getModifideDate()==null) return false;
			if(remark.getRemarks()==null||"".equals(remark.getRemarks())) return false;
		}
		return true;
	}
	
	
	
}