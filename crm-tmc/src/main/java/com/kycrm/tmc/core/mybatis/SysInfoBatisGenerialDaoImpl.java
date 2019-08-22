package com.kycrm.tmc.core.mybatis;
/** 
* @author wy
* @version 创建时间：2018年1月4日 下午5:01:29
*/
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

public class SysInfoBatisGenerialDaoImpl implements SysInfoBatisDao {

    private SqlSession sqlSession;
    
    @Override
    public <E> List<E> findList(String namespace, String statementId, Object param) {
        String statement = namespace + "." + statementId;
        return sqlSession.selectList(statement, param);
    }
    
    @Override
    public <E> List<E> findList(String namespace, String statementId, Map<String, Object> parameters) {
        return findLimitList(namespace, statementId, parameters, null);
    }

    @Override
    public <E> List<E> findLimitList(String namespace, String statementId, Map<String, Object> parameters, Integer top) {
        String statement = namespace + "." + statementId;
        if (top != null) {
            RowBounds rowBounds = new RowBounds(0, top);
            return sqlSession.selectList(statement, parameters, rowBounds);
        } else {
            return sqlSession.selectList(statement, parameters);
        }
    }

   

    @Override
    public <V> Map<String, V> findMap(String namespace, String statementId, Map<String, Object> parameters, String mapKey, Integer top) {
        String statement = namespace + "." + statementId;
        if (top != null) {
            RowBounds rowBounds = new RowBounds(0, top);
            return sqlSession.selectMap(statement, parameters, mapKey, rowBounds);
        } else {
            return sqlSession.selectMap(statement, parameters, mapKey);
        }
    }

    @Override
    public <V> Map<String, V> findMap(String namespace, String statementId, Map<String, Object> parameters, String mapKey) {
        return findMap(namespace, statementId, parameters, mapKey, null);
    }

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findBy(String namespace, String statementId, Object param) {
        String statement = namespace + "." + statementId;
        Object object = sqlSession.selectOne(statement, param);
        return (T) object;
    }
    
    //由于本框架优先采用Hibernate进行数据管理，因此MyBatis仅用于复杂的查询之用，不做数据更新操作以免干扰Hibernate缓存
     @Override
    public int execute(String namespace, String statementId, Object parameter) {
           String statement = namespace + "." + statementId;
           return sqlSession.update(statement, parameter);
        }

    @Override
    public String execute1(String namespace, String statementId,
            Object parameters) {
        return null;
    }

    @Override
    public void commit() {
        this.sqlSession.commit();
    }
}
