package com.kycrm.util;
/** 
* @author wy
* @version 创建时间：2018年1月22日 下午3:03:39
*/
public class TradeStatusCompareUtil {
    private TradeStatusCompareUtil(){};
    
    /**
     * 订单状态比较
     * @author: wy
     * @time: 2018年1月22日 下午3:36:32
     * @param fistStatus 第一个订单状态
     * @param secondStauts 第二个订单状态
     * @return 如果第一个订单状态比第二个订单要新（最新的订单状态）则返回1  ，如果相同返回0，第二个订单状态最新则返回-1
     */
    public static int compareTo(String fistStatus,String secondStauts){
        int first = TradeStatusCompareUtil.tradeStatusToInteger(fistStatus);
        int second = TradeStatusCompareUtil.tradeStatusToInteger(secondStauts);
        if(first == -1 && second == -1){
            throw new RuntimeException("订单状态传递的数据错误，无法比较，第一个订单状态为："+fistStatus+",第二个订单状态为："+secondStauts);
        }
        if(first == -1 && second!= -1){
            return -1;
        }
        if(first != -1 && second== -1){
            return 1;
        }
        if(first == second){
            return 0;
        }
        if(first > second){
            return 1;
        }
        if(first < second){
            return -1;
        }
        return 0;
    }
    
    public static int tradeStatusToInteger(String tradeStatus){
        if(ValidateUtil.isEmpty(tradeStatus)){
            return -1;
        }
        switch (tradeStatus) {
            case TradesInfo.TRADE_NO_CREATE_PAY:{
                return 1;
            }
            case TradesInfo.WAIT_BUYER_PAY:{
                return 2;
            }
            case TradesInfo.PAY_PENDING:{
                return 3;
            }
            case TradesInfo.WAIT_SELLER_SEND_GOODS:{
                return 4;
            }
            case TradesInfo.PAID_FORBID_CONSIGN:{
                return 5;
            }
            case TradesInfo.SELLER_CONSIGNED_PART:{
                return 6;
            }
            case TradesInfo.WAIT_BUYER_CONFIRM_GOODS:{
                return 7;
            }
            case TradesInfo.TRADE_BUYER_SIGNED:{
                return 8;
            }
            case TradesInfo.WAIT_PRE_AUTH_CONFIRM:{
                return 9;
            }
            case TradesInfo.TRADE_FINISHED:{
                return 10;
            }
            case TradesInfo.REFUND_CREATED:{
                return 11;
            }
            case TradesInfo.REFUND_SUCCESS:{
                return 12;
            }
            case TradesInfo.REFUND_SELLER_REFUSE_AGREEMENT:{
                return 16;
            }
            case TradesInfo.TRADE_CLOSED:{
                return 14;
            }
            case TradesInfo.TRADE_CLOSED_BY_TAOBAO:{
                return 99;
            }
            default:{
                return -1;
            }
        }
    }
}
