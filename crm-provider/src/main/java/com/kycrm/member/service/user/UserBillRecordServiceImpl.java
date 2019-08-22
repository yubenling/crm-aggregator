package com.kycrm.member.service.user;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.user.IUserBillRecordDao;
import com.kycrm.member.domain.entity.user.UserBillRecord;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserPayBill;
import com.kycrm.member.domain.vo.user.UserBillRecordVO;

@Service("userBillRecordService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class UserBillRecordServiceImpl implements IUserBillRecordService{
	
	@Autowired
	private IUserBillRecordDao userBillRecordDao;

	@Override
	public List<UserBillRecordVO> selectBillRecordList(UserInfo user,
			UserBillRecordVO userBillRecordVo) {
		userBillRecordVo.setUid(user.getId());
		userBillRecordVo.setStartRows((userBillRecordVo.getPageNo()-1)*userBillRecordVo.getCurrentRows());
		return userBillRecordDao.selectBillRecordList(userBillRecordVo);
	}

	@Override
	public Long saveBillRecord(List<UserPayBill> payBillList,UserBillRecordVO userBillRecordVO) {
		//封装为UserBillRecord对象
		UserBillRecord userBillRecord=getUserBillRecord(payBillList,userBillRecordVO);
		userBillRecordDao.saveBillRecord(userBillRecord);
		return userBillRecord.getId();
	}

	private UserBillRecord getUserBillRecord(List<UserPayBill> payBillList,
			UserBillRecordVO userBillRecordVO) {
		UserBillRecord userBillRecord=new UserBillRecord();
		BigDecimal totalAmount=new BigDecimal("0");
		for(UserPayBill userPayBill:payBillList){
			totalAmount.add(userPayBill.getPayAmount());
		}
		userBillRecord.setUid(userBillRecordVO.getUid());
		userBillRecord.setCreatedBy(userBillRecordVO.getUserName());
		userBillRecord.setLastModifiedBy(userBillRecordVO.getUserName());
		userBillRecord.setApplyPrice(totalAmount);
		userBillRecord.setApplyStatus(1);
		userBillRecord.setBillHead(userBillRecordVO.getBillHead());
		userBillRecord.setBillType(userBillRecordVO.getBillType());
		userBillRecord.setCompanyBank(userBillRecordVO.getCompanyBank());
		userBillRecord.setCompanyCard(userBillRecordVO.getCompanyCard());
		userBillRecord.setCompanyPhone(userBillRecordVO.getCompanyPhone());
		userBillRecord.setCompanyDutyNum(userBillRecordVO.getCompanyDutyNum());
		userBillRecord.setRegisterAddress(userBillRecordVO.getRegisterAddress());
		userBillRecord.setReceiverAddress(userBillRecordVO.getReceiverAddress());
		userBillRecord.setReceiverPhone(userBillRecordVO.getReceiverPhone());
		userBillRecord.setReceiverName(userBillRecordVO.getReceiverName());
		userBillRecord.setApplyPrice(userBillRecordVO.getApplyPrice());
		return userBillRecord;
	}
    /**
     * 根据recordId修改发票记录的状态
     */
	@Override
	public void updateBillRecordStatus(UserInfo user,UserBillRecord userBillRecord) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id", userBillRecord.getId());
		map.put("applyStatus", userBillRecord.getApplyStatus());
		userBillRecordDao.updateBillRecordStatus(map);
	}

	@Override
	public void updateRecordBillInfo(UserBillRecordVO userBillRecord) {
		userBillRecordDao.updateRecordBillInfo(userBillRecord);
	}
    /**
     * 根据发票记录id,删除发票记录
     */
	@Override
	public Integer deleteBillRecordById(Long id) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		return userBillRecordDao.deleteBillRecordById(map);
	}
    /**
     * 根据发票记录id,查询发票记录
     */
	@Override
	public UserBillRecord selectBillRecordById(Long id) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("id", id);
		return userBillRecordDao.selectBillRecordById(map);
	}
    /**
     * 查询发票订单总条数
     */
	@Override
	public Integer selectBillRecordListCount(UserInfo user,
			UserBillRecordVO userBillRecordVo) {
		userBillRecordVo.setUid(user.getId());
		return userBillRecordDao.selectBillRecordListCount(userBillRecordVo);
	}

	

}
