package com.kycrm.member.dao.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.vo.order.OrderVO;
import com.kycrm.member.domain.vo.trade.TradeVO;

public interface IOrderDTODao {
    
    /**
     * 自动创建用户表
     * @author: wy
     * @time: 2018年1月18日 上午11:46:14
     * @param uid 用户表主键id
     */
    public void doCreateTableByNewUser(Long uid);
    
    /**
     * 是否存在短信记录表
     * @author: wy
     * @time: 2018年1月18日 上午11:48:10
     * @param uid
     * @return
     */
    public List<String> isExistsTable(Long uid);
    
    /**
     * 批量保存子订单
     * @author: wy
     * @time: 2018年1月23日 下午3:15:49
     * @param orderDTOList
     */
    public void doCreateOrderDTOByList(Map<String, Object> map);
    
    /**
     * 单个保存子订单
     * @author: wy
     * @time: 2018年1月23日 下午3:17:43
     * @param orderDTO
     */
    public void doCreateOrderDTOByBySingle(OrderDTO orderDTO);
    
    /**
     * 批量更新子订单
     * @author: wy
     * @time: 2018年1月23日 下午3:35:05
     * @param orderDTOList
     */
    public void doUpdateOrderDTOByList(Map<String, Object> map);
    
    /**
     * 更新单个子订单信息
     * @author: wy
     * @time: 2018年1月23日 下午3:35:50
     * @param orderDTO
     */
    public void doUpdateOrderDTOBySingle(OrderDTO orderDTO);
    
    public String findStatusByOid(Map<String,Long> map);
    
    /**
     * 通过条件查询订单数
     * @author HL
     * @time 2018年2月1日 下午2:59:22 
     * @param odVo
     * @return
     */
	public Long findOrderDTOCount(OrderVO odVo);

	/**
	 * 通过条件查询订单集合数据
	 * @author HL
	 * @time 2018年2月1日 下午3:00:21 
	 * @param odVo
	 * @return
	 */
	public List<OrderDTO> findOrderDTOList(OrderVO odVo);

    /**
     * 订单短信群发筛选订单
     * @Title: listOrderDTOs 
     * @param @param tradeVO
     * @param @return 设定文件 
     * @return List<OrderDTO> 返回类型 
     * @throws
     */
	public List<OrderDTO> listOrderDTOs(TradeVO tradeVO);

	/**
	 * 订单短信群发筛选订单总数
	 * @Title: countOrderDTO 
	 * @param @param tradeVO
	 * @param @return 设定文件 
	 * @return long 返回类型 
	 * @throws
	 */
	public Long countOrderDTO(TradeVO tradeVO);

	/**
     * 通过oid 查询订单是否存在
     * @author HL
     * @time 2018年6月1日 下午3:05:24 
     * @param uid
     * @param newTid
     * @return
     */
	public Integer findOneOrderDTOByTid(Map<String, Object> map);
	
	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单信息
	 * @Date 2018年7月25日下午6:09:24
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType List<OrderDTO>
	 */
	public List<OrderDTO> findMemberOrderByConditionAndPagination(Map<String, Object> paramMap)throws Exception;
	
	/**
	 * listOrderByTid(通过tid查询子订单)
	 * @Title: listOrderByTid 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<OrderDTO> 返回类型 
	 * @throws
	 */
	public List<OrderDTO> listOrderByTid(Map<String, Object> map);
	
	/**
	 * listOrderByTids(通过tid集合查询子订单)
	 * @Title: listOrderByTids 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<OrderDTO> 返回类型 
	 * @throws
	 */
	public List<OrderDTO> listOrderByTids(Map<String, Object> map);
	
	/**
	 * listOrderByPhone(根据手机号查询子订单)
	 * @Title: listOrderByPhone 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<OrderDTO> 返回类型 
	 * @throws
	 */
	public List<OrderDTO> listOrderByPhone(Map<String, Object> map);

	/**
	 * 通过oid查询oid是否存在-------次方法有坑，请勿调用
	 * @author HL
	 * @time 2018年8月24日 下午6:34:55 
	 * @param id
	 * @param map
	 * @return
	 */
	public List<Long> findOrderDTOByIds(Map<String, Object> map);
	
	/**
	 * listTradeByNick(查询会员购买历史记录)
	 * @Title: listTradeByNick 
	 * @param @param uid
	 * @param @param buyerNick
	 * @param @return 设定文件 
	 * @return List<OrderDTO> 返回类型 
	 * @throws
	 */
	List<OrderDTO> listTradeByNick(@Param("uid") Long uid,@Param("buyerNick") String buyerNick);
	
	 /**
     * 订单短信群发查询订单
     * @Title: listTradeInfo 
     * @param @param tradeVO
     * @param @return 设定文件 
     * @return List<TradeDTO> 返回类型 
     * @throws
     */
	public List<OrderDTO> listMarketingCenterOrder(TradeVO tradeVO);
	
	/**
	 * 订单短信群发筛选全部订单个数
	 */
	public Long countMarketingCenterOrder(TradeVO tradeVO);
	
	/**
	 * updateLastSendMsg(批量更新订单最后一次发送营销的msgId)
	 * @Title: updateLastSendMsg 
	 * @param @param map 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void updateLastSendMsg(Map<String, Object> map);

	public void addOrderTableIndex(Long uid);
    /**
     * 修改订单的状态
     * @param map
     */
	public void updateOrderStatus(Map<String, Object> map);
	
	public List<OrderDTO> listRefundOrderByTid(Map<String, Object> map);
}
