package com.kycrm.member.service.usermanagement;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.usermanagement.SellerGroup;
import com.kycrm.member.domain.utils.pagination.Pagination;

/**
 * 会员分组服务接口
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月10日下午2:18:56
 * @Tags
 */
public interface ISellerGroupService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 分页查询会员分组
	 * @Date 2018年7月10日下午4:16:42
	 * @param contextPath
	 * @param pageNo
	 * @param groupType
	 * @return
	 * @throws Exception
	 * @ReturnType Pagination
	 */
	public Pagination findAllSellerGroup(String contextPath, Integer pageNo, Long uid, String groupType)
			throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据卖家编号查询所有分组数量
	 * @Date 2018年7月10日下午4:19:46
	 * @param map
	 * @return
	 * @throws Exception
	 * @ReturnType int
	 */
	public int findSellerGroupCount(Map<String, Object> map) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据分组编号删除会员分组
	 * @Date 2018年7月10日下午4:20:37
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType int
	 */
	public int deleteSellerGroup(Map<String, Object> paramMap) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 添加卖家设置的分组
	 * @Date 2018年7月10日下午4:21:04
	 * @param sel
	 * @return
	 * @throws Exception
	 * @ReturnType Long
	 */
	public Long addSellerGroup(SellerGroup sel) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据分组编号修改数据
	 * @Date 2018年7月10日下午4:21:57
	 * @param sg
	 * @return
	 * @throws Exception
	 * @ReturnType int
	 */
	public int updateSellerGroup(SellerGroup sg) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据分组编号查询数据
	 * @Date 2018年7月10日下午4:22:21
	 * @param id
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroup
	 */
	public SellerGroup findSellerGroup(Long uid, String id) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据分组编号查询分组数据
	 * @Date 2018年7月10日下午4:22:50
	 * @param Id
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroup
	 */
	public SellerGroup findSellerGroupInfo(Long Id) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据UID查询该商家所有分组
	 * @Date 2018年7月31日下午5:34:36
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType List<SellerGroup>
	 */
	public List<SellerGroup> findSellerGroupListByUid(Long uid) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询所有默认分组信息
	 * @Date 2018年7月10日下午4:23:15
	 * @param uid
	 * @param taoBaoUserNick
	 * @return
	 * @throws Exception
	 * @ReturnType List<SellerGroup>
	 */
	public List<SellerGroup> findDefaultSellerGroup(Long uid, String taoBaoUserNick) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 添加用户默认分组
	 * @Date 2018年7月10日下午4:23:50
	 * @param userId
	 * @throws Exception
	 * @ReturnType void
	 */
	public void addDefaultGroup(Long uid, String taoBaoUserNick) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据卖家编号查询所有分组（id,groupId,groupName）
	 * @Date 2018年7月10日下午4:24:15
	 * @param userId
	 * @return
	 * @ReturnType List<SellerGroup>
	 */
	public List<SellerGroup> getAllSellerGroup(String userId) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据用户昵称和会员分组名查询分组信息
	 * @Date 2018年7月10日下午4:24:37
	 * @param uid
	 * @param groupName
	 * @return
	 * @ReturnType List<SellerGroup>
	 */
	public Integer existenceSellerGroupInfo(Long uid, String groupName) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 根据用户昵称和会员分组名查询分组信息
	 * @Date 2018年7月10日下午4:24:37
	 * @param uid
	 * @param groupName
	 * @return
	 * @ReturnType List<SellerGroup>
	 */
	public Integer existenceSellerGroupForUpdate(Long uid, Long groupId, String groupName) throws Exception;

}
