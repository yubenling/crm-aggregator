/** 
 * Project Name:s2jh4net 
 * File Name:EncrptAndDecryptUtil.java 
 * Package Name:s2jh.biz.shop.crm.manage.util 
 * Date:2017年7月25日上午10:50:21 
 * Copyright (c) 2017,  All Rights Reserved. 
 * author zlp
*/  
  
package com.kycrm.util;  

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.SecretException;
import com.taobao.api.security.SecurityClient;

/** 
 * ClassName:EncrptAndDecryptUtil <br/> 
 * Date:     2017年7月25日 上午10:50:21 <br/> 
 * @author   zlp
 * @version   1.0   
 *   
 */
public class EncrptAndDecryptClient extends SecurityClient{
	
	//模糊的加密方式  带检索需求Type：常规的为search，手机号码为 phone。  不带检索需求的Type：常规的为simple类型，手机号码为phone.
	public static final String SEARCH = "search";
	public static final String PHONE = "phone";
	public static final String SIMPLE = "simple";

	private EncrptAndDecryptClient(DefaultTaobaoClient taobaoClient,
			String randomNum) {
		super(taobaoClient, randomNum);
	}
    private static EncrptAndDecryptClient secretClient = null;
 
    public static synchronized EncrptAndDecryptClient getInstance() {
       if (secretClient == null) {
    	   secretClient = new EncrptAndDecryptClient(new DefaultTaobaoClient(Constants.TAOBAO_SECRET_URL,Constants.TOP_APP_KEY, Constants.TOP_APP_SECRET),Constants.SECURITY_TOKEN);
       }
       return secretClient;
    }
    //针对单条数据进行加密
    /**
     * 加密（每个用户单独分配秘钥）
     * 
     * @see #encrypt(String, String, String, Long)
     * @return
     */
    public String encryptData(String data,String type,String session) throws SecretException{
    	return secretClient.encrypt(data, type, session);
    }
    //针对单条数据进行加密
    /**
     * 加密（每个用户单独分配秘钥）
     * 
     * @see #encrypt(String, String, String, Long)
     * @return
     */
    public String validateAndEncryptData(String data,String type,String session) throws SecretException{
    	boolean encryptData = EncrptAndDecryptClient.isEncryptData(data, type);
		if(!encryptData){
			return secretClient.encrypt(data, type, session);
		}else{
			return data;
		}
    }
    //针对单条数据进行解密
    /**
     * 解密（每个用户单独分配秘钥）
     * 
     * @param data
     *            密文数据 手机号码格式：$手机号码前3位明文$base64(encrypt(phone后8位))$111$
     *            simple格式：~base64(encrypt(nick))~111~
     * @param type
     *            解密字段类型(例如：simple\phone)
     * @param session
     *            用户身份,用户级加密必填
     * @return
     */
    public String decryptData(String data,String type,String session) throws SecretException{
    	return secretClient.decrypt(data, type, session);
    }
    //针对单条数据进行解密
    /**
     * 解密（每个用户单独分配秘钥）
     * 
     * @param data
     *            密文数据 手机号码格式：$手机号码前3位明文$base64(encrypt(phone后8位))$111$
     *            simple格式：~base64(encrypt(nick))~111~
     * @param type
     *            解密字段类型(例如：simple\phone)
     * @param session
     *            用户身份,用户级加密必填
     * @return
     */
    public String validateAndDecryptData(String data,String type,String session) throws SecretException{
    	boolean encryptData = EncrptAndDecryptClient.isEncryptData(data, type);
		if(encryptData){
			return secretClient.decrypt(data, type, session);
		}else{
			return data;
		}
    }
    
    //针对批量条数据进行加密
    /**
     * 批量加密（每个用户单独分配秘钥）
     * 
     * @see #encrypt(String, String, String, Long)
     * @param dataList
     * @param type
     * @param session
     * @return key=明文数据，value=密文数据
     * @throws SecretException
     */
    public Map<String,String> encryptListData(List<String> dataList, String type,String session) throws SecretException{
    	return secretClient.encrypt(dataList, type, session);
    }
    //针对批量数据进行解密
    /**
     * 批量解密（每个用户单独分配秘钥）
     * 
     * @see #decrypt(String, String, String)
     * @param dataList
     * @param type
     * @param session
     * @return key=密文数据，value=明文数据
     * @throws SecretException
     */
    public Map<String,String> decryptListData(List<String> dataList, String type,String session) throws SecretException{
    	return secretClient.decrypt(dataList, type, session);
    }
}
  