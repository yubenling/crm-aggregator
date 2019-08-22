package com.kycrm.member.service.member;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.to.SimplePage;
import com.kycrm.member.domain.vo.effect.EffectStandardRFMVO;
import com.kycrm.member.domain.vo.member.MemberCriteria;
import com.kycrm.member.domain.vo.member.MemberInfoVO;
import com.kycrm.member.domain.vo.member.MemberMsgCriteria;

/** 
* @author wy
* @version 创建时间：2018年1月18日 下午2:19:22
*/
public interface IMemberDTOService {
    
    /**
     * 根据用户主键id创建对应的短信记录表
     * @author: wy
     * @time: 2018年1月18日 上午11:53:10
     * @param uid 用户主键id
     */
    public void doCreateTableByNewUser(Long uid);
    public void doCreateMemberReceiveDetailTableByNewUser(Long uid);
    
    /** 
	* @Description 订单抽取会员,保存或者更新一批会员数据
	* @param  uid
	* @param  members    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月31日 上午11:56:44
	*/
	public void saveSynMemberData(Long uid, List<MemberInfoDTO> members);
	
   /** 
    * @Description 批量插入同步的会员信息
    * @param  uid
    * @param  members    设定文件 
    * @return void    返回类型 
    * @author jackstraw_yu
    * @date 2018年1月31日 下午6:20:33
    */
	public void batchSaveMemberData(Long uid, List<MemberInfoDTO> members);

   /** 
    * @Description 批量更新同步的会员信息
    * @param  uid
    * @param  members    设定文件 
    * @return void    返回类型 
    * @author jackstraw_yu
    * @date 2018年1月31日 下午6:21:01
    */
	public void batchUpdateMemberData(Long uid, List<MemberInfoDTO> members);
    
   /**
    * 查询会员 
    * @Title: findMemberByParam 
    * @param @param memberInfo
    * @param @return 设定文件 
    * @return MemberInfo 返回类型 
    * @throws
    */
	public MemberInfoDTO findMemberByParam(Long uid, String buyerNick);
	
	/**
	 * 统计会员个数
	 * @Title: countMemberByParam 
	 * @param @param memberInfo
	 * @param @return 设定文件 
	 * @return long 返回类型 
	 * @throws
	 */
	public long countMemberByParam(Long uid,MemberInfoVO memberInfoVO);

	/** 
	* @Description 客户信息页,查询会员分页信息
	* @param  criteria
	* @param  uid
	* @return SimplePage    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月8日 上午10:18:29
	*/
	public SimplePage queryMemberPage(Long uid,MemberCriteria criteria);

	/** 
	* @Description 客户信息页,查询单个客户详情
	* @param  uid
	* @param  criteria
	* @return MemberInfoDTO    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月8日 下午12:11:11
	*/
	public MemberInfoDTO queryMemberInfo(Long uid, MemberCriteria criteria);

	/** 
	* @Description 修改会员信息
	* @param  uid
	* @param  member    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月27日 下午3:42:20
	*/
	public void updateMembeInfo(Long uid, MemberInfoDTO member);

	/** 
	* @Description 更新会员备注
	* @param  uid
	* @param  member    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年2月28日 上午10:50:15
	*/
	public void updateMembeRemarks(Long uid, MemberInfoDTO member);

	/** 
	* @Description 会员短信群发,查询会员
	* @param  uid
	* @param  criteria
	* @param  dynamicKey
	* @return Map<String,Object>    返回类型 
	* @author jackstraw_yu
	* @date 2018年3月7日 下午1:45:30
	*/
	public Map<String, Object> queryMemberList(Long uid, MemberMsgCriteria criteria, String dynamicKey);

	/**
	 * 通过用户昵称查询手机号码
	 * @author HL
	 * @time 2018年4月25日 下午4:26:21 
	 * @param id
	 * @param encryptName
	 * @return
	 */
	public String findPhoneBybuyerNick(Long uid, String encryptName);

