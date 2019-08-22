package com.kycrm.tmc.sysinfo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.tmc.core.mybatis.SysInfoBatisDao;
import com.kycrm.tmc.sysinfo.entity.Refund;
import com.kycrm.tmc.sysinfo.entity.TbTransactionOrder;
import com.kycrm.tmc.util.JudgeUserUtil;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.ApiException;
import com.taobao.api.SecretException;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.response.TradeFullinfoGetResponse;

/**
 * @ClassName: TransactionOrderService
 * @author: jackstraw_yu
 * @date: 2017年3月21日 上午10:29:00
 * 
 */
@Service
public class TransactionOrderService {

	
	private static final Log logger = LogFactory.getLog(TransactionOrderService.class);
    @Autowired
    private JudgeUserUtil judgeUserUtil;

    @Autowired
    private SysInfoBatisDao sysInfoBatisDao;
    
    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 查询订单的状态
     * 
     * @author: wy
     * @time: 2017年10月18日 下午3:00:04
     * @param tid
     * @return
     */
    public String queryTradeStatus(String tid) {
        String status = sysInfoBatisDao.findBy(TbTransactionOrder.class.getName(), "findTradeStatus",
                Long.parseLong(tid));
        if (ValidateUtil.isEmpty(status)) {
            try {
                Thread.sleep(3000L);
                status = sysInfoBatisDao.findBy(TbTransactionOrder.class.getName(), "findTradeStatus", Long.parseLong(tid));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    /**
     * @Title: queryTrade
     * @Description: (通过tid获得trade对象)
     * @param: @param
     *             tid
     * @param: @return
     * @return: Trade
     * @throws @date:
     *             2017年3月21日 上午10:28:47
     * @author: jackstraw_yu
     */
    public Trade queryTrade(String tid,Long uid) {
        // 定义要保存的数据
        Trade trade = null;
        // 1,根据tid查询出TbTransactionOrder(自定义对象)
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("tid", tid);
        TbTransactionOrder tbTransactionOrder = sysInfoBatisDao.findBy(TbTransactionOrder.class.getName(),"getTbTransactionOrders", hashMap);
        if (tbTransactionOrder == null) {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tbTransactionOrder = sysInfoBatisDao.findBy(TbTransactionOrder.class.getName(), "getTbTransactionOrders",hashMap);
        }
        if (tbTransactionOrder != null && !"".equals(tbTransactionOrder.getJdpResponse())) {
            // 解析JdpResponse
            String jdp_response = tbTransactionOrder.getJdpResponse();
            TradeFullinfoGetResponse rsp = null;
            try {
                rsp = TaobaoUtils.parseResponse(jdp_response, TradeFullinfoGetResponse.class);
            } catch (ApiException e) {
                e.printStackTrace();
            }

            if (rsp != null) {
                trade = rsp.getTrade();
            }

        }
        
       if (trade != null) {
        	//如果uid等于null,则是物流消息，物流消息没法传递uid,需要重新取一遍uid
    	   UserInfo userinfo = userInfoService.findUserInfoByTmc(trade.getSellerNick());
        	if(uid==null){
        		if(userinfo==null||userinfo.getAccessToken()==null)return null;
        		uid=userinfo.getId();
        	}
        	
            //TODO redis查询用户sessionKey，是否用卖家昵称查询
            String sessionKey =userinfo.getAccessToken();
            try {
                if (EncrptAndDecryptClient.isEncryptData(trade.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
                    if (sessionKey == null) {
                        return null;
                    }
                    //判断买家昵称是否为密文，如果是则解密
                    trade.setBuyerNick(judgeUserUtil.getDecryptData(trade.getBuyerNick(), EncrptAndDecryptClient.SEARCH,
                            null, sessionKey));
                    //判断买家手机号是否为密文，如果是则解密
                    trade.setReceiverMobile(judgeUserUtil.getDecryptData(trade.getReceiverMobile(),
                            EncrptAndDecryptClient.PHONE, null, sessionKey));
                    //判断买家座机是否为密文，如果是则解密
                    trade.setReceiverPhone(judgeUserUtil.getDecryptData(trade.getReceiverPhone(),
                            EncrptAndDecryptClient.SIMPLE, null, sessionKey));
                    // 判断买家姓名是否为密文，如果是则解密
                    trade.setReceiverName(judgeUserUtil.getDecryptData(trade.getReceiverName(),
                            EncrptAndDecryptClient.SEARCH, null, sessionKey));
                    // 判断收货人街道地址是否为密文，如果是则解密
                    trade.setReceiverAddress(judgeUserUtil.getDecryptData(trade.getReceiverAddress(),
                            EncrptAndDecryptClient.SEARCH, null, sessionKey));
                }
                if (trade.getReceiverMobile() == null) {
                    trade.setReceiverMobile(trade.getReceiverPhone());
                }
                trade.setTid(Long.parseLong(tid));
                if (trade.getTradeFrom() == null) {
                    for (Order order : trade.getOrders()) {
                        if (order.getOrderFrom() != null) {
                            trade.setTradeFrom(order.getOrderFrom());
                            break;
                        }
                    }
                }
            } catch (SecretException e) {
            	logger.info(uid+"获取session失败"+e+"失效的token为"+sessionKey);
                e.printStackTrace();
                return null;
            }
        }
        return trade;
    }

    /**
     * 查询sysInfo表中的订单，不休眠，非tmc查询
     * 
     * @author: wy
     * @time: 2017年11月23日 下午4:46:05
     * @param tid
     *            主订单号
     * @return
     */
    public Trade queryTradeByNoTmc(String tid) {
        // 定义要保存的数据
        Trade trade = null;
        // 1,根据tid查询出TbTransactionOrder(自定义对象)
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("tid", tid);
        TbTransactionOrder tbTransactionOrder = sysInfoBatisDao.findBy(TbTransactionOrder.class.getName(),
                "getTbTransactionOrders", hashMap);
        if (tbTransactionOrder != null && !"".equals(tbTransactionOrder.getJdpResponse())) {
            // 解析JdpResponse
            String jdp_response = tbTransactionOrder.getJdpResponse();
            TradeFullinfoGetResponse rsp = null;
            try {
                rsp = TaobaoUtils.parseResponse(jdp_response, TradeFullinfoGetResponse.class);
            } catch (ApiException e) {
                e.printStackTrace();
            }

            if (rsp != null) {
                trade = rsp.getTrade();
            }

        }
        if (trade != null) {
            String sessionKey = this.judgeUserUtil.getSessionKeyBySellerName(trade.getSellerNick());
            try {
                if (EncrptAndDecryptClient.isEncryptData(trade.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
                    if (sessionKey == null) {
                        return null;
                    }
                    // 判断买家昵称是否为密文，如果是则解密
                    trade.setBuyerNick(judgeUserUtil.getDecryptData(trade.getBuyerNick(), EncrptAndDecryptClient.SEARCH,
                            null, sessionKey));
                    // 判断买家手机号是否为密文，如果是则解密
                    trade.setReceiverMobile(judgeUserUtil.getDecryptData(trade.getReceiverMobile(),
                            EncrptAndDecryptClient.PHONE, null, sessionKey));
                    // 判断买家座机是否为密文，如果是则解密
                    trade.setReceiverPhone(judgeUserUtil.getDecryptData(trade.getReceiverPhone(),
                            EncrptAndDecryptClient.SIMPLE, null, sessionKey));
                    // 判断买家姓名是否为密文，如果是则解密
                    trade.setReceiverName(judgeUserUtil.getDecryptData(trade.getReceiverName(),
                            EncrptAndDecryptClient.SEARCH, null, sessionKey));
                    // 判断收货人街道地址是否为密文，如果是则解密
                    trade.setReceiverAddress(judgeUserUtil.getDecryptData(trade.getReceiverAddress(),
                            EncrptAndDecryptClient.SEARCH, null, sessionKey));
                }
                if (trade.getReceiverMobile() == null) {
                    trade.setReceiverMobile(trade.getReceiverPhone());
                }
                trade.setTid(Long.parseLong(tid));
                if (trade.getTradeFrom() == null) {
                    for (Order order : trade.getOrders()) {
                        if (order.getOrderFrom() != null) {
                            trade.setTradeFrom(order.getOrderFrom());
                            break;
                        }
                    }
                }
            } catch (SecretException e) {
                return null;
            }
        }
        return trade;
    }
    //判断支付时间，是不是在开始时间和结束时间之间
    public boolean queryTradePaymentTime(Long tid, Date startTime, Date endTime) {
        if (startTime == null || startTime == null || endTime == null) {
            return false;
        }
        HashMap<String, Object> hashMap = new HashMap<String, Object>(5);
        hashMap.put("tid", tid);
        TbTransactionOrder tbTransactionOrder = sysInfoBatisDao.findBy(TbTransactionOrder.class.getName(),
                "getTbTransactionOrders", hashMap);
        if (tbTransactionOrder != null && !"".equals(tbTransactionOrder.getJdpResponse())) {
            // 解析JdpResponse
            String jdp_response = tbTransactionOrder.getJdpResponse();
            if (jdp_response.contains("pay_time")) {
                JSONObject reuslt = JSONObject.parseObject(jdp_response);
                String paymentTimeString = reuslt.getJSONObject("trade_fullinfo_get_response").getJSONObject("trade")
                        .getString("pay_time");
                try {
                    Date paymentTime = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss").parse(paymentTimeString);
                    Long pay = paymentTime.getTime();
                    if (startTime.getTime() <= pay && endTime.getTime() >= pay) {
                        return true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 创建人：邱洋 @Title: getRefundNumber @Description:
     * (根据用户昵称查询退款中的数据) @param @param userId @param @return 设定文件 @return int
     * 返回类型 @throws
     */
    public int getRefundNumber(String userId, Date bTime, Date eTime) {
        int count = 0;
        Map<String, Object> map = new HashMap<String, Object>(7);
        map.put("userId", userId);
        map.put("bTime", bTime);
        map.put("eTime", eTime);
        List<String> statusList = sysInfoBatisDao.findList(Refund.class.getName(), "getRefundNumber", map);
        for (String status : statusList) {
            if ("WAIT_SELLER_AGREE".equals(status) || "WAIT_BUYER_RETURN_GOODS".equals(status)
                    || "WAIT_SELLER_CONFIRM_GOODS".equals(status) || "SELLER_REFUSE_BUYER".equals(status)) {
                count++;
            }
        }
        return count;
    }
}
