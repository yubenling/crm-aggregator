package com.kycrm.member.service.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import com.kycrm.member.core.redis.DistributedLock;
import com.kycrm.member.core.redis.RedisLockService;
import com.kycrm.member.dao.message.ISmsSendInfoScheduleDao;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.message.BatchSmsData;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.message.SmsSendInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.message.SendMsgVo;
import com.kycrm.member.service.marketing.IMarketingMemberFilterService;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.vip.IVipUserService;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.DateUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.MobileRegEx;
import com.kycrm.util.MsgType;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.thread.MyFixedThreadPool;
import com.taobao.api.SecretException;


@Service("msgSendService")
public class MsgSendServiceImpl implements IMsgSendService{
	
	private static final Log logger = LogFactory.getLog(MsgSendServiceImpl.class);	
	@Autowired
	private IMultithreadBatchSmsService multithreadService; 
	@Autowired
	private IMsgSendRecordService msgSendRecordService;
	@Autowired
	private ICacheService cacheService;
	@Autowired 
	private DistributedLock distributedLock;
	@Autowired
	private IMemberDTOService memberDTOService; 
	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private RedisLockService redisLockService;
	@Autowired
	private IVipUserService vipUserService;
	@Autowired
	private ISmsSendInfoScheduleService smsSendInfoScheduleService;
	@Autowired
	private ISmsSendInfoScheduleDao smsSendInfoScheduleDao;
	@Autowired
	private IMarketingMemberFilterService marketingMemberFilterService;
//	@Autowired
//	private IMultithreadBatchIndividuationSmsService multithreadBatchIndividuationSmsService;
	

