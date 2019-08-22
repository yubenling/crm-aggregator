package com.kycrm.member.dao.multishopbinding;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.multishopbinding.MultiShopBindingDTO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingConfirmVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingDeleteRecordVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingReleaseBindingVO;

public interface IMultiShopBindingDao {

	public List<MultiShopBindingDTO> findBindingList(Map<String, Object> paramMap) throws Exception;

	public MultiShopBindingDTO findSingleBinding(@Param("id") Long id) throws Exception;

	public int findMultiShopBindingCount(@Param("uid") Long uid, @Param("menuNumber") Integer menuNumber)
			throws Exception;

	public Long addApplyBinding(MultiShopBindingDTO multiShopBindingDTO) throws Exception;

	public Long releaseBinding(MultiShopBindingReleaseBindingVO releaseBindingVO) throws Exception;

	public MultiShopBindingDTO findMultiShopBinding(@Param("id") Long id) throws Exception;

	public Long deleteRecord(MultiShopBindingDeleteRecordVO deleteRecord) throws Exception;

	public Long updateMultiShopBinding(MultiShopBindingDTO multiShopBindingDTO) throws Exception;

	public Long confirm(MultiShopBindingConfirmVO confirmVO) throws Exception;

	public Long isAlreadyBinded(@Param("childShopUid") Long childShopUid, @Param("familyShopUid") Long familyShopUid)
			throws Exception;

	public int checkIfExist(@Param("childShopName") String childShopName,
			@Param("familyShopName") String familyShopName, @Param("bindingStatusArray") Integer[] bindingStatusArray)
			throws Exception;

}
