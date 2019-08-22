package com.kycrm.member.component;

import com.kycrm.member.domain.entity.message.BatchSmsData;

public interface IMessageQueue {
	public void putSmsDataToQueue(BatchSmsData batchSmsData);
}
