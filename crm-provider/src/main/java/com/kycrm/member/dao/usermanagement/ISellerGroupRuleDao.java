package com.kycrm.member.dao.usermanagement;

import java.util.Map;

import com.kycrm.member.domain.entity.usermanagement.SellerGroupRule;


public interface ISellerGroupRuleDao {

	public int addSellerGroupRule(SellerGroupRule sellerGroupRule) throws Exception;

	public int updateSellerGroupRule(SellerGroupRule sellerGroupRule) throws Exception;

	public int delSellerGroupRule(Map<String, Object> paramMap) throws Exception;

	public SellerGroupRule findSellerGroupRule(Map<String, Object> map) throws Exception;

}
