package com.kycrm.member.service.usermanagement;

import java.util.Map;

import com.kycrm.member.domain.entity.usermanagement.SellerGroupRule;

/**
 * 用户分组规则接口
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月12日上午10:17:40
 * @Tags
 */
public interface ISellerGroupRuleService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 添加分组规则条件
	 * @Date 2018年7月12日上午10:14:07
	 * @param ser
	 * @return
	 * @throws Exception
	 * @ReturnType Long
	 */
	public Long addSellerGroupRule(SellerGroupRule ser) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 修改分组规则条件
	 * @Date 2018年7月12日上午10:14:33
	 * @param ser
	 * @return
	 * @throws Exception
	 * @ReturnType int
	 */
	public int updateSellerGroupRule(SellerGroupRule ser) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据RuleId查询数据
	 * @Date 2018年7月12日上午10:14:51
	 * @param id
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroupRule
	 */
	public SellerGroupRule findSellerGroupRule(Long id) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据主键id删除会员分组设置的条件
	 * @Date 2018年7月12日上午10:15:12
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType int
	 */
	public int delSellerGroupRule(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据分组groupId查询组规则
	 * @Date 2018年7月12日上午10:15:34
	 * @param uid
	 * @param groupId
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroupRule
	 */
	public SellerGroupRule findSellerGroupRule(Long uid, Long groupId) throws Exception;

}
