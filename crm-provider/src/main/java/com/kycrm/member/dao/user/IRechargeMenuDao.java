package com.kycrm.member.dao.user;

import java.util.List;

import com.kycrm.member.domain.entity.user.RechargeMenu;

/** 
* @author wy
* @version 创建时间：2018年1月12日 下午12:04:50
*/
public interface IRechargeMenuDao {
    
    public RechargeMenu queryRechargeMenu(String mid);

	public List<RechargeMenu> queryRechargeMenuList();
    
    
}
