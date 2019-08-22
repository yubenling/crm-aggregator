package com.kycrm.member.service.payment;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.domain.vo.payment.AliPayVO;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.user.IUserPayBillService;
import com.kycrm.member.service.user.IUserRechargeService;
import com.kycrm.util.PayUtil;
import com.kycrm.util.SendMessageStatusInfo;

@Service("alipayService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class AlipayServiceImpl implements IAlipayService {
	private static final Log log = LogFactory.getLog(AlipayServiceImpl.class);
	@Autowired
	private IUserRechargeService userRechargeService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IUserAccountService userAccountService;
	@Autowired
	private IUserPayBillService userPayBillService;
	
	
	
    /**
	 * 处理支付通知
	 */
	@Override
	public synchronized String disposePayNotify(AliPayVO vo) {
    	log.info("*********************************处理异步支付通知--状态：("+vo.getTradeStatus()+")，订单：("+vo.getPayTrade()+")，金额：("+vo.getTotalAmount()+")");
    	UserRecharge ur= userRechargeService.getUserRechargeInfo(vo.getPayTrade());
    	Long BeforesmsNum = userAccountService.findUserAccountSms(ur.getUid());
    	if(null != ur){
			if (vo.getTradeStatus().equals(PayUtil.TRADE_FINISHED)
					|| vo.getTradeStatus().equals(PayUtil.TRADE_SUCCESS)) {
    			if("3".equals(ur.getStatus()) && PayUtil.comparePrice(vo.getTotalAmount(),ur.getRechargePrice())){
    				boolean result = this.addUserSms(ur);
					log.info("*******************************充值结果"+result);
					if(result){
						//写入打款记录
						Long pid=null;
						try {		
						    pid=userPayBillService.insertPayBill(ur);
						} catch (Exception e) {
							e.printStackTrace();
							log.error("线上充值写入打款记录出错"+ur.getId());
						}
						Long smsNum = userAccountService.findUserAccountSms(ur.getUid());
						ur.setRechargeLaterNum(smsNum);
						// 充值成功
						ur.setRechargeBeforeNum(BeforesmsNum.intValue());
						ur.setStatus("1");
						ur.setPid(""+pid);
						userRechargeService.updateUserRechargeStatus(ur);
						return "success";
					}else{
						// 充值失败
						ur.setStatus("2");
						userRechargeService.updateUserRechargeStatus(ur);
						return "";
					}
    			}else{
    				log.error("*********************************订单编号：("+vo.getPayTrade()+")不符合充值条件！！！");
    				return "";
    			}
    		}else if(vo.getTradeStatus().equals(PayUtil.TRADE_CLOSED) && "3".equals(ur.getStatus())){
    			ur.setStatus("2");
    			userRechargeService.updateUserRechargeStatus(ur);
    			log.info("*********************************自定义充值短信--订单号：("+vo.getPayTrade()+")支付关闭!");
    			return "";
    		}else{
    			log.info("*********************************自定义充值短信--订单号：("+vo.getPayTrade()+")未支付成功!");
    			return "";
    		}
    	}else{
    		log.info("*********************************自定义充值短信--订单号：("+vo.getPayTrade()+")不存在!");
    		return "";
    	}
	}


	/**
	 * @Description: 封装充值参数 
	 * @author HL
	 * @date 2017年11月17日 上午11:35:44
	 */
	private boolean addUserSms(UserRecharge ur) {
		return userAccountService.doUpdateUserSms(ur.getUid(), ur.getUserNick(), SendMessageStatusInfo.ADD_SMS,
				ur.getRechargeNum(), "短信套餐购买", ur.getUserNick(),
				null, "自定义充值，短信数量：" + ur.getRechargeNum(),
				IUserAccountService.NO_TIME);
	}
}
