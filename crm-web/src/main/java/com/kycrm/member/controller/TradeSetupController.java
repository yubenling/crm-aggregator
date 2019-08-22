package com.kycrm.member.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.item.CommodityGrouping;
import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.item.ItemVO;
import com.kycrm.member.domain.vo.tradecenter.TradeSetupVO;
import com.kycrm.member.service.item.ICommodityGroupingService;
import com.kycrm.member.service.item.IGroupedGoodsService;
import com.kycrm.member.service.item.IItemService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.tradecenter.ITradeSetupService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.vip.IVipUserService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.DateUtils;
import com.kycrm.util.GetCurrentPageUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MobileRegEx;
import com.kycrm.util.MyBeanUtils;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.RedisConstant;



/**	
* @Title: TradSetController
* @Description: (订单中心,各种设置)
* @author:jackstraw_yu
*/
@Controller
@RequestMapping(value="/tradeSetup")
public class TradeSetupController extends BaseController {
	
	private static final Log logger = LogFactory.getLog(TradeSetupController.class);
	//解析时间用
	private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	//正则校验时间用
	private final Pattern regexTime = Pattern.compile("(([01][0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9]");  
	@Autowired
	private ITradeSetupService tradeSetupService;

	@Autowired
	private SessionProvider sessionProvider;

	@Autowired
	private ICommodityGroupingService commodityGroupingService;

	@Autowired
	private IItemService itemService;

	@Autowired
	private IGroupedGoodsService groupedGoodsService;

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private IUserInfoService userInfoService;
	
	@Autowired
	private IVipUserService vipUserService;
	
	
	@RequestMapping("/index")
	public String tradeCenterIndex(){
		return "order/index";
	}
	
	/**
	 * 数据集合
	 * TradeSetupController实例化时创建
	 * 订单中心设置类型-名称
	 * */
	private final Map<String,String> DICT_MAP = new HashMap<String,String>(){   
		private static final long serialVersionUID = 4467723566485282986L;
		{  
          /* put("1", "下单关怀");put("2", "常规催付");put("3", "二次催付");put("4", "聚划算催付");
           put("5", "预售催付");put("6", "发货提醒");put("7", "到达同城提醒");put("8", "派件提醒");  
           put("9", "签收提醒");put("10", "疑难件提醒");put("11", "延时发货提醒");
           put("12", "宝贝关怀");put("13", "付款关怀");put("14", "回款提醒");
           put("16", "自动评价");put("20", "中差评监控");put("21", "中差评安抚");
           put("29", "买家申请退款"); put("30", "退款成功");put("31", "等待退货"); put("32", "拒绝退款");
           put("37", "好评提醒");*/
		   /*  put(OrderSettingInfo.ADVANCE_PUSH_PAYMENT, "预售催付");*//*put(OrderSettingInfo.ABNORMAL_GOODS_REMIND, "疑难件提醒");  新版没有该功能*/
		   put(OrderSettingInfo.CREATE_ORDER, "下单关怀");put(OrderSettingInfo.FIRST_PUSH_PAYMENT, "常规催付");
		   put(OrderSettingInfo.SECOND_PUSH_PAYMENT, "二次催付");put(OrderSettingInfo.PREFERENTIAL_PUSH_PAYMENT, "聚划算催付");
		   put(OrderSettingInfo.SHIPMENT_TO_REMIND, "发货提醒");put(OrderSettingInfo.ARRIVAL_LOCAL_REMIND, "到达同城提醒");
		   put(OrderSettingInfo.SEND_GOODS_REMIND, "派件提醒");put( OrderSettingInfo.REMIND_SIGNFOR, "签收提醒");
           put(OrderSettingInfo.DELAY_SEND_REMIND, "延时发货提醒");put(OrderSettingInfo.COWRY_CARE, "宝贝关怀");
           put(OrderSettingInfo.PAYMENT_CINCERN, "付款关怀");put(OrderSettingInfo.RETURNED_PAYEMNT, "回款提醒"); 
           put(OrderSettingInfo.AUTO_RATE, "自动评价");put(OrderSettingInfo.APPRAISE_MONITORING_ORDER, "中差评监控");
           put(OrderSettingInfo.APPRAISE_PACIFY_ORDER, "中差评安抚");put(OrderSettingInfo.REFUND_CREATED, "买家申请退款");
           put(OrderSettingInfo.REFUND_SUCCESS, "退款成功");put(OrderSettingInfo.REFUND_AGREE, "同意退款");
           put(OrderSettingInfo.REFUND_REFUSE, "拒绝退款");put(OrderSettingInfo.GOOD_VALUTION_REMIND, "提醒好评");
        }  
    };
    
    /**
	 * 数据集合
	 * TradeSetupController实例化时创建
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
     * 短信内容变量与其长度
     * */
    private final Map<String,Integer> PARAM_MAP = new HashMap<String,Integer>(){   
		private static final long serialVersionUID = -5426703862520846913L;
		{  
			/**
			 * 	{买家昵称}:8;{买家姓名}:4;{下单时间}:19;
			 *	{订单编号}:17;{订单金额}:7;{付款链接}:17;
			 *	{物流公司名称}:4;{运单号}:13;{物流链接}:17;
			 *	{到达城市}:5;{确认收货链接}:17;{退款链接}:17;
			 *	{评价链接}:17;
			 * */
           put("{买家昵称}",8); put("{买家姓名}",4); put("{下单时间}",19);
           put("{订单编号}",17); put("{订单金额}",7); put("{付款链接}",17);
           put("{物流公司名称}",4); put("{运单号}",13); put("{物流链接}",17);
           put("{到达城市}",5); put("{确认收货链接}",17); put("{退款链接}",17);
           put("{评价链接}",17);
        }  
    };  
    
