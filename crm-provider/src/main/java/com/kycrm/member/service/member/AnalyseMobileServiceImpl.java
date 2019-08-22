package com.kycrm.member.service.member;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.member.IAnalyseMobileDao;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.util.EncrptAndDecryptClient;

@MyDataSource
@Service("analyseMobileService")
public class AnalyseMobileServiceImpl implements IAnalyseMobileService {

	@Autowired
	private IAnalyseMobileDao analyseMobileDao;

	@Override
	public List<MemberInfoDTO> getMemberMobileByRange(Long uid, String accessToken, Date createdDate, int startPoint,
			int endPoint) throws Exception {
		List<MemberInfoDTO> memberInfoList = this.analyseMobileDao.getMemberMobileByRange(uid, createdDate, startPoint,
				endPoint);
		return this.decryptMemberInfo(memberInfoList, accessToken);
	}

	@Override
	public void updateAnalyseResult(Long uid, List<MemberInfoDTO> memberInfoList) throws Exception {
		this.analyseMobileDao.updateAnalyseResult(uid, memberInfoList);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 解密数据
	 * @Date 2018年8月9日下午3:35:07
	 * @param memberInfoList
	 * @param accessToken
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberInfoDTO>
	 */
	private List<MemberInfoDTO> decryptMemberInfo(List<MemberInfoDTO> memberInfoList, String accessToken)
			throws Exception {
		MemberInfoDTO memberInfoDTO = null;
		boolean isFailed = false;
		for (int i = 0; i < memberInfoList.size(); i++) {
			memberInfoDTO = memberInfoList.get(i);
			try {
				// 手机号
				if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getMobile(), EncrptAndDecryptClient.PHONE)) {
					memberInfoDTO.setMobile(EncrptAndDecryptClient.getInstance().decrypt(memberInfoDTO.getMobile(),
							EncrptAndDecryptClient.PHONE, accessToken));
				}
			} catch (Exception e) {
				e.printStackTrace();
				isFailed = true;
				break;
			}
		}
		if (isFailed) {
			return null;
		} else {
			return memberInfoList;
		}
	}

}
