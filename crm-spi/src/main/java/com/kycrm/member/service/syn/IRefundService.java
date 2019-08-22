package com.kycrm.member.service.syn;

import java.util.List;

import com.kycrm.member.domain.entity.refund.RefundDTO;

public interface IRefundService {
    /**
     * 保存退款
     * @param saveList
     */
	void saveRefund(Long uid,List<RefundDTO> saveList);
    /**
     * 更新退款
     * @param updatelist
     */
	void updateRefund(Long uid,List<RefundDTO> updatelist);
    /**
     * 创建表
     * @param uid
     */
	void doCreateTableByNewUser(Long uid);
	/**
	 * 查询是否存在
	 * @param refundId
	 * @return
	 */
	boolean isExit(Long uid,Long refundId);
}
