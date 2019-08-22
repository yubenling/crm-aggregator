package com.kycrm.transferdata.thread;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.member.IAnalyseMobileService;
import com.kycrm.util.MobileRegEx;

public class ProcessMobileThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessMobileThread.class);

	private UserInfo user;

	private Date createdDate;

	private IAnalyseMobileService analyseMobileService;

	private Map<String, String[]> dnsegAddressMap;

	private Map<String, String> dnsegOperatorMap;

	private int startPosition;

	private int limit;

	public ProcessMobileThread(UserInfo user, IAnalyseMobileService analyseMobileService,
			Map<String, String[]> dnsegAddressMap, Map<String, String> dnsegOperatorMap, Date createdDate,
			int startPosition, int limit) {
		super();
		this.user = user;
		this.analyseMobileService = analyseMobileService;
		this.dnsegAddressMap = dnsegAddressMap;
		this.dnsegOperatorMap = dnsegOperatorMap;
		this.createdDate = createdDate;
		this.startPosition = startPosition;
		this.limit = limit;
	}

	@Override
	public void run() {
		try {
			Long uid = user.getId();
			String accessToken = user.getAccessToken();
			List<MemberInfoDTO> memberInfoList = this.analyseMobileService.getMemberMobileByRange(uid, accessToken,
					createdDate, startPosition, limit);
			if (memberInfoList != null && memberInfoList.size() > 0) {
				MemberInfoDTO memberInfoDTO = null;
				for (int i = 0; i < memberInfoList.size(); i++) {
					memberInfoDTO = memberInfoList.get(i);
					String mobile = memberInfoDTO.getMobile();
					if (mobile != null && !"".equals(mobile)) {
						if (MobileRegEx.validateMobile(mobile)) {
							String dnsegThree = mobile.substring(0, 3);
							String dnsegSeven = mobile.substring(0, 7);
							// 号码前三位
							memberInfoDTO.setDnsegThree(dnsegThree);
							// 号码归属地
							if (dnsegAddressMap.containsKey(dnsegSeven)) {
								String[] provinceAndCity = dnsegAddressMap.get(dnsegSeven);
								// 省份
								memberInfoDTO.setDnsegProvince(provinceAndCity[0]);
								// 城市
								memberInfoDTO.setDnsegCity(provinceAndCity[1]);
							}
							if ("170".equals(dnsegThree) || "171".equals(dnsegThree)) {
								memberInfoDTO.setOperator("其他");
							} else {
								// 运营商
								if (dnsegOperatorMap.containsKey(dnsegThree)) {
									String operator = dnsegOperatorMap.get(dnsegThree);
									memberInfoDTO.setOperator(operator);
								}
							}
						}
					}
				}
				this.analyseMobileService.updateAnalyseResult(uid, memberInfoList);
				LOGGER.info("UID = " + uid + " 同步数据量 = " + memberInfoList.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
