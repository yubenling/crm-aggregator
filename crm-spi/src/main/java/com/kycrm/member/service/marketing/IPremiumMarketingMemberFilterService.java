package com.kycrm.member.service.marketing;

import java.util.Map;

import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterVO;

/**
 * 高级会员筛选接口
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日上午11:32:59
 * @Tags
 */
public interface IPremiumMarketingMemberFilterService {

	public Map<String, Object> findMembersByCondition(Long uid, String taobaoUserNick, String accessToken,
			PremiumMemberFilterVO premiumMemberFilterVO, MemberFilterVO memberFilterVO, byte[] compress)
			throws Exception;

	public Long findMembersCountByCondition(Long uid, PremiumMemberFilterVO premiumMemberFilterVO,
			MemberFilterVO memberFilterVO, byte[] compress) throws Exception;

	public Map<String, Object> findMembersByCondition(Long uid, String taoBaoUserNick, String accessToken,
			PremiumMemberFilterVO premiumMemberFilterVO, MemberFilterVO memberFilterVO, byte[] compress,
			int currentPage, int pageSize, boolean isDownload, boolean isSendMessage) throws Exception;

}
