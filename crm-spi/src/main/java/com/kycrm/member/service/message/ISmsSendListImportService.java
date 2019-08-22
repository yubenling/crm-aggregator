package com.kycrm.member.service.message;

import java.util.List;

import com.kycrm.member.domain.entity.message.SmsSendListImport;
import com.kycrm.member.domain.vo.message.SmsSendListImportVO;


/**
 * 营销中心指定号码群发--电话号码导入服务
 * @author HL
 * @time 2018年2月8日 下午3:40:47
 */
public interface ISmsSendListImportService {

	/**
	 * 添加电话号码导入记录
	 * @author HL
	 * @time 2018年2月28日 下午3:48:10 
	 * @param sendListImport
	 * @return
	 */
	Long saveSmsSendListImport(SmsSendListImport sendListImport);

	/**
	 * 修改电话号码导入记录
	 * @author HL
	 * @time 2018年2月28日 下午4:17:29 
	 * @param smsSendlistImport
	 */
	void updateSmsSendListImportById(SmsSendListImport smsSendlistImport);

	/**
	 * 查询所以导入电话号码记录
	 * @author HL
	 * @time 2018年3月5日 下午3:27:41 
	 * @param vo
	 * @return
	 */
	List<SmsSendListImport> findSmsSendLists(SmsSendListImportVO vo);

	/**
	 * 通过id删除导入记录
	 * @author HL
	 * @time 2018年3月5日 下午3:57:34 
	 * @param vo
	 * @return
	 */
	boolean deleteSmsSendListById(Long id);
	
	/**
	 * 查询导入的电话号码，返回List
	 * @time 2018年9月20日 上午10:10:07 
	 * @param phonesId
	 * @return
	 */
	List<String> findImportPhoneByteById(Long id);

	/**
	 * 次方法有坑只执行一次---------------------------请勿调用------------------------------
	 * @param uid 
	 */
	void disposeImportPhoneToByte(Long uid);
}
