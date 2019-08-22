package com.kycrm.member.service.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.redis.RedisLockService;
import com.kycrm.member.dao.member.IMemberDTODao;
import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.to.SimplePage;
import com.kycrm.member.domain.vo.effect.EffectStandardRFMVO;
import com.kycrm.member.domain.vo.member.MemberCriteria;
import com.kycrm.member.domain.vo.member.MemberInfoVO;
import com.kycrm.member.domain.vo.member.MemberMsgCriteria;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.NumberUtils;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;
import com.kycrm.util.thread.MyFixedThreadPool;
import com.taobao.api.SecretException;

/**
 * @author wy
 * @version 创建时间：2018年1月18日 下午2:21:19
 */
@MyDataSource
@Service("memberDTOService")
public class MemberDTOServiceImpl implements IMemberDTOService {
	private static final Log logger = LogFactory.getLog(MemberDTOServiceImpl.class);

	@Autowired
	private IMemberDTODao memberDao;

	@Autowired
	private RedisLockService redisLockService;
	
	@Autowired
	private ITradeDTOService tradeDTOService;

	/**
	 * 根据用户主键id创建对应的会员表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:53:10
	 * @param uid
	 *            用户主键id
	 */
	@Override
	public void doCreateTableByNewUser(Long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			return;
		}
		List<String> tables = this.memberDao.isExistsTable(uid);
		if (ValidateUtil.isNotNull(tables)) {
			return;
		}
		try {
			this.memberDao.doCreateTableByNewUser(uid);
			// 添加索引
			this.memberDao.addMemberTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据用户主键id创建对应卖家的买家购买统计
	 * 
	 * @author sungk
	 * @param uid
	 */
	@Override
	public void doCreateMemberItemAmountTableByNewUser(long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			return;
		}
		String userId = String.valueOf(uid);
		List<String> tables = this.memberDao.isExistsMemberItemAmountTable(userId);
		if (ValidateUtil.isNotNull(tables)) {
			return;
		}
		try {
			this.memberDao.doCreateMemberItemAmountTableByNewUser(userId);
			this.memberDao.addMemberItemAmountTableIndex(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	@Override
	public void doCreateSmsBlacklistTableByNewUser(long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			return;
		}
		String userId = String.valueOf(uid);
		List<String> tables = this.memberDao.isExistsSmsBlacklistTable(userId);
		if (ValidateUtil.isNotNull(tables)) {
			return;
		}
		try {
			this.memberDao.doCreateSmsBlacklistTableByNewUser(userId);
			this.memberDao.addSmsBlacklistTableIndex(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	@Override
	public void doCreateTradeRatesTableByNewUser(Long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			return;
		}
		String userId = String.valueOf(uid);
		List<String> tables = this.memberDao.isExistsTradeRatesTable(userId);
		if (ValidateUtil.isNotNull(tables)) {
			return;
		}
		try {
			this.memberDao.doCreateTradeRatesTableByNewUser(userId);
			this.memberDao.addTradeRatesTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description 订单抽取会员,保存或者更新一批会员数据
	 * @param uid
	 * @param members
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月31日 上午11:56:44
	 */
	public void saveSynMemberData(Long uid, List<MemberInfoDTO> members) {
		if (ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(members)) {
			return;
		}
		this.batchSaveMemberData(uid, members);
	}

	/**
	 * @Description 批量插入同步的会员信息
	 * @param uid
	 * @param members
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月31日 下午6:20:33
	 */
	public void batchSaveMemberData(Long uid, List<MemberInfoDTO> members) {
		BaseListEntity<MemberInfoDTO> entity = new BaseListEntity<>();
		entity.setUid(uid);
		entity.setEntityList(members);
		memberDao.batchSaveMemberData(entity);
	}

	/**
	 * @Description 批量更新同步的会员信息
	 * @param uid
	 * @param members
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月31日 下午6:21:01
	 */
	public void batchUpdateMemberData(Long uid, List<MemberInfoDTO> members) {
	}

	/**
	 * 查询单个会员 (点击客户回复时展示客户信息)
	 */
	@Override
	public MemberInfoDTO findMemberByParam(Long uid, String buyerNick) {
		if (uid == null || buyerNick == null || "".equals(buyerNick)) {
			return null;
		}
		MemberInfoDTO memberInfo = memberDao.queryMemberByBuyerNick(uid, buyerNick);
		return memberInfo;
	}

	/**
	 * 统计会员个数(首页)
	 */
	@Override
	public long countMemberByParam(Long uid, MemberInfoVO memberInfoVO) {
		Long memberCount = memberDao.countMemberByParam(memberInfoVO);
		return memberCount == null ? 0 : memberCount;
	}

	/**
	 * @Description 客户信息页,查询会员分页信息
	 * @param criteria
	 * @param uid
	 * @return SimplePage 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月8日 上午10:18:29
	 */
	public SimplePage queryMemberPage(Long uid, MemberCriteria criteria) {
		// TODO 手机号加密,模糊处理
		Long count = this.queryMemberCount(uid, criteria);
		List<MemberInfoDTO> datas = this.queryMemberLimitList(uid, criteria);
		SimplePage page = new SimplePage(datas, criteria.getPageNo(), count, criteria.getPageSize());
		return page;
	}

	/**
	 * @Description: 客户信息页,查询会员分页列表
	 * @param uid
	 * @param criteria
	 * @return List<MemberInfoDTO> 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月8日 下午12:01:09
	 */
	private List<MemberInfoDTO> queryMemberLimitList(Long uid, MemberCriteria criteria) {
		return memberDao.queryMemberLimitList(criteria);
	}

	/**
	 * @Description: 客户信息页,查询会员数量
	 * @param uid
	 * @param criteria
	 * @return Long 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月8日 下午12:01:31
	 */
	private Long queryMemberCount(Long uid, MemberCriteria criteria) {
		return memberDao.queryMemberCount(criteria);
	}

	/**
	 * @Description 客户信息页,查询单个客户详情
	 * @param id
	 * @param criteria
	 * @return MemberInfoDTO 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月8日 下午12:11:11
	 */
	public MemberInfoDTO queryMemberInfo(Long uid, MemberCriteria criteria) {
		return memberDao.queryMemberInfo(criteria);
	}

	/**
	 * @Description 修改会员信息
	 * @param uid
	 * @param member
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月27日 下午3:42:20
	 */
	public void updateMembeInfo(Long uid, MemberInfoDTO member) {
		int excute = memberDao.updateMembeInfo(member);
		logger.info("用户" + member.getUid() + "会员昵称" + member.getBuyerNick() + "更新影响行数为" + excute);
		/*
		 * if(excute!=1) throw new RuntimeException("影响的行数不为1! 卖家昵称:"
		 * +member.getUserName()+";" +"买家昵称:"+member.getBuyerNick());
		 */
	}

	/**
	 * @Description 更新会员备注
	 * @param uid
	 * @param member
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月28日 上午10:50:15
	 */
	public void updateMembeRemarks(Long uid, MemberInfoDTO member) {
		int excute = memberDao.updateMembeRemarks(member);
		if (excute != 1)
			throw new RuntimeException(
					"影响的行数不为1! 卖家昵称:" + member.getUserName() + ";" + "买家昵称:" + member.getBuyerNick());

	}

	/**
	 * @Description 会员短信群发,查询会员
	 * @param uid
	 * @param criteria
	 * @param dynamicKey
	 * @return Map<String,Object> 返回类型
	 * @author jackstraw_yu
	 * @date 2018年3月7日 下午1:45:30
	 */
	@Override
	public Map<String, Object> queryMemberList(Long uid, MemberMsgCriteria criteria, String dynamicKey) {
		// 会员短信群发:筛选会员,默认查询会员表,当订单属性的条件存在时,关联订单表查询
		List<MemberInfoDTO> list = new ArrayList<>();
		int count = 0;
		// if(criteria.getQueryTable() !=null &&
		// "TRADE_DTO".equals(criteria.getQueryTable())){
		// list = this.queryMembersFromDefaultTable(uid,criteria);
		// count = this.queryCountFromDefaultTable(uid,criteria);
		// }else{
		// list = this.queryMembersJoinTradeTable(uid,criteria);
		// count = this.queryCountJoinTradeTable(uid,criteria);
		// }
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("data", list);
		map.put("count", count);
		if (count > 0 && list != null && !list.isEmpty()) {// 搜索条件保存一个小时
			redisLockService.putStringValueWithExpireTime(RedisConstant.RediskeyCacheGroup.MEMBER_BATCH_SEND_DATA_KEY
					+ "-" + dynamicKey + "-" + criteria.getUserNick(), JsonUtil.toJson(criteria), TimeUnit.HOURS, 1l);
			map.put("key", dynamicKey);
		}
		return map;
	}

	/**
	 * @Description: 会员短信群发查询会员集合(关联订单表)
	 * @param uid
	 * @param criteria
	 * @return List<MemberInfoDTO> 返回类型
	 * @author jackstraw_yu
	 * @date 2018年3月7日 下午2:00:55
	 */
	public List<MemberInfoDTO> queryMembersJoinTradeTable(Long uid, MemberMsgCriteria criteria) {
		return memberDao.listMemberByTrade(criteria);
	}

	/**
	 * @Description: 会员短信群发查询会员集合 (只查询会员表)
	 * @param uid
	 * @param criteria
	 * @return List<MemberInfoDTO> 返回类型
	 * @author jackstraw_yu
	 * @date 2018年3月7日 下午1:58:38
	 */
	public List<MemberInfoDTO> queryMembersFromDefaultTable(Long uid, MemberMsgCriteria criteria) {
		return memberDao.listMemberByInfo(criteria);
	}

	@Override
	public String findPhoneBybuyerNick(Long uid, String encryptName) {
		if (uid != null && !encryptName.equals("") && null != encryptName) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("buyerNick", encryptName);
			return memberDao.findPhoneBybuyerNick(map);
		}
		return null;
	}

	@Override
	public void batchUpdateMemberStatus(Long uid, Map<String, Object> map) {
		memberDao.batchUpdateMemberStatus(map);
	}

	/**
	 * 会员短信群发查询会员数量
	 */
	@Override
	public int countMemberByInfo(Long uid, MemberMsgCriteria criteria) {
		criteria.setUid(uid);
		int memberCount = 0;
		if (criteria.getQueryTable() != null && !"".equals(criteria.getQueryTable())) {
			memberCount = memberDao.countMemberByTrade(criteria);
		} else {
			memberCount = memberDao.countMemberByInfo(criteria);
		}
		return memberCount;
	}

	/**
	 * 会员短信群发查询会员集合
	 */
	@Override
	public List<MemberInfoDTO> listMemberByInfo(Long uid, MemberMsgCriteria criteria) {
		criteria.setUid(uid);
		List<MemberInfoDTO> memberList = null;
		if (criteria.getQueryTable() != null && !"".equals(criteria.getQueryTable())) {
			memberList = memberDao.listMemberByTrade(criteria);
		} else {
			memberList = memberDao.listMemberByInfo(criteria);
		}
		return memberList;
	}

	/**
	 * 
	 */
	@Override
	public void threadDisposeMemberStatus(Long uid, List<String> buyerNicks, String status) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				try {
					if (null != buyerNicks && buyerNicks.size() > 0) {
						int dataSize = buyerNicks.size();// 总条数
						int start = 0, // 开始次数
								end = 0, // 结束次数
								node = 1000;// 每次处理多少条
						if (dataSize % node == 0) {
							end = dataSize / node;
						} else {
							end = (dataSize + node) / node;
						}
						while (start < end) {
							List<String> subList = new ArrayList<String>();
							if (start == (end - 1)) {
								subList = buyerNicks.subList(start * node, dataSize);
							} else {
								subList = buyerNicks.subList(start * node, (start + 1) * node);
							}
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("buyerNicks", subList);
							map.put("uid", uid);
							map.put("status", status);
							batchUpdateMemberStatus(uid, map);
							start++;
						}

					}
				} catch (Exception e) {
					logger.error("=========黑名单管理===========修改会员status失败===" + uid + "=====" + e.getMessage());
				}
			}
		});
	}

	/**
	 * 手机号查询会员
	 */
	@Override
	public List<MemberInfoDTO> listMemberByPhone(Long uid, String mobile) {
		if (uid == null && mobile == null || "".equals(mobile)) {
			return null;
		}
		List<MemberInfoDTO> memberInfoDTOs = this.memberDao.listMemberByPhone(uid, mobile);
		return memberInfoDTOs;
	}

	/**
	 * 根据卖家昵称更新会员是否为黑名单
	 */
	@Override
	public Boolean updateMemberStatus(Long uid, String buyerNick, String status) {
		if (uid == null && buyerNick == null || "".equals(buyerNick)) {
			return false;
		}
		if (status == null || "".equals(status)) {
			status = "2";
		}
		try {
			this.memberDao.updateMemberStatus(uid, buyerNick, status);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void updateMembeInfoListByPhone(Long uid, List<MemberInfoDTO> memberInfoList) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		List<String> mobiles = new ArrayList<String>();
		for (MemberInfoDTO memberInfo : memberInfoList) {
			if (memberInfo.getMobile() != null && memberInfo.getMobile() != "") {
				mobiles.add(memberInfo.getMobile());
			}
		}
		map.put("list", mobiles);
		this.memberDao.updateMembeInfoListByPhone(map);
	}

	@Override
	public void doCreateMemberReceiveDetailTableByNewUser(Long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			logger.info("************** 创建会员详情表时，uid为空！");
			return;
		}
		// 校验表是否存在
		List<String> tables = this.memberDao.isExistsMemberReceiveDetailTable(uid);
		if (ValidateUtil.isNotNull(tables)) {
			logger.info("************** 创建会员详情表时，该会员表已存在 uid：" + uid);
			return;
		}
		try {
			// 创建表
			this.memberDao.doCreateMemberReceiveDetailTableByNewUser(uid);
			// 添加索引
			this.memberDao.addMemberReceiveDetailTableIndex(uid);
		} catch (Exception e) {
			logger.error("************** doCreateTableByNewUser() Exception" + e.getMessage());
		}
	}

	@Override
	public void doCreatePremiumFilterRecordTable(long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			logger.info("##### 创建高级会员筛选历史记录表时，uid为空！#####");
			return;
		}
		try {
			List<String> tables = this.memberDao.isExistsPremiumFilterRecordTable(uid);
			if (ValidateUtil.isNotNull(tables)) {
				logger.info("##### 创建高级会员筛选历史记录表时，该会员表已存在 uid：" + uid + " #####");
				return;
			}
			// 创建表
			this.memberDao.doCreatePremiumFilterRecordTable(uid);
		} catch (Exception e) {
			logger.error("##### doCreatePremiumFilterRecordTable() Exception" + e.getMessage());
		}
	}
	
