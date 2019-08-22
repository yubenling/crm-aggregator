package com.kycrm.member.dao.orderimport;

import java.util.List;

import com.kycrm.member.domain.entity.orderimport.OrderImportRecord;
import com.kycrm.member.domain.vo.orderimport.OrderImportRecordVO;


public interface IOrderImportRecordDao {

	/**
	 * 创建导入记录返回id
	 * @author HL
	 * @time 2018年5月24日 下午4:47:28 
	 * @param orderImport
	 * @return
	 */
	void insertOrderImportRecord(OrderImportRecord orderImport);

	/**
	 * 通过id更新导入记录状态
	 * @author HL
	 * @param id 
	 * @time 2018年6月1日 下午2:55:40
	 */
	void updateOrderImportRecordState(Long id);

	/**
	 * 查询总条数
	 * @author HL
	 * @time 2018年8月30日 下午4:13:30 
	 * @param vo
	 * @return
	 */
	Long findOrderImportRecordCount(OrderImportRecordVO vo);

	/**
	 * 查询分页数据
	 * @author HL
	 * @time 2018年8月30日 下午4:13:56 
	 * @param vo
	 * @return
	 */
	List<OrderImportRecord> findOrderImportRecordList(OrderImportRecordVO vo);

	/**
	 * 删除历史订单导入记录
	 * @author HL
	 * @time 2018年8月30日 下午4:30:58 
	 * @param vo
	 * @return
	 */
	long deleteOrderImportRecord(OrderImportRecordVO vo);
	
}
