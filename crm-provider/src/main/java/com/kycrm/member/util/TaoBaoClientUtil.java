package com.kycrm.member.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.TmcInfo;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.BillRecordDto;
import com.taobao.api.domain.IncomeConfirmDto;
import com.taobao.api.domain.Trade;
import com.taobao.api.request.CrmServiceChannelShortlinkCreateRequest;
import com.taobao.api.request.FuwuSpBillreordAddRequest;
import com.taobao.api.request.FuwuSpConfirmApplyRequest;
import com.taobao.api.request.JushitaJdpUserAddRequest;
import com.taobao.api.request.LogisticsTraceSearchRequest;
import com.taobao.api.request.OpenuidGetBytradeRequest;
import com.taobao.api.request.OpenuidGetRequest;
import com.taobao.api.request.TmcGroupAddRequest;
import com.taobao.api.request.TmcGroupDeleteRequest;
import com.taobao.api.request.TmcUserCancelRequest;
import com.taobao.api.request.TmcUserGetRequest;
import com.taobao.api.request.TmcUserPermitRequest;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.request.TraderateAddRequest;
import com.taobao.api.request.TradesSoldIncrementGetRequest;
import com.taobao.api.request.UserSellerGetRequest;
import com.taobao.api.request.VasSubscribeGetRequest;
import com.taobao.api.response.CrmServiceChannelShortlinkCreateResponse;
import com.taobao.api.response.JushitaJdpUserAddResponse;
import com.taobao.api.response.LogisticsTraceSearchResponse;
import com.taobao.api.response.OpenuidGetBytradeResponse;
import com.taobao.api.response.OpenuidGetResponse;
import com.taobao.api.response.TmcGroupAddResponse;
import com.taobao.api.response.TmcGroupDeleteResponse;
import com.taobao.api.response.TmcUserCancelResponse;
import com.taobao.api.response.TmcUserGetResponse;
import com.taobao.api.response.TradeFullinfoGetResponse;
import com.taobao.api.response.TraderateAddResponse;
import com.taobao.api.response.TradesSoldIncrementGetResponse;
import com.taobao.api.response.UserSellerGetResponse;
import com.taobao.api.response.VasSubscribeGetResponse;

/**
 * 淘宝api工具类
 * @author zhrt2
 *
 */
