package com.kycrm.member.service.message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.message.ISmsSendListImportDao;
import com.kycrm.member.domain.entity.message.SmsSendListImport;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.SmsSendListImportVO;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.GzipUtil;

@Service("smsSendListImportService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class SmsSendListImportServiceImpl implements ISmsSendListImportService {
	private Logger logger = LoggerFactory
			.getLogger(SmsSendListImportServiceImpl.class);
	@Autowired
	private ISmsSendListImportDao smsSendListImportDao;
	@Autowired
	private IUserInfoService userInfoService;

	@Override
	public List<String> findImportPhoneByteById(Long id) {
		SmsSendListImport sendList = smsSendListImportDao
				.findImportPhoneByteById(id);
		List<String> phones = new ArrayList<String>();
		if(null != sendList){
			byte[] phoneByte = sendList.getImportPhoneByte();
			String smsPhone = null;
			try {
				smsPhone = GzipUtil.uncompressToString(phoneByte);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != smsPhone && smsPhone.length() > 0) {
				String[] split = smsPhone.split(",");
				phones.addAll(Arrays.asList(split));
			}
		}
		return phones;
	}

	@Override
	public Long saveSmsSendListImport(SmsSendListImport sendListImport) {
		smsSendListImportDao.saveSmsSendListImport(sendListImport);
		return sendListImport.getId();
	}

	@Override
	public void updateSmsSendListImportById(SmsSendListImport smsSendlistImport) {
		smsSendListImportDao.updateSmsSendListImportById(smsSendlistImport);
	}

	@Override
	public List<SmsSendListImport> findSmsSendLists(SmsSendListImportVO vo) {
		return smsSendListImportDao.findSmsSendLists(vo);
	}

	@Override
	public boolean deleteSmsSendListById(Long recordId) {
		Long i = smsSendListImportDao.deleteSmsSendListById(recordId);
		if (i > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 次方法有坑只执行一次---------------------------请勿调用------------------------------
	 */
	@Override
	public void disposeImportPhoneToByte(Long uid) {
		List<SmsSendListImport> list = smsSendListImportDao.findSmsSendListImport(uid);
		logger.info("**********crm_sms_sendlist_import，将电话号码压缩******查询出数量："+list.size());
		
		for (SmsSendListImport s : list) {
			logger.info("**********crm_sms_sendlist_import，将电话号码压缩*****uid:"+s.getUid()+"正在压缩。。。。。。开始");
			
			String sendPhones = smsSendListImportDao.findImportPhoneById(s.getId());
			if (null != sendPhones && !"".equals(sendPhones)) {
				try {
					Long userid = s.getUid();
					if (null == userid) {
						logger.info("**********crm_sms_sendlist_import，将电话号码压缩*****uid:"+s.getUid()+"*****uid为空！！！！！！结束");
						continue;
					}

					UserInfo user = userInfoService.findUserInfo(userid);
					if(user.getExpirationTime().before(new Date())){
						logger.info("**********crm_sms_sendlist_import，将电话号码压缩*****uid:"+s.getUid()+"*****用户已过期！！！！！！跳过");
						continue;
					}
						
						
					String[] split = sendPhones.split(",");
					List<String> decryptPhones = decryptListPhone(split, user);// 解密电话号码
					if (null != decryptPhones && decryptPhones.size() > 0) {
						String phones = StringUtils.join(decryptPhones, ",");
						byte[] compress = GzipUtil.compress(phones);
						if (compress != null) {
							s.setImportPhoneByte(compress);
							Integer i = smsSendListImportDao.disposeImportPhoneSaveByte(s);
							if (null != i && i > 0) {
								logger.info("**********crm_sms_sendlist_import，将电话号码压缩*****uid:"+s.getUid()+"*****压缩更新成功√√√√√√√√√√√√总条数："+decryptPhones.size());
							} else {
								logger.info("**********crm_sms_sendlist_import，将电话号码压缩*****uid:"+s.getUid()+"*****压缩更新失败！！！！！！结束");
							}
						} else {
							logger.info("**********crm_sms_sendlist_import，将电话号码压缩*****uid:"+s.getUid()+"*****压缩失败！！！！！！结束");
						}
					} else {
						logger.info("**********crm_sms_sendlist_import，将电话号码压缩*****uid:"+s.getUid()+"*****解密失败！！！！！！结束");
					}
				} catch (Exception e) {
					logger.info("**********crm_sms_sendlist_import，将电话号码压缩*****uid:"+s.getUid()+"*****正在压缩异常！！！！！！结束"+e.getMessage());
				}
			} else {
				logger.info("**********crm_sms_sendlist_import，将电话号码压缩*****uid:"+s.getUid()+"*****号码为空！！！！！！结束");
			}
		}
		
		logger.info("**********crm_sms_sendlist_import，将电话号码压缩*****全部压缩执行完毕√√√√√√√√√√√√√√√√√√√√√√√√");
	}

	/**
	 * 解密电话号码！！！
	 */
	private List<String> decryptListPhone(String[] phones, UserInfo user) {
		List<String> list = new ArrayList<String>();
		if (null != phones && phones.length > 0) {
			for (String phone : phones) {
				try {
					if (EncrptAndDecryptClient.isEncryptData(phone,EncrptAndDecryptClient.PHONE)) {
						String decrypt = EncrptAndDecryptClient.getInstance().decryptData(phone,EncrptAndDecryptClient.PHONE, user.getAccessToken());
						list.add(decrypt);
					} else {
						list.add(phone);
					}
				} catch (Exception e) {
					return null;
				}
			}
		}
		return list;
	}
}
