package com.kycrm.member.dao.marketing;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.filterrecord.FilterRecord;

public interface IFilterRecordDao {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 记录筛选条件
	 * @Date 2018年11月14日下午4:07:30
	 * @param uid
	 * @param filterRecord
	 * @throws Exception
	 * @ReturnType Long
	 */
	public Long insertFilterRecord(FilterRecord filterRecord) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据ID获取筛选条件
	 * @Date 2018年11月14日下午4:07:39
	 * @param uid
	 * @param id
	 * @return
	 * @throws Exception
	 * @ReturnType FilterRecord
	 */
	public FilterRecord getFilterRecordById(@Param("uid") Long uid, @Param("id") Long id) throws Exception;

}
