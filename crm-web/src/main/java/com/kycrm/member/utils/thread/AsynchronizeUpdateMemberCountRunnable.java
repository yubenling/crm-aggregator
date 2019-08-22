package com.kycrm.member.utils.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kycrm.member.domain.entity.usermanagement.SellerGroup;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.service.marketing.IMarketingMemberFilterService;
import com.kycrm.member.service.usermanagement.ISellerGroupService;

/**
 * 异步更新会员数量
 * 
 * @Author ZhengXiaoChen
 * @Date 2019年1月10日下午3:25:52
 * @Tags
 */
public class AsynchronizeUpdateMemberCountRunnable implements Runnable {

	private static final Log logger = LogFactory.getLog(AsynchronizeUpdateMemberCountRunnable.class);

	private Long uid;

	private Long groupId;

	private MemberFilterVO memberFilterVO;

	private ISellerGroupService sellerGroupService;

	private IMarketingMemberFilterService marketingMemberFilterService;

	public AsynchronizeUpdateMemberCountRunnable(Long uid, Long groupId, MemberFilterVO memberFilterVO,
			ISellerGroupService sellerGroupService, IMarketingMemberFilterService marketingMemberFilterService) {
		super();
		this.uid = uid;
		this.groupId = groupId;
		this.memberFilterVO = memberFilterVO;
		this.sellerGroupService = sellerGroupService;
		this.marketingMemberFilterService = marketingMemberFilterService;
	}

	@Override
	public void run() {
		try {
			Long memberCount = this.marketingMemberFilterService.findMemberCountByCondition(uid, memberFilterVO);
			SellerGroup sellerGroup = new SellerGroup();
			sellerGroup.setId(groupId);
			sellerGroup.setMemberCount(memberCount.toString());
			logger.info("UID = " + uid + " GroupId = " + groupId + "异步更新会员数量 = " + memberCount);
			this.sellerGroupService.updateSellerGroup(sellerGroup);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
