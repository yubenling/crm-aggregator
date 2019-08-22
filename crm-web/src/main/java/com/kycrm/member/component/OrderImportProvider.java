package com.kycrm.member.component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.item.ItemImport;
import com.kycrm.member.domain.entity.orderimport.ImportJdpResponse;
import com.kycrm.member.domain.entity.orderimport.ImportJdpResponse.order;
import com.kycrm.member.domain.entity.orderimport.ImportJdpResponse.orders;
import com.kycrm.member.domain.entity.orderimport.ImportJdpResponse.trade;
import com.kycrm.member.domain.entity.orderimport.ImportJdpResponse.trade_fullinfo_get_response;
import com.kycrm.member.domain.entity.trade.TbTrade;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.item.IItemService;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.orderimport.IOrderImportRecordService;
import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.util.DateUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.ItemImportUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.OrderImportUtil;
import com.kycrm.util.thread.MyFixedThreadPool;

@Component("orderImportProvider")
public class OrderImportProvider {	
	private static final Log logger = LogFactory.getLog(OrderImportProvider.class);
	
	@Autowired
	private IOrderImportRecordService orderImportRecordService;
	@Autowired
	private IItemService itemService;
	@Autowired
	private IOrderDTOService orderDTOService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IMongoHistroyTradeService synServiceProvider;

