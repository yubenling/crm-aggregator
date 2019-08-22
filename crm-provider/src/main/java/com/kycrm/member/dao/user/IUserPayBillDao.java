package com.kycrm.member.dao.user;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.user.UserBillRecord;
import com.kycrm.member.domain.entity.user.UserPayBill;
import com.kycrm.member.domain.vo.user.UserBillRecordVO;
import com.kycrm.member.domain.vo.user.UserPayBillVO;

public interface IUserPayBillDao {

	List<UserPayBill> selectPayBillList(Map<String, Object> map);

	List<UserPayBill> findAllPayBillByStatus(Map<String, Object> map);

	List<UserPayBill> findPayBillByOrderId(Map<String, Object> map);

	void updatePayBillStatus(UserPayBill bill);

	List<UserPayBill> selectPayBillByRecord(Map<String, Object> map);

	BigDecimal selectAllPayBillAmont(Map<String, Object> map);

	Long insertPayBill(UserPayBill userPayBill);

	Integer selectPayBillListCount(Map<String, Object> map);

	Integer selectPayBillByRecordIdCount(Map<String, Object> map);

	

	

}
