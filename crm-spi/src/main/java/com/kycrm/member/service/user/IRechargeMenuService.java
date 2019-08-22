package com.kycrm.member.service.user;

import java.util.List;

import com.kycrm.member.domain.entity.user.RechargeMenu;

/** 
* @author wy
* @version 创建时间：2018年1月12日 上午11:28:34
*/
public interface IRechargeMenuService {
    
    /**
     * 查询充值列表,判断充值列表中是否存在该order_id
     * @author: jackstraw_yu
     * @time: 2018年1月12日 下午12:01:53
     * @param mid
     * @return 充值对象
     */
    public RechargeMenu queryRechargeMenu(String mid) ;

    /**
     * 查询充值服务项
     * @author HL
     * @time 2018年1月31日 下午5:28:14 
     * @return
     */
	public List<RechargeMenu> queryRechargeMenuList();
}
