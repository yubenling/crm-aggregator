package com.kycrm.member.service.message;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.message.ISmsReceiveInfoDao;
import com.kycrm.member.domain.entity.message.SmsReceiveInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.receive.ReceiveInfoVO;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.taobao.api.SecretException;
@MyDataSource(MyDataSourceAspect.MASTER)
@Service("smsReceiveInfoService")
public class SmsReceiveInfoServiceImpl implements ISmsReceiveInfoService {

	private Logger logger = LoggerFactory.getLogger(SmsReceiveInfoServiceImpl.class);

	@Autowired
	private ISmsReceiveInfoDao receiveInfoDao;

	/**
	 * 买家回复内容查询列表
	 */
	@Override
	public List<SmsReceiveInfo> listReceiveInfoLimit(Long uid, ReceiveInfoVO receiveInfoVO,UserInfo userInfo) {
		Map<String, Object> queryMap = new HashMap<>();
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		Integer pageNo = null;
		queryMap.put("uid", uid);
		if (receiveInfoVO != null) {
			if (receiveInfoVO.getbTime() != null && !"".equals(receiveInfoVO.getbTime())) {
				queryMap.put("bTime", DateUtils.convertDate(receiveInfoVO.getbTime()));
			}
			if (receiveInfoVO.geteTime() != null && !"".equals(receiveInfoVO.geteTime())) {
				queryMap.put("eTime", DateUtils.convertDate(receiveInfoVO.geteTime()));
			}
		    try {
			    if(receiveInfoVO.getSendPhone() != null && !"".equals(receiveInfoVO.getSendPhone())){
			    	if(EncrptAndDecryptClient.isEncryptData(receiveInfoVO.getSendPhone(), EncrptAndDecryptClient.PHONE)){
			    		queryMap.put("phone", receiveInfoVO.getSendPhone());
			    	}else {
			    		queryMap.put("phone", decryptClient.encryptData(receiveInfoVO.getSendPhone(), EncrptAndDecryptClient.PHONE,userInfo.getAccessToken()));
					}
			    }
				if (receiveInfoVO.getBuyerNick() != null && !"".equals(receiveInfoVO.getBuyerNick())) {
					if(EncrptAndDecryptClient.isEncryptData(receiveInfoVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
						queryMap.put("buyerNick", receiveInfoVO.getBuyerNick());
					}else {
						queryMap.put("buyerNick", decryptClient.encryptData(receiveInfoVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH,userInfo.getAccessToken()));
					}
				}
			} catch (SecretException e) {
				 e.printStackTrace();
			}
			queryMap.put("containN", receiveInfoVO.getContainN());
			pageNo = receiveInfoVO.getPageNo();
		}
		if (pageNo == null) {
			pageNo = 1;
			receiveInfoVO.setPageNo(pageNo);
		}
		Integer startRows = (pageNo - 1) * Constants.PAGE_SIZE_MIDDLE;
		queryMap.put("startRows", startRows);
		queryMap.put("pageSize", Constants.PAGE_SIZE_MIDDLE);
		List<SmsReceiveInfo> receiveInfoList = receiveInfoDao.listReceiveInfoLimit(queryMap);
		if(receiveInfoList != null && !receiveInfoList.isEmpty()){
		SmsReceiveInfo receiveInfo = null;
		for (int i = 0; i < receiveInfoList.size(); i++) {
			receiveInfo = receiveInfoList.get(i);
			if(receiveInfo.getContent() != null && !"".equals(receiveInfo.getContent())){
				try {
					receiveInfo.setContent(URLDecoder.decode(receiveInfo.getContent(), "utf-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			try {
				if(EncrptAndDecryptClient.isEncryptData(receiveInfo.getBuyerNick(),EncrptAndDecryptClient.SEARCH)){
					receiveInfo.setBuyerNick(decryptClient.decryptData(receiveInfo.getBuyerNick(),
					EncrptAndDecryptClient.SEARCH, userInfo.getAccessToken()));
				}
				if(EncrptAndDecryptClient.isEncryptData(receiveInfo.getReceivePhone(),EncrptAndDecryptClient.PHONE)){
					receiveInfo.setReceivePhone(decryptClient.decryptData(receiveInfo.getReceivePhone(),
					EncrptAndDecryptClient.PHONE, userInfo.getAccessToken()));
				}
				if(EncrptAndDecryptClient.isEncryptData(receiveInfo.getSendPhone(), EncrptAndDecryptClient.PHONE)){
					receiveInfo.setSendPhone(decryptClient.decryptData(receiveInfo.getSendPhone(), 
					EncrptAndDecryptClient.PHONE, userInfo.getAccessToken()));
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		}
		return receiveInfoList;
	}

	/**
	 * 买家回复内容查询列表个数
	 */
	@Override
	public Long countReceiveInfo(Long uid, ReceiveInfoVO receiveInfoVO,UserInfo userInfo) {
		Map<String, Object> queryMap = new HashMap<>();
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		queryMap.put("uid", uid);
		if (receiveInfoVO != null) {
			if (receiveInfoVO.getbTime() != null && !"".equals(receiveInfoVO.getbTime())) {
				queryMap.put("bTime", DateUtils.convertDate(receiveInfoVO.getbTime()));
			}
			if (receiveInfoVO.geteTime() != null && !"".equals(receiveInfoVO.geteTime())) {
				queryMap.put("eTime", DateUtils.convertDate(receiveInfoVO.geteTime()));
			}
		    try {
			    if(receiveInfoVO.getSendPhone() != null && !"".equals(receiveInfoVO.getSendPhone())){
			    	if(EncrptAndDecryptClient.isEncryptData(receiveInfoVO.getSendPhone(), EncrptAndDecryptClient.PHONE)){
			    		queryMap.put("phone", receiveInfoVO.getSendPhone());
			    	}else {
			    		queryMap.put("phone", decryptClient.encryptData(receiveInfoVO.getSendPhone(), EncrptAndDecryptClient.PHONE,userInfo.getAccessToken()));
					}
			    }
				if (receiveInfoVO.getBuyerNick() != null && !"".equals(receiveInfoVO.getBuyerNick())) {
					if(EncrptAndDecryptClient.isEncryptData(receiveInfoVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
						queryMap.put("buyerNick", receiveInfoVO.getBuyerNick());
					}else {
						queryMap.put("buyerNick", decryptClient.encryptData(receiveInfoVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH,userInfo.getAccessToken()));
					}
				}
			} catch (SecretException e) {
				 e.printStackTrace();
			}
			queryMap.put("containN", receiveInfoVO.getContainN());
		}
		Long totalCount = receiveInfoDao.countReceiveInfo(queryMap);
		return totalCount == null ? 0:totalCount;
	}

	/**
	 * 根据手机号查询买家回复内容
	 */
	@Override
	public List<ReceiveInfoVO> listReceiveInfoByPhone(Long uid, String sessionKey, String phone) {
		if (uid == null || phone == null) {
			return null;
		}
		 EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("uid", uid);
		 try {
		// ##### 临时使用 #####//
//		queryMap.put("phone", phone);
		// ##### 临时使用 #####//

		 if (EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)) {
			 queryMap.put("phone", phone);
		 } else {
			 queryMap.put("phone", decryptClient.encryptData(phone, EncrptAndDecryptClient.PHONE, sessionKey));
		 }
		 } catch (SecretException e) {
			 logger.info("会员互动=====>>>>>查询买家回复记录,解密异常!");
			 e.printStackTrace();
		 }
		List<SmsReceiveInfo> receiveInfos = receiveInfoDao.listReceiveInfoByPhone(queryMap);
		List<ReceiveInfoVO> receiveVOs = new ArrayList<>();
		if (receiveInfos != null && !receiveInfos.isEmpty()) {
			for (SmsReceiveInfo smsReceiveInfo : receiveInfos) {
				ReceiveInfoVO receiveInfoVO = new ReceiveInfoVO();
				try {
					receiveInfoVO.setReceiveDate(smsReceiveInfo.getReceiveDate());
					if (smsReceiveInfo.getContent() != null && !"".equals(smsReceiveInfo.getContent())) {
						receiveInfoVO.setContent(URLDecoder.decode(smsReceiveInfo.getContent(), "utf-8"));
					} else {
						receiveInfoVO.setContent("");
					}
					receiveVOs.add(receiveInfoVO);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}

		}
		return receiveVOs;
	}

	@Override
	public Boolean deleteRecord(Long uid, Long receiveId) throws Exception {
		if(uid == null || receiveId == null){
			return false;
		}
		Integer dataCount = this.receiveInfoDao.updateReceiveIsShow(receiveId);
		if(dataCount < 1){
			return false;
		}
		return true;
	}

//	@Override
//	public long countReceiveInfo(ReceiveInfoVO receiveInfoVO, UserInfo userInfo) {
//		return this.countReceiveInfo(userInfo.getId(),userInfo.getId(), receiveInfoVO);
//	}

	/**
	 * 添加单条记录
	 */
	@Override
	public void saveReceiverInfo(SmsReceiveInfo smsReceiveInfo) {
		this.receiveInfoDao.saveReceiverInfo(smsReceiveInfo);
	}

}
