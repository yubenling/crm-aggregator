package com.kycrm.member.domain.entity.base;

import java.util.List;

/** 
* @author wy
* @version 创建时间：2018年1月16日 上午11:19:01
*/
public class BaseListEntity<T extends ShardingTable>{
    
    private Long uid;
    /**
     * 要保存的集合
     */
    private List<T> entityList;

    public List<T> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<T> entityList) {
        this.entityList = entityList;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
    
}
