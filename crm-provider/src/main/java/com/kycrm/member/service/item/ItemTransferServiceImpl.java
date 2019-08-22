package com.kycrm.member.service.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.item.IItemDao;
import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;

@Service("itemTransferService")
@MyDataSource
public class ItemTransferServiceImpl implements IItemTransferService {

	@Autowired
	private IItemDao itemDao;

	/**
	 * 批量插入商品信息
	 */
	@Override
	public void batchSaveItemsByUid(Long uid, byte[] compress) throws Exception {
		byte[] uncompress = GzipUtil.uncompress(compress);
		List<Item> itemList = JsonUtil.readValuesAsArrayList(new String(uncompress), Item.class);
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("uid", uid);
		paramMap.put("itemList", itemList);
		this.itemDao.batchSaveItemsByUid(paramMap);
	}

	@Override
	public void trancateTable(Long uid) throws Exception {
		this.itemDao.trancateTable(uid);
	}

}
