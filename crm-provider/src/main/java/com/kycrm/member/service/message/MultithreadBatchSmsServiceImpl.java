/** 
 * Project Name:s2jh4net 
 * File Name:TestClass.java 
 * Package Name:s2jh.biz.shop.crm.taobao.util 
 * Date:2017年3月18日下午3:25:55 
 * Copyright (c) 2017,  All Rights Reserved. 
 * author zlp
*/  
  
package com.kycrm.member.service.message;  
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.message.BatchSmsData;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.message.SmsReportInfo;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.JudgeUserUtil;
import com.kycrm.member.service.user.UserAccountServiceImpl;
import com.kycrm.member.service.vip.VipUserServiceImpl;
import com.kycrm.member.util.ReturnMessage;
import com.kycrm.member.util.sms.SendMessageTestUtil;
import com.kycrm.member.util.sms.SendMessageUtil;
import com.kycrm.member.util.sms.SendMessageVipUtil;
import com.kycrm.util.Constants;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MsgType;
import com.kycrm.util.SendMessageStatusInfo;
import com.kycrm.util.thread.MyFixedThreadPool;


/** 
 * MultithreadService <br/> 
 * Date:     2017年3月18日 下午3:25:55 <br/> 
 * @author   zlp
 * @version   1.0
 */
@Service("multithreadBatchSmsService")
public class MultithreadBatchSmsServiceImpl implements IMultithreadBatchSmsService{
	
	private static final Log logger = LogFactory.getLog(MultithreadBatchSmsServiceImpl.class);
	@Autowired
	private IMemberDTOService memberDTOService;
	@Autowired
	private IUserAccountService userAccountService;
	@Autowired
	private  ISmsRecordDTOService smsRecordService;
	@Autowired
	private  JudgeUserUtil judgeUserUtil;
	@Autowired
	private  ISmsReportInfoService smsReportInfoService;
	
    //回调处理链条
    interface DataHandler{
        void doHandler(String[] data);
    }
    //error处理链条
    interface ErrorHandler{
        void doHandler(Throwable t);
        public static final ErrorHandler PRINTER = new ErrorHandler() {
            public void doHandler(Throwable t) {
                t.printStackTrace();
                logger.error("批量处理线程异常");
            }
        };
    }
    /**
     * 数据处理器
     */
    class BigDataProcessor{
        /**
         * 记录的分隔符，每个分隔符占一行。
         */
        public static final String DEFAULT_SEPERATOR = "***";
        public static final int COUNT = 1000;
        /**
         * 用于干掉处理数据的线程。
         */
        public final Object POISON = new Object();
        private BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(64);
        private String seperator = DEFAULT_SEPERATOR;
        private ErrorHandler errorHandler = ErrorHandler.PRINTER;
        /**
         * 用于终止读取线程，非强制终止。
         */
        private volatile boolean running = false;
        /**
         * 数据读取线程
         */
        private Thread readerThread;
        /**
         * 数据处理线程
         */
        private Thread[] proccessors;
        public BigDataProcessor(final LinkedBlockingQueue<String> dataQuee, final DataHandler handler) {
        	readerThread = new Thread(new Runnable() {
                public void run() {
                    try {
                    	// 将批量数据分批存入队列
                        ArrayList<String> cache = new ArrayList<String>();
                        while(running&&dataQuee!=null&&dataQuee.size()>0){
                        	String takedata = dataQuee.take();
                            if(seperator.equals(takedata)){
                            	String[] data = cache.toArray(new String[cache.size()]);
                            	cache.clear();
                            	queue.put(data);
                            }else{
                                cache.add(takedata);
                            }
                        }
                        if(null!=cache&&cache.size()>0){ //最后一次如果不满足的话
                        	String[] data = cache.toArray(new String[cache.size()]);
                            cache.clear();
                            queue.put(data);
                        }
                    } catch (Throwable t) {
                        errorHandler.doHandler(t);
                    } finally {
                    	try {
                            queue.put(POISON);
                        } catch (InterruptedException e) {
                        	errorHandler.doHandler(e);
                        }
                    }
                }
            },"reader_thread");
            //默认创建处理的线程数，与CPU处理的内核数相同
            proccessors = new Thread[Runtime.getRuntime().availableProcessors()];
            Runnable worker = new Runnable() {
                public void run() {
                	for(;;){
	                    try {
                            Object obj = queue.take();
                            if(obj==POISON){
                                queue.put(obj);
                                break;
                            }else{
                                String[] data =(String[])obj;
                                handler.doHandler(data);
                            }
	                    } catch (Throwable t) {
	                        errorHandler.doHandler(t);
	                    }
                	}
                }
            };
            for(int i=0;i<proccessors.length;i++){
                proccessors[i] = new Thread(worker,"proccessor-thread_"+i);
            }
        }
        public void setErrorHandler(ErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
        }

