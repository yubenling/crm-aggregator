package com.kycrm.member.controller;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.annotation.Table;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.member.MemberMsgCriteria;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;


/** 
* @ClassName: MemberMessageController 
* @Description 会员短信群发
* @author jackstraw_yu
* @date 2018年3月1日 下午4:35:03 
*/
@Controller
@RequestMapping(value="/memberMsg")
public class MemberMessageController extends BaseController {
	private static final Log logger = LogFactory.getLog(MemberMessageController.class);
	 
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private IMemberDTOService memberDTOServiceImpl;
	

	/** 
	* @Description: 会员短信群发,筛选会员:<br/>
	* 1:通过会员分组筛选会员
	* 2:通过会员表,主/子订单表...等联表查询会员
	* @param  params
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年3月1日 下午5:51:13
	*/
	@RequestMapping(value="/queryMemberList")
	@ResponseBody
	public String selectMember(@RequestBody String params,HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		MemberMsgCriteria criteria = JsonUtil.paramsJsonToObject(params, MemberMsgCriteria.class);
		if(criteria==null){
			return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		}
		try {
			boolean check = this.checkQueryParam(criteria);
			if(!check){
				return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
			}
		} catch (Exception e) {
			logger.error("##################### checkQueryTable() Exception:"+e.getMessage());
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		
		criteria.setUid(user.getId());criteria.setUserNick(user.getTaobaoUserNick());
		//会员短信群发-->a.直接根据条件筛选会员筛选;b.通过会员分组id
		try {
			this.checkQueryTable(criteria);//校验查询条件是否要联表查询
		} catch (Exception e) {
			logger.error("##################### checkQueryTable() Exception:"+e.getMessage());
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		this.createQueryParam(criteria);
		//给页面返回动态key和查询结果;{"data":[{.},{.}...],"key":"123","count":250}
		Map<String, Object> result = new HashMap<>();
		String dynamicKey = this.produceQueryKey();
		try {
			MemberInfoDTO memberInfo = new MemberInfoDTO();
			memberInfo.setBuyerNick("哈数据库等哈");
			memberInfo.setLastTradeTime(new Date());
			memberInfo.setTradeNum(100L);
			memberInfo.setTradeAmount(new BigDecimal(1000));
			memberInfo.setAvgTradePrice(new BigDecimal(10.05));
			result  = memberDTOServiceImpl.queryMemberList(user.getId(),criteria,dynamicKey);	
			ArrayList<MemberInfoDTO> arrayList = new ArrayList<MemberInfoDTO>();
			arrayList.add(memberInfo);
			result.put("data", arrayList);result.put("count", 0);result.put("key", dynamicKey);
		} catch (Exception e) {
			logger.error("##################### queryMemberList() Exception:"+e.getMessage());
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		return successReusltMap(null).put(ApiResult.API_RESULT,result).toJson();
	}
	
	private void createQueryParam(MemberMsgCriteria criteria) {
		if(criteria.getMemberGroupId() != null){
			
		}else {
			if(criteria.getNoneTradeTime() != null){
				if(criteria.getNoneTradeTime() == 3){
					criteria.setMinNoneTradeTime(DateUtils.getTimeByDay(new Date(), -3));
				}else {
					criteria.setMinNoneTradeTime(DateUtils.getTimeByDay(new Date(), -criteria.getNoneTradeTime()));
					criteria.setMaxNoneTradeTime(new Date());
				}
			}
			if(criteria.getItemIds() != null && !"".equals(criteria.getItemIds())){
				List<String> items = new ArrayList<>();
				String[] itemArr = criteria.getItemIds().split(",");
				for (int i = 0; i < itemArr.length; i++) {
					items.add(itemArr[i]);
				}
				criteria.setItemIdList(items);
			}
			if(criteria.getProvinces() != null && !"".equals(criteria.getProvinces())){
				List<String> provinces = new ArrayList<>();
				String[] provinceArr = criteria.getProvinces().split(",");
				for (int i = 0; i < provinceArr.length; i++) {
					provinces.add(provinceArr[i]);
				}
				criteria.setItemIdList(provinces);
			}
			if(criteria.getSellerFlag() != null && !"".equals(criteria.getSellerFlag())){
				List<String> flags = new ArrayList<>();
				String[] flagArr = criteria.getSellerFlag().split(",");
				for (int i = 0; i < flagArr.length; i++) {
					flags.add(flagArr[i]);
				}
				criteria.setItemIdList(flags);
			}
			if(criteria.getTradeTime() != null){
				if(criteria.getTradeTime() == 3){
					criteria.setMinTradeTime(DateUtils.getTimeByDay(new Date(), -3));
				}else {
					criteria.setMinTradeTime(DateUtils.getTimeByDay(new Date(), -criteria.getTradeTime()));
					criteria.setMaxTradeTime(new Date());
				}
			}
			if(criteria.getTradeNum() != null && criteria.getTradeNum() == 3L){
				criteria.setMinTradeNum(3L);
			}
			if(criteria.getTradeAmount() != null){
				if(criteria.getTradeAmount() == 300){
					criteria.setMinTradeAmount(new BigDecimal(300));
				}else if(criteria.getTradeAmount() == 200){
					criteria.setMinTradeAmount(new BigDecimal(200));
					criteria.setMaxTradeAmount(new BigDecimal(300));
				}else if(criteria.getTradeAmount() == 100){
					criteria.setMinTradeAmount(new BigDecimal(100));
					criteria.setMaxTradeAmount(new BigDecimal(200));
				}else if(criteria.getTradeAmount() == 1){
					criteria.setMinTradeAmount(new BigDecimal(1));
					criteria.setMaxTradeAmount(new BigDecimal(100));
				}
			}
			if(criteria.getAvgPrice() != null){
				if(criteria.getAvgPrice() == 300){
					criteria.setMinAvgPrice(new BigDecimal(300));
				}else if(criteria.getAvgPrice() == 200){
					criteria.setMinAvgPrice(new BigDecimal(200));
					criteria.setMaxAvgPrice(new BigDecimal(300));
				}else if(criteria.getAvgPrice() == 100){
					criteria.setMinAvgPrice(new BigDecimal(100));
					criteria.setMaxAvgPrice(new BigDecimal(200));
				}else if(criteria.getAvgPrice() == 1){
					criteria.setMinAvgPrice(new BigDecimal(1));
					criteria.setMaxAvgPrice(new BigDecimal(100));
				}
			}
			if(criteria.getCloseTradeNum() != null && criteria.getCloseTradeNum() == 3){
				criteria.setMinCloseTradeNum(3);
			}
			
			
			
		}
		
		
		
		
		MemberMsgCriteria memberCriteriaPamrm = null;
		try {
			memberCriteriaPamrm = new  MemberMsgCriteria();
			/*SellerGroup sgroup = sellerGroupService.findSellerGroupInfo(memberCriteriaVo.getGroupId());
			// 判断用户分组（1为默认分组，2为用户添加分组）
			if (sgroup.getMemberType() != null && sgroup.getMemberType().equals("1")) {
				memberCriteriaPamrm.setGroupType(sgroup.getMemberType());
				// 获取默认分组的会员信息
				HashMap<String,Object> queryMap = new HashMap<String,Object>();
				queryMap.put("userId", memberCriteriaVo.getUserId());
				// 根据组ID获取当前组的查询条件
				MemberLevelSetting memberLevelSetting = memberLevelSettingService.getMemberLevelSettingInfo(memberCriteriaVo.getGroupId());
				if(null!=memberLevelSetting&&null!=memberLevelSetting.getMemberlevel()&&!"".equals(memberLevelSetting.getMemberlevel())){
					if("4".equals(memberLevelSetting.getMemberlevel())){
						memberCriteriaPamrm.setMinTradeNum(memberLevelSetting.getTurnover());
						memberCriteriaPamrm.setMinTradePrice(memberLevelSetting.getTradingVolume().doubleValue()); 
					}else{
						Integer memberlevel  =  Integer.parseInt(memberLevelSetting.getMemberlevel())+1;
						MemberLevelSetting highSettingInfo = memberLevelSettingService.findMemberLevelByLevelAndUserId(memberCriteriaVo.getUserId(),String.valueOf(memberlevel));
						memberCriteriaPamrm.setMinTradeNum(memberLevelSetting.getTurnover());
						memberCriteriaPamrm.setMinTradePrice(memberLevelSetting.getTradingVolume().doubleValue()); 
						if(null!=highSettingInfo){
							memberCriteriaPamrm.setMaxTradeNum(highSettingInfo.getTurnover());
							memberCriteriaPamrm.setMaxTradePrice(highSettingInfo.getTradingVolume().doubleValue()); 
						}
					}
					memberCriteriaPamrm.setGroupType("1");
				}else{
					return null;
				} 
			} else if (sgroup.getMemberType() != null && sgroup.getMemberType().equals("2")) {
				memberCriteriaPamrm.setGroupType(sgroup.getMemberType());
				SellerGroupRule sg = sellerGroupRuleService.findSellerGroupRule(memberCriteriaVo.getGroupId());
				if (sg != null) {
					if (sg.getProvince() != null && sg.getProvince() != "") {
						String province = sg.getProvince().replaceAll("市", "");
						String[] split = province.split(",");
						memberCriteriaPamrm.setAreaList(Arrays.asList(split));  
					}
					// 获取最大、最小累计金额
					if (sg.getMaxAccumulatedAmount() != null&&!"".equals(sg.getMaxAccumulatedAmount())) {
						memberCriteriaPamrm.setMaxTradePrice(Double.valueOf(sg.getMaxAccumulatedAmount())); 
					}
					if (sg.getMinAccumulatedAmount() != null&&!"".equals(sg.getMinAccumulatedAmount())) {
						memberCriteriaPamrm.setMinTradePrice(Double.valueOf(sg.getMinAccumulatedAmount())); 
					}
					List<String> productList = new ArrayList<String>();
					if (sg.getItemIds() != null&&!"".equals(sg.getItemIds())) {
						String[] split = sg.getItemIds().split(",");
						for(String product:split){
							productList.add(product);
						}
						memberCriteriaPamrm.setProductList(productList);  
					}

					// 获取最大、最小平均客单价
					if (sg.getMaxAveragePrice() != null&&!"".equals(sg.getMaxAveragePrice())) {
						memberCriteriaPamrm.setMaxAvgPrice(Double.valueOf(sg.getMaxAveragePrice())); 
					}
					if (sg.getMinAveragePrice() != null&&!"".equals(sg.getMinAveragePrice())) {
						memberCriteriaPamrm.setMinAvgPrice(Double.valueOf(sg.getMinAveragePrice())); 
					}
					// 获取最大、最小交易次数
					if (sg.getMaxTradeNum() != null) {
						memberCriteriaPamrm.setMaxTradeNum(sg.getMaxTradeNum()); 
					}
					if (sg.getMinTradeNum() != null) {
						memberCriteriaPamrm.setMinTradeNum(sg.getMinTradeNum()); 
					}
					memberCriteriaPamrm.setGroupType("2");
					
					//  交易时间
					if(!"不限".equals(sgroup.getStatus())&&sgroup.getStatus()!=null&&!"".equals(sgroup.getStatus())){
						//获取最近交易设置的时间
						Calendar c = Calendar.getInstance();
						if(sg!=null&&sg.getTradeType()!=null){
							if(sg.getTradeType().equals("天")&&sg.getTradeDays()!=0){
								c.add(Calendar.DATE, - sg.getTradeDays());  
							}else if(sg.getTradeType().equals("月")&&sg.getTradeDays()!=0){
								c.add(Calendar.MONTH, - sg.getTradeDays());  
							}else if(sg.getTradeType().equals("年")&&sg.getTradeDays()!=0){
								c.add(Calendar.YEAR, - sg.getTradeDays());  
							}
							Date startTime = c.getTime();
							memberCriteriaPamrm.setTradeStartTime(DateUtils.dateToString(startTime, DateUtils.DEFAULT_TIME_FORMAT));
							memberCriteriaPamrm.setTradeEndTime(DateUtils.dateToString(new Date(), DateUtils.DEFAULT_TIME_FORMAT));
						}
					}
				}else{
					return null;
				}
			 }else{
				 return null;
			 }*/
		} catch (NumberFormatException e) {
//			 logger.error(e.getMessage()+"  用户"+memberCriteriaVo.getUserId()+"选择分组查询会员出错");
//			 return null;
		}
		if(criteria.getItemSend() != null){
			if(criteria.getItemSend() == 1){
				criteria.setBooitemSend(true);
			}else{
			criteria.setBooitemSend(false);
			}
		}
		if(criteria.getProvinceSend() != null){
			if(criteria.getProvinceSend() == 1){
				criteria.setBooprovinceSend(true);
			}else{
			criteria.setBooprovinceSend(false);
			}
		}
		if(criteria.getFilterBlackInt() != null){
			if(criteria.getFilterBlackInt() == 1){
				criteria.setFilterBlack(true);
			}else{
				criteria.setFilterBlack(false);
			}
		}
		if(criteria.getFilterneutralInt() != null){
			if(criteria.getFilterneutralInt() == 1){
				criteria.setFilterneutral(true);
			}else{
				criteria.setFilterneutral(false);
			}
		}
//		return memberCriteriaPamrm;
		
	}

	/**
	 * exportMember(下载会员)
	 * @Title: exportMember 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/exportMember")
	public String exportMember(HttpServletRequest request,HttpServletResponse response){
		
		
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		return resultMap.toJson();
	}


	/** 
	* @Description: 会员筛选:条件筛选/分组筛选是否存在冲突 
	* @param  criteria
	* @return boolean    返回类型 
	* @author jackstraw_yu
	* @date 2018年3月12日 上午11:54:00
	* @throws Exception 
	*/
	private boolean checkQueryParam(MemberMsgCriteria criteria) throws Exception {
		if(criteria.getMemberGroupId()!=null){//会员分组条件不为空时:其他条件需要是null
			  BeanInfo beanInfo = Introspector.getBeanInfo(MemberMsgCriteria.class);  
		        PropertyDescriptor[] proDescrtptors = beanInfo.getPropertyDescriptors();  
		        if (proDescrtptors != null && proDescrtptors.length > 0)
		            for (PropertyDescriptor propDesc : proDescrtptors) {  
		                if (!propDesc.getName().equalsIgnoreCase("MemberGroupId")) {  
		                	 Method readMethod = propDesc.getReadMethod();  
		                     Object objField = readMethod.invoke(criteria);  
		                     if(objField!=null){
		                    	return false;  
		                     }
		                }  
		            }  
		}
		if( (criteria.getMinTradeTime()!=null || criteria.getMaxTradeTime()!=null)
			&& (criteria.getMinNoneTradeTime()!=null || criteria.getMaxNoneTradeTime()!=null) ){
			return false;
		}
		return true;
	}


	/** 
	* @Description: 判断会员短信群发条件筛选是否要查询订单表
	* @param  criteria
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年3月8日 下午6:23:37
	* @throws Exception
	*/
	private void checkQueryTable(MemberMsgCriteria criteria) throws Exception {
		String trade_table="TRADE_DTO";
		Field[] fields = criteria.getClass().getDeclaredFields();
		Table annotation = null;Object obj =null;
		if(fields!=null && fields.length>0)
			for (Field field : fields) {
				field.setAccessible(true);
				annotation = field.getAnnotation(Table.class);
				obj = field.get(criteria);
				if(annotation!=null && annotation.value().equalsIgnoreCase(trade_table)){
					if(obj instanceof String){
						if(obj !=null && !"".equals(obj)){
							criteria.setQueryTable(trade_table);
							break;
						}
					}else{
						if(obj !=null){
							criteria.setQueryTable(trade_table);
							break;
						}
					}
				}
			}
	}

	/** 
	* @Description: 会员短信群发,生成动态查询key(当前毫秒值加三位随机数)
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年3月7日 下午12:07:21
	*/
	private String produceQueryKey(){
		ThreadLocalRandom current = ThreadLocalRandom.current();
		String key = "";
		for (int i = 0; i < 3; i++) key += current.nextInt(10);
		return System.currentTimeMillis()+"-"+key;
	}

	public void handleRequestArgument(MemberMsgCriteria criteria){
		if(criteria == null){
			return;
		}
		if(criteria.getTradeTime() != null){//交易时间
			Integer tradeTime = criteria.getTradeTime();
			criteria.setMinTradeTime(DateUtils.getTimeByDay(new Date(), -tradeTime));
			criteria.setMaxTradeTime(new Date());
		}
		if(criteria.getTradeNum() != null && criteria.getTradeNum() == 3L){//交易成功次数
			criteria.setMinTradeNum(3L);
		}
		if(criteria.getTradeAmount() != null){//累计消费金额
			if(criteria.getTradeAmount() == 1){
				criteria.setMinTradeAmount(new BigDecimal(1));
				criteria.setMaxTradeAmount(new BigDecimal(100));
			}else if(criteria.getTradeAmount() == 100){
				criteria.setMinTradeAmount(new BigDecimal(100));
				criteria.setMaxTradeAmount(new BigDecimal(200));
			}else if(criteria.getTradeAmount() == 200){
				criteria.setMinTradeAmount(new BigDecimal(200));
				criteria.setMaxTradeAmount(new BigDecimal(300));
			}else{
				criteria.setMinTradeAmount(new BigDecimal(300));
			}
		}
		if(criteria.getAvgPrice() != null){//平均客单价
			if(criteria.getAvgPrice() == 1){
				criteria.setMinAvgPrice(new BigDecimal(1));
				criteria.setMaxAvgPrice(new BigDecimal(100));
			}else if(criteria.getAvgPrice() == 100){
				criteria.setMinAvgPrice(new BigDecimal(100));
				criteria.setMaxAvgPrice(new BigDecimal(200));
			}else if(criteria.getAvgPrice() == 200){
				criteria.setMinAvgPrice(new BigDecimal(200));
				criteria.setMaxAvgPrice(new BigDecimal(300));
			}else{
				criteria.setMinAvgPrice(new BigDecimal(300));
			}
		}
		if(criteria.getCloseTradeNum() != null && criteria.getCloseTradeNum() == 3){//关闭交易次数
			criteria.setMinCloseTradeNum(3);
		}
		if(criteria.getJoinTimes() != null && criteria.getJoinTimes() == 3){//参与短信营销次数
			criteria.setMinJoinTimes(3);
		}
	}
	
}