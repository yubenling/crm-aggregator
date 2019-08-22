package com.kycrm.member.service.user;

import java.math.BigDecimal;
import java.util.List;

import com.kycrm.member.domain.entity.user.UserBillRecord;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserPayBill;
import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.domain.vo.user.UserBillRecordVO;
import com.kycrm.member.domain.vo.user.UserPayBillVO;

public interface IUserPayBillService {

	List<UserPayBill> selectPayBillList(UserPayBillVO userPayBillVO, UserInfo user);
    
	List<UserPayBill> findAllPayBillByStatus(Long id,String status);
    
	List<UserPayBill> findPayBillByOrderId(UserInfo user,String orderIdList);

	void updatePayBillStatus(List<UserPayBill> payBillList);


	void updatePayBillStatusByRecord(UserInfo user,
			UserBillRecord userBillRecord);

	List<UserPayBill> selectPayBillByRecordId(UserInfo user, UserBillRecordVO userBillRecord);

	BigDecimal selectAllPayBillAmont(UserInfo user);
  
	Long insertPayBill(UserRecharge ur);

	Integer selectPayBillListCount(UserPayBillVO userPayBillVO, UserInfo user);

	Integer selectPayBillByRecordIdCount(UserInfo user,
			UserBillRecordVO userBillRecord);

	
   
	

}
