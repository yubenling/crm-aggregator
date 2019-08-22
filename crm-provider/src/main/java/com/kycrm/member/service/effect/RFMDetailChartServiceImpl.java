package com.kycrm.member.service.effect;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.effect.IRFMDetailChartDao;
import com.kycrm.member.domain.entity.effect.RFMDetailChart;
import com.kycrm.util.DateUtils;
import com.kycrm.util.NumberUtils;

@Service("rfmDetailChartService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class RFMDetailChartServiceImpl implements IRFMDetailChartService{

	private Logger logger = LoggerFactory.getLogger(RFMDetailChartServiceImpl.class);
	
	@Autowired
	private IRFMDetailChartDao rfmDetailChartDao;
	
	@Override
	public List<RFMDetailChart> listRFMChartForDistinct(Long uid, Integer memberType,
			String dateType, Integer dateNum){
		if(uid == null || memberType == null || dateNum == null || dateNum == null){
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("memberType", memberType);
		map.put("dateType", dateType);
		map.put("dateNum", dateNum);
		List<RFMDetailChart> listRFMChartForDistinct = rfmDetailChartDao.listRFMChartForDistinct(map);
		return listRFMChartForDistinct;
	}
	
	@Override
	public void saveRFMDetailList(Long uid, List<RFMDetailChart> detailCharts){
		if(detailCharts == null || detailCharts.isEmpty()){
			return;
		}
		for (RFMDetailChart rfmDetailChart : detailCharts) {
			List<RFMDetailChart> hasData = this.listRFMChartForDistinct(uid, rfmDetailChart.getMemberType(), 
					rfmDetailChart.getDateType(), rfmDetailChart.getDateNum());
			if(hasData != null && !hasData.isEmpty()){
				rfmDetailChartDao.updateSingleData(rfmDetailChart);
			}else {
				rfmDetailChartDao.saveSingleData(rfmDetailChart);
			}
			
		}
	}
	
	@Override
	public Map<String, Object> queryRFMChartMap(Long uid, Integer memberType,
			Integer dateNum, String dateType){
		if(uid == null || memberType == null || dateNum == null || dateNum == null){
			return null;
		}
		Long l1 = System.currentTimeMillis();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("memberType", memberType);
		map.put("dateType", dateType);
		map.put("dateNum", dateNum);
		RFMDetailChart rfmChartData = rfmDetailChartDao.queryRFMDataForYou(map);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(rfmChartData != null){
			String dateStr = rfmChartData.getDateStr();
			String memberNumStr = rfmChartData.getMemberNumStr();
			String tradeNumStr = rfmChartData.getTradeNumStr();
			String tradeAmountStr = rfmChartData.getTradeAmountStr();
			String avgPriceStr = rfmChartData.getAvgPriceStr();
			List<String> dateList = this.stringToStrList(dateStr);
			List<Integer> createMemberNum = this.stringToIntegerList(memberNumStr);
			List<Integer> paidNum = this.stringToIntegerList(tradeNumStr);
			List<Double> paidFee = this.stringToDoubleList(tradeAmountStr);
			List<Double> avgPriceList = this.stringToDoubleList(avgPriceStr);
			resultMap.put("dateList", dateList);
			resultMap.put("createMemberNum", createMemberNum);
			resultMap.put("paidNum", paidNum);
			resultMap.put("paidFee", paidFee);
			resultMap.put("avgPriceList", avgPriceList);
			
		}else {
			
			Date eTime  = new Date(), bTime = new Date();
			if("month".equals(dateType)){
				if(6 == dateNum){
					bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(180, eTime));
				}else if(12 == dateNum){
					bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(365, eTime));
				}else if(24 == dateNum){
					bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(730, eTime));
				}
			}else if("day".equals(dateType)){
				if(7 == dateNum){
					bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(6, eTime));	
				}else if(15 == dateNum){
					bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(14, eTime));
				}else if(30 == dateNum){
					bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(29, eTime));
				}
			}
			Map<String, String> tempMap = new LinkedHashMap<>();
			if("day".equals(dateType) || "day" == dateType){
				for (int i = 0; i < dateNum; i++) {
					tempMap.put(DateUtils.dateToString(DateUtils.nDaysAfter(i, bTime)), "0,0.0,0");
				}
			}else if ("month".equals(dateType) || "month" == dateType) {
				for (int i = 0; i < dateNum + 1; i++) {
					tempMap.put(DateUtils.dateToStringYM(DateUtils.nDaysAfter(i * 30, bTime)), "0,0.0,0");
				}
			}
			if(tempMap != null){
				Set<Entry<String, String>> entrySet = tempMap.entrySet();
				List<String> dateList = new ArrayList<>();
				List<Integer> createMemberNum = new ArrayList<>();
				List<Integer> paidNum = new ArrayList<>();
				List<Double> paidFee = new ArrayList<>();
				List<Double> avgPriceList = new ArrayList<>();
				for (Entry<String, String> entry : entrySet) {
					String dateStr = entry.getKey();
					String value = entry.getValue();
					String[] dataArr = value.split(",");
					String tradeNumStr = dataArr[0];
					String tradeAmountStr = dataArr[1];
					String memberNumStr = dataArr[2];
					Double avgPrice = Integer.parseInt(memberNumStr) == 0 ? 0.0
							: (NumberUtils.getTwoDouble(
									new BigDecimal(tradeAmountStr).doubleValue() / Integer.parseInt(memberNumStr)));
					dateList.add(dateStr);
					createMemberNum.add(NumberUtils.stringToInteger(memberNumStr));
					paidNum.add(NumberUtils.stringToInteger(tradeNumStr));
					paidFee.add(NumberUtils.stringToDouble(tradeAmountStr));
					avgPriceList.add(avgPrice);
				}
				resultMap.put("dateList", dateList);
				resultMap.put("createMemberNum", createMemberNum);
				resultMap.put("paidNum", paidNum);
				resultMap.put("paidFee", paidFee);
				resultMap.put("avgPriceList", avgPriceList);
			}
			
		}
		Long l2 = System.currentTimeMillis();
		logger.info("uid:" + uid + ",查询RFM详细数据的图表数据完毕，耗时：" + (l2 - l1) + "ms");
		return resultMap;
		
	}
	
	
	
	public List<String> stringToStrList(String str){
		List<String> resultList = null;
		if(str != null && !"".equals(str)){
			resultList = new ArrayList<String>();
			String[] split = str.split(",");
			for (String string : split) {
				if(string != null && !"".equals(string)){
					resultList.add(string);
				}
			}
		}
		return resultList;
	}
	
	public List<Integer> stringToIntegerList(String str){
		List<Integer> resultList = null;
		if(str != null && !"".equals(str)){
			resultList = new ArrayList<Integer>();
			String[] split = str.split(",");
			for (String string : split) {
				if(string != null && !"".equals(string)){
					resultList.add(Integer.parseInt(string));
				}
			}
		}
		return resultList;
	}
	
	
	public List<Double> stringToDoubleList(String str){
		List<Double> resultList = null;
		if(str != null && !"".equals(str)){
			resultList = new ArrayList<Double>();
			String[] split = str.split(",");
			for (String string : split) {
				if(string != null && !"".equals(string)){
					resultList.add(Double.parseDouble(string));
				}
			}
		}
		return resultList;
	}
	
}
