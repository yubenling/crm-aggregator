package com.kycrm.member.service.user;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import com.kycrm.member.core.DataSource;
import com.kycrm.member.core.DataSourceAspect;
import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.user.IUserPayBillDao;
import com.kycrm.member.domain.entity.user.UserBillRecord;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserPayBill;
import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.domain.vo.user.UserBillRecordVO;
import com.kycrm.member.domain.vo.user.UserPayBillVO;
import com.kycrm.util.IdUtils;


@MyDataSource(MyDataSourceAspect.MASTER)
@Service("userPayBillService")
public class UserPayBillServiceImpl implements IUserPayBillService{
	
	@Autowired
	private IUserPayBillDao userPayBillDao;

	
	@Override
	public List<UserPayBill> selectPayBillList(UserPayBillVO userPayBillVO,
			UserInfo user) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", user.getId());
		map.put("payStartTime", userPayBillVO.getPayStartTime());
		map.put("payEndTime", userPayBillVO.getPayEndTime());
		map.put("billStatus", userPayBillVO.getBillStatus());
		map.put("startRows", (userPayBillVO.getPageNo()-1)*userPayBillVO.getCurrentRows());
		map.put("pageSize", userPayBillVO.getCurrentRows());
		return userPayBillDao.selectPayBillList(map);
	}


	@Override
	public List<UserPayBill> findAllPayBillByStatus(Long uid,String status) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("status", status);
		return userPayBillDao.findAllPayBillByStatus(map);
	}


	@Override
	public List<UserPayBill> findPayBillByOrderId(UserInfo user,
			String orderIdList) {
		//将orderIdList装换为集合
		List<String> orderList=null;
		try {
			 orderList=Arrays.asList(orderIdList.split(","));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("uid", user.getId());
		map.put("list", orderList);
		return userPayBillDao.findPayBillByOrderId(map);
	}


	@Override
	public void updatePayBillStatus(List<UserPayBill> payBillList) {
		//单个去更新
		for(UserPayBill bill:payBillList){
			userPayBillDao.updatePayBillStatus(bill);
		}	
	}

    /**
     * 根据发票的id,更改打款记录的状态
     */
	@Override
	public void updatePayBillStatusByRecord(UserInfo user,UserBillRecord userBillRecord) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("billRecordId", userBillRecord.getId());
		map.put("uid", user.getId());
		List<UserPayBill> list=userPayBillDao.selectPayBillByRecord(map);
		for(UserPayBill upb:list){
			//修改打款记录的状态为未打款，并且打款记录id为空
			upb.setBillStatus(0);
			userPayBillDao.updatePayBillStatus(upb);
		}
	}

    /**
     * 根据发票记录分页查询打款记录
     * @return 
     */
	@Override
	public List<UserPayBill> selectPayBillByRecordId(UserInfo user,UserBillRecordVO userBillRecord) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("billRecordId", userBillRecord.getId());
		map.put("uid", user.getId());
		map.put("startRows", (userBillRecord.getPageNo()-1)*userBillRecord.getCurrentRows());
		map.put("pageSize", userBillRecord.getCurrentRows());
		return userPayBillDao.selectPayBillByRecord(map);
		
	}


	@Override
	public BigDecimal selectAllPayBillAmont(UserInfo user) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("uid", user.getId());
		//查询出打款记录状态为0的
		map.put("billStatus", 0);
		return userPayBillDao.selectAllPayBillAmont(map);
	}
    /**
     * 生成打款表
     */
    
	@Override
	public Long insertPayBill(UserRecharge ur) {
		UserPayBill userPayBill =new UserPayBill();
		userPayBill.setPayAmount(BigDecimal.valueOf(ur.getRechargePrice()));
		userPayBill.setOrderId(IdUtils.getTradeId());
		userPayBill.setBillStatus(0);
		userPayBill.setRemark(ur.getRemarks());
		userPayBill.setUid(ur.getUid());
		userPayBill.setIsFinish(0);
		//线上打款
		userPayBill.setType("线上打款");
		userPayBill.setCreatedBy(ur.getCreatedBy());
		userPayBill.setLastModifiedBy(ur.getLastModifiedBy());
		userPayBillDao.insertPayBill(userPayBill);
		return userPayBill.getId();
	}


	@Override
	public Integer selectPayBillListCount(UserPayBillVO userPayBillVO,
			UserInfo user) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", user.getId());
		map.put("payStartTime", userPayBillVO.getPayStartTime());
		map.put("payEndTime", userPayBillVO.getPayEndTime());
		map.put("billStatus", userPayBillVO.getBillStatus());
		return userPayBillDao.selectPayBillListCount(map);
	}


	@Override
	public Integer selectPayBillByRecordIdCount(UserInfo user,
			UserBillRecordVO userBillRecord) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("billRecordId", userBillRecord.getId());
		map.put("uid", user.getId());
		return userPayBillDao.selectPayBillByRecordIdCount(map);
	}


	

  
}
