package com.kycrm.member.service.user;

import java.util.List;

import com.kycrm.member.domain.entity.partition.UserPartitionInfo;

/** 
* @author wy
* @version 创建时间：2018年1月29日 上午10:12:57
*/
public interface IUserPartitionInfoService {
    /**
     * 获取当前最新的用户分库情况
     * @author: wy
     * @time: 2018年1月29日 上午10:13:16
     * @return
     */
    public List<UserPartitionInfo> findAll();
    
    /**
     * 查询是否根据用户主键均匀分库
     * @author: wy
     * @time: 2018年1月29日 上午10:07:17
     * @return
     *  is_hash  true 根据主键分库，false 根据指定的数据库分库
     *  design_table_no  指定的数据库名
     */
    public UserPartitionInfo findIsHash();
    /**
     * 获取当前系统分库数据库总数
     * @author: wy
     * @time: 2018年1月29日 上午10:14:27
     * @return
     */
    public int getSystemDBCount();
    
    /**
     * 创建用户分库关系
     * @author: wy
     * @time: 2018年1月29日 上午10:25:38
     * @param uid
     * @param sellerName
     * @return
     */
    public boolean doCreateUserPartitionInfo(Long uid,String sellerName);
}
