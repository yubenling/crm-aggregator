package com.kycrm.member.domain.utils.pagination;

public abstract interface Paginable {

	public abstract int getTotalCount();
	  
	public abstract int getTotalPage();
  
	public abstract int getPageSize();
  
	public abstract int getPageNo();
  
	public abstract boolean isFirstPage();
  
	public abstract boolean isLastPage();
  
	public abstract int getNextPage();
  
	public abstract int getPrePage();
}
