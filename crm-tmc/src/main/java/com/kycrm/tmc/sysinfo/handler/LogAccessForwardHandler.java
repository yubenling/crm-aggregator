package com.kycrm.tmc.sysinfo.handler;

import java.util.Map;

import org.apache.log4j.Logger;

import com.kycrm.member.domain.entity.eco.log.LogType;
import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.exception.HandlerException;
import com.kycrm.tmc.sysinfo.entity.HttpResult;


/**
 * 类名称：FetchOrderHandler <br/>
 * 类描述：拉取sysInfo 订单数据 <br/>
 * 创建时间：2017年05月20日 下午7:17:10 <br/>
 * 
 * @author zlp
 * @version V1.0
 */

public abstract class LogAccessForwardHandler implements Handler {

	private static final Logger loger = Logger.getLogger(LogAccessForwardHandler.class);

	@Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void doHandle(Map map) throws HandlerException {
		LogType loginType = (LogType)map.get(LogType.class.getName());
		loger.info(loginType);
		String result="";
		if(LogType.LOGIN_TYPE == loginType) {
			result= handleLoginLogType(map);
		}
		if(LogType.ORDER_TYPE == loginType) {
			result= handleOrderLogType(map);
		}
		if(LogType.SENDORDER_TYPE == loginType) {
			result= handleSendOrderLogType(map);
		}
		if(LogType.ACESSDB_TYPE == loginType) {
		    result= handleAccessDBLogType(map);
		}
		map.put(HttpResult.class.getName(), result);
		log(map);
	}
	@SuppressWarnings("rawtypes")
	protected abstract String handleLoginLogType(Map map);
	@SuppressWarnings("rawtypes")
	protected abstract String handleOrderLogType(Map map);
	@SuppressWarnings("rawtypes")
	protected abstract String handleSendOrderLogType(Map map);
	@SuppressWarnings("rawtypes")
	protected abstract String handleAccessDBLogType(Map map);
	@SuppressWarnings("rawtypes")
	protected abstract void log(Map map);
}
