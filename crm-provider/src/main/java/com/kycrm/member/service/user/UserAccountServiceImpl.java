package com.kycrm.member.service.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.core.annotation.MyDataSourceHolder;
import com.kycrm.member.core.redis.DistributedLock;
import com.kycrm.member.dao.user.IUserAccountDao;
import com.kycrm.member.domain.entity.user.UserAccount;
import com.kycrm.member.domain.entity.user.UserOperationLog;
import com.kycrm.member.exception.KycrmApiException;
import com.kycrm.member.service.other.IMobileSettingService;
import com.kycrm.util.Constants;
import com.kycrm.util.GetOperationFunctionString;
import com.kycrm.util.ValidateUtil;




/** 
* @author wy
* @version 创建时间：2017年11月13日 下午3:04:49
*/
@MyDataSource(MyDataSourceAspect.MASTER)
@Service("userAccountService")
public class UserAccountServiceImpl implements IUserAccountService{
    @Autowired
    private UserTmcListernService userTmcListernService;
    
    @Autowired
    private DistributedLock distributedLock;
    
    @Autowired
    private JudgeUserUtil judgeUserUtil;
    
    @Resource(name = "redisTemplateLock")
    private StringRedisTemplate redisTemplate;

    @Autowired
    private IUserAccountDao userAccountDao;
    
    @Autowired
    private IUserInfoService userInfoService;
    
    @Autowired
    private IUserOperationLogService userOperationLogService;
    
    @Autowired
    private IMobileSettingService mobileSettingService;
    
    private  Logger logger = LoggerFactory.getLogger(UserAccountServiceImpl.class);
    