        public void setSeperator(String seperator) {
            this.seperator = seperator;
        }
        /**
         * 开启处理过程
         */
        public synchronized void start(){
            if(running)return ;
            running = true;
            readerThread.start();
            for(int i=0;i<proccessors.length;i++){
                proccessors[i].start();
            }
        }
        /**
         * 中断处理过程，非强制中断
         */
        public synchronized void shutdown(){
            if(running){
                running = false;
            }
        }
        /**
         * 试图等待整个处理过程完毕
         */
        public void join(){
            try {
            	readerThread.join();
            } catch (InterruptedException e) {
                errorHandler.doHandler(e);
            }
            for(int i=0;i<proccessors.length;i++){
                try {
                    proccessors[i].join();
                } catch (InterruptedException e) {
                    errorHandler.doHandler(e);
                }
            }
        }
    }
    /**
     * 
     * BatchSmsData 调用前请赋值  手机号数组 String data[]  存入手机号
     * 		userId 
     *      短信签名autograph
     *      短信内容 content
     *      依次赋值
     *      批量发短信主方法
     *      @author zlp
     *      @param obj
     */
    @Override
    public Map<String, Integer> batchOperateSms(final BatchSmsData obj) {
    	Map<String, Integer> resultMap = new HashMap<String, Integer>(2);
    	resultMap.put("succeedNum", 0);
    	resultMap.put("failedNum", 0);
    	 int isBigData = judgeIsBigData(obj);
    	 logger.info("数据判断"+isBigData);
		 if(2==isBigData){
			batchHandleSmallData(obj);
		 }else if(3==isBigData){
			batchHandleBigData(obj);
		 }else if(1==isBigData){
			 return resultMap;
		 }
		 int sNum = obj.getSuccess(); // 得到成功条数
		 int fNum = obj.getFail(); // 得到失败条数
		 if (sNum != 0 || fNum != 0) {
			resultMap.put("succeedNum", sNum);
			resultMap.put("failedNum", fNum);
		 }
		 logger.info("批量发短信end");
		 return resultMap;
	}
  

	private void batchHandleBigData(final BatchSmsData obj) {
		//声明队列 拼装队列数据
		LinkedBlockingQueue<String> dataQuee  = new LinkedBlockingQueue<String>();
		try {
			dataQuee = buildBlockQueue(obj,dataQuee);
		} catch (InterruptedException e1) {
			 logger.error("封装队列数据失败！"+obj.getTotal());
		}
		if(null!=dataQuee&&dataQuee.size()>0){
		     //队列数据没问题  先扣费  在进行发送
			 logger.info("开始扣费！");
			 boolean result = deductionUserSms(obj);
			 if(result){
				 logger.info("扣费成功！");
				 //回调函数处理
				 DataHandler dataHandler = new DataHandler() {
					 public void doHandler(String[] data) {
						 synchronized (obj) {
							 try {
								 logger.info("批处理进来了！");
								 unifySendSmsData(obj, data);
							 } catch (Exception e) {
								 logger.error("短信发送失败"+e.getMessage()+obj.getContent());
							 }
						 }
					 }
				 };
				 BigDataProcessor processor = new BigDataProcessor(dataQuee,dataHandler);
				 processor.start();
				 processor.join();
				 logger.info("批处理线程处理完毕！");
				 handleLogicAfterSendSms(obj,true);
			 }else{
				 logger.info("余额不足"+obj.getUserId()); 
			 }
		}
	}
	
