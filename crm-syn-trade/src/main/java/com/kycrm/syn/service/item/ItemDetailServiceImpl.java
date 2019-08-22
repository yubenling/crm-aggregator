package com.kycrm.syn.service.item;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.effect.ItemTempTrade;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.vo.effect.CustomerDetailVO;
import com.kycrm.member.service.effect.IItemDetailService;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.dao.item.IItemTempTradeDao;
import com.kycrm.syn.dao.item.IItemTempTradeHistoryDao;

@Service
@MyDataSource
public class ItemDetailServiceImpl implements IItemDetailService {

	@Autowired
	private IItemTempTradeDao itemTempTradeDao;

	@Autowired
	private IItemTempTradeHistoryDao itemTempTradeHistoryDao;

	@Override
	public Boolean doCreateTable(Long uid) {
		if (uid == null) {
			return false;
		}
		List<String> tables = this.itemTempTradeDao.tableIsExist(uid);
		if (tables != null && !tables.isEmpty()) {
			return true;
		}
		try {
			this.itemTempTradeDao.doCreateTable(uid);
			this.itemTempTradeDao.addItemTempTradeTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Boolean doCreateItemHistory(Long uid) {
		if (uid == null) {
			return false;
		}
		List<String> isExist = itemTempTradeHistoryDao.tableIsExist(uid);
		if (isExist != null && !isExist.isEmpty()) {
			return true;
		}
		try {
			itemTempTradeHistoryDao.doCreateTable(uid);
			this.itemTempTradeHistoryDao.addItemTempTradeHistoryTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Map<String, Object> limitItemDetail(Long uid, CustomerDetailVO itemVO) {
		return null;
	}

	@Override
	public void deleteItemTempTrade(Long uid, Date fifteenAgoDate) {
	}

	@Override
	public List<ItemTempTrade> listItemDetail(Long uid, CustomerDetailVO itemVO, Boolean isHistory) {
		return null;
	}

	@Override
	public void saveItemDetail(Long uid, MsgSendRecord msg, List<TradeDTO> tradeDTOs) throws Exception {
		
	}

	@Override
	public void deleteDataByMsgId(Long uid, Long msgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveStepItemDetail(Long uid, MsgSendRecord msg, List<TradeDTO> tradeDTOs) throws Exception {
	}

	@Override
	public Map<String, Object> limitStepItemDetail(Long uid, CustomerDetailVO itemVO) {
		return null;
	}

	

	
}
