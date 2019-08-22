package com.kycrm.member.service.marketing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.marketing.IMarketingMemberFilterDao;
import com.kycrm.member.dao.order.IOrderDTODao;
import com.kycrm.member.dao.trade.ITradeDTODao;
import com.kycrm.member.domain.entity.member.MemberDetailDTO;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.order.MemberInformationDetailOrderInfoDTO;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.utils.pagination.Pagination;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.member.MemberInformationDetailUpdateVO;
import com.kycrm.member.domain.vo.member.MemberInformationDetailVO;
import com.kycrm.member.domain.vo.member.MemberInformationSearchVO;
import com.kycrm.member.domain.vo.member.MemberOrderVO;
import com.kycrm.member.domain.vo.member.MemberRemarkVO;
import com.kycrm.member.service.item.IItemService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.util.CalculateFilterConditionUtil;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.RedisConstant;

/**
 * 会员筛选服务
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月17日下午3:28:47
 * @Tags
 */
@MyDataSource
@Service("marketingMemberFilterService")
public class MarketingMemberFilterServiceImpl implements IMarketingMemberFilterService {

	private static final Log logger = LogFactory.getLog(MarketingMemberFilterServiceImpl.class);

	@Autowired
	private IMarketingMemberFilterDao marketingMemberFilterDao;

	@Autowired
	private IItemService itemService;

	@Autowired
	private ITradeDTODao tradeDao;

	@Autowired
	private IOrderDTODao orderDao;

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private IUserInfoService userInfoService;

	// accessToken是否失效
	private boolean illegalAccessToken;

