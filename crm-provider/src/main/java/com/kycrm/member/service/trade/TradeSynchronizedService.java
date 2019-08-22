package com.kycrm.member.service.trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.util.ValidateUtil;
import com.kycrm.util.thread.MyFixedThreadPool;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Trade;

/** 
* @author wy
* @version 创建时间：2018年1月19日 下午2:38:12
*/
@Service("tradeSynchronizedService")
public class TradeSynchronizedService {
    
    @Autowired
    private ITradeDTOService tradeService;
    
    @Autowired
    private IOrderDTOService orderService;
    
    private Logger logger = LoggerFactory.getLogger(TradeSynchronizedService.class);
    /**
     * 同步订单
     * @author: wy
     * @time: 2018年1月22日 下午5:17:16
     * @param uid
     * @param sellerNick
     * @param tradeList
     */
    public void saveTradeBySellerNick(final long uid,String sellerNick,List<Trade> tradeList){
        if(ValidateUtil.isEmpty(tradeList) || ValidateUtil.isEmpty(sellerNick)){
            this.logger.error("用户订单数据同步错误，参数错误，卖家昵称 为空或者 要保存的订单集合为空："+sellerNick);
            return ;
        }
        int orderSize =  (int)(tradeList.size()*1.5);
        final List<TradeDTO> tradeDTOList = new ArrayList<>(tradeList.size());
        final List<OrderDTO> orderDTOList = new ArrayList<>(orderSize);
        for (Trade trade : tradeList) {
            //不同卖家的订单不保存
            if(!sellerNick.equals(trade.getSellerNick())){
                break;
            }
            try {
                TradeDTO tradeDTO = this.convertTradeDTOByTaoBaoTrade(trade);
                if(tradeDTO==null){
                    break;
                }
                tradeDTO.setTradeSource(1);
                tradeDTOList.add(tradeDTO);
                List<Order> orders = trade.getOrders();
                if(ValidateUtil.isEmpty(orders)){
                    break;
                }
                long num = 0L;
                for (Order order : orders) {
                    try {
                        OrderDTO orderDTO = this.convertOrderDTOByTaoBaoOrder(order, trade,tradeDTO);
                        if(orderDTO==null){
                            break;
                        }
                        orderDTO.setTradeSource(1);
                        orderDTOList.add(orderDTO);
                        num += (orderDTO.getNum() == null ? 0 : orderDTO.getNum());
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
                if (orders != null && orders.size() > 1) {
                    tradeDTO.setNum(num);
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.logger.error("用户订单数据同步错误，主订单转换失败，tid:"+trade.getTidStr());
                break;
            }
        }
        MyFixedThreadPool.getMyFixedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                tradeService.batchInsertTradeList(uid, tradeDTOList);
            }
        });
        MyFixedThreadPool.getMyFixedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
					orderService.batchInsertOrderList(uid, orderDTOList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
    
    /**
     * 将淘宝的订单实体转换为项目中的订单实体
     * @author: wy
     * @time: 2018年1月19日 下午5:01:38
     * @param trade
     * @return
     */
    public TradeDTO convertTradeDTOByTaoBaoTrade(Trade trade){
        if(trade==null){
            return null;
        }
        TradeDTO tradeDTO = new TradeDTO();
        if(null != trade.getAdjustFee()){
            tradeDTO.setAdjustFee(new BigDecimal(trade.getAdjustFee()));
        }
        tradeDTO.setAlipayId(trade.getAlipayId());
        tradeDTO.setAlipayNo(trade.getAlipayNo());
        tradeDTO.setAlipayPoint(trade.getAlipayPoint());
        if(null != trade.getAvailableConfirmFee() ){
            tradeDTO.setAvailableConfirmFee(new BigDecimal(trade.getAvailableConfirmFee()));
        }
        tradeDTO.setBuyerAlipayNo(trade.getBuyerAlipayNo());
        tradeDTO.setBuyerArea(trade.getBuyerArea());
        tradeDTO.setBuyerCodFee(trade.getBuyerCodFee());
        tradeDTO.setBuyerEmail(trade.getBuyerEmail());
        tradeDTO.setBuyerNick(trade.getBuyerNick());
        tradeDTO.setBuyerObtainPointFee(trade.getBuyerObtainPointFee());
        tradeDTO.setBuyerRate(trade.getBuyerRate());
        if(null != trade.getCodFee()){
            tradeDTO.setCodFee(new BigDecimal(trade.getCodFee()));
        }
        tradeDTO.setCodStatus(trade.getCodStatus());
        if(null != trade.getCommissionFee()){
            tradeDTO.setCommissionFee(new BigDecimal(trade.getCommissionFee()));
        }
        tradeDTO.setConsignTime(trade.getConsignTime());
        tradeDTO.setCreated(trade.getConsignTime());
        if(null != trade.getDiscountFee()){
            tradeDTO.setDiscountFee(new BigDecimal(trade.getDiscountFee()));
        }
        tradeDTO.setEndTime(trade.getEndTime());
        tradeDTO.setHasPostFee(trade.getHasPostFee());
        tradeDTO.setIs3D(trade.getIs3D());
        tradeDTO.setIsBrandSale(trade.getIsBrandSale());
        tradeDTO.setIsDaixiao(trade.getIsDaixiao());
        tradeDTO.setIsForceWlb(trade.getIsForceWlb());
        tradeDTO.setIsLgtype(trade.getIsLgtype());
        tradeDTO.setIsPartConsign(trade.getIsPartConsign());
        tradeDTO.setIsWt(trade.getIsWt());
        tradeDTO.setModified(trade.getModified());
        tradeDTO.setPayTime(trade.getPayTime());
        if(null != trade.getPayment()){
            tradeDTO.setPayment(new BigDecimal(trade.getPayment()));
        }
        tradeDTO.setPccAf(trade.getPccAf());
        tradeDTO.setPointFee(trade.getPointFee());
        if(null != trade.getPostFee()){
            tradeDTO.setPostFee(new BigDecimal(trade.getPostFee()));
        }
        tradeDTO.setRealPointFee(trade.getRealPointFee());
        if(null != trade.getReceivedPayment()){
            tradeDTO.setReceivedPayment(new BigDecimal(trade.getReceivedPayment()));
        }
        tradeDTO.setReceiverAddress(trade.getReceiverAddress());
        tradeDTO.setReceiverCity(trade.getReceiverCity());
        tradeDTO.setReceiverDistrict(trade.getReceiverDistrict());
        tradeDTO.setReceiverMobile(trade.getReceiverMobile());
        tradeDTO.setReceiverName(trade.getReceiverName());
        tradeDTO.setReceiverState(trade.getReceiverState());
        tradeDTO.setReceiverZip(trade.getReceiverZip());
        tradeDTO.setSellerAlipayNo(trade.getSellerAlipayNo());
        tradeDTO.setSellerCanRate(trade.getSellerCanRate());
        if(null != trade.getSellerCodFee()){
            tradeDTO.setSellerCodFee(new BigDecimal(trade.getSellerCodFee()));
        } 
        tradeDTO.setSellerEmail(trade.getSellerEmail());
        tradeDTO.setSellerFlag(trade.getSellerFlag());
        tradeDTO.setSellerMobile(trade.getSellerMobile());
        tradeDTO.setSellerName(trade.getSellerName());
        tradeDTO.setSellerNick(trade.getSellerNick());
        tradeDTO.setSellerPhone(trade.getSellerPhone());
        tradeDTO.setSellerRate(trade.getSellerRate());
        tradeDTO.setShippingType(trade.getShippingType());
        tradeDTO.setSnapshotUrl(trade.getSnapshotUrl());
        tradeDTO.setStatus(trade.getStatus());
        tradeDTO.setTid(trade.getTid());
        tradeDTO.setTitle(trade.getTitle());
        if(null != trade.getTotalFee()){
            tradeDTO.setTotalFee(new BigDecimal(trade.getTotalFee()));
        }
        tradeDTO.setTradeFrom(trade.getTradeFrom());
        tradeDTO.setType(trade.getType());
        if(ValidateUtil.isNotNull(trade.getServiceTags())){
            tradeDTO.setServiceTags(JSON.toJSONString(trade.getServiceTags()));
        }
        if(ValidateUtil.isNotNull(trade.getPromotionDetails())){
            tradeDTO.setPromotionDetails(JSON.toJSONString(trade.getPromotionDetails()));
        }
        tradeDTO.setStepTradeStatus(trade.getStepTradeStatus());
        tradeDTO.setStepPaidFee(trade.getStepPaidFee());
        tradeDTO.setReceiverPhone(trade.getReceiverPhone());
        return tradeDTO;
    }
    
    /**
     * 将淘宝的子订单实体转换为项目中的子订单实体
     * @author: wy
     * @time: 2018年1月19日 下午5:02:18
     * @param order
     * @param trade
     * @return
     */
    public OrderDTO convertOrderDTOByTaoBaoOrder(Order order,Trade trade,TradeDTO tradeDTO){
        if(order==null || trade == null){
            return null;
        }
        OrderDTO orderDTO = new OrderDTO();
        if(ValidateUtil.isNotNull(order.getRefundId())){
            tradeDTO.setRefundFlag(true);
        }
        orderDTO.setItemMealName(order.getItemMealName());
        orderDTO.setPicPath(order.getPicPath());
        orderDTO.setSellerNick(trade.getSellerNick());
        orderDTO.setBuyerNick(trade.getBuyerNick());
        orderDTO.setRefundStatus(order.getRefundStatus());
        orderDTO.setOuterIid(order.getOuterIid());
        orderDTO.setSnapshotUrl(order.getSnapshotUrl());
        orderDTO.setSnapshot(order.getSnapshot());
        orderDTO.setTimeoutActionTime(order.getTimeoutActionTime());
        orderDTO.setBuyerRate(order.getBuyerRate());
        orderDTO.setSellerType(order.getSellerType());
        orderDTO.setCid(order.getCid());
        orderDTO.setSubOrderTaxFee(order.getSubOrderTaxFee());
        orderDTO.setSubOrderTaxRate(order.getSubOrderTaxRate());
        orderDTO.setEstimateConTime(order.getEstimateConTime());
        orderDTO.setOid(order.getOid());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setTitle(order.getTitle());
        orderDTO.setType(order.getType());
        orderDTO.setIid(order.getIid());
        if(null != order.getPrice()){
            orderDTO.setPrice(new BigDecimal(order.getPrice()));
        }
        orderDTO.setNumIid(order.getNumIid());
        orderDTO.setItemMealId(order.getItemMealId());
        orderDTO.setSkuId(order.getSkuId());
        orderDTO.setNum(order.getNum());
        orderDTO.setOuterSkuId(order.getOuterSkuId());
        orderDTO.setOrderFrom(order.getOrderFrom());
        if(null != order.getTotalFee()){
            orderDTO.setTotalFee(new BigDecimal(order.getTotalFee()));
        }
        if(null != order.getPayment()){
            orderDTO.setPayment(new BigDecimal(order.getPayment()));
        }
        if(null != order.getDiscountFee()){
            orderDTO.setDiscountFee(new BigDecimal(order.getDiscountFee()));
        }
        if(null != order.getAdjustFee()){
            orderDTO.setAdjustFee(new BigDecimal(order.getAdjustFee()));
        }
        orderDTO.setModified(order.getModified());
        orderDTO.setSkuPropertiesName(order.getSkuPropertiesName());
        orderDTO.setRefundId(order.getRefundId());
        orderDTO.setIsOversold(order.getIsOversold());
        orderDTO.setIsServiceOrder(order.getIsServiceOrder());
        orderDTO.setEndTime(order.getEndTime());
        orderDTO.setConsignTime(order.getConsignTime());
        orderDTO.setOrderAttr(order.getOrderAttr());
        orderDTO.setShippingType(order.getShippingType());
        orderDTO.setBindOid(order.getBindOid());
        orderDTO.setLogisticsCompany(order.getLogisticsCompany());
        orderDTO.setInvoiceNo(order.getInvoiceNo());
        orderDTO.setIsDaixiao(order.getIsDaixiao());
        if(null != order.getDiscountFee()){
            orderDTO.setDivideOrderFee(new BigDecimal(order.getDiscountFee()));
        }
        orderDTO.setPartMjzDiscount(order.getPartMjzDiscount());
        orderDTO.setTicketOuterId(order.getTicketOuterId());
        orderDTO.setTicketExpDateKey(order.getTicketExpdateKey());
        orderDTO.setStoreCode(order.getStoreCode());
        orderDTO.setIsWww(order.getIsWww());
        orderDTO.setTmserSpuCode(order.getTmserSpuCode());
        orderDTO.setBindOids(order.getBindOids());
        orderDTO.setZhengjiStatus(order.getZhengjiStatus());
        orderDTO.setMdQualification(order.getMdQualification());
        if(null != order.getMdFee()){
            orderDTO.setMdFee(new BigDecimal(order.getMdFee()));
        }
        orderDTO.setCustomization(order.getCustomization());
        orderDTO.setInvType(order.getInvType());
        orderDTO.setXxx(order.getXxx());
        orderDTO.setIsShShip(order.getIsShShip());
        orderDTO.setfType(order.getfType());
        orderDTO.setfStatus(order.getfStatus());
        orderDTO.setfTerm(order.getfTerm());
        orderDTO.setComboId(order.getComboId());
        orderDTO.setAssemblyRela(order.getAssemblyRela());
        orderDTO.setAssemblyPrice(order.getAssemblyPrice());
        orderDTO.setAssemblyItem(order.getAssemblyItem());
        orderDTO.setSubOrderTaxPromotionFee(order.getSubOrderTaxPromotionFee());
        orderDTO.setFqgNum(order.getFqgNum());
        orderDTO.setIsFqgSFee(order.getIsFqgSFee());
        if(null != trade.getPayment()){
            orderDTO.setTradePayment(new BigDecimal(trade.getPayment()));
        }
        orderDTO.setTradeSellerRate(trade.getSellerRate());
        orderDTO.setReceiverName(trade.getReceiverName());
        orderDTO.setReceiverState(trade.getReceiverState());
        orderDTO.setReceiverAddress(trade.getReceiverAddress());
        orderDTO.setReceiverZip(trade.getReceiverZip());
        orderDTO.setReceiverMobile(trade.getReceiverMobile());
        orderDTO.setReceiverPhone(trade.getReceiverPhone());
        orderDTO.setTradeConsignTime(trade.getConsignTime());
        if(null != trade.getReceivedPayment()){
            orderDTO.setTradePayment(new BigDecimal(trade.getReceivedPayment()));
        }
        orderDTO.setReceiverCountry(trade.getReceiverCountry());
        orderDTO.setReceiverTown(trade.getReceiverTown());
        orderDTO.setShopPick(trade.getShopPick());
        orderDTO.setTid(trade.getTid());
        orderDTO.setTradeNum(trade.getNum());
        orderDTO.setTradeStatus(trade.getStatus());
        orderDTO.setTradeTitle(trade.getTitle());
        orderDTO.setTradeType(trade.getType());
        if(null != trade.getTotalFee()){
            orderDTO.setTradeTotalFee(new BigDecimal(trade.getTotalFee()));
        }
        orderDTO.setTradeCreated(trade.getCreated());
        orderDTO.setTradePayTime(trade.getPayTime());
        orderDTO.setTradeModified(trade.getModified());
        orderDTO.setTradeEndTime(trade.getEndTime());
        orderDTO.setBuyerFlag(trade.getBuyerFlag());
        orderDTO.setSellerFlag(trade.getSellerFlag());
        orderDTO.setStepTradeStatus(trade.getStepTradeStatus());
        if(null != trade.getStepPaidFee()){
            orderDTO.setStepPaidFee(new BigDecimal(trade.getStepPaidFee()));
        }
        orderDTO.setTradeShippingType(trade.getShippingType());
        if(null != trade.getBuyerCodFee()){
            orderDTO.setBuyerCodFee(new BigDecimal(trade.getBuyerCodFee()));
        }
        if(null != trade.getAdjustFee()){
            orderDTO.setTradeAdjustFee(new BigDecimal(trade.getAdjustFee()));
        }
        orderDTO.setTradeFrom(trade.getTradeFrom());
        orderDTO.setTradeBuyerRate(trade.getBuyerRate());
        orderDTO.setReceiverCity(trade.getReceiverCity());
        orderDTO.setReceiverDistrict(trade.getReceiverDistrict());
        return orderDTO;
    }
}