	/**
	 * 查询RFM标准分析购买次数和最后购买时间对应的会员数
	 */
	@Override
	public Long countMemberAmountByTimes(Long uid, Integer buyTimes, 
			Date bTime, Date eTime) throws Exception{
		if(uid == null || buyTimes == null || bTime == null || eTime == null){
			return 0L;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("buyTimes", buyTimes);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		Long memberAmount = this.memberDao.countMemberAmountByTimes(map);
		return memberAmount == null ? 0 : memberAmount;
	}
	
	/**
	 * 查询RFM标准分析购买次数和最后购买时间对应的累计消费金额以及平均客单价
	 */
	@Override
	public Double sumPaidAmountByTimes(Long uid, Integer buyTimes, 
			Date bTime, Date eTime) throws Exception{
		if(uid == null || buyTimes == null || bTime == null || eTime == null){
			return 0.0;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("buyTimes", buyTimes);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		BigDecimal paidAmount = this.memberDao.sumPaidAmountByTimes(map);
		return paidAmount == null ? 0.0 : paidAmount.doubleValue();
	}
	
	/**
	 * RFM详情分析顶部数据
	 */
	@Override
	public EffectStandardRFMVO aggregateRFMDetail(Long uid, Integer tradeNum) throws Exception{
		if(uid == null || tradeNum == null){
			return null;
		}
		Long l1 = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("tradeNum", 5);
		//所有成交的数据
		Long totalMember = this.memberDao.countDistinctMemberCount(map);
		BigDecimal totalPaidDecimal = this.memberDao.sumMemberPaidAmount(map);
		//所有成交金额
		Double totalPaidFee = totalPaidDecimal == null ? 0.0 : totalPaidDecimal.doubleValue();
		//成交客户数
		Long paidMemberCount = 0L;
		//累计消费金额
		Double paidFeeAmount = 0.0;
		//下单未付款客户数
		Integer unPaidMemberCount = 0;
		if(tradeNum == 0){
			paidMemberCount = totalMember;
			paidFeeAmount = totalPaidFee;
			map.put("tradeNum", 0);
			unPaidMemberCount = this.memberDao.countDistinctMemberCount(map).intValue();
//			unPaidMemberCount = 0;
		}else {
			map.put("tradeNum", tradeNum);
			if(tradeNum == 0){
				map.put("tradeNum", null);
			}
			paidMemberCount = this.memberDao.countDistinctMemberCount(map);
			BigDecimal sumMemberPaidAmount = this.memberDao.sumMemberPaidAmount(map);
			paidFeeAmount = sumMemberPaidAmount == null ? 0.0 : sumMemberPaidAmount.doubleValue();
		}
		//平均客单价
		String avgCusPrice = paidMemberCount == 0 ? "0.0" : (NumberUtils.getTwoDouble(paidFeeAmount / paidMemberCount) + "");
		//成交客户数占比
		String  paidMemberRatio = totalMember == 0 ? "0.0%" : (NumberUtils.getTwoDouble(((double)paidMemberCount / totalMember.intValue()) * 100) + "%");
		//累计消费金额占比
		String paidAmountRatio = totalPaidFee == 0.0 ? "0.0%" : (NumberUtils.getTwoDouble((paidFeeAmount / totalPaidFee) * 100) + "%");
//		//平均客单价占比
//		String avgPriceRatio = "100%";
		//下单未付款客户数占比
//		String unPaidMemberRatio = totalMember == 0 ? "0.0%" : (NumberUtils.getFourDouble(unPaidMemberCount / totalMember.intValue()) * 100 + "");
		EffectStandardRFMVO resultVO = new EffectStandardRFMVO();
//		if(tradeNum == 0){
//			resultVO.setPaidMemberCount(totalMember == null ? 0 : totalMember.intValue());
//			resultVO.setPaidFeeAmount(totalPaidFee);
//			resultVO.setAvgCusPrice(avgCusPrice);
//		}else if(tradeNum == 1){
//			resultVO.setPaidMemberCount(paidMemberCount);
//			resultVO.setPaidFeeAmount(paidFeeAmount);
//			resultVO.setAvgCusPrice(avgCusPrice);
//		}else if(tradeNum == 2){
//			resultVO.setPaidMemberCount(paidMemberCount);
//			resultVO.setPaidFeeAmount(paidFeeAmount);
//			resultVO.setAvgCusPrice(avgCusPrice);
//		}
		resultVO.setPaidMemberCount(paidMemberCount == null ? 0 : paidMemberCount.intValue());
		resultVO.setPaidFeeAmount(paidFeeAmount);
		resultVO.setAvgCusPrice(avgCusPrice);
		resultVO.setUnPaidMemberCount(unPaidMemberCount);
		resultVO.setPaidMemberRatio(paidMemberRatio);
		resultVO.setPaidAmountRatio(paidAmountRatio);
//		resultVO.setAvgPriceRatio(avgPriceRatio);
//		resultVO.setUnPaidMemberRatio(unPaidMemberRatio);
		Long l2 = System.currentTimeMillis();
		logger.info("uid:" + uid + ",执行RFM详细数据的顶部数据耗时：" + (l2 - l1) + "ms");
		return resultVO;
	}
	
	@Override
	public MemberInfoDTO queryStandardRFM(Long uid, Integer tradeNum, Date bTime, Date eTime){
		if(uid == null){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("tradeNum", tradeNum);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		MemberInfoDTO memberInfoDTO = null;
		try {
			memberInfoDTO = this.memberDao.queryStandardRFM(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return memberInfoDTO;
	}

}
