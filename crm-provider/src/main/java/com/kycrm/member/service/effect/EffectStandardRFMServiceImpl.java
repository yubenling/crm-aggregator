package com.kycrm.member.service.effect;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.effect.IEffectStandardRFMDao;
import com.kycrm.member.domain.entity.effect.EffectStandardRFM;
import com.kycrm.member.domain.vo.effect.EffectStandardRFMVO;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.util.DateUtils;
import com.kycrm.util.NumberUtils;

@Service("effectStandardRFMService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class EffectStandardRFMServiceImpl implements IEffectStandardRFMService {

	@Autowired
	private IEffectStandardRFMDao effectStandardRFMDao;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	@Autowired
	private IMemberDTOService memberDTOService;
	
	@Autowired
	private ITradeDTOService tradeDTOService;
	
	/**
	 * 保存单条数据
	 */
	@Override
	public void saveStandardRFM(EffectStandardRFM standardRFM){
		effectStandardRFMDao.saveSingleStandardRFM(standardRFM);
	}
	
	/**
	 * 保存多条数据
	 */
	@Override
	public void saveListStandardRFM(List<EffectStandardRFM> standardRFMs){
		if(standardRFMs == null || standardRFMs.isEmpty()){
			return;
		}
		for (EffectStandardRFM effectStandardRFM : standardRFMs) {
			if(effectStandardRFM.getUid() == null){
				return;
			}
			effectStandardRFM.setCreatedBy(effectStandardRFM.getUid() + "");
			effectStandardRFM.setLastModifiedBy(effectStandardRFM.getUid() + "");
			effectStandardRFM.setLastModifiedDate(new Date());
			List<EffectStandardRFM> resultList = this.listStandardRFMByType(effectStandardRFM.getUid(), effectStandardRFM.getEffectType(), effectStandardRFM.getTimeScope());
			if(resultList != null && !resultList.isEmpty()){
				this.effectStandardRFMDao.updateStandartRFM(effectStandardRFM);
			}else {
				effectStandardRFM.setCreatedDate(new Date());
				this.saveStandardRFM(effectStandardRFM);
			}
		}
	}
	
	/**
	 * 根据类型和时间区间查询数据
	 */
	@Override
	public List<EffectStandardRFM> listStandardRFMByType(Long uid, Integer effectType, String timeScope){
		if(uid == null || effectType == null || "".equals(effectType)){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("effectType", effectType);
		map.put("timeScope", timeScope);
		List<EffectStandardRFM> standardRFMs = effectStandardRFMDao.listRFMByEffectType(map);
		return standardRFMs;
	}
	
	/**
	 * 计算RFM标准分析页面数据
	 */
	@Override
	public EffectStandardRFMVO sumStandardRFM(Long uid, Integer effectType){
		if(uid == null || effectType == null){
			return null;
		}
		List<EffectStandardRFM> standardRFMs = this.listStandardRFMByType(uid, effectType, null);
		if(standardRFMs != null && !standardRFMs.isEmpty()){
			EffectStandardRFMVO resultRFM = new EffectStandardRFMVO();
			for (EffectStandardRFM standardRFM : standardRFMs) {
				if(standardRFM != null && standardRFM.getTimeScope() != null){
					if("ltam".equals(standardRFM.getTimeScope())){
						resultRFM.setLtamBuyOnceData(standardRFM.getBuyOnceData());
						resultRFM.setLtamBuyTwiceData(standardRFM.getBuyTwiceData());
						resultRFM.setLtamBuyThriceData(standardRFM.getBuyThriceData());
						resultRFM.setLtamBuyQuarticData(standardRFM.getBuyQuarticData());
						resultRFM.setLtamBuyQuinticData(standardRFM.getBuyQuinticData());
						/*if(0 == effectType){
							int totalData = NumberUtils.stringToInteger(standardRFM.getBuyOnceData()) + NumberUtils.stringToInteger(standardRFM.getBuyTwiceData()) + 
									NumberUtils.stringToInteger(standardRFM.getBuyThriceData()) + NumberUtils.stringToInteger(standardRFM.getBuyQuarticData()) + 
									NumberUtils.stringToInteger(standardRFM.getBuyQuinticData());
							resultRFM.setLtamBuyTimesData(totalData + "");
						}else{
							double totalData = NumberUtils.stringToDouble(standardRFM.getBuyOnceData()) + NumberUtils.stringToDouble(standardRFM.getBuyTwiceData()) + 
									NumberUtils.stringToDouble(standardRFM.getBuyThriceData()) + NumberUtils.stringToDouble(standardRFM.getBuyQuarticData()) + 
									NumberUtils.stringToDouble(standardRFM.getBuyQuinticData());
							resultRFM.setLtamBuyTimesData(NumberUtils.getTwoDouble(totalData) + "");
						}
						resultRFM.setLtamBuyOnceRatio(standardRFM.getBuyOnceRatio());
						resultRFM.setLtamBuyTwiceRatio(standardRFM.getBuyTwiceRatio());
						resultRFM.setLtamBuyThriceRatio(standardRFM.getBuyThriceRatio());
						resultRFM.setLtamBuyQuarticRatio(standardRFM.getBuyQuarticRatio());
						resultRFM.setLtamBuyQuinticRatio(standardRFM.getBuyQuinticRatio());
						double totalRatio = NumberUtils.stringToDouble(standardRFM.getBuyOnceRatio()) + NumberUtils.stringToDouble(standardRFM.getBuyTwiceRatio()) + 
								NumberUtils.stringToDouble(standardRFM.getBuyThriceRatio()) + NumberUtils.stringToDouble(standardRFM.getBuyQuarticRatio()) + 
								NumberUtils.stringToDouble(standardRFM.getBuyQuinticRatio());
						resultRFM.setLtamBuyTimesRatio(NumberUtils.getTwoDouble(totalRatio) + "%");*/
					}else if("amttm".equals(standardRFM.getTimeScope())){
						resultRFM.setAmttmBuyOnceData(standardRFM.getBuyOnceData());
						resultRFM.setAmttmBuyTwiceData(standardRFM.getBuyTwiceData());
						resultRFM.setAmttmBuyThriceData(standardRFM.getBuyThriceData());
						resultRFM.setAmttmBuyQuarticData(standardRFM.getBuyQuarticData());
						resultRFM.setAmttmBuyQuinticData(standardRFM.getBuyQuinticData());
						/*if(0 == effectType){
							int totalData = NumberUtils.stringToInteger(standardRFM.getBuyOnceData()) + NumberUtils.stringToInteger(standardRFM.getBuyTwiceData()) + 
									NumberUtils.stringToInteger(standardRFM.getBuyThriceData()) + NumberUtils.stringToInteger(standardRFM.getBuyQuarticData()) + 
									NumberUtils.stringToInteger(standardRFM.getBuyQuinticData());
							resultRFM.setAmttmBuyTimesData(totalData + "");
						}else{
							double totalData = NumberUtils.stringToDouble(standardRFM.getBuyOnceData()) + NumberUtils.stringToDouble(standardRFM.getBuyTwiceData()) + 
									NumberUtils.stringToDouble(standardRFM.getBuyThriceData()) + NumberUtils.stringToDouble(standardRFM.getBuyQuarticData()) + 
									NumberUtils.stringToDouble(standardRFM.getBuyQuinticData());
							resultRFM.setAmttmBuyTimesData(NumberUtils.getTwoDouble(totalData) + "");
						}
						resultRFM.setAmttmBuyOnceRatio(standardRFM.getBuyOnceRatio());
						resultRFM.setAmttmBuyTwiceRatio(standardRFM.getBuyTwiceRatio());
						resultRFM.setAmttmBuyThriceRatio(standardRFM.getBuyThriceRatio());
						resultRFM.setAmttmBuyQuarticRatio(standardRFM.getBuyQuarticRatio());
						resultRFM.setAmttmBuyQuinticRatio(standardRFM.getBuyQuinticRatio());
						double totalRatio = NumberUtils.stringToDouble(standardRFM.getBuyOnceRatio()) + NumberUtils.stringToDouble(standardRFM.getBuyTwiceRatio()) + 
								NumberUtils.stringToDouble(standardRFM.getBuyThriceRatio()) + NumberUtils.stringToDouble(standardRFM.getBuyQuarticRatio()) + 
								NumberUtils.stringToDouble(standardRFM.getBuyQuinticRatio());
						resultRFM.setAmttmBuyTimesRatio(NumberUtils.getTwoDouble(totalRatio) + "%");*/
					}else if("tmtsm".equals(standardRFM.getTimeScope())){
						resultRFM.setTmtsmBuyOnceData(standardRFM.getBuyOnceData());
						resultRFM.setTmtsmBuyTwiceData(standardRFM.getBuyTwiceData());
						resultRFM.setTmtsmBuyThriceData(standardRFM.getBuyThriceData());
						resultRFM.setTmtsmBuyQuarticData(standardRFM.getBuyQuarticData());
						resultRFM.setTmtsmBuyQuinticData(standardRFM.getBuyQuinticData());
						/*if(0 == effectType){
							int totalData = NumberUtils.stringToInteger(standardRFM.getBuyOnceData()) + NumberUtils.stringToInteger(standardRFM.getBuyTwiceData()) + 
									NumberUtils.stringToInteger(standardRFM.getBuyThriceData()) + NumberUtils.stringToInteger(standardRFM.getBuyQuarticData()) + 
									NumberUtils.stringToInteger(standardRFM.getBuyQuinticData());
							resultRFM.setTmtsmBuyTimesData(totalData + "");
						}else{
							double totalData = NumberUtils.stringToDouble(standardRFM.getBuyOnceData()) + NumberUtils.stringToDouble(standardRFM.getBuyTwiceData()) + 
									NumberUtils.stringToDouble(standardRFM.getBuyThriceData()) + NumberUtils.stringToDouble(standardRFM.getBuyQuarticData()) + 
									NumberUtils.stringToDouble(standardRFM.getBuyQuinticData());
							resultRFM.setTmtsmBuyTimesData(NumberUtils.getTwoDouble(totalData) + "");
						}
						resultRFM.setTmtsmBuyOnceRatio(standardRFM.getBuyOnceRatio());
						resultRFM.setTmtsmBuyTwiceRatio(standardRFM.getBuyTwiceRatio());
						resultRFM.setTmtsmBuyThriceRatio(standardRFM.getBuyThriceRatio());
						resultRFM.setTmtsmBuyQuarticRatio(standardRFM.getBuyQuarticRatio());
						resultRFM.setTmtsmBuyQuinticRatio(standardRFM.getBuyQuinticRatio());
						double totalRatio = NumberUtils.stringToDouble(standardRFM.getBuyOnceRatio()) + NumberUtils.stringToDouble(standardRFM.getBuyTwiceRatio()) + 
								NumberUtils.stringToDouble(standardRFM.getBuyThriceRatio()) + NumberUtils.stringToDouble(standardRFM.getBuyQuarticRatio()) + 
								NumberUtils.stringToDouble(standardRFM.getBuyQuinticRatio());
						resultRFM.setTmtsmBuyTimesRatio(NumberUtils.getTwoDouble(totalRatio) + "%");*/
					}else if("smtay".equals(standardRFM.getTimeScope())){
						resultRFM.setSmtayBuyOnceData(standardRFM.getBuyOnceData());
						resultRFM.setSmtayBuyTwiceData(standardRFM.getBuyTwiceData());
						resultRFM.setSmtayBuyThriceData(standardRFM.getBuyThriceData());
						resultRFM.setSmtayBuyQuarticData(standardRFM.getBuyQuarticData());
						resultRFM.setSmtayBuyQuinticData(standardRFM.getBuyQuinticData());
						/*if(0 == effectType){
							int totalData = NumberUtils.stringToInteger(standardRFM.getBuyOnceData()) + NumberUtils.stringToInteger(standardRFM.getBuyTwiceData()) + 
									NumberUtils.stringToInteger(standardRFM.getBuyThriceData()) + NumberUtils.stringToInteger(standardRFM.getBuyQuarticData()) + 
									NumberUtils.stringToInteger(standardRFM.getBuyQuinticData());
							resultRFM.setSmtayBuyTimesData(totalData + "");
						}else{
							double totalData = NumberUtils.stringToDouble(standardRFM.getBuyOnceData()) + NumberUtils.stringToDouble(standardRFM.getBuyTwiceData()) + 
									NumberUtils.stringToDouble(standardRFM.getBuyThriceData()) + NumberUtils.stringToDouble(standardRFM.getBuyQuarticData()) + 
									NumberUtils.stringToDouble(standardRFM.getBuyQuinticData());
							resultRFM.setSmtayBuyTimesData(NumberUtils.getTwoDouble(totalData) + "");
						}
						resultRFM.setSmtayBuyOnceRatio(standardRFM.getBuyOnceRatio());
						resultRFM.setSmtayBuyTwiceRatio(standardRFM.getBuyTwiceRatio());
						resultRFM.setSmtayBuyThriceRatio(standardRFM.getBuyThriceRatio());
						resultRFM.setSmtayBuyQuarticRatio(standardRFM.getBuyQuarticRatio());
						resultRFM.setSmtayBuyQuinticRatio(standardRFM.getBuyQuinticRatio());
						double totalRatio = NumberUtils.stringToDouble(standardRFM.getBuyOnceRatio()) + NumberUtils.stringToDouble(standardRFM.getBuyTwiceRatio()) + 
								NumberUtils.stringToDouble(standardRFM.getBuyThriceRatio()) + NumberUtils.stringToDouble(standardRFM.getBuyQuarticRatio()) + 
								NumberUtils.stringToDouble(standardRFM.getBuyQuinticRatio());
						resultRFM.setSmtayBuyTimesRatio(NumberUtils.getTwoDouble(totalRatio) + "%");*/
					}else if ("mtay".equals(standardRFM.getTimeScope())) {
						resultRFM.setMtayBuyOnceData(standardRFM.getBuyOnceData());
						resultRFM.setMtayBuyTwiceData(standardRFM.getBuyTwiceData());
						resultRFM.setMtayBuyThriceData(standardRFM.getBuyThriceData());
						resultRFM.setMtayBuyQuarticData(standardRFM.getBuyQuarticData());
						resultRFM.setMtayBuyQuinticData(standardRFM.getBuyQuinticData());
						/*if(0 == effectType){
							int totalData = NumberUtils.stringToInteger(standardRFM.getBuyOnceData()) + NumberUtils.stringToInteger(standardRFM.getBuyTwiceData()) + 
									NumberUtils.stringToInteger(standardRFM.getBuyThriceData()) + NumberUtils.stringToInteger(standardRFM.getBuyQuarticData()) + 
									NumberUtils.stringToInteger(standardRFM.getBuyQuinticData());
							resultRFM.setMtayBuyTimesData(totalData + "");
						}else{
							double totalData = NumberUtils.stringToDouble(standardRFM.getBuyOnceData()) + NumberUtils.stringToDouble(standardRFM.getBuyTwiceData()) + 
									NumberUtils.stringToDouble(standardRFM.getBuyThriceData()) + NumberUtils.stringToDouble(standardRFM.getBuyQuarticData()) + 
									NumberUtils.stringToDouble(standardRFM.getBuyQuinticData());
							resultRFM.setMtayBuyTimesData(NumberUtils.getTwoDouble(totalData) + "");
						}
						resultRFM.setMtayBuyOnceRatio(standardRFM.getBuyOnceRatio());
						resultRFM.setMtayBuyTwiceRatio(standardRFM.getBuyTwiceRatio());
						resultRFM.setMtayBuyThriceRatio(standardRFM.getBuyThriceRatio());
						resultRFM.setMtayBuyQuarticRatio(standardRFM.getBuyQuarticRatio());
						resultRFM.setMtayBuyQuinticRatio(standardRFM.getBuyQuinticRatio());
						double totalRatio = NumberUtils.stringToDouble(standardRFM.getBuyOnceRatio()) + NumberUtils.stringToDouble(standardRFM.getBuyTwiceRatio()) + 
								NumberUtils.stringToDouble(standardRFM.getBuyThriceRatio()) + NumberUtils.stringToDouble(standardRFM.getBuyQuarticRatio()) + 
								NumberUtils.stringToDouble(standardRFM.getBuyQuinticRatio());
						resultRFM.setMtayBuyTimesRatio(NumberUtils.getTwoDouble(totalRatio) + "%");*/
					}
				}
			}
			
			/*double totalBuyOnceRatio = NumberUtils.stringToDouble(resultRFM.getLtamBuyOnceRatio()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyOnceRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getTmtsmBuyOnceRatio()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyOnceRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getMtayBuyOnceRatio());
			double totalBuyTwiceRatio =NumberUtils.stringToDouble(resultRFM.getLtamBuyTwiceRatio()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyTwiceRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getTmtsmBuyTwiceRatio()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyTwiceRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getMtayBuyTwiceRatio());
			double totalBuyThriceRatio = NumberUtils.stringToDouble(resultRFM.getLtamBuyThriceRatio()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyThriceRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getTmtsmBuyThriceRatio()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyThriceRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getMtayBuyThriceRatio());
			double totalBuyQuarticRatio = NumberUtils.stringToDouble(resultRFM.getLtamBuyQuarticRatio()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyQuarticRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getTmtsmBuyQuarticRatio()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyQuarticRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getMtayBuyQuarticRatio());
			double totalBuyQuinticRatio = NumberUtils.stringToDouble(resultRFM.getLtamBuyQuinticRatio()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyQuinticRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getTmtsmBuyQuinticRatio()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyQuinticRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getMtayBuyQuinticRatio());
			double totalBuyTimesRatio = NumberUtils.stringToDouble(resultRFM.getLtamBuyTimesRatio()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyTimesRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getTmtsmBuyTimesRatio()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyTimesRatio()) + 
					NumberUtils.stringToDouble(resultRFM.getMtayBuyTimesRatio());
			if(effectType == 0){
				int totalBuyOnceData = NumberUtils.stringToInteger(resultRFM.getLtamBuyOnceData()) + NumberUtils.stringToInteger(resultRFM.getAmttmBuyOnceData()) + 
						NumberUtils.stringToInteger(resultRFM.getTmtsmBuyOnceData()) + NumberUtils.stringToInteger(resultRFM.getSmtayBuyOnceData()) + 
						NumberUtils.stringToInteger(resultRFM.getMtayBuyOnceData());
				int totalBuyTwiceData = NumberUtils.stringToInteger(resultRFM.getLtamBuyTwiceData()) + NumberUtils.stringToInteger(resultRFM.getAmttmBuyTwiceData()) + 
						NumberUtils.stringToInteger(resultRFM.getTmtsmBuyTwiceData()) + NumberUtils.stringToInteger(resultRFM.getSmtayBuyTwiceData()) + 
						NumberUtils.stringToInteger(resultRFM.getMtayBuyTwiceData());
				int totalBuyThriceData = NumberUtils.stringToInteger(resultRFM.getLtamBuyThriceData()) + NumberUtils.stringToInteger(resultRFM.getAmttmBuyThriceData()) + 
						NumberUtils.stringToInteger(resultRFM.getTmtsmBuyThriceData()) + NumberUtils.stringToInteger(resultRFM.getSmtayBuyThriceData()) + 
						NumberUtils.stringToInteger(resultRFM.getMtayBuyThriceData());
				int totalBuyQuarticData = NumberUtils.stringToInteger(resultRFM.getLtamBuyQuarticData()) + NumberUtils.stringToInteger(resultRFM.getAmttmBuyQuarticData()) + 
						NumberUtils.stringToInteger(resultRFM.getTmtsmBuyQuarticData()) + NumberUtils.stringToInteger(resultRFM.getSmtayBuyQuarticData()) + 
						NumberUtils.stringToInteger(resultRFM.getMtayBuyQuarticData());
				int totalBuyQuinticData = NumberUtils.stringToInteger(resultRFM.getLtamBuyQuinticData()) + NumberUtils.stringToInteger(resultRFM.getAmttmBuyQuinticData()) + 
						NumberUtils.stringToInteger(resultRFM.getTmtsmBuyQuinticData()) + NumberUtils.stringToInteger(resultRFM.getSmtayBuyQuinticData()) + 
						NumberUtils.stringToInteger(resultRFM.getMtayBuyQuinticData());
				int totalBuyTimesData = NumberUtils.stringToInteger(resultRFM.getLtamBuyTimesData()) + NumberUtils.stringToInteger(resultRFM.getAmttmBuyTimesData()) + 
						NumberUtils.stringToInteger(resultRFM.getTmtsmBuyTimesData()) + NumberUtils.stringToInteger(resultRFM.getSmtayBuyTimesData()) + 
						NumberUtils.stringToInteger(resultRFM.getMtayBuyTimesData());
				resultRFM.setTotalBuyOnceData(totalBuyOnceData + "");
				resultRFM.setTotalBuyTwiceData(totalBuyTwiceData + "");
				resultRFM.setTotalBuyThriceData(totalBuyThriceData + "");
				resultRFM.setTotalBuyQuarticData(totalBuyQuarticData + "");
				resultRFM.setTotalBuyQuinticData(totalBuyQuinticData + "");
				resultRFM.setTotalBuyTimesData(totalBuyTimesData + "");
			}else {
				double totalByOnceAmount = NumberUtils.stringToDouble(resultRFM.getLtamBuyOnceData()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyOnceData()) + 
						NumberUtils.stringToDouble(resultRFM.getTmtsmBuyOnceData()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyOnceData()) + 
						NumberUtils.stringToDouble(resultRFM.getMtayBuyOnceData());;
				double totalByTwiceAmount = NumberUtils.stringToDouble(resultRFM.getLtamBuyTwiceData()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyTwiceData()) + 
						NumberUtils.stringToDouble(resultRFM.getTmtsmBuyTwiceData()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyTwiceData()) + 
						NumberUtils.stringToDouble(resultRFM.getMtayBuyTwiceData());
				double totalByThriceAmount = NumberUtils.stringToDouble(resultRFM.getLtamBuyThriceData()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyThriceData()) + 
						NumberUtils.stringToDouble(resultRFM.getTmtsmBuyThriceData()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyThriceData()) + 
						NumberUtils.stringToDouble(resultRFM.getMtayBuyThriceData());;
				double totalByQuarticAmount = NumberUtils.stringToDouble(resultRFM.getLtamBuyQuarticData()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyQuarticData()) + 
						NumberUtils.stringToDouble(resultRFM.getTmtsmBuyQuarticData()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyQuarticData()) + 
						NumberUtils.stringToDouble(resultRFM.getMtayBuyQuarticData());
				double totalByQuinticAmount = NumberUtils.stringToDouble(resultRFM.getLtamBuyQuinticData()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyQuinticData()) + 
						NumberUtils.stringToDouble(resultRFM.getTmtsmBuyQuinticData()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyQuinticData()) + 
						NumberUtils.stringToDouble(resultRFM.getMtayBuyQuinticData());
				double totalByTimesAmount = NumberUtils.stringToDouble(resultRFM.getLtamBuyTimesData()) + NumberUtils.stringToDouble(resultRFM.getAmttmBuyTimesData()) + 
						NumberUtils.stringToDouble(resultRFM.getTmtsmBuyTimesData()) + NumberUtils.stringToDouble(resultRFM.getSmtayBuyTimesData()) + 
						NumberUtils.stringToDouble(resultRFM.getMtayBuyTimesData());
				resultRFM.setTotalBuyOnceData(NumberUtils.getTwoDouble(totalByOnceAmount) + "");
				resultRFM.setTotalBuyTwiceData(NumberUtils.getTwoDouble(totalByTwiceAmount) + "");
				resultRFM.setTotalBuyThriceData(NumberUtils.getTwoDouble(totalByThriceAmount) + "");
				resultRFM.setTotalBuyQuarticData(NumberUtils.getTwoDouble(totalByQuarticAmount) + "");
				resultRFM.setTotalBuyQuinticData(NumberUtils.getTwoDouble(totalByQuinticAmount) + "");
				resultRFM.setTotalBuyTimesData(NumberUtils.getTwoDouble(totalByTimesAmount) + "");
			}*/
			/*resultRFM.setTotalBuyOnceRatio(NumberUtils.getTwoDouble(totalBuyOnceRatio) + "%");
			resultRFM.setTotalBuyTwiceRatio(NumberUtils.getTwoDouble(totalBuyTwiceRatio) + "%");
			resultRFM.setTotalBuyThriceRatio(NumberUtils.getTwoDouble(totalBuyThriceRatio) + "%");
			resultRFM.setTotalBuyQuarticRatio(NumberUtils.getTwoDouble(totalBuyQuarticRatio) + "%");
			resultRFM.setTotalBuyQuinticRatio(NumberUtils.getTwoDouble(totalBuyQuinticRatio) + "%");
			resultRFM.setTotalBuyTimesRatio(NumberUtils.getTwoDouble(totalBuyTimesRatio) + "%");*/
			return resultRFM;
		}
		return null;
	}
	
	/**
	 * 计算RFM标准分析数据的job
	 */
	/*public void standardRFMJob(){
		List<Long> uidList = userInfoService.listTokenNotNull();
		Date startNodeDate = new Date();
		if(uidList != null && !uidList.isEmpty()){
			for (Long uid : uidList) {
				try {
					List<EffectStandardRFM> customerRFMs = this.listCustomerRFMs(uid, startNodeDate);
					this.saveListStandardRFM(customerRFMs);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}*/
	
	/**
	 * 计算标准RFM中会员数的数据
	 */
	@Override
	public List<EffectStandardRFM> listCustomerRFMs(Long uid, Date startNodeDate,
			Long memberCount, Double totalPaidFee) throws Exception{
		List<EffectStandardRFM> results = new ArrayList<>();
		//计算近30天有交易客户数
		EffectStandardRFM standardCusRFMLtam = new EffectStandardRFM();
		standardCusRFMLtam.setUid(uid);
		standardCusRFMLtam.setTimeScope("ltam");
		standardCusRFMLtam.setEffectType(0);
		//计算近30天有交易消费金额
				EffectStandardRFM standardPaidRFMLtam = new EffectStandardRFM();
				standardPaidRFMLtam.setUid(uid);
				standardPaidRFMLtam.setTimeScope("ltam");
				standardPaidRFMLtam.setEffectType(1);
				//计算近30天有交易平均客单价
				EffectStandardRFM standardAvgRFMLtam = new EffectStandardRFM();
				standardAvgRFMLtam.setUid(uid);
				standardAvgRFMLtam.setEffectType(2);
				standardAvgRFMLtam.setTimeScope("ltam");
				
				
		Date ltamBTime = DateUtils.addDate(startNodeDate, -30), ltamETime = startNodeDate;
		this.assembleCusStandardRFM(standardCusRFMLtam, standardPaidRFMLtam, standardAvgRFMLtam, ltamBTime, ltamETime, memberCount, totalPaidFee);
		results.add(standardCusRFMLtam);
		//计算近30 - 90天有交易客户数
		EffectStandardRFM standardCusRFMAmttm = new EffectStandardRFM();
		standardCusRFMAmttm.setUid(uid);
		standardCusRFMAmttm.setTimeScope("amttm");
		standardCusRFMAmttm.setEffectType(0);
		//计算近30 - 90天有交易消费金额
				EffectStandardRFM standardPaidRFMAmttm = new EffectStandardRFM();
				standardPaidRFMAmttm.setUid(uid);
				standardPaidRFMAmttm.setTimeScope("amttm");
				standardPaidRFMAmttm.setEffectType(1);
				//计算近30 - 90天有交易平均客单价
				EffectStandardRFM standardAvgRFMAmttm = new EffectStandardRFM();
				standardAvgRFMAmttm.setUid(uid);
				standardAvgRFMAmttm.setEffectType(2);
				standardAvgRFMAmttm.setTimeScope("amttm");
				
		Date amttmBTime = DateUtils.addDate(startNodeDate, -90), amttmETime = DateUtils.addDate(startNodeDate, -30);
		this.assembleCusStandardRFM(standardCusRFMAmttm, standardPaidRFMAmttm, standardAvgRFMAmttm, amttmBTime, amttmETime, memberCount, totalPaidFee);
		results.add(standardCusRFMAmttm);
		//计算近90 - 180天有交易客户数
		EffectStandardRFM standardCusRFMTmtsm = new EffectStandardRFM();
		standardCusRFMTmtsm.setUid(uid);
		standardCusRFMTmtsm.setTimeScope("tmtsm");
		standardCusRFMTmtsm.setEffectType(0);
		//计算近90 - 180天有交易消费金额
				EffectStandardRFM standardPaidRFMTmtsm = new EffectStandardRFM();
				standardPaidRFMTmtsm.setUid(uid);
				standardPaidRFMTmtsm.setTimeScope("tmtsm");
				standardPaidRFMTmtsm.setEffectType(1);
				//计算近90 - 180天有交易平均客单价
				EffectStandardRFM standardAvgRFMTmtsm = new EffectStandardRFM();
				standardAvgRFMTmtsm.setUid(uid);
				standardAvgRFMTmtsm.setEffectType(2);
				standardAvgRFMTmtsm.setTimeScope("tmtsm");
				
		Date tmtsmBTime = DateUtils.addDate(startNodeDate, -180), tmtsmETime = DateUtils.addDate(startNodeDate, -90);
		this.assembleCusStandardRFM(standardCusRFMTmtsm, standardPaidRFMTmtsm, standardAvgRFMTmtsm, tmtsmBTime, tmtsmETime, memberCount, totalPaidFee);
		results.add(standardCusRFMTmtsm);
		//计算近180 - 360天有交易客户数
		EffectStandardRFM standardCusRFMSmtay = new EffectStandardRFM();
		standardCusRFMSmtay.setUid(uid);
		standardCusRFMSmtay.setTimeScope("smtay");
		standardCusRFMSmtay.setEffectType(0);
		//计算近180 - 360天有交易消费金额
				EffectStandardRFM standardPaidRFMSmtay = new EffectStandardRFM();
				standardPaidRFMSmtay.setUid(uid);
				standardPaidRFMSmtay.setTimeScope("smtay");
				standardPaidRFMSmtay.setEffectType(1);
				//计算近180 - 360天有交易平均客单价
				EffectStandardRFM standardAvgRFMSmtay = new EffectStandardRFM();
				standardAvgRFMSmtay.setUid(uid);
				standardAvgRFMSmtay.setEffectType(2);
				standardAvgRFMSmtay.setTimeScope("smtay");
				
		Date smtayBTime = DateUtils.addDate(startNodeDate, -360), smtayETime = DateUtils.addDate(startNodeDate, -180);
		this.assembleCusStandardRFM(standardCusRFMSmtay, standardPaidRFMSmtay, standardAvgRFMSmtay, smtayBTime, smtayETime, memberCount, totalPaidFee);
		results.add(standardCusRFMSmtay);
		//计算360天前有交易客户数
		EffectStandardRFM standardCusRFMMtay = new EffectStandardRFM();
		standardCusRFMMtay.setUid(uid);
		standardCusRFMMtay.setTimeScope("mtay");
		standardCusRFMMtay.setEffectType(0);
		//计算360天前有交易消费金额
				EffectStandardRFM standardPaidRFMMtay = new EffectStandardRFM();
				standardPaidRFMMtay.setUid(uid);
				standardPaidRFMMtay.setTimeScope("mtay");
				standardPaidRFMMtay.setEffectType(1);
				//计算360天前有交易平均客单价
				EffectStandardRFM standardAvgRFMMtay = new EffectStandardRFM();
				standardAvgRFMMtay.setUid(uid);
				standardAvgRFMMtay.setEffectType(2);
				standardAvgRFMMtay.setTimeScope("mtay");
				
		Date mtayBTime = DateUtils.addDate(startNodeDate, -360);
		this.assembleCusStandardRFM(standardCusRFMMtay, standardPaidRFMMtay, standardAvgRFMMtay, null, mtayBTime, memberCount, totalPaidFee);
		results.add(standardCusRFMMtay);
		return results;
	}
	
	/**
	 * listPaidFeeRFMs(计算标准RFM中会员累计消费金额的数据)
	 * @Title: listPaidFeeRFMs 
	 * @param @param uid
	 * @param @param startNodeDate
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return List<EffectStandardRFM> 返回类型 
	 * @throws
	 */
	/*public List<EffectStandardRFM> listPaidFeeRFMs(Long uid, Date startNodeDate) throws Exception{
		List<EffectStandardRFM> results = new ArrayList<>();
		
		
		Date ltamBTime = DateUtils.addDate(startNodeDate, -30), ltamETime = startNodeDate;
		this.assemblePaidStandardRFM(standardPaidRFMLtam, ltamBTime, ltamETime, totalPaidFee);
		results.add(standardPaidRFMLtam);
		
		Date amttmBTime = DateUtils.addDate(startNodeDate, -90), amttmETime = DateUtils.addDate(startNodeDate, -30);
		this.assemblePaidStandardRFM(standardPaidRFMAmttm, amttmBTime, amttmETime, totalPaidFee);
		results.add(standardPaidRFMAmttm);
		
		Date tmtsmBTime = DateUtils.addDate(startNodeDate, -180), tmtsmETime = DateUtils.addDate(startNodeDate, -90);
		this.assemblePaidStandardRFM(standardPaidRFMTmtsm, tmtsmBTime, tmtsmETime, totalPaidFee);
		results.add(standardPaidRFMTmtsm);
		
		Date smtayBTime = DateUtils.addDate(startNodeDate, -360), smtayETime = DateUtils.addDate(startNodeDate, -180);
		this.assemblePaidStandardRFM(standardPaidRFMSmtay, smtayBTime, smtayETime, totalPaidFee);
		results.add(standardPaidRFMSmtay);
		
		Date mtayBTime = DateUtils.addDate(startNodeDate, -360);
		this.assemblePaidStandardRFM(standardPaidRFMMtay, null, mtayBTime, totalPaidFee);
		results.add(standardPaidRFMMtay);
		return results;
	}*/
	
	
	public void assembleCusStandardRFM(EffectStandardRFM standardCusRFM,EffectStandardRFM standardPaidRFM,
			EffectStandardRFM standardAvgRFM, Date bTime, Date eTime, Long totalMembers, double totalPaidFee) throws Exception{
		//计算购买一次的会员数
		Long memberAmountOnce = memberDTOService.countMemberAmountByTimes(standardCusRFM.getUid(), 1, bTime, eTime);
		standardCusRFM.setBuyOnceData(memberAmountOnce + "");
		double ratioCusOnce = 0.0;
		if(totalMembers != 0){
			ratioCusOnce = (double)memberAmountOnce / totalMembers;
		}
		standardCusRFM.setBuyOnceRatio(NumberUtils.getFourDouble(ratioCusOnce) * 100 + "%");
		//计算购买二次的会员数
		Long memberAmountTwice = memberDTOService.countMemberAmountByTimes(standardCusRFM.getUid(), 2, bTime, eTime);
		standardCusRFM.setBuyTwiceData(memberAmountTwice + "");
		double ratioCusTwice = 0.0;
		if(totalMembers != 0){
			ratioCusTwice = (double)memberAmountTwice / totalMembers;
		}
		standardCusRFM.setBuyTwiceRatio(NumberUtils.getFourDouble(ratioCusTwice) * 100 + "%");
		//计算购买三次的会员数
		Long memberAmountThrice = memberDTOService.countMemberAmountByTimes(standardCusRFM.getUid(), 3, bTime, eTime);
		standardCusRFM.setBuyThriceData(memberAmountThrice + "");
		double ratioCusThrice = 0.0;
		if(totalMembers != 0){
			ratioCusThrice = (double)memberAmountThrice / totalMembers;
		}
		standardCusRFM.setBuyThriceRatio(NumberUtils.getFourDouble(ratioCusThrice) * 100 + "%");
		//计算购买四次的会员数
		Long memberAmountQuartic = memberDTOService.countMemberAmountByTimes(standardCusRFM.getUid(), 4, bTime, eTime);
		standardCusRFM.setBuyQuarticData(memberAmountQuartic + "");
		double ratioCusQuartic = 0.0;
		if(totalMembers != 0){
			ratioCusQuartic = (double)memberAmountQuartic / totalMembers;
		}
		standardCusRFM.setBuyQuarticRatio(NumberUtils.getFourDouble(ratioCusQuartic) * 100 + "%");
		//计算购买五次的会员数
		Long memberAmountQuintic = memberDTOService.countMemberAmountByTimes(standardCusRFM.getUid(), 5, bTime, eTime);
		standardCusRFM.setBuyQuinticData(memberAmountQuintic + "");
		double ratioCusQuintic = 0.0;
		if(totalMembers != 0){
			ratioCusQuintic = (double)memberAmountQuintic / totalMembers;
		}
		standardCusRFM.setBuyQuinticRatio(NumberUtils.getFourDouble(ratioCusQuintic) * 100 + "%");
		
		//计算购买一次的消费金额
		double paidAmountOnce = memberDTOService.sumPaidAmountByTimes(standardPaidRFM.getUid(), 1, bTime, eTime);
		standardPaidRFM.setBuyOnceData(paidAmountOnce + "");
		double ratioPaidOnce = 0.0;
		if(totalPaidFee != 0.0){
			ratioPaidOnce = paidAmountOnce / totalPaidFee;
		}
		standardPaidRFM.setBuyOnceRatio(NumberUtils.getFourDouble(ratioPaidOnce) * 100 + "%");
		//计算购买二次的消费金额
		double paidAmountTwice = memberDTOService.sumPaidAmountByTimes(standardPaidRFM.getUid(), 2, bTime, eTime);
		standardPaidRFM.setBuyTwiceData(paidAmountTwice + "");
		double ratioPaidTwice = 0.0;
		if(totalPaidFee != 0.0){
			ratioPaidTwice = paidAmountTwice / totalPaidFee;
		}
		standardPaidRFM.setBuyTwiceRatio(NumberUtils.getFourDouble(ratioPaidTwice) * 100 + "%");
		//计算购买三次的消费金额
		double paidAmountThrice = memberDTOService.sumPaidAmountByTimes(standardPaidRFM.getUid(), 3, bTime, eTime);
		standardPaidRFM.setBuyThriceData(paidAmountThrice + "");
		double ratioPaidThrice = 0.0;
		if(totalPaidFee != 0.0){
			ratioPaidThrice = paidAmountThrice / totalPaidFee;
		}
		standardPaidRFM.setBuyThriceRatio(NumberUtils.getFourDouble(ratioPaidThrice) * 100 + "%");
		//计算购买四次的消费金额
		double paidAmountQuartic = memberDTOService.sumPaidAmountByTimes(standardPaidRFM.getUid(), 4, bTime, eTime);
		standardPaidRFM.setBuyQuarticData(paidAmountQuartic + "");
		double ratioPaidQuartic = 0.0;
		if(totalPaidFee != 0.0){
			ratioPaidQuartic = paidAmountQuartic / totalPaidFee;
		}
		standardPaidRFM.setBuyQuarticRatio(NumberUtils.getFourDouble(ratioPaidQuartic) * 100 + "%");
		//计算购买五次的消费金额
		double paidAmountQuintic = memberDTOService.sumPaidAmountByTimes(standardPaidRFM.getUid(), 5, bTime, eTime);
		standardPaidRFM.setBuyQuinticData(paidAmountQuintic + "");
		double ratioPaidQuintic = 0.0;
		if(totalPaidFee != 0.0){
			ratioPaidQuintic = paidAmountQuintic / totalPaidFee;
		}
		standardPaidRFM.setBuyQuinticRatio(NumberUtils.getFourDouble(ratioPaidQuintic) * 100 + "%");
		
		
		/*
		 * 计算平均客单价
		 */
		//购买一次的客单价
		double avgPriceOnce = memberAmountOnce == 0 ? 0.0 : NumberUtils.getTwoDouble(paidAmountOnce / memberAmountOnce);
		//购买二次的客单价
		double avgPriceTwice = memberAmountTwice == 0? 0.0 : NumberUtils.getTwoDouble(paidAmountTwice / memberAmountTwice);
		//购买三次的客单价
		double avgPriceThrice = memberAmountThrice == 0 ? 0.0 : NumberUtils.getTwoDouble(paidAmountThrice / memberAmountThrice);
		//购买四次的客单价
		double avgPriceQuartic = memberAmountQuartic == 0 ? 0.0 : NumberUtils.getTwoDouble(paidAmountQuartic / memberAmountQuartic);
		//购买五次以上的客单价
		double avgPriceQuintic = memberAmountQuintic == 0 ? 0.0 : NumberUtils.getTwoDouble(paidAmountQuintic / memberAmountQuintic);
		//客单价总和(没有实际意思，一点都没，直接加起来算比例)
		double totalAvgPrice = avgPriceOnce + avgPriceTwice + avgPriceThrice + avgPriceQuartic + avgPriceQuintic;
		//购买一次的客单价
		double ratioAvgOnce = 0.0;
		if(totalAvgPrice != 0){
			ratioAvgOnce = avgPriceOnce / totalAvgPrice;
		}
		standardAvgRFM.setBuyOnceData(avgPriceOnce + "");
		standardAvgRFM.setBuyOnceRatio(NumberUtils.getFourDouble(ratioAvgOnce) * 100 + "%");
		//购买二次的客单价
		double ratioAvgTwice = 0.0;
		if(totalAvgPrice != 0){
			ratioAvgTwice = avgPriceTwice / totalAvgPrice;
		}
		standardAvgRFM.setBuyTwiceData(avgPriceTwice + "");
		standardAvgRFM.setBuyTwiceRatio(NumberUtils.getFourDouble(ratioAvgTwice) * 100 + "%");
		//购买三次的客单价
		double ratioAvgThrice = 0.0;
		if(totalAvgPrice != 0){
			ratioAvgThrice = avgPriceThrice / totalAvgPrice;
		}
		standardAvgRFM.setBuyThriceData(avgPriceThrice + "");
		standardAvgRFM.setBuyThriceRatio(NumberUtils.getFourDouble(ratioAvgThrice) * 100 + "%");
		//购买四次的客单价
		double ratioAvgQuartic = 0.0;
		if(totalAvgPrice != 0){
			ratioAvgQuartic = avgPriceQuartic / totalAvgPrice;
		}
		standardAvgRFM.setBuyQuarticData(avgPriceQuartic + "");
		standardAvgRFM.setBuyQuarticRatio(NumberUtils.getFourDouble(ratioAvgQuartic) * 100 + "%");
		//购买五次以上的客单价
		double ratioAvgQuintic = 0.0;
		if(totalAvgPrice != 0){
			ratioAvgQuintic = avgPriceQuintic / totalAvgPrice;
		}
		standardAvgRFM.setBuyQuinticData(avgPriceQuintic + "");
		standardAvgRFM.setBuyQuinticRatio(NumberUtils.getFourDouble(ratioAvgQuintic) * 100 + "%");
	}
	/*public void assemblePaidStandardRFM(
			Date bTime, Date eTime) throws Exception{
		
	}
	*/
	
}
