package com.kycrm.member.service.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserLoginInfo;
import com.kycrm.member.domain.entity.user.UserRecharge;

/**
 * @ClassName: IUserInfoService.java
 */
/** 
* @ClassName: IUserInfoService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author jackstraw_yu
* @date 2018年1月23日 上午10:54:31 
*  
*/
public interface IUserInfoService {
    
    /**
     * 创建新用户
     * @author: wy
     * @time: 2018年1月17日 下午3:59:01
     * @param sellerNick 卖家昵称
     * @param openUid 卖家唯一标识
     * @param taoBaoUserId 淘宝用户id
     * @param sessionKey 卖家秘钥
     * @param sellerExpireTime 卖家软件过期时间
     * @param expiresIn sessionKey过期时间
     * @return 成功返回true，失败返回false
     */
    public boolean doCreateNewUser(String sellerNick, String openUid,String taoBaoUserId,String sessionKey, 
            Date sellerExpireTime, String expiresIn);
    
    /** 
     * @Description: 用户充值
     * @param  userRecharge    充值信息实体 
     * @return void    返回类型 
     * @author jackstraw_yu
     * @date 2017年12月4日 下午6:15:38
     */
     public void doUserRecharge(UserRecharge userRecharge);
     
     /**
      * 定义不明确，有其他方法替代
      * 根据用户昵称查询用户信息
      * 改用  findUserInfoByTaobaoUserNick()
      */
     @Deprecated
     public UserInfo findUserInfo(String taobaoUserNick);
     
     public UserInfo findUserInfoByTaobaoUserNick(String taobaoUserNick);
     
     /**
      * 根据用户id查询用户信息
      */
     public UserInfo findUserInfo(Long uid);
     
     /**
      * 根据用户昵称查询用户信息
      */
     public UserInfo findUserInfoByTmc(String taobaoUserNick);
     
     /**
      * 来自tmc的充值
      */
     public void addUserRechargeByTmc(String json);
     
     /**
      * 查询用户主键id是否存在
      * @author: wy
      * @time: 2017年11月13日 下午6:14:23
      * @param sellerNick 买家昵称
      * @return 存在返回true，不成功返回false
      */
     public boolean isExistsById(long uid);
     
     /**
      * tmc专用 查询用户的过期时间和状态
      */
     public UserInfo findExpireTimeAndSms(String taobaoUserNick);
     /**
      * 通过淘宝唯一标识查询用户的主键id（用户是否存在）
      * @author: wy
      * @time: 2018年1月18日 下午2:56:16
      * @param openUid 淘宝账号唯一标识
      * @return 用户主键id，用户不存在时返回空
      */
     public Long findUidByOpenUid(String openUid);
     
     /**
      * 根据用户主键id更新用户信息
      * @author: wy
      * @time: 2018年1月18日 下午3:32:40
      * @param uid 用户主键id
      * @param sellerNick 卖家昵称
      * @param sessionKey 卖家秘钥
      * @param sellerExpireTime 卖家过期时间
      * @param expiresIn 卖家秘钥过期剩余时间
      * @param loginDate 用户登录时间
      */
     public void updateUserInfoByLogin(long uid,String sellerNick,String sessionKey, Date sellerExpireTime, 
             String expiresIn,Date loginDate,String openUid) ;
     
     /** 
      * @Description: 首页小红包标记为不显示状态
      * @param  userId
      * @param  hasProvide   
      * @return void    返回类型 
      * @author jackstraw_yu
      * @date 2018年1月19日 下午5:11:31
      */
     public void updateUserHasProvide(String userNick, boolean hasProvide);

 	/** 
     * @Description: 首页小红包,保存用户手机号,标记怎送500条短信 
     * @param  mobile
     * @param  qqNum
     * @param  user
     * @return boolean    返回类型 
     * @author jackstraw_yu
     * @date 2018年1月22日 下午3:36:42
     */
    public boolean saveUserMobileInfo(String mobile,String qqNum,UserInfo user);

	/** 
	* @Description 查询用户的店铺名称<br/>
	* 有设置店铺名称返回店铺名称,没有店铺名称返回用户昵称
	* @param  user
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 上午10:36:31
	*/
	public String queryShopName(UserInfo user);

	/** 
	* @Description 更新用户的店铺名称
	* @param  user    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 上午10:54:33
	*/
	public void modiftyShopName(UserInfo user);
      
	/**
	 * 查找用户主键id通过卖家昵称
	 * @author: wy
	 * @time: 2018年1月25日 下午5:30:23
	 * @param sellerNick
	 * @return
	 */
	public Long findUidBySellerNick(String sellerNick);
	
	/**
	 * 查询所有token不为空的用户
	 * @Title: listTokenNotNull 
	 * @param @return 设定文件 
	 * @return List<Long> 返回类型 
	 * @throws
	 */
	public List<Long> listTokenNotNull();
	
	/**
	 * 查询所有的用户
	 * @Title: listAllUser 
	 * @param @return 设定文件 
	 * @return List<UserInfo> 返回类型 
	 * @throws
	 */
	public List<UserInfo> listAllUser();
	
	
	/**
	 * 通过accessToken查询用户
	 * @Title: findUserBytoken 
	 * @param @param accessToken
	 * @param @return 设定文件 
	 * @return UserInfo 返回类型 
	 * @throws
	 */
	public UserInfo findUserBytoken(String accessToken);
	
	/**
	 * 通过id查询accessToken
	 * @Title: findUserTokenById 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	public String findUserTokenById(Long uid);
	
	/**
	 * 保存更新用户设置在首页的快捷链接
	 * @Title: updateUserShortcutLinkStr 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long updateUserShortcutLinkStr(Long uid, String linkStr);
	
	/**
	 * 通过taobaoUserId查询用户信息
	 * @param taobaoUserId
	 * @return UserInfo
	 * @author sungk
	 */
	public UserInfo findUserInfoByTaobaoUserId(String taobaoUserId);

	public UserInfo queryUserTokenInfo(String userNickName) throws Exception;
	
	UserInfo findExpireTimeAndSms(Long uid);
	
	String findLinkByUid(Long uid);

	boolean addUserPermitByMySql(Long uid, String addType);

	Long addUserInfo(String sellerNick, String taoBaoUserId, String sessionKey,
			Date sellerExpireTime, String expiresIn);

	void doCreateInfoByLogin(UserLoginInfo userLoginInfo) throws Exception;

	/**
	 * 定时检查没有等级的用户，并更新用户等级
	 * @author HL
	 * @time 2018年11月9日 上午10:42:29
	 */
	void updateUserInfoLevel();
    /**
     * 更新用户的过期时间
     * @param map
     */
	public void updateUserInfoExpirationTime(Map<String, Object> map);

}
