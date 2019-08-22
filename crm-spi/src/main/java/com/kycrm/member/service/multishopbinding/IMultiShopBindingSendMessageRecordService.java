package com.kycrm.member.service.multishopbinding;

import java.util.Date;
import java.util.Map;

import com.kycrm.member.domain.entity.multishopbinding.MultiShopBindingSendMessageRecordDTO;
import com.kycrm.member.domain.utils.pagination.Pagination;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingSendMessageRecordVO;

public interface IMultiShopBindingSendMessageRecordService {

	public Pagination findSendMessageRecordList(Long uid, String contextPath,
			MultiShopBindingSendMessageRecordVO sendMessageRecordVO) throws Exception;

	public int findSendMessageRecordCount(Map<String, Object> paramMap) throws Exception;

	public Long addSendMessageRecord(MultiShopBindingSendMessageRecordDTO multiShopBindingSendMessageRecordDTO)
			throws Exception;

	public Long findSingleSendCount(Long uid, String dateType, Date bTime, Date eTime) throws Exception;

	public Map<String, MultiShopBindingSendMessageRecordDTO> findSingleSendCountByDate(Long uid, String dateType,
			Date bTime, Date eTime) throws Exception;

	public Long findSingleReceiveCount(Long uid, String dateType, Date bTime, Date eTime) throws Exception;

	public Map<String, MultiShopBindingSendMessageRecordDTO> findSingleReceiveCountByDate(Long uid, String dateType,
			Date bTime, Date eTime) throws Exception;
}
