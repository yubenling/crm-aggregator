package com.kycrm.member.service.marketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.marketing.IPremiumMarketingMemberFilterDao;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterVO;
import com.kycrm.member.service.item.IItemService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.util.CalculateFilterConditionUtil;
import com.kycrm.member.util.CalculatePremiumFilterConditionUtil;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.RedisConstant;

/**
 * 高级会员筛选
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日上午11:33:46
 * @Tags
 */
@MyDataSource
@Service("premiumMarketingMemberFilterService")
public class PremiumMarketingMemberFilterServiceImpl implements IPremiumMarketingMemberFilterService {

	private static final Log logger = LogFactory.getLog(PremiumMarketingMemberFilterServiceImpl.class);

	@Autowired
	private IPremiumMarketingMemberFilterDao premiumMarketingMemberFilterDao;
	
	@Autowired
	private IItemService itemService;

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private IUserInfoService userInfoService;

	// accessToken是否失效
	private boolean illegalAccessToken;

	// 若缓存的accessToken失效，则会去数据库查询最新的accessToken，并更新缓存
	private String dbAccessToken = "";

	/**
	 * 根据筛选条件获取会员信息
	 */
	@Override
	public Map<String, Object> findMembersByCondition(Long uid, String taobaoUserNick, String accessToken,
			PremiumMemberFilterVO premiumMemberFilterVO, MemberFilterVO memberFilterVO, byte[] compress)
			throws Exception {
		illegalAccessToken = false;
		// 组装和计算高级会员筛选条件
		Map<String, Object> premiumParamMap = CalculatePremiumFilterConditionUtil
				.assembleMemberFilterCondition(premiumMemberFilterVO);
		boolean isNullForMemberFilterVO = true;
		if (memberFilterVO != null) {
			// 组装和计算基础会员筛选条件
			Map<String, Object> baseParamMap = CalculateFilterConditionUtil
					.assembleMemberFilterCondition(memberFilterVO);
			premiumParamMap.putAll(baseParamMap);
			isNullForMemberFilterVO = false;
		}
		if (compress != null && compress.length > 0) {
			byte[] uncompress = GzipUtil.uncompress(compress);
			List<Long> groupedNumIidList = JsonUtil.readValuesAsArrayList(new String(uncompress), Long.class);
			if (groupedNumIidList != null && groupedNumIidList.size() > 0) {
				premiumParamMap.put("specifyGoodsNumIidsArray", groupedNumIidList.toArray(new Long[] {}));
			}
		}
		// 判断是否需要订单表进行关联查询
		boolean needSelectOrderDTO = this.isNeedToJoinForPremiumFilter(premiumParamMap, isNullForMemberFilterVO);
		List<MemberInfoDTO> memberInfoList = null;
		// 根据筛选条件选择对应SQL查询数据库
		if (needSelectOrderDTO) {
			// 关联订单表查询会员信息
			memberInfoList = this.premiumMarketingMemberFilterDao.findFromMemberInfoDTOJoinOrderDTO(premiumParamMap);
		} else {
			// 根据筛选条件只需要在会员表中查询会员信息
			memberInfoList = this.premiumMarketingMemberFilterDao.findFromMemberInfoDTO(premiumParamMap);
		}
		memberInfoList = this.decryptMemberInfoList(memberInfoList, taobaoUserNick, accessToken);
		Long memberCount = this.findMembersCountByCondition(uid, premiumMemberFilterVO, memberFilterVO, compress);
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		resultMap.put("memberInfoList", memberInfoList);
		resultMap.put("memberCount", memberCount);
		return resultMap;
	}

