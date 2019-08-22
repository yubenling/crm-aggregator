package com.kycrm.member.dao.message;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.message.SmsTemplate;

public interface ISmsTemplateDao {

	/**
	 * saveSmsTemplate(保存一条记录)
	 * @Title: saveSmsTemplate 
	 * @param @param smsTemplate 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveSmsTemplate(SmsTemplate smsTemplate);
	
	/**
	 * listSmsTemplate(根据条件查询list)
	 * @Title: listSmsTemplate 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<SmsTemplate> 返回类型 
	 * @throws
	 */
	public List<SmsTemplate> listSmsTemplate(Map<String, Object> map);
	
	/**
	 * deleteTemplateById(根据id删除一条记录)
	 * @Title: deleteTemplateById 
	 * @param @param templateId 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void deleteTemplateById(@Param("id")Long templateId);
     /**
      * findSmsTemName 短信模板名称查询模板
      * @param smsTem
      * @return
      */
	public int findSmsTemName(SmsTemplate smsTem);
     /**
      * 查询所有的子类型
      * @param subTpye
      * @return
      */
	public List<SmsTemplate> findSmsSubTpye(String type);
}
