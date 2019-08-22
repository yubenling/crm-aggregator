package com.kycrm.syn.service.item;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.item.TbItem;
import com.kycrm.member.service.item.ITbItemService;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.core.mybatis.MyDataSourceAspect;
import com.kycrm.syn.dao.item.TbItemDao;

/** 
* @author sungk
* @version 创建时间：2018年6月28日 下午4:14:33
*/
@Service
@MyDataSource(MyDataSourceAspect.SYSINFO)
public class TbItemServiceImpl implements ITbItemService {
    @Autowired
    private TbItemDao tbItemDao;
    
    @Override
    public Long findCountByDate(Date startTime,Date endTime){
//        TbItemExample example = new TbItemExample();
//        TbItemExample.Criteria criteria = new TbItemExample().createCriteria();
//        criteria.andModifiedBetween(startTime, endTime);
    	Map<String, Object> map = new HashMap<>(8);
        map.put("beginTime", startTime);
        map.put("endTime", endTime);
        return tbItemDao.findCountBySyn(map);
    }
    
    @Override
    public List<TbItem> findItemByLimit(Date beginTime,Date endTime){
    	Map<String, Object> map = new HashMap<>(8);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
    	return tbItemDao.findItemByLimit(map);
    }
}
