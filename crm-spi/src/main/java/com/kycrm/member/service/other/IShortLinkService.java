package com.kycrm.member.service.other;

import java.util.Date;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.domain.entity.message.SmsSendInfo;

public interface IShortLinkService {

	/**
	 * 订单中心获取转换后的短链接
	 * 
	 * @author: wy
	 * @time: 2017年10月24日 下午6:28:58
	 * @param sellerNick
	 *            买家昵称
	 * @param tid
	 *            主订单号
	 * @param type
	 *            类型
	 * @param taskId
	 *            任务id
	 * @param sendDate
	 *            发送时间
	 * @return 转换后的短链接加映射主键id
	 */
	String getConvertLinkByTmcTradeCenter(long uid, String sellerNick, Long tid, String type, Long taskId,
			Date sendDate, SmsSendInfo smsSendInfo);

	/**
	 * 营销中心获取转换后的短链接
	 * 
	 * @author: wy
	 * @time: 2017年10月24日 下午6:28:58
	 * @param tid
	 *            主订单号
	 * @param type
	 *            类型
	 * @param taskId
	 *            任务id
	 * @param sendDate
	 *            发送时间
	 * @return 转换后的短链接加映射主键id
	 */
	public Map<String, Object> getConvertLinkByMarketingCenter(long uid, String type, Date sendDate,
			String taoBaoShortLink) throws Exception;

	/**
	 * 根据用户、类型和所选定的时间查出对应的短链接记录统计
	 * 
	 * @author: wy
	 * @time: 2017年10月26日 下午12:03:08
	 * @param sellerNick
	 *            卖家昵称
	 * @param type
	 *            类型
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return map集合 <br>
	 *         key = pageClickNum , value = 链接总点击次数<br>
	 *         key = customerClickNum , value = 用户总点击次数<br>
	 *         key = customerClickRate , value = 客户点击率
	 */
	JSONObject getAllEffect(long uid, String type, Long taskId, Date startTime, Date endTime);

	/**
	 * 根据主键ID删除链接映射关系
	 * 
	 * @author: wy
	 * @time: 2017年10月25日 下午6:02:32
	 * @param id
	 *            主键ID
	 * @return 删除成功返回true 删除失败返回false
	 */
	boolean doRemoveById(Long id);

	JSONObject showCustomerClickDetail(Long uid, String type, Long taskId, Long startTimeLong, Long endTimeLong,
			Integer page, Integer lineSize, String isUserPage);

}
