package com.kycrm.syn.service.marketing;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.effect.MarketingCenterEffect;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.vo.effect.PayOrderEffectDetailVO;
import com.kycrm.member.service.effect.IMarketingCenterEffectService;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.dao.marketing.IMarketingCenterEffectDao;

@Service
@MyDataSource
public class MarketingCenterEffectServiceImpl implements IMarketingCenterEffectService {

	@Autowired
	private IMarketingCenterEffectDao marketingCenterEffectDao;
	
	/**
	 * 创建表
	 */
	@Override
	public Boolean doCreateTable(Long uid){
		if(uid == null){
			return false;
		}
		List<String> tables = this.marketingCenterEffectDao.tableIsExist(uid);
		if(tables != null && !tables.isEmpty()){
			return true;
		}
		try {
			this.marketingCenterEffectDao.doCreateTable(uid);
			this.marketingCenterEffectDao.addMarketingCenterEffectTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public void saveMarktingCenterEffect(Long uid, MarketingCenterEffect effect) {
	}

	@Override
	public void updateMarktingEffectByParam(Long uid, MarketingCenterEffect effect) {
	}


	@Override
	public double sumPayFeeByTime(Long uid, Date bTime, Date eTime, Long msgId) {
		return 0;
	}

	@Override
	public List<TradeDTO> queryTradeNumByHour(Long uid, Date beginTime, Date endTime) {
		return null;
	}

	@Override
	public MarketingCenterEffect findEffectByDays(Long uid, Long msgId, String tradeFrom, Integer days) {
		return null;
	}

	@Override
	public MarketingCenterEffect findRealBuyerNum(Long uid, Long msgId, int i, String orderSource) {
		return null;
	}

	@Override
	public List<MarketingCenterEffect> listEffectPicturesByDay(Long uid, MarketingCenterEffect effectPicture) {
		return null;
	}

	@Override
	public List<TradeDTO> queryTradeByMsgRecord(Long uid, MsgSendRecord msgSendRecord, Date bTime, Date eTime) {
		return null;
	}

	@Override
	public MarketingCenterEffect sumMarketingCenterEffect(Long uid, MsgSendRecord msgRecord, String tradeFrom,
			Date bTime, Date eTime) {
		return null;
	}

	@Override
	public List<PayOrderEffectDetailVO> listPayData(Long uid, Long msgId, String tradeFrom, Date bTime, Date eTime) {
		return null;
	}

	@Override
	public void synchSaveMsgTempTrade(Long uid, List<TradeDTO> tradeByMsg, MsgSendRecord msg) {
	}

	@Override
	public void handleData(Long uid, MsgSendRecord msgRecord) {
		
	}


	@Override
	public MarketingCenterEffect findEffectByParam(Long uid, Long msgId,
			Integer days, String tradeFrom) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> findAllMsgIdByTime(Long uid, Date fifteenAgoTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sychnMarketingData(Long uid, MsgSendRecord msgRecord,
			Date bTime, Date eTime) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void singleSynchEffectData(Long uid, MsgSendRecord msgRecord,
			Date bTime, Date eTime) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Double sumPaidByDate(Long uid, MsgSendRecord msgRecord, Date bTime, Date eTime) {
		return null;
	}

	@Override
	public void synchSaveStepMsgTempTrade(Long uid, List<TradeDTO> tradeByMsg, MsgSendRecord msg) throws Exception {
	}

	@Override
	public void handleStepData(Long uid, MsgSendRecord msgRecord) {
	}

	@Override
	public MarketingCenterEffect sumMarketingStepCenterEffect(Long uid, MsgSendRecord msgRecord, String tradeFrom,
			Date bTime, Date eTime) throws Exception {
		return null;
	}

	@Override
	public List<PayOrderEffectDetailVO> listStepPayData(Long uid, Long msgId, String tradeFrom, Date bTime,
			Date eTime) {
		return null;
	}

	@Override
	public List<PayOrderEffectDetailVO> listStepPayFrontData(Long uid, Long msgId, String tradeFrom, Date bTime,
			Date eTime) {
		return null;
	}

	

	

	
}
