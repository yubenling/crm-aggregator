package com.kycrm.member.service.message;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.message.IMessageBillDao;

@MyDataSource
@Service("messageBillService")
public class MessageBillServiceImpl implements IMessageBillService {

	@Autowired
	private IMessageBillDao messageBillDao;

	@Override
	public Long getMessageBillByDate(Long uid, Date bTime, Date eTime) throws Exception {
		return this.messageBillDao.getMessageBillByDate(uid, bTime, eTime);
	}

}
