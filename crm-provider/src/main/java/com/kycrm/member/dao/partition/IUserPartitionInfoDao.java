package com.kycrm.member.dao.partition;

import java.util.List;

import com.kycrm.member.domain.entity.partition.UserPartitionInfo;

/** 
* @author wy
* @version 创建时间：2018年1月26日 下午4:24:26
*/
public interface IUserPartitionInfoDao {
    
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
     * 创建用户分库关系记录
     * @author: wy
     * @time: 2018年1月29日 上午10:41:42
     * @param userPartitionInfo
     */
    public void doCreateUserOartitionInfo(UserPartitionInfo userPartitionInfo);
    
    /**
     * 查询用户uid有多少条记录
     * @author: wy
     * @time: 2018年1月29日 上午11:31:54
     * @param uid
     * @return
     */
    public Integer findCountByUid(Long uid);
}
