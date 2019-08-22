package com.kycrm.transferdata.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.message.SmsBlackListDTO;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.transferdata.entity.SmsBlackList;
import com.kycrm.transferdata.smsblacklist.service.IOldSmsBlackListService;

public class SmsBlackListTransferDataThread implements Callable<Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsBlackListTransferDataThread.class);

	private String userId;

	private Long uid;

	// 从老的RDS数据库上获取黑名单
	private IOldSmsBlackListService oldSmsBlackListService;

	// 分库分表存储黑名单
	private ISmsBlackListDTOService smsBlackListDTOService;

	private int startPosition;

	private int endPosition;

	private Date startDate;

	private Date endDate;

	public SmsBlackListTransferDataThread(String userId, Long uid, IOldSmsBlackListService oldSmsBlackListService,
			ISmsBlackListDTOService smsBlackListDTOService, int startPosition, int endPosition, Date startDate,
			Date endDate) {
		super();
		this.userId = userId;
		this.uid = uid;
		this.oldSmsBlackListService = oldSmsBlackListService;
		this.smsBlackListDTOService = smsBlackListDTOService;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public Integer call() throws Exception {
		Integer transferCount = 0;
		try {
			LOGGER.info("UID = " + uid + " userId = " + userId + " startPosition = " + startPosition + " endPosition = "
					+ endPosition + "startDate = " + startDate + " endDate = " + endDate);
			// 从老的RDS上获取对应用户的黑名单数据
			List<SmsBlackList> oldSmsBlackList = this.oldSmsBlackListService.getSmsBlackListByRange(userId,
					startPosition, endPosition, startDate, endDate);
			List<SmsBlackListDTO> newSmsBlackList = new ArrayList<SmsBlackListDTO>(endPosition);
			SmsBlackList oldSmsBlack = null;
			SmsBlackListDTO newSmsBlack = null;
			for (int i = 0; i < oldSmsBlackList.size(); i++) {
				oldSmsBlack = oldSmsBlackList.get(i);
				newSmsBlack = new SmsBlackListDTO();
				newSmsBlack.setUid(uid);
				newSmsBlack.setNickOrPhone(oldSmsBlack.getContent());
				newSmsBlack.setNick(oldSmsBlack.getNick());
				newSmsBlack.setPhone(oldSmsBlack.getPhone());
				String addSource = oldSmsBlack.getAddSource();
				if ("手动添加".equals(addSource) || "黑名单添加".equals(addSource)) {
					addSource = "1";
				} else if ("黑名单导入".equals(addSource)) {
					addSource = "2";
				} else if ("回复退订".equals(addSource)) {
					addSource = "3";
				} else {
					addSource = "0";
				}
				newSmsBlack.setAddSource(addSource);
				newSmsBlack.setType(oldSmsBlack.getType());
				newSmsBlack.setCreatedDate(oldSmsBlack.getCreatedate());
				newSmsBlackList.add(newSmsBlack);
				transferCount++;
			}
			Map<String, Object> map = new HashMap<String, Object>(2);
			map.put("list", newSmsBlackList);
			map.put("uid", uid);
			// this.smsBlackListDTOService.clearAllSmsBlack(uid);
			this.smsBlackListDTOService.insertSmsBlackList(uid, map);
		} catch (Exception e) {
			LOGGER.error("SmsBlackListTransferDataThread 发送错误 : ", e);
		}
		return transferCount;
	}

}
