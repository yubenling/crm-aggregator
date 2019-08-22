package com.kycrm.member.service.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.kycrm.member.domain.entity.message.MsgSendRecord;

public interface IMsgTempTradeQueueService {

	public static BlockingQueue<MsgSendRecord> queue = new ArrayBlockingQueue<>(1000);

	void handleTradeEffectData();

}
