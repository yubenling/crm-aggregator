package com.kycrm.member.dao.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.domain.vo.user.UserRechargeVO;

/**
 * @author wy
 * @version 创建时间：2018年1月12日 下午12:12:22
 */
public interface IUserRechargeDao {
	/**
	 * 根据订单编号查询充值信息是否存在
	 * 
	 * @param orderId
	 * @return
	 */
	public Long queryUserRechargeCount(String orderId);

	/**
	 * 根据订单编号查询充值详细信息
	 * 
	 * @param orderId
	 * @return UserRecharge
	 */
	public UserRecharge getUserRechargeInfo(String orderId);

	/**
	 * 更改充值记录状态
	 * 
	 * @param userRecharge
	 */
	public void updateUserRechargeStatus(UserRecharge userRecharge);

	/**
	 * 保存充值记录
	 * 
	 * @param ur
	 */
	public int saveUserRechar(UserRecharge userRecharge);

	/**
	 * 查询充值记录状态
	 * 
	 * @param payTrade
	 * @return
	 */
	public String findPayStatus(String payTrade);

	/**
	 * 根据用户id查询充值记录
	 * 
	 * @author HL
	 * @time 2018年1月31日 下午6:13:47
	 * @param urVo
	 * @return
	 */
	public List<UserRecharge> findRechargeRecordlist(UserRechargeVO urVo);

	/**
	 * 根据用户id查询充值记录数
	 * 
	 * @author HL
	 * @time 2018年1月31日 下午6:13:47
	 * @param urVo
	 * @return
	 */
	public long findRechargeRecordListCount(UserRechargeVO urVo);

	public Long countReport(Map<String, Object> map) throws Exception;

	public List<UserRecharge> countReportByDate(Map<String, Object> map) throws Exception;

	public Long findRechargeRecordCountByType(@Param("uid") Long uid, @Param("bTime") Date bTime,
			@Param("eTime") Date eTime, @Param("rechargeType") String rechargeType) throws Exception;
}
