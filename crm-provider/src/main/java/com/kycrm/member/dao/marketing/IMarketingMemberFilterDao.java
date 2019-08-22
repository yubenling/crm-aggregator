package com.kycrm.member.dao.marketing;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberDetailDTO;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;

public interface IMarketingMemberFilterDao {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员表与订单表关联查询会员信息
	 * @Date 2018年7月18日下午6:09:21
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberInfoDTO>
	 */
	public List<MemberInfoDTO> findFromMemberInfoDTOJoinOrderDTO(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件只需要在会员表中查询会员信息
	 * @Date 2018年7月18日下午6:09:26
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberInfoDTO>
	 */
	public List<MemberInfoDTO> findFromMemberInfoDTO(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件分页查询会员信息【会员信息页使用】
	 * @Date 2018年7月25日下午1:56:25
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberInfoDTO>
	 */
	public List<MemberInfoDTO> findFromMemberInfoDTOForMemberInformation(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件，使用会员表与订单表联查获取会员数量
	 * @Date 2018年7月20日下午3:15:25
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType Long
	 */
	public Long findCountFromMemberInfoDTOJoinOrderDTO(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件，使用会员表获取会员数量
	 * @Date 2018年7月20日下午3:16:10
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType Long
	 */
	public Long findCountFromMemberInfoDTO(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询会员信息详情
	 * @Date 2018年7月25日下午3:16:03
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType MemberDetailDTO
	 */
	public MemberDetailDTO findMemberInfoDetail(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 更新会员信息详情中的个人信息部分
	 * @Date 2018年7月25日下午4:16:54
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer updateMemberInformationDetail(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 修改备注
	 * @Date 2018年7月25日下午4:49:13
	 * @param paramMap
	 * @return
	 * @ReturnType Integer
	 */
	public Integer updateMemberRemark(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 删除备注
	 * @Date 2018年7月25日下午4:49:17
	 * @param paramMap
	 * @return
	 * @ReturnType Integer
	 */
	public Integer deleteMemberRemark(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询购买一次的客户数量
	 * @Date 2018年8月15日下午4:33:26
	 * @param paramMap
	 * @throws Exception
	 * @ReturnType void
	 */
	public Integer findBuyingOneTimeCustomer(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询购买两次及以上的客户数量
	 * @Date 2018年8月15日下午4:35:58
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer findBuyingGreaterThanTwoTimes(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询意向客户数量
	 * @Date 2018年8月15日下午4:42:02
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer findIntentionCustomer(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 近三个月未购买客户
	 * @Date 2018年8月15日下午4:50:25
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer findLastThreeMonthsUntradedCustomer(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询全部客户
	 * @Date 2018年8月15日下午5:01:40
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer findAllCustomer(Map<String, Object> paramMap) throws Exception;

	public Long findMemberCountByCreatedDate(Map<String, Object> paramMap) throws Exception;

}
