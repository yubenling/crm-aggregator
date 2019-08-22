package com.kycrm.member.service.message;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.message.ISmsSendInfoScheduleDao;
import com.kycrm.member.domain.entity.message.SmsSendInfo;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.exception.KycrmApiException;
import com.kycrm.member.service.user.JudgeUserUtil;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.SecretException;

@MyDataSource(MyDataSourceAspect.MASTER)
@Service("smsSendInfoScheduleService")
public class SmsSendInfoScheduleServiceImpl implements ISmsSendInfoScheduleService {
	private final Logger logger = org.slf4j.LoggerFactory.getLogger(SmsSendInfoScheduleServiceImpl.class);
	@Autowired
	private JudgeUserUtil judgeUserUtil;
	@Autowired
	private ISmsSendInfoScheduleDao smsSendInfoScheduleDao;
	@Override
	public boolean doCreateRate(Long uid,String sellerNick,String content,String rateType,Date startDate,Long tid,Long oid){
		if(startDate==null){
			return false;
		}
		if(startDate.getTime()<(System.currentTimeMillis()+1000)){
			startDate= new Date(System.currentTimeMillis()+1000);
		}
		SmsSendInfo smsSendInfo = new SmsSendInfo();
		smsSendInfo.setNickname("~Et8xePTHeXvgJUUGR2edvgAsd==~gUkS56uv~2~~");//假用户名
		smsSendInfo.setPhone("$LlJUyVaAffUSJb+UasdJHTbQ==$EYqiNasdd+39TXm3a3msQ==$1$$");
		smsSendInfo.setRateType(rateType);
		smsSendInfo.setContent(content);
		smsSendInfo.setUid(uid);
		smsSendInfo.setUserId(sellerNick);
		smsSendInfo.setType(OrderSettingInfo.AUTO_RATE);
		smsSendInfo.setTid(tid);
		smsSendInfo.setOid(oid);
		smsSendInfo.setStartSend(startDate);
		Integer count = this.smsSendInfoScheduleDao.doCreateByRate(smsSendInfo); 
		if(count!=1){
			return false;
		}
		return true;
	}
	/**
	 * 自动创建定时发送短信任务
	 * @param smsSendInfo
	 * @return
	 */
	@Override
	public boolean doAutoCreate(final SmsSendInfo smsSendInfo){
		if(smsSendInfo==null){
			return false;
		}
		try {
			String session = judgeUserUtil.getUserTokenByRedis(smsSendInfo.getUid());
			if(!EncrptAndDecryptClient.isEncryptData(smsSendInfo.getNickname(), EncrptAndDecryptClient.SEARCH)){
				smsSendInfo.setNickname(EncrptAndDecryptClient.getInstance().encrypt(smsSendInfo.getNickname(), EncrptAndDecryptClient.SEARCH, session));
			}
		} catch (SecretException e) {
			return false;
		}
		if(smsSendInfo.getStartSend()==null){
			logger.debug("************tid:"+smsSendInfo.getTid()+",类型："+(String)smsSendInfo.getType()+",开始时间为空，不允许创建无开始时间的短信   **********");
			return false;
		}
		if(smsSendInfo.getStartSend().getTime()<(System.currentTimeMillis()+60000)){
			smsSendInfo.setStartSend(new Date(System.currentTimeMillis()+60000));
		}
		if(smsSendInfo.getEndSend()!=null){
			if(smsSendInfo.getEndSend().getTime()<smsSendInfo.getStartSend().getTime()){
				logger.debug("************tid:"+smsSendInfo.getTid()+",类型："+(String)smsSendInfo.getType()+",结束时间比开始比时间小，错误   **********");
				return false;
			}
		}
		Integer count = this.smsSendInfoScheduleDao.findMessageByAdd(smsSendInfo);
		if(count==0){
			logger.debug("*******************tid:"+smsSendInfo.getTid()+",类型："+(String)smsSendInfo.getType()+",创建定时短信  开始********************");
			int i = 0;
			try {
				i = this.smsSendInfoScheduleDao.doCreateByScheduleSend(smsSendInfo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.debug("*******************tid:"+smsSendInfo.getTid()+",类型："+(String)smsSendInfo.getType()+",创建定时短信  插入："+i+"条********************");
		}else{
			logger.debug("*******************tid:"+smsSendInfo.getTid()+",类型："+(String)smsSendInfo.getType()+",创建定时短信  失败有重复数据"+count+"********************");
		}
		return true;
	}
	/**
	 * 当项目启动的时候，返回近一个小时未被发送的定时任务短信
	 * @return
	 */
	@Override
	public List<SmsSendInfo> findOneHourMessage(){
		return this.smsSendInfoScheduleDao.findBySendMessageOneHour();
	}
	/**
	 * 每分钟短信检测
	 * @return
	 */
	@Override
	public List<SmsSendInfo> findBySendMessage(Map<String,Object> map){
	    return this.smsSendInfoScheduleDao.findBySendMessage(map);
	}
	/**
	 * 定时短信发送成功后，自动删除对应的记录
	 * @param id 定时任务主键ID
	 */
	@Override
	public void delSmsScheduleBySendSuccess(Long id){
		if(id!=null){
			this.smsSendInfoScheduleDao.doRemoveMessageBySendSuccess(id);
		}
	}
	
	/**
	  * 创建人：邱洋
	  * @Title: 删除schedule表中的定时发送任务
	  * @date 2017年5月3日--下午8:22:52 
	  * @return void
	  * @throws
	 */
	@Override
	public void delSmsScheduleByIds(List<Long> ids){
		HashMap<String, Object>   map  = new HashMap<String, Object>();
		map.put("delList", ids);
		this.smsSendInfoScheduleDao.doRemoveSms(map);
	}
	@Override
	public Date findByTidAndType(Long tid,String type){
		if(ValidateUtil.isEmpty(type) || ValidateUtil.isEmpty(tid)){
			return null;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tid", tid);
		map.put("type", type);
		return this.smsSendInfoScheduleDao.findByTidAndType(map);
	}
	@Override
	public void doUpdateSms(SmsSendInfo smsSendInfo){
		if(smsSendInfo==null){
			return;
		}
		if(smsSendInfo.getId()==null){
			return ;
		}
		if(smsSendInfo.getStartSend().getTime()<(System.currentTimeMillis()+60*1000)){
			smsSendInfo.setStartSend(new Date(System.currentTimeMillis()+60*1000));
		}
		smsSendInfo.setEndSend(null);
		this.smsSendInfoScheduleDao.doUpdateEndTimeBySendError(smsSendInfo);
		this.logger.debug("短信延后成功  "+smsSendInfo.getUserId() + " 类型：" + smsSendInfo.getType() + "id:"+smsSendInfo.getId());
	}
	
	/**
	 * deleteScheduleByMsgId(删除msgId下的定时记录)
	 */
	@Override
	public Boolean deleteScheduleByMsgId(Long msgId){
		if(msgId == null){
			return false;
		}
		try {
			smsSendInfoScheduleDao.removeInfoByMsgId(msgId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	//查询定时表中指定时间段的定时短信的数量
	@Override
	public Integer findBysendMessageCount(Map<String, Object> map) {
		if(map.get("startTime")==null||map.get("endTime")==null){
			logger.info("查询定时短信开始时间或者是结束时间为null");
			return 0;
		}
		return this.smsSendInfoScheduleDao.findBysendMessageCount(map);
	}
	@Override
	public void updateSmsSendScheduleStrartTime(SmsRecordVO vo) {
	    if(vo.getBeginTime()==null&&vo.getMsgId()==null){
	    	logger.info("修改定时发送时间出错,开始时间为"+vo.getbTime()+"总记录表id"+vo.getMsgId());
	    	throw new KycrmApiException("更新开始时间为，uid为空或者开始时间为空或者总记录表id为null");
	    }
	    Map<String,Object> map=new HashMap<String, Object>();
	    map.put("msgId", vo.getMsgId());
	    map.put("startSend", vo.getBeginTime());
		smsSendInfoScheduleDao.updateSmsSendScheduleStrartTime(map);
		
	}
	
}
