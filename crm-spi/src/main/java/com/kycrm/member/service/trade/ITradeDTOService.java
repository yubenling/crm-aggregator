package com.kycrm.member.service.trade;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kycrm.member.domain.entity.effect.EffectStandardRFM;
import com.kycrm.member.domain.entity.effect.RFMDetailChart;
import com.kycrm.member.domain.entity.effect.TradeCenterEffect;
import com.kycrm.member.domain.entity.member.TempEntity;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.trade.TradeTempEntity;
import com.kycrm.member.domain.to.SimplePage;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;
import com.kycrm.member.domain.vo.trade.TradeResultVO;
import com.kycrm.member.domain.vo.trade.TradeVO;

/**
 * @author wy
 * @version 创建时间：2018年1月18日 下午2:38:32
 */
public interface ITradeDTOService {

	/**
	 * 根据用户主键id创建对应的短信记录表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:53:10
	 * @param uid
	 *            用户主键id
	 */
	public void doCreateTableByNewUser(Long uid);

	/**
	 * 查询订单状态
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 下午3:57:05
	 * @param uid
	 *            用户主键id
	 * @param tid
	 *            主订单号
	 * @return 订单存在返回对应状态，不存在返回空
	 */
	public String findTradeStatusByTid(Long uid, Long tid);

	/**
	 * 批量保存订单
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 下午4:31:37
	 * @param uid
	 *            用户主键id
	 * @param tradeList
	 *            要保持的订单集合
	 */
	public void batchInsertTradeList(Long uid, List<TradeDTO> tradeList);

