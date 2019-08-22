package com.kycrm.syn.service.item;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.member.domain.entity.item.TbItem;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.syn.core.mybatis.MyDataSource;
import com.kycrm.syn.core.mybatis.MyDataSourceAspect;
import com.kycrm.syn.dao.item.ItemDao;
import com.kycrm.syn.dao.item.example.ItemExample;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.ApiException;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.response.ItemAddResponse;

@Service
// @Transactional
@MyDataSource
public class ItemServiceImpl /*implements IItemService*/ {
	private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
	@Autowired
	private ItemDao itemDao;

	@Resource(name = "userInfoServiceDubbo")
	private IUserInfoService userInfoService;

	
   /* 
	*//**
	 * 从TbItem获取Item
	 * 
	 * @param TbItem
	 * @return Item
	 *//*
	public Item getItemDTO(TbItem tbItem) {
		String jdpresponse = tbItem.getJdpResponse();
		ItemAddResponse rsp = null;
		try {
			rsp = TaobaoUtils.parseResponse(jdpresponse, ItemAddResponse.class);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		Item item = new Item();
		if (rsp.getItem() == null)
			return null;

		item.setNumIid(rsp.getItem().getNumIid());
		item.setUserName(rsp.getItem().getNick());
		item.setApproveStatus(rsp.getItem().getApproveStatus());
		if (rsp.getItem().getCid() != null) {
			item.setCid(rsp.getItem().getCid().toString());
		}
		if (rsp.getItem().getHasShowcase() != null) {
			item.setHasShowcase(rsp.getItem().getHasShowcase().toString());
		}
		if (rsp.getItem().getHasDiscount() != null) {
			item.setHasDiscount(rsp.getItem().getHasDiscount().toString());
		}
		item.setCreated(rsp.getItem().getCreated());
		item.setModified(rsp.getItem().getModified());
		item.setSellerCids(rsp.getItem().getSellerCids());
		item.setProps(rsp.getItem().getProps());
		item.setInputPids(rsp.getItem().getInputPids());
		item.setInputStr(rsp.getItem().getInputStr());
		if (rsp.getItem().getNum() != null) {
			String num = rsp.getItem().getNum().toString();
			int nums = Integer.parseInt(num);
			item.setNum(nums);
		}
		item.setListTime(rsp.getItem().getListTime());
		item.setDelistTime(rsp.getItem().getDelistTime());
		item.setStuffStatus(rsp.getItem().getStuffStatus());
		if (rsp.getItem().getLocation() != null) {
			item.setZip(rsp.getItem().getLocation().getZip());
			item.setAddress(rsp.getItem().getLocation().getAddress());
			item.setCity(rsp.getItem().getLocation().getCity());
			item.setState(rsp.getItem().getLocation().getState());
			item.setCountry(rsp.getItem().getLocation().getCountry());
			item.setDistrict(rsp.getItem().getLocation().getDistrict());
		}
		item.setFreightPayer(rsp.getItem().getFreightPayer());
		item.setIs3D(rsp.getItem().getIs3D());
		if (rsp.getItem().getScore() != null) {
			String score = rsp.getItem().getScore().toString();
			int scores = Integer.parseInt(score);
			item.setScore(scores);
		}
		item.setSellPromise(rsp.getItem().getSellPromise());
		item.setTitle(rsp.getItem().getTitle().toString());
		item.setType(rsp.getItem().getType());
		item.setItemDesc(rsp.getItem().getDesc());
		item.setPropsName(rsp.getItem().getPropsName());
		item.setPromotedService(rsp.getItem().getPromotedService());
		item.setIsLightningConsignment(rsp.getItem().getIsLightningConsignment());
		if (rsp.getItem().getIsFenxiao() != null) {
			String isFenxiao = rsp.getItem().getIsFenxiao().toString();
			int isFenXiaos = Integer.parseInt(isFenxiao);
			item.setIsFenxiao(isFenXiaos);
		}
		item.setTemplateId(rsp.getItem().getTemplateId());
		if (rsp.getItem().getAuctionPoint() != null) {
			String auctionPoint = rsp.getItem().getAuctionPoint().toString();
			int auctionPoints = Integer.parseInt(auctionPoint);
			item.setAuctionPoint(auctionPoints);
		}
		item.setPropertyAlias(rsp.getItem().getPropertyAlias());
		item.setAfterSaleId(rsp.getItem().getAfterSaleId());
		item.setIsXinpin(rsp.getItem().getIsXinpin());
		if (rsp.getItem().getSubStock() != null) {
			String subStock = rsp.getItem().getSubStock().toString();
			int subStocks = Integer.parseInt(subStock);
			item.setSubStock(subStocks);
		}
		item.setFeatures(rsp.getItem().getFeatures());
		if (rsp.getItem().getWithHoldQuantity() != null) {
			String withHoldQuantity = rsp.getItem().getWithHoldQuantity().toString();
			int withHoldQuantitys = Integer.parseInt(withHoldQuantity);
			item.setSkuWithHoldQuantity(withHoldQuantitys);
		}
		item.setSellPoint(rsp.getItem().getSellPoint());
		if (rsp.getItem().getValidThru() != null) {
			String validThru = rsp.getItem().getValidThru().toString();
			int validThrus = Integer.parseInt(validThru);
			item.setValidThru(validThrus);
		}
		item.setOuterId(rsp.getItem().getOuterId());
		item.setDescModules(rsp.getItem().getDescModules());
		item.setCustomMadeTypeId(rsp.getItem().getCustomMadeTypeId());
		item.setWirelessDesc(rsp.getItem().getWirelessDesc());
		item.setBarcode(rsp.getItem().getBarcode());
		item.setNewprepay(rsp.getItem().getNewprepay());
		item.setPrice(rsp.getItem().getPrice());
		item.setPostFee(rsp.getItem().getPostFee());
		item.setEmsFee(rsp.getItem().getEmsFee());
		item.setGlobalStockType(rsp.getItem().getGlobalStockType());
		item.setGlobalStockCountry(rsp.getItem().getGlobalStockCountry());
		item.setLargeScreenImageUrl(rsp.getItem().getLargeScreenImageUrl());
		if (rsp.getItem().getItemImgs() != null) {
			item.setUrl(rsp.getItem().getItemImgs().get(0).getUrl());
		}
		item.setLastModifiedDate(new Date());
		Long uid = userInfoService.findUidBySellerNick(rsp.getItem().getNick());
		if (uid != null)
			item.setUid(uid);
		return item;
	}
*/
	/**
	 * 批量保存或更新商品信息
	 * 
	 * @param itemlist
	 */
	public void saveOrUpItem(Long uid,List<Item> itemlist) {
		if (ValidateUtil.isEmpty(itemlist)){
			return;	
		}
		try {
			List<Item> updateItems = new ArrayList<>();
			List<Item> insertItems = new ArrayList<>();
			for (Item item : itemlist) {
				if (isExitItem(uid,item) == -1l) {
					insertItems.add(item);
				} else {
					updateItems.add(item);
				}
			}
			// 批量保存
			if (!ValidateUtil.isEmpty(insertItems)) {
				logger.info("用户"+uid+"插入数据"+insertItems.size());
				this.batchSaveItem(uid,insertItems);
			}
			// 批量更新
			if (!ValidateUtil.isEmpty(updateItems)) {
				logger.info("用户"+uid+"更新数据"+updateItems.size());
				this.batchUpdateItem(uid,updateItems);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Item 保存或更新出错");
		}

	}
    
	/**
	 * 批量保存商品信息
	 * 
	 * @param itemlist
	 */
	public void batchSaveItem(Long uid,List<Item> itemlist) {
		if (ValidateUtil.isEmpty(itemlist))
			return;
		try {
			Long start = System.currentTimeMillis();
			if (ValidateUtil.isEmpty(itemlist))
				return;
			itemlist.stream().forEach(item -> this.saveItem(uid,item));
			logger.info("item批量保存了{}条数据,耗时：{}ms", itemlist.size(), System.currentTimeMillis() - start);
		} catch (Exception e) {
			logger.error("item 批量更新失败");
			e.printStackTrace();
		}
	}

	public void saveItem(Long uid,Item item) {
		try {
			item.setId(null);
			item.setCreatedDate(new Date());
			item.setCreatedBy("sync");
			item.setLastModifiedDate(new Date());
			itemDao.insertSelective(item);
		} catch (Exception e) {
			logger.info("保存商品出错");
			e.printStackTrace();
		}
	}

	/**
	 * 批量更新商品
	 * 
	 * @param updateItemList
	 */
	public void batchUpdateItem(Long uid,List<Item> updateItemList) {
		try {
			Long start = System.currentTimeMillis();
			if (ValidateUtil.isEmpty(updateItemList))
				return;
			updateItemList.stream()
				.forEach(item -> this.updateItem(uid,item));
			logger.info("item批量更新了{}条数据 耗时：{}ms", updateItemList.size(), System.currentTimeMillis() - start);
		} catch (Exception e) {
			logger.error("item 批量更新失败");
			e.printStackTrace();
		}
	}

	public void updateItem(Long uid,Item item) {
		try {
			/*ItemExample example = new ItemExample();
			ItemExample.Criteria criteria = example.createCriteria();
			criteria.andNumIidEqualTo(item.getNumIid());*/
			item.setLastModifiedDate(new Date());
			item.setLastModifiedBy("sync");
			itemDao.updateItem(item);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 查询numIid 是否存在
	public Long isExitItem(Long uid,Item item) {
		if (uid == null||item.getNumIid()==null) {
			return -1l;
		}
		/*ItemExample example = new ItemExample();
		ItemExample.Criteria criteria = example.createCriteria();
		criteria.andNumIidEqualTo(numIid);
		criteria.andUidEqualTo(uid);*/
		
		List<Item> list = itemDao.ItemIsExit(item);
		if (list != null && list.size() != 0) {
			return list.get(0).getNumIid();
		}
		return -1l;
	}






	// ===============================================商品全局跑找丢失数据========================================================
	/**
	 * 全局定时任务找回丢失数据 每次找回，处理 1万条数据 定时执行时间（每10分钟一次）
	 */
	// public void overallSituationItemThread() {
	// // 查询出节点执行数据行
	// long taskCount = myBatisDao.findBy(Item.class.getName(), "findTaskCount",
	// null);
	// HashMap<String, Object> hashMap = new HashMap<String, Object>();
	// hashMap.put("pageSize", 200);
	// List<TbItem> tbItem = null;
	// ExecutorService threadPool =
	// OverallSituationItemThreadPool.quanItemThreadPool();
	// int start = 0;
	// List<OverallSituationItemThread> quanitemThreadList = new
	// ArrayList<OverallSituationItemThread>();
	// while (start < 50) {
	// System.out.println("********************商品更新" + (start + 1) +
	// "次处理数据******************");
	// hashMap.put("taskCount", taskCount);
	// tbItem = myBatisDaoT.findList(TbItem.class.getName(), "findTbItemDValue",
	// hashMap);
	// if (null == tbItem || tbItem.size() == 0)
	// break;
	// OverallSituationItemThread quanItemThread = null;
	// if (Math.floor(start * 1.0 / 20) == 0) {
	// quanItemThread = new OverallSituationItemThread();
	// } else {
	// quanItemThread = quanitemThreadList.get(start % 20);
	// }
	// quanItemThread.appendtbItemList(tbItem);
	// quanitemThreadList.add(quanItemThread);
	// start++;
	// taskCount += 200;
	// }
	// for (OverallSituationItemThread i : quanitemThreadList) {
	// threadPool.execute(i);
	// }
	// // 将节点保存到数据库
	// hashMap.put("taskCount", taskCount);
	// hashMap.put("endTime", new Date());
	// try {
	// myBatisDao.execute(Item.class.getName(), "updateTaskCount", hashMap);
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }

	// ===============================================商品全局跑找丢失数据========================================================

	/**
	 * 创建人：邱洋 @Title: getOnsaleNumber @Description:
	 * TODO(查询正在出售中的商品数据) @param @param userId @param @return 设定文件 @return int
	 * 返回类型 @throws
	 */
	// public int getOnsaleNumber(String userId){
	// return myBatisDao.execute(TbItem.class.getName(), "getItemOnsaleNumber",
	// userId);
	// }

	/**
	 * Gg 创建分组 查询商品信息
	 * 
	 * @param userId
	 * @param numIidList
	 * @return Gg
	 */
	// public Item findItem(String userId,String numIid){
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("nick", userId);
	// map.put("numIid", numIid);
	// Item item = myBatisDao.findBy(Item.class.getName(), "findGroupItem",
	// map);
	// return item;
	// }

	/**
	 * Gg 商品分组，查询商品总数
	 * 
	 * @param userId
	 * @return Gg
	 */
	// public Long findtotalCount(CommodityArtifact artifact){
	// Map<String,Object> map = new HashMap<String, Object>();
	// map.put("nick", artifact.getUserId());
	// if(artifact.getQueryNumIid()!=null){
	// map.put("numIid", artifact.getQueryNumIid());
	// }
	// if(artifact.getTitle()!=null && !"".equals(artifact.getTitle())){
	// map.put("title", artifact.getTitle());
	// }
	// if(artifact.getQueryIshow()!=null &&
	// !"".equals(artifact.getQueryIshow())){
	// map.put("approveStatus", artifact.getQueryIshow());
	// }
	// Long totalCount = myBatisDao.findBy(Item.class.getName(),
	// "findItemCount",
	// map);
	// return totalCount;
	// }

	/**
	 * 查询商品分组左侧数据 分页：
	 * 
	 */
	// public Map<String,Object> findItemPagination(CommodityArtifact artifact){
	// //1,根据条件查询出总条数
	// Long totalCount = findtotalCount(artifact);
	// /***
	// * 计算出起始行数
	// */
	// //设置每页显示5条数据
	// Integer currentRows =5;
	// //计算出起始行数
	// Integer startRows =
	// (artifact.getPageNo()==null?1:artifact.getPageNo()-1)*currentRows;
	// //计算出总页数
	// Integer totalPage = (int) Math.ceil(1.0*totalCount/currentRows);
	// //Long totalPage = Long.valueOf((int)
	// Math.ceil(1.0*totalCount/currentRows));
	// Map<String,Object> map = new HashMap<String, Object>();
	// map.put("nick", artifact.getUserId());
	// map.put("startRows",startRows);
	// map.put("currentRows", currentRows);
	// if(artifact.getQueryNumIid()!=null){
	// map.put("numIid", artifact.getQueryNumIid());
	// }
	// if(artifact.getTitle()!=null && !"".equals(artifact.getTitle())){
	// map.put("title", artifact.getTitle());
	// }
	// if(artifact.getQueryIshow()!=null &&
	// !"".equals(artifact.getQueryIshow())){
	// map.put("approveStatus", artifact.getQueryIshow());
	// }
	// List<Item> itemList = myBatisDao.findLimitList(Item.class.getName(),
	// "findItem", map, currentRows);
	// Map<String,Object> result = null;
	// if(itemList!=null && !itemList.isEmpty()){
	// result = new HashMap<String, Object>();
	// result.put("data", itemList);
	// result.put("pageNo", artifact.getPageNo());
	// result.put("totalCount", totalCount);
	// result.put("totalPage", totalPage);
	// }
	// return result;
	// }

	/**
	 * 设置页 numIid不为空时，查询右侧数据
	 * 
	 * @param comm
	 * @return
	 */
	// public List<Item> findSetupItem(CommodityArtifact comm){
	// Map<String,Object> map = new HashMap<String, Object>();
	// map.put("numIid", comm.getNumIid());
	// map.put("nick", comm.getUserId());
	// List<Item> itemList = myBatisDao.findList(Item.class.getName(),
	// "findsetupCommodity", map);
	// return itemList;
	// }

	// public Map<String, Long> insertAndFindItenList(List<Item> itemList,String
	// userNick) {
	// Map<String, Long> map = new HashMap<String, Long>();
	// if(null!=itemList&&itemList.size()>0){
	// myBatisDao.execute(Item.class.getName(), "insertbatchItemList",
	// itemList);
	// }
	// List<Item> queryItem = queryItem("","","",userNick);
	// if(null!=queryItem&&queryItem.size()>0){
	// for (Item item : queryItem) {
	// map.put(item.getTitle(), item.getNumIid());
	// }
	// }
	// return map;
	// }

	/**
	 * @Description: 查询用户所以商品描述及商品id
	 * @author HL
	 * @date 2017年11月17日 下午4:08:55
	 */
	// public Map<String, Long> findItemTitleAndItemid(String userId) {
	// Map<String, Long> map = new HashMap<String, Long>();
	// List<Item> list = myBatisDao.findList(Item.class.getName(),
	// "findItemTitleAndItemid", userId);
	// if(null != list){
	// for (Item item : list) {
	// map.put(item.getTitle().trim(), item.getNumIid());
	// }
	// }
	// return map;
	// }

	/**
	 * 根据商品名称查询商品分页（订单中心商品缩写） @Title: listItemByTitle @param @param
	 * userId @param @param title @param @param pageNo @param @return
	 * 设定文件 @return List<Item> 返回类型 @throws
	 */
	// public List<Item> listItemByTitle(ItemVO itemVO){
	// if(itemVO == null || itemVO.getUserId() == null ||
	// "".equals(itemVO.getUserId())){
	// return null;
	// }
	// if(itemVO.getPageNo() == null){
	// itemVO.setPageNo(1);
	// }
	// Map<String, Object> queryMap = new HashMap<>();
	// Integer startRows = (itemVO.getPageNo() - 1) * PAGE_SIZE;
	// queryMap.put("startRows", startRows);
	// queryMap.put("pageSize", PAGE_SIZE);
	// queryMap.put("userId", itemVO.getUserId());
	// queryMap.put("numIid", itemVO.getItemId());
	// queryMap.put("title", itemVO.getTitle());
	// queryMap.put("groupId", itemVO.getGroupId());
	// if(itemVO.getStatus() == null || itemVO.getStatus() == 0){
	// queryMap.put("approveStatus", null);
	// }else{
	// queryMap.put("approveStatus", "onsale");
	// }
	// List<Item> itemList = myBatisDao.findList(Item.class.getName(),
	// "listItemByTitle", queryMap);
	// return itemList;
	// }

	/**
	 * 根据商品名称查询商品总数（订单中心商品缩写） @Title: countItemByTitle @param @param
	 * userId @param @param title @param @return 设定文件 @return Integer
	 * 返回类型 @throws
	 */
	// public Integer countItemByTitle(ItemVO itemVO){
	// if(itemVO == null || itemVO.getUserId() == null ||
	// "".equals(itemVO.getUserId())){
	// return 0;
	// }
	// Map<String, Object> queryMap = new HashMap<>();
	// queryMap.put("userId", itemVO.getUserId());
	// queryMap.put("numIid", itemVO.getItemId());
	// queryMap.put("title", itemVO.getTitle());
	// queryMap.put("groupId", itemVO.getGroupId());
	// if(itemVO.getStatus() == null || itemVO.getStatus() == 0){
	// queryMap.put("approveStatus", null);
	// }else{
	// queryMap.put("approveStatus", "onsale");
	// }
	// Integer itemCount = myBatisDao.findBy(Item.class.getName(),
	// "countItemByTitle", queryMap);
	// return itemCount;
	// }

	/**
	 * 更改缩写 @Title: updateSubtitleById @param @param itemId @param @param
	 * subtitle 设定文件 @return void 返回类型 @throws
	 */
	// public void updateSubtitleById(Long itemId,String subtitle){
	// Map<String, Object> queryMap = new HashMap<>();
	// queryMap.put("itemId", itemId);
	// queryMap.put("subtitle", subtitle);
	// myBatisDao.execute(Item.class.getName(), "updateItemSubtitle", queryMap);
	// }

	/**
	 * 根据商品id查询商品缩写 @Title: findSubtitleById @param @param itemId @param @return
	 * 设定文件 @return String 返回类型 @throws
	 */
	// public String findSubtitleById(Long itemId){
	// if(null == itemId){
	// return null;
	// }
	// Item item = myBatisDao.findBy(Item.class.getName(), "findSubtitleById",
	// itemId);
	// if(item != null){
	// String subtitle = item.getSubtitle();
	// if(subtitle == null || "".equals(subtitle)){
	// return item.getTitle();
	// }
	// return subtitle;
	// }
	// return null;
	// }
}
