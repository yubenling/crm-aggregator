package com.kycrm.member.service.marketing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.marketing.IMemberReceiveDetailDao;
import com.kycrm.member.domain.entity.member.MemberReceiveDetail;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.member.IMemberReceiveDetailService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.RedisConstant;

@MyDataSource
@Service("memberReceiveDetailService")
public class MemberReceiveDetailServiceImpl implements IMemberReceiveDetailService {

	private static final Log logger = LogFactory.getLog(MemberReceiveDetailServiceImpl.class);

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private IMemberReceiveDetailDao memberReceiveDetailDao;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员详情 - 其他地址
	 * @Date 2018年7月28日上午11:37:41
	 * @param uid
	 * @param accessToken
	 * @param taoBaoUserNick
	 * @param buyerNick
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberReceiveDetail>
	 */
	@Override
	public List<MemberReceiveDetail> findMemberMultiAddress(Long uid, String accessToken, String taoBaoUserNick,
			String buyerNick) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uid", uid);
		if (buyerNick != null && !"".equals(buyerNick)) {
			// 条件搜索 buyerNick 加密
			String encryptionBuyerNick = this.encryptSearchNick(buyerNick, taoBaoUserNick, accessToken);
			paramMap.put("buyerNick", encryptionBuyerNick);
		}
		List<MemberReceiveDetail> memberReceiveDetailList = this.memberReceiveDetailDao
				.findMemberMultiAddress(paramMap);
		MemberReceiveDetail memberReceiveDetail = null;
		for (int i = 0; i < memberReceiveDetailList.size(); i++) {
			memberReceiveDetail = this.decryptMemberInfo(memberReceiveDetailList.get(i), taoBaoUserNick, accessToken);
			memberReceiveDetailList.set(i, memberReceiveDetail);
		}
		return this.decryptMemberInfoList(memberReceiveDetailList, taoBaoUserNick, accessToken);
	}

	/**
	 * 查询最后一次收货信息
	 */
	@Override
	public MemberReceiveDetail findLastDetail(Long uid) {
		if (uid == null) {
			return null;
		}
		MemberReceiveDetail receiverDetail = memberReceiveDetailDao.findLastDetail(uid);
		return receiverDetail;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 解密数据
	 * @Date 2018年8月9日下午3:35:07
	 * @param memberReceiveDetail
	 * @param accessToken
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberInfoDTO>
	 */
	private List<MemberReceiveDetail> decryptMemberInfoList(List<MemberReceiveDetail> memberReceiveDetailList,
			String taoBaoUserNick, String accessToken) throws Exception {
		MemberReceiveDetail memberReceiveDetail = null;
		for (int i = 0; i < memberReceiveDetailList.size(); i++) {
			memberReceiveDetail = memberReceiveDetailList.get(i);
			this.decryptMemberInfo(memberReceiveDetail, taoBaoUserNick, accessToken);
			memberReceiveDetailList.set(i, memberReceiveDetail);
		}
		return memberReceiveDetailList;
	}

	/**
	 * @Description:昵称加密
	 * @Copy_author jackstraw_yu
	 */
	private String encryptSearchNick(String nickName, String userId, String accessToken) throws Exception {
		EncrptAndDecryptClient client = EncrptAndDecryptClient.getInstance();
		String encrypt = "";
		String key = "";
		try {
			encrypt = client.encrypt(nickName, EncrptAndDecryptClient.SEARCH, accessToken);
		} catch (Exception e) {
			logger.error("客户信息-用户昵称模糊查询加密失败!");
			key = getSessionkey(userId, false);
			try {
				encrypt = client.encrypt(nickName, EncrptAndDecryptClient.SEARCH, key);
			} catch (Exception e1) {
				logger.error("客户信息-用户昵称模糊查询<>再次<>加密失败!");
			}
		}

		return encrypt;
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
	private MemberReceiveDetail decryptMemberInfo(MemberReceiveDetail memberReceiveDetail, String taoBaoUserNick,
			String accessToken) throws Exception {
		try {
			if (EncrptAndDecryptClient.isEncryptData(memberReceiveDetail.getBuyerNick(),
					EncrptAndDecryptClient.SEARCH)) {
				memberReceiveDetail.setBuyerNick(EncrptAndDecryptClient.getInstance()
						.decrypt(memberReceiveDetail.getBuyerNick(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			if (EncrptAndDecryptClient.isEncryptData(memberReceiveDetail.getReceiverName(),
					EncrptAndDecryptClient.SEARCH)) {
				memberReceiveDetail.setReceiverName(EncrptAndDecryptClient.getInstance()
						.decrypt(memberReceiveDetail.getReceiverName(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			if (EncrptAndDecryptClient.isEncryptData(memberReceiveDetail.getReceiverMobile(),
					EncrptAndDecryptClient.PHONE)) {
				memberReceiveDetail.setReceiverMobile(EncrptAndDecryptClient.getInstance()
						.decrypt(memberReceiveDetail.getReceiverMobile(), EncrptAndDecryptClient.PHONE, accessToken));
			}
			if (EncrptAndDecryptClient.isEncryptData(memberReceiveDetail.getReceiverAddress(),
					EncrptAndDecryptClient.SEARCH)) {
				memberReceiveDetail.setReceiverAddress(EncrptAndDecryptClient.getInstance()
						.decrypt(memberReceiveDetail.getReceiverAddress(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
		} catch (Exception e) {
			e.printStackTrace();
			accessToken = getSessionkey(taoBaoUserNick, false);
			if (EncrptAndDecryptClient.isEncryptData(memberReceiveDetail.getBuyerNick(),
					EncrptAndDecryptClient.SEARCH)) {
				memberReceiveDetail.setBuyerNick(EncrptAndDecryptClient.getInstance()
						.decrypt(memberReceiveDetail.getBuyerNick(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			if (EncrptAndDecryptClient.isEncryptData(memberReceiveDetail.getReceiverName(),
					EncrptAndDecryptClient.SEARCH)) {
				memberReceiveDetail.setReceiverName(EncrptAndDecryptClient.getInstance()
						.decrypt(memberReceiveDetail.getReceiverName(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			if (EncrptAndDecryptClient.isEncryptData(memberReceiveDetail.getReceiverMobile(),
					EncrptAndDecryptClient.PHONE)) {
				memberReceiveDetail.setReceiverMobile(EncrptAndDecryptClient.getInstance()
						.decrypt(memberReceiveDetail.getReceiverMobile(), EncrptAndDecryptClient.PHONE, accessToken));
			}
			if (EncrptAndDecryptClient.isEncryptData(memberReceiveDetail.getReceiverAddress(),
					EncrptAndDecryptClient.SEARCH)) {
				memberReceiveDetail.setReceiverAddress(EncrptAndDecryptClient.getInstance()
						.decrypt(memberReceiveDetail.getReceiverAddress(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
		}
		return memberReceiveDetail;
	}

	@SuppressWarnings("deprecation")
	private String getSessionkey(String userNickName, boolean flag) throws Exception {
		String token = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
				RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + userNickName);
		if (null != token && !"".equals(token) && flag) {
			return token;
		} else {
			UserInfo user = userInfoService.findUserInfo(userNickName);
			if (user != null)
				if (null != user.getAccessToken() && !"".equals(user.getAccessToken())) {
					cacheService.putNoTime(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
							RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + userNickName,
							user.getAccessToken(), true);
					return user.getAccessToken();
				}
		}
		return null;
	}
}
