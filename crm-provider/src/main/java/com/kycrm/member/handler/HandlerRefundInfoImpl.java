package com.kycrm.member.handler;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.queue.RefundQueue;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.refund.RefundDTO;
import com.kycrm.member.domain.entity.refund.TbRefund;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.syn.IRefundService;
import com.kycrm.member.service.syn.ITbRefundService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.util.thread.SynThreadPool;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;
import com.mysql.fabric.xmlrpc.base.Array;
import com.taobao.api.ApiException;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.Refund;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.response.RefundGetResponse;


@Service("handlerRefundInfo")
public class HandlerRefundInfoImpl implements IHandlerRefundInfo{
	
	
    private static final Logger logger=LoggerFactory.getLogger(HandlerRefundInfoImpl.class);
	
    @Autowired
    private IRefundService  refundService;
    @Autowired
    private ICacheService cacheService;
	@Autowired
	private ITbRefundService  tbRefundService;
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private IOrderDTOService orderDTOService;


	public void startSynRefundInfo(Date beginTime, Date endTime,String totalRefundNum) {
		if(beginTime==null||endTime==null){
			logger.info("退款同步开始时间"+beginTime+"结束时间为"+endTime);
			return;
		}
		Long refundCount=tbRefundService.findConuntByTime(beginTime,endTime);
		Long pageNum=0L;
		Long pageSize=Constants.PROCESS_PAGE_SIZE_MIDDLE;
		if(refundCount/Constants.PROCESS_PAGE_SIZE_MIDDLE==0){
			pageNum=1L;
		}else if(refundCount%Constants.PROCESS_PAGE_SIZE_MIDDLE==0){
			pageNum=refundCount%Constants.PROCESS_PAGE_SIZE_MIDDLE;
		}else{
			pageNum=(refundCount/Constants.PROCESS_PAGE_SIZE_MIDDLE)+1;
		}
		if(refundCount==0){
			pageNum=0L;
		}
		Long start=0L;
		Long oid=0L;
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		logger.info("本次查询总量为"+refundCount+"每页查询数量"+pageSize+"共"+pageNum+"页");
		while(start<pageNum){
		    try {
		    	//循环取出sysinfo的退款数据
		    	List<TbRefund> refundList=tbRefundService.findtbRefundList(beginTime,endTime,pageSize,oid);
		    	logger.info("开始时间"+df.format(beginTime)+"结束时间"+df.format(endTime)+"oid"+oid+"查询条数"+refundList.size());
		    	logger.info("第"+(start+1)+"次循环，开始oid"+oid+"查询条数为"+refundList.size());
		    	//得到最后一个子订单的id
		    	if(refundList.size()>0){
		    		oid=refundList.get(refundList.size()-1).getOid();
		    	}
		    	/*refundList=refundList
		    			.stream()
		    			.filter(refund->refund.getSellerNick().equals("北京冰点零度")
		    					||refund.getSellerNick().equals("溜溜梅旗舰店")
		    					||refund.getSellerNick().equals("小白你什么都没看见哦")
		    					||refund.getSellerNick().equals("七彩虹包装用品")
		    			 )
		    	        .collect(Collectors.toList());*/
		    	if(!ValidateUtil.isEmpty(refundList)){
		    		logger.info("过滤后的退款订单的数量为"+refundList.size()+"放入退款订单队列");
		    		RefundQueue.tbRefundQueue.put(refundList);	
		    	}
		    	start++;
				if(RefundQueue.tbRefundQueue.size()<5000){
					logger.info("本次循环,退款队列大小为"+RefundQueue.tbRefundQueue.size()+"休息10秒钟");
					Thread.sleep(10*1000L);
				}else{
					logger.info("本次循环退款队列长度大于5000，退款队列长度为"+RefundQueue.tbRefundQueue.size()+"休息5秒");
				    Thread.sleep(5*1000L);
				}
			} catch (Exception e) {
				logger.info("放入队列的过程中，出现错误"+e.getMessage());
				e.printStackTrace();
			}
		}
		logger.info("下次同步时间为"+DateUtils.formatDate(endTime, DateUtils.DEFAULT_TIME_FORMAT));
		//将下次同步的时间写入到redis
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.REFUND_NODE_START_TIME_KEY,
				DateUtils.formatDate(endTime, DateUtils.DEFAULT_TIME_FORMAT),false);
		Long newTotalSum=Long.valueOf(totalRefundNum)+refundCount;
		//将同步的数量
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.TOTAL_RETURN_DATA_COUNT_KEY,newTotalSum,false);
	}

    /**
     * 在这里将tbRefund转换为RefundDTO
     */
	@Override
	public void convertRefundDTO(List<TbRefund> tbRefundList) {
		//异步执行保存
		SynThreadPool.getMartetingThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				Map<Long, List<RefundDTO>> refundDtoList = getSplitRefundMap(tbRefundList);
				//分uid进行保存
				for(Long uid:refundDtoList.keySet()){
					try {
						//区分保存或更新
						Map<String, List<RefundDTO>> saveAndupdate = divisionRefudnDto(uid,refundDtoList.get(uid));
						List<RefundDTO> insertRefundList = saveAndupdate.get("insertRefundList");
						List<RefundDTO> updateRefundList = saveAndupdate.get("updateRefundList");
						//保存退款
						if(insertRefundList!=null&&insertRefundList.size()>0){
							logger.info("用户"+uid+"保存退款数量为"+insertRefundList.size());
							refundService.saveRefund(uid,insertRefundList);
						}
						//更新退款
						if(updateRefundList!=null&&updateRefundList.size()>0){
							logger.info("用户"+uid+"更新退款数量为"+updateRefundList.size());
							refundService.updateRefund(uid,updateRefundList);
						}
					} catch (Exception e) {
						logger.error("批量保存或更新退款订单出错");
						e.printStackTrace();
					}
					//无论是否更新或者是保存，都要更新子订单的状态
					try {
						logger.info("更新子订单的退款状态,用户为"+uid);
						updateOrderStatus(uid,refundDtoList.get(uid));
					} catch (Exception e) {
						logger.error("通过退款订单更新子订单失败");
						e.printStackTrace();	
					}
				}
			}

		});
	}
	//通过退款订单修改订单的状态
	private void updateOrderStatus(Long uid, List<RefundDTO> list) {
		 if(uid==null||list==null||list.size()<=0){
			 logger.info("修改子订单的状态，传递的参数有误,uid="+uid+"修改集合"+list);
			 return;
		 }
		 logger.info("用户"+uid+"修改订单个数为"+list.size());
		 List<OrderDTO> orderList=new ArrayList<OrderDTO>();
		 for(RefundDTO refundDto:list){
			 OrderDTO orderdto=new OrderDTO();
			 orderdto.setTbrefundStatus(refundDto.getStatus());
			 orderdto.setTborderStatus(refundDto.getOrderStatus());
			 orderdto.setOid(refundDto.getOid());
			 orderList.add(orderdto);
		 }
		 orderDTOService.updateOrderStatus(uid, orderList);
	}
	/**
	 * 区分该用户是保存还是更新
	 * @param list
	 * @return
	 */
    private Map<String, List<RefundDTO>> divisionRefudnDto(Long uid,List<RefundDTO> list) {
    	Map<String, List<RefundDTO>> map=new HashMap<String, List<RefundDTO>>();
    	List<RefundDTO> insertRefundList=new ArrayList<RefundDTO>();
    	List<RefundDTO> updateRefundList=new ArrayList<RefundDTO>();
    	map.put("insertRefundList", insertRefundList);
    	map.put("updateRefundList", updateRefundList);
    	for(RefundDTO rfd:list){
    		Long refundId = rfd.getRefundId();
    		if(uid==null||refundId==null){
    			logger.info("本次循环参数错误，uid"+uid+"退款id为"+refundId);
    			continue;
    		}
    		if(refundService.isExit(uid,refundId)){
    			updateRefundList.add(rfd);
    		}else{
    			insertRefundList.add(rfd);
    		}
    	}	
		return map;
	}

	/**
     * 将数据安装uid进行分割
     * @param tbRefundList
     * @return
     */
	private Map<Long, List<RefundDTO>> getSplitRefundMap(List<TbRefund> tbRefundList) {
		Map<Long, List<RefundDTO>> map=new HashMap<Long, List<RefundDTO>>();
		for(TbRefund tbr:tbRefundList){
			  RefundDTO refundDto=getRefundDTO(tbr);
			  if(refundDto.getSellerNick()==null){
				  logger.info("卖家昵称为空"+refundDto.getRefundId());
				  continue;
			  }
	          //查询出uid
			  Long uid = userInfoService.findUidBySellerNick(refundDto.getSellerNick());
		      if(uid==null){
		    	  logger.info("用户查不到uid,用户为"+refundDto.getSellerNick());
		    	  continue;
		      }	
		      refundDto.setUid(uid);
		      //进行拆分
		      if(map.containsKey(uid)){
		    	  List<RefundDTO> list = map.get(uid);
		    	  list.add(refundDto);
		      }else{
		    	  List<RefundDTO> list=new ArrayList<RefundDTO>();
		    	  list.add(refundDto);
		    	  map.put(uid, list);
		      }
		}
		return map;
	}
	
    /**
     * 将淘宝的tbRefund对象装换为RefundDto
     * @param tbr
     * @return
     */
	private RefundDTO getRefundDTO(TbRefund tbr) {
		RefundDTO refundDto=new RefundDTO();
		if(tbr!=null &&tbr.getJdpResponse()!=null){
			String jdpResponse = tbr.getJdpResponse();
			try {
				RefundGetResponse parseResponse = TaobaoUtils.parseResponse(jdpResponse, RefundGetResponse.class);
			    if(parseResponse==null||parseResponse.getRefund()==null){
			    	logger.info("淘宝 返回对象为空");
			    	return null;
			    }
			    Refund refund = parseResponse.getRefund();
			    //封装Refund
			    refundDto.setRefundId(refund.getRefundId());
			    refundDto.setStatus(refund.getStatus());
			    refundDto.setSellerNick(refund.getSellerNick());
			    refundDto.setTid(refund.getTid());
			    refundDto.setOid(refund.getOid());
			    refundDto.setCreated(refund.getCreated());
			    refundDto.setModified(refund.getModified());
			    refundDto.setAdvanceStatus(refund.getAdvanceStatus());
			    refundDto.setAlipayNo(refund.getAlipayNo());
			    refundDto.setAttribute(refund.getAttribute());
			    refundDto.setCsStatus(refund.getCsStatus());
			    refundDto.setDesc(refund.getDesc());
			    refundDto.setGoodStatus(refund.getGoodStatus());
			    refundDto.setHasGoodReturn(refund.getHasGoodReturn());
			    refundDto.setNum(refund.getNum());
			    refundDto.setNumIid(refund.getNumIid());
			    refundDto.setOperationContraint(refund.getOperationContraint());
			    refundDto.setOrderStatus(refund.getOrderStatus());
			    refundDto.setOuterId(refund.getOuterId());
			    refundDto.setReason(refund.getReason());
			    refundDto.setRefundPhase(refund.getRefundPhase());
			    refundDto.setRefundVersion(refund.getRefundVersion());
			    refundDto.setSku(refund.getSku());
			    refundDto.setTitle(refundDto.getTitle());
			    if(refund.getPayment()!=null){
			    	refundDto.setPayment(new BigDecimal(refund.getPayment()));
			    }
			    if(refund.getPrice()!=null){
			    	refundDto.setPrice(new BigDecimal(refund.getPrice()));	
			    }
			    if(refund.getRefundFee()!=null){
			    	refundDto.setRefundFee(new BigDecimal(refund.getRefundFee()));	
			    }
			    if(refundDto.getTotalFee()!=null){
			    	refundDto.setTotalFee(refundDto.getTotalFee());
			    }
			} catch (ApiException e) {
				logger.info("淘宝的tbRefund对象装换为RefundDto出错"+e.getMessage());
				e.printStackTrace();
			}
		}
		return refundDto;
	}
	
	
}
