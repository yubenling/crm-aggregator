package com.kycrm.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.user.RechargeMenu;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.domain.vo.payment.AliPayVO;
import com.kycrm.member.domain.vo.user.UserRechargeVO;
import com.kycrm.member.service.payment.IAlipayService;
import com.kycrm.member.service.user.IRechargeMenuService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.user.IUserRechargeService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.member.utils.pay.PayHelperUtil;
import com.kycrm.util.Constants;
import com.kycrm.util.IdUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.PayUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.ArticleItemViewUnit;
import com.taobao.api.domain.OrderConfirmQueryDto;
import com.taobao.api.request.FuwuPurchaseOrderConfirmRequest;
import com.taobao.api.request.FuwuSkuGetRequest;
import com.taobao.api.response.FuwuPurchaseOrderConfirmResponse;
import com.taobao.api.response.FuwuSkuGetResponse;

@Controller
@RequestMapping(value = "/backstage")
public class UserRechargeController extends BaseController {
	private static final Log logger = LogFactory.getLog(UserRechargeController.class);
	@Autowired
	private IUserRechargeService userRechargeService;
	@Autowired
	private IAlipayService alipayService;
	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private IRechargeMenuService rechargeMenuService;
	@Autowired
	private IUserInfoService userInfoService;
	
	
	/**
	 * @Title: 查询充值项目列表
	 * @param @return 参数
	 * @return String 返回类型
	 * @author:jackstraw_yu
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "/rechargeMenu")
	public String toTaobao() {
		Map<String, Object> map = new HashMap<String,Object>();
		//调用service查询出所有的充值项目列表==>不包含父级Mid
		List<RechargeMenu> rechargeMenuList = rechargeMenuService.queryRechargeMenuList();
		map.put("list", rechargeMenuList);
		return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}
	
	
	/**
	 * @Description: 自定义短信充值----二维码支付 
	 * @author HL
	 * @param rechargeNum //必填) 短信充值条数
	 * @param totalAmount (必填) 订单总金额，单位为元，不能超过1亿元
	 * @date 2017年11月16日 下午5:24:35
	 */
    @ResponseBody
    @RequestMapping(value = "/qrCodePay", method = RequestMethod.POST)
	public String qrCodePay(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response) {
    	UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
    	if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
    	AliPayVO vo = new AliPayVO();
    	if (null != params && !"".equals(params)) {
    		try {
    			vo = JsonUtil.paramsJsonToObject(params, AliPayVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
    	
		if (null != user&&null != user.getTaobaoUserNick() && !"".equals(user.getTaobaoUserNick()) && null != vo.getTotalAmount()
				&& vo.getTotalAmount() > 0 && null != vo.getRechargeNum() && vo.getRechargeNum() > 0
				&& PayUtil.checkoutSmsAndMoney(vo.getTotalAmount(), vo.getRechargeNum())) {

        	// (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        	String subject = PayUtil.getSubject(vo.getRechargeNum(),vo.getTotalAmount());
        	
            // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
            String outTradeNo = IdUtils.getTradeId();

            //获取二维码数据
            AlipayF2FPrecreateResult result = PayHelperUtil.buildQRcodePay(outTradeNo, vo.getTotalAmount(), subject);
            
            if(result !=null){
            	switch (result.getTradeStatus()) {
	                 case SUCCESS:
	                	 logger.info("**********二维码支付*************支付宝预下单成功:"+result.getTradeStatus());
	                     AlipayTradePrecreateResponse apResponse = result.getResponse();
		     		
	                     try {
	                    	//响应二维码图片路径
							String qrCodePath = PayUtil.createQrCodeImage(
									PayUtil.createLogoPath(request),
									apResponse.getOutTradeNo(),
									apResponse.getQrCode());
							logger.info("**********二维码支付**************路径=》："+qrCodePath);
							
		     				if(null != qrCodePath && !"".equals(qrCodePath)){
		     					//保存充值记录，
		     					int i = createUserRechargeRecord(user,vo.getTotalAmount(),vo.getRechargeNum(),outTradeNo);
		     					if(i==1){
		     					    Map<String, Object> map=new HashMap<String, Object>();
		     					    map.put("qrCodePath", qrCodePath);
		     					    map.put("outTradeNo", outTradeNo);
		     						return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
		     					}
		     				}
		     			} catch (Exception e) {
		     				logger.error("**********二维码支付************失败********"+e.getMessage());
		     				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		     			}
		     			
	                 case FAILED:
	                	 logger.error("**********二维码支付*****************支付宝预下单失败!!!");
	                     break;
	
	                 case UNKNOWN:
	                	 logger.error("**********二维码支付*****************系统异常，预下单状态未知!!!");
	                     break;
	
	                 default:
	                	 logger.error("**********二维码支付*****************不支持的交易状态，交易返回异常!!!");
	                     break;
            	}
            }
    	}else{
    		logger.error("**********二维码支付****************参数异常：(userNick):"+user.getTaobaoUserNick()+"(totalAmount):"+vo.getTotalAmount()+"(rechargeNum):"+vo.getRechargeNum());
    	}
		return failureReusltMap(ApiResult.RECHARGE_FAILURE).toJson();
    }
    
    /**
     * @Description: 自定义短信充值----跳转支付 
     * @author HL
     * @date 2017年11月16日 下午5:25:25
     * @param rechargeNum //必填) 短信充值条数
	 * @param totalAmount (必填) 订单总金额，单位为元，不能超过1亿元
     */
    @ResponseBody
	@RequestMapping(value = "/skipPay", method = RequestMethod.POST)
	public String skipPay(Double totalAmount,Integer rechargeNum,
			HttpServletRequest request, HttpServletResponse response) {
    	UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
    	if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
    	if (null != user.getTaobaoUserNick() && !"".equals(user.getTaobaoUserNick()) && null != totalAmount
				&& totalAmount > 0 && null != rechargeNum && rechargeNum > 0
				&& PayUtil.checkoutSmsAndMoney(totalAmount, rechargeNum)) {
        	
        	// (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
            String outTradeNo = IdUtils.getTradeId();

        	// (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
            String subject = PayUtil.getSubject(rechargeNum,totalAmount);
        	
			// 获取封装支付宝请求数据
           try {
        	    String result = PayHelperUtil.buildSkipPay(outTradeNo, totalAmount, subject);
        	    if(null != result && !"".equals(result)){
        	    	//保存记录
        	    	int i = createUserRechargeRecord(user,totalAmount, rechargeNum, outTradeNo);
        	    	if(i==1){
        	    		return successReusltMap(null).put(ApiResult.API_RESULT, result).toJson();
        	    	}
        	    }
             } catch (Exception e) {
            	 logger.error("*************跳转支付*************失败*******"+e.getMessage());
            	 return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
             }
		}else{
			logger.error("*************跳转支付*********************参数异常：(userNick):"+user.getTaobaoUserNick()+"，(totalAmount):"+totalAmount+"，(rechargeNum):"+rechargeNum);
    	}
    	return failureReusltMap(ApiResult.RECHARGE_FAILURE).toJson();
    }
    
    
    
    /**
     * 接收支付宝异步返回支付通知消息
     * @author HL
     */
    @ResponseBody
    @RequestMapping(value = "/payNotify")
	public String payNotify(String out_trade_no, String trade_status,
			Double total_amount) {
    	try {
			if (null != trade_status && !"".equals(trade_status)
					&& null != out_trade_no && !"".equals(out_trade_no)
					&& null != total_amount) {
					AliPayVO vo = new AliPayVO();
					vo.setPayTrade(out_trade_no);
					vo.setTotalAmount(total_amount);
					vo.setTradeStatus(trade_status);
				 String status = alipayService.disposePayNotify(vo);
				 return status;
			}
		} catch (Exception e) {
			logger.error("***************************接收支付宝异步通知代码处理异常"+e.getMessage());
		}
		return "";
    }
    
    
    /**
     * 查询充值记录状态
     * @author HL
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/payStatus")
	public String payStatus(@RequestBody String params) {
    	AliPayVO vo = new AliPayVO();
    	if (null != params && !"".equals(params)) {
    		try {
    			vo = JsonUtil.paramsJsonToObject(params, AliPayVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		String status = userRechargeService.findPayStatus(vo.getPayTrade());
		return successReusltMap(null).put(ApiResult.API_RESULT, status).toJson();
    }
    
    /**
     * @Description: 跳转支付的页面跳转 
     * @author HL
     * @date 2017年11月16日 下午6:39:25
     */
    @RequestMapping(value = "/skipPayPage")
	public String skipPayPage(String totalAmount,String rechargeNum,HttpServletRequest request) {
    	request.setAttribute("totalAmount", totalAmount);
    	request.setAttribute("rechargeNum", rechargeNum);
		return "skipPayPage";
    }
    
    
    
	/**
	* @Title: rechargeRecord
	* @Description: 充值记录查询
	* @param @param model
	* @param @return    参数
	* @return String    返回类型
	* @author:jackstraw_yu
	* @throws
	*/
	@ResponseBody
	@RequestMapping(value = "/rechargeRecord")
	public String rechargeRecord(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		UserRechargeVO vo = new UserRechargeVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, UserRechargeVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		vo.setUid(user.getId());
		Map<String, Object> map = userRechargeService.findRechargeRecordList(vo);
		map.put("pageNo", vo.getPageNo());
		return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}
	
    
	
	
	
	/**
	 * 充值菜单-充值短信
	 * @author HL
	 * @time 2018年6月18日 下午5:41:42 
	 * @param mid
	 * @param superMid
	 */
	@RequestMapping(value = "/menuPay")
	public void menuPay(String mid,String superMid,
			HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		
		if(null != user){
			user = userInfoService.findUserInfo(user.getId());
			// 传入获得服务信息
			Map<String, Object> more = getMore(mid,superMid, user.getTaobaoUserNick(),
					user.getAccessToken());
			Long cycNum = (Long) more.get("cycNum");
			Long cycUnit = (Long) more.get("cycUnit");
			String sub_itemCode = (String) more.get("sub_itemCode");

			try {
				// 付款页面:如果信息丢失会导致程序异常,使用try...catch{}
				String path = getPath(sub_itemCode, cycNum, cycUnit);
				if(null != path && !"".equals(path)){
					response.sendRedirect(path);
				}
				response.sendRedirect("/shopData/index#/backstageManagement/rechargeRenewals");
			} catch (Exception e) {
				logger.info("***************菜单充值****************生成付款链接异常"+e.getMessage());
			}
		}else{
			logger.info("***************菜单充值****************UserInfo为空！！！");
		}
	}

	/**
	 * 根据服务code获取具体的服务信息
	 * 
	 * @param itemCode
	 * @param taobao_user_nick
	 * @param token
	 * @return
	 */
	public Map<String, Object> getMore(String mid,String superMid,
			String taobao_user_nick, String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 3、根据内购服务编号获取服务信息
		TaobaoClient client = new DefaultTaobaoClient(Constants.TAOBAO_URL,
				Constants.TOP_APP_KEY, Constants.TOP_APP_SECRET);
		FuwuSkuGetRequest req = new FuwuSkuGetRequest();
		// 服务代码
		req.setArticleCode(mid);
		req.setNick(taobao_user_nick);
		req.setAppKey(Constants.TOP_APP_KEY);
		FuwuSkuGetResponse rsp = null;
		try {
			rsp = client.execute(req, token);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		if (rsp.getErrorCode() != null) {
			return map;
		}
		
		 List<ArticleItemViewUnit> articleItemViewUnits = rsp.getResult().getArticleItemViewUnits();
		 if(articleItemViewUnits!=null&&articleItemViewUnits.size()>0){
			 for(ArticleItemViewUnit article : articleItemViewUnits){
				 //与前端页面传入的子code对比
				 if(article.getItemCode() !=null && article.getItemCode().equals(superMid)){
					 
					 Long cycNum = article.getCycNum();
					 Long cycUnit = article.getCycUnit();
					 String sub_itemCode = article.getItemCode();
					 
					 map.put("cycNum", cycNum);
					 map.put("cycUnit", cycUnit);
					 map.put("sub_itemCode", sub_itemCode);
				 }
			 }
		 }		 
		
		return map;
	}

	/**
	 * 根据服务信息生成付款页面
	 * 
	 * @param sub_itemCode
	 * @param cycNum
	 * @param cycUnit
	 * @return
	 */
	public String getPath(String sub_itemCode, Long cycNum, Long cycUnit) {
		// 4、生成付款页面
		TaobaoClient client = new DefaultTaobaoClient(Constants.TAOBAO_URL,
				Constants.TOP_APP_KEY, Constants.TOP_APP_SECRET);
		FuwuPurchaseOrderConfirmRequest req = new FuwuPurchaseOrderConfirmRequest();
		OrderConfirmQueryDto obj1 = new OrderConfirmQueryDto();
		obj1.setAppKey(Constants.TOP_APP_KEY);
		obj1.setItemCode(sub_itemCode);
		if(cycUnit!=null){
			obj1.setCycUnit(cycUnit.toString());
		}
		if(cycNum!=null){
			obj1.setCycNum(cycNum.toString());
		}		
		req.setParamOrderConfirmQueryDTO(obj1);
		FuwuPurchaseOrderConfirmResponse rsp = null;
		try {
			rsp = client.execute(req);
			logger.info("用户付款链接生成：" + rsp.getBody());
		} catch (Exception e) {
			logger.error("....................................RechargeController.getPath():Exception"+e.getMessage());
		}
		String url = rsp.getUrl();
		logger.info("....................................RechargeController.getPath():path"+url);
		return url;
	}
	
    /**
     * 创建充值对象
     * @throws Exception
     */
	private int createUserRechargeRecord(UserInfo user, Double totalAmount,
			Integer rechargeNum, String outTradeNo) {
			UserRecharge ur = new UserRecharge();
			ur.setUid(user.getId());
			ur.setUserNick(user.getTaobaoUserNick());
			ur.setRechargePrice(totalAmount);
			ur.setRechargeType("1");
			ur.setUnitPrice(PayUtil.univalence(totalAmount));
			ur.setStatus("3");
			ur.setRemarks("自定义充值");
			ur.setOrderId(outTradeNo);
			ur.setRechargeNum(rechargeNum);
			ur.setCreatedBy(user.getTaobaoUserNick());
			ur.setLastModifiedBy(user.getTaobaoUserNick());
			return userRechargeService.saveUserRechar(ur);
	}
}
