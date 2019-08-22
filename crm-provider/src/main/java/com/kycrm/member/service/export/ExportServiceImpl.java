package com.kycrm.member.service.export;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.export.IExportDao;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.util.CalculateFilterConditionUtil;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.RedisConstant;

@MyDataSource
@Service("exportService")
public class ExportServiceImpl implements IExportService {

	@Autowired
	private IExportDao exportDao;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private ICacheService cacheService;

	// accessToken是否失效
	private boolean illegalAccessToken;

	// 若缓存的accessToken失效，则会去数据库查询最新的accessToken，并更新缓存
	private String dbAccessToken = "";

	@Override
	public Map<String, Object> findMembersByCondition(Long uid, String taobaoUserNick, String accessToken,
			MemberFilterVO memberFilterVO, Integer startRows, Integer currentRows, Long limitId) throws Exception {
		illegalAccessToken = false;
		// 组装和计算会员筛选条件
		Map<String, Object> paramMap = CalculateFilterConditionUtil.assembleMemberFilterCondition(memberFilterVO);
		// 计算出总页数
		paramMap.put("startRows", startRows);
		paramMap.put("currentRows", currentRows);
		paramMap.put("limitId", limitId);
		// 判断是否需要订单表进行关联查询
		boolean needSelectOrderDTO = this.isNeedToJoin(paramMap);
		List<MemberInfoDTO> memberInfoList = null;
		// 根据筛选条件选择对应SQL查询数据库
		if (needSelectOrderDTO) {
			// 关联订单表查询会员信息
			memberInfoList = this.exportDao.findMemberInfoJoinOrderDTO(paramMap);
		} else {
			// 根据筛选条件只需要在会员表中查询会员信息
			memberInfoList = this.exportDao.findMemberInfoDTO(paramMap);
		}
		Long lastQueryMinId = null;
		if (memberInfoList != null && memberInfoList.size() > 0) {
			lastQueryMinId = memberInfoList.get(memberInfoList.size() - 1).getId();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		memberInfoList = this.decryptMemberInfoList(memberInfoList, taobaoUserNick, accessToken);
		resultMap.put("memberInfoList", memberInfoList);
		resultMap.put("limitId", lastQueryMinId);
		return resultMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据查询条件调用指定SQL
	 * @Date 2018年7月18日下午12:16:58
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberInfoDTO>
	 */
	private boolean isNeedToJoin(Map<String, Object> paramMap) throws Exception {
		// 是否需要查询子订单表的条件
		Boolean needSelectOrderDTO = false;
		if (paramMap.containsKey("orderFrom")) {// 订单来源
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("numIidArray")) {// 指定商品
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("goodsKeyCode")) { // 商品关键字
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("orderStatus")) {// 订单状态
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("orderStatusArray")) {// 订单状态
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("sellerFlagArray")) {// 卖家标记
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("orderTimeSectionStart")) {// 拍下订单时段起始段
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("orderTimeSectionEnd")) {// 拍下订单时段截止段
			needSelectOrderDTO = true;
		}
		return needSelectOrderDTO;
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
	private List<MemberInfoDTO> decryptMemberInfoList(List<MemberInfoDTO> memberInfoList, String taoBaoUserNick,
			String accessToken) throws Exception {
		MemberInfoDTO memberInfoDTO = null;
		for (int i = 0; i < memberInfoList.size(); i++) {
			memberInfoDTO = memberInfoList.get(i);
			if (!illegalAccessToken) {
				this.decryptMemberInfo(memberInfoDTO, taoBaoUserNick, accessToken);
			} else {
				this.decryptMemberInfo(memberInfoDTO, taoBaoUserNick, dbAccessToken);
			}
			memberInfoList.set(i, memberInfoDTO);
		}
		return memberInfoList;
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
	private MemberInfoDTO decryptMemberInfo(MemberInfoDTO memberInfoDTO, String taoBaoUserNick, String accessToken)
			throws Exception {
		try {
			// 收货人姓名
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getReceiverName(), EncrptAndDecryptClient.SEARCH)) {
				memberInfoDTO.setReceiverName(EncrptAndDecryptClient.getInstance()
						.decrypt(memberInfoDTO.getReceiverName(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 买家昵称
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
				memberInfoDTO.setBuyerNick(EncrptAndDecryptClient.getInstance().decrypt(memberInfoDTO.getBuyerNick(),
						EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 手机号
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getMobile(), EncrptAndDecryptClient.PHONE)) {
				memberInfoDTO.setMobile(EncrptAndDecryptClient.getInstance().decrypt(memberInfoDTO.getMobile(),
						EncrptAndDecryptClient.PHONE, accessToken));
			}
		} catch (Exception e) {
			e.printStackTrace();
			illegalAccessToken = true;
			accessToken = getSessionkey(taoBaoUserNick, false);
			dbAccessToken = accessToken;
			// 收货人姓名
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getReceiverName(), EncrptAndDecryptClient.SEARCH)) {
				memberInfoDTO.setReceiverName(EncrptAndDecryptClient.getInstance()
						.decrypt(memberInfoDTO.getReceiverName(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 买家昵称
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
				memberInfoDTO.setBuyerNick(EncrptAndDecryptClient.getInstance().decrypt(memberInfoDTO.getBuyerNick(),
						EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 手机号
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getMobile(), EncrptAndDecryptClient.PHONE)) {
				memberInfoDTO.setMobile(EncrptAndDecryptClient.getInstance().decrypt(memberInfoDTO.getMobile(),
						EncrptAndDecryptClient.PHONE, accessToken));
			}
		}
		return memberInfoDTO;
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
