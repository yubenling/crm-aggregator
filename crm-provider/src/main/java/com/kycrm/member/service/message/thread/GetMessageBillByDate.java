package com.kycrm.member.service.message.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.message.MessageBill;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.multishopbinding.MultiShopBindingSendMessageRecordDTO;
import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.service.message.IMessageBillService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.multishopbinding.IMultiShopBindingSendMessageRecordService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserRechargeService;

public class GetMessageBillByDate implements Callable<List<MessageBill>> {

	private Logger logger = LoggerFactory.getLogger(GetMessageBillByDate.class);

	private Long uid;

	private ISmsRecordDTOService smsRecordDTOService;

	private IUserRechargeService userRechargeService;

	private IMultiShopBindingSendMessageRecordService multiShopBindingSendMessageRecordService;

	private IUserAccountService userAccountService;

	private String dateType;

	private Date bTime;

	private Date eTime;

	public GetMessageBillByDate(Long uid, ISmsRecordDTOService smsRecordDTOService,
			IUserRechargeService userRechargeService,
			IMultiShopBindingSendMessageRecordService multiShopBindingSendMessageRecordService,
			IUserAccountService userAccountService, String dateType, Date bTime, Date eTime) {
		super();
		this.uid = uid;
		this.smsRecordDTOService = smsRecordDTOService;
		this.userRechargeService = userRechargeService;
		this.multiShopBindingSendMessageRecordService = multiShopBindingSendMessageRecordService;
		this.userAccountService = userAccountService;
		this.dateType = dateType;
		this.bTime = bTime;
		this.eTime = eTime;
	}

	// @Override
	// public MessageBill call() throws Exception {
	// MessageBill messageBill = new MessageBill();
	// messageBill.setSendDate(selectDate);
	// Long userRechargeCount =
	// this.userRechargeService.findRechargeRecordCount(uid, null, bTime,
	// eTime);
	// Long userReceiveCount =
	// this.multiShopBindingSendMessageRecordService.findSingleReceiveCount(uid,
	// null, bTime,
	// eTime);
	// userRechargeCount = userRechargeCount == null ? 0L : userRechargeCount;
	// userReceiveCount = userReceiveCount == null ? 0L : userReceiveCount;
	// // 增加短信条数 = 充值条数 + 获赠条数
	// // messageBill.setAddMessageCount(userRechargeCount + userReceiveCount);
	// // 扣除短信条数=发送条数 + 赠送条数
	// Long userMessageSendCount =
	// this.messageBillService.getMessageBillByDate(uid, bTime, eTime);
	// Long userSingleSendCount =
	// this.multiShopBindingSendMessageRecordService.findSingleSendCount(uid,
	// null, bTime,
	// eTime);
	// userMessageSendCount = userMessageSendCount == null ? 0L :
	// userMessageSendCount;
	// userSingleSendCount = userSingleSendCount == null ? 0L :
	// userSingleSendCount;
	// // messageBill.setDeductMessageCount(userMessageSendCount +
	// // userSingleSendCount);
	// // 剩余短信条数= 当前短信条数 - 扣除短信条数
	// Long userAccountSms = this.userAccountService.findUserAccountSms(uid);
	// userAccountSms = userAccountSms == null ? 0L : userAccountSms;
	// Long deductMessageCount = messageBill.getDeductMessageCount() == null ?
	// 0L
	// : messageBill.getDeductMessageCount();
	// // messageBill.setRestMessageCount(userAccountSms - deductMessageCount);
	// String bTimeStr = DateUtils.formatDate(bTime, "yyyy-MM-dd HH:mm:ss");
	// String eTimeStr = DateUtils.formatDate(eTime, "yyyy-MM-dd HH:mm:ss");
	// logger.info("UID = " + uid + " 从" + bTimeStr + "到" + eTimeStr + " 充值条数 =
	// " + userRechargeCount + " 获赠条数 = "
	// + userReceiveCount + " 发送条数 = " + userMessageSendCount + " 赠送条数 = " +
	// userSingleSendCount + " 当前短信条数 = "
	// + userAccountSms + " 扣除短信条数 = " + deductMessageCount);
	// return messageBill;
	// }

	@Override
	public List<MessageBill> call() throws Exception {
//		// 充值条数
//		List<UserRecharge> userRechargeList = this.userRechargeService.findRechargeRecordCountByDate(uid, dateType,
//				bTime, eTime);
//		// 短信发送条数
//		List<SmsRecordDTO> smsRecordList = this.smsRecordDTOService.sumReportSmsNumByDate(uid, dateType, bTime, eTime);
//		// 赠送条数
//		List<MultiShopBindingSendMessageRecordDTO> sendCountList = this.multiShopBindingSendMessageRecordService
//				.findSingleSendCountByDate(uid, dateType, bTime, eTime);
//		// 获赠条数
//		List<MultiShopBindingSendMessageRecordDTO> receiveCountList = this.multiShopBindingSendMessageRecordService
//				.findSingleReceiveCountByDate(uid, dateType, bTime, eTime);
//		// 当前剩余条数
//		Long userAccountSms = this.userAccountService.findUserAccountSms(uid);
//		List<MessageBill> messageBillList = new ArrayList<MessageBill>();
//		MessageBill messageBill = null;
//		for (int i = 0; i < userRechargeList.size(); i++) {
//			messageBill = new MessageBill();
//			// 增加短信条数 = 充值条数 + 获赠条数
//			messageBill.setAddMessageCount(
//					userRechargeList.get(i).getRechargeNum() + receiveCountList.get(i).getSendMessageCount());
//			// 扣除短信条数=发送条数 + 赠送条数
//			messageBill.setDeductMessageCount(
//					smsRecordList.get(i).getActualDeduction() + sendCountList.get(i).getSendMessageCount());
//			// 剩余短信条数= 当前短信条数 - 扣除短信条数
//			messageBill.setRestMessageCount(userAccountSms.intValue() - messageBill.getDeductMessageCount());
//			messageBillList.add(messageBill);
//		}
//		return messageBillList;
		return null;
	}

}
