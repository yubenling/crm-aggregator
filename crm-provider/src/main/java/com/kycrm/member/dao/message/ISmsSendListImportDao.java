package com.kycrm.member.dao.message;

import java.util.List;

import com.kycrm.member.domain.entity.message.SmsSendListImport;
import com.kycrm.member.domain.vo.message.SmsSendListImportVO;

public interface ISmsSendListImportDao {

	void saveSmsSendListImport(SmsSendListImport sendListImport);

	void updateSmsSendListImportById(SmsSendListImport smsSendlistImport);

	List<SmsSendListImport> findSmsSendLists(SmsSendListImportVO vo);

	Long deleteSmsSendListById(Long recordId);

	SmsSendListImport findImportPhoneByteById(Long id);

	
	
	/**
	 *	以下方法只执行一次 
	 * @param uid 
	 * @time 2018年9月20日 上午11:37:57 
	 * @return
	 */
	List<SmsSendListImport> findSmsSendListImport(Long uid);
	String findImportPhoneById(Long id);
	Integer disposeImportPhoneSaveByte(SmsSendListImport s);
}
