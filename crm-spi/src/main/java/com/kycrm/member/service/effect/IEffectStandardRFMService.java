package com.kycrm.member.service.effect;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.effect.EffectStandardRFM;
import com.kycrm.member.domain.vo.effect.EffectStandardRFMVO;

public interface IEffectStandardRFMService {

	void saveStandardRFM(EffectStandardRFM standardRFM);

	void saveListStandardRFM(List<EffectStandardRFM> standardRFMs);

	List<EffectStandardRFM> listStandardRFMByType(Long uid, Integer effectType,
			String timeScope);

	/**
	 * sumStandardRFM(计算RFM标准分析页面数据)
	 * @Title: sumStandardRFM 
	 * @param @param uid
	 * @param @param effectType
	 * @param @return 设定文件 
	 * @return EffectStandardRFMVO 返回类型 
	 * @throws
	 */
	EffectStandardRFMVO sumStandardRFM(Long uid, Integer effectType);

	/**
	 * listCustomerRFMs(计算标准RFM中会员数的数据)
	 * @Title: listCustomerRFMs 
	 * @param @param uid
	 * @param @param startNodeDate
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return List<EffectStandardRFM> 返回类型 
	 * @throws
	 */
	List<EffectStandardRFM> listCustomerRFMs(Long uid, Date startNodeDate,
			Long memberCount, Double totalPaidFee)
			throws Exception;

}