	/**
	 * 根据筛选条件获取会员数量
	 */
	@Override
	public Long findMembersCountByCondition(Long uid, PremiumMemberFilterVO premiumMemberFilterVO,
			MemberFilterVO memberFilterVO, byte[] compress) throws Exception {
		// 组装和计算高级会员筛选条件
		Map<String, Object> premiumParamMap = CalculatePremiumFilterConditionUtil
				.assembleMemberFilterCondition(premiumMemberFilterVO);
		boolean isNullForMemberFilterVO = true;
		if (memberFilterVO != null) {
			// 组装和计算基础会员筛选条件
			Map<String, Object> baseParamMap = CalculateFilterConditionUtil
					.assembleMemberFilterCondition(memberFilterVO);
			premiumParamMap.putAll(baseParamMap);
			isNullForMemberFilterVO = false;
		}
		if (compress != null && compress.length > 0) {
			byte[] uncompress = GzipUtil.uncompress(compress);
			List<Long> groupedNumIidList = JsonUtil.readValuesAsArrayList(new String(uncompress), Long.class);
			if (groupedNumIidList != null && groupedNumIidList.size() > 0) {
				premiumParamMap.put("specifyGoodsNumIidsArray", groupedNumIidList.toArray(new Long[] {}));
			}
		}
		// 判断是否需要订单表进行关联查询
		boolean needSelectOrderDTO = this.isNeedToJoinForPremiumFilter(premiumParamMap, isNullForMemberFilterVO);
		// 根据筛选条件选择对应SQL查询数据库
		if (needSelectOrderDTO) {
			// 关联订单表查询会员数量
			return this.premiumMarketingMemberFilterDao.findCountFromMemberInfoDTOJoinOrderDTO(premiumParamMap);
		} else {
			// 根据筛选条件只需要在会员表中查询会员数量
			return this.premiumMarketingMemberFilterDao.findCountFromMemberInfoDTO(premiumParamMap);
		}
	}

