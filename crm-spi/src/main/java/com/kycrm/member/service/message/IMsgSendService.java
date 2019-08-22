package com.kycrm.member.service.message;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.vo.message.SendMsgVo;
	/** 
	* @ClassName: MsgSendService 
	* @Description: TODO(会员短信群发>>发送短信) 
	* @author yu
	* @date 2018年7月6日 上午10:16:58 
	*  
	*/
public interface IMsgSendService {

	    /**
	     * 群发短信
	     * @param sendMsgVo
	     * @throws Exception
	     */
        public void sendBatchMsg(SendMsgVo sendMsgVo) throws Exception;
		
		/**
		 * 调用短信群发接口,返回成功失败的条数 
		 */
		public Map<String,Integer> batchSendMsg(List<MemberInfoDTO> sendNums,SendMsgVo sendMsgVo);

	
}
