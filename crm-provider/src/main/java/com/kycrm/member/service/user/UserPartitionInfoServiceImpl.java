package com.kycrm.member.service.user;

import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.partition.IUserPartitionInfoDao;
import com.kycrm.member.domain.entity.partition.UserPartitionInfo;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;

/** 
* @author wy
* @version 创建时间：2018年1月29日 上午10:15:49
*/
@Service("userPartitionInfoService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class UserPartitionInfoServiceImpl implements IUserPartitionInfoService {
    /**
     * 分库数据库数量
     */
    private Integer systemDBCount ;
    
    @Autowired
    private IUserPartitionInfoDao  userPartitionInfoDao;
    
    @Autowired
    private IUserInfoService  userfoService;
    
    @Autowired
    private ICacheService cacheService;
    
    
    @Override
    public List<UserPartitionInfo> findAll() {
        return this.userPartitionInfoDao.findAll();
    }

    @Override
    public UserPartitionInfo findIsHash() {
        return this.userPartitionInfoDao.findIsHash();
    }

    @Override
    public int getSystemDBCount() {
        if(this.systemDBCount==null){
            ResourceBundle resource = ResourceBundle.getBundle("dev");
            String value = resource.getString("system.db.count");
            if(ValidateUtil.isEmpty(value)){
                throw new RuntimeException("dev配置文件中没有 system.db.count参数");
            }
            this.systemDBCount = Integer.parseInt(value);
        }
        return this.systemDBCount;
    }

    @Override
    public boolean doCreateUserPartitionInfo(Long uid, String sellerName) {
        if(!this.userfoService.isExistsById(uid)){
            throw new RuntimeException("创建用户分库关系记录时，传递的用户主键ID："+uid+"卖家昵称："+sellerName+"不存在，创建错误。");
        }
        Integer count = this.userPartitionInfoDao.findCountByUid(uid);
        if(count!=0){
            return true;
        }
        UserPartitionInfo userPartitionInfo = this.findIsHash();
        Boolean isHash = userPartitionInfo.getIsHash();
        if(isHash==null){
            isHash = true;
        }
        UserPartitionInfo newUserPartitionInfo = new UserPartitionInfo();
        newUserPartitionInfo.setUid(uid);
        newUserPartitionInfo.setUserNick(sellerName);
        if(isHash){
            int dbCount = this.getSystemDBCount();
            int tableNo = (int) (uid % dbCount);//对分库个数取余
            newUserPartitionInfo.setTableNo(tableNo);
        }else{
            if(userPartitionInfo.getDesignTableNo()==null){
                throw new RuntimeException("非hash分库时，必须强制指定分库的数据库序号。");
            }
            newUserPartitionInfo.setId(uid);
            newUserPartitionInfo.setTableNo(userPartitionInfo.getDesignTableNo());
        }
        this.userPartitionInfoDao.doCreateUserOartitionInfo(newUserPartitionInfo);
        this.cacheService.putNoTime(RedisConstant.RedisCacheGroup.USER_PARTITION_CACHE, 
                RedisConstant.RediskeyCacheGroup.USER_PARTITION_CACHE_KEY+newUserPartitionInfo.getUid(), 
                String.valueOf(newUserPartitionInfo.getTableNo()),false);
        return true;
    }
}
