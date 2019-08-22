package com.kycrm.member.service.other;

import com.kycrm.member.domain.entity.other.MobileSetting;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.tradecenter.ShortLinkVo;

/** 
* @ClassName: IMobileSettingService 
* @Description 后台管理设置
* @author jackstraw_yu
* @date 2018年1月22日 下午4:20:51 
*  
*/
public interface IMobileSettingService {

	/** 
	* @Description 根据用户id查询后台管理设置
	* @param  uid
	* @return MobileSetting    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月22日 下午4:21:20
	*/
	public MobileSetting findMobileSetting(Long uid);

	/** 
	* @Description 保存一条后台管理设置
	* @param  mobileSetting 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月22日 下午4:24:42
	*/
	public Long saveMobileSetting(MobileSetting mobileSetting);
	
	/** 
	* @Description 初始化保存后台管理设置:<br/> 
	* 1:保存一条初始化后台设置<br/>
	* 2:补全用户手机号,标记赠送短信<br/>
	* 3:赠送用户500条短信
	* @param  mobileSetting   后台管理设置 
	* @param  user   用户 
	* @return Long    返回保存主键
	* @author jackstraw_yu
	* @date 2018年1月22日 下午6:34:33
	*/
	public Long saveInitMobileSetting(MobileSetting mobileSetting,UserInfo user,String qqNum);
	
	
	/** 
	* @Description: 更新后台管理设置
	* @param  mobileSetting   后台管理设置 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月22日 下午6:34:33
	*/
	public void updateMobileSetting(MobileSetting mobileSetting,UserInfo user);

	
	/**
	 * 发送验证码
	 * @author HL
	 * @time 2018年7月26日 下午3:03:37 
	 * @param content
	 * @param mobile
	 * @return
	 */
	public boolean sendSecurityCodeMessage(String content, String mobile);
    /**
     * 
     * @param slo
     * @return
     */
	public String getLink(String token ,ShortLinkVo slo);

	/**
	 * 短信不足提醒
	 * @time 2018年9月7日 下午1:56:31 
	 * @param uid
	 */
	public void proxyResetSmsRemindMark(Long uid);
	
	/**
	 * --------此方法有坑，请勿调用！！！
	 * 定时执行--开启任务
	 * @time 2018年9月7日 下午1:54:55
	 */
	void scanOpenMobileSetting();

	/**
	 * --------此方法有坑，请勿调用！！！
	 * 定时执行 --每天晚上12点;短信不足提醒已发送标记重置
	 * @time 2018年9月7日 下午1:54:39
	 */
	void resetMobileSettingFlag();
}
