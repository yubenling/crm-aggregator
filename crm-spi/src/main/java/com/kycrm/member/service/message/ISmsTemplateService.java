package com.kycrm.member.service.message;

import java.util.List;

import com.kycrm.member.domain.entity.message.SmsTemplate;

public interface ISmsTemplateService {

	/**
	 * saveSingleTemplate(保存一条记录)
	 * @Title: saveSingleTemplate 
	 * @param @param smsTemplate 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void saveSingleTemplate(Long uid,SmsTemplate smsTemplate);

	/**
	 * listSmsTemplate(查询多条记录)
	 * @Title: listSmsTemplate 
	 * @param @param uid
	 * @param @param type
	 * @param @param customerType
	 * @param @return 设定文件 
	 * @return List<SmsTemplate> 返回类型 
	 * @throws
	 */
	List<SmsTemplate> listSmsTemplate(Long uid, String type, String customerType,String subType);

	/**
	 * deleteTemplateById(删除一条记录)
	 * @Title: deleteTemplateById 
	 * @param @param templateId 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void deleteTemplateById(Long templateId);
    /**
     * findSmsTemName 查询短信模板名称
     * @param smsTem
     * @return
     */
	int findSmsTemName(SmsTemplate smsTem);
    /**
     * 查询模板子类型
     * @return
     */
	List<SmsTemplate> findSubType(String type);

}
