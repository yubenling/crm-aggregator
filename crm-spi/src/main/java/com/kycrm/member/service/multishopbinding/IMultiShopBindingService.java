package com.kycrm.member.service.multishopbinding;

import com.kycrm.member.domain.entity.multishopbinding.MultiShopBindingDTO;
import com.kycrm.member.domain.utils.pagination.Pagination;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingDeleteRecordVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingReleaseBindingVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingApplyVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingConfirmVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingSendMessageVO;

/**
 * 多店铺绑定Service
 * 
 * @Author ZhengXiaoChen
 * @Date 2019年3月26日下午2:17:15
 * @Tags
 */
public interface IMultiShopBindingService {

	public Pagination findBindingList(Long uid, Integer menuNumber, String contextPath, Integer pageNo)
			throws Exception;

	public int findBindingListCount(Long uid, Integer menuNumber) throws Exception;

	public Long applyBinding(MultiShopBindingApplyVO multiShopBindingVO) throws Exception;

	public Long releaseBinding(MultiShopBindingReleaseBindingVO releaseBindingVO) throws Exception;

	public Long confirm(MultiShopBindingConfirmVO confirmVO) throws Exception;

	public Long sendMessage(MultiShopBindingSendMessageVO sendMessageVO) throws Exception;

	public Long deleteRecord(MultiShopBindingDeleteRecordVO deleteRecord) throws Exception;

	public MultiShopBindingDTO findSingleBinding(Long id) throws Exception;

	public boolean checkIfExist(String childShopName, String familyShopName, Integer[] bindingStatusArray)
			throws Exception;

}
