package com.kycrm.member.service.multishopbinding;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.multishopbinding.IMultiShopBindingSendMessageRecordDao;
import com.kycrm.member.domain.entity.multishopbinding.MultiShopBindingDTO;
import com.kycrm.member.domain.entity.multishopbinding.MultiShopBindingSendMessageRecordDTO;
import com.kycrm.member.domain.utils.pagination.Pagination;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingSendMessageRecordVO;

/**
 * 多店铺绑定-赠送记录服务
 * 
 * @Author ZhengXiaoChen
 * @Date 2019年3月27日上午10:53:42
 * @Tags
 */
@MyDataSource(MyDataSourceAspect.MASTER)
@Service("multiShopBindingSendMessageRecordService")
public class MultiShopBindingSendMessageRecordServiceImpl implements IMultiShopBindingSendMessageRecordService {

	@Autowired
	private IMultiShopBindingService multiShopBindingService;

	@Autowired
	private IMultiShopBindingSendMessageRecordDao multiShopBindingSendMessageRecordDao;

	/**
	 * 分页查询
	 */
	@Override
	public Pagination findSendMessageRecordList(Long uid, String contextPath,
			MultiShopBindingSendMessageRecordVO sendMessageRecordVO) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(5);
		MultiShopBindingDTO multiShopBindingDTO = this.multiShopBindingService
				.findSingleBinding(sendMessageRecordVO.getId());
		Long childShopUid = multiShopBindingDTO.getChildShopUid();
		Long familyShopUid = multiShopBindingDTO.getFamilyShopUid();
		paramMap.put("multiShopBindingId", multiShopBindingDTO.getId());
		if (uid.equals(childShopUid)) {
			paramMap.put("childShopUid", childShopUid);
			paramMap.put("familyShopUid", familyShopUid);
		}
		if (uid.equals(familyShopUid)) {
			paramMap.put("childShopUid", familyShopUid);
			paramMap.put("familyShopUid", childShopUid);
		}
		// 先设置每页显示的条数为5条
		Integer currentRows = 5;
		Integer pageNo = sendMessageRecordVO.getPageNo();
		// 计算出起始行数
		Integer startRows = (pageNo - 1) * currentRows;
		// 计算出总页数
		paramMap.put("startRows", startRows);
		paramMap.put("currentRows", currentRows);
		List<MultiShopBindingSendMessageRecordDTO> multiShopBindingList = this.multiShopBindingSendMessageRecordDao
				.findSendMessageRecordList(paramMap);
		int count = this.findSendMessageRecordCount(paramMap);
		Pagination pagination = new Pagination(pageNo, currentRows, count, multiShopBindingList);
		StringBuilder requestParams = new StringBuilder();
		// 拼接分页的后角标中的跳转路径与查询的条件
		String url = contextPath + "/multiShopBinding/findBindingList?id=" + sendMessageRecordVO.getId();
		pagination.pageView(url, requestParams.toString());
		return pagination;
	}

	/**
	 * 查询数量
	 */
	@Override
	public int findSendMessageRecordCount(Map<String, Object> paramMap) throws Exception {
		return this.multiShopBindingSendMessageRecordDao.findSendMessageRecordCount(paramMap);
	}

	/**
	 * 增加赠送记录
	 */
	@Override
	public Long addSendMessageRecord(MultiShopBindingSendMessageRecordDTO multiShopBindingSendMessageRecordDTO)
			throws Exception {
		return this.multiShopBindingSendMessageRecordDao.addSendMessageRecord(multiShopBindingSendMessageRecordDTO);
	}

	@Override
	public Long findSingleSendCount(Long uid, String dateType, Date bTime, Date eTime) throws Exception {
		return this.multiShopBindingSendMessageRecordDao.findSingleSendCount(uid, dateType, bTime, eTime);
	}

	@Override
	public Map<String, MultiShopBindingSendMessageRecordDTO> findSingleSendCountByDate(Long uid, String dateType,
			Date bTime, Date eTime) throws Exception {
		List<MultiShopBindingSendMessageRecordDTO> sendCountList = this.multiShopBindingSendMessageRecordDao
				.findSingleSendCountByDate(uid, dateType, bTime, eTime);
		Map<String, MultiShopBindingSendMessageRecordDTO> resultMap = new HashMap<String, MultiShopBindingSendMessageRecordDTO>();
		for (int i = 0; i < sendCountList.size(); i++) {
			if ("day".equals(dateType)) {
				resultMap.put(sendCountList.get(i).getSendDateStr(), sendCountList.get(i));
			} else if ("month".equals(dateType)) {
				resultMap.put(sendCountList.get(i).getSendDateStr(), sendCountList.get(i));
			} else {
				resultMap.put(sendCountList.get(i).getSendDateStr(), sendCountList.get(i));
			}
		}
		return resultMap;
	}

	@Override
	public Long findSingleReceiveCount(Long uid, String dateType, Date bTime, Date eTime) throws Exception {
		return this.multiShopBindingSendMessageRecordDao.findSingleReceiveCount(uid, dateType, bTime, eTime);
	}

	@Override
	public Map<String, MultiShopBindingSendMessageRecordDTO> findSingleReceiveCountByDate(Long uid, String dateType,
			Date bTime, Date eTime) throws Exception {
		List<MultiShopBindingSendMessageRecordDTO> receiveCountList = this.multiShopBindingSendMessageRecordDao
				.findSingleReceiveCountByDate(uid, dateType, bTime, eTime);
		Map<String, MultiShopBindingSendMessageRecordDTO> resultMap = new HashMap<String, MultiShopBindingSendMessageRecordDTO>();
		for (int i = 0; i < receiveCountList.size(); i++) {
			if ("day".equals(dateType)) {
				resultMap.put(receiveCountList.get(i).getSendDateStr(), receiveCountList.get(i));
			} else if ("month".equals(dateType)) {
				resultMap.put(receiveCountList.get(i).getSendDateStr(), receiveCountList.get(i));
			} else {
				resultMap.put(receiveCountList.get(i).getSendDateStr(), receiveCountList.get(i));
			}
		}
		return resultMap;
	}

}