	/**
	 * 高级会员筛选【用途【下载数据】和【发短信】】
	 */
	@Override
	public Map<String, Object> findMembersByCondition(Long uid, String taoBaoUserNick, String accessToken,
			PremiumMemberFilterVO premiumMemberFilterVO, MemberFilterVO memberFilterVO, byte[] compress, int startRows,
			int currentRows, boolean isDownload, boolean isSendMessage) throws Exception {
		illegalAccessToken = false;
		// 组装和计算会员筛选条件
		Map<String, Object> premiumParamMap = CalculatePremiumFilterConditionUtil
				.assembleMemberFilterCondition(premiumMemberFilterVO);
		if (isDownload) {
			// 计算出总页数
			premiumParamMap.put("startRows", (startRows - 1) * currentRows);
			premiumParamMap.put("currentRows", currentRows);
		}
		if (isSendMessage) {
			premiumParamMap.put("startRows", startRows);
			premiumParamMap.put("currentRows", currentRows);
		}
		boolean isNullForMemberFilterVO = true;
		if (memberFilterVO != null) {
			// 组装和计算基础会员筛选条件
			Map<String, Object> baseParamMap = CalculateFilterConditionUtil
					.assembleMemberFilterCondition(memberFilterVO);
			premiumParamMap.putAll(baseParamMap);
			if (memberFilterVO.getGoodsKeyCode() != null && !"".equals(memberFilterVO.getGoodsKeyCode())) {
				List<Long> numIids = itemService.fuzzilyFindNumIidByGoodsKeyCode(uid, memberFilterVO.getGoodsKeyCode());
				if (numIids != null && numIids.size() > 0) {
					premiumParamMap.put("specifyGoodsOrKeyCodeGoods", 1);
					premiumParamMap.put("numIidArray", numIids.toArray(new Long[] {}));
				}
			}
			isNullForMemberFilterVO = false;
		}
		if (compress != null && compress.length > 0) {
			byte[] uncompress = GzipUtil.uncompress(compress);
			List<Long> groupedNumIidList = JsonUtil.readValuesAsArrayList(new String(uncompress), Long.class);
			if (groupedNumIidList != null && groupedNumIidList.size() > 0) {
				premiumParamMap.put("specifyGoodsNumIidsArray", groupedNumIidList.toArray(new Long[] {}));
			}
		}
		// 判断是否需要订单表进行关联查询
		boolean needSelectOrderDTO = this.isNeedToJoinForPremiumFilter(premiumParamMap, isNullForMemberFilterVO);
		List<MemberInfoDTO> memberInfoList = null;
		// 根据筛选条件选择对应SQL查询数据库
		if (needSelectOrderDTO) {
			// 关联订单表查询会员信息
			memberInfoList = this.premiumMarketingMemberFilterDao.findFromMemberInfoDTOJoinOrderDTO(premiumParamMap);
		} else {
			// 根据筛选条件只需要在会员表中查询会员信息
			memberInfoList = this.premiumMarketingMemberFilterDao.findFromMemberInfoDTO(premiumParamMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		List<MemberInfoDTO> memberInfoTop100List = null;
		MemberInfoDTO memberInfoDTO = null;
		if (isDownload) {
			memberInfoList = this.decryptMemberInfoList(memberInfoList, taoBaoUserNick, accessToken);
			resultMap.put("memberInfoList", memberInfoList);
			return resultMap;
		} else {
			if (isSendMessage) {
				memberInfoList = this.decryptMemberInfoList(memberInfoList, taoBaoUserNick, accessToken);
				resultMap.put("memberInfoList", memberInfoList);
			} else {
				memberInfoTop100List = new ArrayList<MemberInfoDTO>(100);
				// 页面仅展示100条数据
				if (memberInfoList.size() > 100) {
					for (int i = 0; i < 100; i++) {
						memberInfoDTO = memberInfoList.get(i);
						memberInfoTop100List.add(memberInfoDTO);
					}
					memberInfoTop100List = this.decryptMemberInfoList(memberInfoTop100List, taoBaoUserNick,
							accessToken);
					resultMap.put("memberInfoList", memberInfoTop100List);
				} else {
					memberInfoList = this.decryptMemberInfoList(memberInfoList, taoBaoUserNick, accessToken);
					resultMap.put("memberInfoList", memberInfoList);
				}
			}
			resultMap.put("memberCount", memberInfoList.size());
			return resultMap;
		}
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
	private boolean isNeedToJoinForPremiumFilter(Map<String, Object> paramMap, boolean isNullForMemberFilterVO)
			throws Exception {
		// 是否需要查询子订单表的条件
		Boolean needSelectOrderDTO = false;
		if (paramMap.containsKey("payGoodsNameList")) {// 付款商品名称
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("payGoodsInPeriodTimeList")) {// 时段内购买某商品
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("goodsPriceList")) {// 商品价格
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("orderPriceList")) {// 订单金额
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("orderStatusInPeriodTimeList")) {// 时段内订单状态
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("orderSentDateList")) {// 订单发货时间
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("orderFromForSQLList")) {// 订单来源
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("sellerFlagList")) {// 卖家备注
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("buyerRemarkList")) {// 买家评价
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("buyGoodsInFirstTimeOfPeriodTimeList")) {// 时段内首次购买商品
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("buyGoodsInLastTimeOfPeriodTimeList")) {// 时段内最后一次购买商品
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("bookDateList")) {// 拍下时间
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("payDateList")) {// 付款时间
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("bookTimeList")) {// 拍下时段
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("payTimeList")) {// 付款时段
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("bookPriceList")) {// 拍下金额
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("payPriceList")) {// 付款金额
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("payInDayOfWeekList")) {// 星期几付款
			needSelectOrderDTO = true;
		}
		if (paramMap.containsKey("specifyGoodsNumIidsArray")) {// 商品分组
			needSelectOrderDTO = true;
		}
		if (!isNullForMemberFilterVO) {
			needSelectOrderDTO = this.isNeedToJoinForBaseFilter(paramMap);
		}
		return needSelectOrderDTO;
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
	private boolean isNeedToJoinForBaseFilter(Map<String, Object> paramMap) throws Exception {
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
			// 买家昵称
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
				memberInfoDTO.setBuyerNick(EncrptAndDecryptClient.getInstance().decrypt(memberInfoDTO.getBuyerNick(),
						EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 收货人姓名
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getReceiverName(), EncrptAndDecryptClient.SEARCH)) {
				memberInfoDTO.setReceiverName(EncrptAndDecryptClient.getInstance()
						.decrypt(memberInfoDTO.getReceiverName(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 手机号
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getMobile(), EncrptAndDecryptClient.PHONE)) {
				memberInfoDTO.setMobile(EncrptAndDecryptClient.getInstance().decrypt(memberInfoDTO.getMobile(),
						EncrptAndDecryptClient.PHONE, accessToken));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("店铺名称 = " + taoBaoUserNick + " 的sessionkey失效");
			illegalAccessToken = true;
			accessToken = getSessionkey(taoBaoUserNick, false);
			dbAccessToken = accessToken;
			// 买家昵称
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
				memberInfoDTO.setBuyerNick(EncrptAndDecryptClient.getInstance().decrypt(memberInfoDTO.getBuyerNick(),
						EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 收货人姓名
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getReceiverName(), EncrptAndDecryptClient.SEARCH)) {
				memberInfoDTO.setReceiverName(EncrptAndDecryptClient.getInstance()
						.decrypt(memberInfoDTO.getReceiverName(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 手机号
			if (EncrptAndDecryptClient.isEncryptData(memberInfoDTO.getMobile(), EncrptAndDecryptClient.PHONE)) {
				memberInfoDTO.setMobile(EncrptAndDecryptClient.getInstance().decrypt(memberInfoDTO.getMobile(),
						EncrptAndDecryptClient.PHONE, accessToken));
			}
		}
		return memberInfoDTO;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取sessionkey
	 * @Date 2018年10月29日下午3:18:28
	 * @param userNickName
	 * @param flag
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
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
