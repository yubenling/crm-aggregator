
package com.kycrm.tmc.core.handle.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kycrm.tmc.core.handle.Handler;
import com.kycrm.tmc.core.handle.HandlerChain;
import com.kycrm.tmc.core.handle.exception.HandlerException;



/**
 * @ClassName: DefaultHandlerChain <br/>
 * @Description: The default business chain <br/>
 * @CreateDate: 2017年03月07日 下午4:34:55 <br/>
 * @author zlp
 * @version V1.0
 */
public class DefaultHandlerChain implements HandlerChain {

	protected List<Handler> handlerList = new ArrayList<Handler>();
	
	@Override
	public HandlerChain addHandler(Handler handler) {
		this.handlerList.add(handler);
		return this;
	}

	@Override
    @SuppressWarnings("rawtypes")
	public void doHandle(Map t) throws HandlerException {

		for (Handler handler : handlerList) {
			handler.doHandle(t);
	    }

	}

	@Override
    public void setHandlerList(List<Handler> handlerList) {
		this.handlerList = handlerList;
	}

}