	/**
	 * 订单短信群发筛选全部订单 @Title: listTradeInfo @param @param tradeVO @param @param
	 * sessionKey @param @return 设定文件 @return List<TradeResultVO> 返回类型 @throws
	 */
	public List<TradeResultVO> listMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey, String queryKey)
			throws Exception;

	/**
	 * 订单短信群发筛选全部订单个数 @Title: countTradeInfo @param @param
	 * tradeVO @param @return 设定文件 @return Long 返回类型 @throws
	 */
	public Long countMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey, String queryKey)
			throws Exception;

	/**
	 * 查询时间段内每天的下单金额 @Title: listWeekPayment @param @param
	 * tradeVO @param @return 设定文件 @return List<Double> 返回类型 @throws
	 */
	public List<Double> listDaysPayment(Long uid, Date endTime);

	/**
	 * 订单中心根据订单id查询催付订单数、金额以及回款的订单数、金额 @Title: sumEarningOrder @param @param
	 * uid @param @param tidList @param @param statusList @param @param
	 * beginTime @param @param endTime @param @return 设定文件 @return
	 * TradeCenterEffect 返回类型 @throws
	 */
	TradeCenterEffect sumEarningOrder(Long uid, List<String> tidList, List<String> statusList, Date beginTime,
			Date endTime);

	/**
	 * 订单中心效果分析查询状态变化的主订单 @Title: listTradeByStatus @param @return 设定文件 @return
	 * List<TradeDTO> 返回类型 @throws
	 */
	public List<TradeDTO> listTradeByStatus(Long uid, TradeVO tradeVO, String sessionKey);

	/**
	 * 根据手机号匹配查询新创建的订单 @Title: listNewTradeByPhones @param @return 设定文件 @return
	 * List<TradeDTO> 返回类型 @throws
	 */
	public List<TradeDTO> listNewTradeByPhones(Long uid, TradeVO tradeVO, String sessionKey);

	/**
	 * @Description 客户管理查询出该会员的相关订单信息
	 * @param uid
	 * @param map
	 * @return SimplePage 返回类型
	 * @author jackstraw_yu
	 * @date 2018年3月1日 下午1:44:58
	 */
	public SimplePage queryMemberTradePage(Long uid, Map<String, Object> map);

	/**
	 * 通过订单查询出tradeDto是否存在
	 * 
	 * @author HL
	 * @time 2018年6月1日 下午1:50:30
	 * @param userId
	 * @param newTid
	 * @return
	 */
	public Integer findOneTradeDTOByTid(Long uid, String tid);

	/**
	 * listTradeByTids(通过订单号匹配订单) @Title: listTradeByTids @param @param
	 * uid @param @param tidList @param @return 设定文件 @return List
	 * <TradeDTO> 返回类型 @throws
	 */
	public List<TradeDTO> listTradeByTids(Long uid, List<String> tidList);

	/**
	 * exportTradeDTO(下载订单数据) @Title: exportTradeDTO @param @param
	 * uid @param @param queryKey @param @return 设定文件 @return List
	 * <TradeResultVO> 返回类型 @throws
	 */
	public List<TradeResultVO> exportTradeDTO(Long uid, String queryKey, String sessionKey);

	/**
	 * listTradeByPhone(根据手机号查询订单，无序加解密) @Title: listTradeByPhone @param @return
	 * 设定文件 @return List<TradeDTO> 返回类型 @throws
	 */
	List<TradeDTO> listTradeByPhone(Long uid, List<String> phones, Date bTime, Date eTime);

	public TradeDTO findTradeDTOByTid(Long uid, Long tid);

	void doSendSms(Long uid, TradeMessageVO messageVO);

	public void updateTableIndex(Long uid);

	public List<Long> getHasTradeButNotFoundOrderList(Long uid) throws Exception;

	public List<Long> getHasTradeButNotFoundMemberList(Long uid) throws Exception;

	public List<TradeDTO> getDirtyData(Long uid, String sellerNick) throws Exception;

	public void deleteDirtyDataBySellerNick(Long uid, String sellerNick) throws Exception;

	public List<Long> getTidWhetherTradeNumIsNullOrEqualsZero(Long uid) throws Exception;

	public List<TempEntity> getAddNumberFromTradeTable(Long uid, Set<String> buyerNickList) throws Exception;

	public List<TempEntity> getBuyNumberFromTradeTable(Long uid, Set<String> buyerNickList) throws Exception;

	public List<TempEntity> getFirstPayTimeFromTradeTable(Long uid, Set<String> buyerNickList) throws Exception;

	Long countMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey);

	List<TradeResultVO> listMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey);

	Map<String, Object> listRFMDetailChart(Long uid, String dateType, Integer days, Date bTime, Date eTime)
			throws Exception;

	Long countMemberAmountByTimes(Long uid, Integer buyTimes, Date bTime, Date eTime) throws Exception;

	Double sumPaidAmountByTimes(Long uid, Integer buyTimes, Date bTime, Date eTime) throws Exception;

	List<TradeDTO> listEffectRFMByTime(Long uid, Date bTime, Date eTime) throws Exception;

	List<EffectStandardRFM> listCustomerRFMs(Long uid, Date startNodeDate) throws Exception;

	Long countUnPaidMember(Long uid);

	List<TradeDTO> listStepTradeByTids(Long uid, List<String> tids, Date bTime, Date eTime);

	/**
	 * @throws Exception
	 *             sumPaymentByTids(根据订单号查询对应订单总金额) @Title:
	 *             sumPaymentByTids @param @param uid @param @param
	 *             msgSendRecord @param @return 设定文件 @return Double 返回类型 @throws
	 */
	Map<String, Object> sumPaymentByTids(Long uid, MsgSendRecord msgSendRecord) throws Exception;

	public List<TradeTempEntity> getTradeNumByBuyerNick(Long uid, Set<String> buyerNickSet) throws Exception;

	public List<TradeTempEntity> getCloseTradeNum(Long uid, Set<String> buyerNickSet) throws Exception;

	public List<TradeTempEntity> getAddNumber(Long uid, Set<String> buyerNickSet) throws Exception;

	public List<TradeTempEntity> getbuyerAlipayNo(Long uid, Set<String> buyerNickSet) throws Exception;

	public List<TradeTempEntity> getReceiverInfoStr(Long uid, Set<String> buyerNickSet) throws Exception;

	/**
	 * 定时计算RFM图表数据（从会员表和订单表查询出需要的数据）
	 * 
	 * @param uid
	 * @param memberType
	 * @param dateType
	 * @param dateNum
	 * @param bTime
	 * @param eTime
	 * @return
	 */
	RFMDetailChart listRFMDetailChartData(Long uid, Integer memberType, String dateType, Integer dateNum, Date bTime,
			Date eTime);

	public List<RFMDetailChart> listRFMDetailChart(Long uid);

	public Long getCount(Long uid) throws Exception;

	public void truncateTable(Long uid) throws Exception;
}
