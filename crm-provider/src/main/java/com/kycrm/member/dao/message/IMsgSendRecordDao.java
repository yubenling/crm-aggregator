package com.kycrm.member.dao.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.message.MsgSendRecord;

/** 
* @author wy
* @version 创建时间：2018年1月15日 下午2:36:02
*/
public interface IMsgSendRecordDao {
    /**
     * 根据任务id更新群发短信完成数
     * @author: wy
     * @time: 2018年1月15日 下午2:38:13
     * @param map
     */
    public void updateMsgRecordByMsgId(MsgSendRecord msgRecord);
    
    /**
     * 根据id查询发送总记录
     * @Title: queryRecordById 
     * @param @param msgId
     * @param @return 设定文件 
     * @return MsgSendRecord 返回类型 
     * @throws
     */
    MsgSendRecord queryRecordById(Long msgId);
    /**
	 * 保存总记录并返回id
	 * @author HL
	 * @time 2018年2月26日 下午2:27:55 
	 * @param msg
	 * @return
	 */
	public Long saveMsgSendRecord(MsgSendRecord msg);
    
    /**
     * 根据时间查询发送总记录的list
     * @Title: listMsgId 
     * @param @param map
     * @param @return 设定文件 
     * @return List<Long> 返回类型 
     * @throws
     */
    public List<Long> listMsgId(Map<String,Object> map);
    
    /**
     * 查询所有需要进行效果分析的msgId
     * @Title: findMsgIdByTime 
     * @param @return 设定文件 
     * @return List<Long> 返回类型 
     * @throws
     */
    public List<MsgSendRecord> queryMsgIdByTime(Map<String, Object> map);
    
    /**
     * listMsgRecord(营销中心群发记录)
     * @Title: listMsgRecord 
     * @param @param map
     * @param @return 设定文件 
     * @return List<MsgSendRecord> 返回类型 
     * @throws
     */
    public List<MsgSendRecord> listMsgRecord(Map<String, Object> map);
    
    /**
     * countMsgRecord(营销中心群发记录总数)
     * @Title: countMsgRecord 
     * @param @param map
     * @param @return 设定文件 
     * @return Integer 返回类型 
     * @throws
     */
    public Integer countMsgRecord(Map<String, Object> map);
    
    /**
     * deleteMsgById(更新msg记录isShow为false，不显示)
     * @Title: deleteMsgById 
     * @param @param map
     * @param @return 设定文件 
     * @return Boolean 返回类型 
     * @throws
     */
    public void updateMsgIsShow(Map<String, Object> map) throws Exception;

	public List<MsgSendRecord> queryLastSendList(Map<String, Object> map)throws Exception;

	public void updateMsgRecordForBatchSend(MsgSendRecord msgRecord)throws Exception;
	
	public List<Long> listUidBySendCreate(@Param("bTime") Date bTime,
			@Param("eTime") Date eTime, @Param("maxUid") Long maxUid);
	
	/**
	 * updateMsgROIByid(更新发送记录的ROI)
	 * @Title: updateMsgROIByid 
	 * @param @param map 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void updateMsgROIByid(Map<String, Object> map);

	public void updateMsgBeginSendCreat(Map<String, Object> map);
}
