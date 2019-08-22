package com.kycrm.member.dao.multishopbinding;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.multishopbinding.MultiShopBindingSendMessageRecordDTO;

public interface IMultiShopBindingSendMessageRecordDao {

	public List<MultiShopBindingSendMessageRecordDTO> findSendMessageRecordList(Map<String, Object> paramMap)
			throws Exception;

	public int findSendMessageRecordCount(Map<String, Object> paramMap) throws Exception;

	public Long addSendMessageRecord(MultiShopBindingSendMessageRecordDTO multiShopBindingSendMessageRecordDTO)
			throws Exception;

	public Long findSingleSendCount(@Param("uid") Long uid, @Param("dateType") String dateType,
			@Param("bTime") Date bTime, @Param("eTime") Date eTime) throws Exception;

	public List<MultiShopBindingSendMessageRecordDTO> findSingleSendCountByDate(@Param("uid") Long uid,
			@Param("dateType") String dateType, @Param("bTime") Date bTime, @Param("eTime") Date eTime)
			throws Exception;

	public Long findSingleReceiveCount(@Param("uid") Long uid, @Param("dateType") String dateType,
			@Param("bTime") Date bTime, @Param("eTime") Date eTime) throws Exception;

	public List<MultiShopBindingSendMessageRecordDTO> findSingleReceiveCountByDate(@Param("uid") Long uid,
			@Param("dateType") String dateType, @Param("bTime") Date bTime, @Param("eTime") Date eTime)
			throws Exception;

}
