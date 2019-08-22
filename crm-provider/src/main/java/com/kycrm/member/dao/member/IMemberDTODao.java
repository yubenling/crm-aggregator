package com.kycrm.member.dao.member;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.vo.member.MemberCriteria;
import com.kycrm.member.domain.vo.member.MemberInfoVO;
import com.kycrm.member.domain.vo.member.MemberMsgCriteria;


/** 
* @ClassName: IMemberDTODao 
* @Description: 会员dao
* @author jackstraw_yu
* @date 2018年2月1日 下午1:50:24 
*  
*/
public interface IMemberDTODao {
    /**
     * 自动创建用户表
     * @author: wy
     * @time: 2018年1月18日 上午11:46:14
     * @param uid 用户表主键id
     */
    public void doCreateTableByNewUser(@Param("uid") Long uid);
    
    /**
     * 自动创建卖家的会员购买统计表
     * @param uid
     */
    public void doCreateMemberItemAmountTableByNewUser(String uid);
    public void doCreateSmsBlacklistTableByNewUser(String uid);
    public void doCreateTradeRatesTableByNewUser(String uid);
    /**
     * 是否存在短信记录表
     * @author: wy
     * @time: 2018年1月18日 上午11:48:10
     * @param uid
     * @return
     */
    public List<String> isExistsTable(@Param("uid") Long uid);

    /**
     * 是否存在卖家的会员购买统计表
     * @param uid
     * @return
     */
    public List<String> isExistsMemberItemAmountTable(String uid);
    public List<String> isExistsSmsBlacklistTable(String uid);
    public List<String> isExistsTradeRatesTable(String uid);
    /** 
	* @Description 查询会员是否存在
	* @param  member    设定文件 
	* @return MemberInfoDTO    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月1日 上午10:28:08
	*/
	public MemberInfoDTO queryMemberByBuyerNick(Long uid, String buyerNick);

	/** 
	* @Description 批量保存同步会员信息
	* @param  entity    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月1日 上午10:53:25
	*/
	public void batchSaveMemberData(BaseListEntity<MemberInfoDTO> entity);

	/** 
	* @Description 批量更新同步会员信息
	* @param  param    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月1日 上午10:54:07
	*/
	public void batchUpdateMemberData(BaseListEntity<MemberInfoDTO> entity);
	
	/**
	 * 查询会员
	 * @Title: findMemberByParam 
	 * @param @param memberInfoDTO
	 * @param @return 设定文件 
	 * @return MemberInfoDTO 返回类型 
	 * @throws
	 */
	public MemberInfoDTO findMemberByParam(MemberInfoDTO memberInfoDTO);

	/**
	 * 统计会员个数(首页)
	 * @Title: countMemberByParam 
	 * @param @param memberInfoVO
	 * @param @return 设定文件 
	 * @return long 返回类型 
	 * @throws
	 */
	public Long countMemberByParam(MemberInfoVO memberInfoVO);

	/** 
	* @Description 会员信息页,查询会员列表
	* @param  criteria
	* @return List<MemberInfoDTO>    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月8日 上午11:02:37
	*/
	public List<MemberInfoDTO> queryMemberLimitList(MemberCriteria criteria);

	/** 
	* @Description 会员信息页,查询会员数量
	* @param  criteria
	* @return Long   返回类型 
	* @author jackstraw_yu
	* @date 2018年2月8日 上午11:02:37
	*/
	public Long queryMemberCount(MemberCriteria criteria);

	/** 
	* @Description 客户信息页,查询单个客户详情
	* @param  id
	* @param  criteria
	* @return MemberInfoDTO    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月8日 下午12:11:11
	*/
	public MemberInfoDTO queryMemberInfo(MemberCriteria criteria);

	/** 
	* @Description 修改会员信息
	* @param  member
	* @param     设定文件 
	* @return int    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月27日 下午3:53:36
	*/
	public int updateMembeInfo(MemberInfoDTO member);

