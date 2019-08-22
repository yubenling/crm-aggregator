package com.kycrm.member.dao.effect;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.effect.MarketingCenterEffect;

public interface IMarketingCenterEffectDao {
	
	/**
	 * tableIsExist(该用户的表是否存在)
	 * @Title: tableIsExist 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return List<String> 返回类型 
	 * @throws
	 */
	public List<String> tableIsExist(@Param("uid") Long uid);
	
	/**
	 * doCreateTable(创建该用户的表)
	 * @Title: doCreateTable 
	 * @param @param uid 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void doCreateTable(Long uid);
	
	/**
	 * 保存一条记录
	 * @Title: saveMarktingCenterEffect 
	 * @param @param effectPicture 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void saveMarktingCenterEffect(MarketingCenterEffect effect);

	/**
	 * 根据条件更新一条记录
	 * @Title: updateMarktingEffectByParam 
	 * @param @param effectPicture 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void updateMarktingEffectByParam(MarketingCenterEffect effect);
	
	/**
	 * 根据userId、msgId、days查询统计分析数据
	 * @Title: findEffectByParam 
	 * @param @param effectPicture
	 * @param @return 设定文件 
	 * @return MarketingCenterEffect 返回类型 
	 * @throws
	 */
	MarketingCenterEffect findEffectByParam(Map<String, Object> map);
	
	/**
	 * 营销中心效果分析汇总数据
	 * @Title: findEffectByDays 
	 * @param @param effectPicture
	 * @param @return 设定文件 
	 * @return EffectPicture 返回类型 
	 * @throws
	 */
	MarketingCenterEffect findEffectByDays(Map<String, Object> map);
	
	/**
	 * 营销中心效果分析汇总数据真实客户数据
	 * @Title: findRealBuyerNum 
	 * @param @return 设定文件 
	 * @return EffectPicture 返回类型 
	 * @throws
	 */
	MarketingCenterEffect findRealBuyerNum();
	
	/**
	 * 营销中心效果分析每日数据的集合
	 * @Title: listEffectPictures 
	 * @param @return 设定文件 
	 * @return List<EffectPicture> 返回类型 
	 * @throws
	 */
	List<MarketingCenterEffect> listEffectPictures(Map<String, Object> map);
	
	/**
	 * 根据时间计算营销回款金额
	 * @Title: findSuccessPayFeeByTime 
	 * @param @return 设定文件 
	 * @return Double 返回类型 
	 * @throws
	 */
	Double findSuccessPayFeeByTime(Map<String, Object> map);
	
	void addMarketingCenterEffectTableIndex(Long uid);
}
