package com.kycrm.syn.service.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.message.SmsBlackListDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.SmsBlackListVO;
import com.kycrm.member.exception.TaobaoSecretException;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.dao.message.ISmsBlackListDTODao;
import com.kycrm.syn.service.user.JudgeUserUtil;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.MobileRegEx;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.SecretException;

/**
* @ClassName: SmsBlackListService
* @author:jackstraw_yu
* @date 2017年2月22日
*
*/
@MyDataSource
@Service("smsBlackListDTOService")
public class SmsBlackListDTOServiceImpl implements ISmsBlackListDTOService{
	private static final Log logger = LogFactory.getLog(SmsBlackListDTOServiceImpl.class);
	@Autowired
	private ISmsBlackListDTODao smsBlackListDao;
	@Autowired
	private JudgeUserUtil judgeUserUtil;
//	@Autowired
//	private ISmsBlackListImportService smsBlackListImportService;
	@Resource(name = "userInfoServiceDubbo")
	private IUserInfoService userInfoService;
	
	/**
	 * 中差评通过昵称查询是否存在黑名单
	 * true 通过昵称存在
	 * false 通过
	 */
	@Override
    public boolean isExists(Long uid, String sellerName, String buyerNick, String buyerPhone) {
        try {
            if(ValidateUtil.isEmpty(uid)){
                return false;
            }
            String session = this.judgeUserUtil.getUserTokenByRedis(uid);
            if(session == null){
                return true;
            }
            if(!EncrptAndDecryptClient.isEncryptData(buyerNick, EncrptAndDecryptClient.SEARCH)){
                buyerNick = EncrptAndDecryptClient.getInstance().encrypt(buyerNick, EncrptAndDecryptClient.SEARCH, session);
            }
            if(!EncrptAndDecryptClient.isEncryptData(buyerPhone, EncrptAndDecryptClient.PHONE)){
                buyerPhone = EncrptAndDecryptClient.getInstance().encrypt(buyerPhone, EncrptAndDecryptClient.PHONE, session);
            }
            Map<String,Object> map = new HashMap<String,Object>(5);
            map.put("uid", uid);
            map.put("nick", buyerNick);
			if (buyerPhone != null) {
				map.put("phone", buyerPhone);
			}        
			//过滤黑名单
			if (this.smsBlackListDao.findExistsByNick(map) == 0) {
				return false;
			} else {
				return true;
			}
        } catch (SecretException e) {
            e.printStackTrace();
            throw new TaobaoSecretException("加解密异常："+e.getMessage());
        }
    }

	@Override
	public List<String> findBlackPhones(Long uid,UserInfo user) {
		List<String> phones = smsBlackListDao.findBlackPhones(uid);
		return decryptListPhone(phones, user);
	}

