package com.kycrm.member.handler;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kycrm.member.core.queue.MemberQueueService;
import com.kycrm.member.core.queue.TradeQueueService;
import com.kycrm.member.domain.entity.trade.TbTrade;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.service.synctrade.member.MemberDTOServiceImplsyn;
import com.kycrm.member.service.synctrade.order.OrderDTOServiceImplsyn;
import com.kycrm.member.service.synctrade.trade.TradeDTOServiceImplsyn;
import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;

@Service("synServiceProvider")
public class SynServiceProvider implements IMongoHistroyTradeService {

	private static final Logger logger = LoggerFactory.getLogger(SynServiceProvider.class);

	// @Autowired
	// private AsyncDataProcessor processor;

	@Autowired
	private TradeDTOServiceImplsyn tradeDTOService;

	@Autowired
	private OrderDTOServiceImplsyn orderDTOService;

	@Autowired
	private ITradeSysInfoServiceSyn tradeSysInfoService;

	@Autowired
	private MemberSyncArtifactService memberSyncArtifactService;
	
	@Autowired
	private MemberDTOServiceImplsyn memberDTOServiceImpl;
	

	@Override
	public void synTradeAndOrderMongoDate(Long uid, byte[] compress) throws Exception {
		try {
			byte[] uncompress = GzipUtil.uncompress(compress);
			List<TbTrade> tradeList = JsonUtil.readValuesAsArrayList(new String(uncompress), TbTrade.class);
			tradeSysInfoService.batchProcessTradeByRedisLock(uid, tradeList);
			// 放入订单内存队列
			// TradeQueueService.TRADE_QUEUE.put(tradeList);
			// 提交处理数据的任务
			// processor.submitTradeAndOrderTask(tradeList);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// logger.info("\n数据迁移或者历史订单导入出错" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("\n数据迁移或者历史订单导入出错" + e.getMessage());
		}
	}

	@Override
	public void synMemberAndReceiveDetailMongoDate(Long uid, byte[] compress) throws Exception {
		try {
			byte[] uncompress = GzipUtil.uncompress(compress);
			List<TbTrade> tradeList = JsonUtil.readValuesAsArrayList(new String(uncompress), TbTrade.class);
			memberSyncArtifactService.batchProcessMemberByRedisLock(uid, tradeList);
			// 放入会员处理内存队列
			// MemberQueueService.MENBER_QUEUE.put(tradeList);
			// 提交处理数据的任务
			// processor.submitMemberAndReceiveDetailTask(tradeList);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// logger.info("\n数据迁移或者历史会员导入出错" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("\n数据迁移或者历史会员导入出错" + e.getMessage());
		}
	}
    
	@Override
	public void synOrderHistory(List<TbTrade> tradeList) {
		// 采用异步线程
		/*MyFixedThreadPool.getTradeAndOrderFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {*/
				try {
					logger.info("本次历史订单导入大小为"+tradeList.size());
					StringBuilder sb=new StringBuilder();
					for(TbTrade td:tradeList){
						sb.append(td.getTid()+"-");
					}
					logger.info("历史订单导入的tid为"+sb);
					TradeQueueService.TRADE_QUEUE.put(tradeList);
					logger.info("放入订单，目前队列中的大小为"+TradeQueueService.TRADE_QUEUE.size());
					// 放入会员处理内存队列
					MemberQueueService.MENBER_QUEUE.put(tradeList);
					logger.info("放入会员，目前队列中的大小为"+MemberQueueService.MENBER_QUEUE.size());
				} catch (InterruptedException e) {
					e.printStackTrace();
					logger.info("\n数据迁移或者历史订单导入出错" + e.getMessage());
				}
		/*	}
		});*/
	}

	/**
	 * 根据用户UID获取每个用户缺失的TID 返回UID对应缺失的TID集合
	 */
	@Override
	public List<Long> getLostTidList(Long uid) throws Exception {
		List<Long> hasTradeButNotFoundOrderList = this.tradeDTOService.getHasTradeButNotFoundOrderList(uid);
		List<Long> hasTradeButNotFoundMemberList = this.tradeDTOService.getHasTradeButNotFoundMemberList(uid);
		List<Long> hasOrderButNotFoundTradeList = this.orderDTOService.getHasOrderButNotFoundTradeList(uid);
		List<Long> hasOrderButNotFoundMemberList = this.orderDTOService.getHasOrderButNotFoundMemberList(uid);
		int totalCount = hasTradeButNotFoundOrderList.size() + hasTradeButNotFoundMemberList.size()
				+ hasOrderButNotFoundTradeList.size() + hasOrderButNotFoundMemberList.size();
		List<Long> resultList = new ArrayList<Long>(totalCount);
		resultList.addAll(hasTradeButNotFoundOrderList);
		resultList.addAll(hasTradeButNotFoundMemberList);
		resultList.addAll(hasOrderButNotFoundTradeList);
		resultList.addAll(hasOrderButNotFoundMemberList);
		return resultList;
	}

	@Override
	public byte[] getDirtyData(Long uid, String sellerNick) throws Exception {
		List<TradeDTO> tradeDTOList = this.tradeDTOService.getDirtyData(uid, sellerNick);
		String json = JsonUtil.toJson(tradeDTOList);
		byte[] compress = GzipUtil.compress(json);
		return compress;
	}

	@Override
	public List<Long> getTidWhetherTradeNumIsNullOrEqualsZero(Long uid) throws Exception {
		return this.tradeDTOService.getTidWhetherTradeNumIsNullOrEqualsZero(uid);
	}

	@Override
	public byte[] getMultiOrderTidList(Long uid) throws Exception {
		List<Long> tidList = this.orderDTOService.getMultiOrderTidList(uid);
		String json = JsonUtil.toJson(tidList);
		byte[] compress = GzipUtil.compress(json);
		return compress;
	}
    
	public void updateLastMarketingTime(Long uid){
		memberDTOServiceImpl.updateLastMarketingTime(uid);
	}
}
