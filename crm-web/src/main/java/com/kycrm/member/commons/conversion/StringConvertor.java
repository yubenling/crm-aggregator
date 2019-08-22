package com.kycrm.member.commons.conversion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;


/** 
* @ClassName StringConvertor 
* @Description 转换器:String字符串 将两端空格直接trim(),如果为"   "直接返回null
* @author jackstraw_yu
* @date 2018年1月16日 上午10:10:07 
*  
*/
public class StringConvertor  implements Converter<String, String>  {

	private static Logger logger = LoggerFactory.getLogger(StringConvertor.class);
	
	@Override
	public String convert(String source) {
		try {
			// "  ",""等字符串全部返回null
			if(null == source || "".equals(source.trim()))
				return null;
			return source.trim();
		} catch (Exception e) {
			logger.error("StringConvertor Exception :"+e.getMessage());
		}
		return null;
	}
}
