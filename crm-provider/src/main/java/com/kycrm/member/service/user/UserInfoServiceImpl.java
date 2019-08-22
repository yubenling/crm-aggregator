package com.kycrm.member.service.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.user.IUserInfoDao;
import com.kycrm.member.dao.user.IUserLoginInfoDao;
import com.kycrm.member.domain.entity.user.RechargeMenu;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserLoginInfo;
import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.tradecenter.ITradeSetupService;
import com.kycrm.member.util.TaoBaoClientUtil;
import com.kycrm.util.Constants;
import com.kycrm.util.GetUserLevel;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.TmcInfo;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.request.TmcUserPermitRequest;

/**
 * @ClassName: UserInfoServiceImpl.java
 * @Description: 
 */
@Service("userInfoService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class UserInfoServiceImpl implements IUserInfoService {

	@Autowired
	private IUserInfoDao userInfoDao;
    
	@Autowired
	private UserTmcListernService userTmcListernService;
	
	@Autowired
	private IUserAccountService userAccountService;
	
	@Autowired
	private IUserRechargeService userRechargeService;
    
    @Autowired
    private IRechargeMenuService rechargeMenuService;
    
    @Autowired
	private ICacheService cacheService;
    
    @Autowired
    private ITradeSetupService tradeSetupService;
    
    @Autowired
    private JudgeUserUtil judgeUsetUtil;
    
    @Autowired
    private IUserLoginInfoDao userLoginInfoDao;
    
    @Autowired
    private IUserPayBillService userPayBillService;
	
    private Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
    @Override
    public boolean doCreateNewUser(String sellerNick, String openUid,String taoBaoUserId,String sessionKey, 
            Date sellerExpireTime, String expiresIn){
        UserInfo userInfo = new UserInfo();
        userInfo.setTaobaoUserNick(sellerNick);
        userInfo.setOpenUid(openUid);
        userInfo.setTaobaoUserId(taoBaoUserId);
        userInfo.setAccessToken(sessionKey);
        userInfo.setExpirationTime(sellerExpireTime);
        userInfo.setExpirationSecs(Long.parseLong(expiresIn));
        userInfo.setStatus(0);
        userInfo.setCreatedDate(new Date());
        userInfo.setLastLoginDate(userInfo.getCreatedDate());
        userInfo.setModifyTime(userInfo.getCreatedDate());
        userInfo.setLevel(TaoBaoClientUtil.getUserSellerGetLevel(sessionKey));
        this.userInfoDao.doCreateNewUser(userInfo);
        if(userInfo.getId()==null){
            return false;
        }
        return this.userAccountService.doCreateUserAccountByUser(userInfo.getId(), sellerNick, 0L);
    }
    
    public Long findUidBySellerNick(String sellerNick){
        if(ValidateUtil.isEmpty(sellerNick)){
            return null;
        }
        UserInfo userInfo = userInfoDao.findUserInfoByTaobaoNickTruly(sellerNick);
        if (userInfo != null) {
        	return userInfo.getId();
		}
        return null;
    }
    
    /**
     * 根据用户昵称查询用户信息
     */
    @Override
    public UserInfo findUserInfoByTmc(String userNick) {
        UserInfo user = this.userInfoDao.findUserInfoByTmc(userNick);
        return user;
    }
    
    
    /**
     * 根据用户id查询用户信息
     */
    @Override
    public UserInfo findUserInfo(Long uid) {
        UserInfo user = this.userInfoDao.findUserInfoByUid(uid);
        return user;
    }
    
    /**
     * tmc专用 查询用户的过期时间和状态
     */
    public UserInfo findExpireTimeAndSms(Long uid) {
        UserInfo user = this.userInfoDao.findExpireTimeAndSms(uid);
        return user;
    }
    
    /** 
    * @Description: 用户充值
    * @param  userRecharge    充值信息实体 
    * @return void    返回类型 
    * @author jackstraw_yu
    * @date 2017年12月4日 下午6:15:38
    */
    @Override
    public void doUserRecharge(UserRecharge userRecharge) {
        logger.info("################################### 有充值进来,用户名称 : "
                    +userRecharge.getUserNick()+";充值条数 : "+ userRecharge.getRechargeNum());
        Long before = userAccountService.findUserAccountSms(userRecharge.getUid());
        // 1,修改用户表的余额数
        boolean excute = this.userAccountService.doUpdateUserSms(userRecharge.getUid(),userRecharge.getUserNick(), Constants.ADD_SMS, 
                                userRecharge.getRechargeNum(), "短信套餐购买", userRecharge.getUserNick(), null, "淘宝服务短信套餐充值，短信数量："+userRecharge.getRechargeNum(),UserAccountServiceImpl.NO_TIME);
        if(excute){// 充值成功
        	Long smsCount = userAccountService.findUserAccountSms(userRecharge.getUid());
            userRecharge.setStatus("1");
            userRecharge.setRechargeLaterNum(smsCount);
            if(before!=null){
            	userRecharge.setRechargeBeforeNum(before.intValue());
            }
            this.userTmcListernService.addUserPermitByMySql(userRecharge.getUid(), userRecharge.getUserNick(),null);
        }else{// 充值失败
            userRecharge.setStatus("2");
        }
        try {
			if(excute){
				userRecharge.setRemarks("tmc充值");
				userRecharge.setCreatedBy(userRecharge.getUserNick());
				userRecharge.setLastModifiedBy(userRecharge.getUserNick());
				Long pid = userPayBillService.insertPayBill(userRecharge);
				logger.info("充值的pid为"+pid);
				if(pid!=null){
					userRecharge.setPid(pid+"");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("添加打款记录出错");
		}
        //保存充值记录
        userRechargeService.saveUserRechar(userRecharge);
    }
    
    /**
     * @Title: queryUserInfo
     * @Description:(通过userId查询出用户)
     * @param @param userId
     * @param @return    参数
     * @return String    返回类型
     * @author:jackstraw_yu
     * @throws
     */
    public UserInfo queryUserTokenInfo(Long uid) {
        return this.userInfoDao.queryTokenInfo(uid);
    }
    
    /**
     * 查询用户是否存在
     * @author: wy
     * @time: 2017年11月13日 下午6:14:23
     * @param sellerNick 买家昵称
     * @return 存在返回true，不成功返回false
     */
    @Override
    public boolean isExistsById(long uid){
        Integer reuslt = this.userInfoDao.findExistsById(uid);
        if(reuslt==0){
            return false;
        }
        return true;
    }
    
    @Override
    public void addUserRechargeByTmc(String str) {
        JSONObject json = JSON.parseObject(str);
     // 判空
        if (json != null ) {
            // 获取订单id
            String orderId = json.getString("order_id");
            // 查询充值列表,判断充值列表中是否存在该order_id
            long l = this.userRechargeService.getUserRechar(orderId);
            // 不存在该orderId
            if (l == 0) {
                UserRecharge userRecharge = new UserRecharge();
                // 充值用户昵称
                userRecharge.setUserNick(json.getString("nick"));
                // 充值用户编号
                userRecharge.setOrderId(json.getString("order_id"));
                UserInfo user = this.userInfoDao.findUserInfoByTmc(userRecharge.getUserNick());
                if(user==null){
                      //userRecharge.setUid(null);
                	return ;
                }else{
                    userRecharge.setUid(user.getId());
                }
                // 调用RechargeMenuService获取到短信套餐的条数
                RechargeMenu rechargeMenu = rechargeMenuService
                        .queryRechargeMenu(json.getString("item_code"));
                if (rechargeMenu != null) {
                    userRecharge.setRechargeNum(rechargeMenu.getNum());
                }

                double rechargePrice = Double.parseDouble(json
                        .getString("total_pay_fee"));
                // 1分换算成1元
                userRecharge.setRechargePrice(rechargePrice / 100);
                userRecharge.setRechargeDate(new Date());
                userRecharge.setRechargeType("3");
                // 调用service保存
                try {
                    this.doUserRecharge(userRecharge);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getUserRechargeInfo(json.getString("order_id"),json.getString("nick"));

            }
        }
    }
    
    /**
     * 根据订单编号查询订单信息，并生成帐单上传到淘宝
     * 
     * @param orderId
     * @return
     */
    private void getUserRechargeInfo(String orderId,String userNick) {
        UserRecharge ur = userRechargeService.getUserRechargeInfo(orderId);

        // 根据卖家昵称查询用户信息（取出用户的sessionKey即access_token）
        UserInfo ui = new UserInfo();
        ui = this.findUserInfo(userNick);
        if (ur == null) {
            return;
        }
        Long fee = new Double(ur.getRechargePrice() * 100).longValue();
        //上次服务订单
        TaoBaoClientUtil.doFuwuSpBillreordAdd(ur.getUserNick(), ur.getStatus(), ui.getMobile(), ui.getId(), 
                fee, ur.getId(), ur.getOrderId(), ui.getAccessToken(), ur.getRechargeDate());
        //确认服务订单
        TaoBaoClientUtil.doFuwuSpConfirmApply(ur.getUserNick(), ur.getOrderId(), fee, ui.getId(), ui.getAccessToken());
    }


    @Override
    public Long findUidByOpenUid(String openUid) {
        return this.userInfoDao.findUidByOpenUid(openUid);
    }


    @Override
    public void updateUserInfoByLogin(long uid, String sellerNick, String sessionKey, Date sellerExpireTime,
            String expiresIn,Date loginDate,String openUid) {
        Map<String,Object> map = new HashMap<String,Object>(9);
        map.put("uid", uid);
        map.put("sellerNick", sellerNick);
        map.put("sessionKey", sessionKey);
        map.put("expirationTime", sellerExpireTime);
        map.put("expirationSecs", expiresIn);
        map.put("loginDate", loginDate);
        map.put("openUid", openUid);
        this.userInfoDao.updateUserInfoByLogin(map);
    }

    /** 
     * @Description: 首页小红包标记为不显示状态
     * @param  userId
     * @param  hasProvide   
     * @return void    返回类型 
     * @author jackstraw_yu
     * @date 2017年11月28日 上午9:29:22
     */
    @Transactional
    public void updateUserHasProvide(String userNick, boolean hasProvide) {
    	Map<String, Object> map = new HashMap<String,Object>();
 		map.put("userNick", userNick);
 		map.put("hasProvide", hasProvide);
 		int execute = userInfoDao.updateUserHasProvide(map);
 		if(execute != 1){
 			throw new RuntimeException("影响的行数不为1! :"+userNick);	
 		}
 	}
    
   /** 
    * @Description: 首页小红包,保存用户手机号,标记怎送500条短信 
    * @param  mobile
    * @param  qqNum
    * @param  user
    * @return boolean    返回类型 
    * @author jackstraw_yu
    * @date 2018年1月22日 下午3:36:42
    */
    @Transactional
    public boolean saveUserMobileInfo(String mobile,String qqNum,UserInfo user){
    	boolean hasModify =  false;
    	UserInfo result = this.findUserInfo(user.getId());
    	if(result!=null){
	    	if(result.getMobile() == null && (result.getHasProvide() == null || result.getHasProvide().booleanValue()==false)){
	    		//手机号为空,是否赠送标记为空或者用户不希望在看到小红包时
	   			Map<String, Object> map = new HashMap<String, Object>();
	   			map.put("mobile", mobile);
	   			map.put("qqNum", qqNum);
	   			map.put("id", user.getId());
	   			map.put("hasProvide", Boolean.TRUE);
	   			int execute = userInfoDao.saveUserMobileInfo(map);
	   			if(execute != 1){
	   				throw new RuntimeException("影响的行数不为1! :"+user.getTaobaoUserNick());	
	   			}
	   			hasModify = true;
			}else{
				//手机号为空,是否赠送标记为空或者用户不希望在看到小红包时
	   			Map<String, Object> map = new HashMap<String, Object>();
	   			map.put("mobile", mobile);
	   			map.put("qqNum", qqNum);
	   			map.put("id", user.getId());
	   			map.put("hasProvide", Boolean.TRUE);
	   			int execute = userInfoDao.saveUserMobileInfo(map);
	   			if(execute != 1){
	   				throw new RuntimeException("影响的行数不为1! :"+user.getTaobaoUserNick());	
	   			}
			}
   		}else{
   			logger.info("保存用户手机号,查询到用户信息为空");
   			throw new RuntimeException("查询不到用户信息! :"+user.getTaobaoUserNick());
   		}
    	return hasModify;
    }

    
    /** 
	* @Description 查询用户的店铺名称<br/>
	* 有设置店铺名称返回店铺名称,没有店铺名称返回用户昵称
	* @param  user
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 上午10:36:31
	*/
	public String queryShopName(UserInfo user){
		String shopName = this.getShopNameFromCache(user.getId());
		//缓存不存在时,查询数据库并放入缓存,注意缓存击穿问题!
		if(shopName == null){
			logger.info("用户"+user.getId()+"缓存中店铺为null");
			shopName = userInfoDao.queryShopName(user.getId());
			if(shopName == null ) return null;
			logger.info("用户"+user.getId()+"从数据库中取出的店铺名为"+shopName);
			putShopNameToCache(user.getId(),shopName);
		}
		return userInfoDao.queryShopName(user.getId());
	}
	
	/** 
	* @Description 更新用户的店铺名称
	* @param  user    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 上午10:54:33
	*/
	public void modiftyShopName(UserInfo user){
		int execute = userInfoDao.modiftyShopName(user);
		logger.info("修改店铺名称为"+user.getShopName()+"修改影响的行数为"+execute);
		if(execute != 1){
			throw new RuntimeException("影响的行数不为1! :"+user.getTaobaoUserNick());	
		}
		//放入缓存
		putShopNameToCache(user.getId(),user.getShopName());
	}
	
	/** 
	* @Description 从redis中获取店铺名称--需要接口么?
	* @param  userName
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 下午6:02:18
	*/
	public String getShopNameFromCache(Long uid){
		String shopName = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.SHOP_NAME_CACHE, 
    							RedisConstant.RediskeyCacheGroup.SHOP_NAME_KEY+uid);
    	if(shopName==null || "".equals(shopName))
    		return null;
    	return shopName;
	}
    
	/** 
	* @Description 将店铺名称放入redis--需要接口么?
	* @param  userName
	* @param  shopName    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 下午6:02:40
	*/
	public void putShopNameToCache(Long uid,String shopName){
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.SHOP_NAME_CACHE, 
    							RedisConstant.RediskeyCacheGroup.SHOP_NAME_KEY+uid,shopName ,false);
	}

	/**
	 * 查询所有token不为空的用户
	 */
	@Override
	public List<Long> listTokenNotNull() {
		List<Long> userList = userInfoDao.listTokenNotNull();
		return userList;
	}

	/**
	 * 查询所有的用户
	 */
	@Override
	public List<UserInfo> listAllUser() {
		List<UserInfo> userList = userInfoDao.listAllUser();
		return userList;
	}

	/**
	 * 通过accessToken查询用户
	 */
	@Override
	public UserInfo findUserBytoken(String accessToken) {
		return userInfoDao.findUserBytoken(accessToken);
	}

	/**
	 * 通过id查询accessToken
	 */
	@Override
	public String findUserTokenById(Long uid) {
		return userInfoDao.findUserTokenById(uid);
	}

	/**
	 * 保存更新用户设置在首页的快捷链接
	 */
	@Override
	public Long updateUserShortcutLinkStr(Long uid, String linkStr) {
		Long id = userInfoDao.updateUserShortcutLinkStr(uid, linkStr);
		return id;
	}

	@Override
	public UserInfo findUserInfo(String taobaoUserNick) {
		return userInfoDao.findUserInfoByTaobaoNickTruly(taobaoUserNick);
	}

	@Override
	public UserInfo findUserInfoByTaobaoUserNick(String taobaoUserNick) {
		return userInfoDao.findUserInfoByTaobaoNickTruly(taobaoUserNick);
	}

	@Override
	public UserInfo findExpireTimeAndSms(String taobaoUserNick) {
		return null;
	}

	@Override
	public UserInfo findUserInfoByTaobaoUserId(String taobaoUserId) {
		return null;
	}

	@Override
	public UserInfo queryUserTokenInfo(String userNickName) throws Exception {
		return null;
	}

	@Override
	public String findLinkByUid(Long uid) {
		String linkStr = userInfoDao.findLinkByUid(uid);
		return linkStr;
	}
	
	 /**
     * 给用户授权，通过查询数据库来添加要接收的tmc消息
     * @author: wy
     * @time: 2017年8月23日 下午3:43:58
     * @param sellerNick 卖家昵称
     * @param addType 要新添加的消息类型（如1--下单关怀），可以为空，为空则根据取出来的值添加消息
     */
	@Override
    public boolean addUserPermitByMySql(Long uid,String addType){
        if(uid == null){
            return false;
        }
        List<String> list = this.tradeSetupService.findTypeBySellerNickTmc(uid);
        Set<String> set = new HashSet<String>(28);
        if(ValidateUtil.isNotNull(list)){
            for (String string : list) {
                String s = this.getTopicBySettingType(string);
                if(s!=null){
                    set.add(s);
                }
            }
        }
        if(ValidateUtil.isNotNull(addType)){
            String addTopic = this.getTopicBySettingType(addType);
            if(ValidateUtil.isNotNull(addTopic)){
                set.add(addTopic);
            }
        }
        set.add(TmcInfo.FUWU_ORDERPAID_TOPIC);
        set.add(TmcInfo.FUWU_SERVICE_OPEN_TOPIC);
        String topics = this.getCommaStringByCollection(set);
        String sessionKey = this.judgeUsetUtil.getUserTokenByRedis(uid);
        return this.openUserPermit(sessionKey, topics);
    }
    
    /**
     * 通过对应的状态取得对应的淘宝TMC消息TOPIC,不提供服务开通付款的消息返回
     * @author: wy
     * @time: 2017年8月23日 下午2:40:56
     * @param type
     * @return
     */
    public String getTopicBySettingType(String type){
        if(ValidateUtil.isEmpty(type)){
            return null;
        }
        switch (type) {
        case "1":{ //1-下单关怀 
            return TmcInfo.TRADE_CREATE_TOPIC;
        }
        case "2":{ //2-常规催付 
            return TmcInfo.TRADE_CREATE_TOPIC;
        }
        case "3":{ //3-二次催付
            return TmcInfo.TRADE_CREATE_TOPIC;
        }
        case "4":{ //4-聚划算催付
            return TmcInfo.TRADE_CREATE_TOPIC;
        }
        case "6":{ //6-发货提醒 
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "7":{ //7-到达同城提醒
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "8":{ //8-派件提醒
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "9":{ //9-签收提醒
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "11":{ //11-延时发货提醒
            return TmcInfo.TRADE_BUYERPAY_TOPIC;
        }
        case "12":{ //12-宝贝关怀
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "13":{ //13-付款关怀 
            return TmcInfo.TRADE_BUYERPAY_TOPIC;
        }
        case "14":{ //14-回款提醒
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "16":{ //16-自动评价
            return TmcInfo.TRADE_SUCCESS_TOPIC;
        }
        case "29":{ //29-买家申请退款
            return TmcInfo.REFUND_CREATED_TOPIC;
        }
        case "30":{ //30-退款成功
            return TmcInfo.REFUND_SUCCESS_TOPIC;
        }
        case "31":{ // 31-等待退货 
            return TmcInfo.REFUND_AGREE_TOPIC;
        }
        case "32":{ //32-拒绝退款
            return TmcInfo.REFUND_REFUSE_TOPIC;
        }
        case "37":{ //37-好评提醒
            return TmcInfo.TRADE_SUCCESS_TOPIC;
        }
        default:
            return null;
        }
    }
    
    /**
     * 将字符串集合转为由英文逗号连接的字符串
     * @author: wy
     * @time: 2017年8月23日 下午2:43:47
     * @param c
     * @return
     */
    public String getCommaStringByCollection(Collection<? extends String> c){
        if(c == null || c.size()==0){
            return null;
        }
        StringBuffer s = new StringBuffer();
        int i = 0;
        for (String string : c) {
            if(string==null){
                continue;
            }
            if(i==0){
                s.append(string);
                i++;
            }else{
                s.append(",").append(string);
            }
        }
        return s.toString();
    }
    
    /**
     * 根据用户的sessionKey和具体的Topic来为用户开启消息
     * @author: wy
     * @time: 2017年8月23日 下午2:59:26
     * @param sessionKey 用户的秘钥
     * @param topics 要为用户授权的消息，如果消息为空，则会强制加上服务开通和服务支付两个消息
     * @return
     */
    public boolean openUserPermit(String sessionKey,String topics){
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
	 * 添加用户信息 
	 */
    @Override
	public Long addUserInfo(String sellerNick, String taoBaoUserId,
            String sessionKey, Date sellerExpireTime, String expiresIn) {
	    UserInfo user = new UserInfo();
	    user.setTaobaoUserNick(sellerNick);
	    user.setCreateTime(new Date());
	    user.setLastLoginDate(new Date());
	    user.setStatus(0);
	    user.setTaobaoUserId(taoBaoUserId);
	    user.setAccessToken(sessionKey);
	    user.setExpirationTime(sellerExpireTime);
	    user.setExpirationSecs(expiresIn == null? 0 : Long.parseLong(expiresIn));
	    user.setLevel(GetUserLevel.getSingleUserLevel(sessionKey));
	    user.setCreatedDate(new Date());
	    user.setLastModifiedDate(new Date());
	    user.setCreatedBy(sellerNick);
	    user.setLastModifiedBy(sellerNick);
	    Long uid = userInfoDao.doCreateNewUser(user);
		this.userAccountService.doCreateUserAccountByUser(uid, sellerNick, 0L);
		return uid;
	}
    
    @Override
    public void doCreateInfoByLogin(UserLoginInfo userLoginInfo) throws Exception{
    	userLoginInfoDao.doCreateInfoByLogin(userLoginInfo);
    }

    /**
	 * 定时检查没有等级的用户，并更新用户等级
	 * @author HL
	 * @time 2018年11月9日 上午10:42:29
	 */
    @Override
    public void updateUserInfoLevel(){
    		logger.info("*****批量更新用户等级*****开始执行...");
			List<UserInfo> list = userInfoDao.findUserInfoByNoLevel();
			List<UserInfo> updateList = new ArrayList<UserInfo>();
			Integer i = 0;
			for (UserInfo userInfo : list) {
				Long level = GetUserLevel.getSingleUserLevel(userInfo
						.getAccessToken());
				if (level != null) {
					userInfo.setLevel(level);
					updateList.add(userInfo);
					i++;
				}
				if (i != 0 && i % 100 == 0) {
					userInfoDao.batchUpdateUserInfoLevel(updateList);
					logger.info("*****批量更新用户等级list：" + updateList.size());
					updateList = new ArrayList<UserInfo>();
				}

			}
			if(updateList !=null && updateList.size()>0){
				userInfoDao.batchUpdateUserInfoLevel(updateList);
				logger.info("*****批量更新用户等级list："+updateList.size());
			}
			logger.info("*****批量更新用户等级*****执行完毕*****更新数量："+i);
	}

	@Override
	public void updateUserInfoExpirationTime(Map<String, Object> map) {
		userInfoDao.updateUserInfoExpirationTime(map);
	}
	
}
