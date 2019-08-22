package com.kycrm.transferdata.thread;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.member.service.item.IItemTransferService;
import com.kycrm.transferdata.item.service.IOldItemService;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;

public class ItemTransferDataThread implements Callable<Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ItemTransferDataThread.class);

	private String userId;

	private Long uid;

	private IOldItemService oldItemService;

	private IItemTransferService itemTransferService;

	private int startPosition;

	private int endPosition;

	private Date startDate;

	private Date endDate;

	public ItemTransferDataThread(String userId, Long uid, IOldItemService oldItemService,
			IItemTransferService itemTransferService, int startPosition, int endPosition, Date startDate,
			Date endDate) {
		super();
		this.userId = userId;
		this.uid = uid;
		this.oldItemService = oldItemService;
		this.itemTransferService = itemTransferService;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public Integer call() throws Exception {
		List<Item> oldItemList = null;
		try {
			LOGGER.info("UID = " + uid + " userId = " + userId + " startPosition = " + startPosition + " endPosition = "
					+ endPosition + " startDate = " + startDate + " endDate = " + endDate);
			// 从老的RDS上获取对应用户的黑名单数据
			oldItemList = this.oldItemService.findItemsByUid(uid, startPosition, endPosition, startDate,
					endDate);
			String jsonList = JsonUtil.toJson(oldItemList);
			byte[] compress = GzipUtil.compress(jsonList);
			// 批量插入到分库分表
			this.itemTransferService.batchSaveItemsByUid(uid, compress);
		} catch (Exception e) {
			LOGGER.error("SmsBlackListTransferDataThread 发送错误 : ", e);
		}
		return oldItemList.size();
	}

}
