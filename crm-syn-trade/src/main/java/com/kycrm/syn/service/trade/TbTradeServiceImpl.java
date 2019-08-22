package com.kycrm.syn.service.trade;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.trade.TbTrade;
import com.kycrm.member.service.trade.ITbTransactionOrderService;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.core.mybatis.MyDataSourceAspect;
import com.kycrm.syn.dao.trade.ITbTransactionOrderDao;

/** 
* @author wy
* @version 创建时间：2018年1月29日 下午4:14:33
*/
@Service
@MyDataSource(MyDataSourceAspect.SYSINFO)
public class TbTradeServiceImpl implements ITbTransactionOrderService{
    @Autowired
    private ITbTransactionOrderDao tbTransactionOrderDao;
    
    @Override
    public Long findCountByDate(Date startTime,Date endTime){
        Map<String,Date> map = new HashMap<>(5);
        map.put("beginTime", startTime);
        map.put("endTime", endTime);
        return this.tbTransactionOrderDao.findCountBySyn(map);
    }
    
    @Override
    public List<TbTrade> findTradeByLimit(Date beginTime,Date endTime){
        Map<String,Object> map = new HashMap<>(8);
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        return this.tbTransactionOrderDao.findTradeListBySyn(map);
    }
}
