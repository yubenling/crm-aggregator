package com.kycrm.syn.service.syn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.item.Item;
import com.kycrm.member.domain.entity.item.TbItem;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.syn.service.item.ItemServiceImpl;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.ApiException;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.response.ItemAddResponse;
/**
 * 用户拆分商品
 * @author wufan
 *
 */
@Service
public class SplitItemService {
	
	@Resource(name = "userInfoServiceDubbo")
	private IUserInfoService userInfoService;
	@Autowired
	private ItemServiceImpl itemService;

	
	/**
	 * 商品数据开始处理
	 */
	public void saveItems(List<TbItem> tbItemList) {
		if (ValidateUtil.isEmpty(tbItemList))
			return;
		List<Item> itemlist = tbItemList.stream()
				.filter(x -> x != null)
				.filter(x -> !ValidateUtil.isEmpty(x.getJdpResponse()))
				.map(tbItem -> this.getItemDTO(tbItem))
				.collect(Collectors.toList());
		Map<Long, List<Item>> addItemToList = addItemToList(itemlist);
		//根据不同用户分开保存
		for(Long uid:addItemToList.keySet()){
			if(uid==null){continue;}
			itemService.saveOrUpItem(uid,addItemToList.get(uid));
		}
	}
	/**
	 * 从TbItem获取Item
	 * 
	 * @param TbItem
	 * @return Item
	 */
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
	
	/**
     * 将查询出来的item根据uid进行拆分 key为uid value为List<Item>
     * @param itemlist
     */
	private Map<Long, List<Item>> addItemToList(List<Item> itemlist) {
		Map<Long, List<Item>> map=new HashMap<Long, List<Item>>();
		for(Item item:itemlist){
			Long uid=item.getUid();
			if(map.containsKey(uid)){
				map.get(uid).add(item);
			}else{
				List<Item> splitItemList=new ArrayList<Item>();
				splitItemList.add(item);
				map.put(uid, splitItemList);
			}
		}
		return map;
	}

}
