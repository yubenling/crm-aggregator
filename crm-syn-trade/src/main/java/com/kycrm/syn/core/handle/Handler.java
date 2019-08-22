/*
 * create on 2007-10-12
 * Copy right (2007)
 * HNA System All rights reserved
 */
package com.kycrm.syn.core.handle;

import java.util.Map;


/**
 * @ClassName: Handler <br/>
 * @Description: The top-level interface <br/>
 * @CreateDate: 2016年4月27日 下午4:31:01 <br/>
 * @author Toby
 * @version V1.0
 */
public interface Handler {
	
	 public void doHandle(@SuppressWarnings("rawtypes") Map map) throws Exception;
	 
}