	/**
	 * 订单上传数据处理，服务入口
	 */
	public void disposeOrderImportData(List<String[]> datasList,
			Map<String, Integer> headerMap,UserInfo user, Long rId) {
		
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
			@Override
			public void run() {
				
				try {
					//将导入的订单做商品拆分，返回商品编号
					Map<String,List<String>> itemTitleMap = extractItemData(headerMap, datasList,user);
					
					//重写处理链条
					DataHandler	dataHandler = new DataHandler(){
						@Override
						public void doHandler(List<String[]> datas) {
							orderImportDataDispose(datas, headerMap, itemTitleMap, user, rId);
						}
					};
					
					//分配队列数据
					disposeOrderData data = new disposeOrderData(datasList, user, dataHandler);
					data.start();
					data.join();
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****订单上传数据处理异常******************"+e.getMessage());
				}
			   	
			   	//所有线程执行完毕更新导入记录状态
			   	orderImportRecordService.updateOrderImportRecordState(rId);
			}
		});
	}
	
	// ****************************以下为队列分发及数据封装处理********************************
	/**
	 * 数据封装
	 * @author HL
	 * @time 2018年8月25日 下午1:28:14 
	 */
	public void orderImportDataDispose(List<String[]> datas,
			Map<String, Integer> headerMap,
			Map<String, List<String>> itemTitleMap, UserInfo user, Long rId) {
		
		Set<Long> itemIds = new HashSet<Long>();//删除商品list
		Map<Long,String> oidOrTitleMap = new HashMap<Long,String>();//封装oid和Title
		Map<Long,TbTrade> tbTradeMap = new HashMap<Long,TbTrade>();//tbTradeMap
		
		for (String[] data : datas) {
			try {
				TbTrade tbTrade = new TbTrade();
				ImportJdpResponse response = new ImportJdpResponse();
				trade trade = response.newTrade();
				order order = response.newOrder();

				String tid = OrderImportUtil.getValFromArray(headerMap, data,"订单编号");
				String newTid = OrderImportUtil.disposeTid(tid);
				if(!"".equals(newTid)){
					long parseLong = Long.parseLong(newTid);
					trade.setTid(newTid);
					order.setOid(newTid);
					tbTrade.setTid(parseLong);
					String title = OrderImportUtil.getValFromArray(headerMap, data, "宝贝标题");
					oidOrTitleMap.put(parseLong, title);
				}else{
					logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****订单号错误******************"+tid);
					continue;
				}
				
				String buyerNick = OrderImportUtil.getValFromArray(headerMap, data, "买家会员名");
				if (buyerNick != null && !buyerNick.equals("")) {
					//需要加密
					String encryptContent = encryptContent(buyerNick, user);
					tbTrade.setBuyerNick(encryptContent);
					trade.setBuyer_nick(encryptContent);
				}
				
				String buyerAlipayNo = OrderImportUtil.getValFromArray(headerMap, data, "买家支付宝账号");
				if (buyerAlipayNo != null && !buyerAlipayNo.equals("")) {
					String encryptContent = encryptContent(buyerAlipayNo, user);
					trade.setBuyer_alipay_no(encryptContent);
				}
				
				String price = OrderImportUtil.getValFromArray(headerMap, data, "买家应付货款");
				if (price != null && !price.equals("")) {
					new BigDecimal(price);
					order.setPrice(price);
				}
				
				String postFee = OrderImportUtil.getValFromArray(headerMap, data, "买家应付邮费");
				if (postFee != null && !postFee.equals("")) {
					new BigDecimal(postFee);
					trade.setPost_fee(postFee);

				}
				
				String pointFee = OrderImportUtil.getValFromArray(headerMap, data, "买家支付积分");
				if (pointFee != null && !pointFee.equals("")) {
					Long.parseLong(pointFee);
					trade.setPoint_fee(pointFee);
				}
				String totalFee = OrderImportUtil.getValFromArray(headerMap, data, "总金额");
				if (totalFee != null && !totalFee.equals("")) {
					new BigDecimal(totalFee);
					trade.setTotal_fee(totalFee);
					order.setTotal_fee(totalFee);
				}
				
				String buyerObtainPointFee = OrderImportUtil.getValFromArray(headerMap, data, "返点积分");
				if (buyerObtainPointFee != null && !buyerObtainPointFee.equals("")) {
					Long.parseLong(buyerObtainPointFee);
					trade.setBuyer_obtain_point_fee(buyerObtainPointFee);
				}
				
				String payment = OrderImportUtil.getValFromArray(headerMap, data, "买家实际支付金额");
				if (payment != null && !payment.equals("")) {
					new BigDecimal(payment);
					trade.setPayment(payment);
					order.setPayment(payment);
				}
				
				String realPointFee = OrderImportUtil.getValFromArray(headerMap, data, "买家实际支付积分");
				if (realPointFee != null && !realPointFee.equals("")) {
					Long.parseLong(realPointFee);
					trade.setReal_point_fee(realPointFee);
				}
				
				String status = OrderImportUtil.getValFromArray(headerMap, data, "订单状态");
				String cause = OrderImportUtil.getValFromArray(headerMap, data, "订单关闭原因");
				if (status != null && !status.equals("")) {
					String getdeal = OrderImportUtil.getdeal(status,cause);
					tbTrade.setStatus(getdeal);
					trade.setStatus(getdeal);
					order.setStatus(getdeal);
					
				}
				
				String receiverName = OrderImportUtil.getValFromArray(headerMap, data, "收货人姓名");
				if (receiverName != null && !receiverName.equals("")) {
					String encryptContent = encryptContent(receiverName, user);
					trade.setReceiver_name(encryptContent);
				}
				
				String receiverAddress = OrderImportUtil.getValFromArray(headerMap, data, "收货地址");
				if (receiverAddress != null && !receiverAddress.equals("")) {
					String city = OrderImportUtil.getCity(receiverAddress);
					String provinces = OrderImportUtil.getProvinces(receiverAddress);
					String encryptContent = encryptContent(receiverAddress, user);
					trade.setReceiver_address(encryptContent);
					trade.setReceiver_city(city);
					trade.setReceiver_state(provinces);
				}
				
				String receiverPhone = OrderImportUtil.getValFromArray(headerMap, data, "联系电话");
				if (receiverPhone != null && !receiverPhone.equals("")) {
					String phone = OrderImportUtil.disposePhone(receiverPhone);
					trade.setReceiver_phone(phone);
				}
				
				String receiverMobile = OrderImportUtil.getValFromArray(headerMap, data, "联系手机");
				if (receiverMobile != null && !receiverMobile.equals("")) {
					String mobile = OrderImportUtil.disposePhone(receiverMobile);
					String encryptMobile = encryptMobile(mobile, user);
					trade.setReceiver_mobile(encryptMobile);
				}
				
				String created = OrderImportUtil.getValFromArray(headerMap, data, "订单创建时间");
				if (created != null && !created.equals("")) {
					try {
						DateUtils.stringToDate(created, OrderImportUtil.DATE_FORMAT_ONE);
						trade.setCreated(created);
					} catch (Exception e) {
						try {
							Date twoDate = DateUtils.stringToDate(created, OrderImportUtil.DATE_FORMAT_TWO);
							String strDate = DateUtils.dateToString(twoDate,DateUtils.DEFAULT_TIME_FORMAT);
							trade.setCreated(strDate);
						} catch (Exception e1) {
							logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****订单创建时间格式错误："+created+"，正确格式：yyyy-MM-dd HH:mm:ss");
							continue;
						}
					}
				}
				
				
				String payTime = OrderImportUtil.getValFromArray(headerMap, data, "订单付款时间");
				if (payTime != null && !payTime.equals("")) {
					try {
						DateUtils.stringToDate(payTime, OrderImportUtil.DATE_FORMAT_ONE);
						trade.setPay_time(payTime);
					} catch (Exception e) {
						try {
							Date twoDate = DateUtils.stringToDate(payTime, OrderImportUtil.DATE_FORMAT_TWO);
							String strDate = DateUtils.dateToString(twoDate,DateUtils.DEFAULT_TIME_FORMAT);
							trade.setPay_time(strDate);
						} catch (Exception e1) {
							logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****订单付款时间格式错误："+payTime+"，正确格式：yyyy-MM-dd HH:mm:ss");
							continue;
						}
					}
				}
				
				String title = OrderImportUtil.getValFromArray(headerMap, data, "宝贝标题");
				if (title != null && !title.equals("")) {
					order.setTitle(title);
					if (null != itemTitleMap && itemTitleMap.size() > 0
							&& itemTitleMap.containsKey(title.trim())) {
							order.setNum_iid(itemTitleMap.get(title.trim()).get(0));
						}
				}
				
				String num = OrderImportUtil.getValFromArray(headerMap, data, "宝贝总数量");
				if (num != null && !num.equals("")) {
					trade.setNum(num);
					order.setNum(num);
				}
				
				String sellerNick = OrderImportUtil.getValFromArray(headerMap, data, "店铺名称");
				if (sellerNick != null && !sellerNick.equals("")) {
					trade.setTitle(sellerNick);
				}
				
				String endTime = OrderImportUtil.getValFromArray(headerMap, data, "确认收货时间");
				if(endTime != null && !endTime.equals("")){
					try {
						DateUtils.stringToDate(endTime, OrderImportUtil.DATE_FORMAT_ONE);
						trade.setEnd_time(endTime);
					} catch (Exception e) {
						try {
							Date twoDate = DateUtils.stringToDate(endTime, OrderImportUtil.DATE_FORMAT_TWO);
							String strDate = DateUtils.dateToString(twoDate,DateUtils.DEFAULT_TIME_FORMAT);
							trade.setEnd_time(strDate);
						} catch (Exception e1) {
							logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****确认收货时间格式错误："+payTime+"，正确格式：yyyy-MM-dd HH:mm:ss");
							continue;
						}
					}
				}
				
				String receivedPayment = OrderImportUtil.getValFromArray(headerMap, data, "打款商家金额");
				if(receivedPayment != null && !receivedPayment.equals("")){
					receivedPayment = receivedPayment.replace("元", "");
					new BigDecimal(receivedPayment);
					trade.setReceived_payment(receivedPayment);
				}
				
				// 补充参数
				trade.setSeller_nick(user.getTaobaoUserNick());
				tbTrade.setSellerNick(user.getTaobaoUserNick());
				// 给type字段复制import--标识是导入的订单数据
				trade.setTrade_from("import");
				order.setOrder_from("import");
				order.setRefund_status("NO_REFUND");

				if (trade != null && order != null) {
					List<order> orderList = new ArrayList<order>();
					orderList.add(order);
					orders orders = response.newOrders();
					orders.setOrder(orderList);
					trade.setOrders(orders);
					trade_fullinfo_get_response get_response = response.newTrade_fullinfo_get_response();
					get_response.setTrade(trade);
					response.setTrade_fullinfo_get_response(get_response);
					String jdpResponse = JsonUtil.toJson(response);
					tbTrade.setJdpResponse(jdpResponse);
					
					//添加到Map
					tbTradeMap.put(tbTrade.getTid(), tbTrade);
				}
			} catch (Exception e) {
				logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****循环封装数据异常"+e.getMessage());
				continue;
			}
		}
		
		//通过订单id查询订单是否存在---存在，获取导入拆分的商品id
		try {
			Set<Long> keySet = oidOrTitleMap.keySet();
			if(null != keySet && keySet.size()>0){
				List<Long> oids = new ArrayList<Long>(keySet);
				List<Long> findIds = orderDTOService.findOrderDTOByIds(user.getId(), oids);
				for (Long oid : findIds) {
					//判断订单是否存在
					if(tbTradeMap.containsKey(oid)){
						tbTradeMap.remove(oid);
						logger.info("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****重复订单丢弃: "+oid);
					}
					
					String title = oidOrTitleMap.get(oid);
					if(title == null || "".equals(title))
						continue;
					
					List<String> list = itemTitleMap.get(title.trim());
					if (null != list && list.size() > 1 && null != list.get(1)
							&& "import".equals(list.get(1))) {
						itemIds.add(Long.parseLong(itemTitleMap.get(title.trim()).get(0)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****订单中获取商品id异常:"+e.getMessage());
		}
		
		//将加密数据调用订单处理逻辑-----------本领的方法------------------------
		try {
			Collection<TbTrade> values = tbTradeMap.values();
			if(null != values && values.size()>0){
				List<TbTrade> list = new ArrayList<TbTrade>(values);
				synServiceProvider.synOrderHistory(list);
				logger.info("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****单次批量调用同步服务订单数:"+values.size());
			}else{
				logger.info("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****订单集合为空不调用同步服务！！！");
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****订单批量处理异常:"+e.getMessage());
		}
		
		//删除拆分的商品
		try {
			if (itemIds != null && itemIds.size() > 0) {
				List<Long> list = new ArrayList<Long>(itemIds);
				itemService.batchDeleteImportItems(user.getId(),list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****删除已存在商品异常:"+e.getMessage());
		}
	}

	
	  //回调处理链条
    interface DataHandler{
        void doHandler(List<String[]> datas);
    }
	/**
	 * 订单数据进行多线程分发
	 * @author HL
	 * @time 2018年8月25日 下午1:28:57
	 */
	class disposeOrderData {
	   /**
         * 用于干掉处理数据的线程。
         */
        public final List<String[]> POISON = new ArrayList<String[]>();
		/**
		 * 数据读取线程
		 */
		private Thread readerThread;
		/**
		 * 数据处理线程
		 */
		private Thread[] proccessors;
		/**
		 * 队列
		 */
		private BlockingQueue<List<String[]>> queue = new ArrayBlockingQueue<List<String[]>>(1000);

		public disposeOrderData(List<String[]> datasList, UserInfo user, DataHandler dataHandler) {
			// 数据封装队列
			readerThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						if (null != datasList && datasList.size() > 0) {
							long i = 0;//处理条数
							int dataSize = datasList.size();// 总条数
							int start = 0, // 开始次数
							end = 0, // 结束次数
							node = 300;// 每次处理多少条
							if (dataSize % node == 0) {
								end = dataSize / node;
							} else {
								end = (dataSize + node) / node;
							}

							while (start < end) {
								List<String[]> subList = new ArrayList<String[]>();
								if (start == (end - 1)) {
									subList = datasList.subList(start * node,dataSize);
								} else {
									subList = datasList.subList(start * node,(start + 1) * node);
								}
								start++;
								queue.put(subList);
								i+=subList.size();
							}
							logger.info("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****放入队列数据总数："+i+"条*****");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						try {
							queue.put(POISON);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}, "prepare_thread");

			// 获取队列数据进行处理
			proccessors = new Thread[4];
			Runnable worker = new Runnable() {
				public void run() {
					for (;;) {
						try {
							List<String[]> take = queue.take();
							if (null != take && take.size() > 0) {
								logger.info("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****单次取出队列数："+take.size());
								// 处理数据
								dataHandler.doHandler(take);
							} else {
								queue.put(POISON);
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
							break;
						}
					}
				}
			};
			for (int i = 0; i < proccessors.length; i++) {
				proccessors[i] = new Thread(worker, "proccessor-thread_" + i);
			}

		}

		public synchronized void start() {
			readerThread.start();
			for (int i = 0; i < proccessors.length; i++) {
				proccessors[i].start();
			}
			
		}

		public void join() {
			try {
				readerThread.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < proccessors.length; i++) {
				try {
					proccessors[i].join();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	
	/** 加密手机号 */
	private String encryptMobile(String mobile,UserInfo user) {
		try {
			String encryptPhone = EncrptAndDecryptClient.getInstance().encryptData(mobile, EncrptAndDecryptClient.PHONE,
					user.getAccessToken());
			return encryptPhone;
		} catch (Exception e) {
			try {
				String accessToken = userInfoService.findUserTokenById(user.getId());
				user.setAccessToken(accessToken);
				String encryptPhone = EncrptAndDecryptClient.getInstance().encryptData(mobile, EncrptAndDecryptClient.PHONE,
						accessToken);
				return encryptPhone;
			} catch (Exception e1) {
				logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****加密出错 ***********PHONE*********" + e1.getMessage());
			}
		}
		return mobile;
	}

	/** 昵称加密 */
	private String encryptContent(String content, UserInfo user) {
		try {
			String encryptContent = EncrptAndDecryptClient.getInstance().encryptData(content, EncrptAndDecryptClient.SEARCH,
					user.getAccessToken());
			return encryptContent;
		} catch (Exception e) {
			try {
				String accessToken = userInfoService.findUserTokenById(user.getId());
				user.setAccessToken(accessToken);
				String encryptContent = EncrptAndDecryptClient.getInstance().encryptData(content, EncrptAndDecryptClient.SEARCH,
						accessToken);
				return encryptContent;
			} catch (Exception e1) {
				logger.error("*****订单上传，userNick："+user.getTaobaoUserNick()+"*****加密出错 ***********SEARCH*********" + e1.getMessage());
			}
		}
		return content;
	}
	
	/**
	 * 提取商品信息，保存并返回 
	 */
	private Map<String, List<String>> extractItemData(Map<String, Integer> headerMap,
			List<String[]> datasList, UserInfo user) {
		//封装所有导入的商品标题
		Map<String, List<String>> ImportTitles = ItemImportUtil.getImportTitles(headerMap, datasList);
		//查询出的商品标题
		Map<String, Long> itemList = new HashMap<String, Long>();
		
		//获取商品标题
		Set<String> titles = ImportTitles.keySet();
		if(null != titles && titles.size()>0 ){
			List<String> subTitles = new ArrayList<String>(titles);
			int dataSize = titles.size();//总条数
			int start = 0,//开始次数
				end =0,//结束次数
				node = 1000;//每次处理多少条
			if(dataSize%node==0){
				end=dataSize/node;
			}else{
				end=(dataSize+node)/node;
			}

			while (start<end) {
				List<String> subList = new ArrayList<String>();
				if(start==(end-1)){
					subList = subTitles.subList(start*node, dataSize);
				}else{
					subList = subTitles.subList(start*node, (start+1)*node);
				}
				Map<String, Object> pageMap = new HashMap<String,Object>();
				pageMap.put("list", subList);
				pageMap.put("uid", user.getId());
				List<ItemImport> list = itemService.findItemTitleAndItemid(user.getId(),pageMap);
				if(null != list){
					for (ItemImport item : list) {
						itemList.put(item.getTitle().trim(), item.getNumIid());
					}
				}
				start++;
			}
		}
		
		//将该用户的商品和当前导入的比对 --key需要与待导入的订单比对，value--需要将数据保存到新建商品表
		Map<Map<String, List<String>>, List<ItemImport>> map = ItemImportUtil.comparisonItemTitle(itemList,ImportTitles,user);
		//获取map中的key 数据进行订单比对
		Map<String,List<String>> itemTitleMap = ItemImportUtil.getMapKey(map);
		//获取map中的value数据，将订单拆分的商品数据保存
		List<ItemImport>  mapValue = ItemImportUtil.getMapValue(map);
		
		this.disposeItemImportList(user.getId(),mapValue);
		return itemTitleMap;
	}
	
	/**
	 * 分批保存导入拆分商品
	 * @param uid 
	 * @time 2018年9月12日 下午3:25:14 
	 * @param list
	 */
	private void disposeItemImportList(Long uid, List<ItemImport> list) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread(){
			@Override
			public void run() {
					if(null != list && list.size()>0 ){
						long i = 0;//处理条数
						int dataSize = list.size();//总条数
						int start = 0,//开始次数
							end =0,//结束次数
							node = 1000;//每次处理多少条
						if(dataSize%node==0){
							end=dataSize/node;
						}else{
							end=(dataSize+node)/node;
						}

						while (start<end) {
							List<ItemImport> subList = new ArrayList<ItemImport>();
							if(start==(end-1)){
								subList = list.subList(start*node, dataSize);
							}else{
								subList = list.subList(start*node, (start+1)*node);
							}
							start++;
							try {
								i+=itemService.insertItemImportList(uid,subList);
							} catch (Exception e) {
								logger.error("************订单上传*************分批保存导入拆分商品*****异常*************"+e.getMessage());
								continue;
							}
						}
						logger.info("************订单上传*************保存拆分商品："+i);
					}
			}
		});
	}
}
