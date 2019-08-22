package com.kycrm.syn.dao.member;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.member.MemberInfoDTO;

/**
 * @ClassName: IMemberDTODao
 * @Description 会员dao
 * @author jackstraw_yu
 * @date 2018年2月1日 下午1:45:18
 * 
 */
public interface IMemberDTODao {

	/**
	 * 自动创建用户表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:46:14
	 * @param uid
	 *            用户表主键id
	 */
	public void doCreateTableByNewUser(Long uid);

	public void doCreateMemberReceiveDetailTableByNewUser(Long uid);

	public void doCreateTradeRatesTableByNewUser(Long uid);

	/**
	 * @Description 会员添加索引
	 * @param uid
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月5日 下午6:19:45
	 */
	public void addMemberTableIndex(Long uid);

	/**
	 * 是否存在短信记录表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:48:10
	 * @param uid
	 * @return
	 */
	public List<String> isExistsTable(Long uid);

	public List<String> isExistsMemberReceiveDetailTable(Long uid);

	public List<String> isExistsTradeRatesTable(Long uid);

	/**
	 * 是否存在卖家的会员购买统计表
	 * 
	 * @param uid
	 * @return
	 */
	public List<String> isExistsMemberItemAmountTable(String uid);

	public List<String> isExistsSmsBlacklistTable(String uid);

	public List<String> isExistsTradeRatesTable(String uid);

	/**
	 * 自动创建卖家的会员购买统计表
	 * 
	 * @param uid
	 */
	public void doCreateMemberItemAmountTableByNewUser(String uid);

	public void doCreateSmsBlacklistTableByNewUser(String uid);

	public void doCreateTradeRatesTableByNewUser(String uid);

	/**
	 * @Description 查询会员是否存在
	 * @param member
	 *            设定文件
	 * @return MemberInfoDTO 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月1日 上午10:28:08
	 */
	public int queryMemberByBuyerNick(Map<String, Object> map);

	/**
	 * @Description 批量保存同步会员信息
	 * @param entity
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月1日 上午10:53:25
	 */
	public void batchSaveMemberData(
			Map<String, Object> map/* BaseListEntity<MemberInfoDTO> entity */);

	/**
	 * @Description 批量更新同步会员信息
	 * @param param
	 *            设定文件
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月1日 上午10:54:07
	 */
	public void batchUpdateMemberData(
			Map<String, Object> map/* BaseListEntity<MemberInfoDTO> entity */);

	/**
	 * 根据手机号查询会员
	 * 
	 * @param uid
	 * @param mobile
	 * @return
	 */

	public MemberInfoDTO findMemberInfo(Map<String, Object> map);

	public int updateMembeInfo(MemberInfoDTO member);

	public void addMemberReceiveDetailTableIndex(Long uid);

	public void addTradeRatesTableIndex(Long uid);

	public void addSmsBlacklistTableIndex(String userId);

	public void addMemberItemAmountTableIndex(String userId);

	public void updateLastMarketingTime(@Param("uid") Long uid);

	public List<String> isExistsPremiumFilterRecordTable(@Param("uid") Long uid) throws Exception;

	public void doCreatePremiumFilterRecordTable(@Param("uid") Long uid) throws Exception;

	public void batchUpdateMemberNum(Map<String, Object> map);

	public Long getCountByCondition(@Param("uid") Long uid, @Param("endDate") Date endDate,
			@Param("column") String column) throws Exception;

	public void batchUpdate(@Param("uid") Long uid, @Param("column") String column,
			@Param("memberInfoList") List<MemberInfoDTO> memberInfoList) throws Exception;

	public List<MemberInfoDTO> getBuyerNickList(@Param("uid") Long uid, @Param("column") String column,
			@Param("startPoint") Integer startPoint, @Param("range") Integer range) throws Exception;

	public void batchUpdateMember(@Param("uid") Long uid, @Param("memberInfoList") List<MemberInfoDTO> memberInfoList)
			throws Exception;

	public int getFilterRecordCount(@Param("uid") Long uid) throws Exception;

	public void dropTable(@Param("uid") Long uid) throws Exception;

	public void batchUpdateRerunColumn(@Param("uid") Long uid,
			@Param("memberInfoList") List<MemberInfoDTO> memberInfoList) throws Exception;

	public List<MemberInfoDTO> getBuyerNickListByLimitId(@Param("uid") Long uid, @Param("limitId") Long limitId,
			@Param("startPoint") Integer startPosition, @Param("range") Integer range) throws Exception;

	/**
	 * 更新会员表字段长度
	 * 
	 * @param map
	 */
	public void alterTableMember(Map<String, Object> map);

	/**
	 * 修改用户的信息
	 * 
	 * @param memberInfo
	 * @return
	 */
	public int updateMemberInfoNum(MemberInfoDTO memberInfo);

	/**
	 * 修改平均客单价
	 * 
	 * @param map
	 */
	public void updateMemberAvg(Map<String, Object> map);

	public Long getCount(@Param("uid") Long uid)throws Exception;

}
