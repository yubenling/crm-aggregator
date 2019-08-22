package com.kycrm.member.service.trade.thread;

import com.kycrm.member.domain.entity.eco.log.LogAccessDTO;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;
import com.kycrm.member.service.message.ITradeMsgSendService;

public class TradeSendMessageThread implements Runnable{
	
	private ITradeMsgSendService tradeMsgSendService;
	private TradeMessageVO messageVO;
	private LogAccessDTO logAccessDTO;
	
	public TradeSendMessageThread(ITradeMsgSendService tradeMsgSendService,TradeMessageVO messageVO,
			LogAccessDTO logAccessDTO){
		this.tradeMsgSendService = tradeMsgSendService;
		this.messageVO = messageVO;
		this.logAccessDTO = logAccessDTO;
	}
	
	@Override
	public void run() {
		try {
			tradeMsgSendService.batchSendOrderMsg(messageVO,logAccessDTO);
			System.out.println("~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!!@@@@@@@@@@@@@@@@@@@@@@@@@@@thread");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
