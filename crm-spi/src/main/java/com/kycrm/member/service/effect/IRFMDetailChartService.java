package com.kycrm.member.service.effect;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.effect.RFMDetailChart;

public interface IRFMDetailChartService {

	List<RFMDetailChart> listRFMChartForDistinct(Long uid, Integer memberType,
			String dateType, Integer dateNum);

	void saveRFMDetailList(Long uid, List<RFMDetailChart> detailCharts);

	Map<String, Object> queryRFMChartMap(Long uid, Integer memberType,
			Integer dateNum, String dateType);

}