	@Override
	public Map<String, Object> findSmsBlackListPage(Long uid,
			SmsBlackListVO vo, String accessToken) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(vo.getNickOrPhone()!=null){
				if(MobileRegEx.validateMobile(vo.getNickOrPhone())){
					try {
						String encryptPhone = EncrptAndDecryptClient.getInstance().encryptData(vo.getNickOrPhone(), EncrptAndDecryptClient.PHONE,accessToken);
						vo.setNickOrPhone(encryptPhone);
					} catch (Exception e) {
						logger.error("*********黑名单管理**********加密出错，**********************"+e.getMessage());
					}
				}else{
					try {
						String encryptName = EncrptAndDecryptClient.getInstance().encryptData(vo.getNickOrPhone(), EncrptAndDecryptClient.SEARCH, accessToken);
						vo.setNickOrPhone(encryptName);
					} catch (Exception e) {
						logger.error("*********黑名单管理**********加密出错，**********************"+e.getMessage());
					}
				}
			
		}
		Long totalCount = this.smsBlackListDao.findSmsBlackCount(vo);
		if(null == totalCount)
			totalCount = 0L;
		Integer totalPage = (int) Math.ceil(1.0*totalCount/vo.getCurrentRows());
		List<SmsBlackListDTO> list= this.smsBlackListDao.findSmsBlackList(vo);
		for (SmsBlackListDTO smsBlackList : list) {
				if("1".equals(smsBlackList.getType())){
					try {
						String decryptPhone = EncrptAndDecryptClient.getInstance().decryptData(smsBlackList.getNickOrPhone(), EncrptAndDecryptClient.PHONE, accessToken);
						smsBlackList.setNickOrPhone(decryptPhone);
					} catch (Exception e) {
						logger.error("********黑名单管理*******解密出错，**********************"+e.getMessage());
					}
				}else{
					try {
						String decryptName = EncrptAndDecryptClient.getInstance().decryptData(smsBlackList.getNickOrPhone(), EncrptAndDecryptClient.SEARCH, accessToken);
						smsBlackList.setNickOrPhone(decryptName);
					} catch (Exception e) {
						logger.error("*********黑名单管理******解密出错，**********************"+e.getMessage());
					}
				}
		}
		map.put("totalPage", totalPage);
		map.put("list", list);
		return map;
	}

	/**
	 * 删除黑名单数据-----此方法有坑请勿调用！！！
	 * @author HL
	 * @time 2018年3月6日 下午2:56:46 
	 * @param vo
	 * @return
	 */
	@Override
	public Map<String,Object> deleteSmsBlack(Long uid,SmsBlackListVO vo) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> buyerNicks = new ArrayList<String>();
		map.put("status", false);
		map.put("content", buyerNicks);
		try {
			if(null != vo.getPhoneIds() && vo.getPhoneIds().size()>0){
				buyerNicks = smsBlackListDao.findSmsBlackListNick(vo);
				long i = smsBlackListDao.deleteSmsBlack(vo);
				if(i>0){
					map.put("status", true);
					map.put("content", buyerNicks);
				}
			}else if("black-del-all".equals(vo.getType())){
				buyerNicks = smsBlackListDao.findSmsBlackListNick(vo);
				long i = smsBlackListDao.deleteSmsBlack(vo);
				if(i>0){
					map.put("status", true);
					map.put("content", buyerNicks);
				}
			}
		} catch (Exception e) {
			logger.error("=========黑名单管理===========删除失败==="+vo.getUid()+"====="+e.getMessage());
		}
		return map;
	}

	/**
	 * 判断单个手机号是否在黑名单
	 */
	@Override
	public boolean phoneIsBlack(Long uid, String phone) {
		Map<String, Object> map = new HashMap<>();
		map.put("phone", phone);
		map.put("uid", uid);
		Integer count = smsBlackListDao.findExistsByPhone(map);
		if(count != null && count > 0){
			return true;
		}
		return false;
	}
	
	@Override
	public long insertSmsBlackList(Long uid, Map<String, Object> map) {
		return smsBlackListDao.insertSmsBlackList(map);
	}
	
	/**
	 * 解密电话号码！！！
	 */
	private List<String> decryptListPhone(List<String> phones,UserInfo user) {
		List<String> list = new ArrayList<String>();
		if(null != phones && phones.size()>0){
			String newToken = user.getAccessToken();
			for (String phone : phones) {
				try {
					if(EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)){
						String decrypt = EncrptAndDecryptClient.getInstance().decryptData(phone, EncrptAndDecryptClient.PHONE,newToken);
						list.add(decrypt);
					}else{
						list.add(phone);
					}
				} catch (Exception e) {
					try {
						newToken = userInfoService.findUserTokenById(user.getId());
						if(EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)){
							String decrypt = EncrptAndDecryptClient.getInstance().decryptData(phone, EncrptAndDecryptClient.PHONE,newToken);
							list.add(decrypt);
						}else{
							list.add(phone);
						}
					} catch (Exception e1) {
						logger.error("=========指定号码群发======="+user.getTaobaoUserNick()+"=========解密电话号码失败"+e1.getMessage());
						continue;
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 保存单条记录
	 */
	@Override
	public Boolean saveBlackListDTO(Long uid,SmsBlackListDTO blackListDTO){
		if(uid == null){
			return false;
		}
		try {
			this.smsBlackListDao.saveBlackList(blackListDTO);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<String> findBlackNick(Long uid, UserInfo user) {
		List<String> blackNicks = smsBlackListDao.findBlackNick(uid);
		return decryptListNick(blackNicks, user);
	}
	
    /**
     * 解密昵称集合
     * @param blackNicks
     * @param user
     * @return
     */
	private List<String> decryptListNick(List<String> blackNicks, UserInfo user) {
		List<String> list = new ArrayList<String>();
		if(null != blackNicks && blackNicks.size()>0){
			String newToken = user.getAccessToken();
			for (String nick : blackNicks) {
				try {
					if(EncrptAndDecryptClient.isEncryptData(nick, EncrptAndDecryptClient.SEARCH)){
						String decrypt = EncrptAndDecryptClient.getInstance().decryptData(nick, EncrptAndDecryptClient.PHONE,newToken);
						list.add(decrypt);
					}else{
						list.add(nick);
					}
				} catch (Exception e) {
					try {
						newToken = userInfoService.findUserTokenById(user.getId());
						if(EncrptAndDecryptClient.isEncryptData(nick, EncrptAndDecryptClient.SEARCH)){
							String decrypt = EncrptAndDecryptClient.getInstance().decryptData(nick, EncrptAndDecryptClient.PHONE,newToken);
							list.add(decrypt);
						}else{
							list.add(nick);
						}
					} catch (Exception e1) {
						logger.error("=========指定号码群发======="+user.getTaobaoUserNick()+"=========解密电话号码失败"+e1.getMessage());
						continue;
					}
				}
			}
		}
		return list;
	}

	@Override
	public void clearAllSmsBlack(Long uid) throws Exception {
		
	}

	@Override
	public boolean nickIsBlack(Long uid, String buyerNick) {
		return false;
	}
	
}
