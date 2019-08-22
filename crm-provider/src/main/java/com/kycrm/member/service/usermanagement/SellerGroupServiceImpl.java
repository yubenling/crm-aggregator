package com.kycrm.member.service.usermanagement;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.usermanagement.ISellerGroupDao;
import com.kycrm.member.domain.entity.usermanagement.SellerGroup;
import com.kycrm.member.domain.entity.usermanagement.SellerGroupRule;
import com.kycrm.member.domain.utils.pagination.Pagination;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.util.GenerateQueryKey;
import com.kycrm.util.AssembleFilterConditionUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.RedisConstant;

@Service("sellerGroupService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class SellerGroupServiceImpl implements ISellerGroupService {

	// 用户分组规则服务
	@Autowired
	private ISellerGroupRuleService sellerGroupRuleService;

	@Autowired
	private ISellerGroupDao sellerGroupDao;

	@Autowired
	private ICacheService cacheService;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 分页查询会员分组
	 * @Date 2018年7月10日下午4:16:42
	 * @param uid
	 * @param contextPath
	 * @param pageNo
	 * @param groupType
	 * @return
	 * @throws Exception
	 * @ReturnType Pagination
	 */
	@Override
	public Pagination findAllSellerGroup(String contextPath, Integer pageNo, Long uid, String groupType)
			throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(4);
		paramMap.put("uid", uid);
		paramMap.put("groupType", groupType);
		// 先设置每页显示的条数为10条
		Integer currentRows = 10;
		// 计算出起始行数
		Integer startRows = (pageNo - 1) * currentRows;
		// 计算出总页数
		int count = findSellerGroupCount(paramMap);
		paramMap.put("startRows", startRows);
		paramMap.put("currentRows", currentRows);
		List<SellerGroup> sellerGroupList = sellerGroupDao.findAllSellerGroup(paramMap);
		SellerGroup sellerGroup = null;
		SellerGroupRule sellerGroupRule = null;
		MemberFilterVO memberFilterVO = null;
		for (int i = 0; i < sellerGroupList.size(); i++) {
			sellerGroup = sellerGroupList.get(i);
			// 根据会员分组ID查询会员分组规则
			sellerGroupRule = this.sellerGroupRuleService.findSellerGroupRule(uid, sellerGroup.getId());
			if (sellerGroupRule != null) {
				// 组装会员筛选条件
				memberFilterVO = AssembleFilterConditionUtil.assembleMemberFilterCondition(uid, sellerGroupRule);
				// 获取查询key值
				String queryKey = GenerateQueryKey.getKey();
				this.cacheService.putString(
						RedisConstant.RediskeyCacheGroup.BASE_MEMBER_FILTER + "-" + queryKey + "-" + uid,
						JsonUtil.toJson(memberFilterVO));
				sellerGroup.setQueryKey(queryKey);
				sellerGroupList.set(i, sellerGroup);
			}
		}
		Pagination pagination = new Pagination(pageNo, currentRows, count, sellerGroupList);
		StringBuilder requestParams = new StringBuilder();
		if (groupType != null) {
			requestParams.append("&memberType=").append(groupType);
		}
		// 拼接分页的后角标中的跳转路径与查询的条件
		String url = contextPath + "/memberGroup/findMemberGroup";
		pagination.pageView(url, requestParams.toString());
		return pagination;
	}

	@Override
	public int findSellerGroupCount(Map<String, Object> map) throws Exception {
		int count = sellerGroupDao.findSellerGroupCount(map);
		return count;
	}

	@Override
	public int deleteSellerGroup(Map<String, Object> paramMap) throws Exception {
		int date = sellerGroupDao.deleteSellerGroup(paramMap);
		return date;
	}

	@Override
	public Long addSellerGroup(SellerGroup selllerGroup) throws Exception {
		sellerGroupDao.addSellerGroup(selllerGroup);
		if (selllerGroup.getId() != null) {
			return selllerGroup.getId();
		} else {
			List<SellerGroup> findList = sellerGroupDao.findGroupIdLast(null);
			if (findList != null && findList.get(0).getId() != null) {
				return findList.get(0).getId();
			} else {
				return null;
			}
		}
	}

	@Override
	public int updateSellerGroup(SellerGroup sellerGroup) throws Exception {
		int date = sellerGroupDao.updateSellerGroup(sellerGroup);
		return date;
	}

	@Override
	public SellerGroup findSellerGroup(Long uid, String id) throws Exception {
		Long groupId = Long.parseLong(id);
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("uid", uid);
		map.put("groupId", groupId);
		SellerGroup sg = sellerGroupDao.findSellerGroup(map);
		return sg;
	}

	@Override
	public SellerGroup findSellerGroupInfo(Long groupId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("groupId", groupId);
		SellerGroup sg = sellerGroupDao.findSellerGroupInfo(map);
		return sg;
	}

	@Override
	public List<SellerGroup> findSellerGroupListByUid(Long uid) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("uid", uid);
		return this.sellerGroupDao.findSellerGroupListByUid(paramMap);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询所有默认分组信息
	 * @Date 2018年7月10日下午4:23:15
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType List<SellerGroup>
	 */
	@Override
	public List<SellerGroup> findDefaultSellerGroup(Long uid, String taoBaoUserNick) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("uid", uid);
		// 查询该用户默认分组数量
		Integer defaultSellerGroupCount = this.sellerGroupDao.findDefaultSellerGroupCount(map);
		if (defaultSellerGroupCount == 0) {
			this.addDefaultGroup(uid, taoBaoUserNick);
		}
		List<SellerGroup> list = this.sellerGroupDao.findDefaultSellerGroup(map);
		return list;
	}

	/**
	 * 增加默认分组
	 */
	@Override
	public void addDefaultGroup(Long uid, String taoBaoUserNick) throws Exception {
		// 添加默认分组【购买一次客户】
		this.addSellerGroup(this.addBuyingOneTimeCustomer(uid, taoBaoUserNick));
		// 添加默认分组【购买两次及两次以上客户】
		this.addSellerGroup(this.addBuyingGreaterThanTwoTimes(uid, taoBaoUserNick));
		// 添加默认分组【意向客户】
		this.addSellerGroup(this.addIntentionCustomer(uid, taoBaoUserNick));
		// 添加默认分组【近三个月未购买客户】
		this.addSellerGroup(this.addLastThreeMonthsUntradedCustomer(uid, taoBaoUserNick));
		// 添加默认分组【全部客户】
		this.addSellerGroup(this.addAllCustomer(uid, taoBaoUserNick));
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 增加默认分组【购买一次客户】
	 * @Date 2018年8月15日下午4:14:31
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroup
	 */
	private SellerGroup addBuyingOneTimeCustomer(Long uid, String taoBaoUserNick) throws Exception {
		SellerGroup sellerGroup = new SellerGroup();
		sellerGroup.setGroupName("购买一次客户");
		sellerGroup.setUserId(taoBaoUserNick);
		sellerGroup.setStatus("1");
		sellerGroup.setUid(uid);
		sellerGroup.setGroupCreate(new Date());
		// sellerGroup.setMemberCount(this.marketingMemberFilterService.findBuyingOneTimeCustomer(uid));
		sellerGroup.setGroupType("1");
		sellerGroup.setLastModifiedDate(new Date());
		sellerGroup.setRemark("店铺内成交一次客户");
		return sellerGroup;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 增加默认分组【购买两次及以上客户】
	 * @Date 2018年8月15日下午4:10:37
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroup
	 */
	private SellerGroup addBuyingGreaterThanTwoTimes(Long uid, String taoBaoUserNick) throws Exception {
		SellerGroup sellerGroup = new SellerGroup();
		sellerGroup.setGroupName("购买两次及以上客户");
		sellerGroup.setUserId(taoBaoUserNick);
		sellerGroup.setStatus("1");
		sellerGroup.setUid(uid);
		sellerGroup.setGroupCreate(new Date());
		// sellerGroup.setMemberCount(this.marketingMemberFilterService.findBuyingGreaterThanTwoTimes(uid));
		sellerGroup.setGroupType("1");
		sellerGroup.setLastModifiedDate(new Date());
		sellerGroup.setRemark("店铺内成交两次及两次以上客户");
		return sellerGroup;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 增加默认分组【意向客户】
	 * @Date 2018年8月15日下午4:10:37
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroup
	 */
	private SellerGroup addIntentionCustomer(Long uid, String taoBaoUserNick) throws Exception {
		SellerGroup sellerGroup = new SellerGroup();
		sellerGroup.setGroupName("意向客户");
		sellerGroup.setUserId(taoBaoUserNick);
		sellerGroup.setStatus("1");
		sellerGroup.setUid(uid);
		sellerGroup.setGroupCreate(new Date());
		// sellerGroup.setMemberCount(this.marketingMemberFilterService.findIntentionCustomer(uid));
		sellerGroup.setGroupType("1");
		sellerGroup.setLastModifiedDate(new Date());
		sellerGroup.setRemark("店铺内下单未付款客户");
		return sellerGroup;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 增加默认分组【近三个月未购买客户】
	 * @Date 2018年8月15日下午4:10:37
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroup
	 */
	private SellerGroup addLastThreeMonthsUntradedCustomer(Long uid, String taoBaoUserNick) throws Exception {
		SellerGroup sellerGroup = new SellerGroup();
		sellerGroup.setGroupName("近三个月未购买客户");
		sellerGroup.setUserId(taoBaoUserNick);
		sellerGroup.setStatus("1");
		sellerGroup.setUid(uid);
		sellerGroup.setGroupCreate(new Date());
		// sellerGroup.setMemberCount(this.marketingMemberFilterService.findLastThreeMonthsUntradedCustomer(uid));
		sellerGroup.setGroupType("1");
		sellerGroup.setLastModifiedDate(new Date());
		sellerGroup.setRemark("店铺内近3个月未消费客户");
		return sellerGroup;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 增加默认分组【全部客户】
	 * @Date 2018年8月15日下午4:14:02
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroup
	 */
	private SellerGroup addAllCustomer(Long uid, String taoBaoUserNick) throws Exception {
		SellerGroup sellerGroup = new SellerGroup();
		sellerGroup.setGroupName("全部客户");
		sellerGroup.setUserId(taoBaoUserNick);
		sellerGroup.setStatus("1");
		sellerGroup.setUid(uid);
		sellerGroup.setGroupCreate(new Date());
		// sellerGroup.setMemberCount(this.marketingMemberFilterService.findAllCustomer(uid));
		sellerGroup.setGroupType("1");
		sellerGroup.setLastModifiedDate(new Date());
		sellerGroup.setRemark("店铺所有客户");
		return sellerGroup;
	}

	/**
	 * 获取所有客户
	 */
	@Override
	public List<SellerGroup> getAllSellerGroup(String userId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("userId", userId);
		List<SellerGroup> list = sellerGroupDao.findAllGroup(map);
		return list;
	}

	/**
	 * 查询用户分组是否存在
	 */
	@Override
	public Integer existenceSellerGroupInfo(Long uid, String groupName) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("uid", uid);
		map.put("groupName", groupName);
		Integer count = sellerGroupDao.existenceSellerGroupInfo(map);
		return count;
	}

	@Override
	public Integer existenceSellerGroupForUpdate(Long uid, Long groupId, String groupName) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("uid", uid);
		map.put("groupId", groupId);
		map.put("groupName", groupName);
		Integer count = sellerGroupDao.existenceSellerGroupForUpdate(map);
		return count;
	}

}
