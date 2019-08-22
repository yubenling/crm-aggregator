package com.kycrm.member.service.user;



import com.kycrm.member.domain.entity.user.UserBillInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.user.UserBillInfoVO;

public interface IUserBillInfoService {


	Integer saveBillInfo(UserInfo user, UserBillInfoVO userBillInfoVO);

	UserBillInfo selcetBillInfo(Long uid);

	Integer updateBillInfo(UserInfo user, UserBillInfoVO userBillInfoVO);

}
