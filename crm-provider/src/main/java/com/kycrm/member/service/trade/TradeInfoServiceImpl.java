package com.kycrm.member.service.trade;

import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.to.Pageination;
import com.kycrm.member.domain.vo.order.OrdersVo;
import com.kycrm.util.EncrptAndDecryptClient;

@Service("tradeInfoService")
public class TradeInfoServiceImpl implements ITradeInfoService {

	@Override
	public Pageination<TradeDTO> findByTradeDTOList(String contextPath, Integer pageNo, String userId, OrdersVo oVo,
			String buyerNick, String sessionkey) {
		// 加密接口
		EncrptAndDecryptClient securityClient = EncrptAndDecryptClient.getInstance();
		// 先设置每页显示的条数为15条
		Integer currentRows = 15;
//		Query query = new Query();
//		// 封装params
//		StringBuilder params = this.tradeInfoParams(userNickName, ordersVoParam, buyerNick);
//		// 封装page
		Pageination<TradeDTO> page = new Pageination<TradeDTO>();
//		page.setPageNo(pageNo);
//		page.setPageSize(currentRows);
//		// 查询数据 buyerNick 是明文还是密文
//		Long count = this.quneryBuyerNickCount(userNickName, buyerNick);
//		if (count == 0) {
//			String encryptionBuyerNick = securityClient.encrypt(buyerNick, EncrptAndDecryptClient.SEARCH, session);
//			try {
//				query.addCriteria(Criteria.where("buyerNick").is(encryptionBuyerNick));
//			} catch (Exception e) {
//				query.addCriteria(Criteria.where("buyerNick").is(buyerNick));
//				loger.info("*********************用户" + userNickName + "的会员" + buyerNick
//						+ "解密出错，session可能过期或者为空！*****************");
//			}
//		} else {
//			query.addCriteria(Criteria.where("buyerNick").is(buyerNick));
//		}
//		query.with(new Sort(new Sort.Order(Direction.DESC, "createdUTC")));
//		packageCriteriaParam(query, ordersVoParam);
//		// 查询数据
//		if (null != userNickName && !"".equals(userNickName)) {
//			page = tradeRepository.findPage(page, query, userNickName);
//		}
//		// 封装分页数据
//		page.pageView(contextPath, params.toString());
		return page;
	}

}
