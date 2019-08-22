package com.kycrm.member.service.user;
/** 
* @author wy
* @version 创建时间：2018年1月12日 上午11:28:20
*/
public interface IUserAccountService {
    
    public static final boolean TIME_OUT = true;
    
    public static final boolean NO_TIME = false;
    
    /**
     * 创建用户账户信息
     * @author: wy
     * @time: 2017年11月13日 下午3:18:51
     * @param sellerNick 卖家昵称，不可为空
     * @param smsNum 短信余额，如果为空默认为0
     * @return 如果创建成功返回true，失败返回false
     */
    public boolean doCreateUserAccountByUser(long uid,String sellerNick,Long smsNum);
    
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
    public boolean doUpdateUserSms(Long uid,String userId,Boolean isDelete,int smsNum,
            String settingType,String operator,String ipAdd,String remark,boolean isTimeOut);
    
    
    /**
     * 查询用户余额
     * @author: wy
     * @time: 2017年11月13日 下午3:23:05
     * @param sellerNick
     * @return
     */
    public Long findUserAccountSms(Long uid);
    
    /**
     * 充值给用户添加授权，扣费时校验如果余额为0取消用户授权
     * @author: wy
     * @time: 2017年10月10日 下午12:00:24
     * @param sellerNick 卖家昵称
     */
    public void doTmcUser(Long uid,String sellerName,boolean isDelete);
    
}
