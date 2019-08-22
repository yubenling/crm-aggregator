package com.kycrm.syn.service.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.service.message.IMsgTempTradeHistoryService;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.dao.message.IMsgTempTradeHistoryDao;

@Service
@MyDataSource
public class MsgTempTradeHistoryServiceImpl implements IMsgTempTradeHistoryService {

	@Autowired
	private IMsgTempTradeHistoryDao msgTempTradeHistoryDao;
	
	@Override
	public Boolean doCreateTable(Long uid) {
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
