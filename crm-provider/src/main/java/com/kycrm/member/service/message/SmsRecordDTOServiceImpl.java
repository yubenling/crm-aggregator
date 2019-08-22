package com.kycrm.member.service.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.redis.RedisLockService;
import com.kycrm.member.dao.message.ISmsRecordDTODao;
import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.domain.entity.message.MessageBill;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;
import com.kycrm.member.domain.vo.trade.TradeVO;
import com.kycrm.member.service.message.thread.GetMessageBillByDate;
import com.kycrm.member.service.multishopbinding.IMultiShopBindingSendMessageRecordService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.user.IUserRechargeService;
import com.kycrm.member.util.thread.PidThreadFactory;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MobileRegEx;
import com.kycrm.util.MsgType;
import com.kycrm.util.NumberUtils;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.SecretException;

/**
 * 短信记录
 * 
 * @author wy
 * @version 创建时间：2018年1月15日 下午3:54:58
 */
@Service("smsRecordDTOService")
@MyDataSource
public class SmsRecordDTOServiceImpl implements ISmsRecordDTOService {

	private Logger logger = LoggerFactory.getLogger(SmsRecordDTOServiceImpl.class);

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private IUserAccountService userAccountService;

	// 充值记录
	@Autowired
	private IUserRechargeService userRechargeService;

	// 获赠|赠送记录
	@Autowired
	private IMultiShopBindingSendMessageRecordService multiShopBindingSendMessageRecordService;

	@Autowired
	private ISmsRecordDTODao smsRecordDTODao;

	@Autowired
	private RedisLockService redisLockService;

	@Override
	public void saveSmsRecordBySingle(Long uid, SmsRecordDTO smsSendRecord) {
		if (smsSendRecord == null || ValidateUtil.isEmpty(smsSendRecord.getRecNum()) || ValidateUtil.isEmpty(uid)) {
			return;
		}
		smsSendRecord.setUid(uid);
		this.smsRecordDTODao.doCreateSmsRecordDTOBySingle(smsSendRecord);
	}

	/**
	 * 根据用户主键id创建对应的短信记录表
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
		String userId = String.valueOf(uid);
		List<String> tables = this.smsRecordDTODao.isExistsTable(userId);
		if (ValidateUtil.isNotNull(tables)) {
			return;
		}
		try {
			this.smsRecordDTODao.doCreateTableByNewUser(userId);
			this.smsRecordDTODao.addSmsRecordTableIndex(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 订单中心效果分析查询发送记录的订单id
	 */
	@Override
	public List<String> tradeCenterEffectRecordTid(Long uid, String type, Date beginTime, Date endTime, Long taskId) {
		TradeCenterEffectVO tradeCenterEffectVO = new TradeCenterEffectVO();
		tradeCenterEffectVO.setBeginTime(beginTime);
		tradeCenterEffectVO.setEndTime(endTime);
		tradeCenterEffectVO.setTaskId(taskId);
		tradeCenterEffectVO.setUid(uid);
		tradeCenterEffectVO.setType(type);
		List<String> tidList = smsRecordDTODao.tradeCenterEffectRecordTid(tradeCenterEffectVO);
		return tidList;
	}

	/**
	 * 查询订单发送时间(订单中心效果分析)
	 */
	@Override
	public Date queryTidSendTime(Long uid, SmsRecordDTO smsRecordDTO) {
		return smsRecordDTODao.queryTidSendTime(smsRecordDTO);
	}

	@Override
	public void doCreaterSmsRecordDTOByList(Long uid, List<SmsRecordDTO> smsRecordDTOList) {
		if (ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(smsRecordDTOList)) {
			return;
		}
		BaseListEntity<SmsRecordDTO> entityList = new BaseListEntity<SmsRecordDTO>();
		entityList.setEntityList(smsRecordDTOList);
		entityList.setUid(uid);
		this.smsRecordDTODao.doCreateSmsRecordDTOByList(entityList);
	}

