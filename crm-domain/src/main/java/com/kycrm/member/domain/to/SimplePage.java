package com.kycrm.member.domain.to;

import java.io.Serializable;
import java.util.List;

/** 
* @ClassName: SimplePage 
* @Description: 一个简单的分页对象,用于provider向consumer返回基础分页信息
* @author jackstraw_yu
* @date 2018年2月7日 下午6:12:03 
*  
*/
public class SimplePage implements Serializable {
	
	private static final long serialVersionUID = 1624223377626143631L;

	
	public SimplePage(List<?> datas, Integer pageNo, Long count, Integer pageSize) {
		this.datas = datas;
		this.pageNo = pageNo==null||pageNo.intValue()<=0?1:pageNo;
		this.count = count;
		this.pageSize = pageSize==null||pageSize.intValue()<=0?5:pageSize;
	}
	public SimplePage() {
	}

	/**
	 * 分页的当前列表
	 */
	private List<?> datas;

	/**
	 * 第几页
	 */
	private Integer pageNo;
	
	/**
	 * 总条数
	 */
	private Long count;

	/**
	 * 总页数
	 */
	private Integer pageSize;


	public List<?> getDatas() {
		return datas;
	}
	public void setDatas(List<?> datas) {
		this.datas = datas;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo==null||pageNo.intValue()<=0?1:pageNo;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize==null||pageSize.intValue()<=0?5:pageSize;
	}
	
}
