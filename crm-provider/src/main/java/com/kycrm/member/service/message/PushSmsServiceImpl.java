package com.kycrm.member.service.message;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kycrm.member.util.ReturnMessage;
import com.kycrm.member.util.sms.SendMessageUtil;
import com.kycrm.member.util.sms.SendMessageUtilForHangYe;
import com.kycrm.util.SendMessageStatusInfo;
import com.kycrm.util.ValidateUtil;

/** 
* @author wy
* @version 创建时间：2018年1月15日 下午3:19:09
*/
@Service("pushSmsService")
public class PushSmsServiceImpl implements IPushSmsService{
    /**
     * 发送营销短信
     * @author: wy
     * @time: 2018年1月15日 下午3:22:36
     */
    @Override
    public String sendSmsByMarketing(String phone,String content){
        if(ValidateUtil.isEmpty(phone) || ValidateUtil.isEmpty(content)){
            return SendMessageStatusInfo.FORBIDDEN_CHARACTER;
        }
        String sendStatus = SendMessageUtil.sendMessage(phone,content, null, null);
        ReturnMessage returnMessage = JSON.parseObject(sendStatus, ReturnMessage.class);
        sendStatus = returnMessage.getReturnCode();
        return sendStatus;
    }
    /**
     * 发送单条短信
     * @author: wy
     * @time: 2018年1月15日 下午3:24:15
     */
    @Override
    public String sendSmsBySingle(String phone,String content){
        if(ValidateUtil.isEmpty(phone) || ValidateUtil.isEmpty(content)){
            return SendMessageStatusInfo.FORBIDDEN_CHARACTER;
        }
        String sendStatus = SendMessageUtilForHangYe.sendMessage(phone,content, null, null);
        ReturnMessage returnMessage = JSON.parseObject(sendStatus, ReturnMessage.class);
        sendStatus = returnMessage.getReturnCode();
        return sendStatus;
    }
}
