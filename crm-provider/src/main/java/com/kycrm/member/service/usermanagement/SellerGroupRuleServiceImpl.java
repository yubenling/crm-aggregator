package com.kycrm.member.service.usermanagement;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.usermanagement.ISellerGroupRuleDao;
import com.kycrm.member.domain.entity.usermanagement.SellerGroupRule;

/**
 * 用户分组规则接口实现
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月12日上午10:17:54
 * @Tags
 */
@Service("sellerGroupRuleService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class SellerGroupRuleServiceImpl implements ISellerGroupRuleService {

	@Autowired
	private ISellerGroupRuleDao sellerGroupRuleDao;

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
	@Override
	public Long addSellerGroupRule(SellerGroupRule sellerGroupRule) throws Exception {
		int date = sellerGroupRuleDao.addSellerGroupRule(sellerGroupRule);
		if (date != 0 && sellerGroupRule.getId() != null) {
			return sellerGroupRule.getId();
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 修改分组规则条件
	 * @Date 2018年7月12日上午10:14:33
	 * @param sellerGroupRule
	 * @return
	 * @throws Exception
	 * @ReturnType int
	 */
	@Override
	public int updateSellerGroupRule(SellerGroupRule sellerGroupRule) throws Exception {
		int date = sellerGroupRuleDao.updateSellerGroupRule(sellerGroupRule);
		return date;
	}

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
	@Override
	public SellerGroupRule findSellerGroupRule(Long id) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("id", id);
		SellerGroupRule sellerGroupRule = sellerGroupRuleDao.findSellerGroupRule(paramMap);
		return sellerGroupRule;
	}

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
	@Override
	public int delSellerGroupRule(Map<String, Object> paramMap) throws Exception {
		int status = sellerGroupRuleDao.delSellerGroupRule(paramMap);
		return status;
	}

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
	@Override
	public SellerGroupRule findSellerGroupRule(Long uid, Long groupId) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("uid", uid);
		paramMap.put("groupId", groupId);
		SellerGroupRule sellerGroupRule = sellerGroupRuleDao.findSellerGroupRule(paramMap);
		return sellerGroupRule;
	}

}
