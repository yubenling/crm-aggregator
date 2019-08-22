package com.kycrm.member.service.effect;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.effect.ItemTempTrade;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.vo.effect.CustomerDetailVO;

public interface IItemDetailService {

	/**
	 * limitItemDetail(效果分析商品详情的分页统计) @Title: limitItemDetail @param @param
	 * uid @param @param itemVO @param @return 设定文件 @return Map<String,Object>
	 * 返回类型 @throws
	 */
	Map<String, Object> limitItemDetail(Long uid, CustomerDetailVO itemVO);

	/**
	 * @throws Exception saveItemDetail(添加到itemTempTrade) @Title:
	 * saveItemDetail @param @param uid @param @param msgId @param @param
	 * tradeDTOs 设定文件 @return void 返回类型 @throws
	 */
	void saveItemDetail(Long uid, MsgSendRecord msg, List<TradeDTO> tradeDTOs) throws Exception;

	/**
	 * deleteItemTempTrade(删除15天之前的临时商品订单表数据) @Title:
	 * deleteItemTempTrade @param @param uid @param @param fifteenAgoDate
	 * 设定文件 @return void 返回类型 @throws
	 */
	void deleteItemTempTrade(Long uid, Date fifteenAgoDate);

	/**
	 * doCreateTable(是否存在该用户的表，不存在则创建) @Title: doCreateTable @param @param
	 * uid @param @return 设定文件 @return Boolean 返回类型 @throws
	 */
	Boolean doCreateTable(Long uid);

	/**
	 * doCreateItemHistory(创建itemTempTradeHistory) @Title:
	 * doCreateItemHistory @param @param uid @param @return 设定文件 @return Boolean
	 * 返回类型 @throws
	 */
	public Boolean doCreateItemHistory(Long uid);

	List<ItemTempTrade> listItemDetail(Long uid, CustomerDetailVO itemVO, Boolean isHistory);

	/**
	 * deleteDataByMsgId(根据msgId删除对应记录) @Title: deleteDataByMsgId @param @param
	 * uid @param @param msgId 设定文件 @return void 返回类型 @throws
	 */
	void deleteDataByMsgId(Long uid, Long msgId);

	void saveStepItemDetail(Long uid, MsgSendRecord msg, List<TradeDTO> tradeDTOs) throws Exception;

	Map<String, Object> limitStepItemDetail(Long uid, CustomerDetailVO itemVO);
}
