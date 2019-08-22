package com.kycrm.member.service.message;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.message.SmsBlackListDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.SmsBlackListVO;


/** 
* @author wy
* @version 创建时间：2018年1月12日 上午11:20:48
*/
public interface ISmsBlackListDTOService {
    /**
     * 查询当前买家是否在买家黑名单中
     * @author: wy
     * @time: 2017年9月1日 下午4:49:08
     * @param sellerNick 卖家昵称
     * @param buyerNick 买家昵称
     * @return true 存在 false 不存在
     */
    public boolean isExists(Long uid,String sellerName,String buyerNick,String buyerPhone) ;

    /**
     * 
     * @author HL
     * @time 2018年3月1日 下午2:15:25 
     * @param uid
     * @return
     */
	public List<String> findBlackPhones(Long uid,UserInfo user);

	/**
	 * 黑名单查询分页数据
	 * @author HL
	 * @time 2018年3月6日 下午2:07:16 
	 * @param vo
	 * @param accessToken
	 * @return
	 */
	public Map<String, Object> findSmsBlackListPage(Long uid,SmsBlackListVO vo,
			String accessToken);

	/**
	 * 删除黑名单数据-----此方法有坑请勿调用！！！
	 * @author HL
	 * @time 2018年3月6日 下午2:56:46 
	 * @param vo
	 * @return
	 */
	public Map<String,Object> deleteSmsBlack(Long uid,SmsBlackListVO vo);

	/**
	 * 添加黑名单数据
	 * @author HL
	 * @time 2018年3月7日 下午4:06:38 
	 * @param list
	 * @return
	 */
	public long insertSmsBlackList(Long uid, Map<String, Object> map);

    /**
     * 判断单个手机号是否在黑名单
     * @Title: phoneIsBlack 
     * @param @param uid
     * @param @param phone
     * @param @return 设定文件 
     * @return boolean 返回类型 
     * @throws
     */
    public boolean phoneIsBlack(Long uid, String phone);
   /**
    * 判断单个昵称是否在黑名单
    * @param uid
    * @param buyerNick
    * @return
    */
    public boolean nickIsBlack(Long uid,String buyerNick);
    /**
     * saveBlackListDTO(保存单条记录)
     * @Title: saveBlackListDTO 
     * @param @param uid
     * @param @param blackListDTO
     * @param @return 设定文件 
     * @return Boolean 返回类型 
     * @throws
     */
	Boolean saveBlackListDTO(Long uid, SmsBlackListDTO blackListDTO);
     /**
      * 查询出昵称黑名单集合
      * @param id
      * @param user
      * @return
      */
	public List<String> findBlackNick(Long uid, UserInfo user);
	
	public void clearAllSmsBlack(Long uid)throws Exception;

}
