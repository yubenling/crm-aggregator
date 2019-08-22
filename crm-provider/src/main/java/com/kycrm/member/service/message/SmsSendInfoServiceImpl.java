package com.kycrm.member.service.message;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.message.ISmsSendInfoDao;
import com.kycrm.member.domain.entity.message.SmsSendInfo;
import com.kycrm.member.service.user.JudgeUserUtil;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.SecretException;


@Service("smsSendInfoService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class SmsSendInfoServiceImpl implements ISmsSendInfoService {
	
	
	@Autowired
	private ISmsSendInfoDao smsSendInfoDao;
	
	@Autowired
	private JudgeUserUtil judgeUserUtil;
	
	/**
	 * 查询今天发过的短信发现记录，
	 * @author: wy
	 * @time: 2017年9月8日 下午4:56:42
	 * @param sellerNick 卖家昵称
	 * @param type 类型
	 * @return 只返回手机号码和短信内容 <br>
	 *  key = phone <br>
	 *  key = content<br>
	 */
	@Override
	public List<Map<String,String>> findNowSendHistory(Long uid,String type){
		if(ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(type)){
			return null;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("uid", uid);
		map.put("type", type);
		SimpleDateFormat fomartDate = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = new Date();
		String startSendTime = fomartDate.format(nowDate);
		Date startDate = null;
		try {
			startDate = fomartDate.parse(startSendTime);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		map.put("startDate", startDate);
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.DATE, 1);
		map.put("endDate", cal.getTime());
		return this.smsSendInfoDao.findSellerHistory(map);
	}
	/**
	 * 创建保存两天的短信记录
	 * @author: wy
	 * @time: 2017年9月4日 下午5:03:40
	 * @param smsSendInfo
	 * @return true 保存成功 ，false 保存失败
	 */
	@Override
	public boolean saveSmsTemporary(SmsSendInfo smsSendInfo){
		try {
			String buyerNick = smsSendInfo.getNickname();
			String phone = smsSendInfo.getPhone();
			String session = judgeUserUtil.getUserTokenByRedis(smsSendInfo.getUid());
			if(!EncrptAndDecryptClient.isEncryptData(buyerNick, EncrptAndDecryptClient.SEARCH)){
				smsSendInfo.setNickname(EncrptAndDecryptClient.getInstance().encrypt(buyerNick, EncrptAndDecryptClient.SEARCH, session));
			}
			if(!EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)){
				smsSendInfo.setPhone(EncrptAndDecryptClient.getInstance().encrypt(phone, EncrptAndDecryptClient.PHONE, session));
			}
			Integer result = this.smsSendInfoDao.doCreateByMessage(smsSendInfo);
			if(result==1){
				return true;
			}
			return false;
		} catch (SecretException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 根据卖家昵称和买家昵称和类型  查询短信是否有发送记录
	 * @author: wy
	 * @time: 2017年9月4日 下午6:21:10
	 * @param sellerNick 卖家昵称
	 * @param buyerNick 买家昵称
	 * @param type 类型
	 * @return true：存在 ，false：不存在
	 */
	@Override
	public boolean isExists(Long uid,String sellerName,String buyerNick,String type){
		if(ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(type)|| ValidateUtil.isEmpty(buyerNick)){
			return false;
		}
		Map<String,Object> map = new HashMap<String,Object>(8);
		map.put("uid", uid);
		try {
            if(!EncrptAndDecryptClient.isEncryptData(buyerNick, EncrptAndDecryptClient.SEARCH)){
                String sessionKey = this.judgeUserUtil.getUserTokenByRedis(uid);
                buyerNick = EncrptAndDecryptClient.getInstance().encrypt(buyerNick, EncrptAndDecryptClient.SEARCH,sessionKey);
            }
        } catch (SecretException e) {
            e.printStackTrace();
            return true;
        }
		SimpleDateFormat fomartDate = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = new Date();
        String startSendTime = fomartDate.format(nowDate);
        Date startDate = null;
        try {
            startDate = fomartDate.parse(startSendTime);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        map.put("startDate", startDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, 1);
        map.put("endDate", cal.getTime());
		map.put("buyerNick", buyerNick);
		map.put("type", type);
 		int result = this.smsSendInfoDao.findCountByNickAndType(map);
		if(result>0){
			return true;
		}
		return false;
	}
	/**
	 * 查询订单和类型  是否有发送记录
	 * @author: wy 
	 * @time: 2017年9月4日 下午6:17:34
	 * @param tid 订单号
	 * @param type 类型 
	 * @return true：存在。false 不存在
	 */
	@Override
	public boolean isExists(Long tid,String type){
		if(ValidateUtil.isEmpty(tid) || ValidateUtil.isEmpty(type)){
			return false;
		}
		Map<String,Object> map = new HashMap<String,Object>(5);
		map.put("tid", tid);
		map.put("type", type);
		int result = this.smsSendInfoDao.findCountByTidAndType(map);
		if(result>0){
			return true;
		}
		return false;
	}
	public int findSmsByRemoveCount(Date startDate){
		if(startDate ==null){
			return 0;
		}
		int count = this.smsSendInfoDao.findByOnceDayCount(startDate);
		return count;
	}
	/**
	 * 查询超过两天的短信
	 * @author: wy
	 * @time: 2017年9月4日 下午5:05:12
	 * @return
	 */
	@Override
	public List<Long> findSmsByRemove(int page,Date startDate){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("nowDate", startDate);
		map.put("lineSize", ISmsSendInfoService.RANGE);
		List<Long> ids = this.smsSendInfoDao.findByOnceDay(map); 
		if(ValidateUtil.isEmpty(ids)){
			return null;
		}
		return ids;
	}
	/**
	 * 删除指定的ID，如果删除的id个数超过限制，会分多次删除
	 * @author: wy
	 * @time: 2017年9月4日 下午6:00:10
	 * @param idsList
	 * @return true 删除成功，false 删除失败
	 */
	@Override
	public boolean removeIds(List<Long> idsList) {
		if(ValidateUtil.isEmpty(idsList)){
			return true;
		}
		int length = idsList.size();
		Map<String,List<Long>> map = new HashMap<String,List<Long>>(5);
		if(length<=RANGE){
			map.put("idsList", idsList);
			this.smsSendInfoDao.removeByInvalid(map);
			return true;
		}
		int i = 0;
		while(i<length){
			int toIndex = i + RANGE;
			if(toIndex>length){
				toIndex = length;
			}
			List<Long> list = idsList.subList(i, toIndex);
			map.put("idsList", list);
			this.smsSendInfoDao.removeByInvalid(map);
			i = toIndex;
		}
		return true;
	}

}
