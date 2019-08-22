package com.kycrm.member.service.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.message.ISmsTemplateDao;
import com.kycrm.member.domain.entity.message.SmsTemplate;

@MyDataSource(MyDataSourceAspect.MASTER)
@Service("smsTemplateService")
public class SmsTemplateServiceImpl implements ISmsTemplateService {

	@Autowired
	private ISmsTemplateDao smsTemplateDao;
	
	@Override
	public void saveSingleTemplate(Long uid,SmsTemplate smsTemplate){
		smsTemplate.setUid(uid);
		smsTemplateDao.saveSmsTemplate(smsTemplate);
	}
	
	@Override
	public List<SmsTemplate> listSmsTemplate(Long uid,String type,String customerType,String subType){
		Map<String, Object> map = new HashMap<String, Object>();
		if("customer".equals(customerType)){
			map.put("uid", uid);	
		}
		map.put("type", type);
		map.put("customerType", customerType);
		map.put("subType", subType);
		List<SmsTemplate> smsTemplate = smsTemplateDao.listSmsTemplate(map);
		return smsTemplate;
	}
	
	@Override
	public void deleteTemplateById(Long templateId){
		smsTemplateDao.deleteTemplateById(templateId);
	}

	@Override
	public int findSmsTemName(SmsTemplate smsTem) {
		return smsTemplateDao.findSmsTemName(smsTem);
	}

	@Override
	public List<SmsTemplate> findSubType(String type) {
		return smsTemplateDao.findSmsSubTpye(type);
	}
}
