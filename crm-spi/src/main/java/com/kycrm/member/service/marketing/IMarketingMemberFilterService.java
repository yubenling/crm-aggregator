package com.kycrm.member.service.marketing;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberDetailDTO;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.utils.pagination.Pagination;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.member.MemberInformationDetailUpdateVO;
import com.kycrm.member.domain.vo.member.MemberInformationDetailVO;
import com.kycrm.member.domain.vo.member.MemberInformationSearchVO;
import com.kycrm.member.domain.vo.member.MemberOrderVO;
import com.kycrm.member.domain.vo.member.MemberRemarkVO;

public interface IMarketingMemberFilterService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件查询会员信息
	 * @Date 2018年7月17日下午3:27:02
	 * @param uid
	 * @param accessToken
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String, Object>
	 */
	public Map<String, Object> findMembersByCondition(Long uid, String taobaoUserNick, String accessToken,
			MemberFilterVO memberFilterVO) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件查询会员信息
	 * @Date 2018年9月3日下午3:11:25
	 * @param uid
	 * @param accessToken
	 * @param memberFilterVO
	 * @param pageNo
	 * @param currentRows
	 * @param isDownload
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	public Map<String, Object> findMembersByCondition(Long uid, String taobaoUserNick, String accessToken,
			MemberFilterVO memberFilterVO, Integer pageNo, Integer currentRows, Boolean isDownload,
			Boolean isSendMessage) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件分页查询会员信息
	 * @Date 2018年7月21日下午3:20:47
	 * @param uid
	 * @param accessToken
	 * @param memberInfoSearchVO
	 * @param contextPath
	 * @param pageNo
	 * @return
	 * @throws Exception
	 * @ReturnType Pagination
	 */
	public Pagination findMembersByConditionAndPagination(Long uid, String taoBaoUserNick, String accessToken,
			MemberInformationSearchVO memberInfoSearchVO, String contextPath, Integer pageNo) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件分页查询会员信息
	 * @Date 2018年8月6日下午4:08:07
	 * @param uid
	 * @param accessToken
	 * @param memberFilterVO
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @throws Exception
	 * @ReturnType List<MemberInfoDTO>
	 */
	public List<MemberInfoDTO> findMembersByCondition(Long uid, String taobaoUserNick, String accessToken,
			MemberInformationSearchVO memberInfoSearchVO, Integer pageSize, Integer pageNo) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据筛选条件查询会员数量
	 * @Date 2018年7月20日下午2:52:12
	 * @param uid
	 * @param memberFilterVO
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Long findMemberCountByCondition(Long uid, MemberFilterVO memberFilterVO) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询客户信息详情
	 * @Date 2018年7月25日下午3:04:59
	 * @param uid
	 * @param accessToken
	 * @param memberInformationDetailVO
	 * @return
	 * @ReturnType MemberDetailDTO
	 */
	public MemberDetailDTO findMemberInfoDetail(Long uid, String taobaoUserNick, String accessToken,
			MemberInformationDetailVO memberInformationDetailVO) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 更新会员信息详情中的个人信息部分
	 * @Date 2018年7月25日下午3:47:58
	 * @param uid
	 * @param memberInformationDetailUpdateVO
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer updateMemberInformationDetail(Long uid,
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 添加备注
	 * @Date 2018年7月25日下午4:45:23
	 * @param uid
	 * @param memberRemarkVO
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer addMemberRemark(Long uid, MemberRemarkVO memberRemarkVO) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 修改备注
	 * @Date 2018年7月25日下午4:45:23
	 * @param uid
	 * @param memberRemarkVO
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer updateMemberRemark(Long uid, MemberRemarkVO memberRemarkVO) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 删除备注
	 * @Date 2018年7月25日下午4:45:23
	 * @param uid
	 * @param memberRemarkVO
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer deleteMemberRemark(Long uid, MemberRemarkVO memberRemarkVO) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 订单信息
	 * @Date 2018年7月25日下午5:32:16
	 * @param uid
	 * @param memberOrderVO
	 * @param uid
	 * @param contextPath
	 * @param pageNo
	 * @return
	 * @throws Exception
	 * @ReturnType Pagination
	 */
	public Pagination findMemberOrderByConditionAndPagination(Long uid, MemberOrderVO memberOrderVO, String userId,
			String accessToken, String contextPath, Integer pageNo) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询购买一次的客户数量
	 * @Date 2018年8月15日下午4:30:27
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer findBuyingOneTimeCustomer(Long uid) throws Exception;

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
	public Integer findBuyingGreaterThanTwoTimes(Long uid) throws Exception;

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
	public Integer findIntentionCustomer(Long uid) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询近三个月未购买客户数量
	 * @Date 2018年8月15日下午4:50:25
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer findLastThreeMonthsUntradedCustomer(Long uid) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询全部客户数量
	 * @Date 2018年8月15日下午5:00:40
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer findAllCustomer(Long uid) throws Exception;
	
	
	public Long findMemberCountByCreatedDate(Long uid, Date createdDate) throws Exception;

}
