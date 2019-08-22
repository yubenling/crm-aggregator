package com.kycrm.member.service.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.queue.LogAccessQueueService;
import com.kycrm.member.dao.member.ITestDao;
import com.kycrm.member.domain.entity.eco.log.LogAccessDTO;
import com.kycrm.member.domain.entity.eco.log.LogType;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.vo.member.MemberInfoVO;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.trade.FirstSynTradeService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.util.TaoBaoClientUtil;

/**
 * @ClassName: MemberInfoServiceImpl.java
 * @Description:
 */
@Service("memberInfoService")
public class TestServiceImpl implements ITestService {

    @Autowired
    private ITestDao memberDao;

    @Autowired
    private IOrderDTOService orderManageService;

    @Autowired
    private ITradeDTOService tradeDTOService;

    @Autowired
    private FirstSynTradeService firstSynTradeService;

    @Override
    public long countMemberByParam(MemberInfoVO memberInfo) {
        // return memberDao.countMemberByParam(memberInfo);
        return 0;
    }

    // 测试多数据源，可以删除
    @Override
    public String findUserByNick(String sellerNick, String userId) {
        String userByNick = this.memberDao.findUserByNick(userId);
        return userByNick;
    }

    @Override
    public MemberInfoVO findMemberByParam(MemberInfoVO memberInfo) {

        return null;
    }

    public static void main(String[] args) {
        // System.out.println("$r9fCryxXaVYXd7JLyifkbA==$Tvj9hWuXo/eYIFoofTEZew==$1$$".length());
        System.out.println(TaoBaoClientUtil.getUserSellerGetLevel(
                "70002100728167875c978925ae9f704db175b340169e1dc36292f96a96a65d51b3d82783012074941"));
    }