	@Override
	public void doCreaterSmsRecordDTOByCompressList(Long uid, byte[] compress) {
		byte[] uncompress = null;
		try {
			uncompress = GzipUtil.uncompress(compress);
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<SmsRecordDTO> smsRecordDTOList = JsonUtil.readValuesAsArrayList(new String(uncompress),
				SmsRecordDTO.class);
		this.doCreaterSmsRecordDTOByList(uid, smsRecordDTOList);
	}

	@Override
	public Map<String, Object> findSmsSendRecordPage(Long uid, SmsRecordVO vo, String accessToken) {
		/* 封装参数 */
		packagingData(vo, accessToken);
		Map<String, Object> map = new HashMap<String, Object>();
		Long totalCount = this.smsRecordDTODao.findSendRecordCount(vo);
		Integer totalPage = null;
		Long sendCount = null;
		List<SmsRecordDTO> list = null;
		if (null != totalCount) {
			totalPage = (int) Math.ceil(1.0 * totalCount / vo.getCurrentRows());
			list = this.smsRecordDTODao.findSmsSendRecordList(vo);
			for (SmsRecordDTO dto : list) {
				if (null != dto.getRecNum() && !"".equals(dto.getRecNum())) {
					try {
						if (EncrptAndDecryptClient.isEncryptData(dto.getRecNum(), EncrptAndDecryptClient.PHONE)) {
							String decrypt = EncrptAndDecryptClient.getInstance().decryptData(dto.getRecNum(),
									EncrptAndDecryptClient.PHONE, accessToken);
							dto.setRecNum(decrypt);
						}
					} catch (Exception e) {
						logger.error("*********短信发送记录**********解密出错，***********查询*********" + e.getMessage());
					}
				}

				if (null != dto.getBuyerNick() && !"".equals(dto.getBuyerNick())) {
					try {
						if (EncrptAndDecryptClient.isEncryptData(dto.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
							String decrypt = EncrptAndDecryptClient.getInstance().decryptData(dto.getBuyerNick(),
									EncrptAndDecryptClient.SEARCH, accessToken);
							dto.setBuyerNick(decrypt);
						}
					} catch (Exception e) {
						logger.error("*********短信发送记录**********解密出错，***********查询*********" + e.getMessage());
					}
				}
			}
			sendCount = this.smsRecordDTODao.findSendDeductionNum(vo);
			if (null == sendCount)
				sendCount = 0L;
		}
		map.put("sendCount", sendCount);
		map.put("totalPage", totalPage);
		map.put("list", list);
		return map;
	}

	/**
	 * 查询时间段内每天的下单金额
	 */
	@Override
	public List<Double> listDaysSmsMoney(Long uid, Date endTime) {
		if (uid == null) {
			return null;
		}
		Date beginTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(6, endTime));
		Map<String, Double> deafaultMap = new LinkedHashMap<String, Double>();
		for (int i = 0; i < 6; i++) {
			String nDaysAgo = DateUtils.dateToString(DateUtils.nDaysAgo(i, endTime));
			deafaultMap.put(nDaysAgo, 0.00);
		}
		TradeVO tradeVO = new TradeVO();
		tradeVO.setUid(uid);
		tradeVO.setMinCreatedTime(beginTime);
		tradeVO.setMaxCreatedTime(endTime);
		List<TradeVO> TradeVOList = smsRecordDTODao.listDaysSmsMoney(tradeVO);
		if (TradeVOList != null && !TradeVOList.isEmpty()) {
			for (TradeVO resultVO : TradeVOList) {
				if (deafaultMap.containsKey(resultVO.getMinCreatedTimeStr())) {
					deafaultMap.put(resultVO.getMinCreatedTimeStr(), resultVO.getMinPayment());
				}
			}
		}
		Set<Entry<String, Double>> entrySet = deafaultMap.entrySet();
		List<Double> values = new ArrayList<>();
		for (Entry<String, Double> entry : entrySet) {
			values.add(entry.getValue());
		}
		return values;
	}

	/**
	 * 查询时间段内发送的短信条数
	 */
	@Override
	public Integer sumSmsNumByTime(Long uid, Date beginTime, Date endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		Integer smsNum = smsRecordDTODao.sumSmsNumByTime(map);
		return smsNum == null ? 0 : smsNum;
	}

	/**
	 * 查询时间段内发送的记录条数
	 */
	@Override
	public Integer countRecordByTime(Long uid, Date beginTime, Date endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		Integer recordCount = smsRecordDTODao.countRecordByTime(map);
		return recordCount;
	}

	/**
	 * 根据taskId以及时间查询发送记录
	 */
	@Override
	public List<SmsRecordDTO> listRecordByTaskId(Long uid, Long taskId, Date beginTime, Date endTime) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("taskId", taskId);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		List<SmsRecordDTO> recordList = smsRecordDTODao.listRecordByTaskId(map);
		return recordList;
	}

	/**
	 * 
	 */
	@Override
	public List<String> queryPhoneList(Long uid, SmsRecordVO smsRecordVO) {
		if (smsRecordVO == null || uid == null) {
			return null;
		}
		smsRecordVO.setUid(uid);
		return smsRecordDTODao.queryPhoneList(smsRecordVO);
	}

	/**
	 * 根据msgId统计发送成功短信条数
	 */
	@Override
	public Integer countSuccessRecordByMsgId(Long uid, Long msgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("msgId", msgId);
		Integer successCount = smsRecordDTODao.countSuccessRecordByMsgId(map);
		return NumberUtils.getResult(successCount);
	}

