package com.kycrm.tmc.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.exception.HandlerException;
import com.kycrm.tmc.entity.TmcMessages;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.domain.Order;

/** 
 * 校验订单的商品选择
* @author wy
* @version 创建时间：2017年8月31日 下午2:59:55
*/
@Component("tradeGoodsHandler")
public class TradeGoodsHandler implements Handler {
	private final Logger logger = Logger.getLogger(TradeGoodsHandler.class);
	
	@Override
	public void doHandle(@SuppressWarnings("rawtypes") Map map) throws HandlerException {
		TmcMessages tmcMessages = (TmcMessages) map.get("tmcMessages");
		if(tmcMessages==null){
			this.logger.info("传递的对象为空，无法进行判断！！！");
			return;
		}
		if(!tmcMessages.getFlag()){
			return;
		}
		TradeSetup tradeSetup = tmcMessages.getTradeSetup();
		if(tradeSetup == null){
			this.logger.info("传递的用户设置对象为空，无法进行判断！！！");
			return ;
		}
		if(tmcMessages.getTrade()==null){
			this.logger.info("订单对象为空，无法进行判断！！！");
			return ;
		}
		if(tradeSetup.getProductType()==null){
			return ;
		}
		String sellerProducts = tradeSetup.getProducts();
		if(ValidateUtil.isEmpty(sellerProducts)){
			return ;
		}
		List<Order> orders = tmcMessages.getTrade().getOrders();
		if(ValidateUtil.isEmpty(orders)){
			return ;
		}
		List<String> idsList = new ArrayList<String>(orders.size());
		for (Order order : orders) {
			idsList.add(String.valueOf(order.getNumIid()));
		}
		boolean flag = false ;
		//指定商品发送
		if(tradeSetup.getProductType()){
			for (String string : idsList) {
				//找到有一个匹配的，即可发送短信
				if(sellerProducts.contains(string)){
					flag = true; 
					break;
				}
			}
			if(!flag){
				this.logger.info("指定商品过滤，商品id和设置的不符不发送短信  tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
			}
		}else{
			//排除指定商品发送
			for (String string : idsList) {
				//有一个商品不在排除中，则发送
				if(!sellerProducts.contains(string)){
					flag = true; 
					break;
				}
			}
			if(!flag){
				this.logger.info("排除指定商品过滤成功，商品不发送短信  tid:"+tmcMessages.getTid()+",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
			}
		}
		if(!flag){
			tmcMessages.setFlag(false);
			return ;
		}
	}
	
}
