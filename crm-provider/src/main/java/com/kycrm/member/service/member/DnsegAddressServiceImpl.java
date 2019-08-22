package com.kycrm.member.service.member;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.member.IDnsegAddressDao;
import com.kycrm.member.domain.entity.member.DnsegAddressDTO;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;


@Service("dnsegAddressService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class DnsegAddressServiceImpl implements IDnsegAddressService{
 
	
	@Autowired
	private IDnsegAddressDao  dnsegAddressDao;
	
	@Override
	public List<DnsegAddressDTO> findAll(Map<String, Object> map) {
		return dnsegAddressDao.findAllbyPage(map);
	}

	@Override
	public Long findAllCount() {
		return dnsegAddressDao.findAllCount();
	}

	@Override
	public byte[] findAllByCompress() throws Exception {
		List<DnsegAddressDTO> list = dnsegAddressDao.findAll();
		String json = JsonUtil.toJson(list);
		byte[] compress = GzipUtil.compress(json);
		return compress;
	}

}
