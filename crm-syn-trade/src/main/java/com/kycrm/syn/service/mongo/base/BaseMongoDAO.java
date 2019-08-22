package com.kycrm.syn.service.mongo.base;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.kycrm.syn.service.mongo.entity.Pageination;


public interface BaseMongoDAO<T> {

	
	 /** 
     * 通过条件查询实体(集合) 
     *  
     * @param query 
     */  
    public List<T> find(Query query,String userNickName) ;  
    
    /** 
     * 通过条件更新实体(集合) 
     *  
     * @param updateMulti 
     */  
    public void updateMulti(Query query,Update update ,String userNickName) ;  
  
    /** 
     * 通过一定的条件查询一个实体 
     *  
     * @param query 
     * @return 
     */  
    public T findOne(Query query,String userNickName) ;  
  
    /** 
     * 通过条件查询更新数据 
     *  
     * @param query 
     * @param update 
     * @return 
     */  
    public void update(Query query, Update update,String userNickName) ;  
  
    /** 
     * 保存一个对象到mongodb 
     *  
     * @param entity 
     * @return 
     */  
    public T save(T entity,String userNickName) ;  
    
    /**
     * 
    * 创建人：邱洋
    * @Title: saves 
    * @Description: TODO(将对象批量保存到数据库中) 
    * @param @param entity
    * @param @param userNickName
    * @param @return    设定文件 
    * @return long    返回类型 
    * @throws
     */
    public long saveList(List<T> T,String userNickName);
    
    /** 
     * 通过ID获取记录,并且指定了集合名(表的意思) 
     *  
     * @param id 
     * @param collectionName 
     *            集合名 
     * @return 
     */  
    public T findById(String id, String userNickName) ;  
      
    /** 
     * 分页查询 
     * @param page 
     * @param query 
     * @return 
     */  
    public Pageination<T> findPage(Pageination<T> page,Query query,String userNickName);  
      
    /** 
     * 求数据总和 
     * @param query 
     * @return 
     */  
    public long count(Query query,String userNickName);  

}
