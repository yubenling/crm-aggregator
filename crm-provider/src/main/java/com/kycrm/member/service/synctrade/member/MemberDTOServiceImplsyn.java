package com.kycrm.member.service.synctrade.member;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.syntrade.member.IMemberDTODaosyn;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.util.ValidateUtil;

/**
 * @author wy
 * @version 创建时间：2018年1月18日 下午2:21:19
 */
@MyDataSource
@Service
public class MemberDTOServiceImplsyn /* implements IMemberDTOService */ {
	private static final Log logger = LogFactory.getLog(MemberDTOServiceImplsyn.class);

	/**
	 * 同步订单list分割长度单元
	 */
	// private final Integer LIST_SEPERATOR = 500;

	@Autowired
	private IMemberDTODaosyn memberDao;

	// ##################### showSetupMenu() Exception:
	/**
	 * 根据用户主键id创建对应的会员表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:53:10
	 * @param uid
	 *            用户主键id
	 */
	// @Override
	public void doCreateTableByNewUser(Long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			logger.info("##################### 创建会员表时，uid为空！");
			return;
		}
		// String userName = String.valueOf(uid);
		List<String> tables = this.memberDao.isExistsTable(uid);
		if (ValidateUtil.isNotNull(tables)) {
			logger.info("##################### 创建会员表时，该会员表已存在 uid：" + uid);
			return;
		}
		try {
			// 创建表
			this.memberDao.doCreateTableByNewUser(uid);
			// 添加索引
			this.memberDao.addMemberTableIndex(uid);
		} catch (Exception e) {
			logger.error("##################### doCreateTableByNewUser() Exception" + e.getMessage());
		}
	}

	/**
	 * 
	 */
	// @Override
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

	/**
	 * @Description 订单抽取会员,保存或者更新一批会员数据
	 * @param uid
	 * @param members
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月31日 上午11:56:44
	 */
	// @Override
	public void saveSynMemberData(Long uid, List<MemberInfoDTO> members) {
		if (uid == null || members == null || members.isEmpty())
			return;
		List<MemberInfoDTO> uList = new ArrayList<MemberInfoDTO>(), sList = new ArrayList<MemberInfoDTO>();
		boolean exist=false;
		for (MemberInfoDTO member : members) {
			// 查询是否存在该用户
			exist = this.queryMemberExist(uid, member);
			if (exist) {
				uList.add(member);
			} else {
				sList.add(member);
			}
		}
		if (sList.size() > 0) {
			logger.info("用户" + uid + "批量插入会员数量为" + sList.size());
			this.batchSaveMemberData(uid, sList);
		}
		if (uList.size() > 0) {
			// 如果是更新数据，需要将数据库中的数据重新的取出来计算
			for (MemberInfoDTO member : uList) {
				MemberInfoDTO member_from = findMemberInfo(member.getUid(), member.getBuyerNick());
				// 计算交易来源,如果数据库是1，那么永远是1
				if(member_from.getRelationSource()!=null&&member_from.getRelationSource()==1){
					member.setRelationSource(1);
				}
				// 会员有首次购买时间，则不填充
				if (member_from.getFirstPayTime() != null && member.getFirstPayTime() != null
						&& member_from.getFirstPayTime().before(member.getFirstPayTime())) {
					member.setFirstPayTime(null);
				}
				//首次完成时间
				if(member_from.getFirstTradeFinishTime()!=null&&member.getFirstTradeFinishTime()!=null&&
						member_from.getFirstTradeFinishTime().before(member.getFirstTradeFinishTime())){
					member.setFirstTradeFinishTime(null); 
				}
				//首次交易时间
				if(member_from.getFirstTradeTime()!=null&&member.getFirstTradeTime()!=null&&
						member_from.getFirstTradeTime().before(member.getFirstTradeTime())){
					member.setFirstTradeTime(null);
				}
				//末次交易时间
				if (member_from.getLastTradeTime() != null && member.getLastTradeTime() != null
						&& member_from.getLastTradeTime().after(member.getLastTradeTime())) {
					member.setLastTradeTime(null);
				}
				//末次完成时间
				if(member_from.getLastTradeFinishTime()!=null &&member.getLastTradeFinishTime()!=null
						&&member_from.getLastTradeFinishTime().after(member.getLastTradeFinishTime())){
					member.setLastTradeFinishTime(null);
				}
				//末次付款时间
				if(member_from.getLastPayTime()!=null&&member.getLastPayTime()!=null
						&&member_from.getLastPayTime().after(member.getLastPayTime())){
					member.setLastPayTime(null);
				}
			}
			logger.info("用户" + uid + "批量更新的会员数量为" + uList.size());
			this.batchUpdateMemberData(uid, uList);
		}
	}

	// 查询出会员信息
	public MemberInfoDTO findMemberInfo(Long uid, String buyerNick) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("buyerNick", buyerNick);
		return memberDao.findMemberInfo(map);
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
	@Transactional
	public void batchSaveMemberData(Long uid, List<MemberInfoDTO> members) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("entityList", members);
		memberDao.batchSaveMemberData(map);
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
	@Transactional
	public void batchUpdateMemberData(Long uid, List<MemberInfoDTO> members) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("entityList", members);
		memberDao.batchUpdateMemberData(map);
	}

	/**
	 * @Description 查询会员是否存在，存在返回true,不存在返回false
	 * @param member
	 * @return boolean 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月1日 上午10:33:55
	 */
	private boolean queryMemberExist(Long uid, MemberInfoDTO member) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("buyerNick", member.getBuyerNick());
		int result = memberDao.queryMemberByBuyerNick(map);
		if (result == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void updateMembeInfo(Long uid, MemberInfoDTO member) {
		int excute = memberDao.updateMembeInfo(member);
		if (excute != 1)
			throw new RuntimeException(
					"影响的行数不为1! 卖家昵称:" + member.getUserName() + ";" + "买家昵称:" + member.getBuyerNick());
	}

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

	public void doCreateTradeRatesTableByNewUser(Long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			return;
		}
		List<String> tables = this.memberDao.isExistsTradeRatesTable(uid);
		if (ValidateUtil.isNotNull(tables)) {
			return;
		}
		try {
			this.memberDao.doCreateTradeRatesTableByNewUser(uid);
			this.memberDao.addTradeRatesTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateLastMarketingTime(Long uid) {
		logger.info("updateLastMarketingTime" + uid);
		memberDao.updateLastMarketingTime(uid);
	}

	public void doCreatePremiumFilterRecordTable(Long uid) {
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
	 * 修改会员拍下订单数量和拍下商品数量
	 */
	public void updateMemberNum(Long uid, List<TradeDTO> list) {
		try {
			Map<String, MemberInfoDTO> member_map = new HashMap<String, MemberInfoDTO>();
			for (TradeDTO td : list) {
				String key = td.getBuyerNick();
				if (key == null) {
					logger.info("买家昵称为null订单的tid为" + td.getTid());
					continue;
				}
				if (member_map.containsKey(key)) {
					MemberInfoDTO memberInfoDTO = member_map.get(key);
					memberInfoDTO.setAddItemNum(memberInfoDTO.getAddItemNum() == null ? td.getNum()
							: (memberInfoDTO.getAddItemNum() + td.getNum()));
					memberInfoDTO.setAddNumber(
							memberInfoDTO.getAddNumber() == null ? 1 : (memberInfoDTO.getAddNumber() + 1));
					member_map.put(key, memberInfoDTO);
				} else {
					// 新订单加一
					MemberInfoDTO memberInfoDTO = new MemberInfoDTO();
					memberInfoDTO.setBuyerNick(key);
					memberInfoDTO.setAddNumber(1);
					memberInfoDTO.setAddItemNum(td.getNum());
					member_map.put(key, memberInfoDTO);
				}
			}
			List<MemberInfoDTO> memberList = new ArrayList<MemberInfoDTO>(member_map.values());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("list", memberList);
			// 批量更新会员
			memberDao.batchUpdateMemberNum(map);
		} catch (Exception e) {
			logger.info("更新会员拍下数量出错" + e);
		}

	}

	public Long getCountByCondition(Long uid, Date endDate, String column) throws Exception {
		return this.memberDao.getCountByCondition(uid, endDate, column);
	}

	public void batchUpdate(Long uid, String column, List<MemberInfoDTO> memberInfoList) throws Exception {
		this.memberDao.batchUpdate(uid, column, memberInfoList);
	}

	public void batchUpdateMember(Long uid, List<MemberInfoDTO> memberInfoList) throws Exception {
		this.memberDao.batchUpdateMember(uid, memberInfoList);
	}

	public List<MemberInfoDTO> getBuyerNickList(Long uid, String column, Integer startPoint, Integer range)
			throws Exception {
		return this.memberDao.getBuyerNickList(uid, column, startPoint, range);
	}

	public void recreateFilterRecordTable(Long uid) throws Exception {
		List<String> list = this.memberDao.isExistsPremiumFilterRecordTable(uid);
		if (ValidateUtil.isNotNull(list)) {
			int filterRecordCount = this.memberDao.getFilterRecordCount(uid);
			if (filterRecordCount == 0) {
				logger.info("重新创建crm_premium_filter_record" + uid + "表");
				this.memberDao.dropTable(uid);
				this.memberDao.doCreatePremiumFilterRecordTable(uid);
			}
		} else {
			logger.info("创建crm_premium_filter_record" + uid + "表");
			this.memberDao.doCreatePremiumFilterRecordTable(uid);
		}

	}

	public void batchUpdateRerunColumn(Long uid, List<MemberInfoDTO> memberInfoList) throws Exception {
		this.memberDao.batchUpdateRerunColumn(uid, memberInfoList);
	}

	public List<MemberInfoDTO> getBuyerNickListByLimitId(Long uid, Long limitId, Integer startPosition,
			Integer BATCH_SIZE) throws Exception {
		return this.memberDao.getBuyerNickListByLimitId(uid, limitId, startPosition, BATCH_SIZE);
	}

	 /**
     * 修改用户的信息
     * @param memberInfo
     */
	public int updateMemberInfoNum(Long uid, MemberInfoDTO memberInfo) {
		return  memberDao.updateMemberInfoNum(memberInfo);
	}
	 /**
     * 修改用户的平均客单价
     * @param uid
     * @param updateMember
     */
	public void updateMemberAvg(Long uid, MemberInfoDTO updateMember) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("buyerNick", updateMember.getBuyerNick());
		memberDao.updateMemberAvg(map);
	}
}