    /**
	 * 数据集合;单任务集合
	 * */
    private  final List<String>  SINGLE_LIST = new ArrayList<String>(){   
		private static final long serialVersionUID = -5426703862520846913L;
		{  
			add(OrderSettingInfo.SECOND_PUSH_PAYMENT);//二次催付
			add(OrderSettingInfo.PREFERENTIAL_PUSH_PAYMENT);//聚划算催付
			add(OrderSettingInfo.REFUND_CREATED);//买家申请退款
			add(OrderSettingInfo.REFUND_AGREE);//同意退款
			add(OrderSettingInfo.REFUND_REFUSE);//拒绝退款
			add(OrderSettingInfo.REFUND_SUCCESS);//"退款成功
			add(OrderSettingInfo.AUTO_RATE); //自动评价
			add(OrderSettingInfo.APPRAISE_MONITORING_ORDER);//中差评监控
			add(OrderSettingInfo.APPRAISE_PACIFY_ORDER);//中差评安抚
			add(OrderSettingInfo.GOOD_VALUTION_REMIND);//提醒好评
        }  
    };  
    /**
   	 * 数据集合;多任务集合 
   	 * */
   private  final List<String> MULTI_LIST = new ArrayList<String>(){   
	   private static final long serialVersionUID = -7237592889901587410L;
	   {  
   			add(OrderSettingInfo.CREATE_ORDER);//下单关怀
   			add(OrderSettingInfo.FIRST_PUSH_PAYMENT);//常规催付
            add(OrderSettingInfo.SHIPMENT_TO_REMIND);//发货提醒
   			add(OrderSettingInfo.REMIND_SIGNFOR);//签收提醒
            add(OrderSettingInfo.ARRIVAL_LOCAL_REMIND);//到达同城提醒
   			add(OrderSettingInfo.SEND_GOODS_REMIND);//派件提醒
            add(OrderSettingInfo.DELAY_SEND_REMIND);//延时发货提醒
   			add(OrderSettingInfo.COWRY_CARE);//宝贝关怀
            add(OrderSettingInfo.PAYMENT_CINCERN);//付款关怀
   			add(OrderSettingInfo.RETURNED_PAYEMNT);//回款提醒
   	   }  
   };  
  
