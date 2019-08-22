package com.kycrm.member.dao.user;


import java.util.Map;

import com.kycrm.member.domain.entity.user.UserBillInfo;
import com.kycrm.member.domain.vo.user.UserBillInfoVO;

public interface IUserBillInfoDao {

	UserBillInfo selcetBillInfo(Map<String, Object> map);

	Integer saveBillInfo(UserBillInfoVO userBillInfoVO);

	Integer updateBillInfo(UserBillInfoVO userBillInfoVO);

	

}
