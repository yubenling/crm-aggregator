package com.kycrm.member.service.login;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserTableCache;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.trade.FirstSynTradeService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.user.IUserPartitionInfoService;
import com.kycrm.member.service.user.UserLoginInfoService;
import com.kycrm.member.service.user.UserTmcListernService;
import com.kycrm.member.util.TaoBaoClientUtil;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;
import com.kycrm.util.thread.MyFixedThreadPool;
import com.taobao.api.ApiException;
import com.taobao.api.internal.util.WebUtils;
import com.taobao.api.request.JushitaJdpUserAddRequest;
import com.taobao.api.request.TmcUserGetRequest;
import com.taobao.api.request.VasSubscribeGetRequest;
import com.taobao.api.response.JushitaJdpUserAddResponse;
import com.taobao.api.response.TmcUserGetResponse;
import com.taobao.api.response.VasSubscribeGetResponse;

/**
 * @author wy
 * @version 创建时间：2017年11月13日 下午1:58:01
 */
@Service("loginService")
public class LoginServiceImpl implements ILoginService {

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;

	@Autowired
	private IMemberDTOService memeberDTOService;

	@Autowired
	private ITradeDTOService tradeDTOService;

	@Autowired
	private IOrderDTOService orderDTOService;

	@Autowired
	private IUserPartitionInfoService userPartitionInfoService;

	@Autowired
	private UserTmcListernService userTmcListernService;

	@Autowired
	private FirstSynTradeService firstSynTradeService;

	@Autowired
	private UserLoginInfoService userLoginInfoService;

