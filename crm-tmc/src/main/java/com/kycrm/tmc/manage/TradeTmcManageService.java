package com.kycrm.tmc.manage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.tmc.service.TmcLogisticsService;
import com.kycrm.tmc.service.TmcRefundAgreeService;
import com.kycrm.tmc.service.TmcRefundCreateService;
import com.kycrm.tmc.service.TmcRefundRefuseService;
import com.kycrm.tmc.service.TmcRefundSuccessService;
import com.kycrm.tmc.service.TmcTradeCreateService;
import com.kycrm.tmc.service.TmcTradeSuccessService;
import com.kycrm.tmc.service.TmcTradeTradeBuyerPayService;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.TmcInfo;

/** 
 * 订单tmc消息管理类
* @author wy
* @version 创建时间：2017年9月5日 下午5:13:20
*/
@Service
public class TradeTmcManageService {
	private Logger logger = LoggerFactory.getLogger(TradeTmcManageService.class);
	@Autowired
	private  TmcTradeCreateService tmcTradeCreateService;
	@Autowired
	private  TmcTradeSuccessService tmcTradeSuccessService;
	@Autowired
	private  TmcLogisticsService tmcLogisticsService;
	@Autowired
	private  TmcTradeTradeBuyerPayService tmcTradeTradeBuyerPayService;
	@Autowired
	private  TmcRefundCreateService tmcRefundCreateService;
	@Autowired
	private  TmcRefundAgreeService tmcRefundAgreeService;
	@Autowired
	private  TmcRefundRefuseService tmcRefundRefuseService;
	@Autowired
	private  TmcRefundSuccessService tmcRefundSuccessService;
	
	public void doHandle(String string) throws Exception {
		long startTime = System.currentTimeMillis();
		this.logger.info("订单tmc开始处理了。 "+string);
        JSONObject json = JSONObject.parseObject(string);
        String topic = json.getString(OrderSettingInfo.TOPIC);
		try {
			switch (topic) {
				case TmcInfo.TRADE_CREATE_TOPIC:{
					//下单
					this.tmcTradeCreateService.doHandle(json);
					break;
				}
				case TmcInfo.TRADE_BUYERPAY_TOPIC:{
					//付款消息
					this.tmcTradeTradeBuyerPayService.doHandle(json);
					break;
				}
				case TmcInfo.LOGSTIC_DETAIL_TOPIC:{
					//物流消息
					this.tmcLogisticsService.doHandle(json);
					break;
				}
				case TmcInfo.TRADE_SUCCESS_TOPIC:{ 
					//订单交易成功
					this.tmcTradeSuccessService.doHandle(json);
					break;
				}
				case TmcInfo.REFUND_CREATED_TOPIC:{ 
					//退款申请
					this.tmcRefundCreateService.doHandle(json);
					break;
				}
				case TmcInfo.REFUND_AGREE_TOPIC:{ 
					//退款同意
					this.tmcRefundAgreeService.doHandle(json);
					break;
				}
				case TmcInfo.REFUND_REFUSE_TOPIC:{ 
					//退款拒绝
					this.tmcRefundRefuseService.doHandle(json);
					break;
				}
				case TmcInfo.REFUND_SUCCESS_TOPIC:{ 
					//退款成功
					this.tmcRefundSuccessService.doHandle(json);
					break;
				}
				default:{
					break;
				}
			}
		}catch (Exception e) {
		    this.logger.info("tmc消息处理异常start：" + string);;
		    e.printStackTrace();
		    this.logger.info("tmc消息处理异常end：" + json.getString("tid") +" 错误：" +e.getMessage());;
		} finally {
			long endTime = System.currentTimeMillis();
			long result = endTime-startTime;
			if(result>10000){
				logger.info("tmc执行结束，执行花费时间："+(result)+"ms,执行topic："+string);
			}
		}
	}
	
}
