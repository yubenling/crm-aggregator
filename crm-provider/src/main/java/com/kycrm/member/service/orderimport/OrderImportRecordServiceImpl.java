package com.kycrm.member.service.orderimport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.orderimport.IOrderImportRecordDao;
import com.kycrm.member.domain.entity.orderimport.OrderImportRecord;
import com.kycrm.member.domain.vo.orderimport.OrderImportRecordVO;

/**
 * @author HL
 * @time 2018年5月24日 下午4:48:47
 */
@Service("orderImportRecordService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class OrderImportRecordServiceImpl implements IOrderImportRecordService {

	@Autowired
    private IOrderImportRecordDao orderImportRecordDao;

	/**
	 * 创建导入记录返回id
	 * @author HL
	 * @time 2018年5月24日 下午4:47:28 
	 * @param orderImport
	 * @return
	 */
	@Override
	public Long insertOrderImportRecord(OrderImportRecord orderImport) {
		orderImportRecordDao.insertOrderImportRecord(orderImport);
		return orderImport.getId();
	}
	/**
	 * 修改导入记录返回id
	 * @author HL
	 * @time 2018年5月24日 下午4:47:28 
	 * @param orderImport
	 * @return
	 */
	@Override
	public void updateOrderImportRecordState(Long id) {
		orderImportRecordDao.updateOrderImportRecordState(id);
	}
	
	/**
	 * 查询历史订单导入记录
	 * @author HL
	 * @time 2018年8月30日 下午4:10:25 
	 * @param vo
	 * @return
	 */
	@Override
	public Map<String, Object> findOrderImportRecord(OrderImportRecordVO vo) {
		Map<String, Object> map = new HashMap<String, Object>();
		Long totalCount = orderImportRecordDao.findOrderImportRecordCount(vo);
		if(null == totalCount)
			totalCount = 0L;
		Integer totalPage = (int) Math.ceil(1.0*totalCount/vo.getCurrentRows());
		List<OrderImportRecord> list= orderImportRecordDao.findOrderImportRecordList(vo);
		map.put("totalPage", totalPage);
		map.put("list", list);
		return map;
	}
	
	/**
	 * 删除历史订单导入记录
	 * @author HL
	 * @time 2018年8月30日 下午4:30:58 
	 * @param vo
	 * @return
	 */
	@Override
	public boolean deleteOrderImportRecord(OrderImportRecordVO vo) {
			long i = orderImportRecordDao.deleteOrderImportRecord(vo);
			if(i>0){
				return true;
			}
		return false;
	}
	
}
