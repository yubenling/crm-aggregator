package com.kycrm.member.service.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.marketing.IFilterRecordDao;
import com.kycrm.member.domain.entity.filterrecord.FilterRecord;

/**
 * 
 * 筛选记录服务
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年11月14日下午4:08:37
 * @Tags
 */
@Service("filterRecordService")
@MyDataSource
public class FilterRecordServiceImpl implements IFilterRecordService {

	@Autowired
	private IFilterRecordDao filterRecordDao;

	/**
	 * 存储筛选记录
	 */
	@Override
	public Long insertFilterRecord(Long uid, FilterRecord filterRecord) throws Exception {
		this.filterRecordDao.insertFilterRecord(filterRecord);
		return filterRecord.getId();
	}

	/**
	 * 根据ID查询筛选记录
	 */
	@Override
	public FilterRecord getFilterRecordById(Long uid, Long id) throws Exception {
		return this.filterRecordDao.getFilterRecordById(uid, id);
	}

}
