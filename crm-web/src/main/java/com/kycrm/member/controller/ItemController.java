package com.kycrm.member.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.item.CommodityGrouping;
import com.kycrm.member.domain.entity.item.GroupedGoods;
import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.item.CommodityVO;
import com.kycrm.member.domain.vo.item.ItemVO;
import com.kycrm.member.service.item.ICommodityGroupingService;
import com.kycrm.member.service.item.IGroupedGoodsService;
import com.kycrm.member.service.item.IItemService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.GetCurrentPageUtil;
import com.kycrm.util.JsonUtil;

/** 
* @ClassName: ItemController 
* @Description:  1:商品CRUD<br/>
* 2:商品分组CRUD
* @author jackstraw_yu
* @date 2018年1月29日 下午5:06:35 
*/
@Controller
@RequestMapping(value="/item")
public class ItemController extends BaseController{
	
	@Autowired
	private IItemService itemService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private ICommodityGroupingService commodityGroupingService;
	
	@Autowired
	private IGroupedGoodsService groupdGoodsService;
	
	
	
	/**
	 * listItem(分页查询商品信息（商品缩写）)
	 * @Title: listItem 
	 * @param @param request
	 * @param @param response
	 * @param @param params uid,pageNo,numIid,title,groupId,minPrice,maxPrice
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/list")
	@ResponseBody
	public String listItem(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		ItemVO itemVO = null;
		try {
			itemVO = JsonUtil.paramsJsonToObject(params, ItemVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null || itemVO == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		if(itemVO.getPageNo() == null){
			itemVO.setPageNo(1);
		}
		itemVO.setUid(userInfo.getId());
		if(itemVO.getGroupId() != null){
			List<Long> numIids = groupdGoodsService.listGroupedNumIid(userInfo.getId(), itemVO.getGroupId());
			if(numIids != null && !numIids.isEmpty()){
				itemVO.setNumIids(numIids);
			}
		}
		List<Item> itemList = itemService.limitItemList(userInfo.getId(),itemVO);
		Integer totalCount = itemService.countItemByTitle(userInfo.getId(), itemVO);
		int totalPage = GetCurrentPageUtil.getTotalPage(totalCount, ConstantUtils.PAGE_SIZE_MIN);
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true).put("data", itemList);
		resultMap.put("pageNo", itemVO.getPageNo());
		resultMap.put("totalPage", totalPage);
		return resultMap.toJson();
	}
	
	/**
	 * editCommodityGroup(编辑商品分组，添加或更新)
	 * @Title: editCommodityGroup 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("/editGroup")
	public String editCommodityGroup(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		CommodityVO commodityVO = null;
		try {
			commodityVO = JsonUtil.paramsJsonToObject(params, CommodityVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || commodityVO == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		if(commodityVO.getGroupName() == null || "".equals(commodityVO.getGroupName())){
			return rsMap(101, "操作失败,分组名不能为空").put("status", false).toJson();
		}
		if(commodityVO.getNumIid() == null || commodityVO.getCommodityNum() != commodityVO.getNumIid().length){
			return rsMap(101, "操作失败,商品为空").put("status", false).toJson();
		}
		Long groupId = commodityGroupingService.groupNameIsExist(userInfo.getId(), commodityVO.getGroupName());
		try {
			if(commodityVO.getGroupId() != null){
				if(groupId != null && groupId != commodityVO.getGroupId()){
					return rsMap(101, "操作失败,商品分组名称重复").put("status", false).toJson();
				}
				//更新商品分组，并删除原本的分组下的已分组商品
				commodityGroupingService.updateItemGrouping(userInfo.getId(), commodityVO);
				//查询并保存新的已分组商品
				List<GroupedGoods> groupedGoods = this.createGroupedGoods(userInfo.getId(), commodityVO);
				if(groupedGoods != null && !groupedGoods.isEmpty()){
					groupdGoodsService.saveGroupedGoodsList(userInfo.getId(), groupedGoods);
					commodityVO.setCommodityNum(groupedGoods.size());
					commodityGroupingService.updateCommodityItemNum(userInfo.getId(), commodityVO);
				}
			}else {
				if(groupId != null){
					return rsMap(101, "操作失败,商品分组名称重复").put("status", false).toJson();
				}
				Long newGroupId = commodityGroupingService.saveCommodityGrouping(userInfo.getId(), commodityVO);
				commodityVO.setGroupId(newGroupId);
				List<GroupedGoods> groupedGoods = this.createGroupedGoods(userInfo.getId(), commodityVO);
				if(groupedGoods != null && !groupedGoods.isEmpty()){
					groupdGoodsService.saveGroupedGoodsList(userInfo.getId(), groupedGoods);
					commodityVO.setCommodityNum(groupedGoods.size());
					commodityGroupingService.updateCommodityItemNum(userInfo.getId(), commodityVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败！保存或更新异常").put("status", false).toJson();
		}
		return rsMap(100, "操作成功").put("status", true).toJson();
	}
	
	/**
	 * listAllItem(展示未分组商品和已分组商品)
	 * @Title: listAllItem 
	 * @param @param request
	 * @param @param response
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/showItem")
	@ResponseBody
	public String listAllItem(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		ItemVO itemVO = null;
		try {
			itemVO = JsonUtil.paramsJsonToObject(params, ItemVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || itemVO == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		if(itemVO.getPageNo() == null){
			itemVO.setPageNo(1);
		}
		itemVO.setUid(userInfo.getId());
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		if(itemVO.getNumIid() != null){
			resultMap.put("groupGoods", null);
		}/*else {
			if(itemVO.getGroupId() != null){
				List<GroupedGoods> groupedGoods = groupdGoodsService.listGroupedGoods(userInfo.getId(), itemVO.getGroupId());
				resultMap.put("groupGoods", groupedGoods);
			}else{
				resultMap.put("groupGoods", null);
			}
		}*/
		if(itemVO.getItemIds() != null && itemVO.getItemIds().length > 0){
			List<Long> numIids = new ArrayList<>();
			Long[] itemIds = itemVO.getItemIds();
			for (Long itemId : itemIds) {
				numIids.add(itemId);
			}
			List<Item> itemList = itemService.listItemByIds(userInfo.getId(), numIids);
			resultMap.put("groupGoods", itemList);
		}
		itemVO.setGroupId(null);
		List<Item> itemList = itemService.limitItemList(userInfo.getId(), itemVO);
		Integer itemCount = itemService.countItemByTitle(userInfo.getId(), itemVO);
		int totalPage = GetCurrentPageUtil.getTotalPage(itemCount, ConstantUtils.PAGE_SIZE_MIN);
		resultMap.put("data", itemList);
		resultMap.put("totalPage", totalPage);
		resultMap.put("pageNo", itemVO.getPageNo());
		return resultMap.toJson();
	}
	
	/**
	 * findGroupById(点击创建或修改商品分组，根据id查询分组)
	 * @Title: asdna 
	 * @param @param request
	 * @param @param response
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/findGroup")
	@ResponseBody
	public String findGroupById(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		ItemVO itemVO = null;
		try {
			itemVO = JsonUtil.paramsJsonToObject(params, ItemVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || itemVO == null || itemVO.getGroupId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		CommodityGrouping commodityGrouping = commodityGroupingService.findGroupById(userInfo.getId(), itemVO.getGroupId());
		List<Long> numIids = groupdGoodsService.listGroupedNumIid(userInfo.getId(), itemVO.getGroupId());
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("data", commodityGrouping);
		resultMap.put("numIid", numIids);
		return resultMap.toJson();
	}
	
	/**
	 * lisrCommodityGroup(查询用户所有的商品分组信息)
	 * @Title: lisrCommodityGroup 
	 * @param @param request
	 * @param @param response
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/limitGroup")
	@ResponseBody
	public String lisrCommodityGroup(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		ItemVO itemVO = null;
		try {
			itemVO = JsonUtil.paramsJsonToObject(params, ItemVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || itemVO == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		if(itemVO.getPageNo() == null){
			itemVO.setPageNo(1);
		}
		List<CommodityGrouping> groupList = commodityGroupingService.limitUidItemGroup(userInfo.getId(), itemVO.getPageNo());
		Integer totalCount = commodityGroupingService.countUidGroup(userInfo.getId());
		int totalPage = GetCurrentPageUtil.getTotalPage(totalCount, ConstantUtils.PAGE_SIZE_MIN);
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("data", groupList);
		resultMap.put("totalPage", totalPage);
		resultMap.put("pageNo", itemVO.getPageNo());
		return resultMap.toJson();
	}
	
	/**
	 * removeItemGroup(删除分组)
	 * @Title: removeItemGroup 
	 * @param @param request
	 * @param @param response
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/removeGroup")
	@ResponseBody
	public String removeItemGroup(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		ItemVO itemVO = null;
		try {
			itemVO = JsonUtil.paramsJsonToObject(params, ItemVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || itemVO == null || itemVO.getGroupId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		if(itemVO.getGroupId() == null){
			return rsMap(101, "操作失败,groupId为空").put("status", false).toJson();
		}
		Boolean isRemove = commodityGroupingService.removeGroupedAndGoods(userInfo.getId(), itemVO.getGroupId());
		ResultMap<String,Object> resultMap = null;
		if(isRemove){
			resultMap = rsMap(100, "删除成功").put("status", true);
		}else {
			resultMap = rsMap(101, "删除失败").put("status", false);
		}
		return resultMap.toJson();
	}
	
	/**
	 * 设置页添加短连接 商品展示
	 * @param request
	 * @param params
	 * @return
	 * 
	 */
	@RequestMapping(value="/setupItemShow",produces="text/html;charset=UTF-8",method = RequestMethod.POST)	
	@ResponseBody
	public String setupItemShow(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){
		ItemVO itemVo = new ItemVO();
		try {
			JSONObject parseObject = JSON.parseObject(params);
			String param = parseObject.getString("params");
			itemVo = JsonUtil.fromJson(param,ItemVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(itemVo == null){
			return failureReusltMap(ApiResult.PARAM_LACK).toJson();
		}
		if(userInfo == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		if(itemVo.getPageNo() == null){itemVo.setPageNo(1);}
		itemVo.setUid(userInfo.getId());
		Map<String, Object> map=new HashMap<String, Object>();
		if(itemVo.getGroupId() != null){
			List<Long> numIids = groupdGoodsService.listGroupedNumIid(userInfo.getId(), itemVo.getGroupId());
			if(numIids != null && !numIids.isEmpty()){
				itemVo.setNumIids(numIids);
			}
		}
		List<Item> itemList = itemService.limitItemList(userInfo.getId(),itemVo);
		Integer itemCount = itemService.countItemByTitle(userInfo.getId(),itemVo);
		int totalPage = GetCurrentPageUtil.getTotalPage(itemCount, ConstantUtils.PAGE_SIZE_MIN);
		map.put("data", itemList);
		map.put("totalPage", totalPage);
		map.put("pageNo", itemVo.getPageNo());
		return successReusltMap(null).put(ApiResult.API_RESULT, map) .toJson();
	}
	/**
	 * 商品设置页查询分组名称下拉框
	 * @param request
	 * @param params
	 * @return
	 */
	@RequestMapping(value="/findGroupName",produces="text/html;charset=UTF-8",method = RequestMethod.POST)	
	@ResponseBody
	public String findGroupName(HttpServletRequest request,HttpServletResponse response){
		UserInfo user=sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(user==null){
			return failureReusltMap(ApiResult.PARAM_LACK).toJson();
		}
		List<CommodityGrouping> commNameList = commodityGroupingService.findCommName(user.getId());
	    return successReusltMap(null).put(ApiResult.API_RESULT,commNameList).toJson();
	}
	/**
	 * 设置页所需要的商品展示数据
	 * @param request
	 * @param params pageNo
	 * @return
	 * approveStatusStr:""itemIds:nullmaxPrice:""minPrice:""numIid:""pageNo:1title:""groupId:""
	 */
	@RequestMapping(value="/setupCommodity",produces="text/html;charset=UTF-8",method = RequestMethod.POST)	
	@ResponseBody
	public String setupCommodity(HttpServletRequest request,@RequestBody String params,HttpServletResponse response){
		ItemVO  itemVo= null;
		try {
			JSONObject parseObject = JSON.parseObject(params);
			String param = parseObject.getString("params");
			itemVo = JsonUtil.fromJson(param,ItemVO.class);
		} catch (Exception e) {
			return  rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if(itemVo ==null) return  failureReusltMap(ApiResult.PARAM_LACK).toJson();
		if(itemVo.getPageNo() == null){itemVo.setPageNo(1);}
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(user == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		itemVo.setUid(user.getId());
		Map<String,Object> map=new HashMap<String, Object>();
		if(itemVo.getGroupId() != null){
			List<Long> numIids = groupdGoodsService.listGroupedNumIid(user.getId(), itemVo.getGroupId());
			if(numIids != null && !numIids.isEmpty()){
				itemVo.setNumIids(numIids);
			}
		}
		//根据条件查询出左侧数据
		List<Item> limitItemList = itemService.limitItemList(user.getId(),itemVo);
		Integer itemCount = itemService.countItemByTitle(user.getId(), itemVo);
		int totalPage = GetCurrentPageUtil.getTotalPage(itemCount, ConstantUtils.PAGE_SIZE_MIN);
		map.put("data", limitItemList);
		map.put("totalPage", totalPage);
		map.put("pageNo", itemVo.getPageNo());
		//当itemIds不为空的情况下查询出右侧展示的数据
		if(itemVo.getItemIds()!=null&&itemVo.getItemIds().length>0){
			List<Long> numIids = new ArrayList<>();
			for (Long itemId : itemVo.getItemIds()) {
				numIids.add(itemId);
			}
			//1查询已分组的商品数据
			List<Item> itemData = itemService.listItemByIds(user.getId(), numIids);
			if(itemData!=null){
				map.put("groupGoods", itemData);
			}
		}
		return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();
	}
	
	/**
	 * limitItem(营销中心选择商品)
	 * @Title: limitItem 
	 * @param @param request
	 * @param @param response
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/limitItem")
	@ResponseBody
	public String limitItem(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		ItemVO itemVO = null;
		try {
			itemVO = JsonUtil.paramsJsonToObject(params, ItemVO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || itemVO == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		if(itemVO.getPageNo() == null){
			itemVO.setPageNo(1);
		}
		if(itemVO.getGroupId() != null){
			List<Long> numIids = groupdGoodsService.listGroupedNumIid(userInfo.getId(), itemVO.getGroupId());
			if(numIids != null && !numIids.isEmpty()){
				itemVO.setNumIids(numIids);
			}
		}
		List<Item> itemList = itemService.limitItemList(userInfo.getId(), itemVO);
		Integer itemCount = itemService.countItemByTitle(userInfo.getId(), itemVO);
		int totalPage = GetCurrentPageUtil.getTotalPage(itemCount, ConstantUtils.PAGE_SIZE_MIN);
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("data", itemList);
		resultMap.put("pageNo", itemVO.getPageNo());
		resultMap.put("totalPage", totalPage);
		return resultMap.toJson();
	}
	
	public List<GroupedGoods> createGroupedGoods(Long uid, CommodityVO commodityVO){
		List<Item> items = new ArrayList<>();
		if(commodityVO != null && commodityVO.getIsTitle() != null && "1".equals(commodityVO.getIsTitle())){
			items = itemService.listAllItemByTitle(uid, commodityVO.getTitle());
		}else {
			if(commodityVO != null && commodityVO.getNumIid() != null){
				String[] numIids = commodityVO.getNumIid();
				List<Long> numIdList = new ArrayList<Long>();
				for (String numIdStr : numIids) {
					if(numIdStr != null && !"".equals(numIdStr)){
						numIdList.add(Long.parseLong(numIdStr));
					}
				}
				items = itemService.listItemByIds(uid, numIdList);
			}
		}
		if(items != null && !items.isEmpty()){
			List<GroupedGoods> resultList = new ArrayList<>();
			for (Item item : items) {
				GroupedGoods groupedGoods = new GroupedGoods();
				groupedGoods.setUid(uid);
				groupedGoods.setNumIid(item.getNumIid());
				groupedGoods.setTitle(item.getTitle());
				groupedGoods.setApproveStatus(item.getApproveStatus());
				groupedGoods.setPrice(item.getPrice());
				groupedGoods.setUrl(item.getUrl());
				groupedGoods.setGroupId(commodityVO.getGroupId());
				resultList.add(groupedGoods);
			}
			return resultList;
		}
		return null;
	}
	
}
