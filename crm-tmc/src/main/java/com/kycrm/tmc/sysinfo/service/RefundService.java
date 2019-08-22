package com.kycrm.tmc.sysinfo.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.tmc.core.mybatis.SysInfoBatisDao;
import com.kycrm.tmc.sysinfo.entity.Refund;
import com.kycrm.util.ValidateUtil;

/** 
* @author wy
* @version 创建时间：2017年9月14日 上午11:05:22
*/
@Service
public class RefundService {
    @Autowired
    private SysInfoBatisDao sysInfoBatisDao;
	/**
	 * 查询这个订单有多少个子订单被退款了
	 * @author: wy
	 * @time: 2017年9月14日 上午11:06:10
	 * @param sellerNick 卖家昵称 
	 * @param tid 主订单 
	 * @return 子订单的集合  查询不到返回null
	 */
	public List<Long> findRefundOidByTid(String sellerNick,String tid){
		if(ValidateUtil.isEmpty(tid) ||ValidateUtil.isEmpty(sellerNick)){
			return null;
		}
		Map<String,Object> map = new HashMap<String,Object>(6);
		map.put("tid", tid);
		Calendar cal = Calendar.getInstance();
		map.put("endTime", cal.getTime());
		cal.add(Calendar.HOUR, -1);
		map.put("startTime", cal.getTime());
		List<Map<String,String>> list = this.sysInfoBatisDao.findList(Refund.class.getName(), "findRefundByTid", map);
		if(ValidateUtil.isEmpty(list)){
			return null;
		}
		List<Long> result = new ArrayList<Long>();
		for(Map<String,String> resultMap : list ){
			String resultTid = String.valueOf(resultMap.get("tid"));
			if(tid.equals(resultTid)){
				result.add(Long.parseLong(String.valueOf(resultMap.get("oid"))));
			}
		}
		return result;
	}
}

