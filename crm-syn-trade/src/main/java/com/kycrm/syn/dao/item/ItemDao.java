package com.kycrm.syn.dao.item;

import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.syn.dao.item.example.ItemExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ItemDao {
    long countByExample(ItemExample example);

    int deleteByExample(ItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Item record);

    int insertSelective(Item record);

    List<Item> selectByExample(ItemExample example);

    Item selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Item record, @Param("example") ItemExample example);

    int updateByExample(@Param("record") Item record, @Param("example") ItemExample example);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);
    
    List<Item> ItemIsExit(Item item);

	void updateItem(Item item);
}