package com.kycrm.syn.dao.itemImport;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.item.ItemImport;
import com.kycrm.syn.dao.itemImport.example.ItemImportExample;

public interface ItemImportDao {
    long countByExample(ItemImportExample example);

    int deleteByExample(ItemImportExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ItemImport record);

    int insertSelective(ItemImport record);

    List<ItemImport> selectByExample(ItemImportExample example);

    ItemImport selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ItemImport record, @Param("example") ItemImportExample example);

    int updateByExample(@Param("record") ItemImport record, @Param("example") ItemImportExample example);

    int updateByPrimaryKeySelective(ItemImport record);

    int updateByPrimaryKey(ItemImport record);
}