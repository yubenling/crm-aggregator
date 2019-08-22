package com.kycrm.member.domain.entity.eco.log;

import java.util.HashMap;

/**
 * 类名称：LogType <br/>
 * 类描述：log类型 <br/>
 * 创建时间：2017年12月01日 下午2:53:54 <br/>
 * @author zlp  
 * @version V1.0
 */
public enum LogType {
	
LOGIN_TYPE("LOGINTYPE"),
    
    ORDER_TYPE("ORDERTYPE"),
    
    SENDORDER_TYPE("SENDORDERTYPE"),
    
    ACESSDB_TYPE("ACESSDBTYPE"),
    
    BATCH_LOG_TYPE("BATCHLOGTYPE");

    private final String alias;
    
    private static HashMap<String, LogType> map;
    
    static {
        map = new HashMap<String, LogType>();
        
        for (LogType type : LogType.values()) {
            map.put(type.alias, type);
        }
    }

    LogType(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return this.alias;
    }

    /**
     * 名称：valueOfAlias <br/>
     * 描述：类型 <br/>
     * @param alias
     * @return
     */
    public static LogType valueOfAlias(String alias) {
        LogType type = map.get(alias);
        if (type == null) {
            throw new IllegalArgumentException("Unknown log type alias [" + alias + "]");
        }
        return type;
    }
}
