package com.kycrm.member.dao.syn;

import java.util.Map;

public interface IRefundDao {
    /**
     * 表是否存在 
     * @param uid  用户id
     * @return
     */
	String isExistsTable(Long uid);
     /**
      * 创建表
      * @param uid 用户id
      */
	void doCreateTableByNewUser(Long uid);
	/**
	 * 判断退款订单是不是存在
	 * @param map
	 * @return
	 */
	Long isExit(Map<String, Object> map);
	/**
	 * 保存退款订单
	 * @param map
	 */
	void saveRefund(Map<String, Object> map);
	/**
	 * 更新退款订单
	 * @param map
	 */
	void updateRefund(Map<String, Object> map);
	/**
	 * 给退款表添加索引
	 * @param uid
	 */
	void addIndex(Long uid);

}
