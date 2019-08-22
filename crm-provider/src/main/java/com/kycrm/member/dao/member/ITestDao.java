
/** 
* @Title: IUserDao.java 
* @Package com.kycrm.member.dao 
* Copyright: Copyright (c) 2017 
* Company:北京冰点零度科技有限公司 *
* @author zlp 
* @date 2018年1月3日 下午2:59:27 
* @version V1.0
*/
   package com.kycrm.member.dao.member;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;

/** 
 * @ClassName: IUserDao  
 * @author zlp 
 * @date 2018年1月3日 下午2:59:27 *  
 */
public interface ITestDao {
	
	
	public String findUserByNick(String sellerNick);
	
	public void batchSaveMemberInfoDTO(Map<String, Object> paramMap)throws Exception;
	
	public List<MemberInfoDTO> getMemberInfoList(Map<String, Object> paramMap)throws Exception;
}
