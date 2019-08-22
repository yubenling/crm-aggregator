package com.kycrm.member.dao.syntrade.item;

import java.util.List;
import java.util.Map;


import com.kycrm.member.domain.entity.item.TbItem;

public interface TbItemDaosyn {
   

    int deleteByPrimaryKey(Long numIid);

    int insert(TbItem record);

    int insertSelective(TbItem record);

  
    TbItem selectByPrimaryKey(Long numIid);

  
    int updateByPrimaryKeySelective(TbItem record);

    int updateByPrimaryKeyWithBLOBs(TbItem record);

    int updateByPrimaryKey(TbItem record);
    
    /*-------------自定义------------------*/
    List<TbItem> findItemByLimit(Map<String, Object> map);
    Long findCountBySyn(Map<String, Object> map);
}