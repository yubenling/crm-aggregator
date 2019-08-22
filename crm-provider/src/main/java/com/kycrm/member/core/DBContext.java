
/** 
* @Title: DBContext.java 
* @Package com.kycrm.member.core 
* Copyright: Copyright (c) 2017 
* Company:北京冰点零度科技有限公司 *
* @author zlp 
* @date 2018年1月3日 下午4:44:54 
* @version V1.0
*/
   package com.kycrm.member.core; 
/** 
 * @ClassName: DBContext  
 * @author zlp 
 * @date 2018年1月3日 下午4:44:54 *  
 */
public class DBContext {
	//define count of database and it must match with resources/properties/jdbc.properties
    private static final int DB_COUNT = 2;

    private static final ThreadLocal<String> tlDbKey = new ThreadLocal<String>();

    public static String getDBKey() {
        return tlDbKey.get();
    }

    public static void setDBKey(String dbKey) {
        tlDbKey.set(dbKey);
    }

    public static String getDBKeyByUserId(int userId) {
        int dbIndex = userId % DB_COUNT;
        return "dataSource" + (++dbIndex);
    }
}