	@Override
	public void sendBatchMsg(SendMsgVo sendMsgVo) throws Exception {
		if(sendMsgVo.getTotalCount() ==null || sendMsgVo.getTotalCount() ==0 || sendMsgVo.getSchedule()==null)
			throw new Exception("会员短信群发数据筛选异常!");
//		try {
//			distributedLock.tryLock(RedisConstant.RediskeyCacheGroup.MEMBER_BATCH_SEND_DATA_KEY+"-"+sendMsgVo.getQueryKey()+"-"+sendMsgVo.getUserId());
			/*MemberCriteriaVo memberCriteriaVo = cacheService.get(RedisConstant.RedisCacheGroup.MEMBER_BATCH_SEND_DATA_CACHE, 
					RedisConstant.RediskeyCacheGroup.MEMBER_BATCH_SEND_DATA_KEY+"-"+sendMsgVo.getQueryKey()+"-"+sendMsgVo.getUserId(), MemberCriteriaVo.class);*/
		   MemberFilterVO memberFilterVO =redisLockService.getValue(
					RedisConstant.RediskeyCacheGroup.MEMBER_BATCH_SEND_DATA_KEY+"-"+sendMsgVo.getQueryKey()+"-"+sendMsgVo.getUid(),
					MemberFilterVO.class);
			if(null!=memberFilterVO){
				//判断是立即发送还是定时发送
				Date startTime = null,endTime = null;
				if(sendMsgVo.getSchedule()){
					startTime = DateUtils.parseDate(sendMsgVo.getSendTime(), "yyyy-MM-dd HH:mm");
					endTime = DateUtils.addDate(startTime, 1);  //结束时间比开始时间加一天
				}else{ 
					startTime =new Date();  
				}
				//验证,保存子记录与更新总记录
				MsgSendRecord msg = saveMsgRecord(sendMsgVo,startTime,MsgType.MSG_STATUS_SENDING);
				sendMsgVo.setMsgId(msg.getId());
				if(!sendMsgVo.getSchedule()){//立即发送的话
					processMsgSendData(sendMsgVo,memberFilterVO,msg,startTime,endTime);
				}else{//定时发送的
					processScheduleData(sendMsgVo,memberFilterVO,msg,startTime,endTime);
				}
			}
//		}finally{  
//			distributedLock.unLock(RedisConstant.RediskeyCacheGroup.MEMBER_BATCH_SEND_DATA_KEY+"-"+sendMsgVo.getQueryKey()+"-"+sendMsgVo.getUserId());
//		}
		
	}
	@Override
	public Map<String, Integer> batchSendMsg(List<MemberInfoDTO> sendNums,SendMsgVo sendMsgVo) {
		long s = System.currentTimeMillis();
		BatchSmsData  batchSmsData =null;
		if(sendMsgVo.getContent().contains("{买家昵称}")||sendMsgVo.getContent().contains("{买家姓名}")){
		    batchSmsData = new BatchSmsData(null);
			for(MemberInfoDTO memberInfoDTO:sendNums){
					List<String> phoneAndContent=new ArrayList<String>();
					for(MemberInfoDTO memberInfo_DTO:sendNums){
						String smsContent=getNewSmsContent(sendMsgVo.getContent(),memberInfo_DTO);
						phoneAndContent.add(memberInfo_DTO.getMobile()+":"+smsContent);
					} 
					batchSmsData.setContentArr(phoneAndContent.toArray(new String[phoneAndContent .size()]));	
					batchSmsData.setType(sendMsgVo.getMsgType());
					batchSmsData.setIpAdd(sendMsgVo.getIpAddress());
					batchSmsData.setChannel(sendMsgVo.getAutograph());
					batchSmsData.setAutograph(sendMsgVo.getAutograph());
					batchSmsData.setUserId(sendMsgVo.getUserId());
					batchSmsData.setContent(sendMsgVo.getContent());
					batchSmsData.setMsgId(sendMsgVo.getMsgId());
					batchSmsData.setTotal(sendMsgVo.getTotalCount().intValue());
					/*查询当前用户是否为vip*/
					boolean isVip = vipUserService.findVipUserIfExist(sendMsgVo.getUid());
					batchSmsData.setVip(isVip);
//					multithreadBatchIndividuationSmsService.batchIndividuationSms(batchSmsData);
			}	
		}else{
			List<String> phones=new ArrayList<String>();
			for(MemberInfoDTO  memberInfoDTO:sendNums){
				phones.add(memberInfoDTO.getMobile());
			}
			batchSmsData = new BatchSmsData(phones.toArray(new String[phones.size()]));//构造方法将电话号设置进去了
			batchSmsData.setType(sendMsgVo.getMsgType());
			batchSmsData.setIpAdd(sendMsgVo.getIpAddress());
			batchSmsData.setChannel(sendMsgVo.getAutograph());
			batchSmsData.setAutograph(sendMsgVo.getAutograph());
			batchSmsData.setUserId(sendMsgVo.getUserId());
			batchSmsData.setContent(sendMsgVo.getContent());
			batchSmsData.setMsgId(sendMsgVo.getMsgId());
			batchSmsData.setTotal(sendMsgVo.getTotalCount().intValue());
			/*查询当前用户是否为vip*/
			boolean isVip = vipUserService.findVipUserIfExist(sendMsgVo.getUid());
			batchSmsData.setVip(isVip);
		    multithreadService.batchOperateSms(batchSmsData);
		}
		int sNum = batchSmsData.getSuccess();  //得到成功条数
		int fNum = batchSmsData.getFail();    //得到失败条数
		Map<String,Integer> resultMap =null; 
		if(sNum!=0 || fNum!=0){
			resultMap = new HashMap<String,Integer>();
			resultMap.put("succeedNum", sNum);
			resultMap.put("failedNum", fNum);
		}
		logger.info("会员短信群发-立即发送:批量发送时间开销:"+(System.currentTimeMillis()-s)+"millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		return resultMap;
	}
	/**
	 * 拼凑短信内容（不对短链接进行拼凑） 
	 * @author: wy
	 * @time: 2017年9月4日 下午12:13:21
	 * @param oldSms 未拼凑的短信内容
	 * @param trade 订单
	 * @param userInfo 用户的签名
	 * @return 拼凑好的短信内容
	 */
	private String getNewSmsContent(String oldSms,MemberInfoDTO memberInfo_DTO){
		oldSms =oldSms.replaceAll("\\{买家昵称\\}",memberInfo_DTO.getBuyerNick());
		oldSms =oldSms.replaceAll("\\{买家姓名\\}", memberInfo_DTO.getReceiverName());
		return oldSms;
	}

	/**
	 * 立即发送:处理短信群发数据
	 * 一,筛选错误手机号,重复手机号....
	 * 四,更新总表
	 */
	@SuppressWarnings("unchecked")
	private void processMsgSendData(final SendMsgVo sendMsgVo,final MemberFilterVO memberFilterVO,
									final MsgSendRecord msg,
									final Date startTime, final Date endTime){
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
			@Override
			public void run() {
				//辅助验证的集合
				Set<MemberInfoDTO> vSet = new HashSet<MemberInfoDTO>();
			    List<MemberInfoDTO> wrongNums = new ArrayList<MemberInfoDTO>(),repeatNums = new ArrayList<MemberInfoDTO>();
			    Map<String,Object> sortMap = null;
			    int successSmsSum = 0;
			    int resumeSmsSum=0;
			    int successNo =0,
			    	   errorNo=0;
			    boolean isLast = false;
				long end = 0l, start = 0l, exist=0l,unExist=0l, 
					 totalCount = sendMsgVo.getTotalCount(); 
				if(totalCount/ConstantUtils.PROCESS_PAGE_SIZE_MAX==0){
					end  = 1l;
				}else if(totalCount%ConstantUtils.PROCESS_PAGE_SIZE_MAX==0){
					 end  = totalCount/ConstantUtils.PROCESS_PAGE_SIZE_MAX;
				}else{
					 end  = (totalCount+ConstantUtils.PROCESS_PAGE_SIZE_MAX)/ConstantUtils.PROCESS_PAGE_SIZE_MAX;
				}
//				memberFilterVO.setPageSize(ConstantUtils.PROCESS_PAGE_SIZE_MAX);
				while(start<end){
					if(start == (end-1)){  	
//						memberFilterVO.setPageSize(totalCount-start*ConstantUtils.PROCESS_PAGE_SIZE_MAX);
					}
					logger.info("会员短信群发-立即发送:第"+(start+1)+"次循环^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
//					memberFilterVO.setStartRows(start*ConstantUtils.PROCESS_PAGE_SIZE_MAX);
					long s1 = System.currentTimeMillis();
					/***
					 * 查询出数据
					 **/
					logger.info("会员短信群发-立即发送:筛选条件打印"+"\r\n"+memberFilterVO.toString()+"\r\n");
					List<MemberInfoDTO> findMembersByCondition=null;
					//这个暂时留在这里
					try {
						findMembersByCondition =null;// marketingMemberFilterService.findMembersByCondition(sendMsgVo.getUid(), memberInfoSearchVO, pageSize, pageNo)
					} catch (Exception e) {
						
					}
					start++;
					if(findMembersByCondition==null || findMembersByCondition.isEmpty()){
						unExist++;
						continue;
					}
					exist++;
					logger.info("会员短信群发-立即发送:查询出"+(findMembersByCondition.size())+"条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
					logger.info("会员短信群发-立即发送:此次查询时间开销"+(System.currentTimeMillis()-s1)+"millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
					List<MemberInfoDTO> decMemberInfoDTOs = decryptMemeberMobiles(findMembersByCondition,sendMsgVo.getUid());
					if(decMemberInfoDTOs==null || decMemberInfoDTOs.isEmpty()){
						logger.info("会员短信群发-立即发送:此次查询结果为空!"+"millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
						continue;
					}
					logger.info("会员短信群发-立即发送:解密后的数据大小为"+(decMemberInfoDTOs.size())+"条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
					//筛选出重复手机号,错误手机号.....
					sortMap = sortDataBatch(decMemberInfoDTOs,vSet);
					if(sortMap==null || sortMap.isEmpty()){
						continue;
											}
					if(sortMap.get("wrongNums") !=null){
						wrongNums.addAll((ArrayList<MemberInfoDTO>)sortMap.get("wrongNums"));
					}
					if(sortMap.get("repeatNums") !=null){
						repeatNums.addAll((ArrayList<MemberInfoDTO>)sortMap.get("repeatNums"));
					}
					if(sortMap.get("sendNums") !=null){
						List<MemberInfoDTO> sendNums = (ArrayList<MemberInfoDTO>)sortMap.get("sendNums");
						if(sendNums.size()>0){
							//successNo+=sendNums.size();
							if((exist+unExist)==end) isLast = true;//TODO 最后一次发送
							Map<String, Integer> resultMap =batchSendMsg(sendNums,sendMsgVo);
							if(resultMap!=null&&!resultMap.isEmpty()){
								successNo += (resultMap.get("succeedNum")==null?0:resultMap.get("succeedNum"));
								errorNo += (resultMap.get("failedNum")==null?0:resultMap.get("failedNum"));
							    successSmsSum+=(resultMap.get("successSmsSum")==null?0:resultMap.get("successSmsSum"));
							}
						}
					}
					vSet.addAll(findMembersByCondition);
				}
				if(wrongNums.size()>0){
					msg.setWrongCount(wrongNums.size());
					logger.info("会员短信群发-立即发送:筛选出手机号错误,共"+(wrongNums.size())+"条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
					//smsRecordDTOService.saveErrorMsgNums(encryptMemeberMobiles(wrongNums,sendMsgVo.getUid()),sendMsgVo,MsgType.SMS_STATUS_WRONGNUM,startTime);	
				}
				if(repeatNums.size()>0){
					msg.setRepeatCount(repeatNums.size());
					logger.info("会员短信群发-立即发送:筛选出手机号重复,共"+(repeatNums.size())+"条数据 ^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
					//msgSendRecordService.saveErrorMsgNums(encryptMemeberMobiles(repeatNums,sendMsgVo.getUserId()),sendMsgVo,MsgType.SMS_STATUS_REPEATNUM,startTime);
					//smsRecordDTOService.saveErrorMsgNums(repeatNums,sendMsgVo,MsgType.SMS_STATUS_REPEATNUM,startTime);
				}
				msg.setSucceedCount(successNo);
				msg.setFailedCount(errorNo);
				msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
				msgSendRecordService.updateMsgRecordByMsgId(msg);
			}

			
		});
	}
	
	/**
	 * 保存定时发送的数据
	 * 去除重复手机号,错误手机号...
	 * */
	@SuppressWarnings("unchecked")
	private void processScheduleData(final SendMsgVo sendMsgVo, final MemberFilterVO memberFilterVO, 
									 final MsgSendRecord msg,
									 final Date startTime,final Date endTime) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
			@SuppressWarnings("unused")
			@Override
			public void run() {
				Set<MemberInfoDTO> vSet = new HashSet<MemberInfoDTO>();
				List<MemberInfoDTO> sendNums = new ArrayList<MemberInfoDTO>(),
						wrongNums = new ArrayList<MemberInfoDTO>(),
						repeatNums = new ArrayList<MemberInfoDTO>();
				Map<String,Object> sortMap = null;
				Integer wrongNo = null,
						repeatNo = null /*,successNo =0*/;
				long end = 0l,start = 0l;
				long totalCount = sendMsgVo.getTotalCount();
				if(totalCount/ConstantUtils.PROCESS_PAGE_SIZE_MAX==0){
					end  = 1l;
				}else if(totalCount%ConstantUtils.PROCESS_PAGE_SIZE_MAX==0){
					 end  = totalCount/ConstantUtils.PROCESS_PAGE_SIZE_MAX;
				}else{
					 end  = (totalCount+ConstantUtils.PROCESS_PAGE_SIZE_MAX)/ConstantUtils.PROCESS_PAGE_SIZE_MAX;
				}
//				memberFilterVO.setPageSize(ConstantUtils.PROCESS_PAGE_SIZE_MAX);
				while(start<end){
//					if(start == (end-1)){
//						memberFilterVO.setPageSize(totalCount-start*ConstantUtils.PROCESS_PAGE_SIZE_MAX);
//					}
//					memberFilterVO.setStartRows(start*ConstantUtils.PROCESS_PAGE_SIZE_MAX);
					logger.info("会员短信群发-定时发送:第"+(start+1)+"次循环^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
					long s1 = System.currentTimeMillis();
					/**
					 * 查询出数据
					 * **/
					List<MemberInfoDTO> memberInfoDTOs  =null;// marketingMemberFilterService.findMembersByCondition(memberFilterVO);
					start++;
					if(memberInfoDTOs==null || memberInfoDTOs.isEmpty()){
						continue;
					}
					logger.info("会员短信群发-定时发送:查询出"+(memberInfoDTOs.size())+"条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
					logger.info("会员短信群发-定时发送:此次查询时间开销"+(System.currentTimeMillis()-s1)+"millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
					List<MemberInfoDTO> decNums = decryptMemeberMobiles(memberInfoDTOs,sendMsgVo.getUid());
					if(decNums==null || decNums.isEmpty()){
						continue;
					}
					logger.info("会员短信群发-立即发送:解密后的数据大小为"+(decNums.size())+"条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
					//过滤数据
					sortMap = sortDataBatch(decNums,vSet);
					if(sortMap==null || sortMap.isEmpty()){
						continue;
					}
					if(sortMap.get("wrongNums") !=null){
						wrongNums.addAll((ArrayList<MemberInfoDTO>)sortMap.get("wrongNums"));
					}
					if(sortMap.get("repeatNums") !=null){
						repeatNums.addAll((ArrayList<MemberInfoDTO>)sortMap.get("repeatNums"));
					}
					if(sortMap.get("sendNums") !=null){
						sendNums.addAll((ArrayList<MemberInfoDTO>)sortMap.get("sendNums"));
					}
					vSet.addAll(memberInfoDTOs);
				}
				if(sendNums.size()>0){
					//successNo+=sendNums.size();
					//如果一次群发的短信数量较大，所以要拆分下。
					splitScheduleData(encryptMemeberMobiles(sendNums,sendMsgVo.getUid()),sendMsgVo,startTime,endTime);
				}
				if(wrongNums.size()>0){
					wrongNo = wrongNums.size();
					logger.info("会员短信群发-定时发送:筛选出手机号错误,共"+(wrongNums.size())+"条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
					//smsRecordDTOService.saveErrorMsgNums(encryptMemeberMobiles(wrongNums,sendMsgVo.getUid()),sendMsgVo,MsgType.SMS_STATUS_WRONGNUM,startTime);
					//smsSendRecordService.saveErrorMsgNums(wrongNums,sendMsgVo,MsgType.SMS_STATUS_WRONGNUM,startTime);
				}
				if(repeatNums.size()>0){
					repeatNo = repeatNums.size();
					logger.info("会员短信群发-定时发送:筛选出手机号重复,共"+(wrongNums.size())+"条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
				//	smsRecordDTOService.saveErrorMsgNums(encryptMemeberMobiles(repeatNums,sendMsgVo.getUid()),sendMsgVo,MsgType.SMS_STATUS_REPEATNUM,startTime);
					//smsSendRecordService.saveErrorMsgNums(wrongNums,sendMsgVo,MsgType.SMS_STATUS_WRONGNUM,startTime);
				}
				//更新总记录
				//msg.setSucceedCount(successNo);
				//定时这里设置一个0值
				msg.setSucceedCount(0);
				msg.setFailedCount(0);
				msg.setRepeatCount(repeatNo);
				msg.setWrongCount(wrongNo);
				msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
				msgSendRecordService.updateMsgRecordByMsgId(msg);
			}
		});
	}
	
	
	/**
	 * 以20万长度为节点 拆分要保存的定时数据
	 * 此处取消线程异步处理---确保取消定时时候数据安全
	 * **/
	private void splitScheduleData(List<MemberInfoDTO> memberInfoDTOs, SendMsgVo sendMsgVo, Date startTime, Date endTime) {
		if(memberInfoDTOs.isEmpty()) return;
		List<MemberInfoDTO> sendNums = new ArrayList<MemberInfoDTO>(memberInfoDTOs);
		logger.info("会员短信群发-定时发送:"+sendMsgVo.getUserId()+"保存"+(sendNums.size())+"条定时数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		//Integer end = 0,start = 0,node=200000,dataSize = sendNums.size();
		//加密后单条手机号长度变长:原理13位加密后54位
		Integer end = 0, //拆分几次
				start = 0, //开始
				node=100000,//每次拆分的长度
				dataSize = sendNums.size();//数据长度
		List<MemberInfoDTO> subNums =null;
		if(dataSize/node==0){
			end	= 1;
		}else if(dataSize%node==0){  
			end  = dataSize/node;
		}else{
			end  = (dataSize+node)/node;
		}
		while(start<end){
			if(start == (end-1)){
				subNums = sendNums.subList(start*node, dataSize); //为了最后一个不满一个节点的长度
			}else{
				subNums = sendNums.subList(start*node, (start+1)*node);
			}
			if(subNums==null) continue;
			start++;
			logger.info("会员短信群发-定时发送:"+sendMsgVo.getUserId()+"保存第"+start+"次组装的数据,数据量为:"+subNums.size()+"条^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
			saveMsgScheduleBatch(subNums,sendMsgVo,startTime,endTime);
		}

	}
	/**
	 * 保存群发短信到定时数据表,多条or一条
	 */
	private void saveMsgScheduleBatch(List<MemberInfoDTO> memberInfoDTOs,final SendMsgVo sendMsgVo,Date startTime,Date endTime) {
		long s = System.currentTimeMillis();
		List<String> str=new ArrayList<String>(); 
		for(MemberInfoDTO memberInfoDTO:memberInfoDTOs){
			str.add(memberInfoDTO.getMobile()+":"+memberInfoDTO.getBuyerNick()+":"+memberInfoDTO.getReceiverName());
		}
		String mobiles = StringUtils.join(str.toArray(),",");
		SmsSendInfo smsSendInfo = new SmsSendInfo();
		smsSendInfo.setUserId(sendMsgVo.getUserId());
		smsSendInfo.setMsgId(sendMsgVo.getMsgId());
		smsSendInfo.setType(sendMsgVo.getMsgType());
		smsSendInfo.setContent(sendMsgVo.getContent());
		smsSendInfo.setPhone(mobiles);
		smsSendInfo.setStartSend(startTime);
		smsSendInfo.setEndSend(endTime);
		smsSendInfo.setChannel(sendMsgVo.getAutograph());
		smsSendInfo.setCreatedDate(new Date());
		smsSendInfo.setLastModifiedDate(new Date());
		try {
			/*myBatisDao.execute(
					SmsSendInfo.class.getName() + "Schedule",
					"doCreateByScheduleSend", smsSendInfo);*/
			smsSendInfoScheduleDao.doCreateByScheduleSend(smsSendInfo); //这个就直接去创建了	
		} catch (Exception e) {
			logger.error("会员短信群发-定时发送:保存单笔定时数据失败!!^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		}
		logger.info("会员短信群发-定时发送:保存单笔定时数据时间开销:"+(System.currentTimeMillis()-s)+"millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
	}
	/** 
	*  核心方法
	*  (会员短信群发:大数据量时筛选手机号错误,手机号重复) 
	* @author jackstraw_yu    {加密成功的集合，}
	*/
	private Map<String, Object>  sortDataBatch(List<MemberInfoDTO> nums,Set<MemberInfoDTO> vSet) {
		logger.info("会员短信群发:核心筛选区接收到"+(nums.size())+"条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		List<MemberInfoDTO> mList =  new ArrayList<MemberInfoDTO>(nums);
		List<MemberInfoDTO> wrongNums = new ArrayList<MemberInfoDTO>(), //错误手机号
				repeatNums = new ArrayList<MemberInfoDTO>(); //重复手机号
		Map<String,Object> hashMap = new HashMap<String,Object>();
		MemberInfoDTO str = null; //迭代解密后的手机号
		Set<MemberInfoDTO> numSet =null,
				    vNumSet =null;
		Iterator<MemberInfoDTO> iterator = mList.iterator();
		long h1 = System.currentTimeMillis();
		//校验会员手机号--订单抽取出的会员信息手机号有的会有问题,需要进行正则校验!
		while(iterator.hasNext()){  //{迭代器中可以边遍历边删除}
			str = iterator.next();
			if(str==null || !MobileRegEx.validateMobile(str.getMobile())){  //进行手机号的验证
				wrongNums.add(str);
				iterator.remove();
			}
		}
		long h2 = System.currentTimeMillis();
		logger.info("会员短信群发:送筛选出错误手机号,时间开销"+(h2-h1)+"millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		if(!mList.isEmpty()){
			repeatNums.addAll(mList);
			numSet = new HashSet<MemberInfoDTO>(mList);
			vNumSet = new HashSet<MemberInfoDTO>(mList);
			for (MemberInfoDTO n : numSet) {
				repeatNums.remove(n);  //得到重复的手机号
			}
		}
		//求得重复手机号
		if(!repeatNums.isEmpty()){
			hashMap.put("repeatNums", repeatNums);
		}
		if(vNumSet!=null && !vNumSet.isEmpty()){
			vNumSet.retainAll(vSet);
			repeatNums.addAll(vNumSet);
			hashMap.put("repeatNums", repeatNums);
			numSet.removeAll(vNumSet);//发送的手机号
		}
		if(numSet!=null &&!numSet.isEmpty()){
			hashMap.put("sendNums", new ArrayList<MemberInfoDTO>(numSet));
		}
		long h3 = System.currentTimeMillis();
		logger.info("会员短信群发:筛选出重复手机号,时间开销"+(h3-h2)+"millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		if(!wrongNums.isEmpty()){
			hashMap.put("wrongNums", wrongNums);
		}
		return hashMap;
	}
	
	
	/**
	 * 会员短信群发:保存总记录
	 */
	private MsgSendRecord saveMsgRecord(SendMsgVo sendMsgVo,Date sendTime,String status){
		MsgSendRecord msg = new MsgSendRecord();
		msg.setActivityName(sendMsgVo.getActivityName());
		msg.setTotalCount(Integer.parseInt(sendMsgVo.getTotalCount().toString()));
		msg.setTemplateContent(sendMsgVo.getContent());
		msg.setUserId(sendMsgVo.getUserId());
		msg.setType(sendMsgVo.getMsgType());
		msg.setSendCreat(sendTime);
		msg.setIsShow(true);
		//是定时发送--false 立即发送true
		msg.setIsSent(!sendMsgVo.getSchedule());
		msg.setStatus(status); //4:发送中/保存中
		//保存总记录返回总记录Id
		//msgSendRecordService.saveMsg(msg);
		msgSendRecordService.saveMsgSendRecord(msg);
		return msg;
	}
	

	/**
	 * @Description:会员短信群发,解密手机号
	 * @author jackstraw_yu
	 */
	private List<MemberInfoDTO> decryptMemeberMobiles(List<MemberInfoDTO> findMembersByCondition,Long uid) {
		EncrptAndDecryptClient client = null; 
		List<MemberInfoDTO> list = null;
		String key = null;
		if(findMembersByCondition!=null && findMembersByCondition.size()>0){
			client =  EncrptAndDecryptClient.getInstance();
			list = new ArrayList<MemberInfoDTO>();
			key = getSessionkey(uid,true);
			for (MemberInfoDTO memberInfoDTO : findMembersByCondition) {
				try {
					if(!EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getMobile(), EncrptAndDecryptClient.PHONE)){
						list.add(memberInfoDTO);
					}else{
						try {
							memberInfoDTO.setMobile(client.decrypt(memberInfoDTO.getMobile(), EncrptAndDecryptClient.PHONE,key));
							list.add(memberInfoDTO);
						} catch (Exception e) {
							logger.error("会员短信群发单条手机号解密失败!");
							key = getSessionkey(uid,false);
							try {
								memberInfoDTO.setMobile(client.decrypt(memberInfoDTO.getMobile(), EncrptAndDecryptClient.PHONE,key));
								list.add(memberInfoDTO);
							} catch (Exception e1) {
								logger.error("会员短信群发单条手机号<>再次<>解密失败!");
								//break;
							}
							continue;
						}
					}
				} catch (SecretException e) {
					logger.error("判断会员短信群发单条手机号是否加密失败!...会员对象为:"+memberInfoDTO);
					continue;
				}
			}	
			return  list.isEmpty()?null:list;
		}
		
		return list;
	}
	
	
	/**
	 * @Description:会员短信群发,加密手机号
	 * @author jackstraw_yu
	 */
	private List<MemberInfoDTO> encryptMemeberMobiles(List<MemberInfoDTO> memberInfoDTOs,Long uid) {
		EncrptAndDecryptClient client = null; 
		List<MemberInfoDTO> list = null;
		String key = null,encrypt = null;
		if(memberInfoDTOs!=null && memberInfoDTOs.size()>0){
			client =  EncrptAndDecryptClient.getInstance();
			list = new ArrayList<MemberInfoDTO>();
			key = getSessionkey(uid,true);
			for (MemberInfoDTO memberInfoDTO : memberInfoDTOs) {
				try {
					memberInfoDTO.setMobile(client.encrypt(memberInfoDTO.getMobile(), EncrptAndDecryptClient.PHONE,key));
					list.add(memberInfoDTO);
				} catch (Exception e) {
					logger.error("会员短信群发单条手机号加密失败!");
					key = getSessionkey(uid,false);
					try {
						memberInfoDTO.setMobile(client.encrypt(memberInfoDTO.getMobile(), EncrptAndDecryptClient.PHONE,key));
						list.add(memberInfoDTO);
					} catch (Exception e1) {
						logger.error("会员短信群发单条手机号<>再次<>加密失败!");
						//break;
					}
					continue;
				}
			}	
			return list.isEmpty()?null:list;
		}	
		return list;
	}
	
	
	
	/**
	 * @Description:会员短信群发,加密手机号--批量
	 * @author jackstraw_yu
	 */
	@Deprecated
	private List<String> encryptMobilesBatch(List<String> mobiles,Long uid) {
		EncrptAndDecryptClient client = null; 
		List<String> list = null;
		String key = null;
		Map<String, String> encrypt = null;
		if(mobiles!=null && mobiles.size()>0){
			client =  EncrptAndDecryptClient.getInstance();
			list = new ArrayList<String>();
			key = getSessionkey(uid,true);
			try {
				encrypt = client.encrypt(mobiles, EncrptAndDecryptClient.PHONE,key);
				list = new ArrayList<String>(encrypt.values());
			} catch (SecretException e) {
				logger.error("会员短信群发批量手机号加密失败!");
				key = getSessionkey(uid,false);
				try {
					encrypt = client.encrypt(mobiles, EncrptAndDecryptClient.PHONE,key);
					list = new ArrayList<String>(encrypt.values());
				} catch (SecretException e1) {
					logger.error("会员短信群发批量手机号<>再次<>加密失败!");
				}
			}
		}
		return list;
	}
	
	
	/**
	 * @Description:会员短信群发,解密手机号--批量
	 * @author jackstraw_yu
	 */
	@Deprecated
	private List<String> decryptMobilesBatch(List<String> mobiles,Long uid) {
		EncrptAndDecryptClient client = null; 
		List<String> list = null;
		String key = null;
		Map<String, String> decrypt = null;
		if(mobiles!=null && mobiles.size()>0){
			client =  EncrptAndDecryptClient.getInstance();
			key = getSessionkey(uid,true);
			try {
				if(EncrptAndDecryptClient.isPartEncryptData(mobiles, EncrptAndDecryptClient.PHONE)){
					decrypt = client.decrypt(mobiles, EncrptAndDecryptClient.PHONE, key);
					list = new ArrayList<String>(decrypt.values());
				}else{
					return mobiles;
				}
			} catch (SecretException e) {
				logger.error("会员短信群发批量手机号解密失败!");
				key = getSessionkey(uid,false);
				try {
					if(EncrptAndDecryptClient.isPartEncryptData(mobiles, EncrptAndDecryptClient.PHONE)){
						decrypt = client.decrypt(mobiles, EncrptAndDecryptClient.PHONE, key);
						list = new ArrayList<String>(decrypt.values());
					}else{
						return mobiles;
					}
				} catch (SecretException e1) {
					logger.error("会员短信群发批量手机号<>再次<>解密失败!");
				}
			}
		}
		return list;
	}
	/**
	 * @Description:获取加密解密使用 的token
	 * @Copy_author jackstraw_yu
	 */
	 private  String getSessionkey(Long uid,boolean flag){
		    String  token = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE, RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY+uid);
			if(null!=token&&!"".equals(token)&&flag){
				 return token;
			}else{
				UserInfo user = userInfoService.findUserInfo(uid);
				if(user!=null)
					if(null!=user.getAccessToken()&&!"".equals(user.getAccessToken())){
						 cacheService.putNoTime(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE, RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY+uid,user.getAccessToken(),false);
						 return user.getAccessToken(); 
					}
			}
			return "";
	   }
	 /*class ForkJoinUtil  extends RecursiveTask<List<Map<String,Object>>>{ 
	    private static final int THREAD_HOLD = 01;
        private MemberCriteriaVo memberCriteriaVo;
	    long end = 0l, start = 0l, exist=0l,unExist=0l, 
			 totalCount;
		private SendMsgVo  sendMsgVo;
		public ForkJoinUtil(Long start,Long end,SendMsgVo sendMsgVo,MemberCriteriaVo memberCriteriaVo){
		     this.start = start;
		     this.end = end;
		     this.sendMsgVo=sendMsgVo;
		     this.memberCriteriaVo=memberCriteriaVo;
	    }
		@Override
		protected List<Map<String,Object>> compute() {
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	        //如果任务足够小就计算
	        boolean canCompute = (end - start) <= THREAD_HOLD;
	        if(canCompute){
			    memberCriteriaVo.setStartRows(start*ConstantUtils.PROCESS_PAGE_SIZE_MAX);
			    long s1 = System.currentTimeMillis();
			    logger.info("会员短信群发-立即发送:筛选条件打印"+"\r\n"+memberCriteriaVo.toString()+"\r\n");
			    List<String> nums = null;//memberDTOService.findSendMemberInfoList(memberCriteriaVo,memberCriteriaVo.getUserId());
			    start++;
			    if(nums==null || nums.isEmpty()){
				  unExist++;
				  return null;
			    }
			     exist++;
			   logger.info("会员短信群发-立即发送:查询出"+(nums.size())+"条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
			   logger.info("会员短信群发-立即发送:此次查询时间开销"+(System.currentTimeMillis()-s1)+"millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
			   List<String> decNums = decryptMemeberMobiles(nums,sendMsgVo.getUid());
			   if(decNums==null || decNums.isEmpty()){
				logger.info("会员短信群发-立即发送:此次查询结果为空!"+"millis^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
				return null;
			   }
			  logger.info("会员短信群发-立即发送:解密后的数据大小为"+(decNums.size())+"条数据^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
			  //筛选出重复手机号,错误手机号.....
			 Map<String,Object>  sortMap = sortDataBatch(decNums,vSet);
			 if(sortMap.get("sendNums") !=null){
				List<String> sendNums = (ArrayList<String>)sortMap.get("sendNums");
				if(sendNums.size()>0){
					Map<String, Integer> resultMap =null;//batchSendMsg(sortMap,sendNums.toArray(new String[0]),sendMsgVo,true,false,pool,task);
					if(resultMap!=null&&!resultMap.isEmpty()){
					    sortMap.put("succeedNum", resultMap.get("succeedNum")==null?0:resultMap.get("succeedNum"));
					    sortMap.put("failedNum", resultMap.get("failedNum")==null?0:resultMap.get("failedNum"));
					    sortMap.put("successSmsSum", resultMap.get("successSmsSum")==null?0:resultMap.get("successSmsSum"));
					}
				}
			 }
			 //需要个东西接收他
			 list.add(sortMap);
	        }else{
	        	Long middle = (start + end) / 2;
	            ForkJoinUtil left = new ForkJoinUtil(start,middle, sendMsgVo, memberCriteriaVo);
	            ForkJoinUtil right = new ForkJoinUtil(middle+1,end, sendMsgVo, memberCriteriaVo);
	            //执行子任务
	            left.fork();
	            right.fork();
	            //获取子任务结果
	            List<Map<String, Object>> lResult = left.join();
	            List<Map<String, Object>> rResult = right.join();
	            list.addAll(lResult);
	            list.addAll(rResult);
	        }
	        return list;
		}
		}*/

}