	private void batchHandleSmallData(BatchSmsData obj) {
	 	boolean result = deductionUserSms(obj);
	    if(result){
	    	unifySendSmsData(obj,obj.getDatas());
	    	handleLogicAfterSendSms(obj,true);
	    }
	}
	/**
	 * 
	 * @param obj
	 * @param data 手机号:买家昵称
	 */
	private void unifySendSmsData(final BatchSmsData obj, String[] data) {
		List<String> phoneList=new ArrayList<String>();
		for(String phoneAndNick:data){ //将手机号分离出来
			 phoneList.add(phoneAndNick.split(Constants.SMSSEPARATOR_S)[0]);
		}
	    String s = Arrays.toString(phoneList.toArray(new String[phoneList.size()])).replace("[", "").replace("]", "").replace(" ", "");
		Map<String,Object>  linkedMap =  new LinkedHashMap<String, Object>();
		String status = null;
		/*if(obj.isVip()){
			if(MsgType.MSG_CSFS.equals(obj.getType())){
				String result = SendMessageTestUtil.sendMessage(s,obj.getContent(), null, obj.getUid().toString());
				ReturnMessage returnMessage = JsonUtil.fromJson(result, ReturnMessage.class);
				status = returnMessage.getReturnCode();
			}else{
				String result = SendMessageVipUtil.sendMessage(s,obj.getContent(), null, obj.getUid().toString());
				ReturnMessage returnMessage = JsonUtil.fromJson(result, ReturnMessage.class);
				status = returnMessage.getReturnCode();
			}
		}else{*/
			if(MsgType.MSG_CSFS.equals(obj.getType())){
				String result = SendMessageTestUtil.sendMessage(s,obj.getContent(), null, obj.getUid().toString());
				ReturnMessage returnMessage = JsonUtil.fromJson(result, ReturnMessage.class);
				status = returnMessage.getReturnCode();
			}else{
				String result = SendMessageUtil.sendMessage(s,obj.getContent(), null, obj.getUid().toString());
				ReturnMessage returnMessage = JsonUtil.fromJson(result, ReturnMessage.class);
				status = returnMessage.getReturnCode();		
			}
		/*}*/
		if("100".equals(status)){
			obj.setSuccess(obj.getSuccess()+data.length);
		    linkedMap.put(status, Arrays.asList(data));
		 	obj.getSuccessData().add(linkedMap);
		}else{
			obj.setFail(obj.getFail()+data.length);
			linkedMap.put(status, Arrays.asList(data));
			obj.getFailData().add(linkedMap);
		}
    }
	public void handleLogicAfterSendSms( BatchSmsData obj,boolean isASync){
		logger.info("isASync！"+isASync+"失败数量"+obj.getFail());
		if(obj.getFail()!=0){
			 resumeUserSms(obj);
		}
		if(isASync){
			logger.info("异步处理");
			asyncHandleRecord(obj);
		}else{
			logger.info("同步处理");
			batchHandleResultData(obj);
		}
	}


	private LinkedBlockingQueue<String> buildBlockQueue(final BatchSmsData obj,LinkedBlockingQueue<String> dataQuee)
			throws InterruptedException {
		//声明队列 拼装队列数据每五百条作为一个队列组
		for (int i = 0; i < obj.getTotal(); i++) {
			if(i!=0&&i % 500 == 0){
				dataQuee.put(BigDataProcessor.DEFAULT_SEPERATOR);
			}
			dataQuee.put(obj.getDatas()[i]);
		}
		logger.info("dataQuee size"+dataQuee.size());
		return dataQuee;
	}

	
    /**
     * judgeIsBigData:判断参数数据是否是大数据   大数据 则调用多线程异步处理 . <br/> 
     * @author zlp
     * @param  BatchSmsData   
     * @return int 1 是参数错误  2标识小数据  3 标识大数据
     */
	private int judgeIsBigData( BatchSmsData obj){
		int flag = 1;
		if(null!=obj&&obj.getTotal()>0){
			logger.info("短信内容"+obj.getContent()+"发送条数"+obj.getTotal());
			if(obj.getTotal()<=1000){ flag = 2; }else{ flag = 3;}
		}
		return flag;
	}
    
    

