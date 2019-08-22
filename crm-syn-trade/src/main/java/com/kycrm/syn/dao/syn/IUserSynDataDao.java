package com.kycrm.syn.dao.syn;

import java.util.List;

import com.kycrm.member.domain.entity.syn.UserSynData;

/** 
* @author wy
* @version 创建时间：2018年2月6日 下午2:10:15
*/
public interface IUserSynDataDao {
    /**
     * 第一次初始化     保存同步进度
     * @author: wy
     * @time: 2018年2月6日 下午6:00:58
     * @param userSynData
     */
    public void doCreateUserSynDataByInit(UserSynData userSynData);
    
    /**
     * 查询全部 同步放到redis中
     * @author: wy
     * @time: 2018年2月7日 上午10:33:53
     * @return
     */
    public List<UserSynData> findAll();
    
    /**
     * 更新订单同步状态
     * @author: wy
     * @time: 2018年2月8日 上午11:42:12
     * @param userSynData
     */
    public void doUpdateTradeStatus(UserSynData userSynData);
    /**
     * 更新会员同步状态
     * @author: wy
     * @time: 2018年2月8日 上午11:46:16
     * @param userSynData
     */
    public void doUpdateMemberStatus(UserSynData userSynData);
    /**
     * 更新短信同步状态
     * @author: wy
     * @time: 2018年2月8日 上午11:46:19
     * @param userSynData
     */
    public void doUpdateSmsRecordStatus(UserSynData userSynData);
    
    /**
     * 查询需要同步订单的用户
     * @author: wy
     * @time: 2018年3月2日 下午3:11:43
     * @return
     */
    public List<UserSynData> findTradeSynStart();
    
    /**
     * 查询需要同步短信的用户
     * @author: wy
     * @time: 2018年3月2日 下午3:11:43
     * @return
     */
    public List<UserSynData> findSmsRecordSynStart();
}
