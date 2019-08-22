package com.kycrm.member.service.synctrade.rate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.core.redis.CacheServicesyn;
import com.kycrm.member.dao.syntrade.member.IMemberDTODaosyn;
import com.kycrm.member.dao.syntrade.tradeSetup.TradeSetupDaosyn;
import com.kycrm.member.dao.syntrade.tradeSetup.TradeSetupExample;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.message.SmsBlackListDTO;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.handler.ITradeSysInfoServiceSyn;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.message.IPushSmsService;
import com.kycrm.member.service.rate.ITradeRatesServiceSyn;
import com.kycrm.member.service.synctrade.message.SmsBlackListDTOServiceImplsyn;
import com.kycrm.member.service.synctrade.order.OrderDTOServiceImplsyn;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
//import com.kycrm.tmc.service.RateMonitoringPacifyService;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.SendMessageStatusInfo;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.SecretException;
import com.taobao.api.domain.TradeRate;
import com.taobao.api.request.TraderatesGetRequest;
import com.taobao.api.response.TraderatesGetResponse;

@Service("tradeRatesServiceImplsyn")
//@Transactional
@MyDataSource(MyDataSourceAspect.MASTER)
public class TradeRatesServiceImplsyn implements ITradeRatesServiceSyn {
	
	private static Logger logger = LoggerFactory.getLogger(TradeRatesServiceImplsyn.class);
	@Autowired
	private CacheServicesyn cacheService;
	@Autowired
	private TradeSetupDaosyn tradeSetupDao;
	@Autowired
	private IMemberDTODaosyn memberDTODao;
	@Autowired
	private SmsBlackListDTOServiceImplsyn smsBlackListDTOService;
//	@Autowired
//	private RateMonitoringPacifyService rateMonitoringPacifyService;
	@Autowired
	private IPushSmsService pushSmsSerive;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IMemberDTOService memberDTOService;
	@Autowired
	private IUserAccountService userAccountService;
	@Autowired
	private TradeRatesSaveTempService tempService;
	@Autowired
	private OrderDTOServiceImplsyn orderDTOService;
	
