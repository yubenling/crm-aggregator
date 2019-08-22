package com.kycrm.member.dao.effect;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.effect.EffectStandardRFM;

public interface IEffectStandardRFMDao {
	

	/**
	 * saveSingleStandardRFM(保存单条数据)
	 * @Title: saveSingleStandardRFM 
	 * @param @param standardRFM 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveSingleStandardRFM(EffectStandardRFM standardRFM);
	
	/**
	 * saveListStandardRFM(批量保存数据)
	 * @Title: saveListStandardRFM 
	 * @param @param map 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveListStandardRFM(Map<String, Object> map);
	
	/**
	 * updateStandartRFM(更新一条数据)
	 * @Title: updateStandartRFM 
	 * @param @param standardRFM 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void updateStandartRFM(EffectStandardRFM standardRFM);
	
	/**
	 * listRFMByEffectType(根据类型查询list)
	 * @Title: listRFMByEffectType 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return EffectStandardRFM 返回类型 
	 * @throws
	 */
	public List<EffectStandardRFM> listRFMByEffectType(Map<String, Object> map);
}
