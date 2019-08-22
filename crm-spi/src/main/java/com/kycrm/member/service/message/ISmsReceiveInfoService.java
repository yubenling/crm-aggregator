package com.kycrm.member.service.message;

import java.util.List;

import com.kycrm.member.domain.entity.message.SmsReceiveInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.receive.ReceiveInfoVO;

public interface ISmsReceiveInfoService {

	/**
	 * 查询买家回复消息列表
	 * @Title: listReceiveInfo 
	 * @param @param receiveInfoVO
	 * @param @return 设定文件 
	 * @return List<ReceiveInfoVO> 返回类型 
	 * @throws
	 */
	List<SmsReceiveInfo> listReceiveInfoLimit(Long uid, ReceiveInfoVO receiveInfoVO,UserInfo userInfo);
	
	/**
	 * 查询买家回复消息列表总个数
	 * @Title: countReceiveInfo 
	 * @param @param receiveInfoVO
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	Long countReceiveInfo(Long uid, ReceiveInfoVO receiveInfoVO,UserInfo userInfo);
	
	/**
	 * 根据手机号查询买家回复内容
	 * @Title: listReceiveInfoByPhone 
	 * @param @param uid
	 * @param @param sessionKey
	 * @param @param phone
	 * @param @return 设定文件 
	 * @return List<SmsReceiveInfo> 返回类型 
	 * @throws
	 */
	List<ReceiveInfoVO> listReceiveInfoByPhone(Long uid,String sessionKey,String phone);

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 删除记录
	 * @Date 2018年7月31日下午5:00:00
	 * @param id
	 * @param receiveInfoVO
	 * @return
	 * @ReturnType Integer
	 */
	Boolean deleteRecord(Long id, Long receiveId) throws Exception;

	/**
	 * saveReceiverInfo(添加单条记录)
	 * @Title: saveReceiverInfo 
	 * @param @param receiveInfoVO 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveReceiverInfo(SmsReceiveInfo smsReceiveInfo);
	
}
