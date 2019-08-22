package com.kycrm.member.service.user;

import java.util.List;

import com.kycrm.member.domain.entity.user.UserBillRecord;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserPayBill;
import com.kycrm.member.domain.vo.user.UserBillRecordVO;

public interface IUserBillRecordService {

	List<UserBillRecordVO> selectBillRecordList(UserInfo user,
			UserBillRecordVO userBillRecordVo);
    /**
     * 根据打款记录保存发票记录表
     * @param payBillList
     * @param userBillRecordVO
     * @return
     */
	Long saveBillRecord(List<UserPayBill> payBillList, UserBillRecordVO userBillRecordVO);
	/**
	 * 修改发票记录的状态
	 * @param user
	 * @param userBillRecord
	 */
	void updateBillRecordStatus(UserInfo user, UserBillRecord userBillRecord);
	void updateRecordBillInfo(UserBillRecordVO userBillRecord);
	/**
	 * 根据id删除发票记录
	 * @param id
	 * @return
	 */
	Integer deleteBillRecordById(Long id);
	/**
	 * 根据id,查询发票记录信息
	 * @param id
	 * @return
	 */
	UserBillRecord selectBillRecordById(Long id);
	//查询总条数
	Integer selectBillRecordListCount(UserInfo user,
			UserBillRecordVO userBillRecordVo);

}