    // 测试redis 可删除
    public void testRedis() {
//        this.firstSynTradeService.synTradeOneDay(4L, "优茵旗舰店",
//                "70002100029d174f1716e875ddb5e05608dbb0e6a121dd29aa1dc770b984a147b5a519b3243070394");
//        this.testSaveOrder();
//        this.testSaveTradeDTO();
        
        Map<String, Object> params = new HashMap<String, Object>();
        LogAccessDTO logAccessDTO = new LogAccessDTO();
        logAccessDTO.setUserId("");
        logAccessDTO.setUserIp("127.0.0.1");
        logAccessDTO.setAti("");
        logAccessDTO.setUrl("定时订单拉取");
        logAccessDTO.setTradeIds("1L,2L");
        logAccessDTO.setOperation("定时订单拉取");
        params.put(LogType.class.getName(), LogType.ORDER_TYPE);
        params.put(LogAccessDTO.class.getName(), logAccessDTO);
        try {
            LogAccessQueueService.getQueue().put(params);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // LogAccessDTO logAccess1 = new LogAccessDTO();
        // LogAccessDTO logAccess2 = new LogAccessDTO();
        // logAccess1.setAti("");
        // logAccess1.setOperation("定时订单同步");
        // logAccess1.setServerIp("127.0.0.1");
        // logAccess1.setTime(String.valueOf(System.currentTimeMillis()));
        // logAccess1.setTradeIds("1L,2L");
        // logAccess1.setUrl("定时订单同步");
        // logAccess1.setUserId("");
        // logAccess1.setUserIp("127.0.0.1");
        // logAccess2.setAti("");
        // logAccess2.setOperation("定时订单同步2");
        // logAccess2.setServerIp("127.0.0.2");
        // logAccess2.setTime(String.valueOf(System.currentTimeMillis()));
        // logAccess2.setTradeIds("3L,4L");
        // logAccess2.setUrl("定时订单同步2");
        // logAccess2.setUserId("");
        // logAccess2.setUserIp("127.0.0.2");
        // JSONArray data = new JSONArray();
        // data.add(logAccess1);
        // data.add(logAccess2);
        // Map<String, Object> params = new HashMap<String, Object>();
        // LogAccessDTO logAccessDTO = new LogAccessDTO();
        // logAccessDTO.setMethod("order");
        // logAccessDTO.setData(data.toJSONString());
        // params.put(LogType.class.getName(), LogType.BATCH_LOG_TYPE);
        // params.put(LogAccessDTO.class.getName(), logAccessDTO);
        // try {
        // LogAccessQueueService.LOG_QUEUE.put(params);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }
    }

    @SuppressWarnings("unused")
    private void testSaveOrder() throws Exception {
        OrderDTO orderOne = new OrderDTO();
        OrderDTO orderTwo = new OrderDTO();
        BigDecimal bigDecimalOne = new BigDecimal("1.1");
        BigDecimal bigDecimalTwo = new BigDecimal("2.1");
        orderOne.setItemMealName("套餐的值 -1");
        orderOne.setPicPath("picPath");
        orderOne.setSellerNick("卖家昵称   -1");
        orderOne.setBuyerNick("买家昵称 -1");
        orderOne.setRefundStatus("退款状态 -1");
        orderOne.setOuterIid("商家外部编码(可与商家外部系统对接) -1");
        orderOne.setSnapshotUrl("订单快照UR -1");
        orderOne.setSnapshot("订单快照详细信息 -1");
        orderOne.setTimeoutActionTime(new Date());
        orderOne.setBuyerRate(false);
        orderOne.setSellerRate(false);
        orderOne.setSellerType("卖家类型 -1");
        orderOne.setCid(1L);
        orderOne.setSubOrderTaxFee("天猫国际官网直供子订单关税税费   -1");
        orderOne.setSubOrderTaxRate("天猫国际官网直供子订单关税税率 -1");
        orderOne.setEstimateConTime("子订单预计发货时间 -1");
        orderOne.setOid(1L);
        orderOne.setStatus("订单状态 -1");
        orderOne.setTitle("商品标题 -1");
        orderOne.setType("交易类型  -1");
        orderOne.setIid("商品的字符串编号 -1");
        orderOne.setPrice(bigDecimalOne);
        orderOne.setNumIid(1L);
        orderOne.setItemMealId(1L);
        orderOne.setSkuId("商品的最小库存单位 -1");
        orderOne.setNum(1L);
        orderOne.setOuterSkuId("外部网店自己定义的Sku编号  -1");
        orderOne.setOrderFrom("子订单来源");
        orderOne.setTotalFee(bigDecimalOne);
        orderOne.setPayment(bigDecimalOne);
        orderOne.setDiscountFee(bigDecimalOne);
        orderOne.setAdjustFee(bigDecimalOne);
        orderOne.setModified(new Date());
        orderOne.setSkuPropertiesName("手机套餐:官方标配 -1");
        orderOne.setRefundId(1L);
        orderOne.setIsOversold(false);
        orderOne.setIsServiceOrder(false);
        orderOne.setEndTime(new Date());
        orderOne.setConsignTime("子订单发货时间 -1");
        orderOne.setOrderAttr("top动态字段 -1");
        orderOne.setShippingType("子订单的运送方式 -1");
        orderOne.setBindOid(1L);
        orderOne.setLogisticsCompany("子订单发货的快递公司名称 -1");
        orderOne.setInvoiceNo("子订单所在包裹的运单号 -1");
        orderOne.setIsDaixiao(false);
        orderOne.setDivideOrderFee(bigDecimalOne);
        orderOne.setPartMjzDiscount("优惠分摊 -1");
        orderOne.setTicketOuterId("对应门票有效期的外部id -1");
        orderOne.setTicketExpDateKey("门票有效期的key -1");
        orderOne.setStoreCode("发货的仓库编码 -1");
        orderOne.setIsWww(false);
        orderOne.setTmserSpuCode("支持家装类物流的类型    -1");
        orderOne.setBindOids("bind_oid字段的升级 -1");
        orderOne.setZhengjiStatus("征集状态 -1");
        orderOne.setMdQualification("免单资格属性 -1");
        orderOne.setMdFee(bigDecimalOne);
        orderOne.setCustomization("定制信息     -1");
        orderOne.setInvType("库存类型 -1");
        orderOne.setXxx("xxx");
        orderOne.setIsShShip(false);
        orderOne.setShipper("仓储信息 -1");
        orderOne.setfType("订单履行类型， -1");
        orderOne.setfStatus("订单履行状态 -1");
        orderOne.setfTerm("单履行内容 -1");
        orderOne.setComboId("天猫搭配宝 -1");
        orderOne.setAssemblyRela("主商品订单id   -1");
        orderOne.setAssemblyPrice("价格 -1");
        orderOne.setAssemblyItem("assemblyItem -1 ");
        orderOne.setSubOrderTaxPromotionFee("天猫国际子订单计税优惠金额  -1 ");
        orderOne.setFqgNum(1L);
        orderOne.setIsFqgSFee(false);
        orderOne.setTradePayment(bigDecimalOne);
        orderOne.setTradeSellerRate(false);
        orderOne.setReceiverName("收货人的姓名 -1");
        orderOne.setReceiverState("收货人的所在省份 -1");
        orderOne.setReceiverAddress("收货人的详细地址 -1");
        orderOne.setReceiverZip("收货人的邮编 -1");
        orderOne.setReceiverMobile("收货人的手机号码 -1");
        orderOne.setReceiverPhone("收货人的电话号码 -1");
        orderOne.setTradeConsignTime(new Date());
        orderOne.setReceivedPayment(bigDecimalOne);
        orderOne.setReceiverCountry("收货人国籍 -1");
        orderOne.setReceiverTown("收货人街道地址  -1");
        orderOne.setShopPick("门店自提 -1");
        orderOne.setTid(1L);
        orderOne.setTradeNum(1L);
        orderOne.setTradeStatus("交易状态 -1");
        orderOne.setTradeTitle("交易标题 -1");
        orderOne.setTradeType("交易类型列表 -1");
        orderOne.setTradeTotalFee(bigDecimalOne);
        orderOne.setTradeCreated(new Date());
        orderOne.setTradePayTime(new Date());
        orderOne.setTradeModified(new Date());
        orderOne.setTradeEndTime(new Date());
        orderOne.setBuyerFlag(1L);
        orderOne.setSellerFlag(1L);
        orderOne.setStepTradeStatus("分阶段付款的订单状态 -1");
        orderOne.setStepPaidFee(bigDecimalOne);
        orderOne.setTradeShippingType("创建交易时的物流方式 -1");
        orderOne.setBuyerCodFee(bigDecimalOne);
        orderOne.setTradeAdjustFee(bigDecimalOne);
        orderOne.setTradeFrom("交易内部来源 -1");
        orderOne.setTradeBuyerRate(false);
        orderOne.setReceiverCity("收货人的所在城市 -1");
        orderOne.setReceiverDistrict("收货人的所在地区 -1");
        orderOne.setTradeSource(1);
        orderOne.setCreatedDate(new Date());

        orderTwo.setItemMealName("套餐的值 -2");
        orderTwo.setPicPath("picPath");
        orderTwo.setSellerNick("卖家昵称   -2");
        orderTwo.setBuyerNick("买家昵称 -2");
        orderTwo.setRefundStatus("退款状态 -2");
        orderTwo.setOuterIid("商家外部编码(可与商家外部系统对接) -2");
        orderTwo.setSnapshotUrl("订单快照UR -2");
        orderTwo.setSnapshot("订单快照详细信息 -2");
        orderTwo.setTimeoutActionTime(new Date());
        orderTwo.setBuyerRate(true);
        orderTwo.setSellerRate(true);
        orderTwo.setSellerType("卖家类型 -2");
        orderTwo.setCid(2L);
        orderTwo.setSubOrderTaxFee("天猫国际官网直供子订单关税税费   -2");
        orderTwo.setSubOrderTaxRate("天猫国际官网直供子订单关税税率 -2");
        orderTwo.setEstimateConTime("子订单预计发货时间 -2");
        orderTwo.setOid(2L);
        orderTwo.setStatus("订单状态 -2");
        orderTwo.setTitle("商品标题 -2");
        orderTwo.setType("交易类型  -2");
        orderTwo.setIid("商品的字符串编号 -2");
        orderTwo.setPrice(bigDecimalTwo);
        orderTwo.setNumIid(2L);
        orderTwo.setItemMealId(2L);
        orderTwo.setSkuId("商品的最小库存单位 -2");
        orderTwo.setNum(2L);
        orderTwo.setOuterSkuId("外部网店自己定义的Sku编号  -2");
        orderTwo.setOrderFrom("子订单来源");
        orderTwo.setTotalFee(bigDecimalTwo);
        orderTwo.setPayment(bigDecimalTwo);
        orderTwo.setDiscountFee(bigDecimalTwo);
        orderTwo.setAdjustFee(bigDecimalTwo);
        orderTwo.setModified(new Date());
        orderTwo.setSkuPropertiesName("手机套餐:官方标配 -2");
        orderTwo.setRefundId(2L);
        orderTwo.setIsOversold(true);
        orderTwo.setIsServiceOrder(true);
        orderTwo.setEndTime(new Date());
        orderTwo.setConsignTime("子订单发货时间 -2");
        orderTwo.setOrderAttr("top动态字段 -2");
        orderTwo.setShippingType("子订单的运送方式 -2");
        orderTwo.setBindOid(2L);
        orderTwo.setLogisticsCompany("子订单发货的快递公司名称 -2");
        orderTwo.setInvoiceNo("子订单所在包裹的运单号 -2");
        orderTwo.setIsDaixiao(true);
        orderTwo.setDivideOrderFee(bigDecimalTwo);
        orderTwo.setPartMjzDiscount("优惠分摊 -2");
        orderTwo.setTicketOuterId("对应门票有效期的外部id -2");
        orderTwo.setTicketExpDateKey("门票有效期的key -2");
        orderTwo.setStoreCode("发货的仓库编码 -2");
        orderTwo.setIsWww(true);
        orderTwo.setTmserSpuCode("支持家装类物流的类型    -2");
        orderTwo.setBindOids("bind_oid字段的升级 -2");
        orderTwo.setZhengjiStatus("征集状态 -2");
        orderTwo.setMdQualification("免单资格属性 -2");
        orderTwo.setMdFee(bigDecimalTwo);
        orderTwo.setCustomization("定制信息     -2");
        orderTwo.setInvType("库存类型 -2");
        orderTwo.setXxx("xxx");
        orderTwo.setIsShShip(true);
        orderTwo.setShipper("仓储信息 -2");
        orderTwo.setfType("订单履行类型， -2");
        orderTwo.setfStatus("订单履行状态 -2");
        orderTwo.setfTerm("单履行内容 -2");
        orderTwo.setComboId("天猫搭配宝 -2");
        orderTwo.setAssemblyRela("主商品订单id   -2");
        orderTwo.setAssemblyPrice("价格 -2");
        orderTwo.setAssemblyItem("assemblyItem -2 ");
        orderTwo.setSubOrderTaxPromotionFee("天猫国际子订单计税优惠金额  -2 ");
        orderTwo.setFqgNum(2L);
        orderTwo.setIsFqgSFee(true);
        orderTwo.setTradePayment(bigDecimalTwo);
        orderTwo.setTradeSellerRate(true);
        orderTwo.setReceiverName("收货人的姓名 -2");
        orderTwo.setReceiverState("收货人的所在省份 -2");
        orderTwo.setReceiverAddress("收货人的详细地址 -2");
        orderTwo.setReceiverZip("收货人的邮编 -2");
        orderTwo.setReceiverMobile("收货人的手机号码 -2");
        orderTwo.setReceiverPhone("收货人的电话号码 -2");
        orderTwo.setTradeConsignTime(new Date());
        orderTwo.setReceivedPayment(bigDecimalTwo);
        orderTwo.setReceiverCountry("收货人国籍 -2");
        orderTwo.setReceiverTown("收货人街道地址  -2");
        orderTwo.setShopPick("门店自提 -2");
        orderTwo.setTid(2L);
        orderTwo.setTradeNum(2L);
        orderTwo.setTradeStatus("交易状态 -2");
        orderTwo.setTradeTitle("交易标题 -2");
        orderTwo.setTradeType("交易类型列表 -2");
        orderTwo.setTradeTotalFee(bigDecimalTwo);
        orderTwo.setTradeCreated(new Date());
        orderTwo.setTradePayTime(new Date());
        orderTwo.setTradeModified(new Date());
        orderTwo.setTradeEndTime(new Date());
        orderTwo.setBuyerFlag(2L);
        orderTwo.setSellerFlag(2L);
        orderTwo.setStepTradeStatus("分阶段付款的订单状态 -2");
        orderTwo.setStepPaidFee(bigDecimalTwo);
        orderTwo.setTradeShippingType("创建交易时的物流方式 -2");
        orderTwo.setBuyerCodFee(bigDecimalTwo);
        orderTwo.setTradeAdjustFee(bigDecimalTwo);
        orderTwo.setTradeFrom("交易内部来源 -2");
        orderTwo.setTradeBuyerRate(true);
        orderTwo.setReceiverCity("收货人的所在城市 -2");
        orderTwo.setReceiverDistrict("收货人的所在地区 -2");
        orderTwo.setTradeSource(2);
        orderTwo.setCreatedDate(new Date());
        List<OrderDTO> orderList = new ArrayList<OrderDTO>(2);
        orderList.add(orderTwo);
        orderList.add(orderOne);

        // this.orderDTOService.saveOrderDTO(4L, orderOne);
        // this.orderDTOService.saveOrderDTOByList(4L, orderList);
        //
        // this.orderDTOService.doUpdateOrderDTO(4L, orderTwo);
        // this.orderDTOService.doUpdateOrderDTOByList(4L, orderList);
        this.orderManageService.batchInsertOrderList(5L, orderList);
    }

    @SuppressWarnings("unused")
    private void testSaveTradeDTO() {
        List<TradeDTO> tradeList = new ArrayList<TradeDTO>(2);
        BigDecimal bigDecimalOne = new BigDecimal("1.1");
        BigDecimal bigDecimalTwo = new BigDecimal("2.1");
        TradeDTO tradeTwo = new TradeDTO();
        TradeDTO tradeOne = new TradeDTO();
        tradeOne.setAdjustFee(bigDecimalOne);
        tradeOne.setAlipayId(1L);
        tradeOne.setAlipayNo("支付宝交易号 -1");
        tradeOne.setAlipayPoint(1L);
        tradeOne.setAvailableConfirmFee(bigDecimalOne);
        tradeOne.setBuyerAlipayNo("买家支付宝账号 -1");
        tradeOne.setBuyerArea("买家下单的地区 -1");
        tradeOne.setBuyerCodFee("买家货到付款服务费 -1");
        tradeOne.setBuyerEmail("买家邮件地址-1");
        tradeOne.setBuyerNick("买家昵称 -1");
        tradeOne.setBuyerObtainPointFee(1L);
        tradeOne.setBuyerRate(false);
        tradeOne.setCodFee(bigDecimalOne);
        tradeOne.setCodStatus("货到付款物流状态-1");
        tradeOne.setCommissionFee(bigDecimalOne);
        tradeOne.setConsignTime(new Date());
        tradeOne.setCreated(new Date());
        tradeOne.setDiscountFee(bigDecimalOne);
        tradeOne.setEndTime(new Date());
        tradeOne.setHasPostFee(false);
        tradeOne.setIs3D(false);
        tradeOne.setIsBrandSale(false);
        tradeOne.setIsDaixiao(false);
        tradeOne.setIsForceWlb(false);
        tradeOne.setIsLgtype(false);
        tradeOne.setIsPartConsign(false);
        tradeOne.setIsWt(false);
        tradeOne.setModified(new Date());
        tradeOne.setPayTime(new Date());
        tradeOne.setPayment(bigDecimalOne);
        tradeOne.setPccAf(1L);
        tradeOne.setPointFee(1L);
        tradeOne.setPostFee(bigDecimalOne);
        tradeOne.setRealPointFee(1L);
        tradeOne.setReceivedPayment(bigDecimalOne);
        tradeOne.setReceiverAddress("收货人的详细地址 -1");
        tradeOne.setReceiverCity("收货人的所在城市 -1");
        tradeOne.setReceiverDistrict("收货人的所在地区 -1");
        tradeOne.setReceiverMobile("收货人的手机号码 -1");
        tradeOne.setReceiverName("收货人的姓名 -1");
        tradeOne.setReceiverState("收货人的所在省份  -1");
        tradeOne.setReceiverZip("收货人的邮编  -1");
        tradeOne.setSellerAlipayNo("卖家支付宝账号 -1");
        tradeOne.setSellerCanRate(false);
        tradeOne.setSellerCodFee(bigDecimalOne);
        tradeOne.setSellerEmail("卖家邮件地址 -1");
        tradeOne.setSellerFlag(1L);
        tradeOne.setSellerMobile("卖家手机 -1");
        tradeOne.setSellerName("卖家姓名 -1");
        tradeOne.setSellerNick("卖家昵称 -1");
        tradeOne.setSellerPhone("卖家电话 -1");
        tradeOne.setSellerRate(false);
        tradeOne.setShippingType("创建交易时的物流方式 -1");
        tradeOne.setSnapshotUrl("交易快照地址 -1");
        tradeOne.setStatus("交易状态 -1");
        tradeOne.setTid(1L);
        tradeOne.setTitle("交易标题 -1");
        tradeOne.setTotalFee(bigDecimalOne);
        tradeOne.setTradeFrom("交易内部来源 -1");
        tradeOne.setType("交易类型列表 -1");
        tradeOne.setServiceTags("物流标签 -1");
        tradeOne.setPromotionDetails("优惠详情 -1");
        tradeOne.setStepPaidFee("分阶段付款的已付金额  -1");
        tradeOne.setStepTradeStatus("分阶段付款的订单状态 -1");
        tradeOne.setMsgId(1L);
        tradeOne.setLastSendSmsTime(new Date());
        tradeOne.setTradeSource(1);
        tradeOne.setReceiverPhone("收货人固定电话 -1");

        tradeTwo.setAdjustFee(bigDecimalTwo);
        tradeTwo.setAlipayId(2L);
        tradeTwo.setAlipayNo("支付宝交易号 -2");
        tradeTwo.setAlipayPoint(2L);
        tradeTwo.setAvailableConfirmFee(bigDecimalTwo);
        tradeTwo.setBuyerAlipayNo("买家支付宝账号 -2");
        tradeTwo.setBuyerArea("买家下单的地区 -2");
        tradeTwo.setBuyerCodFee("买家货到付款服务费 -2");
        tradeTwo.setBuyerEmail("买家邮件地址-2");
        tradeTwo.setBuyerNick("买家昵称 -2");
        tradeTwo.setBuyerObtainPointFee(2L);
        tradeTwo.setBuyerRate(true);
        tradeTwo.setCodFee(bigDecimalTwo);
        tradeTwo.setCodStatus("货到付款物流状态-2");
        tradeTwo.setCommissionFee(bigDecimalTwo);
        tradeTwo.setConsignTime(new Date());
        tradeTwo.setCreated(new Date());
        tradeTwo.setDiscountFee(bigDecimalTwo);
        tradeTwo.setEndTime(new Date());
        tradeTwo.setHasPostFee(true);
        tradeTwo.setIs3D(true);
        tradeTwo.setIsBrandSale(true);
        tradeTwo.setIsDaixiao(true);
        tradeTwo.setIsForceWlb(true);
        tradeTwo.setIsLgtype(true);
        tradeTwo.setIsPartConsign(true);
        tradeTwo.setIsWt(true);
        tradeTwo.setModified(new Date());
        tradeTwo.setPayTime(new Date());
        tradeTwo.setPayment(bigDecimalTwo);
        tradeTwo.setPccAf(2L);
        tradeTwo.setPointFee(2L);
        tradeTwo.setPostFee(bigDecimalTwo);
        tradeTwo.setRealPointFee(2L);
        tradeTwo.setReceivedPayment(bigDecimalTwo);
        tradeTwo.setReceiverAddress("收货人的详细地址 -2");
        tradeTwo.setReceiverCity("收货人的所在城市 -2");
        tradeTwo.setReceiverDistrict("收货人的所在地区 -2");
        tradeTwo.setReceiverMobile("收货人的手机号码 -2");
        tradeTwo.setReceiverName("收货人的姓名 -2");
        tradeTwo.setReceiverState("收货人的所在省份  -2");
        tradeTwo.setReceiverZip("收货人的邮编  -2");
        tradeTwo.setSellerAlipayNo("卖家支付宝账号 -2");
        tradeTwo.setSellerCanRate(true);
        tradeTwo.setSellerCodFee(bigDecimalTwo);
        tradeTwo.setSellerEmail("卖家邮件地址 -2");
        tradeTwo.setSellerFlag(2L);
        tradeTwo.setSellerMobile("卖家手机 -2");
        tradeTwo.setSellerName("卖家姓名 -2");
        tradeTwo.setSellerNick("卖家昵称 -2");
        tradeTwo.setSellerPhone("卖家电话 -2");
        tradeTwo.setSellerRate(true);
        tradeTwo.setShippingType("创建交易时的物流方式 -2");
        tradeTwo.setSnapshotUrl("交易快照地址 -2");
        tradeTwo.setStatus("交易状态 -2");
        tradeTwo.setTid(2L);
        tradeTwo.setTitle("交易标题 -2");
        tradeTwo.setTotalFee(bigDecimalTwo);
        tradeTwo.setTradeFrom("交易内部来源 -2");
        tradeTwo.setType("交易类型列表 -2");
        tradeTwo.setServiceTags("物流标签 -2");
        tradeTwo.setPromotionDetails("优惠详情 -2");
        tradeTwo.setStepPaidFee("分阶段付款的已付金额  -2");
        tradeTwo.setStepTradeStatus("分阶段付款的订单状态 -2");
        tradeTwo.setMsgId(2L);
        tradeTwo.setLastSendSmsTime(new Date());
        tradeTwo.setTradeSource(1);
        tradeTwo.setReceiverPhone("收货人固定电话 -2");

        tradeList.add(tradeTwo);
        tradeList.add(tradeOne);
        this.tradeDTOService.batchInsertTradeList(5L, tradeList);
        // this.tradeDTOService.saveTradeDTO(4L, tradeOne);
    }
}
