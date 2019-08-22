package com.kycrm.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;

/**
 * 过滤手机号
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年11月12日下午4:38:36
 * @Tags
 */
public class FilterMobileUtil {

	private static final Log logger = LogFactory.getLog(FilterMobileUtil.class);

	// 锁
	private final ReentrantLock lock = new ReentrantLock();

	private Set<String> filterPhonesSet;

	/**
	 * 去除重复/错误手机号
	 * 
	 * @author HL
	 * @time 2018年8月3日 下午2:39:07
	 * @param phones
	 * @return
	 */
	public Map<String, Collection<MemberInfoDTO>> disposePhones(List<MemberInfoDTO> memberInfoList) throws Exception {
		lock.lock();
		Map<String, Collection<MemberInfoDTO>> resultMap;
		try {
			resultMap = new HashMap<String, Collection<MemberInfoDTO>>();
			Set<MemberInfoDTO> correctPhoneSet = new HashSet<MemberInfoDTO>();
			Map<String, MemberInfoDTO> correctPhoneMap = new HashMap<String, MemberInfoDTO>();// 正确的手机号码
			List<MemberInfoDTO> wrongPhoneList = new ArrayList<MemberInfoDTO>();// 错误的手机号码
			List<MemberInfoDTO> repeatPhoneList = new ArrayList<MemberInfoDTO>();// 重复的手机号码
			MemberInfoDTO memberInfoDTO = null;
			// 去掉本批次和之前所有批次重复的手机号
			if (filterPhonesSet != null) {
				for (int i = 0; i < memberInfoList.size(); i++) {
					memberInfoDTO = memberInfoList.get(i);
					String mobile = memberInfoDTO.getMobile();
					if (filterPhonesSet.contains(mobile)) {
						memberInfoList.remove(i);
						repeatPhoneList.add(memberInfoDTO);
					}
				}
			}	
			if (null != memberInfoList && memberInfoList.size() > 0) {
				for (int i = 0; i < memberInfoList.size(); i++) {
					memberInfoDTO = memberInfoList.get(i);
					String phone = memberInfoDTO.getMobile();
					if (null == phone || "".equals(phone))
						continue;
					if (MobileRegEx.validateMobile(phone)) {
						if (correctPhoneMap.containsKey(phone)) {
							repeatPhoneList.add(memberInfoDTO);
						} else {
							correctPhoneMap.put(phone, memberInfoDTO);
						}
					} else {
						wrongPhoneList.add(memberInfoDTO);
					}
				}
				for (Entry<String, MemberInfoDTO> map : correctPhoneMap.entrySet()) {
					correctPhoneSet.add(map.getValue());
				}
			}
			resultMap.put("phoneSet", correctPhoneSet);
			resultMap.put("wrongNums", wrongPhoneList);
			resultMap.put("repeatNums", repeatPhoneList);
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("过滤手机号出错", e);
		} finally {
			if (lock.isLocked()) {
				lock.unlock();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 添加新数据
	 * @Date 2018年11月12日下午4:40:09
	 * @throws Exception
	 * @ReturnType void
	 */
	public void addNewData(Set<String> correctPhones, int batch) throws Exception {
		lock.lock();
		if (filterPhonesSet == null) {
			filterPhonesSet = new HashSet<String>();
		}
		filterPhonesSet.addAll(correctPhones);
		logger.info("第 " + batch + " 批次，去重且不重复手机号总数 = " + filterPhonesSet.size());
		lock.unlock();
	}

}
