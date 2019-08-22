package com.kycrm.member.service.effect;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.effect.MarketingCenterEffect;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.vo.effect.PayOrderEffectDetailVO;

public interface IMarketingCenterEffectService {
	public Boolean doCreateTable(Long uid);

	/**
	 * 保存一条记录
	 * @Title: saveMarktingCenterEffect 
	 * @param @param effectPicture 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void saveMarktingCenterEffect(Long uid, MarketingCenterEffect effect);

	/**
	 * 根据条件更新一条记录
	 * @Title: updateMarktingEffectByParam 
	 * @param @param effectPicture 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void updateMarktingEffectByParam(Long uid, MarketingCenterEffect effect);
	
	/**
	 * 根据userId、msgId、days查询统计分析数据
	 * @Title: findEffectByParam 
	 * @param @param effectPicture
	 * @param @return 设定文件 
	 * @return MarketingCenterEffect 返回类型 
	 * @throws
	 */
	MarketingCenterEffect findEffectByParam(Long uid,Long msgId,Integer days, String tradeFrom);
	
	/**
	 * 首页计算会员营销数据
	 * ZTK2017年9月6日上午11:03:54
	 */
	double sumPayFeeByTime(Long uid,Date bTime,Date eTime,Long msgId);
	
	/**
	 * 首页查询每小时下单的订单数
	 * @Title: queryTradeNumByHour 
	 * @param @param tradeStatusModifiedLog
	 * @param @return 设定文件 
	 * @return TradeStatusModifiedLog 返回类型 
	 * @throws
	 */
	List<TradeDTO> queryTradeNumByHour(Long uid,Date beginTime,Date endTime);
	
	/**
	 * 营销中心效果分析汇总数据
	 * @Title: sumMarktingCenterEffect 
	 * @param @return 设定文件 
	 * @return EffectPicture 返回类型 
	 * @throws
	 */
	MarketingCenterEffect findEffectByDays(Long uid,Long msgId,String tradeFrom,
			Integer days);

	/**
	 * 营销中心效果分析汇总数据真实客户数据
	 * @Title: findRealBuyerNum 
	 * @param @param msgId
	 * @param @param i
	 * @param @param orderSource
	 * @param @return 设定文件 
	 * @return EffectPicture 返回类型 
	 * @throws
	 */
	MarketingCenterEffect findRealBuyerNum(Long uid, Long msgId, int i, String orderSource);
	
	/**
	 * 营销中心效果分析每日数据的集合
	 * @Title: listEffectPictures 
	 * @param @param Map
	 * @param @return 设定文件 
	 * @return List<EffectPicture> 返回类型 
	 * @throws
	 */
	List<MarketingCenterEffect> listEffectPicturesByDay(Long uid, MarketingCenterEffect effectPicture);

	/**
	 * handleData(处理发送总记录--营销效果分析)
	 * @Title: handleData 
	 * @param @param msgRecord 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void handleData(Long uid, MsgSendRecord msgRecord);

	/**
	 * queryTradeByMsgRecord(据发送总记录查询所有的发送号码)
	 * @Title: queryPhonesByMsgRecord 
	 * @param @param msgSendRecord
	 * @param @return 设定文件 
	 * @return List<TradeDTo> 返回类型 
	 * @throws
	 */
	List<TradeDTO> queryTradeByMsgRecord(Long uid, MsgSendRecord msgSendRecord,Date bTime,Date eTime) throws Exception;

	/**
	 * @throws Exception 
	 * sumMarketingCenterEffect(效果分析主页面的计算)
	 * @Title: sumMarketingCenterEffect 
	 * @param @param uid
	 * @param @param tradeFrom
	 * @param @param bTime
	 * @param @param eTime
	 * @param @return 设定文件 
	 * @return MarketingCenterEffect 返回类型 
	 * @throws
	 */
	MarketingCenterEffect sumMarketingCenterEffect(Long uid,MsgSendRecord msgRecord, String tradeFrom,
			Date bTime, Date eTime) throws Exception;

