package com.kycrm.tmc.core.handle;

import java.util.List;
import java.util.Map;

import com.kycrm.tmc.core.handle.exception.HandlerException;

/**
 * @ClassName: HandlerChain <br/>
 * @Description: Interfaces can operate <br/>
 * @CreateDate: 2017年03月07日 下午4:34:55 <br/>
 * @author zlp
 * @version V1.0
 */
public interface HandlerChain {

	/**
	 * do the handle in list 
	 * @param source
	 * @throws HandlerException
	 */
	public void doHandle(@SuppressWarnings("rawtypes") Map t) throws HandlerException;
    
	/**
	 * add handler to handler list
	 * @param handle
	 * @return
	 */
    public HandlerChain addHandler(Handler handler);
    
    /**
     * Set handler list to do
     * @param handlerList
     */
    public void setHandlerList(List<Handler> handlerList);
	
}
