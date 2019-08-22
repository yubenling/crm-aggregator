package com.kycrm.util;

import java.util.Map;

public class TradeCenterModuleLinkMap {
	
	private static Map<String, String> map = null;

	/**
	 * 1-下单关怀 
	 * 2-常规催付 
	 * 3-二次催付 
	 * 4-聚划算催付
	 * 6-发货提醒 
	 * 13-付款关怀
	 * 11-延时发货提醒 
	 * 7-到达同城提醒 
	 * 8-派件提醒 
	 * 9-签收提醒 
	 * 12-宝贝关怀
	 * 14-回款提醒
	 * 15-退款关怀
	 * 16-自动评价
	 * 19-中差评查看
	 * 33-会员短信群发 
	 * 34-指定号码群发 
	 * 35-订单短信群发
	 * 37-好评提醒
	 * 短信账单
	 */
	
	
	private static String CONTEXT_PATH = "crm.kycrm.com";
	
	//1-下单关怀 
	public static final String XDGH = CONTEXT_PATH + "";
	//2-常规催付 
	public static final String CGCF = CONTEXT_PATH + "";
	//3-二次催付 
	public static final String ECCF = CONTEXT_PATH + "";
	//4-聚划算催付
	public static final String JHSCF = CONTEXT_PATH + "";
	//6-发货提醒 
	public static final String FHTX = CONTEXT_PATH + "";
	//13-付款关怀
	public static final String FKGH = CONTEXT_PATH + "";
	//11-延时发货提醒 
	public static final String YSFHTX = CONTEXT_PATH + "";
	//7-到达同城提醒
	public static final String DDTCTX = CONTEXT_PATH + "";
	//8-派件提醒 
	public static final String PJTX = CONTEXT_PATH + "";
	//9-签收提醒 
	public static final String QSTX = CONTEXT_PATH + "";
	//12-宝贝关怀
	public static final String BBGH = CONTEXT_PATH + "";
	//14-回款提醒
	public static final String HKTX = CONTEXT_PATH + "";
	//15-退款关怀
	public static final String TKGH = CONTEXT_PATH + "";
	//16-自动评价
	public static final String ZDPJ = CONTEXT_PATH + "";
	//19-中差评查看
	public static final String ZCPGL = CONTEXT_PATH + "";
	//33-会员短信群发
	public static final String HYDXQF = CONTEXT_PATH + "";
	//34-指定号码群发 
	public static final String ZDHMQF = CONTEXT_PATH + "";
	//35-订单短信群发
	public static final String DDDXQF = CONTEXT_PATH + "";
	//37-好评提醒
	public static final String HPTX = CONTEXT_PATH + "";
	//短信账单
	public static final String DXZD = CONTEXT_PATH + "";
	
	
	public static Map<String, String> getInstance(){
		map.put("1", XDGH);
		map.put("2", CGCF);
		map.put("3", ECCF);
		map.put("4", JHSCF);
		map.put("6", FHTX);
		map.put("7", DDTCTX);
		map.put("8", PJTX);
		map.put("9", QSTX);
		map.put("11", YSFHTX);
		map.put("12", BBGH);
		map.put("13", FKGH);
		map.put("14", HKTX);
		map.put("15", TKGH);
		map.put("16", ZDPJ);
		map.put("19", ZCPGL);
		map.put("33", HYDXQF);
		map.put("34", ZDHMQF);
		map.put("35", DDDXQF);
		map.put("37", HPTX);
		map.put("", DXZD);
		return map;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
