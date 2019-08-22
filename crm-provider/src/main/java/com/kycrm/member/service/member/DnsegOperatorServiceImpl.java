package com.kycrm.member.service.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.member.IDnsegOperatorDTODao;
import com.kycrm.member.domain.entity.member.DnsegOperatorDTO;



@Service("dnsegOperatorService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class DnsegOperatorServiceImpl implements IDnsegOperatorService{
    
	
	@Autowired
	private IDnsegOperatorDTODao dnsegOperatorDTODao;
	@Override
	public List<DnsegOperatorDTO> findAll() {
		return dnsegOperatorDTODao.findAll();
	}

}
