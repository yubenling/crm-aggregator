package com.kycrm.member.dao.usermanagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.usermanagement.SellerGroup;
import com.kycrm.member.domain.to.MemberLevelSetting;

public interface ISellerGroupDao {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 分页查询会员分组
	 * @Date 2018年7月10日下午4:49:15
	 * @param map
	 * @return
	 * @throws Exception
	 * @ReturnType List<SellerGroup>
	 */
	public List<SellerGroup> findAllSellerGroup(Map<String, Object> map) throws Exception;

	public int findSellerGroupCount(Map<String, Object> map) throws Exception;

	public int deleteSellerGroup(Map<String, Object> paramMap) throws Exception;

	public void addSellerGroup(SellerGroup sel) throws Exception;

	public List<SellerGroup> findGroupIdLast(Object object) throws Exception;

	public int updateSellerGroup(SellerGroup sg) throws Exception;

	public SellerGroup findSellerGroup(Map<String, Object> map) throws Exception;

	public SellerGroup findSellerGroupInfo(Map<String, Object> map) throws Exception;

	public Integer findDefaultSellerGroupCount(Map<String, Object> map)throws Exception;
	
	public List<SellerGroup> findDefaultSellerGroup(Map<String, Object> map) throws Exception;

	public MemberLevelSetting findMemberLevelByLevelAndUserId(HashMap<String, Object> maps) throws Exception;

	public void updateSetting(Map<String, Object> mapParam) throws Exception;

	public List<SellerGroup> findAllGroup(Map<String, Object> map) throws Exception;

	public Integer existenceSellerGroupInfo(Map<String, Object> map) throws Exception;

	public List<SellerGroup> findSellerGroupListByUid(Map<String, Object> paramMap) throws Exception;

	public Integer existenceSellerGroupForUpdate(Map<String, Object> map) throws Exception;

}
