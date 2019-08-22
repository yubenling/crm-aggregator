package com.kycrm.syn.service.mongo.base;
import java.util.List;

import javax.annotation.Resource;


import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.syn.service.mongo.entity.Pageination;
import com.kycrm.syn.service.mongo.entity.UserTableDTO;
import com.kycrm.syn.service.mongo.util.ReflectionUtils;
import com.kycrm.util.RedisConstant;


public abstract class BaseMongoDAOImpl<T> implements BaseMongoDAO<T>{
	
    @SuppressWarnings("unused")
	private static final int DEFAULT_SKIP = 0;  
    @SuppressWarnings("unused")
    private static final int DEFAULT_LIMIT = 100;  
    
    
	      
    /** 
     * spring mongodb　集成操作类　 
     */  
    @Resource(name="mongoTemplate")
    protected MongoTemplate mongoTemplate;  
    @Resource(name="cacheService")
    protected CacheService cacheService;  
  
    @Override  
    public List<T> find(Query query,String userNickName) {
    	String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return null ; 
    	}
        return mongoTemplate.find(query, this.getEntityClass(),collectionName);  
    }  
    
  
    @Override
	public void updateMulti(Query query, Update update, String userNickName) {
    	String collectionName = getCollectionName(userNickName);
    	if(!"".equals(collectionName)){
    		mongoTemplate.updateMulti(query, update, collectionName);
    	}
	}


	@Override  
    public T findOne(Query query,String userNickName) { 
    	String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return null ; 
    	}
	    return  mongoTemplate.findOne(query, this.getEntityClass(),collectionName); 
    }  
  
    @Override  
    public void update(Query query, Update update,String userNickName) { 
    	String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    	}else{
    		mongoTemplate.findAndModify(query, update, this.getEntityClass(),collectionName);
    	}
    }  
  
    @Override  
    public T save(T entity,String userNickName) {
    	String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return null ; 
    	}
    	mongoTemplate.save(entity,collectionName);  
        return entity;  
    }  
    
    @Override
    public long saveList(List<T> T,String userNickName){
    	String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return -1L;
    	}
		mongoTemplate.insert(T,collectionName); 
    	return 0L;
    }
  
  
    @Override  
    public T findById(String id, String userNickName) {
    	String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return null ;
    	}
        return mongoTemplate.findById(id, this.getEntityClass(), collectionName);
    }  
      
    @Override
    public Pageination<T> findPage(Pageination<T> page,Query query,String userNickName){
        long count = this.count(query,userNickName);
        page.setTotalCount(count);
        int pageNumber = page.getPageNo();
        int pageSize = page.getPageSize();
        query.skip((pageNumber - 1) * pageSize).limit(pageSize);
        List<T> rows = this.find(query,userNickName);
        page.setDatas(rows);
        return page;
    }  
      
    @Override  
    public long count(Query query,String userNickName){  
    	String collectionName = getCollectionName(userNickName);
    	if("".equals(collectionName)){
    		return 0l ; 
    	}
    	return mongoTemplate.count(query, this.getEntityClass(),collectionName); 
    }  
    
    
    /** 
     * 获取需要操作的实体类class 
     *  
     * @return 
     */  
    private Class<T> getEntityClass(){ 
    	return ReflectionUtils.getSuperClassGenricType(getClass());  
    }  
    
    
    public String getCollectionName(String userNickName){
     	String tableName="";
    	try {
			UserTableDTO userTableDTO = cacheService.get(RedisConstant.RedisCacheGroup.USRENICK_TABLE_CACHE, RedisConstant.RediskeyCacheGroup.USRENICK_TABLE_CACHE_KEY+userNickName, UserTableDTO.class);
			if(null!=userTableDTO&&null!=userTableDTO.getUserId()&&!"".equals(userTableDTO.getUserId())){
				tableName= this.getEntityClass().getSimpleName()+userTableDTO.getUserId();
			}
		} catch (Exception e) {
			tableName="";
		}
    	return  tableName;
    }
  
}
