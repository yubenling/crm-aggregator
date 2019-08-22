
/** 
* @Title: IUserDao.java 
* @Package com.kycrm.member.dao 
* Copyright: Copyright (c) 2017 
* Company:北京冰点零度科技有限公司 *
* @author zlp 
* @date 2018年1月3日 下午2:59:27 
* @version V1.0
*/
package com.kycrm.syn.dao.user;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.user.UserInfo;

/** 
 * @ClassName: IUserDao  
 * @author zlp 
 * @date 2018年1月3日 下午2:59:27 *  
 */
public interface IUserInfoDao {
    public UserInfo findUserInfoByTaobaoNick(Long uid);
    
    public UserInfo findUserInfoByTmc(String userNick);
    
    public UserInfo findUserInfoByUid(Long uid);
    
    public UserInfo queryTokenInfo(Long uid);
    
    public UserInfo findExpireTimeAndSms(Long uid);
    
    public String findSessionTokenBySellerNick(Long uid);
    
    public Integer findExistsById(long uid);
    
    public void doCreateNewUser(UserInfo userInfo);
    
    public Long findUidByOpenUid(String openUid);
    
    /**
     * 登录成功后更新用户数据
     * @author: wy
     * @time: 2018年1月18日 下午3:36:34
     * @param map 参考 IUserInfoService.updateUserInfo 方法参数
     */
    public void updateUserInfoByLogin(Map<String,Object> map);

	/** 
	* @Description  首页小红包标记为不显示状态
	* @param  map
	* @return int    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月19日 下午5:30:10
	*/
	public int updateUserHasProvide(Map<String, Object> map);

	/** 
	* @Description 首页小红包保存用户信息
	* @param  map
	* @return int    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月22日 下午3:29:31
	*/
	public int saveUserMobileInfo(Map<String, Object> map);

	/** 
	* @Description 查询用户的店铺名称<br/>
	* 有设置店铺名称返回店铺名称,没有店铺名称返回用户昵称
	* @param  user
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 上午10:36:31
	*/
	public String queryShopName(Long id);

	/** 
	* @Description 更新用户的店铺名称
	* @param  user    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 上午10:54:33
	*/
	public int modiftyShopName(UserInfo user);
	
	public Long findUidBySellerNick(String sellerNick);
	
	/**
	 * 查询
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
	public Long updateUserShortcutLinkStr(Long uid);
	
	/**
	 * 通过taobaoUserId查询用户信息
	 * @param taobaoUserId
	 * @return UserInfo
	 * @author sungk
	 */
	public UserInfo findUserInfoByTaobaoUserId(String taobaoUserId);
	
	public List<UserInfo> findAll();
}
