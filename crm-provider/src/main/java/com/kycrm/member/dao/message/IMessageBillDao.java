package com.kycrm.member.dao.message;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

public interface IMessageBillDao {

	public Long getMessageBillByDate(@Param("uid") Long uid, @Param("bTime") Date bTime, @Param("eTime") Date eTime)
			throws Exception;

}
