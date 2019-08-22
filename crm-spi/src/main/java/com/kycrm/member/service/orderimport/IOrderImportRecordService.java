package com.kycrm.member.service.orderimport;

import java.util.Map;

import com.kycrm.member.domain.entity.orderimport.OrderImportRecord;
import com.kycrm.member.domain.vo.orderimport.OrderImportRecordVO;

/**
 * 历史订单导入记录
 * @author HL
 * @time 2018年5月24日 下午4:46:31
 */
public interface IOrderImportRecordService {

	/**
	 * 创建导入记录返回id
	 * @author HL
	 * @time 2018年5月24日 下午4:47:28 
	 * @param orderImport
	 * @return
	 */
	public Long insertOrderImportRecord(OrderImportRecord orderImport);

	/**
	 * 修改导入记录返回id
	 * @author HL
	 * @time 2018年5月24日 下午4:47:28 
	 * @param orderImport
	 * @return
	 */
	public void updateOrderImportRecordState(Long id);

	/**
	 * 查询历史订单导入记录
	 * @author HL
	 * @time 2018年8月30日 下午4:10:25 
	 * @param vo
	 * @return
	 */
	public Map<String, Object> findOrderImportRecord(OrderImportRecordVO vo);

	/**
	 * 删除历史订单导入记录
	 * @author HL
	 * @time 2018年8月30日 下午4:30:58 
	 * @param vo
	 * @return
	 */
	public boolean deleteOrderImportRecord(OrderImportRecordVO vo);

	
}