	private boolean  resumeUserSms( BatchSmsData obj) {
    	boolean result = false;
		try {
			result  = userAccountService.doUpdateUserSms(obj.getUid(),obj.getUserId(), Constants.ADD_SMS, 
														obj.getFail()*obj.getActualDeduction(),obj.getType(),obj.getUserId(),obj.getIpAdd(),
														Constants.USER_OPERATION_MORE+"，增加短信"+obj.getFail()*obj.getActualDeduction()+"条",UserAccountServiceImpl.TIME_OUT);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("短信内容"+obj.getContent()+"恢复"+obj.getUserId()+"发送条数"+obj.getTotal()+"失败"+e.getMessage());
		}
		if(result){
			 updateReprotInfo(obj);
		}
		return  result;
	}
    /**
     * 更新账单
     * @param obj
     */
	private void updateReprotInfo(BatchSmsData obj) {
		SmsReportInfo  smsReportInfo=new SmsReportInfo();
		smsReportInfo.setId(obj.getReportInfoId());
		smsReportInfo.setSmsNum(obj.getFail()*obj.getActualDeduction().longValue());
		smsReportInfoService.updateReprotInfo(obj.getUid(),smsReportInfo);	
	}
	
	private boolean deductionUserSms(BatchSmsData obj) {
    	boolean result = false;
		try {
			result =userAccountService.doUpdateUserSms(obj.getUid(),obj.getUserId(), SendMessageStatusInfo.DEL_SMS, 
														obj.getDatas().length*obj.getActualDeduction(),obj.getType(),obj.getUserId(),obj.getIpAdd(),
														Constants.USER_OPERATION_MORE+"，扣除短信"+obj.getDatas().length*obj.getActualDeduction()+"条",UserAccountServiceImpl.TIME_OUT);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("短信内容"+obj.getContent()+"扣费"+obj.getUserId()+"发送条数"+obj.getTotal()+"失败"+e.getMessage());
			result = false;
		}
		if(result){
			//扣除账单写入短信
			addReportInfo(obj);
		}
		return result;
	}
    /**
     * 
	 * 向短信账单中添加数据
	 * @param obj
	 */
	private void addReportInfo(BatchSmsData obj) {
		SmsReportInfo smsReportInfo=new SmsReportInfo();
		smsReportInfo.setSmsNum(obj.getDatas().length*obj.getActualDeduction().longValue());
		smsReportInfo.setUid(obj.getUid());
		smsReportInfo.setCreatedBy(obj.getUserId());
		smsReportInfo.setLastModifiedBy(obj.getUserId());
		smsReportInfo.setType(obj.getType());
		smsReportInfo.setSendDate(new Date());
		Long reportInfoId=smsReportInfoService.addSmsReportInfo(obj.getUid(),smsReportInfo);
		obj.setReportInfoId(reportInfoId);
	}
	private   void asyncHandleRecord(final BatchSmsData obj) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
			@Override
			public void run() {
				batchHandleResultData(obj);
			}
		});
	}

	public void  batchHandleResultData(final BatchSmsData obj){
		if(obj.getFailData().size()>0){
			logger.info("保存失败短信！");
			batchHandleRecord(obj,1);
		}
		if(obj.getSuccessData().size()>0){
			logger.info("保存成功短信！");
			batchHandleRecord(obj,2);
		}
	}
	

	public List<SmsRecordDTO> batchHandleRecord(final BatchSmsData obj,int type){
		logger.info("type 2 是保存  info 表记录！ "+type);
		List<SmsRecordDTO> smsSendRecordList =null;
		logger.info("保存所有发送的短信");
		try {
			List<Map<String, Object>> failData = type==1?obj.getFailData():obj.getSuccessData();
			for (Map<String, Object> map : failData) {
				smsSendRecordList = new ArrayList<SmsRecordDTO>();
				Set<String> keySet = map.keySet();
				for (String key : keySet) {
					@SuppressWarnings("rawtypes")
					List list = (List)map.get(key);
					String sessionkey ="";
					try {
						sessionkey =this.judgeUserUtil.getUserTokenByRedis(obj.getUid());
					} catch (Exception e) {
						logger.info("保存记录session获取出错");
					}
					if("".equals(sessionkey)&&sessionkey==null){
						logger.error("非个性化sessionkey为null");
						break;
					}
					SmsRecordDTO  smsSendRecord  = null;
					for (Object object : list) {
						String [] phoneAndNick=((String)object).split(Constants.SMSSEPARATOR_S);
						smsSendRecord = new SmsRecordDTO();
						Date date = new Date();
						//区分指定号码群发,加买家昵称
						if(phoneAndNick.length==2){
							smsSendRecord.setBuyerNick(phoneAndNick[1]);	
						}
						smsSendRecord.setContent(obj.getContent());
						smsSendRecord.setSendTime(date);
						smsSendRecord.setReceiverTime(date);
						smsSendRecord.setType(obj.getType());
						smsSendRecord.setChannel(obj.getChannel());
						smsSendRecord.setActualDeduction("100".equals(key)?obj.getActualDeduction():0);
					    smsSendRecord.setResultCode(key);
					    smsSendRecord.setOrderId(obj.getTid()==null?null:obj.getTid());
                        smsSendRecord.setUid(obj.getUid());
					    smsSendRecord.setMsgId(obj.getMsgId()==null?null:obj.getMsgId());
					    smsSendRecord.setSource("2");
					    smsSendRecord.setStatus("100".equals(key)?SendMessageStatusInfo.RECORDSUCESS:SendMessageStatusInfo.RECORDFAIL);
						smsSendRecord.setAutograph(obj.getAutograph());
						smsSendRecord.setUserId(obj.getUserId());
						smsSendRecord.setRecNum(EncrptAndDecryptClient.getInstance().encrypt(phoneAndNick[0], EncrptAndDecryptClient.PHONE,sessionkey));
						smsSendRecord.setShow(true);
						//预售加tid
						if(phoneAndNick.length==3){
							smsSendRecord.setBuyerNick(phoneAndNick[1]);
							smsSendRecord.setOrderId(phoneAndNick[2]);
						}
						smsSendRecordList.add(smsSendRecord);
					}
				}
				if(null!=smsSendRecordList&&smsSendRecordList.size()>0){
					Thread.sleep(200);
					logger.info("保存  record 表记录！ ");
					smsRecordService.doCreaterSmsRecordDTOByList(obj.getUid(),smsSendRecordList);
					if(type==2){ //短信发出成功后，还需要更新会员信息
						List<SmsRecordDTO> syncUpdate=smsSendRecordList;
						MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
							@Override
							public void run() {
								try {
									Thread.sleep(500);
									batchHandleSmsInfoData(obj.getUid(),syncUpdate);
								} catch (InterruptedException e) {
									logger.info("更新会员信息出错");
									e.printStackTrace();
								}
							}
						});
					}
				}
			}
		} catch (Exception e) {
			logger.info("保存短信记录失败");
			e.printStackTrace();
		}
		return type==1?null:smsSendRecordList;
	} 
	/**
	 * 主要是更新会员的最后发送时间
	 * @param smsSendRecordList
	 */
	public void  batchHandleSmsInfoData(Long uid,List<SmsRecordDTO> smsSendRecordList){
	    //TODO 重要！！！  群发短信内容时，保存会员信息
		
    	if(null!=smsSendRecordList&&smsSendRecordList.size()>0){
    		String type = smsSendRecordList.get(0).getType();
    		logger.info("保存  info VipMember 表记录！type "+type);
    		List<MemberInfoDTO> memberList=new ArrayList<MemberInfoDTO>();
    		if(MsgType.MSG_HYDXQF.equals(type)||MsgType.MSG_DDDXQF.equals(type)){
    			for (SmsRecordDTO smsSendRecord : smsSendRecordList) {
    				MemberInfoDTO memberInfo =  new MemberInfoDTO();
    				memberInfo.setUid(smsSendRecord.getUid());
    				memberInfo.setMobile(smsSendRecord.getRecNum());
    				memberInfo.setLastMarketingTime(new Date());
    				memberInfo.setLastSendTime(new Date());
    				memberInfo.setMarketingSmsNumber(1);
    				memberList.add(memberInfo);
    			    //缺一个根据会员的电话，更新会员的最后发送时间
    			}
    			memberDTOService.updateMembeInfoListByPhone(uid, memberList);
    		}
    	}
		logger.info("更新vipMember  info 表记录！结束 ");
	}
	

}
  