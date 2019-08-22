package com.kycrm.tmc.core.mybatis;


import java.util.List;
import java.util.Map;

public interface SysInfoBatisDao {

    <E> List<E> findList(String namespace, String statementId, Object param);
    
    <E> List<E> findList(String namespace, String statementId, Map<String, Object> parameters);

    <E> List<E> findLimitList(String namespace, String statementId, Map<String, Object> parameters, Integer top);

    <V> Map<String, V> findMap(String namespace, String statementId, Map<String, Object> parameters, String mapKey);

    <V> Map<String, V> findMap(String namespace, String statementId, Map<String, Object> parameter, String mapKey, Integer top);

    /**
	 * 查询一个对象
	 * 
	 * @param namespace
	 * @param statementId
	 * @param param
	 * @return
	 */
	<T> T findBy(String namespace, String statementId, Object param);


    /**
     * 执行更新操作，包括insert、update、delete
     * @param namespace 一般用实体类的全路径,如 User.class.getName()
     * @param statementId insert、update、delete等语句标识
     * @param parameters 操作所需参数对象，可能是实体对象或Map或其他类型等
     * @return 注意: 返回的是操作影响的记录数,不是主键
     */
    //由于本框架优先采用Hibernate进行数据管理，因此MyBatis仅用于复杂的查询之用，不做数据更新操作以免干扰Hibernate缓存
    int execute(String namespace, String statementId, Object parameters);
    
    String execute1(String namespace, String statementId, Object parameters);
    
    public void commit();
}
