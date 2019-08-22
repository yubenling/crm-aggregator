package com.kycrm.member.domain.vo.item;

import java.io.Serializable;
import java.util.List;

import com.kycrm.member.domain.entity.item.CommodityGrouping;
import com.kycrm.member.domain.entity.item.GroupedGoods;
import com.kycrm.member.domain.entity.item.Item;

/**
 * @deprecated:商品分组返回搜索数据的vo
 * @author Administrator
 *
 */
public class CommodityViewer<T> implements Serializable{
	
	private static final long serialVersionUID = -1711150185983921371L;

	/**
	 * 商品分组：左侧搜索栏数据
	 * */
	private List<T> searchData;
	//private List<Item> searchData;
	
	/**
	 * 右侧数据
	 * */
	private List<GroupedGoods> selectData;
	
	/**
	 * 设置页 numIid不为空时
	 */
	private List<Item> itemData;
	
	/**
	 * 商品主页面查询
	 */
	private List<CommodityGrouping> groupedData;
	
	/**
	 * 当前第几页
	 * */
	private Integer currentPage;
	
	/**
	 * 总条数
	 * */
	private Long totalCout;
	
	/**
	 * 总页数
	 */
	private Integer totalPage;

	public List<T> getSearchData() {
		return searchData;
	}

	public void setSearchData(List<T> searchData) {
		this.searchData = searchData;
	}

	public List<GroupedGoods> getSelectData() {
		return selectData;
	}

	public void setSelectData(List<GroupedGoods> selectData) {
		this.selectData = selectData;
	}

	public List<Item> getItemData() {
		return itemData;
	}

	public void setItemData(List<Item> itemData) {
		this.itemData = itemData;
	}

	public List<CommodityGrouping> getGroupedData() {
		return groupedData;
	}

	public void setGroupedData(List<CommodityGrouping> groupedData) {
		this.groupedData = groupedData;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Long getTotalCout() {
		return totalCout;
	}

	public void setTotalCout(Long totalCout) {
		this.totalCout = totalCout;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	
	
}
