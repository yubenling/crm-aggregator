package com.kycrm.member.service.rate;

import java.util.Date;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.taobao.api.SecretException;
import com.taobao.api.domain.TradeRate;
import com.taobao.api.response.TraderatesGetResponse;
public interface ITradeRatesServiceSyn {
	
	
	
	
	/**
	 * 获取淘宝中差评并存储
	 * @param userInfo
	 */
	public void getTaobaoRates(Long uid, Date startDate) ;
	
	/**
	 * 判断用户是否开启了中差评监控
	 * 目前此处查询只是沿用行前项目sql，未进行验证
	 * @author sungk
	 * @param uid
	 * @return true为开启
	 */
	public Boolean getRateMoniterStatus(Long uid);
	/**
	 * 拉取评价信息设置拉取条件
	 * @author sungk
	 */
	public void installReq(String result, Long uid, Date startDate);
	
	/**
	 * 保存拉取的淘宝评价信息
	 * @author sungk
	 * 
	 */
	public void saveTradeRates(Long uid, TraderatesGetResponse tgr, String session);
	/**
	 * 修改会员信息的加黑名单
	 * @param uid
	 * @param tradeRate
	 * @param memberInfoDTO
	 */

	public void updateMemberBlackList(Long uid, TradeRate tradeRate,MemberInfoDTO memberInfoDTO);
	
	/**
	 * 分库保存黑名单 评论同步专用
	 * 通过昵称添加
	 * @param uid
	 * @param tradeRate
	 */

	public void saveBlackList(Long uid, TradeRate tradeRate);
	
	/**
	 * 中差评通知安抚逻辑
	 * @param uid
	 */
	public void rateMonitoringPacify(Long uid, MemberInfoDTO memberInfoDTO, String session);
	
	/**
	 * 买家安抚
	 * @param memberId
	 * @param memberInfoDTO
	 */
	public void rateMonitoringPacifySmsBuyer(Long uid, Long memberId, MemberInfoDTO memberInfoDTO, String session);
	
	/**
	 * 卖家通知
	 * @param uid
	 * @param phone
	 */
	public void rateMonitoringPacifySmsSeller(Long uid, String phone, String session);
		
	/**
     * 判断用户是否是过期用户或者短信余额是否不足
     * @param Long uid  用户id
     * @return true 用户是正常用户且短信语言大于0
     */
	public UserInfo isNormalUser(Long uid, UserInfo user);
	
	/**
	 * 获取解密后的数据
	 * @author: wy
	 * @time: 2017年7月27日 上午11:00:40
	 * @param oldData 原始数据，不可以为空
	 * @param type 要解密的类型，不可以空
	 * @param sellerNick 卖家昵称（昵称秘钥二选一）
	 * @param sessionKey 卖家秘钥（昵称秘钥二选一）
	 * @return 
	 * @throws SecretException
	 */
	public String getDecryptData(Long uid, String oldData, String type, String sessionKey) ;

}