	private Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Override
	public Map<String, String> login(String code, String ipAddr) {
		this.logger.info("登录请求来了 ，code：" + code + " ipAddr" + ipAddr);
		if (ValidateUtil.isEmpty(code)) {
			return null;
		}
		Map<String, String> props = new HashMap<String, String>(8);
		props.put("grant_type", "authorization_code");
		props.put("code", code);
		props.put("client_id", Constants.TOP_APP_KEY);
		props.put("client_secret", Constants.TOP_APP_SECRET);
		props.put("redirect_uri", Constants.TAOBAO_CALLBACK_URL);
		props.put("view", "web");
		String json = null;
		try {
			json = WebUtils.doPost(Constants.TAOBAO_TOKEN_URL, props, 30000, 30000);
			this.logger.info("获取用户信息源数据：" + json);
		} catch (Exception e) {
			logger.error("WebUtils.doPost异常!!^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		}
		if (ValidateUtil.isEmpty(json)) {
			this.logger.info("code和淘宝换取数据错误 ，code:" + code + " ipAddr:" + ipAddr);
			return null;
		}
		JSONObject userJson = JSONObject.parseObject(json);
		// 卖家用户秘钥
		String sessionKey = userJson.getString("access_token");
		// 卖家昵称
		String sellerNick = userJson.getString("taobao_user_nick");
		// 卖家淘宝ID
		String taoBaoUserId = userJson.getString("taobao_user_id");
		// 软件过期时间
		String expiresIn = userJson.getString("expires_in");

		try {
			sellerNick = URLDecoder.decode(sellerNick, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		sellerNick = sellerNick.trim();

		// 改sellerNick 用taoBaoUserId判断用户是否存在
		UserInfo userInfo = this.userInfoService.findUserInfoByTaobaoUserId(taoBaoUserId);
		Optional<UserInfo> op = Optional.ofNullable(userInfo);
		op.orElse(new UserInfo());
		Long uid = op.get().getId();
		String openUid = null;
		// 昵称查不到userid,所以就用sessionkey查openUid然后查userId
		if (uid == null) {
			openUid = TaoBaoClientUtil.getOpenuidGetBySession(sessionKey);
			if (openUid == null) {
				this.logger.info("卖家openUid获取不到，昵称：{} sessionKey：{} 淘宝ID：{} 过期时间：{}", sellerNick, sessionKey,
						taoBaoUserId, expiresIn);
				return null;
			}
			uid = this.userInfoService.findUidByOpenUid(openUid);
		}
		this.logger.info("转码后的数据-->卖家昵称：" + sellerNick + "，卖家秘钥：" + sessionKey + "，卖家淘宝ID：" + taoBaoUserId + "，卖家过期时间："
				+ expiresIn + ",卖家唯一标识：" + openUid);
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
				RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + uid, sessionKey, false);

		Date sellerExpireTime = this.getUserExpireTimeByTaobao(sellerNick);
		if (sellerExpireTime == null) {
			this.logger.info("获取用户过期时间失败：" + json);
			return null;
		}
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.SELLER_EXPIRATION_TIME,
				RedisConstant.RedisCacheGroup.SELLER_EXPIRATION_TIME + uid,
				Long.parseLong(expiresIn) * 1000 + sellerExpireTime.getTime(), false);
		// 判断用户是否为新用户
		if (uid == null) {
			if (openUid == null) {
				openUid = TaoBaoClientUtil.getOpenuidGetBySession(sessionKey);
			}
			// 新用户 --> 启动一个线程，同步新用户一天的订单数据
			// 创建新用户
			this.userInfoService.doCreateNewUser(sellerNick, openUid, taoBaoUserId, sessionKey, sellerExpireTime,
					expiresIn);
			uid = this.userInfoService.findUidByOpenUid(openUid);
			asyncHandleData(uid, sellerNick, sessionKey);
		}
		// 多线程更新用户信息
		this.updateUserByLogin(uid, sellerNick, sessionKey, taoBaoUserId, expiresIn, sellerExpireTime, ipAddr,
				new Date());
		Map<String, String> reuslt = new HashMap<String, String>(9);
		Map<String, Long> map = getExpirationTime(sellerExpireTime);
		// 到期天数
		if (map.get("days") != null) {
			reuslt.put("dayCount", String.valueOf(map.get("days")));
		} else {
			reuslt.put("hourCount", String.valueOf(map.get("hours")));
		}
		reuslt.put("taobao_user_id", sellerNick);
		reuslt.put("taobao_user_nick", sellerNick);
		reuslt.put("uid", String.valueOf(uid));
		reuslt.put("access_token", sessionKey);
		return reuslt;
	}

	/**
	 * 开启线程更新用户信息
	 * 
	 * @author: wy
	 * @time: 2017年11月14日 上午10:41:43
	 * @param sellerNick
	 *            卖家昵称
	 * @param sessionKey
	 *            卖家密钥
	 * @param taoBaoUserId
	 *            卖家淘宝id
	 * @param expiresIn
	 *            卖家过期时间
	 * @param sellerExpireTime
	 *            卖家过期时间
	 */
	private void updateUserByLogin(final long uid, final String sellerNick, final String sessionKey,
			final String taoBaoUserId, final String expiresIn, final Date sellerExpireTime, final String ipAddress,
			final Date loginDate) {
		MyFixedThreadPool.getLoginThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				updateUser(uid, sellerNick, sessionKey, taoBaoUserId, expiresIn, sellerExpireTime, ipAddress,
						loginDate);
			}
		});
	}

	/**
	 * 更新用户信息
	 * 
	 * @author: wy
	 * @time: 2017年11月14日 上午10:41:43
	 * @param sellerNick
	 *            卖家昵称
	 * @param sessionKey
	 *            卖家密钥
	 * @param taoBaoUserId
	 *            卖家淘宝id
	 * @param expiresIn
	 *            卖家过期时间
	 * @param sellerExpireTime
	 *            卖家过期时间
	 */
	private void updateUser(long uid, String sellerNick, String sessionKey, String taoBaoUserId, String expiresIn,
			Date sellerExpireTime, String ipAddress, Date loginDate) {
		long startTime = System.currentTimeMillis();
		this.logger.info("卖家：" + sellerNick + "开始更新信息");
		String openUid = TaoBaoClientUtil.getOpenuidGetBySession(sessionKey);
		this.userInfoService.updateUserInfoByLogin(uid, sellerNick, sessionKey, sellerExpireTime, expiresIn, loginDate,
				openUid);
		// 将新用户添加到聚石塔同步列表
		this.addJstUser(sessionKey);
		if (!getTmcUserTopic(sellerNick)) {
			this.userTmcListernService.addUserPermitByMySql(uid, sellerNick, null);
		}
		userLoginInfoService.doCreateInfoByLogin(uid, sellerNick, ipAddress, loginDate);
		this.logger.info("卖家：" + sellerNick + "更新结束，花费了：" + (System.currentTimeMillis() - startTime) + "ms");
		long startTime1 = System.currentTimeMillis();
		updateTable(uid, sellerNick);
		this.logger.info("更新" + sellerNick + "  更新结束，花费了：" + (System.currentTimeMillis() - startTime1) + "ms");
	}

	/**
	 * 根据用户昵称查询用户是否添加到tmc同步列表中
	 * 
	 * @param taobao_user_nick
	 * @return
	 */
	public boolean getTmcUserTopic(String taobaoUserNick) {
		logger.info("================================<=>查询用户：" + taobaoUserNick
				+ "是否在tmc同步列表中================================");
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
		if (rsp.getTmcUser() != null && rsp.getTmcUser().getTopics() != null
				&& rsp.getTmcUser().getTopics().size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 添加新用户到聚石塔
	 * 
	 * @author: wy
	 * @time: 2017年11月14日 下午3:29:33
	 * @param token
	 */
	private void addJstUser(String token) {
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>添加用户到聚石塔数据同步列表================================");
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
		this.logger.info("聚石塔结果为：" + rsp.getBody());
	}

	/**
	 * 根据用户昵称和应用服务code获取用户应用的到期时间
	 * 
	 * @param taobao_user_nick
	 * @return
	 */
	private Date getUserExpireTimeByTaobao(String taobaoUserNick) {
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
			return rsp.getArticleUserSubscribes().get(timeCount - 1).getDeadline();
		}
		return null;
	}

	/**
	 * 获取用户软件到期时间
	 * 
	 * @param model
	 * @param userId
	 * @param request
	 * @throws Exception
	 */
	public Long getData(Date expirationTime, String userId) throws Exception {
		UserInfo userInfo = null;
		if (expirationTime == null) {
			userInfo = userInfoService.findUserInfo(userId);
			expirationTime = userInfo.getExpirationTime();
		}
		return this.getUserExpireDayNum(expirationTime);
	}

	/**
	 * 创建人：邱洋
	 * 
	 * @Title: 开启一个线程同步新用户一天之内的订单数据 @date 2017年4月7日--上午10:45:43 @return
	 *         void @throws
	 */
	private void asyncHandleData(final long uid, final String sellerName, final String sessionKey) {
		MyFixedThreadPool.getLoginThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				createTablesByNewUser(uid, sellerName);
				System.out.print("创建" + sellerName + "   表");
				firstSynTradeService.synTradeOneDay(uid, sellerName, sessionKey);
			}
		});
	}

	@Override
	public void updateTable(long uid, String sellerName) {
		try {
			if (!userInfoService.isExistsById(uid)) {
				return;
			}
			UserTableCache usertable = new UserTableCache();
			usertable.setDataCount(0l);
			usertable.setUserId(String.valueOf(uid));
			usertable.setUserNickName(sellerName);
			cacheService.putNoTime(RedisConstant.RedisCacheGroup.USRENICK_TABLE_CACHE,
					RedisConstant.RediskeyCacheGroup.USRENICK_TABLE_CACHE_KEY + uid, JsonUtil.toJson(usertable), false);
			if (this.userPartitionInfoService.doCreateUserPartitionInfo(uid, sellerName)) {
				this.smsRecordDTOService.doCreateTableByNewUser(uid);
				this.tradeDTOService.doCreateTableByNewUser(uid);
				this.orderDTOService.doCreateTableByNewUser(uid);
				this.memeberDTOService.doCreateTableByNewUser(uid);
				// 新加表 2018.07
				this.memeberDTOService.doCreateMemberItemAmountTableByNewUser(uid);
				this.memeberDTOService.doCreateSmsBlacklistTableByNewUser(uid);
				this.memeberDTOService.doCreateTradeRatesTableByNewUser(uid);
				// 创建高级会员筛选历史记录表
				this.memeberDTOService.doCreatePremiumFilterRecordTable(uid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createTablesByNewUser(long uid, String sellerName) {

		if (!userInfoService.isExistsById(uid)) {
			return;
		}
		UserTableCache usertable = new UserTableCache();
		usertable.setDataCount(0l);
		usertable.setUserId(String.valueOf(uid));
		usertable.setUserNickName(sellerName);
		// todo
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.USRENICK_TABLE_CACHE,
				RedisConstant.RediskeyCacheGroup.USRENICK_TABLE_CACHE_KEY + uid, JsonUtil.toJson(usertable), false);
		if (this.userPartitionInfoService.doCreateUserPartitionInfo(uid, sellerName)) {
			this.smsRecordDTOService.doCreateTableByNewUser(uid);
			this.tradeDTOService.doCreateTableByNewUser(uid);
			this.orderDTOService.doCreateTableByNewUser(uid);
			this.memeberDTOService.doCreateTableByNewUser(uid);
			// 新加表 2018.07
			this.memeberDTOService.doCreateMemberItemAmountTableByNewUser(uid);
			this.memeberDTOService.doCreateSmsBlacklistTableByNewUser(uid);
			this.memeberDTOService.doCreateTradeRatesTableByNewUser(uid);
			// 创建高级会员筛选历史记录表
			this.memeberDTOService.doCreatePremiumFilterRecordTable(uid);
		}
	}

	/**
	 * 计算指定时间距离今天相差的天数
	 * 
	 * @author: wy
	 * @time: 2017年11月14日 上午10:13:06
	 * @param userExpireDate
	 *            要计算的过期时间
	 * @return 计算好的天数
	 */
	private Long getUserExpireDayNum(Date userExpireDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long dayCount = 0L;
		try {
			if (userExpireDate != null) {
				String start = sdf.format(new Date());
				String end = sdf.format(userExpireDate);
				long timeStart = sdf.parse(start).getTime();
				long timeEnd = sdf.parse(end).getTime();
				// 两个日期想减得到天数
				dayCount = (timeEnd - timeStart) / (24 * 3600 * 1000);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dayCount;
	}

	/**
	 * 获取用户的过期时间<br/>
	 * 大于一天显示N+1天<br/>
	 * 小于一天显示N小时<br/>
	 * 
	 * @param userInfo
	 * @param userId
	 * @return Map<String,Long> 返回类型
	 * @author jackstraw_yu
	 * @date 2017年11月17日 下午4:03:47
	 */
	private Map<String, Long> getExpirationTime(Date date) {
		Map<String, Long> map = new HashMap<String, Long>();
		// 软件到期时间
		if (date != null) {
			// 小于一天显示小时,大于一天显示天,忽略整整一天的情况
			if ((date.getTime() - System.currentTimeMillis()) < 86400000) {
				map.put("hours", (date.getTime() - System.currentTimeMillis()) / (3600000));
			} else {
				map.put("days", (date.getTime() - System.currentTimeMillis()) / (86400000) + 1);
			}
		}
		return map;
	}
}
