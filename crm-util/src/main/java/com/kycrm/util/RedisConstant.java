package com.kycrm.util;

import java.util.Map;

public class RedisConstant {

	private Map<String, Long> redisExpireTimeMap;

	// 判断cacheName是否存在
	public boolean isExistCache(String cacheName) {
		if (redisExpireTimeMap.containsKey(cacheName)) {
			return true;
		} else {
			return false;
		}
	}

	// 根据cacheName获取过期
	public long getExpireTime(String cacheName) {
		if (redisExpireTimeMap.containsKey(cacheName)) {
			return redisExpireTimeMap.get(cacheName);
		} else {
			return -1;
		}
	}

	public RedisConstant() {
	}

	public RedisConstant(Map<String, Long> redisExpireTimeMap) {
		super();
		this.redisExpireTimeMap = redisExpireTimeMap;
	}

	/**
	 * 类名称：RedisCacheGroup <br/>
	 * 类描述：RedisCacheGroup组 <br/>
	 * 创建时间：2017年5月21日 下午5:20:57 <br/>
	 * 
	 * @author zlp
	 * @version V1.0
	 */
	public static class RedisCacheGroup {
		/**
		 * 用户数据同步详情 mongo数据迁移至mysql，完成后可删除
		 */
		public static final String SYN_DATA_USER_CACHE = "SYNDATAUSERCACHE";
		// 用户名称对应的mongo表缓存
		public static final String USRENICK_TABLE_CACHE = "USERNICKTABLECACHE";
		/**
		 * 用户主键id对应用户分库关系
		 */
		public static final String USER_PARTITION_CACHE = "USERPARTITIONCACHE";
		/**
		 * 订单同步锁，间隔时间内的任务只能有一个job能执行
		 */
		public static final String TRADE_SYN_LOCK = "SYN_LOCK";
		/**
		 * 商品同步锁
		 */
		public static final String ITEM_SYN_LOCK = "ITEM_SYN_LOCK";
		// 用户名称对应的token缓存
		public static final String USRENICK_TOKEN_CACHE = "USRENICKTOKENCACHE";
		// 数据缓存节点
		public static final String NODE_DATA_CACHE = "NODE_DATA_CACHE";
		// item同步缓存时间节点
		public static final String ITEM_SYNC_TIME_NODE = "ITEM_SYNC_TIME_NODE";
		// 会员群发缓存查询条件
		public static final String MEMBER_BATCH_SEND_DATA_CACHE = "MEMBERBATCHSENDDATACACHE";
		// 订单群发缓存查询条件
		public static final String ORDER_BATCH_SEND_DATA_CACHE = "ORDERBATCHSENDDATACACHE";
		// 验证码
		public static final String VALIDATE_CODE_DATA_CACHE = "VALIDATECODEDATACACHE";
		// 控制用户发送验证码的上限
		public static final String VALIDATE_CODE_TOP_CACHE = "VALIDATECODETOPCACHE";
		// 用户发送短信msg缓存
		public static final String MESSAGE_MSG_CACHE = "MESSAGEMSGCACHE";
		// 店铺数据缓存
		public static final String SHOP_DATA_CACHE = "SHOPDATACACHE";
		// 用户过期时间
		public static final String SELLER_EXPIRATION_TIME = "SELLEREXPIRATIONTIME";
		// 用户session过期时间（单位：毫秒）
		public static final String SELLER_EXPIRATION_DATE = "SELLEREXPIRATIONDATE";
		// 后台设置--短信签名修改缓存
		public static final String SHOP_NAME_CACHE = "SHOPNAMECACHE";
		// 首页昨日数据
		public static final String YESTERDAY_DATA_CACHE = "YESTERDAYDATACACHE";
		//手机号运营商
		public static final String PHONE_OPERATOR_CHCHE="PHONEOPERATORCHCHE";
		//手机号所在省
		public static final String PHONE_PROVINCE="PHONEPROVINCE";
		//手机号所在市
		public static final String PHONE_CITY="PHONECITY";
	}

	/**
	 * 类名称：RediskeyCacheGroup <br/>
	 * 类描述：RediskeyCacheGroup组 <br/>
	 * 创建时间：2017年5月21日 下午5:20:57 <br/>
	 * 
	 * @author zlp
	 * @version V1.0
	 */
	public static class RediskeyCacheGroup {
		/**
		 * 用户数据同步详情 mongo数据迁移至mysql，完成后可删除
		 */
		public static final String SYN_DATA_USER_CACHE_KEY = "SYNDATAUSERCACHEKEY_";
		/***
		 * 
		 * 数据缓存的节点key 信息
		 */
		public static final String USRENICK_TABLE_CACHE_KEY = "USRENICKTABLECACHEKEY";
		/**
		 * 用户主键id对应用户分库关系key
		 */
		public static final String USER_PARTITION_CACHE_KEY = "USERPARTITIONCACHE_UID_";
		
