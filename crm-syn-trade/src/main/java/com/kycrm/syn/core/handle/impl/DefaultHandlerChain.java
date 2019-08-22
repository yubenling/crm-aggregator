package com.kycrm.syn.core.handle.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kycrm.syn.core.handle.Handler;
import com.kycrm.syn.core.handle.HandlerChain;



/**
 * @ClassName: DefaultHandlerChain <br/>
 * @Description: The default business chain <br/>
 * @CreateDate: 2017年03月07日 下午4:34:55 <br/>
 * @author zlp
 * @version V1.0
 */
public class DefaultHandlerChain implements HandlerChain {

	protected List<Handler> handlerList = new ArrayList<Handler>();

	public HandlerChain addHandler(Handler handler) {
		this.handlerList.add(handler);
		return this;
	}

	@SuppressWarnings("rawtypes")
	public void doHandle(Map t) throws Exception {

		for (Handler handler : handlerList) {
			handler.doHandle(t);
	    }

	}

	public void setHandlerList(List<Handler> handlerList) {
		this.handlerList = handlerList;
	}

}
