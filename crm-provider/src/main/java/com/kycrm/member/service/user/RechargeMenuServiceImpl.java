package com.kycrm.member.service.user;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.user.IRechargeMenuDao;
import com.kycrm.member.domain.entity.user.RechargeMenu;

/** 
* @author wy
* @version 创建时间：2018年1月10日 下午5:02:36
*/
@Service("rechargeMenuService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class RechargeMenuServiceImpl implements IRechargeMenuService{
    
    @Autowired
    private IRechargeMenuDao rechargeMenuDao;
    
    @Override
    public RechargeMenu queryRechargeMenu(String mid) {
        RechargeMenu rechargeMenu = this.rechargeMenuDao.queryRechargeMenu(mid);
        return rechargeMenu;
    }

	@Override
	public List<RechargeMenu> queryRechargeMenuList() {
	        return this.rechargeMenuDao.queryRechargeMenuList();
	}
}