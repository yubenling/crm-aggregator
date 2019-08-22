package com.kycrm.member.service.user;

import java.util.Date;
import java.util.Map;

import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.domain.vo.user.UserRechargeVO;

/**
 * @author wy
 * @version 创建时间：2018年1月12日 下午12:09:38
 */
public interface IUserRechargeService {
	/**
	 * 根据订单编号查询充值信息是否存在
	 * 
	 * @param orderId
	 * @return
	 */
	public Long getUserRechar(String orderId);

	/**
	 * 根据订单编号查询充值详细信息
	 * 
	 * @param orderId
	 * @return UserRecharge
	 */
	public UserRecharge getUserRechargeInfo(String orderId);

	/**
	 * 查询充值记录状态
	 * 
	 * @param payTrade
	 * @return
	 */
	public String findPayStatus(String payTrade);

	/**
	 * 保存充值记录
	 * 
	 * @param ur
	 */
	public int saveUserRechar(UserRecharge ur);

	/**
	 * 更改充值记录状态
	 * 
	 * @param userRecharge
	 */
	public void updateUserRechargeStatus(UserRecharge userRecharge);

	/**
	 * 根据用户id查询充值记录
	 * 
	 * @author HL
	 * @time 2018年1月31日 下午6:13:47
	 * @param urVo
	 * @return
	 */
	public Map<String, Object> findRechargeRecordList(UserRechargeVO urVo);

	public Long findRechargeRecordCount(Long uid, String dateType, Date bTime, Date eTime) throws Exception;

	public Long findRechargeRecordCountByType(Long uid, Date bTime, Date eTime, String rechargeType) throws Exception;

	public Map<String, UserRecharge> findRechargeRecordCountByDate(Long uid, String dateType, Date bTime, Date eTime)
			throws Exception;
}
