package com.kycrm.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.kycrm.member.domain.entity.item.ItemImport;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.util.IdUtils;

public class ItemImportUtil {
	
	//时间格式
	public static final String DATE_FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_TWO = "yyyy/MM/dd HH:mm";
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	
	/**
	 * 通过方法获取数据
	 * @return
	 */
	public static String getValFromArray(Map<String, Integer> headerMap,
			String[] dataArr, String header) {
		if (headerMap.containsKey(header)) {
			return dataArr[headerMap.get(header)];
		}
		return "";
	}
	
	/**
	 * 计算单价
	 */
	private static String unitPrice(String totalFee, String num) {
		try {
			double price = Double.parseDouble(totalFee)/Double.parseDouble(num);
			return Double.parseDouble(df.format(price))+"";
		} catch (Exception e) {
			return "0";
		}
	}
	
	
	/**
	 * 获取所以宝贝标题&创建商品id
	 * @return
	 */
	public static Map<String,List<String>> getImportTitles(Map<String, Integer> headerMap,
			List<String[]> datasList) {
		Map<String,List<String>> map= new HashMap<String,List<String>>();
		for (String[] dataArr : datasList) {
			try {
				List<String> list = new ArrayList<String>();
				String title = getValFromArray(headerMap, dataArr, "宝贝标题");
				if(null != title && !"".equals(title)){
					String price = unitPrice(
							getValFromArray(headerMap, dataArr, "总金额"),
							getValFromArray(headerMap, dataArr, "宝贝总数量"));
					String itemId = IdUtils.getTradeId();
					list.add(itemId);
					list.add(price);
					map.put(title.trim(), list);
				}
			} catch (Exception e) {
				continue;
			}
		}
		return map;
	}
	
	/*
	 * 将该用户的商品和当前导入的比对 --key需要与待导入的订单比对，value--需要将数据保存到新建商品表
	 */
	public static Map<Map<String, List<String>>, List<ItemImport>> comparisonItemTitle(
			Map<String, Long> itemMap, Map<String, List<String>> importTitles,UserInfo user) {
		Map<Map<String, List<String>>, List<ItemImport>> returnMap = new HashMap<Map<String, List<String>>, List<ItemImport>>();
		
		Map<String, List<String>> returnKay = new HashMap<String, List<String>>();
		List<ItemImport> returnValue = new ArrayList<ItemImport>();
		
		Set<Entry<String,List<String>>> entrySet = importTitles.entrySet();
		for (Entry<String, List<String>> entry : entrySet) {
			List<String> itemLong = new ArrayList<String>();
			try {
				if(entry.getKey().length()>255){
					continue;
				}
				if(itemMap.containsKey(entry.getKey())){
					itemLong.add(itemMap.get(entry.getKey())+"");
					returnKay.put(entry.getKey(), itemLong);
				}else{
					//封装itemImport对象
					String price = entry.getValue().get(1);
					Long itemId = Long.parseLong(entry.getValue().get(0));
					itemLong.add(itemId+"");
					itemLong.add("import");//标识为导入拆分商品
					returnKay.put(entry.getKey(), itemLong);
					ItemImport saveItem = packagingItemImport(entry.getKey(),itemId,price,user);
					returnValue.add(saveItem);
				}
			} catch (Exception e) {
				continue;
			}
		}
		returnMap.put(returnKay, returnValue);
		return returnMap;
	}
	
	/*
	 * 获取mapkey数据
	 */
	public static Map<String, List<String>> getMapKey(
			Map<Map<String, List<String>>, List<ItemImport>> map) {
		Map<String, List<String>> hashMap = new HashMap<String, List<String>>();
		Set<Entry<Map<String,List<String>>,List<ItemImport>>> entrySet = map.entrySet();
		for (Entry<Map<String, List<String>>, List<ItemImport>> entry : entrySet) {
			Map<String, List<String>> key = entry.getKey();
			hashMap.putAll(key);
		}
		return hashMap;
	}
	
	/*
	 * 获取mapvalue数据
	 */
	public static List<ItemImport> getMapValue(
			Map<Map<String, List<String>>, List<ItemImport>> map) {
		List<ItemImport> list = new ArrayList<ItemImport>();
		Set<Entry<Map<String,List<String>>,List<ItemImport>>> entrySet = map.entrySet();
		for (Entry<Map<String, List<String>>, List<ItemImport>> entry : entrySet) {
			List<ItemImport> value = entry.getValue();
			list.addAll(value);
		}
		return list;
	}
	
	/**
	 * 封装ItemImport
	 */
	private static ItemImport packagingItemImport(String title, Long itemId,
			String price,UserInfo user) {
		ItemImport itemImport = new ItemImport();
		itemImport.setUid(user.getId());
		itemImport.setNick(user.getTaobaoUserNick());
		itemImport.setNumIid(itemId);
		itemImport.setTitle(title);
		itemImport.setPrice(price);
		itemImport.setApproveStatus("import");
		itemImport.setCreatedBy(user.getTaobaoUserNick());
		itemImport.setLastModifiedBy(user.getTaobaoUserNick());
		return itemImport;
	}
	
}
