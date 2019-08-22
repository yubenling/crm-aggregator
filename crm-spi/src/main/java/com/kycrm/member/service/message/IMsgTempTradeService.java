package com.kycrm.member.service.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.trade.MsgTempTrade;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.effect.CustomerDetailVO;

public interface IMsgTempTradeService {

	/**
	 * listFifteenTrade(查询所有十五天之前的数据)
	 * @Title: listFifteenTrade 
	 * @param @return 设定文件 
	 * @return List<MsgTempTrade> 返回类型 
	 * @throws
	 */
	public List<MsgTempTrade> listFifteenTrade(Long uid, Date fifteenAgoTime,int startRows,int pageSize);

	/**
	 * limitCustomerDetail(分页计算客户详情)
	 * @Title: limitCustomerDetail 
	 * @param @param uid
	 * @param @param customerDetailVO
	 * @param @return 设定文件 
	 * @return Map<String,Object> 返回类型 
	 * @throws
	 */
	Map<String, Object> limitCustomerDetail(Long uid,
			CustomerDetailVO customerDetailVO, UserInfo userInfo);

	/**
	 * doCreateTable(此用户是否存在表，不存在则创建)
	 * @Title: doCreateTable 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return Boolean 返回类型 
	 * @throws
	 */
	Boolean doCreateTable(Long uid);

	List<MsgTempTrade> listTempTradeByMsgId(Long uid, Long msgId);

	void deleteDataByMsgId(Long uid, Long msgId);

	/**
	 * sumPaymentByDate(首页昨日营销回款金额)
	 * @Title: sumPaymentByDate 
	 * @param @param uid
	 * @param @param bTime
	 * @param @param eTime
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	Double sumPaymentByDate(Long uid, Date bTime, Date eTime);

	Map<String, Object> limitStepCustomerDetail(Long uid,
			CustomerDetailVO customerDetailVO, UserInfo userInfo);


}
