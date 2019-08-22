package com.kycrm.syn.service.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.core.mybatis.MyDataSourceAspect;
import com.kycrm.syn.dao.user.IUserInfoDao;

@MyDataSource(MyDataSourceAspect.MASTER)
@Component("userInfoService")
public class UserInfoService {

	@Autowired
	private IUserInfoDao userInfoDao;

	public UserInfo findUserInfoByTaobaoNick(Long uid) throws Exception {
		return this.userInfoDao.findUserInfoByTaobaoNick(uid);
	}

	public UserInfo findUserInfoByTmc(String userNick)throws Exception{
		return this.userInfoDao.findUserInfoByTmc(userNick);
	}

	public UserInfo findUserInfoByUid(Long uid)throws Exception{
		return this.userInfoDao.findUserInfoByUid(uid);
	}

	public UserInfo queryTokenInfo(Long uid)throws Exception{
		return this.userInfoDao.queryTokenInfo(uid);
	}

	public UserInfo findExpireTimeAndSms(Long uid)throws Exception{
		return this.userInfoDao.findExpireTimeAndSms(uid);
	}

	public String findSessionTokenBySellerNick(Long uid)throws Exception{
		return this.userInfoDao.findSessionTokenBySellerNick(uid);
	}

	public Integer findExistsById(long uid)throws Exception{
		return this.userInfoDao.findExistsById(uid);
	}

	public void doCreateNewUser(UserInfo userInfo) throws Exception{
		this.userInfoDao.doCreateNewUser(userInfo);
	}

	public Long findUidByOpenUid(String openUid) throws Exception{
		return this.userInfoDao.findUidByOpenUid(openUid);
	}

	/**
	 * 登录成功后更新用户数据
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 下午3:36:34
	 * @param map
	 *            参考 IUserInfoService.updateUserInfo 方法参数
	 */
	public void updateUserInfoByLogin(Map<String, Object> map) throws Exception{
		this.userInfoDao.updateUserInfoByLogin(map);
	}

	/**
	 * @Description 首页小红包标记为不显示状态
	 * @param map
	 * @return int 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月19日 下午5:30:10
	 */
	public int updateUserHasProvide(Map<String, Object> map) throws Exception{
		return this.userInfoDao.updateUserHasProvide(map);
	}

	/**
	 * @Description 首页小红包保存用户信息
	 * @param map
	 * @return int 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 下午3:29:31
	 */
	public int saveUserMobileInfo(Map<String, Object> map) throws Exception{
		return this.userInfoDao.saveUserMobileInfo(map);
	}

	/**
	 * @Description 查询用户的店铺名称<br/>
	 *              有设置店铺名称返回店铺名称,没有店铺名称返回用户昵称
	 * @param user
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月23日 上午10:36:31
	 */
	public String queryShopName(Long id) throws Exception{
		return this.userInfoDao.queryShopName(id);
	}

	/**
	 * @Description 更新用户的店铺名称
	 * @param user
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月23日 上午10:54:33
	 */
	public int modiftyShopName(UserInfo user) throws Exception{
		return this.userInfoDao.modiftyShopName(user);
	}

	public Long findUidBySellerNick(String sellerNick) throws Exception{
		return this.userInfoDao.findUidBySellerNick(sellerNick);
	}

	/**
	 * 查询 @Title: listTokenNotNull @param @return 设定文件 @return List<Long>
	 * 返回类型 @throws
	 */
	public List<Long> listTokenNotNull() throws Exception{
		return this.userInfoDao.listTokenNotNull();
	}

	/**
	 * 查询所有的用户 @Title: listAllUser @param @return 设定文件 @return List<UserInfo>
	 * 返回类型 @throws
	 */
	public List<UserInfo> listAllUser() throws Exception{
		return this.userInfoDao.listAllUser();
	}

	/**
	 * 通过accessToken查询用户 @Title: findUserBytoken @param @param
	 * accessToken @param @return 设定文件 @return UserInfo 返回类型 @throws
	 */
	public UserInfo findUserBytoken(String accessToken) throws Exception{
		return this.userInfoDao.findUserBytoken(accessToken);
	}

	/**
	 * 通过id查询accessToken @Title: findUserTokenById @param @param
	 * uid @param @return 设定文件 @return String 返回类型 @throws
	 */
	public String findUserTokenById(Long uid) throws Exception{
		return this.findUserTokenById(uid);
	}

	/**
	 * 保存更新用户设置在首页的快捷链接 @Title: updateUserShortcutLinkStr @param @param
	 * uid @param @return 设定文件 @return Long 返回类型 @throws
	 */
	public Long updateUserShortcutLinkStr(Long uid) throws Exception {
		return this.userInfoDao.updateUserShortcutLinkStr(uid);
	}

	/**
	 * 通过taobaoUserId查询用户信息
	 * 
	 * @param taobaoUserId
	 * @return UserInfo
	 * @author sungk
	 */
	public UserInfo findUserInfoByTaobaoUserId(String taobaoUserId) throws Exception {
		return this.userInfoDao.findUserInfoByTaobaoUserId(taobaoUserId);
	}

	public List<UserInfo> findAll() throws Exception {
		return this.userInfoDao.findAll();
	}
}