	final String TEMPLATE_SMS = "【淘宝】亲爱的AaA，你肿么能残酷地给了我们中差评呢。心碎勒~相信您是个善良的银，待会儿跟我们说说怎么回事哦";
	/**
	 * 定时同步评价数据到CRM数据库
	 * @author sungk
	 */
	@Deprecated
	public void startSyncTradeRate(Date startNodeDate){
		try {
			//历史处理数量
//			String totalRateNumStr = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
//					RedisConstant.RediskeyCacheGroup.TOTAL_RATE_DATA_COUNT_KEY);
			
//			List<UserInfo> userInfoList = userInfoDao.findAll();
//			if (ValidateUtil.isEmpty(userInfoList)) {
//				logger.error("*************  rate同步 获取所有用户没有 或者 失败");
//				return;
//			}
//			userInfoList.stream()
//						.filter(x -> x.getId() == 1243l)// TODO 测试
//						.filter(x -> ValidateUtil.isEmpty(x))
//						.collect(Collectors.toList())
//						.forEach(userInfo -> this.getTaobaoRates(userInfo, startNodeDate));
			//本次处理数量
//			Integer i = Optional.ofNullable(userInfoList.size()).orElse(0);
//			
//			//redis 存储节点数据
//			cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE, 
//					RedisConstant.RediskeyCacheGroup.TOTAL_RATE_DATA_COUNT_KEY, 
//					i + Integer.parseInt(Optional.ofNullable(totalRateNumStr).orElse("0")));
//			String nextNodeDate = DateUtils.formatDate(
//					DateUtils.addMinute(startNodeDate, Constants.RATE_SYN_TIME_MINUTE), DateUtils.DEFAULT_TIME_FORMAT);
//			// TODO 测试时候 时间死循环 
//			if (DateUtils.addMinute(startNodeDate, Constants.RATE_SYN_TIME_MINUTE).after(new Date())) {
//				nextNodeDate = "2018-08-20 02:00:00";
//				logger.debug("*************************** 换个时间再来一次 2018-08-20 02:00:00");
//			}
//			//存储下一次节点时间
//			cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE, 
//					RedisConstant.RediskeyCacheGroup.RATE_NODE_LAST_TIME_KEY, nextNodeDate);
//			logger.debug("rate同步 下次节点时间: {}", nextNodeDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取淘宝中差评并存储
	 * @param userInfo
	 */
	@Override
	public void getTaobaoRates(Long uid, Date startDate) {
		
		// 未开启中差评 返回
		if(!this.getRateMoniterStatus(uid)) {
			return;
		}
		logger.debug("**** 评价同步 同步用户uid：{} 开启了评价同步", uid);
		
		//获取中评 并存储
		installReq(OrderSettingInfo.NEUTRAL_RATE, uid, startDate);
		//获取差评 并存储
		installReq(OrderSettingInfo.BAD_RATE, uid, startDate);
	}
	
	/**
	 * 判断用户是否开启了中差评监控
	 * 目前此处查询只是沿用行前项目sql，未进行验证
	 * @author sungk
	 * @param uid
	 * @return true为开启
	 */
	@Override
	public Boolean getRateMoniterStatus(Long uid) {
		if (uid == null) return false;
		TradeSetupExample example = new TradeSetupExample();
		TradeSetupExample.Criteria criteria = example.createCriteria();
		criteria.andUidEqualTo(uid)
				// 16-自动评价,20-中差评监控 ,21-中差评安抚
				.andTypeIn(Arrays.asList("16", "20", "21"))
				.andStatusEqualTo(1)
				.andInUseEqualTo(1);
		Long count = tradeSetupDao.countByExample(example);
		if (count > 0L) return true;
		return false;
	}
	/**
	 * 拉取评价信息设置拉取条件
	 * @author sungk
	 */
	@Override
	public void installReq(String result, Long uid, Date startDate){
		DefaultTaobaoClient taobaoClient = new DefaultTaobaoClient(Constants.TAOBAO_URL, 
				Constants.TOP_APP_KEY, Constants.TOP_APP_SECRET);
		// 获取token
		String accessToken = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE, 
				RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + uid);
		logger.info("**** Rate评价同步 uid:{} 的 accessToken:{}", uid, accessToken);
		if(accessToken==null){
			UserInfo userInfo = userInfoService.findUserInfo(uid);
			accessToken=userInfo.getAccessToken();
			logger.info("从数据库中取出的sessinkey为"+accessToken);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TraderatesGetRequest req = new TraderatesGetRequest();
		// 设置调用接口查询条件
		req.setFields("tid,oid,role,nick,result,created,rated_nick,item_title,item_price,content,reply,num_iid");
		req.setRateType("get"); // 当 get buyer 以卖家身份得到买家给的评价 
		req.setRole("buyer");  
		req.setPageSize(150l);
		req.setResult(result); 
		// 根据评价时间查询
		req.setStartDate(startDate);
//		req.setStartDate(StringUtils.parseDateTime(DateUtils.getTimeByHour(sdf.format(new Date()), -720)));
		req.setEndDate(DateUtils.addMinute(startDate, Constants.RATE_SYN_TIME_MINUTE)); 
		logger.info("**** Rate本次同步时长{}分钟 从 {} 到  {}", Constants.RATE_SYN_TIME_MINUTE,
				sdf.format(startDate), sdf.format(DateUtils.addMinute(startDate, Constants.RATE_SYN_TIME_MINUTE)));
		TraderatesGetResponse tgr = new TraderatesGetResponse();
		try {
			tgr = taobaoClient.execute(req, accessToken);
			logger.info("同步淘宝的评论评价返回的对象为"+tgr);
			if (tgr.getErrorCode() != null) {
				logger.error("**** Rate向淘宝拉取评价消息uid:{} taobao返回错误消息：{}", uid, tgr.getMsg());
				tgr = null;
				accessToken = userInfoService.findUserTokenById(uid);
				tgr = taobaoClient.execute(req, accessToken);
				if (tgr.getErrorCode() != null) {
					logger.error("**** Rate再次从数据库获取session无效 taobao返回错误消息：{}", tgr.getMsg());
				}
			} 
		} catch (ApiException e) {
			e.printStackTrace();
		}
		if(tgr!=null){
			logger.info("从淘宝获取的评评价数量为"+tgr.getTotalResults());	
		}
		//存储评价
		this.saveTradeRates(uid, tgr, accessToken);
		logger.info("**** Rate从taobao获取 {} 的评论条数： {}", result.equals("bad")?"差评":"中评", tgr.getTotalResults());
	}
	
	/**
	 * 保存拉取的淘宝评价信息
	 * @author sungk
	 * 
	 */
	@Override
	public void saveTradeRates(Long uid, TraderatesGetResponse tgr, String session){
		try {
			if(tgr==null|| ValidateUtil.isEmpty(tgr.getTradeRates())){
				logger.info("数据库返回的对象为空,用户id为"+uid+"用户sessionkey为"+session);
				return;
			}
			logger.info("**** Rate搜索到uid:{} 的评价总条数:{} 进行处理存储", uid, tgr.getTotalResults());
			MemberInfoDTO memberInfoDTO = new MemberInfoDTO();
			tgr.getTradeRates().stream()
					.forEach(tradeRate -> {
//						this.saveTradeRateSingle(uid, tradeRate);
						tempService.saveTradeRateSingle(uid, tradeRate);
						this.saveBlackList(uid, tradeRate);
						//在子订单表中添加订单评价结果
						this.updateOrderResult(uid,tradeRate);
						this.updateMemberBlackList(uid, tradeRate, memberInfoDTO);
						logger.info("**** Rate本次订单tid：{} oid：{}",tradeRate.getTid(), tradeRate.getOid());
						logger.info("**** Rate存储uid：{} 的一条 {}评 并加入黑名单", uid, tradeRate.getResult().equals("bad")?"差":"中");
					});
//			rateMonitoringPacifyService.doHandle(uid, tgr.getTradeRates());
			this.rateMonitoringPacify(uid, memberInfoDTO, session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据oid,修改订单的中差评结果
	 * @param uid
	 * @param tradeRate
	 */
	private void updateOrderResult(Long uid, TradeRate tradeRate) {
		OrderDTO orderDTO=new OrderDTO();
		orderDTO.setResult(tradeRate.getResult());
		orderDTO.setOid(tradeRate.getOid());
		orderDTOService.updateOrderResult(uid, orderDTO);
	}

	/**
	 * 修改会员信息的加黑名单
	 * @param uid
	 * @param tradeRate
	 * @param memberInfoDTO
	 */
	@Override
	@MyDataSource
	public void updateMemberBlackList(Long uid, TradeRate tradeRate,MemberInfoDTO memberInfoDTO) {
		//更新会员是否为中差评
		memberInfoDTO.setUid(uid);
		memberInfoDTO.setStatus("2");//会员状态 1-正常 2-黑名单
		memberInfoDTO.setBuyerNick(tradeRate.getNick());
		memberInfoDTO.setNeutralBadRate(true);
		memberDTODao.updateMembeInfo(memberInfoDTO);
	}
	
	/**
	 * 分库保存黑名单 评论同步专用
	 * 通过昵称添加
	 * @param uid
	 * @param tradeRate
	 */
	@Override
	@MyDataSource
	public void saveBlackList(Long uid, TradeRate tradeRate) {
		boolean isExistBlackList = smsBlackListDTOService.isExists(uid, tradeRate.getRatedNick(), tradeRate.getNick(), null);
		if (isExistBlackList) {
			logger.info("**** 该用户uid：{} 已经通过昵称 存在 黑名单中", uid);
			// 存在 更新 --存疑 暂停
			/*SmsBlackListDTO blackListDTO = new SmsBlackListDTO();
			blackListDTO.setNickOrPhone(tradeRate.getNick());
			blackListDTO.setNick(tradeRate.getNick());
			blackListDTO.setType("2"); //黑名单类型 1-手机号 2-客户昵称
			blackListDTO.setCreatedDate(new Date());
			blackListDTO.setCreatedBy(tradeRate.getRatedNick());
			blackListDTO.setLastModifiedDate(new Date());
			blackListDTO.setLastModifiedBy(tradeRate.getRatedNick());
			this.smsBlackListDTOService.saveBlackListDTO(uid, blackListDTO);*/
		} else {
			// 新加
			SmsBlackListDTO blackListDTO = new SmsBlackListDTO();
			blackListDTO.setAddSource("4"); //添加来源 1-单个添加 2-批量添加 3-退订回N/TD 4-中差评拉黑   5 退款添加
			blackListDTO.setUid(uid);
			blackListDTO.setNickOrPhone(tradeRate.getNick());
			blackListDTO.setNick(tradeRate.getNick());
			blackListDTO.setType("2"); //黑名单类型 1-手机号 2-客户昵称
			blackListDTO.setCreatedDate(new Date());
			blackListDTO.setCreatedBy(tradeRate.getRatedNick());
			blackListDTO.setLastModifiedDate(new Date());
			blackListDTO.setLastModifiedBy(tradeRate.getRatedNick());
			this.smsBlackListDTOService.saveBlackListDTO(uid, blackListDTO);
			logger.info("**** 该用户uid：{} 已经通过昵称 添加到 黑名单中", uid+"昵称为"+tradeRate.getNick());
		}
	}
	
	/**
	 * 中差评通知安抚逻辑
	 * @param uid
	 */
	@Override
	public void rateMonitoringPacify(Long uid, MemberInfoDTO memberInfoDTO, String session) {
		//判断用户身份 短信条数 等
		UserInfo user = userInfoService.findUserInfo(uid);
		user = this.isNormalUser(uid, user);
        if(user == null){
            logger.info("中差评监控和安抚  用户状态异常，短信内容为0或者已过期黑名单 uid:{}", uid);
            return;
        }
		this.rateMonitoringPacifySmsBuyer(uid, memberInfoDTO.getId(), memberInfoDTO, session);
		this.rateMonitoringPacifySmsSeller(uid, user.getMobile(), session);
	}
	
	/**
	 * 买家安抚
	 * @param memberId
	 * @param memberInfoDTO
	 */
	@Override
	public void rateMonitoringPacifySmsBuyer(Long uid, Long memberId, MemberInfoDTO memberInfoDTO, String session) {
		String buyerPhone = memberDTOService.findPhoneBybuyerNick(uid, memberInfoDTO.getBuyerNick());
		String phone = this.getDecryptData(uid, buyerPhone, EncrptAndDecryptClient.PHONE, session);
		// 获取短信模板
		// smsTemplateService.findSubType(type)
		// 昵称解密
		String nick = this.getDecryptData(uid, memberInfoDTO.getBuyerNick(), EncrptAndDecryptClient.SIMPLE, session);
		// 昵称替换
		String context = TEMPLATE_SMS.replaceFirst("AaA", nick);
		String returnCode = pushSmsSerive.sendSmsBySingle(phone, context);
		// 短信发送成功 扣除短信条数
		if (returnCode.equals("100")) {
			// 扣费并创建记录
			// 20-中差评监控 21-中差评安抚 22-中差评统计 23-中差评原因 24中差评原因设置
			userAccountService.doUpdateUserSms(uid, uid.toString(), SendMessageStatusInfo.DEL_SMS, 1, "21",
					uid.toString(), "127.0.0.1", Constants.USER_OPERATION_SINGLE, false);
			logger.info("**** Rate买家安抚 短信发送了 uid:{} phone:{}", uid, phone);
		}
	}
	
	/**
	 * 卖家通知
	 * @param uid
	 * @param phone
	 */
	@Override
	public void rateMonitoringPacifySmsSeller(Long uid, String phone, String session) {
		// 手机号解密
		phone = this.getDecryptData(uid, phone, EncrptAndDecryptClient.PHONE, session);
		String returnCode = pushSmsSerive.sendSmsBySingle(phone, "【淘宝】您收到一条中差评，请及时处理");
		if (returnCode.equals("100")) {
			// 扣费并创建记录
			// 20-中差评监控 21-中差评安抚 22-中差评统计 23-中差评原因 24中差评原因设置
			userAccountService.doUpdateUserSms(uid, uid.toString(), SendMessageStatusInfo.DEL_SMS, 1, "20",
					uid.toString(), "127.0.0.1", Constants.USER_OPERATION_SINGLE, false);
			logger.info("**** 卖家通知 短信发送了 uid:{} phone:{}", uid, phone);
		}
	}
		
	/**
     * 判断用户是否是过期用户或者短信余额是否不足
     * @param Long uid  用户id
     * @return true 用户是正常用户且短信语言大于0
     */
	@Override
	public UserInfo isNormalUser(Long uid, UserInfo user) {
		if (user == null) {
			return user;
		}
		if (user.getExpirationTime() == null) {
			return null;
		}
		if (user.getExpirationTime().getTime() < System.currentTimeMillis()) {
			return null;
		}
		if (user.getStatus() == null) {
			return null;
		}
		if (user.getStatus() != 0) {
			return null;
		}
		if (ValidateUtil.isEmpty(user.getId())) {
			return null;
		}
		String sellerName = user.getTaobaoUserNick();
		long userSms = this.userAccountService.findUserAccountSms(user.getId());
		if (userSms <= 0) {
			this.userAccountService.doTmcUser(user.getId(), sellerName, Constants.DEL_SMS);
			return null;
		}
		user.setTaobaoUserNick(sellerName);
		user.setUserAccountSms(userSms);
		return user;
	}
	
	/**
	 * 获取解密后的数据
	 * @author: wy
	 * @time: 2017年7月27日 上午11:00:40
	 * @param oldData 原始数据，不可以为空
	 * @param type 要解密的类型，不可以空
	 * @param sellerNick 卖家昵称（昵称秘钥二选一）
	 * @param sessionKey 卖家秘钥（昵称秘钥二选一）
	 * @return 
	 * @throws SecretException
	 */
	@Override
	public String getDecryptData(Long uid, String oldData, String type, String sessionKey) {
		try {
			if (ValidateUtil.isEmpty(oldData) || ValidateUtil.isEmpty(type)
					|| !EncrptAndDecryptClient.isEncryptData(oldData, type)) {
				return oldData;
			}
			if (!ValidateUtil.isNotNull(sessionKey)) {
				return oldData;
			}
			if (!ValidateUtil.isNotNull(uid)) {
				return oldData;
			}
			if (EncrptAndDecryptClient.isEncryptData(oldData, type)) {
				return EncrptAndDecryptClient.getInstance().decrypt(oldData, type, sessionKey);
			}
		} catch (SecretException e) {
			logger.error("**** Rate订单评论 解密手机号失败用户 id：{}", uid);
		}
		return oldData;
	}
}
