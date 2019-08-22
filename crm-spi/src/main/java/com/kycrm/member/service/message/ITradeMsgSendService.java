package com.kycrm.member.service.message;

import com.kycrm.member.domain.entity.eco.log.LogAccessDTO;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;

/**
 * 订单短信群发
 * @ClassName: ITradeMsgSendService  
 * @author ztk
 * @date 2018年8月27日 下午1:40:21
 */
public interface ITradeMsgSendService {

	void batchSendOrderMsg(TradeMessageVO messageVO, LogAccessDTO logAccessDTO)
			throws Exception;

}