	/** 
	* @Description 更新会员备注
	* @param  uid
	* @param  member    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月28日 上午10:50:15
	*/
	public int updateMembeRemarks(MemberInfoDTO member);


	/**
	 * 会员短信群发查询会员数量(关联订单表)
	 * @Title: countMemberByInfo 
	 * @param @param criteria
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public Integer countMemberByTrade(MemberMsgCriteria criteria);
	
	/**
	 * listMemberByTrade(会员短信群发查询会员集合(关联订单表))
	 * @Title: listMemberByTrade 
	 * @param @param criteria
	 * @param @return 设定文件 
	 * @return List<MemberInfoDTO> 返回类型 
	 * @throws
	 */
	public List<MemberInfoDTO> listMemberByTrade(MemberMsgCriteria criteria);
	

	/**
	 * countMemberByInfo(会员短信群发查询会员数量 (只查询会员表))
	 * @Title: countMemberByInfo 
	 * @param @param criteria
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public Integer countMemberByInfo(MemberMsgCriteria criteria);

	/**
	 * listMemberByInfo(会员短信群发查询会员集合 (只查询会员表))
	 * @Title: listMemberByInfo 
	 * @param @param criteria
	 * @param @return 设定文件 
	 * @return List<MemberInfoDTO> 返回类型 
	 * @throws
	 */
	public List<MemberInfoDTO> listMemberByInfo(MemberMsgCriteria criteria);

	/**
	 * 通过用户昵称查询手机号码
	 * @author HL
	 * @time 2018年4月25日 下午4:26:21 
	 * @param id
	 * @param encryptName
	 * @return
	 */
	public String findPhoneBybuyerNick(Map<String, Object> map);
	
	
	/**
	 * 通过会员名称修改会员状态
	 * @author HL
	 * @time 2018年5月7日 下午3:06:29 
	 * @param map
	 */
	public void batchUpdateMemberStatus(Map<String, Object> map);
	
	
	public List<MemberInfoDTO> listMemberByPhone(@Param("uid") Long uid, @Param("mobile") String mobile);
	
	
	public void updateMemberStatus(@Param("uid") Long uid, 
			@Param("buyerNick") String buyerNick,@Param("status") String status);

	public void updateMembeInfoListByPhone(Map<String, Object> map);
	
	public List<String> isExistsMemberReceiveDetailTable(Long uid);
	
	public void doCreateMemberReceiveDetailTableByNewUser(Long uid);
	
	public void addMemberReceiveDetailTableIndex(Long uid);
	
	 /** 
	    * @Description 会员添加索引
	    * @param  uid    设定文件 
	    * @return void    返回类型 
	    * @author jackstraw_yu
	    * @date 2018年2月5日 下午6:19:45
	    */
	    public void addMemberTableIndex(Long uid);

	/**
	 * 添加评价表索引
	 */
	public void addTradeRatesTableIndex(Long uid);
	
	/**
	 * 添加黑名单表索引
	 */
	public void addSmsBlacklistTableIndex(String userId);

	public void addMemberItemAmountTableIndex(String userId);
	
	public List<String> isExistsPremiumFilterRecordTable(@Param("uid") Long uid) throws Exception;

	public void doCreatePremiumFilterRecordTable(@Param("uid") Long uid) throws Exception;
	
	public Long countMemberAmountByTimes(Map<String, Object> map) throws Exception;
	
	public BigDecimal sumPaidAmountByTimes(Map<String, Object> map) throws Exception;
	
	
	/**
	 * RFM详情查询成交会员总数
	 */
	public Long countDistinctMemberCount(Map<String, Object> map) throws Exception;
	
	/**
	 * RFM详情查询成交会员累计消费金额
	 */
	public BigDecimal sumMemberPaidAmount(Map<String, Object> map) throws Exception;
	
	/**
	 * 计算标准RFM分析中的数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public MemberInfoDTO queryStandardRFM(Map<String, Object> map) throws Exception;
	
}
