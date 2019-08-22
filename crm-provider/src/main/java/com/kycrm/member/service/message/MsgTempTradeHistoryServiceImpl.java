package com.kycrm.member.service.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.message.IMsgTempTradeHistoryDao;

@MyDataSource
@Service("tempTradeHistoryService")
public class MsgTempTradeHistoryServiceImpl implements
		IMsgTempTradeHistoryService {
	
	@Autowired
	private IMsgTempTradeHistoryDao msgTempTradeHistoryDao;

	/**
	 * 创建该用户的表
	 */
	@Override
	public Boolean doCreateTable(Long uid){
		if (uid == null) {
			return false;
		}
		List<String> tables = msgTempTradeHistoryDao.tableIsExist(uid);
		if (tables != null && !tables.isEmpty()) {
			return true;
		}
		try {
			msgTempTradeHistoryDao.doCreateTable(uid);
			this.msgTempTradeHistoryDao.addMsgTempTradeHistoryTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
