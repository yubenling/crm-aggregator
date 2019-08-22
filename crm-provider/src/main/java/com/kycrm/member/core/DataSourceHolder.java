package com.kycrm.member.core;

/**
 * Function:动态数据源
 * @author chenjiec
 *         Date: 2017/1/2 上午12:19
 * @since JDK 1.7
 */
public class DataSourceHolder {
    private static final ThreadLocal<String> dataSources = new ThreadLocal<String>();

    private static final int DB_COUNT = 2;
    
    public static void setDataSources(String dataSource) {
        dataSources.set(dataSource);
    }

    public static String getDataSources() {
        return dataSources.get();
    }
    
    public static void clearDataSource() {
        dataSources.remove();
    }
    
    public static String getDBKeyByUserId(int userId) {
        int dbIndex = userId % DB_COUNT;
        return "dataSource" + (++dbIndex);
    }
    
    public static void main(String[] args) {
	   System.out.println(getDBKeyByUserId(1));
	   System.out.println(getDBKeyByUserId(2));
	   System.out.println(getDBKeyByUserId(3));
	}
    
}
