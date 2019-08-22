package com.kycrm.member.service.vip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.vip.IVipUserDao;


@Service("vipUserService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class VipUserServiceImpl implements IVipUserService {
	
	@Autowired
	private IVipUserDao vipUserDao;

	/**
	 * 查询该用户是否是vip
	 */
	@Override
	public boolean findVipUserIfExist(Long uid){
		try {
			Integer count = this.vipUserDao.findVipUserIfExist(uid);
			if(count>0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
