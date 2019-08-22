package com.kycrm.tmc.schdule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.service.message.ISmsSendInfoService;



/** 
* @author wy
* @version 创建时间：2017年9月6日 下午5:59:26
*/
@Service
public class TmcDeleteOldSms {
	@Autowired
	private ISmsSendInfoService smsSendInfoService;
	
	public void doHandle(){
		SimpleDateFormat fomartDate = new SimpleDateFormat("yyyy-MM-dd");
		String startSendTime = fomartDate.format(new Date());
		Date startDate = null;
		try {
			startDate = fomartDate.parse(startSendTime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		if(startDate==null){
			return;
		}
		int count = this.smsSendInfoService.findSmsByRemoveCount(startDate);
		int rage = ISmsSendInfoService.RANGE;
		int page = (count+rage-1)/rage;
		int i = 0;
		while(i<=page){
			//定时删除超过两条的 订单中心的短信
			List<Long> ids = this.smsSendInfoService.findSmsByRemove(i,startDate);
			this.smsSendInfoService.removeIds(ids);
			i++;
		}
	}
}
