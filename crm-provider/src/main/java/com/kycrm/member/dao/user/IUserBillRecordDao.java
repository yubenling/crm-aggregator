package com.kycrm.member.dao.user;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.user.UserBillRecord;
import com.kycrm.member.domain.vo.user.UserBillRecordVO;

public interface IUserBillRecordDao {

	List<UserBillRecordVO> selectBillRecordList(
			UserBillRecordVO userBillRecordVo);

	Long saveBillRecord(UserBillRecord userBillRecord);

	void updateBillRecordStatus(Map<String, Object> map);

	void updateRecordBillInfo(UserBillRecordVO userBillRecord);

	Integer deleteBillRecordById(Map<String, Object> map);

	UserBillRecord selectBillRecordById(Map<String, Object> map);

	Integer selectBillRecordListCount(UserBillRecordVO userBillRecordVo);

	

}
