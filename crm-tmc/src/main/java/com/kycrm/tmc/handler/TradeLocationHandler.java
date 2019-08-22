package com.kycrm.tmc.handler;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.exception.HandlerException;
import com.kycrm.tmc.entity.TmcMessages;
import com.kycrm.util.ValidateUtil;

/** 
 * 校验订单的地区
* @author wy
* @version 创建时间：2017年8月31日 下午2:59:55
*/
@Component("tradeLocationHandler")
public class TradeLocationHandler implements Handler {
	private final Logger logger = Logger.getLogger(TradeLocationHandler.class);
	
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
		String sellerProvince = tradeSetup.getProvince();
		if(ValidateUtil.isEmpty(sellerProvince)){
		    this.validateCity(tmcMessages,true);
			return ;
		}
		String tradeProvince = tmcMessages.getTrade().getReceiverState();
		//地区不在指定发送区域
		if(!sellerProvince.contains(tradeProvince)){
		    this.validateCity(tmcMessages,false);
		}
	}
	/**
	 * 校验城市是否正确，省份为空或者省份匹配不正确时调用
	 * @author: wy
	 * @time: 2017年11月2日 下午4:02:14
	 * @param tmcMessages tmc消息中转类
	 * @param proviceIsNull 省份是否为空
	 */
	private void validateCity(TmcMessages tmcMessages,boolean proviceIsNull){
	    TradeSetup tradeSetup = tmcMessages.getTradeSetup();
	    String sellerCity = tradeSetup.getCity();
        if(ValidateUtil.isEmpty(sellerCity)){
            if(!proviceIsNull){
                tmcMessages.setFlag(false); 
                this.logger.info("地区省份筛选判断未通过且无城市设置，订单省份: "+tmcMessages.getTrade().getReceiverState()+" ,用户选择的筛选省份: "+tradeSetup.getProvince()+" tid:"+tmcMessages.getTid()+
                        ",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
            }
            return ;
        }
        String tradeCity = tmcMessages.getTrade().getReceiverCity();
        if(tradeCity==null){
            tradeCity = tmcMessages.getTrade().getReceiverDistrict();
        }
        if(ValidateUtil.isEmpty(tradeCity)){
            tmcMessages.setFlag(false); 
            this.logger.info("订单的城市获取不到，订单城市: "+tmcMessages.getTrade().getReceiverCity()+" ,订单区: "+tmcMessages.getTrade().getReceiverDistrict()+" tid:"+tmcMessages.getTid()+
                    ",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
            return ;
        }
        tradeCity = tradeCity.replaceAll("市", "").replaceAll("区", "");
        if(!sellerCity.contains(tradeCity)){
            tmcMessages.setFlag(false); 
            this.logger.info("地区城市筛选判断未通过，订单城市: "+tradeCity+" ,用户选择的筛选城市: "+sellerCity+" tid:"+tmcMessages.getTid()+
                    ",类型："+tmcMessages.getTradeSetup().getType()+" 用户设置的ID:+"+tradeSetup.getId());
            return ;
        }
	}
}
