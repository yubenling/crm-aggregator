package com.kycrm.member.service.syn;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.syn.ITbRefundDao;
import com.kycrm.member.domain.entity.refund.TbRefund;


@Service("tbRefundService")
@MyDataSource(MyDataSourceAspect.SYSINFO)
public class TbRefundServiceImpl implements ITbRefundService{
    
	
	@Autowired
	private ITbRefundDao tbRefundDao;
	
	/**
	 * 查询指定时间段退款订单的数量
	 */
	@Override
	public Long findConuntByTime(Date beginTime, Date endTime) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		return tbRefundDao.findConuntByTime(map);
	}
    /**
     * 分页查询退款订单
     */
	@Override
	public List<TbRefund> findtbRefundList(Date beginTime, Date endTime,
			Long pageSize, Long oid) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("beginTime", beginTime);
		map.put("endTime", endTime);
		map.put("pageSize", pageSize);
		map.put("oid", oid);
		return tbRefundDao.findtbRefundList(map);
	}

}