		public static final String TRADEDTO_TABLE_LOCKE_KEY = "TRADEDTO_UPDATE_";
		
		public static final String ORDERDTO_TABLE_LOCKE_KEY = "ORDERDTO_UPDATE_";
		
		public static final String MEMBERDTO_TABLE_LOCK_KEY = "MEMBERDTO_UPDATE_";
		
		public static final String USRENICK_TOKEN_CACHE_KEY = "USRENICKTOKENCACHEKEY";
		
		public static final String TRADE_NODE_START_TIME_KEY = "TRADE_NODE_START_TIME_KEY";
		
		public static final String ITEM_NODE_START_TIME_KEY = "ITEM_NODE_START_TIME_KEY";
		
		public static final String REFUND_NODE_START_TIME_KEY = "REFUND_NODE_START_TIME_KEY";
		/**
		 * 评论同步的最后一次时间
		 */
		public static final String RATE_NODE_LAST_TIME_KEY = "RATE_NODE_LAST_TIME_KEY";

		public static final String TOTAL_TRADE_DATA_COUNT_KEY = "TOTAL_TRADE_DATA_COUNT_KEY";
		/**
		 * 商品同步数量
		 */
		public static final String TOTAL_ITEM_DATA_COUNT_KEY = "TOTAL_ITEM_DATA_COUNT_KEY";
		/**
		 * 同步评论的数据量
		 */
		public static final String TOTAL_RATE_DATA_COUNT_KEY = "TOTAL_RATE_DATA_COUNT_KEY";

		public static final String TRADE_IMPORT_NODE_KEY_START_TIME = "TRADEIMPORTNODESTARTTIMEKEY";
		
		public static final String TRADE_IMPORT_NODE_KEY_END_TIME = "TRADEIMPORTNODEENDTIMEKEY";
		
		public static final String TOTAL_IMPORT_DATA_COUNT_KEY = "TOTALIMPORTDATACOUNTKEY";

		public static final String MEMBER_BATCH_SEND_DATA_KEY = "MEMBERBATCHSEND";

		public static final String ORDER_BATCH_SEND_DATA_KEY = "ORDERBATCHSENDDATAKEY";

		public static final String VALIDATE_CODE_KEY = "VALIDATECODE";
		
		public static final String VALIDATE_CODE_TOP_KEY = "VALIDATECODETOP";

		public static final String SHOP_DATA_KEY = "SHOPDATAKEY";

		public static final String SMSRECORD_NODE_KEY = "SMSRECORD";

		public static final String TOTAL_SMSRECORD_DATA_KEY = "TOTALSMSRECORDDATAKEY";

		public static final String SYNC_HISTORY_TRADE_DATA_KEY = "SYNCHISTORYTRADEDATAKEY";

		public static final String TOTAL_SYNC_HISTORY_TRADE_DATA_KEY = "TOTALSYNCHISTORYTRADEDATAKEY";

		public static final String SYNC_HISTORY_MEMBER_DATA_KEY = "SYNCHISTORYMEMBERDATAKEY";

		public static final String TOTAL_SYNC_HISTORY_MEMBER_DATA_KEY = "TOTALSYNCHISTORYMEMBERDATAKEY";

		public static final String SYNC_EFFECT_MSGID_KEY = "SYNCEFFECTMSGIDKEY";

		public static final String SYNC_EFFECT_DATA_KEY = "SYNCEFFECTDATAKEY";

		public static final String SYNC_RELATION_ITEM_DATA_KEY = "SYNCRELATIONITEMDATAKEY";

		public static final String SYNC_EFFECT_RECORD_DATA_KEY = "SYNCEFFECTRECORDDATAKEY";

		// 后台管理--短信签名KEY
		public static final String SHOP_NAME_KEY = "SHOP_NAME_KEY";
		/**
		 * 营销中心效果分析本次查询的发送记录是不是历史数据
		 */
		public static final String MSG_IS_HISTORY_DATA = "MSGISHISTORYDATA";
		
		public static final String BASE_MEMBER_FILTER = "BASE_MEMBER_FILTER";
		
		public static final String PREMIUM_MEMBER_FILTER = "PREMIUM_MEMBER_FILTER";
		/**
		 * 手机号段运营商
		 */
		public static final String PHONE_OPERATOR_CHCHE_KEY ="PHONEOPERATORCHCHEKEY";
		/**
		 * 手机号段对应省
		 */
		public static final String PHONE_PROVINCE_KEY = "PHONEPROVINCEKEY";
		/**
		 * 手机号段对应市
		 */
		public static final String PHONE_CITY_KEY = "PHONECITYKEY";
		public static final String SCHEDULE_SEND_MESSAGE = "SCHEDULE_SEND_MESSAGE";
		/*
		 * 同步退款订单的数量额key
		 */
		public static final String TOTAL_RETURN_DATA_COUNT_KEY = "TOTAL_RETURN_DATA_COUNT_KEY";
		public static final String MEMBER_ID = "MEMBER_ID";

	}

}
