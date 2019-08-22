package com.kycrm.member.service.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.message.ISmsBlackListImportDao;
import com.kycrm.member.domain.entity.message.SmsBlackListImport;


@Service("smsBlackListImportService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class SmsBlackListImportServiceImpl implements ISmsBlackListImportService{

	@Autowired
	private ISmsBlackListImportDao smsBlackListImportDao;
	public Long insertSmsBlackListImport(SmsBlackListImport blackListImport) {
		smsBlackListImportDao.insertSmsBlackListImport(blackListImport);
		return blackListImport.getId();
	}
	@Override
	public void updateSmsBlackListImport(Map<String, Object> map) {
		smsBlackListImportDao.updateSmsBlackListImport(map);
	}
	@Override
	public Map<String, Object> findSmsBlackListImport(Long uid) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SmsBlackListImport> list = smsBlackListImportDao.findSmsBlackListImport(uid);
		map.put("list", list);
		return map;
	}
	@Override
	public boolean deleteSmsBlackImport(Long id) {
		try {
			long i = smsBlackListImportDao.deleteSmsBlackImport(id);
			if(i>0){
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}
}