   /**
  	 * 数据集合;需要选择发送时间
  	 * */
   private  final List<String> REMIND_LIST = new ArrayList<String>(){   
 	   private static final long serialVersionUID = -7237592889901587410L;
 	   {  
 			add(OrderSettingInfo.SECOND_PUSH_PAYMENT);//二次催付
 			add(OrderSettingInfo.PREFERENTIAL_PUSH_PAYMENT);//聚划算催付
 			add(OrderSettingInfo.AUTO_RATE); //自动评价
 			add(OrderSettingInfo.APPRAISE_MONITORING_ORDER);//中差评监控
 			add(OrderSettingInfo.GOOD_VALUTION_REMIND);//好评提醒
    		add(OrderSettingInfo.CREATE_ORDER);//下单关怀
    		add(OrderSettingInfo.FIRST_PUSH_PAYMENT);//常规催付
            add(OrderSettingInfo.DELAY_SEND_REMIND);//延时发货提醒
            add(OrderSettingInfo.COWRY_CARE);//宝贝关怀
    		add(OrderSettingInfo.RETURNED_PAYEMNT);//回款提醒
 	  }
    };
   
	
	/** 
	* @Description 展示各种订单中心的设置是否开启或者关闭
	* @param  request
	* @param  response
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 上午11:48:06
	*/
	@RequestMapping(value="/showSetupMenu",/*method=RequestMethod.POST,*/produces="text/html;charset=UTF-8")
	public @ResponseBody
	String showSetupMenu(HttpServletRequest request,HttpServletResponse response){ 
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		Map<String,Boolean> map =null;
		try {
			//整个用户的订单中心个各种设置在biz层进行判断
			//true:有一个开启;false:未设置或者位全部开启
			map = tradeSetupService.showSetupMenu(user.getId(),new HashSet<String>(DICT_MAP.keySet()));
		} catch (Exception e) {
			logger.error("##################### showSetupMenu() Exception:"+e.getMessage());
			//DUBBO-超时可重试?
			if(e.getCause() instanceof TimeoutException)
				return exceptionReusltMap(ApiResult.TIMEOUT_EXCEPTION_TRY_AGAGIN).toJson();
			//DUBBO-调用失败可重试?
			if(e.getCause() instanceof RemotingException)
				return exceptionReusltMap(ApiResult.REMOTING_EXCEPTION_TRY_AGAIN).toJson();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}
	
	/**
	* @Title: showShopName
	* @Description: (订单中心显示店铺名称?签名)
	* @return String    返回类型
	* @author:jackstraw_yu
	* @throws
	*/
	@RequestMapping(value="/showShopName",/*method=RequestMethod.POST,*/produces="text/html;charset=UTF-8")
	public @ResponseBody
	String showShopName(Model model,HttpServletRequest request,HttpServletResponse response){ 
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		
		String shopName = null;
		try {
			shopName = queryShopName(user.getId());
			if(shopName==null)
				return failureReusltMap(ApiResult.SHOPNAME_IS_NULL).toJson();
		} catch (Exception e) {
			logger.error("##################### TradeSetupController.showSetupMenu() Exception:"+e.getMessage());
			return  failureReusltMap(ApiResult.OPERATE_EXCEPTION).toJson();
		}
		return successReusltMap(null).put(ApiResult.API_RESULT, shopName).toJson();
	}
	/** 
	* @Description: 保存订单中心的各种设置
	* @param  request
	* @param  response
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午2:00:03
	*/
	@RequestMapping(value="/saveTradeSetup",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public @ResponseBody
	String saveTradeSetup(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){ 
		TradeSetupVO setup = null;
		try {
		setup = parseJsonToVO(params);
		} catch (Exception e) {
			logger.error("##################### parseJsonToVO() Exception"+e.getMessage());
			return  exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		//转空认为失败
		if(setup==null) return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		//加点用户属性uer
//		UserInfo user =userInfoService.findUserInfo(1243L);
		setup.setUserId(user.getTaobaoUserNick());
		setup.setUid(user.getId());
		try {
			//1:校验用户的设置(验证订单的设置是不是正确)
			Map<String, Object> map = checkTradeSetup(setup,user,true);
			if(!(Boolean)map.get("status")){
				return failureReusltMap((String)map.get("msgKey")).toJson();
			}else{
				TradeSetup tradeSetup = null,showVO = null;
				//2:加工转换数据
				tradeSetup = convertSaveSetupData(setup,new TradeSetup());
				//多任务时需设置级别
				if(MULTI_LIST.contains(tradeSetup.getType())){
					Integer level = tradeSetupService.queryMaxTaskLevelByType(tradeSetup);
					tradeSetup.setTaskLevel(level==null?1:++level);
				}
				//3保存??是否返回实体
				showVO = tradeSetupService.saveTradeSetup(tradeSetup);
				try {
					//4进行授权
					tradeSetupService.addUserPermitByMySql(user.getId(), user.getTaobaoUserNick());
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("淘宝授权出错");
				}
				return successReusltMap(ApiResult.SAVE_SET_SUCCESS).put(ApiResult.API_RESULT, showVO).toJson();
			}
		} catch (Exception e) {
			logger.error("##################### showSetupMenu() Exception:"+e.getMessage());
			//DUBBO-不可重试?
			if(e.getCause() instanceof TimeoutException)
				return exceptionReusltMap(ApiResult.TIMEOUT_EXCEPTION).toJson();
			//DUBBO-不失败可重试?
			if(e.getCause() instanceof RemotingException)
				return exceptionReusltMap(ApiResult.REMOTING_EXCEPTION).toJson();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
	}
	
	
	/** 
	* @Description 查询订单中心设置多任务列表
	* @param request
	* @param response
	* @param params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午10:48:02
	*/
	@RequestMapping(value="/queryTradeSetupTable",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public @ResponseBody
	String queryTradeSetupTable(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){ 
		TradeSetupVO setup = null;
		try {
			setup = parseJsonToVO(params);
		} catch (Exception e) {
			logger.error("##################### parseJsonToVO() Exception"+e.getMessage());
			return  exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		//转空认为失败
		if(setup==null) return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		setup.setUid(user.getId());
		setup.setUserId(user.getTaobaoUserNick());
		//搜索参数:任务名称,任务状态;类型,用户名称
		if(setup.getType()==null || "".equals(setup.getType())){
			return  failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(!MULTI_LIST.contains(setup.getType())){
			return  failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else{
			List<TradeSetup> data = null;
			try {
				//查询
				data = tradeSetupService.queryTradeSetupTable(setup);
			} catch (Exception e) {
				logger.error("##################### queryTradeSetupTable() Exception:"+e.getMessage());
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
			return successReusltMap(null).put(ApiResult.API_RESULT,data).toJson();
		}
	}
	
	
	/** 
	* @Description 查询订单中心设置单条设置
	* @param  request
	* @param  response
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午11:06:11
	*/
	@RequestMapping(value="/querySingleTradeSetup",/*method=RequestMethod.POST,*/produces="text/html;charset=UTF-8")
	public @ResponseBody
	String querySingleTradeSetup(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){ 
		TradeSetupVO setup = null;
		try {
			setup = parseJsonToVO(params);
		} catch (Exception e) {
			logger.error("##################### parseJsonToVO() Exception"+e.getMessage());
			return  exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		//转空认为失败
		if(setup==null) return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		setup.setUid(user.getId());
		setup.setUserId(user.getTaobaoUserNick());
		//搜索参数:任务名称,任务状态;类型,用户名称
		if(setup.getType()==null || "".equals(setup.getType())){
			return  failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(!SINGLE_LIST.contains(setup.getType())){
			return  failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else{
			TradeSetup tradeSetup = null;
			try {
				//查询
				tradeSetup = tradeSetupService.querySingleTradeSetup(setup);
			} catch (Exception e) {
				logger.error("##################### querySingleTradeSetup() Exception:"+e.getMessage());
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
			return successReusltMap(null).put(ApiResult.API_RESULT,tradeSetup).toJson();
		}
	}
	

	/** 
	* @Description 查询单个订单中心设置,用户数据回填
	* @param  request
	* @param  response
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午11:17:42
	*/
	@RequestMapping(value="/showSingleTradeSetup",/*method=RequestMethod.POST,*/produces="text/html;charset=UTF-8")
	public @ResponseBody
	String showSingleTradeSetup(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){ 
		TradeSetupVO setup = null;
		try {
			setup = parseJsonToVO(params);
		} catch (Exception e) {
			logger.error("##################### parseJsonToVO() Exception"+e.getMessage());
			return  exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		//转空认为失败
		if(setup==null) return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		setup.setUid(user.getId());
		setup.setUserId(user.getTaobaoUserNick());
		//搜索参数:任务名称,任务状态;类型,用户名称
		if(setup.getType()==null || "".equals(setup.getType())){
			return  failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(!DICT_MAP.containsKey(setup.getType())){
			return  failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(setup.getId()==null){
			return  failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else{
			TradeSetup tradeSetup = null;
			try {
				//查询
				tradeSetup = tradeSetupService.showSingleTradeSetup(setup);
			} catch (Exception e) {
				logger.error("##################### querySingleTradeSetup() Exception:"+e.getMessage());
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
			return successReusltMap(null).put(ApiResult.API_RESULT,tradeSetup).toJson();
		}
	}
	
	
	/** 
	* @Description 删除订单中心相关设置
	* @param  request
	* @param  response
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午11:39:24
	*/
	@RequestMapping(value="/deleteTradeSetup",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public @ResponseBody
	String deleteTradeSetup(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){ 
		TradeSetupVO setup = null;
		try {
			setup = parseJsonToVO(params);
		} catch (Exception e) {
			logger.error("##################### parseJsonToVO() Exception"+e.getMessage());
			return  exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		//转空认为失败
		if(setup==null) return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		setup.setUid(user.getId());
		setup.setUserId(user.getTaobaoUserNick());
		//必须参数:id,类型;用户名称
		if(setup.getType()==null || "".equals(setup.getType())){
			return	failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(!DICT_MAP.containsKey(setup.getType())){
			return  failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(setup.getId()==null){
			return  failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else{
			try {
				//查询
				tradeSetupService.deleteTradeSetup(setup);
			} catch (Exception e) {
				logger.error("##################### deleteTradeSetup() Exception:"+e.getMessage());
				//DUBBO-不可重试?
				if(e.getCause() instanceof TimeoutException)
					return exceptionReusltMap(ApiResult.TIMEOUT_EXCEPTION).toJson();
				//DUBBO-不失败可重试?
				if(e.getCause() instanceof RemotingException)
					return exceptionReusltMap(ApiResult.REMOTING_EXCEPTION).toJson();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
			return successReusltMap(ApiResult.DEL_SET_SUCCESS).toJson();
		}
	}
	

	/** 
	* @Description 打开或关闭订单中心设置
	* @param  request
	* @param  response
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午11:40:37
	*/
	@RequestMapping(value="/switchTradeSetup",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public @ResponseBody
	String switchTradeSetup(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){ 
		TradeSetupVO setup = null;
		try {
			setup = parseJsonToVO(params);
		} catch (Exception e) {
			logger.error("##################### parseJsonToVO() Exception"+e.getMessage());
			return  exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		//转空认为失败
		if(setup==null) return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		setup.setUid(user.getId());
		setup.setUserId(user.getTaobaoUserNick());
		//必须参数:id,类型,状态;用户名称
		if(setup.getType()==null || "".equals(setup.getType())){
			return	failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(!DICT_MAP.containsKey(setup.getType())){
			return	failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(setup.getId()==null){
			return	failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(setup.getStatus()==null){
			return	failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else{
			try {
				if("2".equals(setup.getType()) && setup.getStatus().equals(Boolean.FALSE)){
					long count = tradeSetupService.queryTradeSetupCount(setup.getUid(), "3",Boolean.TRUE);
					if(count>0)
					return failureReusltMap(ApiResult.CLOSE_TWICE_PAYMENT).toJson();
				}
				//打开或者关闭
				tradeSetupService.switchTradeSetup(setup);
				try {
					//添加授权
					tradeSetupService.addUserPermitByMySql(user.getId(), user.getTaobaoUserNick());	
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("打开或者关闭中心设置授权出错");
				}
			} catch (Exception e) {
				logger.error("##################### switchTradeSetup() Exception:"+e.getMessage());
				//DUBBO-不可重试?
				if(e.getCause() instanceof TimeoutException)
					return exceptionReusltMap(ApiResult.TIMEOUT_EXCEPTION).toJson();
				//DUBBO-不失败可重试?
				if(e.getCause() instanceof RemotingException)
					return exceptionReusltMap(ApiResult.REMOTING_EXCEPTION).toJson();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
			return successReusltMap(ApiResult.UPDATE_SUCCESS).toJson();
		}
	}
	
	
	/**
	* @Title: restTradeSetupLevel
	* @Description: (修改订单中心设置的任务级别)
	* @return String    返回类型
	* @author:jackstraw_yu
	* @throws
	*/
	@RequestMapping(value="/resetTradeSetupLevel",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public @ResponseBody
	String resetTradeSetupLevel(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){ 
		TradeSetupVO setup = null;
		try {
			setup = parseJsonToVO(params);
		} catch (Exception e) {
			logger.error("##################### parseJsonToVO() Exception"+e.getMessage());
			return  exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		//转空认为失败
		if(setup==null) return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		setup.setUid(user.getId());
		setup.setUserId(user.getTaobaoUserNick());
		if(setup.getType()==null || "".equals(setup.getType())){
			return	failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(!DICT_MAP.containsKey(setup.getType())){
			return	failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(setup.getId()==null){
			return	failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else if(setup.getTaskLevel()==null){
			return	failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}else{
			try {
				//打开或者关闭
				tradeSetupService.resetTradeSetupLevel(setup);
			} catch (Exception e) {
				logger.error("##################### switchTradeSetup() Exception:"+e.getMessage());
				//DUBBO-不可重试?
				if(e.getCause() instanceof TimeoutException)
					return exceptionReusltMap(ApiResult.TIMEOUT_EXCEPTION).toJson();
				//DUBBO-不失败可重试?
				if(e.getCause() instanceof RemotingException)
					return exceptionReusltMap(ApiResult.REMOTING_EXCEPTION).toJson();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
			return successReusltMap(ApiResult.UPDATE_SUCCESS).toJson();
		}
	}
	
	
	/** 
	* @Description 修改订单中心设置
	* @param  request
	* @param  response
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 下午12:07:00
	*/
	@RequestMapping(value="/updateTradeSetup",method=RequestMethod.POST,produces="text/html;charset=UTF-8")
	public @ResponseBody
	String updateTradeSetup(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){ 
		TradeSetupVO setup = null;
		try {
			setup = parseJsonToVO(params);
		} catch (Exception e) {
			logger.error("##################### parseJsonToVO() Exception"+e.getMessage());
			return  exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		//转空认为失败
		if(setup==null) return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		setup.setUid(user.getId());
		setup.setUserId(user.getTaobaoUserNick());
		try {
			Map<String, Object> map = checkTradeSetup(setup,user,false);
			if(!(Boolean)map.get("status")){
				return  failureReusltMap((String)map.get("msgKey")).toJson();
			}else if(setup.getId()==null){
				return  failureReusltMap(ApiResult.PARAM_WRONG).toJson();
			}else{
				TradeSetup tradeSetup =null;
				//转换
				tradeSetup = convertUpdateSetupData(setup,new TradeSetup());
				//更新??是否返回实体
				tradeSetupService.updateTradeSetup(tradeSetup);
				try {
					//添加授权
					tradeSetupService.addUserPermitByMySql(user.getId(), user.getTaobaoUserNick());	
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("修改订单中心设置授权出错");
				}
				return successReusltMap(ApiResult.UPDATE_SUCCESS).put(ApiResult.API_RESULT, tradeSetup).toJson();
			}
		} catch (Exception e) {
			logger.error("##################### updateTradeSetup() Exception:"+e.getMessage());
			//DUBBO-不可重试?
			if(e.getCause() instanceof TimeoutException)
				return exceptionReusltMap(ApiResult.TIMEOUT_EXCEPTION).toJson();
			//DUBBO-不失败可重试?
			if(e.getCause() instanceof RemotingException)
				return exceptionReusltMap(ApiResult.REMOTING_EXCEPTION).toJson();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
	}
	
//======================================私有方法
	
	/** 
	* @Description String装换成TradeSetupVO对象
	* @param  params
	* @return TradeSetupVO    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午4:10:22
	*/
	private TradeSetupVO parseJsonToVO(String params){
		TradeSetupVO setup = null;
		if(params!=null){
			JSONObject parseObject = JSON.parseObject(params);
			String param = parseObject.getString("params");
			if(param==null || "".equals(param)) return setup;
			setup = JsonUtil.fromJson(param,TradeSetupVO.class);
			//setup = JSON.parseObject(object, TradeSetupVO.class);
		}
		return setup;
	}
	
	/** 
	* @Description 验证用户保存的订单中心的设置&非必须字段值赋空值!!
	* @param  sourceVO
	* @param  user
	* @param  control
	* @return Map<String,Object>    返回类型 
	* @author jackstraw_yu
	* @throws Exception 
	* @date 2018年1月23日 下午4:29:08
	*/
	private Map<String,Object> checkTradeSetup(TradeSetupVO sourceVO,UserInfo user,boolean control) throws Exception{
		if(sourceVO.getType()==null || "".equals(sourceVO.getType()) || !DICT_MAP.containsKey(sourceVO.getType()))
			return resultMap(false,ApiResult.SET_TYPE_ERROR);//设置类型错误!
		if(sourceVO.getStatus()==null)
			return resultMap(false,ApiResult.SET_STATUS_NULL);//设置状态错误!
		//短信内容不能过长,大于500字
		if(sourceVO.getSmsContent()==null || "".equals(sourceVO.getSmsContent())){
			return resultMap(false,ApiResult.SMS_CONTENT_NULL);//短信内容不能为空!
		}else{
			String shopName  = queryShopName(sourceVO.getUid());
			if(!checkSmsContentLength(sourceVO.getSmsContent(),shopName==null?"【】":"【"+shopName+"】","退订回N"))
				return resultMap(false,ApiResult.SMS_CONTENT_TOO_LONG);//短信内容不能大于500字!
		}
		//仅仅针对自动评价
		if(OrderSettingInfo.AUTO_RATE.equals(sourceVO.getType())){
			if(sourceVO.getDelayEvaluate()==null||sourceVO.getEvaluateBlack()==null)
				return resultMap(false,ApiResult.OPTION_NO_SELECT);//必选项不能为空!
			if(!sourceVO.getDelayEvaluate())sourceVO.setDelayDate(null);
			if(sourceVO.getEvaluateBlack()){ 
				sourceVO.setEvaluateBlackType(null);
				sourceVO.setEvaluateBlackContent(null);
			}else{
				if(sourceVO.getEvaluateBlackContent()!=null && "".equals(sourceVO.getEvaluateBlackContent()) 
				   && sourceVO.getEvaluateBlackContent().length()>500)
					return resultMap(false,ApiResult.EVALUATE_BLACK_CONTENT_TOO_LONG);//黑名单评价内容不能大于500字!
			}
		}else if(OrderSettingInfo.APPRAISE_MONITORING_ORDER.equals(sourceVO.getType())){
			if(sourceVO.getTimeOutInform()==null)
				return resultMap(false,ApiResult.OPTION_NO_SELECT);//必选项不能为空!
			if(sourceVO.getInformMobile()!=null && !"".equals(sourceVO.getInformMobile())){
				String[] mobiles = sourceVO.getInformMobile().split(",|，");
				if(mobiles!=null && mobiles.length>=1){
					for (String m : mobiles) {
						if(!MobileRegEx.validateMobile(m))
							return resultMap(false,ApiResult.INFORM_MOBILE_ERROR);//通知号码填写错误!
					}
				}else{
					return resultMap(false,ApiResult.INFORM_MOBILE_ERROR);//通知号码填写错误!
				}
			}
		}else{
			//卖家标记(屏蔽/不屏蔽),指定商品(指定/排除指定) vo.getSellerRemark()==null || vo.getProductType()==null
			//执行类型(持续开启/定时开启),超出时间(不发送/次日发送)
			if(sourceVO.getExecuteType()==null || sourceVO.getTimeOutInform()==null)
				return resultMap(false,ApiResult.OPTION_NO_SELECT);//必选项不能为空!
			//定时开启,时间不能为空
			if(sourceVO.getExecuteType() == null 
				|| (!sourceVO.getExecuteType() 
				&& ( sourceVO.getMinExecuteTime()==null || sourceVO.getMaxExecuteTime()==null
					 || sourceVO.getMaxExecuteTime().before(sourceVO.getMinExecuteTime()) 
					) )
			  )
				return resultMap(false,ApiResult.EXECUTE_TIME_ERROR);//时间为空或错误!
			//立即执行
			if(sourceVO.getExecuteType()){
				 sourceVO.setMinExecuteTime(null);
				 sourceVO.setMaxExecuteTime(null); 
			}
		}
		if(BLOCK_LIST.contains(sourceVO.getType())){
			if(sourceVO.getTradeBlock()==null)
				return resultMap(false,ApiResult.TRDE_BLOCK_NULL);//发送短信订单范围不能为空!
		}else{
			sourceVO.setTradeBlock(null);
			sourceVO.setChosenTime(null);
		}
		//通知时间&排除通知时间单独验证
		Map<String,Object> result =  checkInformTime(sourceVO);
		if(!(Boolean)result.get("status"))
			return resultMap(false,(String)result.get("msgKey"));
		if(REMIND_LIST.contains(sourceVO.getType()))
			if((sourceVO.getTimeType() !=null && sourceVO.getRemindTime()==null)
				|| (sourceVO.getTimeType() ==null && sourceVO.getRemindTime()!=null)
			  )
				return resultMap(false,ApiResult.INFORM_TIME_OPTION_PARAM_ERROR);//通知时间参数错误!
		//仅保存时调用调用该校验时:
		if(control){
			//多任务允许创建最多20个
			long count = tradeSetupService.queryTradeSetupCount(sourceVO.getUid(), sourceVO.getType(),null);
			if(MULTI_LIST.contains(sourceVO.getType())){
				if(count>=20)
					return resultMap(false,ApiResult.SET_TOO_MANY);//设置数量达到上限!
			}else{
				if(count>=1)
					return resultMap(false,ApiResult.THIS_TYPE_SET_EXIST);//该类型设置已经存在!
				sourceVO.setTaskLevel(null);
				sourceVO.setTaskName(null);
			}
		}
		if(MULTI_LIST.contains(sourceVO.getType())){
			//多任务名称的任务是任务名称不能相同
			if(sourceVO.getTaskName()==null || "".equals(sourceVO.getTaskName()))
				return resultMap(false,ApiResult.SET_NAME_NULL);//该设置的任务名称不能为空!
			TradeSetup setup = tradeSetupService.queryTradeSetupByTaskName(sourceVO);
			//保存或者修改校验逻辑不一致
			//1保存时num查询结果只能为0
			if(control){
				if(setup !=null)
					return resultMap(false,ApiResult.THIS_SET_NAME_EXIST);//该设置的任务名称已存在!
			}else{
			//2修改时通过userId&type&taskName 查询出的结果为空或者两者实体的id相等
				if(setup!=null && !setup.getId().equals(sourceVO.getId()))
					return resultMap(false,ApiResult.THIS_SET_NAME_EXIST);//该设置的任务名称已存在!
			}
			
		}
		//商品数量后者大于前者
		if(sourceVO.getMinProductNum()!=null && sourceVO.getMaxProductNum()!=null
			&&(sourceVO.getMaxProductNum().compareTo(sourceVO.getMinProductNum())==-1))
			return resultMap(false,ApiResult.PRODUCT_NUM_ERROR);//商品数量填写错误!
		if(sourceVO.getMinPayment()!=null && sourceVO.getMaxPayment()!=null
			&& (sourceVO.getMaxPayment().compareTo(sourceVO.getMinPayment())==-1))
			return resultMap(false,ApiResult.PAY_MENT_ERROR);//支付金额填写或错误!
		
		/**
		 * 校验催付-2-常规催付,3-二次催付
		 * 1，关闭常规催付，判断二次催付是否开启，开启，请关闭"二次催付"
		 * 2，开启二次催付，判断常规催付是否开启，未开启，请开启"常规催付"
		 */
		if("2".equals(sourceVO.getType()) && sourceVO.getStatus().equals(Boolean.FALSE)){
			long count = tradeSetupService.queryTradeSetupCount(sourceVO.getUid(), "3",Boolean.TRUE);
			if(count>0)
				return resultMap(false,ApiResult.CLOSE_TWICE_PAYMENT);//请关闭"二次催付"！
		}
		
		if("3".equals(sourceVO.getType()) && sourceVO.getStatus().equals(Boolean.TRUE)){
			long count = tradeSetupService.queryTradeSetupCount(sourceVO.getUid(), "2",Boolean.TRUE);
			if(count<1)
				return resultMap(false,ApiResult.OPEN_GENERAL_PAYMENT);//请开启"常规催付"！
		}
		
		return resultMap(true,null);
	}
	
	/** 
	* @Description: 校验短信长度 不能大于500;未做重复变量的校验(有坑)
	* @param  message
	* @param  prefix
	* @param  suffix
	* @return boolean    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午4:30:50
	*/
	private boolean checkSmsContentLength(String message,String prefix,String suffix){
		int length = prefix.length()+suffix.length();
		for (String key : PARAM_MAP.keySet()) {
			if(message.contains(key)){
				message = message.replace(key, "");
				length += PARAM_MAP.get(key);
			}
		}
		length += message.length();
		if(length>500)
			return false;
		return true;
	}
	
	
	/** 
	* @Description 校验订单中心的通知时间和排除时间段是否存在冲突
	* @param  source
	* @param @throws Exception    设定文件 
	* @return Map<String,Object>    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午4:40:06
	*/
	private Map<String,Object> checkInformTime(TradeSetupVO source) throws Exception{
		Date minInformTime =  regexParseTime(source.getMinInformTime());
		Date maxInformTime = regexParseTime(source.getMaxInformTime());
		Date minPrimaryInformTime = regexParseTime(source.getMinPrimaryInformTime());
		Date maxPrimaryInformTime = regexParseTime(source.getMaxPrimaryInformTime());
		Date minMiddleInformTime = regexParseTime(source.getMinMiddleInformTime());
		Date maxMiddleInformTime =  regexParseTime(source.getMaxMiddleInformTime());
		Date minSeniorInformTime = regexParseTime(source.getMinSeniorInformTime());
		Date maxSeniorInformTime = regexParseTime(source.getMaxSeniorInformTime());
		if(minInformTime!=null){
			if(maxInformTime == null || maxInformTime.before(minInformTime))
				return resultMap(false,ApiResult.INFORM_TIME_ERROR);//通知时间填写错误!
			//fromTime判断
			if(minPrimaryInformTime!=null && minPrimaryInformTime.before(minInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			if(minMiddleInformTime!=null && minMiddleInformTime.before(minInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			
			if(minSeniorInformTime!=null && minSeniorInformTime.before(minInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);	//排除时间填写错误!
			//toTime判断
			if(maxPrimaryInformTime!=null && maxPrimaryInformTime.before(minInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			if(maxMiddleInformTime!=null && maxMiddleInformTime.before(minInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			if(maxSeniorInformTime!=null && maxSeniorInformTime.before(minInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
		}
		if(maxInformTime!=null){
			if(minInformTime ==null ||  maxInformTime.before(minInformTime))
				return resultMap(false,ApiResult.INFORM_TIME_ERROR);//通知时间填写错误!
			//fromTime
			if(maxPrimaryInformTime!=null && maxInformTime.before(maxPrimaryInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			if(maxMiddleInformTime!=null && maxInformTime.before(maxMiddleInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			if(maxSeniorInformTime!=null && maxInformTime.before(maxSeniorInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			//toTime
			if(minPrimaryInformTime!=null && maxInformTime.before(minPrimaryInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			if(minMiddleInformTime!=null && maxInformTime.before(minMiddleInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			if(minSeniorInformTime!=null && maxInformTime.before(minSeniorInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
		}
		//======================================================
		//	自身比较或者排除时间是否交叉
		//======================================================
		//一,
		if(minPrimaryInformTime!=null && maxPrimaryInformTime!=null){
			if(maxPrimaryInformTime.before(minPrimaryInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			//校验时间是否交叉
			if(minMiddleInformTime!=null && maxMiddleInformTime !=null)
				if(!checkOverlapTime(minPrimaryInformTime,maxPrimaryInformTime,
						minMiddleInformTime,maxMiddleInformTime))
					return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			
			if(minSeniorInformTime!=null && maxSeniorInformTime!=null)
				if(!checkOverlapTime(minPrimaryInformTime,maxPrimaryInformTime,
						minSeniorInformTime,maxSeniorInformTime))
					return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
		}
		//时间段必须是关闭
		if((minPrimaryInformTime !=null && maxPrimaryInformTime ==null) 
			|| (minPrimaryInformTime ==null && maxPrimaryInformTime !=null))
			return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
		//二,
		if(minMiddleInformTime!=null && maxMiddleInformTime !=null){
			if(maxMiddleInformTime.before(minMiddleInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			//校验时间是否交叉
			if(minPrimaryInformTime!=null && maxPrimaryInformTime !=null)
				if(!checkOverlapTime(minMiddleInformTime,maxMiddleInformTime,
						minPrimaryInformTime,maxPrimaryInformTime))
					return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			if(minSeniorInformTime!=null && maxSeniorInformTime!=null)
				if(!checkOverlapTime(minMiddleInformTime,maxMiddleInformTime,
						minSeniorInformTime,maxSeniorInformTime))
					return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
		}
		//时间段必须是关闭
		if((minMiddleInformTime !=null && maxMiddleInformTime ==null) 
				|| (minMiddleInformTime ==null && maxMiddleInformTime !=null))
			return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
		//三,
		if(minSeniorInformTime!=null && maxSeniorInformTime!=null){
			if(maxSeniorInformTime.before(minSeniorInformTime))
				return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			//校验时间是否交叉
			if(minPrimaryInformTime!=null && maxPrimaryInformTime !=null)
				if(!checkOverlapTime(minSeniorInformTime,maxSeniorInformTime,
						minPrimaryInformTime,maxPrimaryInformTime))
					return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
			if(minSeniorInformTime!=null && maxSeniorInformTime!=null)
				if(!checkOverlapTime(minSeniorInformTime,maxSeniorInformTime,
						minMiddleInformTime,maxMiddleInformTime))
					return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
		}
		//时间段必须是关闭
		if((minSeniorInformTime !=null && maxSeniorInformTime ==null) 
				|| (minSeniorInformTime ==null && maxSeniorInformTime !=null))
			return resultMap(false,ApiResult.EXCEPT_TIME_ERRO);//排除时间填写错误!
		return  resultMap(true,null);
	}
	
	/** 
	* @Description 校验排时间是否交叉
	* @param  minSourceTime
	* @param  maxSourceTime
	* @param  minTargetTime
	* @param  maxTargetTime
	* @return boolean    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午4:46:15
	*/
	private boolean checkOverlapTime(Date minSourceTime,Date maxSourceTime,Date minTargetTime,Date maxTargetTime){
		if(minSourceTime.after(minTargetTime) && minSourceTime.before(maxTargetTime))
			return false;
		if(maxSourceTime.after(minTargetTime) && maxSourceTime.before(maxTargetTime))
			return false;
		if(minTargetTime.after(minSourceTime) && minTargetTime.before(maxSourceTime))
			return false;
		if(maxTargetTime.after(minSourceTime) && maxTargetTime.before(maxSourceTime))
			return false;
		return true;
	}
	
	/** 
	* @Description 解析时:分:秒是否符合规则,不符合直接抛异常
	* @param  time
	* @return Date    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午4:46:50
	*/
	private Date regexParseTime(String time) throws Exception{
		if(time!=null && !"".equals(time)){
			logger.info("##################### TradeSetupController.regexParseTime() Parameter:"+time);
			if(!regexTime.matcher(time).matches()){
				throw  new Exception("##################### TradeSetupController.regexParseTime() Exception: Invalid argument");
			}else{
				return formatter.parse(time);
			}
		}
		return null;
	}
	
	
	
	
	/** 
	* @Description 返回验证结果
	* @param  status
	* @param  msgKey
	* @return Map<String,Object>    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午4:18:16
	*/
	private Map<String,Object> resultMap(Boolean status,String msgKey){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("status", status);
		map.put("msgKey", msgKey);
		return map;
	}
	
	/** 
	* @Description 转换订单中心设置保存的数据
	* @param  source
	* @param  target
	* @return TradeSetup    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午5:51:13
	* @throws Exception    设定文件 
	*/
	private TradeSetup convertSaveSetupData(TradeSetupVO source,TradeSetup target) throws Exception{
		//属性copy
		MyBeanUtils.copyProperties(target, source);
		target.setCreatedBy(source.getUserId());
		target.setLastModifiedBy(source.getUserId());
		target.setCreatedDate(new Date());
		target.setLastModifiedDate(new Date());
		target.setUserId(source.getUserId());
		if(source.getExecuteType()!=null)
			if(source.getExecuteType()){
				//为真:立即执行,置空
				target.setMinExecuteTime(null);
				target.setMaxExecuteTime(null);
			}else{
				target.setMinExecuteTime(source.getMinExecuteTime());
				target.setMaxExecuteTime(source.getMaxExecuteTime());
			}
		//各种set/get
//		target.setProvince(source.getProvince());
//		target.setBadEvaluateInform(source.getBadEvaluateInform());
//		target.setDelayDate(source.getDelayDate());
//		target.setDelayEvaluate(source.getDelayEvaluate());
//		target.setEvaluateBlack(source.getEvaluateBlack());
//		target.setEvaluateBlackContent(source.getEvaluateBlackContent());
//		target.setEvaluateBlackType(source.getEvaluateBlackType());
//		target.setEvaluateType(source.getEvaluateType());
//		target.setExecuteType(source.getExecuteType());
//		target.setFilterBlack(source.getFilterBlack());
//		target.setFilterOnce(source.getFilterOnce());
//		target.setMaxExecuteTime(source.getMaxExecuteTime());
//		target.setMaxInformTime(source.getMaxInformTime());
//		target.setMaxPayment(source.getMaxPayment());
//		target.setMaxProductNum(source.getMaxProductNum());
//		target.setMemberLevel(source.getMemberLevel());
//		target.setMinExecuteTime(source.getMinExecuteTime());
//		target.setMinInformTime(source.getMinInformTime());
//		target.setMinPayment(source.getMinPayment());
//		target.setMinProductNum(source.getMinProductNum());
//		target.setMinProductNum(source.getMinProductNum());
//		target.setNeutralEvaluateInform(source.getNeutralEvaluateInform());
//		target.setProducts(source.getProducts());
//		target.setProductType(source.getProductType());
//		target.setSellerFlag(source.getSellerFlag());
//		target.setSellerRemark(source.getSellerRemark());
//		target.setSmsContent(source.getSmsContent());
//		target.setStatus(source.getStatus());
//		target.setTaskLevel(source.getTaskLevel());
//		target.setTaskName(source.getTaskName());
//		target.setTime(source.getTime());
//		target.setTimeOutInform(source.getTimeOutInform());
//		target.setTimeType(source.getTimeType());
//		target.setTradeFrom(source.getTradeFrom());
//		target.setType(source.getType());
//		target.setUid(source.getUid());
		return target;
	}
	
	
	/** 
	* @Description 转换订单中心跟新数据
	* @param  source
	* @param  target
	* @param @throws Exception    设定文件 
	* @return TradeSetup    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午6:21:06
	*/
	private TradeSetup convertUpdateSetupData(TradeSetupVO source,TradeSetup target) throws Exception{
		MyBeanUtils.copyProperties(target, source);
		target.setLastModifiedBy(source.getUserId());
		target.setLastModifiedDate(new Date());
		target.setId(source.getId());
		if(source.getExecuteType()!=null)
			if(source.getExecuteType()){
				//为真:立即执行,置空
				target.setMinExecuteTime(null);
				target.setMaxExecuteTime(null);
			}else{
				target.setMinExecuteTime(source.getMinExecuteTime());
				target.setMaxExecuteTime(source.getMaxExecuteTime());
			}
		return 	target;
	}
	
	
	
	/**
	* @Title: queryShopName
	* @Description: (获取店铺签名,签名为空时 以店铺昵称为准)
	* @return String    返回类型
	* @author:jackstraw_yu
	* @throws
	*/
	private String queryShopName(Long uid){
    	String shopName=null;
    	shopName = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.SHOP_NAME_CACHE, 
    			RedisConstant.RediskeyCacheGroup.SHOP_NAME_KEY+uid);
    	if(shopName==null || "".equals(shopName)){
    		UserInfo user=new UserInfo();
    		user.setId(uid);
    		shopName = userInfoService.queryShopName(user);
    		if(shopName!=null && !"".equals(shopName))
   			cacheService.putNoTime(RedisConstant.RedisCacheGroup.SHOP_NAME_CACHE, 
    					RedisConstant.RediskeyCacheGroup.SHOP_NAME_KEY+uid,shopName,false);
    	}
    	return shopName;
    }
	

	/** 
	* @Description 生成一个设置名称:汉字名称+年月日时分秒
	* @param  type
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午6:22:16
	*/
	@SuppressWarnings("unused")
	private String returnTaskName(String type){
		String name = null;
		if(DICT_MAP.containsKey(type)){
			name = DICT_MAP.get(type);
		}else{
			name = "";
		}
		return name+DateUtils.formatDate(new Date(),DateUtils.FORMAT_YYYYMMDDHHMMSS);
   }
	
	/**
	 * 查询用户所有商品分组名称(商品缩写)
	 * @Title: listItemGroupName 
	 * @param @param params uid:用户主键id
	 * @param @param request
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws,produces="text/html;charset=UTF-8"
	 */
	@RequestMapping(value="/listGroupName")
	@ResponseBody
	public String listItemGroupName(HttpServletRequest request,HttpServletResponse response){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null){
			return rsMap(102, "操作失败，请重新操作或联系管理员").put("status", false).toJson();
		}
		List<CommodityGrouping> itemGroups;
		try {
			itemGroups = commodityGroupingService.listUidItemGroup(userInfo.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("data", itemGroups);
		return resultMap.toJson();
	}
	
	/**
	 * 查询商品列表分页(商品缩写)
	 * @Title: limitListItem 
	 * @param @param params
	 * @param @param request
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws,produces="text/html;charset=UTF-8"
	 */
	@RequestMapping(value="/listItem")
	@ResponseBody
	public String limitListItem(@RequestBody String params,HttpServletRequest request,
			HttpServletResponse response){
		ItemVO itemVO = null;
		try {
			JSONObject parseObject = JSON.parseObject(params);
			String object = parseObject.getString("params");
			itemVO = JSON.parseObject(object, ItemVO.class);
		} catch (Exception e) {
			logger.info("~~~~~~~~~~~~~~~~~~~json转对象异常");
			e.printStackTrace();
			return rsMap(102, "操作失败，请重新操作或联系管理员").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(itemVO == null){
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		itemVO.setUid(userInfo.getId());
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		List<Item> itemList = itemService.limitItemList(userInfo.getId(),itemVO);
		Integer itemCount = itemService.countItemByTitle(userInfo.getId(),itemVO);
		int totalPage = GetCurrentPageUtil.getTotalPage(itemCount, ConstantUtils.PAGE_SIZE_MIN);
		return resultMap.put("data", itemList).put("totalPage", totalPage).put("pageNo", itemVO.getPageNo()).toJson();
	}
	
	/**
	 * 更改商品缩写(商品缩写)
	 * @Title: updateItemSubtitle 
	 * @param @param params
	 * @param @param request
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws,produces="text/html;charset=UTF-8"
	 */
	@RequestMapping(value="/updateSubtitle")
	@ResponseBody
	public String updateItemSubtitle(@RequestBody String params,HttpServletRequest request,
			HttpServletResponse response){
		ItemVO itemVO = null;
		try {
			JSONObject parseObject = JSON.parseObject(params);
			String object = parseObject.getString("params");
			itemVO = JSON.parseObject(object, ItemVO.class);
		} catch (Exception e) {
			logger.info("~~~~~~~~~~~~~~~~~~~json转对象异常");
			e.printStackTrace();
			return rsMap(102, "操作失败，请重新操作或联系管理员").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(itemVO == null || userInfo == null || userInfo.getId() == null){
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		
		String subtitle = itemVO.getSubtitle();
		Long itemId = itemVO.getNumIid();
		try {
			itemService.updateSubtitleById(userInfo.getId(), itemId, subtitle);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败，请重新操作或联系管理员").put("status", false).toJson();
		}
		
		return rsMap(100, "操作成功").put("status", true).toJson();
	}	
//	
//	private <T> T copyPerproties(TradeDTO source,Class<T> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException{
//		Object instance = clazz.newInstance();
//		BeanUtils.copyProperties(source, instance);
//		return (T) instance;
//	}
//	public void method() throws IllegalAccessException, InvocationTargetException, InstantiationException{
//		MemberInfo member = copyPerproties(new TradeDTO(),MemberInfo.class);
//	}
}
