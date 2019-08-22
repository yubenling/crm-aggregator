package com.kycrm.syn.core.handle.log;

import java.util.Map;

import org.apache.log4j.Logger;

import com.kycrm.member.domain.entity.eco.log.LogType;
import com.kycrm.syn.core.handle.Handler;


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

	@SuppressWarnings({ "rawtypes"})
	public void doHandle(Map map) throws Exception {
		LogType loginType = (LogType)map.get(LogType.class.getName());
		loger.info(loginType);
		if(LogType.LOGIN_TYPE == loginType) {
			handleLoginLogType(map);
		}
		else if(LogType.ORDER_TYPE == loginType) {
			handleOrderLogType(map);
		}
		else if(LogType.SENDORDER_TYPE == loginType) {
			handleSendOrderLogType(map);
		}
		else if(LogType.ACESSDB_TYPE == loginType) {
		    handleAccessDBLogType(map);
		}
		else if(LogType.BATCH_LOG_TYPE == loginType){
		    handleBatchLogType(map);
		}
		else{
		    throw new RuntimeException("错误的日志上传类型："+loginType);
		}
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
	protected abstract String handleBatchLogType(Map map);
	@SuppressWarnings("rawtypes")
	protected abstract void log(Map map);
}
