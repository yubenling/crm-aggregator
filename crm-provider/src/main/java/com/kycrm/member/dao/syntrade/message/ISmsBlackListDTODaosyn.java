package com.kycrm.member.dao.syntrade.message;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.message.SmsBlackListDTO;
import com.kycrm.member.domain.vo.message.SmsBlackListVO;

/** 
* @Package com.kycrm.member.dao 
* Copyright: Copyright (c) 2017 
* Company:北京冰点零度科技有限公司 *
* @author wy
* @date 2018年1月12日 下午2:59:27 
* @version V1.0
*/
public interface ISmsBlackListDTODaosyn {
	
	public Integer findExistsByNick(Map<String,Object> map);
	
	public Integer findExistsByPhone(Map<String,Object> map);

	public List<String> findBlackPhones(Long uid);

	public Long findSmsBlackCount(SmsBlackListVO vo);

	public List<SmsBlackListDTO> findSmsBlackList(SmsBlackListVO vo);

	public long deleteSmsBlack(SmsBlackListVO vo);

	public long insertSmsBlackList(Map<String, Object> map);


	/**
	 *   <!--!!!!!!!!!!! 此方法有坑请勿调用!!!!!!!!!!! -->
	 * 通过id查询黑名单昵称     
	 * @author HL
	 * @time 2018年3月8日 上午11:53:07 
	 * @param phoneIds
	 * @return
	 */
	@Deprecated
	public List<String> findSmsBlackListNick(SmsBlackListVO vo);
	
	/**
	 * saveBlackList(保存单条记录)
	 * @Title: saveBlackList 
	 * @param @param blackListDTO 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveBlackList(SmsBlackListDTO blackListDTO);
    /**
     * 查询出该用户的昵称黑名单集合
     * @param uid
     * @return
     */
	public List<String> findBlackNick(Long uid);
}
