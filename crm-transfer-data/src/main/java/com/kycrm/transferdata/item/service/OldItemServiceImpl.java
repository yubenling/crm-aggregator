package com.kycrm.transferdata.item.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.dao.IItemDao;
import com.kycrm.member.domain.entity.item.Item;

@Service("oldItemService")
public class OldItemServiceImpl implements IOldItemService {

	@Autowired
	private IItemDao itemDao;
	
	@Override
	public List<Item> findItemsByUid(Long uid, int startRows, int pageSize, Date startDate, Date endDate) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("uid", uid);
		paramMap.put("startRows", startRows);
		paramMap.put("pageSize", pageSize);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		return this.itemDao.findItemsByUid(paramMap);
	}

	@Override
	public Long getCount(Long uid, Date startDate, Date endDate) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(3);
		paramMap.put("uid", uid);
		paramMap.put("startDate", startDate);
		paramMap.put("endDate", endDate);
		return this.itemDao.getCount(paramMap);
	}

}
