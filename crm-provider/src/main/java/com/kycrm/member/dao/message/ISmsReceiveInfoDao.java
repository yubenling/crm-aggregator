package com.kycrm.member.dao.message;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.message.SmsReceiveInfo;

public interface ISmsReceiveInfoDao {

	/**
	 * 买家回复内容查询列表
	 * @Title: listReceiveInfo 
	 * @param @param receiveInfoVO
	 * @param @return 设定文件 
	 * @return List<SmsReceiveInfo> 返回类型 
	 * @throws
	 */
	public List<SmsReceiveInfo> listReceiveInfoLimit(Map<String, Object> map);

	/**
	 * 买家回复内容查询列表个数
	 * countReceiveInfo(这里用一句话描述这个方法的作用)
	 * @Title: countReceiveInfo 
	 * @param @param receiveInfoVO
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long countReceiveInfo(Map<String, Object> map);
	
	
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
	public List<SmsReceiveInfo> listReceiveInfoByPhone(Map<String, Object> map);

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 删除记录
	 * @Date 2018年7月31日下午5:02:30
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @ReturnType Integer
	 */
	public Integer updateReceiveIsShow(Long id)throws Exception;
	
	/**
	 * saveReceiverInfo(添加单条记录)
	 * @Title: saveReceiverInfo 
	 * @param @param receiveInfo 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveReceiverInfo(SmsReceiveInfo receiveInfo);
}