public class TaoBaoClientUtil {
	private TaoBaoClientUtil(){};
	private static Logger logger = LoggerFactory.getLogger(TaoBaoClientUtil.class);
	public static final TaobaoClient TAOBAO_CLIENT = new DefaultTaobaoClient(Constants.TAOBAO_URL,Constants.TOP_APP_KEY, Constants.TOP_APP_SECRET);
	/**
	 * 根据tid和用户的sessionKey查询出对应的主订单信息<br>
	 * api网址:http://open.taobao.com/docs/api.htm?spm=a219a.7395905.0.0.FKc4Uq&apiId=54
	 * @param tid 主订单id
	 * @param sessionKey 登录密钥
	 * @return 如果查询到 返回trade对象 如果查询不到 返回null
	 */
	public static Trade getTradeByTaoBaoAPI(Long tid,String sessionKey){
		if(tid!=null && sessionKey!=null){
			try {
				logger.debug("调用了api订单查询  tid:   "+ tid);
				TaobaoClient client = TaoBaoClientUtil.TAOBAO_CLIENT;
				TradeFullinfoGetRequest req = new TradeFullinfoGetRequest();
				req.setFields("tid,seller_nick,trade_from,order_from,payment,seller_rate,receiver_name,receiver_state,receiver_address,receiver_mobile,receiver_phone,consign_time,receiver_country,receiver_town,num,num_iid,status,type,price,total_fee,created,pay_time,modified,end_time,seller_flag,buyer_nick,mark_desc,buyer_rate,receiver_city,receiver_district,orders,step_trade_status");
				req.setTid(tid);
				TradeFullinfoGetResponse rsp;
				rsp = client.execute(req, sessionKey);
				String subCode = rsp.getSubCode();
				if(subCode !=null && subCode.equals("invalid-sessionkey")){
					return null;
				}
				Trade trade = rsp.getTrade();
				if(trade==null)
					return null;
				//判断买家昵称是否为密文，如果是则解密
				if(EncrptAndDecryptClient.isEncryptData(trade.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
					trade.setBuyerNick(EncrptAndDecryptClient.getInstance().decrypt(trade.getBuyerNick(),EncrptAndDecryptClient.SEARCH,sessionKey));
				}
				//判断买家手机号是否为密文，如果是则解密
				if(EncrptAndDecryptClient.isEncryptData(trade.getReceiverMobile(), EncrptAndDecryptClient.PHONE)){
					trade.setReceiverMobile(EncrptAndDecryptClient.getInstance().decrypt(trade.getReceiverMobile(),EncrptAndDecryptClient.PHONE,sessionKey));
				}
				//判断买家座机是否为密文，如果是则解密
				if(EncrptAndDecryptClient.isEncryptData(trade.getReceiverPhone(), EncrptAndDecryptClient.SIMPLE)){
					trade.setReceiverPhone(EncrptAndDecryptClient.getInstance().decrypt(trade.getReceiverPhone(),EncrptAndDecryptClient.SIMPLE,sessionKey));
				}
				//判断买家姓名是否为密文，如果是则解密
				if(EncrptAndDecryptClient.isEncryptData(trade.getReceiverName(), EncrptAndDecryptClient.SEARCH)){
					trade.setReceiverName(EncrptAndDecryptClient.getInstance().decrypt(trade.getReceiverName(),EncrptAndDecryptClient.SEARCH,sessionKey));
				}
				//判断收货人街道地址是否为密文，如果是则解密
				if(EncrptAndDecryptClient.isEncryptData(trade.getReceiverAddress(), EncrptAndDecryptClient.SEARCH)){
					trade.setReceiverAddress(EncrptAndDecryptClient.getInstance().decrypt(trade.getReceiverAddress(),EncrptAndDecryptClient.SEARCH,sessionKey));
				}
				
				return trade;
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 淘宝自动评价工具类   <br>
	 * api网址：http://open.taobao.com/docs/api.htm?spm=a219a.7395905.0.0.jiDYWd&apiId=56
	 * @param tid 主订单ＩＤ
	 * @param oid 子订单ID
	 * @param sellerNick 卖家昵称
	 * @param content 评价内容
	 * @param type 评价类型  good bad  
	 * @param sessionKey 卖家的sessionKey
	 * @return 评价结果
	 * @throws ApiException
	 */
	public static TraderateAddResponse autoTaobaoTraderate(Long tid,Long oid,String content,String type,String sessionKey){
		TraderateAddResponse rsp = null;
		try {
			TaobaoClient client = TaoBaoClientUtil.TAOBAO_CLIENT;
			TraderateAddRequest req = new TraderateAddRequest();
			req.setTid(tid);
			req.setOid(oid);
			req.setResult(type);
			req.setRole("seller");
			req.setContent(content);
			rsp = client.execute(req, sessionKey);
		} catch (ApiException e) {
			e.printStackTrace();
			logger.info("------------------------------自动评价调用接口异常！！！！----------------------------------");
		}
		if(rsp != null){
			logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~评价成功，结果是："+rsp.getBody()+"IIIIIIIII~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
		return rsp;
	}
	/**
	 * 获取用户是不是在tmc列表监听内
	 * @author: wy
	 * @time: 2017年11月30日 下午3:34:37
	 * @param taobaoUserNick
	 * @return
	 */
	public static boolean getTmcUserTopic(String taobaoUserNick) {
        logger.info("================================<=>查询用户："+taobaoUserNick+"是否在tmc同步列表中================================");
        TmcUserGetRequest req = new TmcUserGetRequest();
        req.setFields("user_nick,topics,user_id,is_valid,created,modified");
        req.setNick(taobaoUserNick);
        req.setUserPlatform("tbUIC");
        TmcUserGetResponse rsp = null;
        try {
            rsp = TaoBaoClientUtil.TAOBAO_CLIENT.execute(req);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        System.out.println("getBody:  ——>  "+rsp.getBody());
        System.out.println("getErrorCode:  ——>  "+rsp.getErrorCode());
        System.out.println("getMsg:  ——>  "+rsp.getMsg());
        System.out.println("getSubCode:  ——>  "+rsp.getSubCode());
        System.out.println("getSubMsg:  ——>  "+rsp.getSubMsg());
        System.out.println("getClass:  ——>  "+rsp.getClass());
        System.out.println("getParams:  ——>  "+rsp.getParams());
        System.out.println("getTmcUser:  ——>  "+rsp.getTmcUser());
        if (rsp.getTmcUser() != null && rsp.getTmcUser().getTopics() != null
                && rsp.getTmcUser().getTopics().size() > 0) {
            return true;
        }
        return false;
    }
	public static void main(String[] args) throws ApiException{
	    //getTmcUserTopic("小白你什么都没看见哦"); 
	    doOpenUserPermit("700021008166785651710057fb091a0f15e2d8a8798c35cd65e9488182ab7eac3ea772e60336516", TmcInfo.LOGSTIC_DETAIL_TOPIC);
	   // getTmcUserTopic("奇声宇博专卖店");
	    System.out.println(System.getProperties().getProperty("os.name"));
	    System.out.println(System.getProperties());
	}
	 /**
     * 根据用户的sessionKey和具体的Topic来为用户开启消息
     * @author: wy
     * @time: 2017年8月23日 下午2:59:26
     * @param sessionKey 用户的秘钥
     * @param topics 要为用户授权的消息，如果消息为空，则会强制加上服务开通和服务支付两个消息
     * @return
     */
    public static boolean doOpenUserPermit(String sessionKey,String topics){
        if(ValidateUtil.isEmpty(sessionKey)){
            return false;
        }
        if(ValidateUtil.isEmpty(topics)){
            topics = "";
        }
        if(!topics.contains(TmcInfo.FUWU_SERVICE_OPEN_TOPIC)){
            topics = topics +","+TmcInfo.FUWU_SERVICE_OPEN_TOPIC;
        }
        if(!topics.contains(TmcInfo.FUWU_ORDERPAID_TOPIC)){
            topics = topics +","+TmcInfo.FUWU_ORDERPAID_TOPIC;
        }
        if(topics.startsWith(",")){
            topics = topics.substring(1);
        }
        TmcUserPermitRequest req = new TmcUserPermitRequest();
        req.setTopics(topics);
        try {
            TaoBaoClientUtil.TAOBAO_CLIENT.execute(req, sessionKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    } 
    
 
	/**
    * 根据用户昵称和应用服务code获取用户应用的到期时间
    * @param taobao_user_nick
    * @return
    */
	public static Date getUserExpireTimeByTaobao(String taobaoUserNick) {
	       VasSubscribeGetRequest req = new VasSubscribeGetRequest();
	       req.setArticleCode(Constants.TOP_APP_CODE);
	       req.setNick(taobaoUserNick);
	       VasSubscribeGetResponse rsp = null;
	       try {
	           rsp = TaoBaoClientUtil.TAOBAO_CLIENT.execute(req);
	       } catch (Exception e) {
	           e.printStackTrace();
	       }
	       if (rsp.getArticleUserSubscribes() != null) {
	           int timeCount = rsp.getArticleUserSubscribes().size();
	           return rsp.getArticleUserSubscribes().get(timeCount - 1)
	                   .getDeadline();
	       }
	       return null;
	   }
	 /**
     * 添加新用户到聚石塔
     * @author: wy
     * @time: 2017年11月14日 下午3:29:33
     * @param token
     */
    public static void addJstUser(String token) {
        JushitaJdpUserAddRequest req = new JushitaJdpUserAddRequest();
        req.setRdsName(Constants.RDS_MYSQL_ACCOUNT);
        req.setTopics("item,trade");
        req.setHistoryDays(90L);
        JushitaJdpUserAddResponse rsp = null;
        try {
            rsp = TaoBaoClientUtil.TAOBAO_CLIENT.execute(req, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("聚石塔结果为："+rsp.getBody());
    }
	/**
	 * 为已开通用户添加用户分组     <br>
	 * api网址：http://open.taobao.com/docs/api.htm?spm=a219a.7395905.0.0.09RfdX&apiId=21983
	 * @author: wy
	 * @time: 2017年10月10日 上午11:32:05
	 * @param groppName 分组名称
	 * @param sellerNicks 卖家昵称
	 * @return true：添加成功，false：添加失败
	 */
	public static boolean doTmcGroupAdd(String groppName,String sellerNicks){
		try {
			TaobaoClient client = TaoBaoClientUtil.TAOBAO_CLIENT;
			TmcGroupAddRequest req = new TmcGroupAddRequest();
			req.setGroupName(groppName);
			req.setNicks(sellerNicks);
			TmcGroupAddResponse  rsp = client.execute(req);
			String msg = rsp.getMsg();
			if("Remote service error".equals(msg) || msg ==null){
				return false;
			}
			return true;
		} catch (ApiException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 删除指定分组下的用户     <br>
	 * api网址：http://open.taobao.com/docs/api.htm?spm=a219a.7395905.0.0.xJ2eyr&apiId=21982
	 * @author: wy
	 * @time: 2017年9月25日 下午5:51:32
	 * @param groppName 用户分组
	 * @param sellerNicks 买家昵称集合
	 * @return 成功返回true，失败返回false
	 */
	public static boolean doTmcGroupDelete(String groppName,String sellerNicks){
		try {
			TaobaoClient client = TaoBaoClientUtil.TAOBAO_CLIENT;
			TmcGroupDeleteRequest req = new TmcGroupDeleteRequest();
			req.setGroupName(groppName);
			req.setNicks(sellerNicks);
			TmcGroupDeleteResponse rsp = client.execute(req);
			String msg = rsp.getMsg();
			if("Remote service error".equals(msg) || msg ==null){
				return false;
			}
			return true;
		} catch (ApiException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 为已授权的用户开通消息服务,根据用户的sessionKey和具体的Topic来为用户开启消息 <br>
	 * api网址：http://open.taobao.com/docs/api.htm?spm=a219a.7395905.0.0.FOPKkl&apiId=21990
	 * @author: wy
	 * @time: 2017年8月23日 下午2:59:26
	 * @param sessionKey 用户的秘钥
	 * @param topics 要为用户授权的消息，如果消息为空，则会强制加上服务开通和服务支付两个消息
	 * @return 成功返回true，失败返回false
	 */
	public static  boolean doTmcUserPermit(String sessionKey,String topics){
		TaobaoClient client = TaoBaoClientUtil.TAOBAO_CLIENT;
		if(ValidateUtil.isEmpty(sessionKey)){
			return false;
		}
		if(ValidateUtil.isEmpty(topics)){
			topics = "";
		}
		if(!topics.contains(TmcInfo.FUWU_SERVICE_OPEN_TOPIC)){
			topics = TmcInfo.FUWU_SERVICE_OPEN_TOPIC + topics;
		}
		if(!topics.contains(TmcInfo.FUWU_ORDERPAID_TOPIC)){
			topics = TmcInfo.FUWU_ORDERPAID_TOPIC + topics;
		}
		TmcUserPermitRequest req = new TmcUserPermitRequest();
		req.setTopics(topics);
		try {
			client.execute(req, sessionKey);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	} 
	/**
	 * 取消用户的消息服务 <br>
	 * api网址：http://open.taobao.com/docs/api.htm?spm=a219a.7395905.0.0.fN0XzF&apiId=21989
	 * @author: wy
	 * @time: 2017年10月10日 上午11:50:31
	 * @param sellerNicks 卖家昵称
	 * @return 成功返回true，失败返回false
	 */
	public static boolean doTmcUserCancel(String sellerNicks){
		try {
			TaobaoClient client = TaoBaoClientUtil.TAOBAO_CLIENT;
			TmcUserCancelRequest req = new TmcUserCancelRequest();
			req.setNick("testNick");
			TmcUserCancelResponse rsp = client.execute(req);
			String msg = rsp.getMsg();
			if("Remote service error".equals(msg) || msg ==null){
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 上传充值订单详情
	 * @author: wy
	 * @time: 2017年11月30日 下午3:53:39
	 * @param sellerNick 卖家昵称
	 * @param status 服务充值是否成功
	 * @param mobile 卖家的手机号码
	 * @param sellerNickId 卖家的数据库主键ID
	 * @param rechargePrice 本次服务充值的订单价格
	 * @param rechargeId 服务充值的订单主键ID
	 * @param orderId 服务充值的订单ID
	 * @param sessionKey 用户秘钥
	 * @param rechargeDate 发送交易的时间
	 */
	public static void doFuwuSpBillreordAdd(String sellerNick,String status,String mobile,
	        long sellerNickId,long fee,long rechargeId,String orderId,String sessionKey,Date rechargeDate){
	    //上传充值订单详情
        BillRecordDto billRecordDto = new BillRecordDto();
        billRecordDto.setStartDate(rechargeDate);
        if (status != null) {
            billRecordDto.setStatus(Long.parseLong(status));
        }
        billRecordDto.setAppkey(Constants.TOP_APP_KEY);
        billRecordDto.setType(1L);
        if (ValidateUtil.isEmpty(mobile)) {
            billRecordDto.setTargetNo(mobile);
        } else {
            billRecordDto.setTargetNo(String.valueOf(sellerNickId));
        }
        billRecordDto.setFee(fee);
        billRecordDto.setNick(sellerNick);
        billRecordDto.setOutConfirmId(String.valueOf(rechargeId));
        if (orderId != null) {
            billRecordDto.setOrderId(Long.parseLong(orderId));
        }
        billRecordDto.setOutOrderId(orderId);
        FuwuSpBillreordAddRequest req = new FuwuSpBillreordAddRequest();
        req.setParamBillRecordDTO(billRecordDto);
        try {
            TaoBaoClientUtil.TAOBAO_CLIENT.execute(req, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
	}
	/**
	 * 服务充值订单确认
	 * @author: wy
	 * @time: 2017年11月30日 下午4:07:04
	 * @param sellerNick 买家昵称
	 * @param orderId 服务订单号
	 * @param fee 金额
	 * @param sellerNickId 用户数据库主键id
	 * @param sessionKey 卖家密钥
	 */
	public static void doFuwuSpConfirmApply(String sellerNick,String orderId,long fee,long sellerNickId,String sessionKey){
	    FuwuSpConfirmApplyRequest req1 = new FuwuSpConfirmApplyRequest();
        IncomeConfirmDto obj2 = new IncomeConfirmDto();
        obj2.setFee(fee);
        obj2.setNick(sellerNick);
        obj2.setAppkey(Constants.TOP_APP_KEY);
        if (orderId != null) {
            obj2.setOrderId(Long.parseLong(orderId));
        }
        obj2.setOutConfirmId(String.valueOf(sellerNickId));
        req1.setParamIncomeConfirmDTO(obj2);
        try {
            TaoBaoClientUtil.TAOBAO_CLIENT.execute(req1, sessionKey);
        } catch (ApiException e) {
            e.printStackTrace();
        }
	}
	/**
	 * http://open.taobao.com/docs/api.htm?spm=a219a.7386797.0.0.XhSP2o&source=search&apiId=10463
	 * 获取物流的信息
	 * @author: wy
	 * @time: 2017年11月30日 下午4:17:02
	 * @param sellerNick 卖家昵称
	 * @param tid 主订单号
	 * @return 请求的结果
	 * @throws ApiException
	 */
	public static LogisticsTraceSearchResponse doLogisticsTraceSearch(String sellerNick,Long tid) throws ApiException{
	    TaobaoClient client = TaoBaoClientUtil.TAOBAO_CLIENT;
        LogisticsTraceSearchRequest req = new LogisticsTraceSearchRequest();
        req.setTid(tid);
        req.setIsSplit(0L);
        req.setSellerNick(sellerNick);
        return client.execute(req);
	}
	/**
	 * 校验数据
	 */
	private static List<String> assist = new ArrayList<String>(){
		private static final long serialVersionUID = -7379595965111490439L;
		{
			add("LT_ITEM");add("LT_ACTIVITY");
			add("LT_SHOP");add("LT_TRADE");
		}
	};
	/**
	 * 短链接响应示例
	 *  <crm_service_channel_shortlink_create_response>
	 *   	<short_link>c.tb.cn/DFe3c</short_link>
	 *  </crm_service_channel_shortlink_create_response>
	 *   
	 *	异常示示例
	 *	<error_response>
	 *    	<code>50</code>
	 *    	<msg>Remote service error</msg>
	 *    	<sub_code>isv.invalid-parameter</sub_code>
	 *    	<sub_msg>非法参数</sub_msg>
	 *  </error_response>
	 *	文档访问地址:http://open.taobao.com/docs/api.htm?spm=a219a.7386797.0.0.PygMst&source=search&apiId=25034
	 * 
	 * @Title: creatLink
	 * @Description: 根据类型:<店铺首页,商品,活动,订单>生成相关短链接
	 * @return String  返回类型
	 * @author:jackstraw_yu
	 * @throws
	*/
	public static String creatLink(String token,String linkType,String param){
		if(linkType==null || !assist.contains(linkType))
			return null;
		if(!"LT_SHOP".equals(linkType) && param==null)//短链接为店铺首页时:req.setShortLinkData(null);
			return null;
		CrmServiceChannelShortlinkCreateRequest req = new CrmServiceChannelShortlinkCreateRequest();
		req.setShortLinkName("生成短链接"+DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT));	
		if("LT_ITEM".equals(linkType)){//商品
			req.setLinkType("LT_ITEM");
		}else if ("LT_ACTIVITY".equals(linkType)){//活动
			req.setLinkType("LT_ACTIVITY");
		}else if("LT_SHOP".equals(linkType)){//店铺首页
			req.setLinkType("LT_SHOP");
		}else if("LT_TRADE".equals(linkType)){//订单
			req.setLinkType("LT_TRADE");
		}else{
			return null;
		}
		req.setShortLinkData(param);
		CrmServiceChannelShortlinkCreateResponse rsp = null;
		try {
			rsp = TAOBAO_CLIENT.execute(req, token);
		} catch (Exception e) {
			logger.error("########################TaoBaoClientUtil.creatLink Exception"+e.getMessage());
			return null;
		}
		if(rsp.getMsg()!=null&&rsp.getMsg().equals("Remote service error")){
			if(rsp.getSubMsg()!=null)
				return rsp.getSubMsg();
		}else{
			if(rsp.getShortLink()!=null)
				return rsp.getShortLink();
		}
		return null;
	}
	/**
	 * 通过sessionKey换取用户的唯一id <br>
	 * http://open.taobao.com/docs/api.htm?spm=a219a.7386797.0.0.nTRnrR&source=search&apiId=33220
	 * @author: wy
	 * @time: 2018年1月17日 下午3:25:35
	 * @param sessionKey 用户秘钥
	 * @return 用户在本应用中淘宝给的唯一标识OpenUid
	 */
	public static String getOpenuidGetBySession(String sessionKey){
	    TaobaoClient client = TaoBaoClientUtil.TAOBAO_CLIENT;
	    OpenuidGetRequest req = new OpenuidGetRequest();
	    OpenuidGetResponse rsp;
        try {
            rsp = client.execute(req, sessionKey);
            return rsp.getOpenUid();
        } catch (ApiException e) {
            e.printStackTrace();
            return null;
        }
	}
	/**
	 * 通过订单好获取买家的唯一标识<br>
	 * http://open.taobao.com/docs/api.htm?spm=a219a.7386797.0.0.oL5P2h&source=search&apiId=33221
	 * @author: wy
	 * @time: 2018年1月17日 下午3:28:57
	 * @param tid 订单好
	 * @param sessionKey 卖家秘钥
	 * @return 买家的唯一标识OpenUid
	 */
	public static String getOpenuidGetBytrade(long tid,String sessionKey){
	    if(ValidateUtil.isEmpty(sessionKey) || tid==0){
	        return null;
	    }
	    TaobaoClient client = TaoBaoClientUtil.TAOBAO_CLIENT;
	    OpenuidGetBytradeRequest req = new OpenuidGetBytradeRequest();
	    req.setTid(tid);
	    try {
            OpenuidGetBytradeResponse rsp = client.execute(req, sessionKey);
            return rsp.getOpenUid();
        } catch (ApiException e) {
            e.printStackTrace();
        }
	    return null;
	}
	/**
	 * 获取卖家信息中的卖家等级<br>
	 * http://open.taobao.com/docs/api.htm?spm=a219a.7386797.0.0.lsyh1R&source=search&apiId=21349
	 * @author: wy
	 * @time: 2018年1月17日 下午4:21:09
	 * @param sessionKey 卖家秘钥
	 * @return 卖家在淘宝中的等级
	 */
	public static Long getUserSellerGetLevel(String sessionKey){
	    if(ValidateUtil.isEmpty(sessionKey)){
	        return  null;
	    }
	    TaobaoClient client = TaoBaoClientUtil.TAOBAO_CLIENT;
        UserSellerGetRequest req = new UserSellerGetRequest();
        req.setFields("nick,seller_credit");
        UserSellerGetResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
            return rsp.getUser().getSellerCredit().getLevel();
        } catch (ApiException e) {
            e.printStackTrace();
        }
	    return null;
	}
	
	/**
	 * 查询卖家已卖出的增量交易数据（根据修改时间） <br>
	 * http://open.taobao.com/docs/api.htm?spm=a219a.7386797.0.0.rCfJob&source=search&apiId=128
	 * @author: wy
	 * @time: 2018年1月19日 下午2:26:48
	 * @param sessionKey 卖家秘钥
	 * @param pageNo 页数
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	public static TradesSoldIncrementGetResponse getTradesSoldIncrement(String sessionKey,long pageNo,Date startDate,Date endDate){
	    if(ValidateUtil.isEmpty(sessionKey) || ValidateUtil.isEmpty(sessionKey) || ValidateUtil.isEmpty(sessionKey)){
	        return null;
	    }
	    TaobaoClient client =TaoBaoClientUtil.TAOBAO_CLIENT;
        TradesSoldIncrementGetRequest req = new TradesSoldIncrementGetRequest();
        req.setFields("tid,seller_nick,pic_path,payment,seller_rate,post_fee,receiver_name,receiver_state,receiver_address,receiver_zip,receiver_mobile,receiver_phone,consign_time,received_payment,receiver_country,receiver_town,order_tax_fee,shop_pick,num,num_iid,status,title,type,price,discount_fee,total_fee,created,pay_time,modified,end_time,seller_flag,buyer_nick,has_buyer_message,credit_card_fee,step_trade_status,step_paid_fee,mark_desc,shipping_type,adjust_fee,buyer_rate,receiver_city,receiver_district,orders,rx_audit_status,post_gate_declare,cross_bonded_declare,order_tax_promotion_fee,trade_from");
        req.setStartModified(startDate);
        req.setEndModified(endDate);
        req.setPageNo(pageNo);
        req.setPageSize(50L);
        req.setUseHasNext(true);
        TradesSoldIncrementGetResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
            return rsp;
        } catch (ApiException e) {
            e.printStackTrace();
        }
	    return null;
	}
}
