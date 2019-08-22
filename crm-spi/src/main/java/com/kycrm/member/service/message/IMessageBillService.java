package com.kycrm.member.service.message;

import java.util.Date;

public interface IMessageBillService {

	public Long getMessageBillByDate(Long uid, Date bTime, Date eTime) throws Exception;

}