	/**
	 * 通过会员名称修改会员状态
	 * @author HL
	 * @time 2018年5月7日 下午3:04:12 
	 * @param map
	 */
	public void batchUpdateMemberStatus(Long uid,Map<String, Object> map);
	
	
	/**
	 * countMemberByInfo(会员短信群发查询会员数量 )
	 * @Title: countMemberByInfo 
	 * @param @param criteria
	 * @param @return 设定文件 
	 * @return int 返回类型 
	 * @throws
	 */
	public int countMemberByInfo(Long uid, MemberMsgCriteria criteria);

	/**
	 * listMemberByInfo(会员短信群发查询会员集合)
	 * @Title: listMemberByInfo 
	 * @param @param criteria
	 * @param @return 设定文件 
	 * @return List<MemberInfoDTO> 返回类型 
	 * @throws
	 */
	public List<MemberInfoDTO> listMemberByInfo(Long uid, MemberMsgCriteria criteria);

	/**
	 * 根据用户主键id创建对应卖家的买家购买统计
	 * @author sungk
	 * @param uid
	 */
	public void doCreateMemberItemAmountTableByNewUser(long uid);

	public void doCreateSmsBlacklistTableByNewUser(long uid);

	public void doCreateTradeRatesTableByNewUser(Long uid);

	/**
	 * 异步修改会员状态
	 * @author HL
	 * @time 2018年8月6日 下午12:00:44 
	 * @param uid
	 * @param buyerNicks
	 * @param status
	 */
	public void threadDisposeMemberStatus(Long uid,
			List<String> buyerNicks, String status);

	/**
	 * listMemberByPhone(手机号查询会员)
	 * @Title: listMemberByPhone 
	 * @param @param uid
	 * @param @param mobile
	 * @param @return 设定文件 
	 * @return List<MemberInfoDTO> 返回类型 
	 * @throws
	 */
	List<MemberInfoDTO> listMemberByPhone(Long uid, String mobile);
	/**
	 * updateMemberStatus(更新会员状态)
	 * @Title: updateMemberStatus 
	 * @param @param uid
	 * @param @param status 设定文件 
	 * @return Boolean 返回类型 
	 * @throws
	 */
	public Boolean updateMemberStatus(Long uid, String buyerNick, String status);
	/**
	 * updateMembeInfoByPhone根据手机号修改会员信息
	 * @param uid
	 * @param memberInfo
	 */
	public void updateMembeInfoListByPhone(Long uid, List<MemberInfoDTO> memberInfoList);
	
	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 创建高级会员筛选历史记录表
	 * @Date 2018年11月14日上午11:33:19
	 * @param uid
	 * @ReturnType void
	 */
	public void doCreatePremiumFilterRecordTable(long uid);
	
	/**
	 * countMemberAmountByTimes(查询RFM标准分析购买次数和最后购买时间对应的会员数)
	 * @Title: countMemberAmountByTimes 
	 * @param @param uid
	 * @param @param buyTimes
	 * @param @param bTime
	 * @param @param eTime
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	Long countMemberAmountByTimes(Long uid, Integer buyTimes, Date bTime,
			Date eTime) throws Exception;
	
	/**
	 * sumPaidAmountByTimes(查询RFM标准分析购买次数和最后购买时间对应的累计消费金额以及平均客单价)
	 * @Title: sumPaidAmountByTimes 
	 * @param @param uid
	 * @param @param buyTimes
	 * @param @param bTime
	 * @param @param eTime
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return Double 返回类型 
	 * @throws
	 */
	Double sumPaidAmountByTimes(Long uid, Integer buyTimes, Date bTime,
			Date eTime) throws Exception;
	
	/**
	 * aggregateRFMDetail(计算RFM详情分析顶部数据)
	 * @Title: aggregateRFMDetail 
	 * @param @param uid
	 * @param @param tradeNum
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return EffectStandardRFMVO 返回类型 
	 * @throws
	 */
	EffectStandardRFMVO aggregateRFMDetail(Long uid, Integer tradeNum)
			throws Exception;
	
	/**
	 * 计算RFM标准分析数据
	 * @param uid
	 * @param tradeNum
	 * @param bTime
	 * @param eTime
	 * @return
	 */
	MemberInfoDTO queryStandardRFM(Long uid, Integer tradeNum, Date bTime,
			Date eTime);
}
