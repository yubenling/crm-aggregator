package com.kycrm.member.service.message;

import com.kycrm.member.domain.vo.message.BatchSendMemberMessageBaseVO;

public interface IBatchSendMemberMessageService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员短信群发
	 * @Date 2018年12月11日下午4:12:38
	 * @param uid
	 * @param batchSendMemberMessageBaseVO
	 * @param isSendScheduleMessage
	 * @throws Exception
	 * @ReturnType void
	 */
	public void batchSendMemberMessageBaseMethod(Long uid, BatchSendMemberMessageBaseVO batchSendMemberMessageBaseVO,
			boolean isSendScheduleMessage) throws Exception;

}