	/**
	 * 根据买家手机号查询发送记录
	 */
	@Override
	public List<SmsRecordDTO> listSmsRecordByPhone(Long uid, String sessionKey, String phone) {
		if (uid == null || phone == null) {
			return null;
		}
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("uid", uid);
		queryMap.put("phone", phone);
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		try {
			if (EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)) {
				queryMap.put("phone", phone);
			} else {
				queryMap.put("phone", decryptClient.encrypt(phone, EncrptAndDecryptClient.PHONE, sessionKey));
			}
		} catch (SecretException e) {
			logger.info("会员互动======>>>>>>根据手机号查询发送记录,手机号加密异常!!!");
			e.printStackTrace();
		}
		List<SmsRecordDTO> recordDTOs = smsRecordDTODao.listSmsRecordByPhone(queryMap);
		return recordDTOs;
	}

	/**
	 * 根据条件查询卖家回复记录
	 */
	@Override
	public List<SmsRecordDTO> listSellerRevert(Long uid, SmsRecordVO smsRecordVO, String sessionKey) {
		if (smsRecordVO == null) {
			return null;
		}
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		String buyerNick = smsRecordVO.getBuyerNick();
		String phone = smsRecordVO.getRecNum();
		try {
			if (buyerNick != null && !"".equals(buyerNick)) {
				if (EncrptAndDecryptClient.isEncryptData(buyerNick, EncrptAndDecryptClient.SEARCH)) {
					smsRecordVO.setBuyerNick(buyerNick);
				} else {
					smsRecordVO.setBuyerNick(
							decryptClient.decryptData(buyerNick, EncrptAndDecryptClient.SEARCH, sessionKey));
				}
			}
			if (phone != null && !"".equals(phone)) {
				if (EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)) {
					smsRecordVO.setRecNum(phone);
				} else {
					smsRecordVO.setRecNum(decryptClient.decryptData(phone, EncrptAndDecryptClient.PHONE, sessionKey));
				}
			}
		} catch (SecretException e) {
			e.printStackTrace();
		}
		smsRecordVO.setUid(uid);
		List<SmsRecordDTO> sellerRevertList = smsRecordDTODao.listSellerRevert(smsRecordVO);
		if (sellerRevertList != null && !sellerRevertList.isEmpty()) {
			for (SmsRecordDTO smsRecordDTO : sellerRevertList) {
				if (smsRecordDTO != null) {
					try {
						if (smsRecordDTO.getBuyerNick() != null && !"".equals(smsRecordDTO.getBuyerNick())) {
							if (EncrptAndDecryptClient.isEncryptData(smsRecordDTO.getBuyerNick(),
									EncrptAndDecryptClient.SEARCH)) {
								smsRecordDTO.setBuyerNick(decryptClient.decryptData(smsRecordDTO.getBuyerNick(),
										EncrptAndDecryptClient.SEARCH, sessionKey));
							}
						}
						if (smsRecordDTO.getRecNum() != null && !"".equals(smsRecordDTO.getRecNum())) {
							if (EncrptAndDecryptClient.isEncryptData(smsRecordDTO.getRecNum(),
									EncrptAndDecryptClient.PHONE)) {
								smsRecordDTO.setRecNum(decryptClient.decryptData(smsRecordDTO.getRecNum(),
										EncrptAndDecryptClient.PHONE, sessionKey));
							}
						}
					} catch (SecretException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return sellerRevertList;
	}

	/**
	 * 根据条件查询卖家回复记录总记录数
	 */
	@Override
	public Integer countSellerRevert(Long uid, SmsRecordVO smsRecordVO, String sessionKey) {
		if (smsRecordVO == null) {
			return 0;
		}
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		String buyerNick = smsRecordVO.getBuyerNick();
		String phone = smsRecordVO.getRecNum();
		try {
			if (buyerNick != null && !"".equals(buyerNick)) {
				if (EncrptAndDecryptClient.isEncryptData(buyerNick, EncrptAndDecryptClient.SEARCH)) {
					smsRecordVO.setBuyerNick(buyerNick);
				} else {
					smsRecordVO.setBuyerNick(
							decryptClient.decryptData(buyerNick, EncrptAndDecryptClient.SEARCH, sessionKey));
				}
			}
			if (phone != null && !"".equals(phone)) {
				if (EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)) {
					smsRecordVO.setRecNum(phone);
				} else {
					smsRecordVO.setRecNum(decryptClient.decryptData(phone, EncrptAndDecryptClient.PHONE, sessionKey));
				}
			}
		} catch (SecretException e) {
			e.printStackTrace();
		}
		smsRecordVO.setUid(uid);
		Integer revertCount = smsRecordDTODao.countSellerRevert(smsRecordVO);
		return revertCount == null ? 0 : revertCount;
	}

	@Override
	public List<Object[]> findSmsSendRecordAndExport(Long uid, SmsRecordVO vo, String accessToken) {
		List<Object[]> dataList = new ArrayList<Object[]>();
		final Map<String, String> DICT_MAP = new HashMap<String, String>() {
			private static final long serialVersionUID = -2106721601599616204L;
			{
				put(MsgType.MSG_XDGH, "下单关怀");
				put(MsgType.MSG_CGCF, "常规催付");
				put(MsgType.MSG_ECCF, "二次催付");
				put(MsgType.MSG_JHSCF, "聚划算催付");
				put(MsgType.MSG_YSCF, "预售催付");
				put(MsgType.MSG_FHTX, "发货提醒");
				put(MsgType.MSG_DDTCTX, "到达同城提醒");
				put(MsgType.MSG_PJTX, "派件提醒");
				put(MsgType.MSG_QSTX, "签收提醒");
				put(MsgType.MSG_YNJTX, "疑难件提醒 ");
				put(MsgType.MSG_YSFHTX, "延时发货提醒 ");
				put(MsgType.MSG_BBGH, "宝贝关怀");
				put(MsgType.MSG_FKGH, "付款关怀");
				put(MsgType.MSG_HKTX, "回款提醒");
				put(MsgType.MSG_TKGH, "退款关怀");
				put(MsgType.MSG_ZDPJ, "自动评价");
				put(MsgType.MSG_PLPJ, "批量评价");
				put(MsgType.MSG_PJJL, "评价记录");
				put(MsgType.MSG_ZCPCK, "中差评查看");
				put(MsgType.MSG_ZCPJK, "中差评监控");
				put(MsgType.MSG_ZCPAF, "中差评安抚");
				put(MsgType.MSG_ZCPTJ, "中差评统计");
				put(MsgType.MSG_ZCPYY, "中差评原因");
				put(MsgType.MSG_ZCPYYSZ, "中差评原因设置");
				put(MsgType.MSG_ZCPYYFX, "中差评原因分析");
				put(MsgType.MSG_SDDDTX, "手动订单提醒");
				put(MsgType.MSG_YXCFAL, "优秀催付案例");
				put(MsgType.MSG_XGTJ, "效果统计");
				put(MsgType.MSG_MJSQTK, "买家申请退款");
				put(MsgType.MSG_TKCG, "退款成功");
				put(MsgType.MSG_DDTH, "等待退货 ");
				put(MsgType.MSG_JJTK, "拒绝退款");
				put(MsgType.MSG_HYDXQF, "会员短信群发");
				put(MsgType.MSG_ZDHMQF, "指定号码群发");
				put(MsgType.MSG_DDDXQF, "订单短信群发");
				put(MsgType.MSG_HYHD, "会员互动");
				put(MsgType.MSG_HPTX, "好评提醒");
				put(MsgType.MSG_CSFS, "测试发送");

			}
		};
		List<SmsRecordDTO> list = smsRecordDTODao.findSmsSendRecordList(vo);
		for (SmsRecordDTO dto : list) {
			if (null != dto.getRecNum() && !"".equals(dto.getRecNum())) {
				try {
					if (EncrptAndDecryptClient.isEncryptData(dto.getRecNum(), EncrptAndDecryptClient.PHONE)) {
						String decrypt = EncrptAndDecryptClient.getInstance().decryptData(dto.getRecNum(),
								EncrptAndDecryptClient.PHONE, accessToken);
						dto.setRecNum(decrypt);
					}
				} catch (Exception e) {
					logger.error("*********短信发送记录**********解密出错，***********查询*********" + e.getMessage());
				}
			}

			if (null != dto.getBuyerNick() && !"".equals(dto.getBuyerNick())) {
				try {
					if (EncrptAndDecryptClient.isEncryptData(dto.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
						String decrypt = EncrptAndDecryptClient.getInstance().decryptData(dto.getBuyerNick(),
								EncrptAndDecryptClient.SEARCH, accessToken);
						dto.setBuyerNick(decrypt);
					}
				} catch (Exception e) {
					logger.error("*********短信发送记录**********解密出错，***********查询*********" + e.getMessage());
				}
			}

			if (null != dto.getType() && !"".equals(dto.getType())) {
				String typeVal = DICT_MAP.get(dto.getType());
				if (null != typeVal && !"".equals(typeVal))
					dto.setType(typeVal);
			}

			if (null != dto.getStatus() && !"".equals(dto.getStatus())) {
				// 发送状态 1：发送失败，2：发送成功，3：手机号码不正确，4：号码重复， 5 ：黑名单， 6 ：重复被屏蔽 /重复发送
				switch (dto.getStatus()) {
				case 1:
					dto.setExportStatus("发送失败");
					break;
				case 2:
					dto.setExportStatus("发送成功");
					break;
				case 3:
					dto.setExportStatus("手机号码不正确");
					break;
				case 4:
					dto.setExportStatus("号码重复");
					break;
				case 5:
					dto.setExportStatus("黑名单");
					break;
				case 6:
					dto.setExportStatus("重复被屏蔽");
					break;
				}

			} // "短信类型","手机号 ","买家昵称","发送时间","短信内容","发送状态","计费（条数）"
				// 添加数据到list
			Date sendTime = dto.getSendTime();
			String formatDate = DateUtils.formatDate(sendTime, DateUtils.DEFAULT_TIME_FORMAT);
			dataList.add(new Object[] { dto.getType(), dto.getRecNum(), dto.getBuyerNick(), formatDate,
					dto.getContent(), dto.getExportStatus(), dto.getActualDeduction() });
		}
		return dataList;
	}

	@Override
	public List<String> findSmsRecordDTOShieldDay(Long uid, Integer sendDay, UserInfo user, List<String> list) {
		Calendar calendar = Calendar.getInstance();
		Date endTime = calendar.getTime();// 获取的是系统当前时间
		calendar.add(Calendar.DATE, -sendDay);
		Date beginTime = calendar.getTime(); // 得到屏蔽几天的日期
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		map.put("list", list);
		List<String> phones = smsRecordDTODao.findSmsRecordDTOShieldDay(map);
		return decryptListPhone(phones, user);
	}

	/**
	 * 查询发送总记录详情
	 */
	@Override
	public Map<String, Object> pageRecordDetail(Long uidLong, SmsRecordVO vo, UserInfo userInfo) {
		if (vo == null || userInfo == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", userInfo.getId());
		map.put("msgId", vo.getMsgId());
		map.put("status", vo.getStatus());
		Integer startRows = (vo.getPageNo() - 1) * ConstantUtils.PAGE_SIZE_MIN;
		map.put("startRows", startRows);
		map.put("pageSize", ConstantUtils.PAGE_SIZE_MIN);
		if (vo.getRecNum() != null && !"".equals(vo.getRecNum())) {
			EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
			try {
				if (EncrptAndDecryptClient.isEncryptData(vo.getRecNum(), EncrptAndDecryptClient.PHONE)) {
					map.put("mobile", vo.getRecNum());
				} else {
					String mobile = decryptClient.encrypt(vo.getRecNum(), EncrptAndDecryptClient.PHONE,
							userInfo.getAccessToken());
					map.put("mobile", mobile);
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		List<SmsRecordDTO> recordList = smsRecordDTODao.listMsgDetail(map);
		// 解密手机号
		if (recordList != null && !recordList.isEmpty()) {
			for (SmsRecordDTO smsRecordDTO : recordList) {
				String recNum = smsRecordDTO.getRecNum();
				if (recNum != null && !"".equals(recNum)) {
					EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
					try {
						if (EncrptAndDecryptClient.isEncryptData(recNum, EncrptAndDecryptClient.PHONE)) {
							String mobile = decryptClient.decrypt(recNum, EncrptAndDecryptClient.PHONE,
									userInfo.getAccessToken());
							smsRecordDTO.setRecNum(mobile);
						}
					} catch (SecretException e) {
						e.printStackTrace();
					}
				}
			}
		}
		Integer recordCount = smsRecordDTODao.countMsgDetail(map);
		Map<String, Object> resultMap = new HashMap<>();

		resultMap.put("recordList", recordList);
		resultMap.put("recordCount", recordCount);

		return resultMap;

	}

	@Override
	public Long findSmsSendRecordCount(Long uid, SmsRecordVO smsRecordVO, String accessToken) {
		return this.smsRecordDTODao.findSendRecordCount(smsRecordVO);
	}

	/**
	 * 解密电话号码！！！
	 */
	private List<String> decryptListPhone(List<String> phones, UserInfo user) {
		List<String> list = new ArrayList<String>();
		if (null != phones && phones.size() > 0) {
			String newToken = user.getAccessToken();
			for (String phone : phones) {
				try {
					if (EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)) {
						String decrypt = EncrptAndDecryptClient.getInstance().decryptData(phone,
								EncrptAndDecryptClient.PHONE, newToken);
						list.add(decrypt);
					} else {
						list.add(phone);
					}
				} catch (Exception e) {
					try {
						newToken = userInfoService.findUserTokenById(user.getId());
						if (EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)) {
							String decrypt = EncrptAndDecryptClient.getInstance().decryptData(phone,
									EncrptAndDecryptClient.PHONE, newToken);
							list.add(decrypt);
						} else {
							list.add(phone);
						}
					} catch (Exception e1) {
						logger.error("=========指定号码群发=======" + user.getTaobaoUserNick() + "=========解密电话号码失败"
								+ e1.getMessage());
						continue;
					}
				}
			}
		}
		return list;
	}

	@Override
	public Boolean removeRecordById(Long uid, Long recordId) {
		if (uid == null || recordId == null) {
			return false;
		}
		try {
			smsRecordDTODao.updateRecordIsShow(uid, recordId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 根据发送总记录id查询实际扣费条数
	 */
	@Override
	public Long sumDeductionById(Long uid, Long msgId) {
		if (uid == null || msgId == null) {
			return 0L;
		}
		Long deduction = this.smsRecordDTODao.sumDeductionById(uid, msgId);
		if (deduction == null) {
			return 0L;
		}
		return deduction;
	}

	/** ================== 封装查询参数====================== */
	private void packagingData(SmsRecordVO vo, String accessToken) {

		if (null != vo.getRecNum() && !"".equals(vo.getRecNum())) {
			vo.setRecNum(encryptSearchMobiles(vo.getRecNum(), accessToken));
		}
		if (null != vo.getBuyerNick() && !"".equals(vo.getBuyerNick())) {
			vo.setBuyerNick(encryptSearchNick(vo.getBuyerNick(), accessToken));
		}

		if (null != vo.getParameters() && !"".equals(vo.getParameters())) {
			if (MobileRegEx.validateMobile(vo.getParameters())) {
				String phone = encryptSearchMobiles(vo.getParameters(), accessToken);
				/* 手机号码 */
				vo.setRecNum(phone);
			}
			/* 买家昵称 */
			String buyer = encryptSearchNick(vo.getParameters(), accessToken);
			vo.setBuyerNick(buyer);
			/* 订单编号 */
			vo.setOrderId(vo.getParameters());

		}
	}

	/** 加密手机号 */
	private String encryptSearchMobiles(String mobile, String accessToken) {
		try {
			String encryptPhone = EncrptAndDecryptClient.getInstance().encryptData(mobile, EncrptAndDecryptClient.PHONE,
					accessToken);
			return encryptPhone;
		} catch (Exception e) {
			logger.error("*********短信发送记录**********加密出错，***********查询*********" + e.getMessage());
		}
		return mobile;
	}

	/** 昵称加密 */
	private String encryptSearchNick(String nick, String accessToken) {
		try {
			String encryptName = EncrptAndDecryptClient.getInstance().encryptData(nick, EncrptAndDecryptClient.SEARCH,
					accessToken);
			return encryptName;
		} catch (Exception e) {
			logger.error("*********短信发送记录**********加密出错，***********查询*********" + e.getMessage());
		}
		return nick;
	}

	/**
	 * 批量:保存过滤的手机号(号码重复,手机号不正确...)
	 */
	@Override
	public void saveErrorMsgNums(Long uid, List<String> errorNums, TradeMessageVO messageVO, Integer status,
			Date startTime) {
		List<SmsRecordDTO> smsRecordDTOList = null;
		try {
			if (errorNums != null && errorNums.size() > 0) {
				smsRecordDTOList = new ArrayList<SmsRecordDTO>();
				SmsRecordDTO record = null;
				for (String str : errorNums) {
					record = new SmsRecordDTO();
					if (str == null || "".equals(str)) {
						continue;
					}
					String[] phoneArr = str.split(Constants.SMSSEPARATOR_S);
					if (phoneArr != null && phoneArr.length > 0) {
						if (phoneArr.length == 3) {
							record.setRecNum(phoneArr[0]);
							record.setContent(messageVO.getContent());
							record.setBuyerNick(phoneArr[1]);
							record.setOrderId(phoneArr[2]);
						} else if (phoneArr.length == 4) {
							record.setRecNum(phoneArr[0]);
							record.setContent(phoneArr[1]);
							record.setBuyerNick(phoneArr[2]);
							record.setOrderId(phoneArr[3]);
						} else {
							record.setRecNum(str);
							record.setContent(messageVO.getContent());
						}
					}
					record.setUid(messageVO.getUid());
					record.setUserId(messageVO.getUserId());
					record.setMsgId(messageVO.getMsgId());
					record.setActualDeduction(0);// 错误的短信或者 没有发送出去的的短信
													// 记录实际扣费数为0条
					record.setType(messageVO.getMsgType());
					record.setStatus(status);

					record.setSendTime(startTime);
					record.setSource("2");
					record.setChannel(messageVO.getAutograph());
					record.setAutograph(messageVO.getAutograph());
					record.setShow(true);
					smsRecordDTOList.add(record);
				}
				// saveSmsRecord(smsRecordDTOList, sendMsgVo.getUserId());
				this.doCreaterSmsRecordDTOByList(messageVO.getUid(), smsRecordDTOList);
			}
		} catch (Exception e) {
			logger.info("订单短信群发时保存错误手机号异常：{}", e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public Long findSmsRecordDTOFrontDayCount(Long uid) {
		return smsRecordDTODao.findSmsRecordDTOFrontDayCount(uid);
	}

	@Override
	public Long findSmsRecordDTOReminderCount(Long uid) {
		return smsRecordDTODao.findSmsRecordDTOReminderCount(uid);
	}

	/**
	 * 通过号码和屏蔽天数查询数据是否存在
	 * 
	 * @time 2018年9月15日 上午11:41:08
	 * @return
	 */
	@Override
	public boolean findShieldByPhoneAndDay(Long uid, Integer shieldDay, String phone) {
		if (null != shieldDay && shieldDay > 0) {
			Calendar calendar = Calendar.getInstance();
			Date endTime = calendar.getTime();// 获取的是系统当前时间
			calendar.add(Calendar.DATE, -shieldDay);
			Date beginTime = calendar.getTime(); // 得到屏蔽几天的日期
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("uid", uid);
			map.put("phone", phone);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			Integer i = smsRecordDTODao.findShieldByPhoneAndDay(map);
			if (null != i && i > 0)
				return true;
		}
		return false;
	}

	@Override
	public void deleteAllSmsRecordDTO(Long uid) {
		this.smsRecordDTODao.deleteAllSmsRecordDTO(uid);
	}

	/**
	 * 查询导出count
	 * 
	 * @time 2018年9月17日 下午12:08:54
	 * @return
	 */
	@Override
	public Long findSmsSendRecordAndExportCount(Long id, SmsRecordVO vo) {
		Long i = smsRecordDTODao.findSendRecordCount(vo);
		if (null == i)
			i = 0L;
		return i;
	}

	@Override
	public void batchUpdateSmsRecordSendAndReceiveTime(Long uid) throws Exception {
		this.smsRecordDTODao.batchUpdateSmsRecordSendAndRecieveDate(uid);
	}

	/**
	 * 按照日期分页查询账单
	 */
	@Override
	public List<SmsRecordDTO> limitReportListByDate(Long uid, String dateType, Date bTime, Date eTime, Integer pageNo) {
		if (uid == null) {
			return null;
		}
		Integer startRows = (pageNo - 1) * ConstantUtils.PAGE_SIZE_MIDDLE;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		map.put("startRows", startRows);
		map.put("pageSize", ConstantUtils.PAGE_SIZE_MIDDLE);
		redisLockService.putStringValueWithExpireTime("REPORT_QUERY_MAP" + uid, JsonUtil.toJson(map), TimeUnit.HOURS,
				1L);
		List<SmsRecordDTO> reportList = this.smsRecordDTODao.listReportByDate(map);
		return reportList;
	}

	/**
	 * 按照日期分页查询账单总记录数
	 */
	@Override
	public Integer countReportByDate(Long uid, String dateType, Date bTime, Date eTime) {
		if (uid == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		Integer reportCount = this.smsRecordDTODao.countReportByDate(map);
		return reportCount;
	}

	/**
	 * 按照日期查询账单
	 */
	@Override
	public List<SmsRecordDTO> listReportList(Long uid, String dateType, Date bTime, Date eTime) {
		if (uid == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		redisLockService.putStringValueWithExpireTime("REPORT_QUERY_MAP" + uid, JsonUtil.toJson(map), TimeUnit.HOURS,
				1L);
		List<SmsRecordDTO> reportList = this.smsRecordDTODao.listReportByDate(map);
		return reportList;
	}

	/**
	 * 计算所有短信消费条数
	 */
	@Override
	public Long sumReportSmsNum(Long uid, String dateType, Date bTime, Date eTime) throws Exception {
		if (uid == null) {
			return 0L;
		}
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		Long totalNum = this.smsRecordDTODao.sumReportSmsNum(map);
		return totalNum;
	}

	@Override
	public Map<String, SmsRecordDTO> sumReportSmsNumByDate(Long uid, String dateType, Date bTime, Date eTime)
			throws Exception {
		if (uid == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		List<SmsRecordDTO> smsRecordDTOList = this.smsRecordDTODao.sumReportSmsNumByDate(map);
		Map<String, SmsRecordDTO> resultMap = new HashMap<String, SmsRecordDTO>();
		for (int i = 0; i < smsRecordDTOList.size(); i++) {
			if ("day".equals(dateType)) {
				resultMap.put(smsRecordDTOList.get(i).getDisplayDate(), smsRecordDTOList.get(i));
			} else if ("month".equals(dateType)) {
				resultMap.put(smsRecordDTOList.get(i).getDisplayDate(), smsRecordDTOList.get(i));
			} else {
				resultMap.put(smsRecordDTOList.get(i).getDisplayDate(), smsRecordDTOList.get(i));
			}
		}
		return resultMap;
	}

	@Override
	public List<String> queryTidList(Long uid, SmsRecordVO smsRecordVO) {
		if (uid == null || smsRecordVO == null) {
			return null;
		}
		List<String> tids = this.smsRecordDTODao.queryTidList(smsRecordVO);
		return tids;
	}

	@Override
	public List<MessageBill> findMessageReportByDate(Long uid, String dateType, Date bTime, Date eTime, Integer pageNo,
			Integer reportSize) throws Exception {
		if (uid == null) {
			return null;
		}
		Integer dateRange = 10;
		ExecutorService threadPool = Executors.newFixedThreadPool(dateRange,
				new PidThreadFactory("GetMessageBillThread", true));
		Map<Integer, MessageBill> treeMap = new TreeMap<Integer, MessageBill>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});
		List<GetMessageBillByDate> threadList = new ArrayList<GetMessageBillByDate>(dateRange);
		List<Future<MessageBill>> futureList = new ArrayList<Future<MessageBill>>(dateRange);
		String selectDate = null;
		Integer baseDate = 0;
		Integer reportRange = 0;
		if (pageNo == 1) {
			baseDate = pageNo * dateRange - dateRange;
			reportRange = reportSize < dateRange ? reportSize : pageNo * dateRange;
		} else {
			baseDate = pageNo * dateRange - dateRange;
			reportRange = (reportSize - (pageNo * dateRange - dateRange)) < dateRange ? reportSize : pageNo * dateRange;
		}
		Date startTime = null;
		Date endTime = null;
		for (int i = baseDate; i < reportRange; i++) {
			if (i == baseDate && pageNo == 1) {
				if ("day".equals(dateType)) {
					startTime = DateUtils.nDaysStartTime(i, eTime == null ? new Date() : eTime);
					endTime = DateUtils.nDaysEndTime(i, eTime == null ? new Date() : eTime);
					selectDate = DateUtils.formatDate(startTime, "yyyy-MM-dd");
				} else if ("month".equals(dateType)) {
					startTime = DateUtils.getFirstDayOfMonth(i, eTime == null ? new Date() : eTime);
					endTime = DateUtils.getLastDayOfMonth(i, eTime == null ? new Date() : eTime);
					selectDate = DateUtils.formatDate(startTime, "yyyy-MM");
				} else {
					startTime = DateUtils.getFirstDayOfYear(i, eTime == null ? new Date() : eTime);
					endTime = DateUtils.getLastDayOfYear(i, eTime == null ? new Date() : eTime);
					selectDate = DateUtils.formatDate(startTime, "yyyy");
				}
			} else {
				if ("day".equals(dateType)) {
					startTime = DateUtils.nDaysStartTime(i, eTime == null ? new Date() : eTime);
					endTime = DateUtils.nDaysEndTime(i, eTime == null ? new Date() : eTime);
					selectDate = DateUtils.formatDate(startTime, "yyyy-MM-dd");
				} else if ("month".equals(dateType)) {
					startTime = DateUtils.getFirstDayOfMonth(i, eTime == null ? new Date() : eTime);
					endTime = DateUtils.getLastDayOfMonth(i, eTime == null ? new Date() : eTime);
					selectDate = DateUtils.formatDate(startTime, "yyyy-MM");
				} else {
					startTime = DateUtils.getFirstDayOfYear(i, eTime == null ? new Date() : eTime);
					endTime = DateUtils.getLastDayOfYear(i, eTime == null ? new Date() : eTime);
					selectDate = DateUtils.formatDate(startTime, "yyyy");
				}
			}
			if ("day".equals(dateType) || "month".equals(dateType)) {
				treeMap.put(new Integer(selectDate.replace("-", "")), null);
			} else {
				treeMap.put(new Integer(selectDate), null);
			}
			// threadList.add(new GetMessageBillByDate(uid, messageBillService,
			// userRechargeService,
			// multiShopBindingSendMessageRecordService, userAccountService,
			// selectDate, startTime, endTime));
		}
		// for (int i = 0; i < threadList.size(); i++) {
		// futureList.add(threadPool.submit(threadList.get(i)));
		// }
		List<MessageBill> messageBillList = new ArrayList<MessageBill>(dateRange);
		MessageBill messageBill = null;
		for (int i = 0; i < threadList.size(); i++) {
			messageBill = futureList.get(i).get();
			if ("day".equals(dateType) || "month".equals(dateType)) {
				treeMap.put(new Integer(messageBill.getSendDate().replace("-", "")), messageBill);
			} else {
				treeMap.put(new Integer(messageBill.getSendDate()), messageBill);
			}
		}
		for (Entry<Integer, MessageBill> result : treeMap.entrySet()) {
			messageBillList.add(result.getValue());
		}
		threadPool.shutdown();
		return messageBillList;
	}

	// @Override
	// public List<MessageBill> findMessageReportByDate(Long uid, String
	// dateType, Date bTime, Date eTime, Integer pageNo,
	// Integer reportSize) throws Exception {
	// if (uid == null) {
	// return null;
	// }
	// ExecutorService threadPool = Executors.newFixedThreadPool(1,
	// new PidThreadFactory("GetMessageBillThread", true));
	// Future<List<MessageBill>> future = threadPool.submit(new
	// GetMessageBillByDate(uid, this, userRechargeService,
	// multiShopBindingSendMessageRecordService, userAccountService, dateType,
	// bTime, eTime));
	// List<MessageBill> messageBillList = future.get();
	//
	// return messageBillList;
	// }

}
