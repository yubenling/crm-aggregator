package com.kycrm.member.dao.effect;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.effect.RFMDetailChart;

public interface IRFMDetailChartDao {

	public void saveSingleData(RFMDetailChart rfmDetailChart);
	
	
	public void updateSingleData(RFMDetailChart rfmDetailChart);
	
	
	public List<RFMDetailChart> listRFMChartForDistinct(Map<String, Object> map);
	
	
	public RFMDetailChart queryRFMDataForYou(Map<String, Object> map);
}
