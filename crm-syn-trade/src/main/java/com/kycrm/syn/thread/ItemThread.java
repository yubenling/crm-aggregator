package com.kycrm.syn.thread;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.kycrm.member.domain.entity.item.TbItem;
//import com.kycrm.syn.service.item.ItemService;
//
//import s2jh.biz.shop.utils.SpringContextUtil;

public class ItemThread extends Thread {
	
	private List<TbItem> tbItemList = new CopyOnWriteArrayList<TbItem>();

    
	public List<TbItem> getTbItemList() {
		return tbItemList;
	}

	public void setTbItemList(List<TbItem> tbItemList) {
		this.tbItemList = tbItemList;
	}

	public void appendtbItemList(List<TbItem> tbItemList){
		this.tbItemList.addAll(tbItemList);
	}
	
	@Override
	public void run(){
//		ItemService itemService = SpringContextUtil.getBean("itemService");
//		itemService.saveItemsys(tbItemList);
		//itemService.synchronizeItem();
	}

}
