package com.kycrm.member.dao.syntrade.item;

import com.kycrm.member.domain.entity.item.Item;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ItemDaosyn {
    

    int deleteByPrimaryKey(Long id);

    int insert(Item record);

    int insertSelective(Item record);


    Item selectByPrimaryKey(Long id);

  
    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);
    
    List<Item> ItemIsExit(Item item);

	void updateItem(Item item);
}