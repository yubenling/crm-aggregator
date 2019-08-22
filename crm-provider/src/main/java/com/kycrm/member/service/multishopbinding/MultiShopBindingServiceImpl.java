package com.kycrm.member.service.multishopbinding;

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
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.multishopbinding.IMultiShopBindingDao;
import com.kycrm.member.domain.entity.multishopbinding.MultiShopBindingDTO;
import com.kycrm.member.domain.entity.multishopbinding.MultiShopBindingSendMessageRecordDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.domain.utils.pagination.Pagination;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingApplyVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingConfirmVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingDeleteRecordVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingReleaseBindingVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingSendMessageVO;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.user.IUserRechargeService;
import com.kycrm.util.IdUtils;
import com.kycrm.util.MsgType;

@Service("multiShopBindingService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class MultiShopBindingServiceImpl implements IMultiShopBindingService {

	private static final Log logger = LogFactory.getLog(MultiShopBindingServiceImpl.class);

	@Autowired
	private IMultiShopBindingDao multiShopBindingDao;

	@Autowired
	private IMultiShopBindingSendMessageRecordService multiShopBindingSendMessageRecordService;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private IUserAccountService userAccountService;

	@Autowired
	private IUserRechargeService userRechargeService;

	/**
	 * 分页查询
	 */
	@Override
	public Pagination findBindingList(Long uid, Integer menuNumber, String contextPath, Integer pageNo)
			throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("uid", uid);
		// 先设置每页显示的条数为5条
		Integer currentRows = 5;
		// 计算出起始行数
		Integer startRows = (pageNo - 1) * currentRows;
		// 计算出总页数
		int count = this.findBindingListCount(uid, menuNumber);
		paramMap.put("menuNumber", menuNumber);
		paramMap.put("startRows", startRows);
		paramMap.put("currentRows", currentRows);
		List<MultiShopBindingDTO> multiShopBindingList = this.multiShopBindingDao.findBindingList(paramMap);
		MultiShopBindingDTO multiShopBindingDTO = null;
		for (int i = 0; i < multiShopBindingList.size(); i++) {
			multiShopBindingDTO = multiShopBindingList.get(i);
			if (menuNumber == 1) {
				if (uid.equals(multiShopBindingDTO.getChildShopUid())) {
					multiShopBindingDTO.setShowShopName(multiShopBindingDTO.getFamilyShopName());
					multiShopBindingDTO
							.setShowTotalSendMessageCount(multiShopBindingDTO.getChildShopTotalSendMessageCount());
					multiShopBindingDTO.setShowTotalReceiveMessageCount(
							multiShopBindingDTO.getChildShopTotalReceiveMessageCount());
				}
				if (uid.equals(multiShopBindingDTO.getFamilyShopUid())) {
					multiShopBindingDTO.setShowShopName(multiShopBindingDTO.getChildShopName());
					multiShopBindingDTO
							.setShowTotalSendMessageCount(multiShopBindingDTO.getFamilyShopTotalSendMessageCount());
					multiShopBindingDTO.setShowTotalReceiveMessageCount(
							multiShopBindingDTO.getFamilyShopTotalReceiveMessageCount());
				}
			} else if (menuNumber == 2) {
				multiShopBindingDTO.setShowShopName(multiShopBindingDTO.getFamilyShopName());
				multiShopBindingDTO
						.setShowTotalSendMessageCount(multiShopBindingDTO.getFamilyShopTotalSendMessageCount());
				multiShopBindingDTO
						.setShowTotalReceiveMessageCount(multiShopBindingDTO.getFamilyShopTotalReceiveMessageCount());
			} else {
				multiShopBindingDTO.setShowShopName(multiShopBindingDTO.getChildShopName());
				multiShopBindingDTO
						.setShowTotalSendMessageCount(multiShopBindingDTO.getChildShopTotalSendMessageCount());
				multiShopBindingDTO
						.setShowTotalReceiveMessageCount(multiShopBindingDTO.getChildShopTotalReceiveMessageCount());
			}
		}
		Pagination pagination = new Pagination(pageNo, currentRows, count, multiShopBindingList);
		StringBuilder requestParams = new StringBuilder();
		// 拼接分页的后角标中的跳转路径与查询的条件
		String url = contextPath + "/multiShopBinding/findBindingList";
		pagination.pageView(url, requestParams.toString());
		return pagination;
	}

	@Override
	public MultiShopBindingDTO findSingleBinding(Long id) throws Exception {
		return this.multiShopBindingDao.findSingleBinding(id);
	}

	/**
	 * 查询数量
	 */
	@Override
	public int findBindingListCount(Long uid, Integer menuNumber) throws Exception {
		return this.multiShopBindingDao.findMultiShopBindingCount(uid, menuNumber);
	}

	/**
	 * 申请绑定
	 */
	@Override
	@SuppressWarnings("deprecation")
	public Long applyBinding(MultiShopBindingApplyVO multiShopBindingVO) throws Exception {
		UserInfo childShopUserInfo = this.userInfoService.findUserInfo(multiShopBindingVO.getChildShopUid());
		if (childShopUserInfo == null) {
			logger.error("当前店铺用户数据为空!!!");
			throw new Exception("4013");
		}
		UserInfo familyShopUserInfo = this.userInfoService.findUserInfo(multiShopBindingVO.getFamilyShopName());
		if (familyShopUserInfo == null) {
			logger.error("将要绑定的主店铺不存在!!!");
			throw new Exception("4014");
		}
		if (familyShopUserInfo.getExpirationTime().before(new Date())) {
			logger.error("将要绑定的主店铺已过期!!!");
			throw new Exception("4015");
		}
		multiShopBindingVO.setFamilyShopUid(familyShopUserInfo.getId());
		Long result = this.multiShopBindingDao.isAlreadyBinded(multiShopBindingVO.getChildShopUid(),
				multiShopBindingVO.getFamilyShopUid());
		if (result > 0) {
			logger.error("店铺已经被绑定, 不能重复绑定!!!");
			throw new Exception("4016");
		}
		MultiShopBindingDTO multiShopBindingDTO = new MultiShopBindingDTO();
		multiShopBindingDTO.setChildShopUid(multiShopBindingVO.getChildShopUid());
		multiShopBindingDTO.setChildShopMobile(multiShopBindingVO.getChildShopMobile());
		multiShopBindingDTO.setChildShopName(childShopUserInfo.getTaobaoUserNick());
		multiShopBindingDTO.setFamilyShopUid(familyShopUserInfo.getId());
		multiShopBindingDTO.setFamilyShopMobile(familyShopUserInfo.getMobile());
		multiShopBindingDTO.setFamilyShopName(familyShopUserInfo.getTaobaoUserNick());
		multiShopBindingDTO.setCreateDate(new Date());
		multiShopBindingDTO.setBindingDate(null);
		multiShopBindingDTO.setUnbindingDate(null);
		multiShopBindingDTO.setChildShopTotalSendMessageCount(0L);
		multiShopBindingDTO.setChildShopTotalReceiveMessageCount(0L);
		multiShopBindingDTO.setFamilyShopTotalSendMessageCount(0L);
		multiShopBindingDTO.setFamilyShopTotalReceiveMessageCount(0L);
		multiShopBindingDTO.setChildShopApplyFlag(1);
		multiShopBindingDTO.setFamilyShopApplyFlag(1);
		// 未绑定
		multiShopBindingDTO.setBindingStatus(0);
		// 1为正常 0为删除
		multiShopBindingDTO.setStatus(1);
		return this.multiShopBindingDao.addApplyBinding(multiShopBindingDTO);
	}

	/**
	 * 解除绑定
	 */
	@Override
	public Long releaseBinding(MultiShopBindingReleaseBindingVO releaseBindingVO) throws Exception {
		return this.multiShopBindingDao.releaseBinding(releaseBindingVO);
	}

	/**
	 * 接受或拒绝绑定
	 */
	@Override
	public Long confirm(MultiShopBindingConfirmVO confirmVO) throws Exception {
		MultiShopBindingDTO binding = this.multiShopBindingDao.findSingleBinding(confirmVO.getId());
		Long result = this.multiShopBindingDao.isAlreadyBinded(binding.getChildShopUid(), binding.getFamilyShopUid());
		if (result > 0) {
			logger.error("店铺已经被绑定, 不能重复绑定！");
			throw new Exception("4016");
		}
		return this.multiShopBindingDao.confirm(confirmVO);
	}

	/**
	 * 赠送短信
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Long sendMessage(MultiShopBindingSendMessageVO sendMessageVO) throws Exception {
		// 根据系统绑定手机号获取绑定记录
		MultiShopBindingDTO multiShopBindingDTO = this.multiShopBindingDao.findMultiShopBinding(sendMessageVO.getId());
		if (multiShopBindingDTO == null) {
			logger.error("未获取到绑定信息");
			throw new Exception("4008");
		}
		// 根据uid获取childShopUserInfo
		UserInfo childShopUserInfo = userInfoService.findUserInfo(multiShopBindingDTO.getChildShopUid());
		if (childShopUserInfo == null) {
			logger.error("子店铺信息为空");
			throw new Exception("4009");
		}
		if (childShopUserInfo.getExpirationTime().before(new Date())) {
			logger.error("子店铺已过期");
			throw new Exception("4011");
		}
		// 根据uid获取familyShopUserInfo
		UserInfo familyShopUserInfo = userInfoService.findUserInfo(multiShopBindingDTO.getFamilyShopUid());
		if (familyShopUserInfo == null) {
			logger.error("主店铺信息为空");
			throw new Exception("4010");
		}
		if (familyShopUserInfo.getExpirationTime().before(new Date())) {
			logger.error("主店铺已过期");
			throw new Exception("4012");
		}
		boolean isChildShop = false;
		if (sendMessageVO.getUid().equals(childShopUserInfo.getId())) {
			isChildShop = true;
		}
		// 子店铺短信余额
		Long childShopUserAccountSms = this.userAccountService.findUserAccountSms(childShopUserInfo.getId());
		// 主店铺短信余额
		Long familyShopUserAccountSms = this.userAccountService.findUserAccountSms(familyShopUserInfo.getId());
		// 赠送短信条数
		Integer sendMessageCount = sendMessageVO.getSendMessageCount();
		if (isChildShop) {
			logger.info("##### 子店铺向主店铺赠送短信 #####");
			logger.info("子店铺 UID = " + childShopUserInfo.getId() + " 赠送前短信余额 = " + childShopUserAccountSms);
			logger.info("主店铺 UID = " + familyShopUserInfo.getId() + " 赠送前短信余额 = " + familyShopUserAccountSms);
			logger.info("子店铺向主店铺赠送" + sendMessageCount + "条短信");
			if (childShopUserAccountSms.compareTo(sendMessageCount.longValue()) == -1) {
				logger.error("子店铺短信余额不足, 无法赠送！");
				throw new Exception("4006");
			}
		} else {
			logger.info("##### 主店铺向子店铺赠送短信 #####");
			logger.info("主店铺 UID = " + familyShopUserInfo.getId() + " 赠送前短信余额 = " + familyShopUserAccountSms);
			logger.info("子店铺 UID = " + childShopUserInfo.getId() + " 赠送前短信余额 = " + childShopUserAccountSms);
			logger.info("主店铺向子店铺赠送" + sendMessageCount + "条短信");
			if (familyShopUserAccountSms.compareTo(sendMessageCount.longValue()) == -1) {
				logger.error("主店铺短信余额不足, 无法赠送！");
				throw new Exception("4007");
			}
		}
		boolean sendUpdateUserSmsResult = false;
		boolean receiveUpdateUserSmsResult = false;
		if (isChildShop) {// 子店铺向主店铺赠送短信
			sendUpdateUserSmsResult = userAccountService.doUpdateUserSms(multiShopBindingDTO.getChildShopUid(),
					childShopUserInfo.getTaobaoUserNick(), true, sendMessageCount.intValue(),
					MsgType.MSG_MULTI_SHOP_SEND, childShopUserInfo.getTaobaoUserNick(), null, null, false);
			receiveUpdateUserSmsResult = userAccountService.doUpdateUserSms(multiShopBindingDTO.getFamilyShopUid(),
					familyShopUserInfo.getTaobaoUserNick(), false, sendMessageCount.intValue(),
					MsgType.MSG_MULTI_SHOP_SEND, familyShopUserInfo.getTaobaoUserNick(), null, null, false);
		} else {// 主店铺向子店铺赠送短信
			sendUpdateUserSmsResult = userAccountService.doUpdateUserSms(multiShopBindingDTO.getChildShopUid(),
					childShopUserInfo.getTaobaoUserNick(), false, sendMessageCount.intValue(),
					MsgType.MSG_MULTI_SHOP_SEND, childShopUserInfo.getTaobaoUserNick(), null, null, false);
			receiveUpdateUserSmsResult = userAccountService.doUpdateUserSms(multiShopBindingDTO.getFamilyShopUid(),
					familyShopUserInfo.getTaobaoUserNick(), true, sendMessageCount.intValue(),
					MsgType.MSG_MULTI_SHOP_SEND, familyShopUserInfo.getTaobaoUserNick(), null, null, false);
		}
		if (sendUpdateUserSmsResult && receiveUpdateUserSmsResult) {
			// 更新【子店铺, 主店铺】总赠送条数和总获赠条数
			multiShopBindingDTO.setChildShopUid(childShopUserInfo.getId());
			multiShopBindingDTO.setFamilyShopUid(familyShopUserInfo.getId());
			// 子店铺向主店铺赠送短信
			if (isChildShop) {
				multiShopBindingDTO.setChildShopTotalSendMessageCount(
						multiShopBindingDTO.getChildShopTotalSendMessageCount() + sendMessageCount);
				multiShopBindingDTO.setFamilyShopTotalReceiveMessageCount(
						multiShopBindingDTO.getFamilyShopTotalReceiveMessageCount() + sendMessageCount);
			} else {
				multiShopBindingDTO.setFamilyShopTotalSendMessageCount(
						multiShopBindingDTO.getFamilyShopTotalSendMessageCount() + sendMessageCount);
				multiShopBindingDTO.setChildShopTotalReceiveMessageCount(
						multiShopBindingDTO.getChildShopTotalReceiveMessageCount() + sendMessageCount);
			}
			Long updateMultiShopBindingResult = this.multiShopBindingDao.updateMultiShopBinding(multiShopBindingDTO);
			if (updateMultiShopBindingResult == 1) {
				MultiShopBindingSendMessageRecordDTO multiShopBindingRecord = null;
				if (isChildShop) {
					// 添加赠送方赠送记录
					multiShopBindingRecord = new MultiShopBindingSendMessageRecordDTO();
					multiShopBindingRecord.setMultiShopBindingId(multiShopBindingDTO.getId());
					multiShopBindingRecord.setChildShopUid(childShopUserInfo.getId());
					multiShopBindingRecord.setFamilyShopUid(familyShopUserInfo.getId());
					multiShopBindingRecord.setSendMessageCount(sendMessageCount);
					multiShopBindingRecord.setSendDate(new Date());
					multiShopBindingRecord.setSendOrReceive(1);// 1表示赠送 2表示获赠
					this.multiShopBindingSendMessageRecordService.addSendMessageRecord(multiShopBindingRecord);
					// 添加获赠方赠送记录
					multiShopBindingRecord = new MultiShopBindingSendMessageRecordDTO();
					multiShopBindingRecord.setMultiShopBindingId(multiShopBindingDTO.getId());
					multiShopBindingRecord.setChildShopUid(familyShopUserInfo.getId());
					multiShopBindingRecord.setFamilyShopUid(childShopUserInfo.getId());
					multiShopBindingRecord.setSendMessageCount(sendMessageCount);
					multiShopBindingRecord.setSendDate(new Date());
					multiShopBindingRecord.setSendOrReceive(2);// 1表示赠送 2表示获赠
					this.multiShopBindingSendMessageRecordService.addSendMessageRecord(multiShopBindingRecord);
				} else {
					// 添加赠送方赠送记录
					multiShopBindingRecord = new MultiShopBindingSendMessageRecordDTO();
					multiShopBindingRecord.setMultiShopBindingId(multiShopBindingDTO.getId());
					multiShopBindingRecord.setChildShopUid(familyShopUserInfo.getId());
					multiShopBindingRecord.setFamilyShopUid(childShopUserInfo.getId());
					multiShopBindingRecord.setSendMessageCount(sendMessageCount);
					multiShopBindingRecord.setSendDate(new Date());
					multiShopBindingRecord.setSendOrReceive(1);// 1表示赠送 2表示获赠
					this.multiShopBindingSendMessageRecordService.addSendMessageRecord(multiShopBindingRecord);
					// 添加获赠方赠送记录
					multiShopBindingRecord = new MultiShopBindingSendMessageRecordDTO();
					multiShopBindingRecord.setMultiShopBindingId(multiShopBindingDTO.getId());
					multiShopBindingRecord.setChildShopUid(childShopUserInfo.getId());
					multiShopBindingRecord.setFamilyShopUid(familyShopUserInfo.getId());
					multiShopBindingRecord.setSendMessageCount(sendMessageCount);
					multiShopBindingRecord.setSendDate(new Date());
					multiShopBindingRecord.setSendOrReceive(2);// 1表示赠送 2表示获赠
					this.multiShopBindingSendMessageRecordService.addSendMessageRecord(multiShopBindingRecord);
				}
				// 充值记录
				UserRecharge childUserRecharge = null;
				UserRecharge familyUserRecharge = null;
				if (isChildShop) {
					// 赠送方
					childUserRecharge = new UserRecharge();
					childUserRecharge.setUid(childShopUserInfo.getId());
					childUserRecharge.setOrderId(IdUtils.getTradeId());
					childUserRecharge.setRechargeDate(new Date());
					childUserRecharge.setRechargeBeforeNum(childShopUserAccountSms.intValue());
					childUserRecharge.setRechargeNum(sendMessageCount);
					childUserRecharge.setRechargeLaterNum(childShopUserAccountSms - sendMessageCount);
					childUserRecharge.setStatus("1");
					childUserRecharge.setRechargeType("7");
					childUserRecharge.setRemarks("店铺赠送");
					childUserRecharge.setCreatedDate(new Date());
					childUserRecharge.setUserNick(familyShopUserInfo.getTaobaoUserNick());
					childUserRecharge.setOptlock(0);
					this.userRechargeService.saveUserRechar(childUserRecharge);
					// 获赠方
					familyUserRecharge = new UserRecharge();
					familyUserRecharge.setUid(familyShopUserInfo.getId());
					familyUserRecharge.setOrderId(IdUtils.getTradeId());
					familyUserRecharge.setRechargeDate(new Date());
					familyUserRecharge.setRechargeBeforeNum(familyShopUserAccountSms.intValue());
					familyUserRecharge.setRechargeNum(sendMessageCount);
					familyUserRecharge.setRechargeLaterNum(familyShopUserAccountSms + sendMessageCount);
					familyUserRecharge.setStatus("1");
					familyUserRecharge.setRechargeType("6");
					familyUserRecharge.setRemarks("店铺获赠");
					familyUserRecharge.setCreatedDate(new Date());
					familyUserRecharge.setUserNick(childShopUserInfo.getTaobaoUserNick());
					familyUserRecharge.setOptlock(0);
					this.userRechargeService.saveUserRechar(familyUserRecharge);
				} else {
					// 赠送方
					familyUserRecharge = new UserRecharge();
					familyUserRecharge.setUid(familyShopUserInfo.getId());
					familyUserRecharge.setOrderId(IdUtils.getTradeId());
					familyUserRecharge.setRechargeDate(new Date());
					familyUserRecharge.setRechargeBeforeNum(familyShopUserAccountSms.intValue());
					familyUserRecharge.setRechargeNum(sendMessageCount);
					familyUserRecharge.setRechargeLaterNum(familyShopUserAccountSms - sendMessageCount);
					familyUserRecharge.setStatus("1");
					familyUserRecharge.setRechargeType("7");
					familyUserRecharge.setRemarks("店铺赠送");
					familyUserRecharge.setCreatedDate(new Date());
					familyUserRecharge.setUserNick(familyShopUserInfo.getTaobaoUserNick());
					familyUserRecharge.setOptlock(0);
					this.userRechargeService.saveUserRechar(familyUserRecharge);
					// 获赠方
					childUserRecharge = new UserRecharge();
					childUserRecharge.setUid(childShopUserInfo.getId());
					childUserRecharge.setOrderId(IdUtils.getTradeId());
					childUserRecharge.setRechargeDate(new Date());
					childUserRecharge.setRechargeBeforeNum(childShopUserAccountSms.intValue());
					childUserRecharge.setRechargeNum(sendMessageCount);
					childUserRecharge.setRechargeLaterNum(childShopUserAccountSms + sendMessageCount);
					childUserRecharge.setStatus("1");
					childUserRecharge.setRechargeType("6");
					childUserRecharge.setRemarks("店铺获赠");
					childUserRecharge.setCreatedDate(new Date());
					childUserRecharge.setUserNick(childShopUserInfo.getTaobaoUserNick());
					childUserRecharge.setOptlock(0);
					this.userRechargeService.saveUserRechar(childUserRecharge);
				}
				return 1L;
			} else {
				return 0L;
			}
		} else {
			return 0L;
		}
	}

	@Override
	public Long deleteRecord(MultiShopBindingDeleteRecordVO deleteRecord) throws Exception {
		return this.multiShopBindingDao.deleteRecord(deleteRecord);
	}

	@Override
	public boolean checkIfExist(String childShopName, String familyShopName, Integer[] bindingStatusArray)
			throws Exception {
		int result = this.multiShopBindingDao.checkIfExist(childShopName, familyShopName, bindingStatusArray);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

}
