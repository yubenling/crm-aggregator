package com.kycrm.member.dao.other;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.other.MobileSetting;

/** 
* @author wy
* @version 创建时间：2018年1月12日 下午3:43:33
*/
public interface IMobileSettingDao {
    
    /** 
    * @Description: 根据uid后台管理设置
    * @param  uid
    * @return MobileSetting    返回类型 
    * @author jackstraw_yu
    * @date 2018年1月22日 下午7:01:03
    */
    public MobileSetting findMobileSetting(Long uid);
   
    /** 
    * @Description 保存后台设置
    * @param @param mobileSetting
    * @return Long    返回类型 
    * @author jackstraw_yu
    * @date 2018年1月22日 下午6:59:10
    */
    public Long saveMobileSetting(MobileSetting mobileSetting);
    
    /** 
    * @Description 更新后台设置
    * @param  mobileSetting
    * @return int    返回类型 
    * @author jackstraw_yu
    * @date 2018年1月22日 下午6:58:39
    */
    public int updateMobileSetting(MobileSetting mobileSetting);
   
   
   /**
    * 每天晚上12点;短信不足提醒已发送标记重置 
    * @time 2018年9月7日 下午12:08:25
    */
    public void resetMobileSettingFlag();

	/**
	 * 开启催付效果的户总条数
	 * @time 2018年9月6日 下午3:22:20 
	 * @return
	 */
	public Long findOpenExpeditingCount();

	/**
	 * 开启催付效果的用户信息
	 * @time 2018年9月6日 下午3:22:11 
	 * @param map
	 * @return
	 */
	public List<MobileSetting> findOpenExpeditingList(Map<String, Object> map);

	/**
	 * 软件过期提醒用户总条数
	 * @time 2018年9月6日 下午5:53:25 
	 * @return
	 */
	public Long findOpenServiceExpireCount();

	/**
	 * 软件过期提醒用户信息
	 * @time 2018年9月6日 下午6:29:17 
	 * @param map
	 * @return
	 */
	public List<MobileSetting> findOpenServiceExpireList(Map<String, Object> map);

	/**
	 * 短信余额不足提醒总条数
	 * @time 2018年9月6日 下午6:33:59 
	 * @return
	 */
	public Long findOpenMessageRemainderCount();

	/**
	 * 短信余额不足提醒用户信息
	 * @time 2018年9月6日 下午6:35:24 
	 * @param map
	 * @return
	 */
	public List<MobileSetting> findOpenMessageRemainderList(
			Map<String, Object> map);

	
	/**
	 * 标记已发送
	 * @param id 
	 * @time 2018年9月7日 下午12:32:53
	 */
	public void updateMobileSettingFlag(Long id);

	/**
	 * 查询是否开启余额不足提醒
	 * @param uid 
	 * @time 2018年9月7日 下午2:02:27 
	 * @return
	 */
	public MobileSetting findMessageRemainderByUid(Long uid);
}