    /**
     * 创建用户账户信息
     * @author: wy
     * @time: 2017年11月13日 下午3:18:51
     * @param sellerNick 卖家昵称，不可为空
     * @param smsNum 短信余额，如果为空默认为0
     * @return 如果创建成功返回true，失败返回false
     */
    public boolean doCreateUserAccountByUser(long uid,String sellerNick,Long smsNum){
        if(ValidateUtil.isEmpty(sellerNick)){
            return false;
        }
        if(!this.userInfoService.isExistsById(uid)){
            return false;
        }
        if(smsNum==null){
            smsNum = 0L;
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(sellerNick);
        userAccount.setSmsNum(smsNum);
        userAccount.setId(uid); 
        userAccount.setUid(uid);
        int result = this.userAccountDao.doCreateUserAccount(userAccount);
        if(result==1){
            return true;
        }
        return false;
    }
    /**
     * 创建用户账户信息
     * @author: wy
     * @time: 2017年11月13日 下午3:18:51
     * @param sellerNick 卖家昵称，不可为空
     * @param smsNum 短信余额，如果为空默认为0
     * @return 如果创建成功返回true，失败返回false
     */
    public boolean doCreateUserAccountByUser(long uid,String sellerNick,Integer smsNum){
        if(smsNum==null){
            smsNum = 0;
        }
        return this.doCreateUserAccountByUser(uid,sellerNick, (long)smsNum);
    }
    /**
     * 查询用户余额
     * @author: wy
     * @time: 2017年11月13日 下午3:23:05
     * @param sellerNick
     * @return
     */
	@Override
	public Long findUserAccountSms(Long uid) {
		MyDataSourceHolder.setDataSourceType(MyDataSourceAspect.MASTER);
		Long smsNum = this.userAccountDao.findUserAccountSms(uid);
		if (smsNum == null) {
			smsNum = 0L;
		}
		return smsNum;
	}
    /**
     * 修改用户短信余额
     * @author: wy
     * @time: 2017年11月13日 下午3:23:05
     * @param userId 用户的昵称
     * @param isDelete  true删除短信，false增加短信
     * @param smsNum  更改的短信数量（正整数）
     * @param settingType 操作的类型（下单关怀，常规催付）
     * @param operator 操作人（用户自己 填昵称，系统自动发送填 auto）
     * @param ipAdd 地址ip，可为空
     * @param remark 备注，可为空  空代表发送单条短信
     * @param isTimeOut true:有超时设置，false没超时设置
     * @return 返回boolean 用户短信余额是否修改成功 true->成功,false->失败
     */
    @Override
    public boolean doUpdateUserSms(Long uid,String userId,Boolean isDelete,int smsNum,
            String settingType,String operator,String ipAdd,String remark,boolean isTimeOut){
        long startTime = System.currentTimeMillis();
        boolean flag = false;
        if(ValidateUtil.isEmpty(uid)){
            throw new KycrmApiException("扣费时，传递的用户主键id为空！！！");
        }
        if(isDelete==null ||ValidateUtil.isEmpty(smsNum)
                ||ValidateUtil.isEmpty(settingType) ){
            return flag;
        }
        UserOperationLog userLog = new UserOperationLog();
        userLog.setFunctionGens(settingType);
        String type = isDelete?Constants.DEL_MESSEGE:Constants.ADD_MESSEGE;
        userLog.setType(type);
        userLog.setOperator(operator);
        userLog.setUid(uid);
        userLog.setUserId(userId);
        userLog.setUserId(userId);
        userLog.setFunctions(GetOperationFunctionString.getFunctionsByType(settingType));
        userLog.setDate(new Date());
        userLog.setIpAdd(ipAdd);
        userLog.setRemark(remark==null?Constants.USER_OPERATION_SINGLE:remark);
        MyDataSourceHolder.setDataSourceType(MyDataSourceAspect.MASTER);
        try {
            if(isTimeOut){
                flag = this.synUpdateUserSmsTimeOut(uid, isDelete, smsNum);
            }else{
                flag = this.synUpdateUserSmsNoTime(uid, isDelete, smsNum);
            }
        }catch (Exception e) {
            this.logger.info("用户："+userId+"扣除短信："+smsNum +"，代码错误："+e.getMessage());
            e.printStackTrace();
        } 
        finally {
            userLog.setState(flag?Constants.USER_OPERATION_LOG_SUCCESS:Constants.USER_OPERATION_LOG_FAIL);
            try {
				this.userOperationLogService.insert(userLog);
			} catch (Exception e) {
				logger.info("保存用户操作日志出错");
				e.printStackTrace();
			}
        }
        if(flag){
        	MyDataSourceHolder.setDataSourceType(MyDataSourceAspect.MASTER);
            //用户余额如果为0，则取消用户的授权
           this.doTmcUser(uid,userId,isDelete);
           //异步调用,余额提醒重置
           this.mobileSettingService.proxyResetSmsRemindMark(uid);
        }
        this.logger.info("用户："+userId+"扣除短信："+smsNum+"花费了"+(System.currentTimeMillis()-startTime)+"ms");
        return flag;
    }
    /**
     * 改变用户的短信条数,超时五秒就默认为本次扣费失败
     * @param userId  用户条数
     * @param isDelete  true删除短信，false增加短信
     * @param smsNum  更改的短信数量（正整数）
     * @return 扣成功返回true,失败返回false
     */
    private  boolean synUpdateUserSmsTimeOut(Long uid,boolean isDelete,int smsNum){
        long startTime = System.currentTimeMillis();
        //RedisLock lock = new RedisLock(this.redisTemplate,uid+"_user");
        try {
            //if(lock.lock()){
                Map<String,Object> map = new HashMap<String,Object>(5);
                map.put("uid", uid);
                map.put("smsNum", smsNum);
                try {
                	MyDataSourceHolder.setDataSourceType(MyDataSourceAspect.MASTER);
                    //扣短信
                    if(isDelete){
                        long userSmsCount = this.findUserAccountSms(uid);
                        if(smsNum>userSmsCount){
                            return false;
                        }
                        this.userAccountDao.doDeleteUserSms(map);
                        return true;
                    }
                    //增加短信
                    else {
                        this.userAccountDao.doAddUserSms(map);
                        return true;
                    }
                } catch (Exception e) {
                    this.logger.error("严重，警急！！！用户扣费错误。卖家id："+uid+",短信数量："+smsNum+"是否删除短信："+isDelete +"  错误："+e.getMessage()) ;
                    e.printStackTrace();
                    return false;
                }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
           // lock.unlock();
            long endTime = System.currentTimeMillis();
            if((endTime-startTime)>4000){
                this.logger.debug("用户扣费超时！！！ "+(endTime-startTime)+"ms  ," + uid);
            }
        }
        return false;
    }
    /**
     * 改变用户的短信条数，没有超时时间，直到数据库返回处理结果
     * @param userId  用户条数
     * @param isDelete  true删除短信，false增加短信
     * @param smsNum  更改的短信数量（正整数）
     * @return 扣成功返回true,失败返回false
     */
    private  boolean synUpdateUserSmsNoTime(Long uid,boolean isDelete,int smsNum){
        long startTime = System.currentTimeMillis();
        Map<String,Object> map = new HashMap<String,Object>(5);
        map.put("uid", uid);
        map.put("smsNum", smsNum);
        try {
          //  if(this.distributedLock.tryLock(uid+"_user")){
                //扣短信
                if(isDelete){
                    long userSmsCount = this.findUserAccountSms(uid);
                    if(smsNum>userSmsCount){
                        return false;
                    }
                    this.userAccountDao.doDeleteUserSms(map);
                    return true;
                }
                //增加短信
                else {
                    this.userAccountDao.doAddUserSms(map);
                    return true;
               }
            /*}else{
                this.logger.debug("用户扣费竞争锁失败！uid: " + uid);
            }*/
        } catch (Exception e) {
            this.logger.error("严重，警急！！！用户扣费错误。卖家id："+uid+" 短信数量："+smsNum+"是否删除短信："+isDelete +"  错误："+e.getMessage()) ;
            e.printStackTrace();
            return false;
        }finally{
            //this.distributedLock.unLock(uid+"_user");
            long endTime = System.currentTimeMillis();
            if((endTime-startTime)>4000){
                this.logger.debug("用户扣费超时！！！ "+(endTime-startTime)+"ms  ," + uid);
            }
        }
    }
    /**
     * 查询设置是否存在
     * @author: wy
     * @time: 2017年11月13日 下午4:43:06
     * @param userId 卖家昵称
     * @return 存在返回true，不存在返回false
     */
    public boolean findExistsUserAccount(String userId){
        int reuslt = this.userAccountDao.findExistsUser(userId);
        if(reuslt==1){
            return true;
        }
        return false;
    }
    /**
     * 充值给用户添加授权，扣费时校验如果余额为0取消用户授权
     * @author: wy
     * @time: 2017年10月10日 下午12:00:24
     * @param sellerNick 卖家昵称
     */
    @Override
    public void doTmcUser(final Long uid,final String sellerName,final boolean isDelete){
        /*MyFixedThreadPool.getMyFixedThreadPool().execute(new Runnable() {
            @Override
            public void run() {*/
                try {
                    if(isDelete){
                    	MyDataSourceHolder.setDataSourceType(MyDataSourceAspect.MASTER);
                        long smsNum = findUserAccountSms(uid);
                        if(smsNum<=0){
                            String sessionKey = judgeUserUtil.getUserTokenByRedis(uid);
                            userTmcListernService.openUserPermit(sessionKey, null);
                        }
                    }else {
                        userTmcListernService.addUserPermitByMySql(uid,sellerName, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
       /* });*/
    /*}*/
    
    
}
