package com.kycrm.syn.dao.item;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.item.TbItem;
import com.kycrm.syn.dao.item.example.TbItemExample;

public interface TbItemDao {
    long countByExample(TbItemExample example);

    int deleteByExample(TbItemExample example);

    int deleteByPrimaryKey(Long numIid);

    int insert(TbItem record);

    int insertSelective(TbItem record);

    List<TbItem> selectByExampleWithBLOBs(TbItemExample example);

    List<TbItem> selectByExample(TbItemExample example);

    TbItem selectByPrimaryKey(Long numIid);

    int updateByExampleSelective(@Param("record") TbItem record, @Param("example") TbItemExample example);

    int updateByExampleWithBLOBs(@Param("record") TbItem record, @Param("example") TbItemExample example);

    int updateByExample(@Param("record") TbItem record, @Param("example") TbItemExample example);

    int updateByPrimaryKeySelective(TbItem record);

    int updateByPrimaryKeyWithBLOBs(TbItem record);

    int updateByPrimaryKey(TbItem record);
    
    /*-------------自定义------------------*/
    List<TbItem> findItemByLimit(Map<String, Object> map);
    Long findCountBySyn(Map<String, Object> map);
}