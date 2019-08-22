package com.kycrm.member.service.message;

import java.util.Map;

import com.kycrm.member.domain.entity.message.BatchSmsData;

public interface IMultithreadBatchIndividuationSmsService {
	/**
     * 群发个性化短信
     */
    public Map<String, Integer> batchIndividuationSms(final BatchSmsData obj);

}
