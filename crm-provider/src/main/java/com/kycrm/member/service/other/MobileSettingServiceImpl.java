package com.kycrm.member.service.other;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.other.IMobileSettingDao;
import com.kycrm.member.domain.entity.other.MobileSetting;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.tradecenter.ShortLinkVo;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.user.UserAccountServiceImpl;
import com.kycrm.member.util.ReturnMessage;
import com.kycrm.member.util.TaoBaoClientUtil;
import com.kycrm.member.util.sms.SendMessageIndexUtil;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.SendMessageStatusInfo;
import com.kycrm.util.thread.MyFixedThreadPool;


@Service("mobileSettingService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class MobileSettingServiceImpl implements IMobileSettingService{

private static final Log logger = LogFactory.getLog(MobileSettingServiceImpl.class);
    
    @Autowired
    private IUserAccountService userAccountService;
    
    @Autowired
    private IMobileSettingDao mobileSettingDao;
    
    @Autowired
	private IUserInfoService userInfoService;
    
    @Autowired
	private ISmsRecordDTOService smsRecordDTOService;
    
    
    /** 
	* @Description 根据用户id查询后台管理设置
	* @param  uid
	* @return MobileSetting    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月22日 下午4:21:20
	*/
    @Override
    public MobileSetting findMobileSetting(Long uid) {
    	return this.mobileSettingDao.findMobileSetting(uid);
    }
    
     
 	/** 
 	 * @Description 保存一条后台管理设置
 	 * @param  mobileSetting
 	 * @return Long    返回保存主键
 	 * @author jackstraw_yu
 	 * @date 2017年11月28日 上午9:52:10
 	 */
 	@Transactional
 	@Override
 	public Long saveMobileSetting(MobileSetting mobileSetting) {
 		//返回主键
 		mobileSettingDao.saveMobileSetting(mobileSetting);
 		if(mobileSetting.getId() ==null){
 			logger.info("保存后台管理设置,无法返回主键 :"+mobileSetting.getUserId());
 			throw new RuntimeException("MobileSetting:can not return primary key after saving a mobileSetting !");
 		}
 		return mobileSetting.getId();
 	}
    
 	/** 
	* @Description 初始化保存后台管理设置:<br/> 
	* 1:保存一条初始化后台设置<br/>
	* 2:补全用户手机号,标记赠送短信<br/>
	* 3:赠送用户500条短信
	* @param  mobileSetting   后台管理设置 
	* @return Long    返回保存主键
	* @author jackstraw_yu
	* @date 2017年11月27日 下午7:01:11
	*/
	@Transactional
	@Override
	public Long saveInitMobileSetting(MobileSetting mobileSetting,UserInfo user,String qqNum) {
		MobileSetting result = this.findMobileSetting(user.getId());
		if(result != null){
			logger.info("用户的后台管理设置已经存在 :"+mobileSetting.getUserId());
			throw new RuntimeException("用户的后台管理设置已经存在!! "+mobileSetting.getUserId());
		}
		//1:保存用户初始化话后台设置
		Long primaryKey = this.saveMobileSetting(mobileSetting);
		//2:保存用户手机号,赠送短信标记
		boolean hasModify = userInfoService.saveUserMobileInfo(mobileSetting.getMobile(), qqNum,user);
		if(hasModify){
			userAccountService.doUpdateUserSms(user.getId(),user.getTaobaoUserNick(), SendMessageStatusInfo.ADD_SMS, 
					Constants.PROVIDER_CUSTOMER_SMS_NUM, "首次保存后台管理设置", 
					Constants.USER_OPERATION_MORE, null, "保存手机号码送500条短信",UserAccountServiceImpl.NO_TIME);
		}
		return primaryKey;
	}
 	
 	
	/** 
	* @Description: 更新后台管理设置
	* @param  mobileSetting   后台管理设置 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2017年11月27日 下午7:03:35
	*/
	@Transactional
	@Override
	public void updateMobileSetting(MobileSetting mobileSetting,UserInfo user) {
		int execute = mobileSettingDao.updateMobileSetting(mobileSetting);
		userInfoService.saveUserMobileInfo(mobileSetting.getMobile(), null,user);
		if(execute != 1){
			logger.info("影响的行数不为1 :"+mobileSetting.getUserId());
			throw new RuntimeException("影响的行数不为1! :"+mobileSetting.getUserId());	
		}
	}

	/** 
     * @Description 发送验证码,成功返回true,其他返回false
     * @param  content
     * @param  numbers
     * @return boolean    返回类型 
     * @author jackstraw_yu
     * @date 2018年1月22日 下午2:05:24
     */
	@Override
     public boolean sendSecurityCodeMessage(String content, String mobile){
     	boolean send =false;
     	if(content==null || "".equals(content.trim()) || mobile==null)
     		return send;
     	String result = SendMessageIndexUtil.sendMessage(mobile, content, null, null);
     	if(result==null || "".equals(result))
     		return send;
 		ReturnMessage resultCode = JsonUtil.fromJson(result, ReturnMessage.class);
 		String status = resultCode.getReturnCode();
 		//发送成功则标记已发送
 		if(status!=null && "100".equals(status))
 			send =true;
     	return send;
     }

    /**
     * 在返回的前后都加入空格。链接才能打开
     */
	@Override
	public String getLink(String token,ShortLinkVo slo) {
		return " "+TaoBaoClientUtil.creatLink(token, slo.getType(), slo.getValue())+" ";
	} 
	
	/**
	 * 
	 * @time 2018年9月7日 下午1:57:07 
	 * @param uid
	 */
	@Override
	public void proxyResetSmsRemindMark(Long uid) {
		try {
			MobileSetting ms = mobileSettingDao.findMessageRemainderByUid(uid);
			if(null != ms){
				List<MobileSetting> list = new ArrayList<MobileSetting>();
				list.add(ms);
				this.processMessageRemainderData(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//********************************后台管理设置的功能实现********************************
	/** 
	 * ********************此方法有坑，请勿调用！！！*********************
	 * @Description: 检索后台管理设置,实现设置的功能<br/>
	 * 催付效果，短信发送量和当前短信条数（每天9点发送前一天数据）<br/> 
	 * 软件过期提醒<br/> 
	 * 短信余额不足提醒
	 * 最新促销活动通知(暂时没有该功能)<br/> 
	 * @return void    返回类型 
	 * @date 2017年11月28日 下午12:17:46
	 */
	@Override
	public void scanOpenMobileSetting(){
		/**
		 * 催付效果，短信发送提醒
		 */
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
			@Override
			public void run() {
				expeditingRemind();
			}
		});
		
		/**
		 * 软件过期提醒
		 */
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
			@Override
			public void run() {
				serviceExpireRemind();
			}
		});
		
		/**
		 * 短信余额不足提醒
		 */
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
			@Override
			public void run() {
				messageRemainderRemind();
			}
		});
	}
	
	/**
	 * 每天晚上12点;短信不足提醒已发送标记重置
	 * @time 2018年9月7日 下午12:08:25
	 */
	@Override
	public void resetMobileSettingFlag(){
		mobileSettingDao.resetMobileSettingFlag();
		logger.info("*****后台管理设置*****每天0点重置手机号设置余额不足提醒-发送标识*****执行完毕√√√√√*****"+new Date());
	}
	
	/**
	 * 短信余额不足提醒 
	 * 查询出设置crm_mobile_setting 
	 * 在进行crm_user_account比对短信条数、
	 * 小于设置条数 发送短信
	 * @time 2018年9月6日 下午2:44:42
	 */
	private void messageRemainderRemind() {
		Long count = mobileSettingDao.findOpenMessageRemainderCount();
		logger.info("*****后台管理设置**********短信余额不足提醒 **********查询出条数:"+count);
		if(null == count || count==0) return; 
		long end = 0,start = 0,page=500;
		if(count<page){
			end	= 1;
		}else if(count%page==0){
			end  = count/page;
		}else{
			end  = (count+page)/page;
		}
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("pageSize", page);
		while(start<end){
			map.put("startRow", start*page);
			List<MobileSetting> list = mobileSettingDao.findOpenMessageRemainderList(map);
			this.processMessageRemainderData(list);
			start++;
		}
	}


	/**
	 * 软件过期提醒
	 * 查询crm_user当前时间减去到期是等7或者3都发送短信
	 * @time 2018年9月6日 下午2:42:24
	 */
	private void serviceExpireRemind() {
		Long count = mobileSettingDao.findOpenServiceExpireCount();
		logger.info("*****后台管理设置**********软件过期提醒**********查询出条数:"+count);
		if(null == count || count==0) return; 
		long end = 0,start = 0,page=500;
		if(count<page){
			end	= 1;
		}else if(count%page==0){
			end  = count/page;
		}else{
			end  = (count+page)/page;
		}
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("pageSize", page);
		while(start<end){
			map.put("startRow", start*page);
			List<MobileSetting> list = mobileSettingDao.findOpenServiceExpireList(map);
			this.processServiceExpireData(list);
			start++;
		}
		
	}

	

	/**
	 * 催付效果，短信发送提醒
	 * 查询开启设置用户uid crm_mobile_setting 
	 * 循环用户uid 查询出前一天发送的短信总条数（大于100）crm_sms_record_dto 
	 * 查询出催付条数 （TYPE===add("2");add("3");add("4")）
	 * 当前短信剩余条数 crm_user_account 
	 * 发送短信
	 * @time 2018年9月6日 下午2:42:24 
	 */
	private void expeditingRemind() {
		Long count = mobileSettingDao.findOpenExpeditingCount();
		logger.info("*****后台管理设置**********催付效果，短信发送提醒**********查询出条数:"+count);
		if(null == count || count==0) return; 
		long end = 0,start = 0,page=500;
		if(count<page){
			end	= 1;
		}else if(count%page==0){
			end  = count/page;
		}else{
			end  = (count+page)/page;
		}
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("pageSize", page);
		while(start<end){
			map.put("startRow", start*page);
			List<MobileSetting> list = mobileSettingDao.findOpenExpeditingList(map);
			this.processExpeditingData(list);
			start++;
		}
	}

	
	/**
	 * 短信余额不足提醒
	 * @time 2018年9月6日 下午5:24:09 
	 * @param ms
	 */
	private void processMessageRemainderData(List<MobileSetting> list) {
		for (MobileSetting ms : list) {
			try {
				//拼凑短信
				String content = Constants.MESSAGE_SMSCOUNT_CONTNET
						.replace("USERNAME", ms.getUserId())
						.replace("COUNT",ms.getMessageCount()+"");
				//发送短信
				sendSecurityCodeMessage(content, ms.getMobile());
				
				//标记已发送
				mobileSettingDao.updateMobileSettingFlag(ms.getId());
				logger.info("*****后台管理设置*****发送短信*****短信余额不足提醒 *****用户id："+ms.getUid());
			} catch (Exception e) {
				logger.info("*****后台管理设置*****发送短信*****异常"+e.getMessage());
				e.printStackTrace();
				continue;
			}
		}
	}


	/**
	 * 软件过期提醒
	 * @time 2018年9月6日 下午5:17:18 
	 * @param ms
	 */
	private void processServiceExpireData(List<MobileSetting> list) {
		for (MobileSetting ms : list) {
			try {
				//拼凑短信
				String content = Constants.MESSAGE_SERVICEEXPIRE_CONTNET.replace(
						"USERNAME", ms.getUserId()).replace("EXPIRATIONTIME",
								DateUtils.formatDate(ms.getExpirationTime(), "yyyy-MM-dd"));
				
				//发送短信
				sendSecurityCodeMessage(content, ms.getMobile());
				logger.info("*****后台管理设置*****发送短信*****软件过期提醒 *****用户id："+ms.getUid());
			} catch (Exception e) {
				logger.info("*****后台管理设置*****发送短信*****异常"+e.getMessage());
				e.printStackTrace();
				continue;
			}
		}
	}


	/**
	 * 处理催付效果，短信发送提醒
	 * @time 2018年9月6日 下午4:28:06 
	 * @param ms
	 */
	private void processExpeditingData(List<MobileSetting> list) {
		
		for (MobileSetting ms : list) {
			try {
				//查询出前一天发送的短信总条数
				Long sendCount = smsRecordDTOService.findSmsRecordDTOFrontDayCount(ms.getUid());
				if(sendCount !=null && sendCount>=Constants.COUNT_SEND){
					//查询催付总条数
					Long rCount = smsRecordDTOService.findSmsRecordDTOReminderCount(ms.getUid());
					
					//拼凑短信
					String content = Constants.MESSAGE_EXPEDITING_CONTNET
							.replace("USERNAME", ms.getUserId())
							.replace("CURRENTCOUNT", ms.getSmsNum() + "")
							.replace("SENDCOUNT", sendCount + "")
							.replace("EXPEDITING", rCount + "");
					
					//发送短信
					sendSecurityCodeMessage(content, ms.getMobile());
					logger.info("*****后台管理设置*****发送短信*****催付效果，短信发送提醒*****用户id："+ms.getUid());
				}
			} catch (Exception e) {
				logger.info("*****后台管理设置*****发送短信*****异常"+e.getMessage());
				e.printStackTrace();
				continue;
			}
		}
	}
}
