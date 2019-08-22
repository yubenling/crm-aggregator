package com.kycrm.transferdata.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.service.marketing.IMarketingMemberFilterService;
import com.kycrm.member.service.member.IAnalyseMobileService;
import com.kycrm.transferdata.util.PidThreadFactory;

public class ProcessMobileBaseThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessMobileBaseThread.class);

	private int i;

	private UserInfo user;

	private Date createdDate;

	// 解析手机号
	private IAnalyseMobileService analyseMobileService;

	private Map<String, String[]> dnsegAddressMap;

	private Map<String, String> dnsegOperatorMap;

	// 会员筛选
	private IMarketingMemberFilterService marketingMemberFilterService;

	private ExecutorService threadPool;

	private static final Integer BATCH_SIZE = 1000;

	private static final Long THREAD_BASE_SLEEP_TIME = 1000L;

	public ProcessMobileBaseThread(int i, UserInfo user, Date createdDate, IAnalyseMobileService analyseMobileService,
			Map<String, String[]> dnsegAddressMap, Map<String, String> dnsegOperatorMap,
			IMarketingMemberFilterService marketingMemberFilterService) {
		super();
		this.user = user;
		this.createdDate = createdDate;
		this.analyseMobileService = analyseMobileService;
		this.dnsegAddressMap = dnsegAddressMap;
		this.dnsegOperatorMap = dnsegOperatorMap;
		this.marketingMemberFilterService = marketingMemberFilterService;
	}

	@Override
	public void run() {
		try {
			Long uid = user.getId();
			MemberFilterVO memberFilterVO = new MemberFilterVO();
			memberFilterVO.setUid(uid);
			memberFilterVO.setTradeOrUntradeTime("1");
			memberFilterVO.setSendOrNotSendForArea(1);
			memberFilterVO.setSendOrNotSendForGoods(1);
			memberFilterVO.setSpecifyGoodsOrKeyCodeGoods("1");
			// 会员总数
			Long memberCount = this.marketingMemberFilterService.findMemberCountByCreatedDate(uid, createdDate);
			if (memberCount > 0) {
				int totalPageNo = 0;
				if (memberCount % BATCH_SIZE == 0) {
					totalPageNo = (int) (memberCount / BATCH_SIZE);
				} else {
					totalPageNo = (int) (memberCount / BATCH_SIZE) + 1;
				}
				LOGGER.info("集合名称 = crm_order_dto" + uid + " 总记录数 = " + memberCount + " 共分 " + totalPageNo
						+ " 批次迁移数据 每批次共 " + BATCH_SIZE + " 条数据");
				if (totalPageNo != 0) {
					// 创建线程池
					threadPool = Executors.newFixedThreadPool(2, new PidThreadFactory("ProcessMobileBaseThread", true));
					this.analyseMobile(i, uid, totalPageNo);
					threadPool.shutdown();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void analyseMobile(int i, Long uid, int totalPageNo) throws Exception {
		// 线程集合
		List<ProcessMobileThread> threadList = new ArrayList<ProcessMobileThread>(totalPageNo);
		for (i = 0; i < totalPageNo; i++) {
			// for (i = totalPageNo - 1; i >= 0; i--) {
			LOGGER.info("UID = " + uid + " startPosition = " + i * BATCH_SIZE + " limit = " + BATCH_SIZE);
			threadList.add(new ProcessMobileThread(user, analyseMobileService, dnsegAddressMap, dnsegOperatorMap,
					createdDate, i * BATCH_SIZE, BATCH_SIZE));
		}
		// 提交线程
		for (i = 0; i < totalPageNo; i++) {
			threadPool.execute(threadList.get(i));
			Thread.sleep(THREAD_BASE_SLEEP_TIME * 5);
		}
	}

}