	/**
	 * listPayData(计算用户选择时间段内每天成交数据)
	 * @Title: listPayData 
	 * @param @param uid
	 * @param @param msgId
	 * @param @param tradeFrom
	 * @param @param bTime
	 * @param @param eTime 设定文件 
	 * @return List<PayOrderEffectDetailVO> 返回类型 
	 * @throws
	 */
	List<PayOrderEffectDetailVO> listPayData(Long uid, Long msgId, String tradeFrom, Date bTime,
			Date eTime);

	/**
	 * @throws Exception 
	 * synchSaveMsgTempTrade(每小时同步十五天的发送记录同步的订单到tempTrade中)
	 * @Title: synchSaveMsgTempTrade 
	 * @param  设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void synchSaveMsgTempTrade(Long uid, List<TradeDTO> tradeByMsg,MsgSendRecord msg) throws Exception;

	List<Long> findAllMsgIdByTime(Long uid, Date fifteenAgoTime);

	/**
	 * sychnMarketingData(同步订单到临时订单表用于实时计算营销中心效果分析)
	 * @Title: sychnMarketingData 
	 * @param @param uid
	 * @param @param msgRecord
	 * @param @param bTime
	 * @param @param eTime
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void sychnMarketingData(Long uid, MsgSendRecord msgRecord, Date bTime,
			Date eTime) throws Exception;

	/**
	 * singleSynchEffectData(一次性定时任务，同步历史订单数据到临时订单表)
	 * @Title: singleSynchEffectData 
	 * @param @param uid
	 * @param @param msgRecord
	 * @param @param bTime
	 * @param @param eTime
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void singleSynchEffectData(Long uid, MsgSendRecord msgRecord, Date bTime,
			Date eTime) throws Exception;

	/**
	 * sumPaidByDate(计算效果分析时间段内成交金额)
	 * @Title: sumPaidByDate 
	 * @param @param uid
	 * @param @param msgRecord
	 * @param @param bTime
	 * @param @param eTime
	 * @param @return 设定文件 
	 * @return Double 返回类型 
	 * @throws
	 */
	Double sumPaidByDate(Long uid, MsgSendRecord msgRecord, Date bTime,
			Date eTime);

	/**
	 * synchSaveStepMsgTempTrade(同步预售订单到效果分析临时库)
	 * @Title: synchSaveStepMsgTempTrade 
	 * @param @param uid
	 * @param @param tradeByMsg
	 * @param @param msg
	 * @param @throws Exception 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void synchSaveStepMsgTempTrade(Long uid, List<TradeDTO> tradeByMsg,
			MsgSendRecord msg) throws Exception;

	void handleStepData(Long uid, MsgSendRecord msgRecord);

	/**
	 * sumMarketingStepCenterEffect(预售效果分析主页面计算)
	 * @Title: sumMarketingStepCenterEffect 
	 * @param @param uid
	 * @param @param msgRecord
	 * @param @param tradeFrom
	 * @param @param bTime
	 * @param @param eTime
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return MarketingCenterEffect 返回类型 
	 * @throws
	 */
	MarketingCenterEffect sumMarketingStepCenterEffect(Long uid,
			MsgSendRecord msgRecord, String tradeFrom, Date bTime, Date eTime)
			throws Exception;

	/**
	 * listStepPayData(计算预售用户选择时间段内每天成交数据)
	 * @Title: listStepPayData 
	 * @param @param uid
	 * @param @param msgId
	 * @param @param tradeFrom
	 * @param @param bTime
	 * @param @param eTime
	 * @param @return 设定文件 
	 * @return List<PayOrderEffectDetailVO> 返回类型 
	 * @throws
	 */
	List<PayOrderEffectDetailVO> listStepPayData(Long uid, Long msgId,
			String tradeFrom, Date bTime, Date eTime);

	List<PayOrderEffectDetailVO> listStepPayFrontData(Long uid, Long msgId,
			String tradeFrom, Date bTime, Date eTime);
}
