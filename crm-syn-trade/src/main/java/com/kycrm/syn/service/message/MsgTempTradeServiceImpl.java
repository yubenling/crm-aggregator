package com.kycrm.syn.service.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.trade.MsgTempTrade;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.effect.CustomerDetailVO;
import com.kycrm.member.service.message.IMsgTempTradeService;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.dao.message.IMsgTempTradeDao;

@Service
@MyDataSource
public class MsgTempTradeServiceImpl implements IMsgTempTradeService {

	private Logger logger = LoggerFactory.getLogger(MsgTempTradeServiceImpl.class);
	
	@Autowired
	private IMsgTempTradeDao tempTradeDao;

	@Override
	public Boolean doCreateTable(Long uid) {
		if (uid == null) {
			return false;
		}
		String tables = tempTradeDao.tableIsExist(uid);
		if (tables != null && !tables.isEmpty()) {
			return true;
		}
		try {
			tempTradeDao.doCreateTable(uid);
			logger.info("创建UID = "+ uid +" 的MsgTempTrade表");
			tempTradeDao.addMsgTempTradeTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<MsgTempTrade> listFifteenTrade(Long uid, Date fifteenAgoTime, int startRows, int pageSize) {
		return null;
	}


	@Override
	public Map<String, Object> limitCustomerDetail(Long uid, CustomerDetailVO customerDetailVO, UserInfo userInfo) {
		return null;
	}

	@Override
	public List<MsgTempTrade> listTempTradeByMsgId(Long uid, Long msgId) {
		return null;
	}

	@Override
	public void deleteDataByMsgId(Long uid, Long msgId) {
		
	}

	@Override
	public Double sumPaymentByDate(Long uid, Date bTime, Date eTime) {
		return null;
	}

	@Override
	public Map<String, Object> limitStepCustomerDetail(Long uid, CustomerDetailVO customerDetailVO, UserInfo userInfo) {
		return null;
	}

}
