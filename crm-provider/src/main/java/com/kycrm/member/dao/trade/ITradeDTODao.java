package com.kycrm.member.dao.trade;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.vo.order.OrderVO;
import com.kycrm.member.domain.vo.trade.TradeVO;

public interface ITradeDTODao {
    
	/**
	 * 自动创建用户表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:46:14
	 * @param uid
	 *            用户表主键id
	 */
	public void doCreateTableByNewUser(String uid);
    
    /**
	 * 是否存在短信记录表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:48:10
	 * @param uid
	 * @return
	 */
	public List<String> isExistsTable(String uid);

    
    /**
     * 批量保存新的订单
     * @author: wy
     * @time: 2018年1月22日 上午11:29:21
     * @param tradeDTO
     */
    public void doCreateTradeDTOByList(Map<String, Object> map);
    
    
    /**
     * 保存单个短信记录
     * @author: wy
     * @time: 2018年1月22日 下午2:20:09
     * @param tradeDTO
     */
    public void doCreateTradeDTOByBySingle(TradeDTO tradeDTO);
    
    /**
     * 通过主订单号找到订单状态
     * @author: wy
     * @time: 2018年1月22日 下午2:58:27
     * @param map 
     *       tid    主订单号
     *       uid    用户主键id
     * @return
     */
    public String findStatusByTid(Map<String,Long> map);
    
    /**
     * 批量更新多个订单
     * @author: wy
     * @time: 2018年1月23日 上午11:24:54
     * @param tradeDTOList
     */
    public void doUpdateTradeDTOByList(Map<String, Object> map);
    
    /**
     * 更新单个订单
     * @author: wy
     * @time: 2018年1月23日 上午11:25:28
     * @param tradeDTO
     */
    public void doUpdateTradeDTOBySingle(TradeDTO tradeDTO);
    
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
	 * 查询时间段内每天的下单金额
	 * @Title: listDaysPayment 
	 * @param @return 设定文件 
	 * @return List<TradeDTO> 返回类型 
	 * @throws
	 */
	public List<TradeVO> listDaysPayment(TradeVO tradeVO);
	
	/**
	 * 订单中心效果分析查询状态变化的主订单
	 * @Title: listTradeByStatus 
	 * @param @return 设定文件 
	 * @return List<TradeDTO> 返回类型 
	 * @throws
	 */
	public List<TradeDTO> listTradeByStatus(TradeVO tradeVO);

	/**
	 * 根据手机号匹配查询新创建的订单
	 * @Title: listNewTradeByPhones 
	 * @param @param tradeVO 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public List<TradeDTO> listNewTradeByPhones(TradeVO tradeVO);

	/**
	 * 通过订单查询出tradeDto是否存在
	 * @author HL
	 * @time 2018年6月1日 下午1:50:30 
	 * @param userId
	 * @param newTid
	 * @return
	 */
	public Integer findOneTradeDTOByTid(Map<String, Object> map);

	
	
	/**
	 * listTradeByTids(通过订单号匹配订单)
	 * @Title: listTradeByTids 
	 * @param @param tidList
	 * @param @return 设定文件 
	 * @return List<TradeDTO> 返回类型 
	 * @throws
	 */
	public List<TradeDTO> listTradeByTids(Map<String, Object> map);

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单信息
	 * @Date 2018年7月26日下午8:49:40
	 * @param paramMap
	 * @throws Exception
	 * @ReturnType void
	 */
	public List<TradeDTO> findMemberTradeByConditionAndPagination(Map<String, Object> paramMap)throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单信息条数
	 * @Date 2018年7月30日下午6:20:08
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer findMemberTradeCount(Map<String, Object> paramMap)throws Exception;
	
	/**
	 * listTradeByPhones(根据手机号查询订单)
	 * @Title: listTradeByPhones 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<TradeDTO> 返回类型 
	 * @throws
	 */
	public List<TradeDTO> listTradeByPhones(Map<String, Object> map);
	
	/**
	 * queryTradeNumByHour(查询时间段内某种状态的订单数（首页）)
	 * @Title: queryTradeNumByHour 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Map<String,Object> 返回类型 
	 * @throws
	 */
	public List<TradeDTO> queryTradeNumByHour(Map<String, Object> map);
	
	/**
	 * sumTradeCenterEffect(订单中心效果分析回款数据)
	 * @Title: sumTradeCenterEffect 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return TradeDTO 返回类型 
	 * @throws
	 */
	public TradeDTO sumTradeCenterEffect(Map<String, Object> map);
	
	/**
	 * listManageTradeDTO(后台管理订单查询)
	 * @Title: listManageTradeDTO 
	 * @param @param orderVO
	 * @param @return 设定文件 
	 * @return List<TradeDTO> 返回类型 
	 * @throws
	 */
	public List<TradeDTO> listManageTradeDTO(OrderVO orderVO);
	
	/**
	 * countManageTradeDTO(后台管理订单个数)
	 * @Title: countManageTradeDTO 
	 * @param @param orderVO
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long countManageTradeDTO(OrderVO orderVO);
	
	public void addTradeTableIndex(String userId);
	
	public Long countMemberAmountByTimes(Map<String, Object> map) throws Exception;
	
	public BigDecimal sumPaidAmountByTimes(Map<String, Object> map) throws Exception;
	
	/**
	 * RFM详情页面的图标数据(下单客户数)
	 */
	public List<TradeVO> listCreateCustomerAmount(Map<String, Object> map) throws Exception;
	
	/**
	 * RFM详情页面的图标数据(购买次数和消费金额)
	 */
	public List<TradeVO> listPaidData(Map<String, Object> map) throws Exception;
	
	
	public List<TradeDTO> listEffectRFMByTime(Map<String, Object> map) throws Exception;
	
	/**
	 * 查询下单未付款客户数
	 */
	public Long countUnPaidMember(Map<String, Object> map) throws Exception;
	
	/**
	 * listTradeByTids(根据订单号查询订单)
	 * @Title: listTradeByTids 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return List<TradeDTO> 返回类型 
	 * @throws
	 */
	public List<TradeDTO> listStepTradeByTids(Map<String, Object> map) throws Exception;
	
	/**
	 * sumPaymentByTids(根据订单号查询总计应付金额)
	 * @Title: sumPaymentByTids 
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return BigDecimal 返回类型 
	 * @throws
	 */
	public BigDecimal sumPaymentByTids(Map<String, Object> map) throws Exception;
}