	// 若缓存的accessToken失效，则会去数据库查询最新的accessToken，并更新缓存
	private String dbAccessToken = "";

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件查询会员信息
	 * @Date 2018年7月17日下午3:27:02
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String, Object>
	 */
	@Override
	public Map<String, Object> findMembersByCondition(Long uid, String taobaoUserNick, String accessToken,
			MemberFilterVO memberFilterVO) throws Exception {
		illegalAccessToken = false;
		// 组装和计算会员筛选条件
		Map<String, Object> paramMap = CalculateFilterConditionUtil.assembleMemberFilterCondition(memberFilterVO);
		paramMap.put("startRows", 0);
		paramMap.put("currentRows", 100);
		if (memberFilterVO.getGoodsKeyCode() != null && !"".equals(memberFilterVO.getGoodsKeyCode())) {
			List<Long> numIids = itemService.fuzzilyFindNumIidByGoodsKeyCode(uid, memberFilterVO.getGoodsKeyCode());
			if (numIids != null && numIids.size() > 0) {
				paramMap.put("specifyGoodsOrKeyCodeGoods", 1);
				paramMap.put("numIidArray", numIids.toArray(new Long[] {}));
			}
		}
		// 判断是否需要订单表进行关联查询
		boolean needSelectOrderDTO = this.isNeedToJoin(paramMap);
		List<MemberInfoDTO> memberInfoList = null;
		// 根据筛选条件选择对应SQL查询数据库
		if (needSelectOrderDTO) {
			// 关联订单表查询会员信息
			memberInfoList = this.marketingMemberFilterDao.findFromMemberInfoDTOJoinOrderDTO(paramMap);
		} else {
			// 根据筛选条件只需要在会员表中查询会员信息
			memberInfoList = this.marketingMemberFilterDao.findFromMemberInfoDTO(paramMap);
		}
		Long memberCount = this.findMemberCountByCondition(uid, memberFilterVO);
		memberInfoList = this.decryptMemberInfoList(memberInfoList, taobaoUserNick, accessToken);
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		resultMap.put("memberInfoList", memberInfoList);
		resultMap.put("memberCount", memberCount);
		return resultMap;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件查询会员信息
	 * @Date 2018年7月17日下午3:27:02
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String, Object>
	 */
	@Override
	public Map<String, Object> findMembersByCondition(Long uid, String taobaoUserNick, String accessToken,
			MemberFilterVO memberFilterVO, Integer startRows, Integer currentRows, Boolean isDownload,
			Boolean isSendMessage) throws Exception {
		illegalAccessToken = false;
		// 组装和计算会员筛选条件
		Map<String, Object> paramMap = CalculateFilterConditionUtil.assembleMemberFilterCondition(memberFilterVO);
		if (isDownload) {
			// 计算出总页数
			paramMap.put("startRows", (startRows - 1) * currentRows);
			paramMap.put("currentRows", currentRows);
		}
		if (isSendMessage) {
			paramMap.put("startRows", startRows);
			paramMap.put("currentRows", currentRows);
		}
		if (memberFilterVO.getGoodsKeyCode() != null && !"".equals(memberFilterVO.getGoodsKeyCode())) {
			List<Long> numIids = itemService.fuzzilyFindNumIidByGoodsKeyCode(uid, memberFilterVO.getGoodsKeyCode());
			if (numIids != null && numIids.size() > 0) {
				paramMap.put("specifyGoodsOrKeyCodeGoods", 1);
				paramMap.put("numIidArray", numIids.toArray(new Long[] {}));
			}
		}
		// 判断是否需要订单表进行关联查询
		boolean needSelectOrderDTO = this.isNeedToJoin(paramMap);
		List<MemberInfoDTO> memberInfoList = null;
		// 根据筛选条件选择对应SQL查询数据库
		if (needSelectOrderDTO) {
			// 关联订单表查询会员信息
			memberInfoList = this.marketingMemberFilterDao.findFromMemberInfoDTOJoinOrderDTO(paramMap);
		} else {
			// 根据筛选条件只需要在会员表中查询会员信息
			memberInfoList = this.marketingMemberFilterDao.findFromMemberInfoDTO(paramMap);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		List<MemberInfoDTO> memberInfoTop100List = null;
		MemberInfoDTO memberInfoDTO = null;
		if (isDownload) {
			memberInfoList = this.decryptMemberInfoList(memberInfoList, taobaoUserNick, accessToken);
			resultMap.put("memberInfoList", memberInfoList);
			return resultMap;
		} else {
			if (isSendMessage) {
				memberInfoList = this.decryptMemberInfoList(memberInfoList, taobaoUserNick, accessToken);
				resultMap.put("memberInfoList", memberInfoList);
			} else {
				memberInfoTop100List = new ArrayList<MemberInfoDTO>(100);
				// 页面仅展示100条数据
				if (memberInfoList.size() > 100) {
					for (int i = 0; i < 100; i++) {
						memberInfoDTO = memberInfoList.get(i);
						memberInfoTop100List.add(memberInfoDTO);
					}
					memberInfoTop100List = this.decryptMemberInfoList(memberInfoTop100List, taobaoUserNick,
							accessToken);
					resultMap.put("memberInfoList", memberInfoTop100List);
				} else {
					memberInfoList = this.decryptMemberInfoList(memberInfoList, taobaoUserNick, accessToken);
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
	 * @Description 根据筛选条件分页查询会员信息
	 * @Date 2018年7月21日下午3:20:47
	 * @param memberFilterVO
	 * @param uid
	 * @param contextPath
	 * @param pageNo
	 * @return
	 * @throws Exception
	 * @ReturnType Pagination
	 */
	@Override
	public Pagination findMembersByConditionAndPagination(Long uid, String taoBaoUserNick, String accessToken,
			MemberInformationSearchVO memberInfoSearchVO, String contextPath, Integer pageNo) throws Exception {
		illegalAccessToken = false;
		Integer currentRows = 10;
		// 判断是否需要订单表进行关联查询
		Pagination pagination = null;
		// 加密接口
		String buyerNick = memberInfoSearchVO.getBuyerNick();
		if (buyerNick != null && !"".equals(buyerNick)) {
			// 条件搜索 buyerNick 加密
			String encryptionBuyerNick = this.encryptSearchNick(buyerNick, taoBaoUserNick, accessToken);
			memberInfoSearchVO.setBuyerNick(encryptionBuyerNick);
			logger.info("UID = " + uid + " buyerNick加密后 = " + encryptionBuyerNick);
		}
		String mobile = memberInfoSearchVO.getMobile();
		if (mobile != null && !"".equals(mobile)) {
			// 条件搜索 mobile 加密
			String encryptionMobile = this.encryptSearchMobiles(mobile, taoBaoUserNick, accessToken);
			memberInfoSearchVO.setMobile(encryptionMobile);
			logger.info("UID = " + uid + " mobile加密后 = " + encryptionMobile);
		}
		// 根据筛选条件选择对应SQL查询数据库
		List<MemberInfoDTO> memberInfoList = this.findMembersByCondition(uid, taoBaoUserNick, accessToken,
				memberInfoSearchVO, null, pageNo);
		// 根据筛选条件统计总记录数
		Long memberCount = this.findMemberCountByCondition(uid, memberInfoSearchVO);
		// 解密用户昵称和手机号
		memberInfoList = this.decryptMemberInfoList(memberInfoList, taoBaoUserNick, accessToken);
		pagination = new Pagination(pageNo, currentRows, memberCount.intValue(), memberInfoList);
		// 拼接分页的后角标中的跳转路径与查询的条件
		String url = contextPath + "/memberInformation/findMemberInformation";
		pagination.pageView(url, this.appendMemberFilterParameter(memberInfoSearchVO));
		return pagination;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件分页查询会员信息
	 * @Date 2018年8月6日下午4:08:07
	 * @param uid
	 * @param accessToken
	 * @param memberFilterVO
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberInfoDTO>
	 */
	@Override
	public List<MemberInfoDTO> findMembersByCondition(Long uid, String taobaoUserNick, String accessToken,
			MemberInformationSearchVO memberInfoSearchVO, Integer pageSize, Integer pageNo) throws Exception {
		illegalAccessToken = false;
		// 组装和计算会员筛选条件
		Map<String, Object> paramMap = CalculateFilterConditionUtil
				.assembleMemberInformationCondition(memberInfoSearchVO);
		// 设置起始页
		if (pageNo == null) {
			pageNo = 1;
		}
		Integer currentRows = null;
		if (pageSize == null) {
			// 先设置每页显示的条数为10条
			currentRows = 10;
		}
		// 计算出起始行数
		Integer startRows = (pageNo - 1) * currentRows;
		// 计算出总页数
		paramMap.put("startRows", startRows);
		paramMap.put("currentRows", currentRows);
		// 根据筛选条件选择对应SQL查询数据库
		List<MemberInfoDTO> memberInfoList = this.marketingMemberFilterDao
				.findFromMemberInfoDTOForMemberInformation(paramMap);
		return this.decryptMemberInfoList(memberInfoList, taobaoUserNick, accessToken);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件查询会员数量
	 * @Date 2018年7月20日下午2:52:12
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Long
	 */
	@Override
	public Long findMemberCountByCondition(Long uid, MemberFilterVO memberFilterVO) throws Exception {
		Map<String, Object> paramMap = null;
		if (memberFilterVO instanceof MemberInformationSearchVO) {
			MemberInformationSearchVO memberInfoSearchVO = (MemberInformationSearchVO) memberFilterVO;
			// 组装和计算会员筛选条件
			paramMap = CalculateFilterConditionUtil.assembleMemberInformationCondition(memberInfoSearchVO);
		} else {
			// 组装和计算会员筛选条件
			paramMap = CalculateFilterConditionUtil.assembleMemberFilterCondition(memberFilterVO);
		}
		if (memberFilterVO.getGoodsKeyCode() != null && !"".equals(memberFilterVO.getGoodsKeyCode())) {
			List<Long> numIids = itemService.fuzzilyFindNumIidByGoodsKeyCode(uid, memberFilterVO.getGoodsKeyCode());
			if (numIids != null && numIids.size() > 0) {
				paramMap.put("specifyGoodsOrKeyCodeGoods", 1);
				paramMap.put("numIidArray", numIids.toArray(new Long[] {}));
			}
		}
		// 判断是否需要订单表进行关联查询
		boolean needSelectOrderDTO = this.isNeedToJoin(paramMap);
		// 根据筛选条件选择对应SQL查询数据库
		if (needSelectOrderDTO) {
			// 关联订单表查询会员信息
			return this.marketingMemberFilterDao.findCountFromMemberInfoDTOJoinOrderDTO(paramMap);
		} else {
			// 根据筛选条件只需要在会员表中查询会员信息
			return this.marketingMemberFilterDao.findCountFromMemberInfoDTO(paramMap);
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询客户信息详情
	 * @Date 2018年7月25日下午3:04:59
	 * @param memberInformationDetailVO
	 * @return
	 * @ReturnType MemberDetailDTO
	 */
	@Override
	public MemberDetailDTO findMemberInfoDetail(Long uid, String taobaoUserNick, String accessToken,
			MemberInformationDetailVO memberInformationDetailVO) throws Exception {
		// 组装和计算会员筛选条件
		Map<String, Object> paramMap = CalculateFilterConditionUtil
				.assembleMemberInformationDetail(memberInformationDetailVO);
		MemberDetailDTO memberDetailDTO = this.marketingMemberFilterDao.findMemberInfoDetail(paramMap);
		return this.decryptMemberDetail(memberDetailDTO, taobaoUserNick, accessToken);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 更新会员信息详情中的个人信息部分
	 * @Date 2018年7月25日下午3:47:58
	 * @param memberInformationDetailVO
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	@Override
	public Integer updateMemberInformationDetail(Long uid,
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO) throws Exception {
		// 组装和计算会员筛选条件
		Map<String, Object> paramMap = CalculateFilterConditionUtil
				.assembleUpdateMemberInformationDetail(memberInformationDetailUpdateVO);
		return this.marketingMemberFilterDao.updateMemberInformationDetail(paramMap);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 添加备注
	 * @Date 2018年7月25日下午4:45:23
	 * @param memberRemarkVO
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	@Override
	public Integer addMemberRemark(Long uid, MemberRemarkVO memberRemarkVO) throws Exception {
		return this.updateMemberRemark(uid, memberRemarkVO);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 修改备注
	 * @Date 2018年7月25日下午4:45:23
	 * @param memberRemarkVO
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	@Override
	public Integer updateMemberRemark(Long uid, MemberRemarkVO memberRemarkVO) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("uid", memberRemarkVO.getUid());
		paramMap.put("id", memberRemarkVO.getMemberId());
		paramMap.put("remark", memberRemarkVO.getRemark());
		paramMap.put("remarkStrTime", new Date());
		return this.marketingMemberFilterDao.updateMemberRemark(paramMap);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 删除备注
	 * @Date 2018年7月25日下午4:45:23
	 * @param memberRemarkVO
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	@Override
	public Integer deleteMemberRemark(Long uid, MemberRemarkVO memberRemarkVO) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("uid", memberRemarkVO.getUid());
		paramMap.put("id", memberRemarkVO.getMemberId());
		return this.marketingMemberFilterDao.deleteMemberRemark(paramMap);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单信息
	 * @Date 2018年7月25日下午5:32:16
	 * @param memberOrderVO
	 * @param uid
	 * @param contextPath
	 * @param pageNo
	 * @return
	 * @throws Exception
	 * @ReturnType Pagination
	 */
	@Override
	public Pagination findMemberOrderByConditionAndPagination(Long uid, MemberOrderVO memberOrderVO, String userId,
			String accessToken, String contextPath, Integer pageNo) throws Exception {
		memberOrderVO.setBuyerNick(this.encryptSearchNick(memberOrderVO.getBuyerNick(), userId, accessToken));
		Map<String, Object> paramMap = CalculateFilterConditionUtil.assembleMemberOrderCondition(memberOrderVO);
		paramMap.put("uid", uid);
		// 先设置每页显示的条数为10条
		Integer currentRows = 10;
		// 计算出起始行数
		Integer startRows = (pageNo - 1) * currentRows;
		// 计算出总页数
		paramMap.put("startRows", startRows);
		paramMap.put("currentRows", currentRows);
		// 根据筛选条件选择对应SQL查询数据库
		List<TradeDTO> memberTradeList = this.tradeDao.findMemberTradeByConditionAndPagination(paramMap);
		Integer count = this.tradeDao.findMemberTradeCount(paramMap);
		List<Long> tradeIdList = new ArrayList<Long>(currentRows);
		for (int i = 0; i < memberTradeList.size(); i++) {
			tradeIdList.add(memberTradeList.get(i).getTid());
		}
		List<MemberInformationDetailOrderInfoDTO> memberInfoDetailOrderList = new ArrayList<MemberInformationDetailOrderInfoDTO>();
		if (tradeIdList.size() > 0) {
			paramMap.put("tids", tradeIdList);
			List<OrderDTO> memberOrderList = this.orderDao.findMemberOrderByConditionAndPagination(paramMap);
			TradeDTO tradeDTO = null;
			OrderDTO orderDTO = null;
			List<OrderDTO> subList = null;
			MemberInformationDetailOrderInfoDTO memberInfoDetailOrderInfo = null;
			for (int i = 0; i < memberTradeList.size(); i++) {
				tradeDTO = memberTradeList.get(i);
				memberInfoDetailOrderInfo = new MemberInformationDetailOrderInfoDTO();
				// 订单号
				memberInfoDetailOrderInfo.setTid(tradeDTO.getTid().toString());
				// 交易时间
				memberInfoDetailOrderInfo.setTradeTime(tradeDTO.getCreated());
				// 订单状态
				memberInfoDetailOrderInfo.setOrderStatus(tradeDTO.getStatus());
				subList = memberInfoDetailOrderInfo.getOrderList();
				int time = 0;
				for (int j = 0; j < memberOrderList.size(); j++) {
					if (tradeDTO.getTid().equals(memberOrderList.get(j).getTid()) && time == 0) {
						orderDTO = memberOrderList.get(j);
						memberInfoDetailOrderInfo.setTitle(orderDTO.getTitle());
						memberInfoDetailOrderInfo.setTradePayment(orderDTO.getTradePayment());
						memberInfoDetailOrderInfo.setTradeNum(orderDTO.getNum());
						memberInfoDetailOrderInfo.setStatus(orderDTO.getStatus());
						memberInfoDetailOrderInfo.setSellerFlag(orderDTO.getSellerFlag());
						memberInfoDetailOrderInfo.setTradeRates(orderDTO.getTradeRates());
						time++;
						continue;
					}
					if (tradeDTO.getTid().equals(memberOrderList.get(j).getTid())) {
						subList.add(memberOrderList.get(j));
					}
				}
				memberInfoDetailOrderInfo.setOrderList(subList);
				memberInfoDetailOrderList.add(memberInfoDetailOrderInfo);
			}
		}
		Pagination pagination = new Pagination(pageNo, currentRows, count, memberInfoDetailOrderList);
		// 拼接分页的后角标中的跳转路径与查询的条件
		String url = contextPath + "/memberInformation/findMemberInfoOrderDetail";
		pagination.pageView(url, this.appendMemberOrderParameter(memberOrderVO));
		return pagination;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询购买一次的客户数量
	 * @Date 2018年8月15日下午4:30:27
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	@Override
	public Integer findBuyingOneTimeCustomer(Long uid) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("uid", uid);
		return this.marketingMemberFilterDao.findBuyingOneTimeCustomer(paramMap);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询购买两次及以上的客户数量
	 * @Date 2018年8月15日下午4:35:58
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	@Override
	public Integer findBuyingGreaterThanTwoTimes(Long uid) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("uid", uid);
		return this.marketingMemberFilterDao.findBuyingGreaterThanTwoTimes(paramMap);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询意向客户数量
	 * @Date 2018年8月15日下午4:42:02
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	@Override
	public Integer findIntentionCustomer(Long uid) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("uid", uid);
		return this.marketingMemberFilterDao.findIntentionCustomer(paramMap);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 近三个月未购买客户
	 * @Date 2018年8月15日下午4:50:25
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	@Override
	public Integer findLastThreeMonthsUntradedCustomer(Long uid) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("uid", uid);
		return this.marketingMemberFilterDao.findLastThreeMonthsUntradedCustomer(paramMap);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询全部客户数量
	 * @Date 2018年8月15日下午5:00:40
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	@Override
	public Integer findAllCustomer(Long uid) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("uid", uid);
		return this.marketingMemberFilterDao.findAllCustomer(paramMap);
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
	 * @Description 拼接请求入参
	 * @Date 2018年7月21日下午3:45:43
	 * @param memberInfoSearchVO
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	private String appendMemberFilterParameter(MemberInformationSearchVO memberInfoSearchVO) throws Exception {
		StringBuffer params = new StringBuffer();
		params.append("?uid=" + memberInfoSearchVO.getUid());
		// 用户昵称
		String buyerNick = memberInfoSearchVO.getBuyerNick();
		if (buyerNick != null && !"".equals(buyerNick)) {
			params.append("&buyerNick=" + buyerNick);
		}
		// 交易时间与未交易时间标识
		String tradeTimeOrUntradeTime = memberInfoSearchVO.getTradeOrUntradeTime();
		if (tradeTimeOrUntradeTime != null && !"".equals(tradeTimeOrUntradeTime)) {
			params.append("&tradeTimeOrUntradeTime=" + tradeTimeOrUntradeTime);
		}
		// 最后交易时间
		String tradeTime = memberInfoSearchVO.getTradeTime();
		if (tradeTime != null && !"".equals(tradeTime)) {
			params.append("&tradeTime=" + tradeTime);
		}
		// 最小最后交易时间
		String minTradeTime = memberInfoSearchVO.getMinTradeTime();
		if (minTradeTime != null && !"".equals(minTradeTime)) {
			params.append("&minTradeTime=" + minTradeTime);
		}
		// 最大最后交易时间
		String maxTradeTime = memberInfoSearchVO.getMaxTradeTime();
		if (maxTradeTime != null && !"".equals(maxTradeTime)) {
			params.append("&maxTradeTime=" + maxTradeTime);
		}
		// 手机号
		String mobile = memberInfoSearchVO.getMobile();
		if (mobile != null && !"".equals(mobile)) {
			params.append("&phone=" + mobile);
		}
		// 最小累计交易金额
		Double minTradeAmount = memberInfoSearchVO.getMinAccumulatedAmount();
		if (minTradeTime != null) {
			params.append("&minLastTradeAmount=" + minTradeAmount);
		}
		// 最大累计交易金额
		Double maxTradeAmount = memberInfoSearchVO.getMaxAccumulatedAmount();
		if (minTradeTime != null) {
			params.append("&maxTradeAmount=" + maxTradeAmount);
		}
		// 最小平均订单金额
		BigDecimal minAvgTradePrice = memberInfoSearchVO.getMinAveragePrice();
		if (minAvgTradePrice != null && !"".equals(minAvgTradePrice)) {
			params.append("&minAvgTradePrice=" + minAvgTradePrice);
		}
		// 最大平均订单金额
		BigDecimal maxAvgTradePrice = memberInfoSearchVO.getMaxAveragePrice();
		if (maxAvgTradePrice != null && !"".equals(maxAvgTradePrice)) {
			params.append("&maxAvgTradePrice=" + maxAvgTradePrice);
		}
		// 交易成功次数
		Long tradeNum = memberInfoSearchVO.getTradeNum();
		if (tradeNum != null) {
			params.append("&tradeNum=" + tradeNum);
		}
		// 最小交易成功次数
		Long minTradeNum = memberInfoSearchVO.getMinTradeNum();
		if (minTradeNum != null) {
			params.append("&minTradeNum=" + minTradeNum);
		}
		// 最大交易成功次数
		Long maxTradeNum = memberInfoSearchVO.getMaxTradeNum();
		if (tradeNum != null) {
			params.append("&maxTradeNum=" + maxTradeNum);
		}
		return params.toString();
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装订单信息参数
	 * @Date 2018年7月25日下午5:46:21
	 * @param memberOrderVO
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	private String appendMemberOrderParameter(MemberOrderVO memberOrderVO) throws Exception {
		StringBuffer params = new StringBuffer();
		params.append("?uid=" + memberOrderVO.getUid());
		params.append("&buyerNick=" + memberOrderVO.getBuyerNick());
		String minTradeTime = memberOrderVO.getMinTradeTime();
		if (minTradeTime != null && !"".equals(minTradeTime)) {
			params.append("&minTradeTime=" + minTradeTime);
		}
		String maxTradeTime = memberOrderVO.getMaxTradeTime();
		if (maxTradeTime != null && !"".equals(maxTradeTime)) {
			params.append("&maxTradeTime=" + maxTradeTime);
		}
		String orderStatus = memberOrderVO.getOrderStatus();
		if (orderStatus != null && !"".equals(orderStatus)) {
			params.append("&orderStatus=" + orderStatus);
		}
		return params.toString();
	}

	/**
	 * @Description:入参手机号长度大于4则直接加密 小于等于4模糊加密
	 * @author jackstraw_yu
	 */
	private String encryptSearchMobiles(String mobile, String userId, String accessToken) throws Exception {
		EncrptAndDecryptClient client = EncrptAndDecryptClient.getInstance();
		String encrypt = "";
		String key = "";
		if (mobile.length() != 4) {
			try {
				encrypt = client.encrypt(mobile, EncrptAndDecryptClient.PHONE, accessToken);
			} catch (Exception e) {
				logger.error("客户信息-手机号查询加密失败!");
				key = getSessionkey(userId, false);
				try {
					encrypt = client.encrypt(mobile, EncrptAndDecryptClient.PHONE, key);
				} catch (Exception e1) {
					logger.error("客户信息-手机号查询<>再次<>加密失败!");
				}
			}
		} else {
			try {
				encrypt = client.search(mobile, EncrptAndDecryptClient.PHONE, key).replace("$1$$", "").replace("$", "")
						.replace("=", "");
			} catch (Exception e) {
				logger.error("客户信息-手机号查询加密失败!");
				key = getSessionkey(userId, false);
				try {
					encrypt = client.search(mobile, EncrptAndDecryptClient.PHONE, key).replace("$1$$", "")
							.replace("$", "").replace("=", "");
				} catch (Exception e1) {
					logger.error("客户信息-手机号模糊查询<>再次<>加密失败!");
				}
			}
		}

		return encrypt;
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
			encrypt = client.encryptData(nickName, EncrptAndDecryptClient.SEARCH, accessToken);
		} catch (Exception e) {
			logger.error("客户信息-用户昵称模糊查询加密失败!");
			key = getSessionkey(userId, false);
			try {
				encrypt = client.search(nickName, EncrptAndDecryptClient.SEARCH, key);
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
			// OrderDTO orderDTO = memberInfoDTO.getOrderDTO();
			// // 收货地址
			// if (orderDTO != null) {
			// if
			// (EncrptAndDecryptClient.isEncryptData(orderDTO.getReceiverAddress(),
			// EncrptAndDecryptClient.SEARCH)) {
			// orderDTO.setReceiverAddress(EncrptAndDecryptClient.getInstance()
			// .decrypt(orderDTO.getReceiverAddress(),
			// EncrptAndDecryptClient.SEARCH, accessToken));
			// memberInfoDTO.setOrderDTO(orderDTO);
			// }
			// }
		} catch (Exception e) {
			e.printStackTrace();
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
			// OrderDTO orderDTO = memberInfoDTO.getOrderDTO();
			// // 收货地址
			// if (orderDTO != null) {
			// if
			// (EncrptAndDecryptClient.isEncryptData(orderDTO.getReceiverAddress(),
			// EncrptAndDecryptClient.SEARCH)) {
			// orderDTO.setReceiverAddress(EncrptAndDecryptClient.getInstance()
			// .decrypt(orderDTO.getReceiverAddress(),
			// EncrptAndDecryptClient.SEARCH, accessToken));
			// memberInfoDTO.setOrderDTO(orderDTO);
			// }
			// }
		}
		return memberInfoDTO;
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
	private MemberDetailDTO decryptMemberDetail(MemberDetailDTO memberDetailDTO, String taobaoUserNick,
			String accessToken) throws Exception {
		try {
			// 买家昵称
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
				memberDetailDTO.setBuyerNick(EncrptAndDecryptClient.getInstance()
						.decrypt(memberDetailDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 收货人姓名
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getReceiverName(),
					EncrptAndDecryptClient.SEARCH)) {
				memberDetailDTO.setReceiverName(EncrptAndDecryptClient.getInstance()
						.decrypt(memberDetailDTO.getReceiverName(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 手机
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getMobile(), EncrptAndDecryptClient.PHONE)) {
				memberDetailDTO.setMobile(EncrptAndDecryptClient.getInstance().decrypt(memberDetailDTO.getMobile(),
						EncrptAndDecryptClient.PHONE, accessToken));
			}
			// 邮箱
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getBuyerEmail(), EncrptAndDecryptClient.SEARCH)) {
				memberDetailDTO.setBuyerEmail(EncrptAndDecryptClient.getInstance()
						.decrypt(memberDetailDTO.getBuyerEmail(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 支付宝
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getBuyerAlipayNo(),
					EncrptAndDecryptClient.SEARCH)) {
				memberDetailDTO.setBuyerAlipayNo(EncrptAndDecryptClient.getInstance()
						.decrypt(memberDetailDTO.getBuyerAlipayNo(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 最近收货地址
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getReceiverInfoStr(),
					EncrptAndDecryptClient.SEARCH)) {
				memberDetailDTO.setReceiverInfoStr(EncrptAndDecryptClient.getInstance()
						.decrypt(memberDetailDTO.getReceiverInfoStr(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
		} catch (Exception e) {
			e.printStackTrace();
			accessToken = getSessionkey(taobaoUserNick, false);
			// 买家昵称
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
				memberDetailDTO.setBuyerNick(EncrptAndDecryptClient.getInstance()
						.decrypt(memberDetailDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 收货人姓名
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getReceiverName(),
					EncrptAndDecryptClient.SEARCH)) {
				memberDetailDTO.setReceiverName(EncrptAndDecryptClient.getInstance()
						.decrypt(memberDetailDTO.getReceiverName(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 手机
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getMobile(), EncrptAndDecryptClient.PHONE)) {
				memberDetailDTO.setMobile(EncrptAndDecryptClient.getInstance().decrypt(memberDetailDTO.getMobile(),
						EncrptAndDecryptClient.PHONE, accessToken));
			}
			// 邮箱
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getBuyerEmail(), EncrptAndDecryptClient.SEARCH)) {
				memberDetailDTO.setBuyerEmail(EncrptAndDecryptClient.getInstance()
						.decrypt(memberDetailDTO.getBuyerEmail(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 支付宝
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getBuyerAlipayNo(),
					EncrptAndDecryptClient.SEARCH)) {
				memberDetailDTO.setBuyerAlipayNo(EncrptAndDecryptClient.getInstance()
						.decrypt(memberDetailDTO.getBuyerAlipayNo(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			// 最近收货地址
			if (EncrptAndDecryptClient.isEncryptData(memberDetailDTO.getReceiverInfoStr(),
					EncrptAndDecryptClient.SEARCH)) {
				memberDetailDTO.setReceiverInfoStr(EncrptAndDecryptClient.getInstance()
						.decrypt(memberDetailDTO.getReceiverInfoStr(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
		}
		return memberDetailDTO;
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

	@Override
	public Long findMemberCountByCreatedDate(Long uid, Date createdDate) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("uid", uid);
		paramMap.put("createdDate", createdDate);
		return this.marketingMemberFilterDao.findMemberCountByCreatedDate(paramMap);
	}

}
