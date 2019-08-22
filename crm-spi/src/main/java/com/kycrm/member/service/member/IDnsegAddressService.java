package com.kycrm.member.service.member;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.DnsegAddressDTO;

public interface IDnsegAddressService {

	List<DnsegAddressDTO> findAll(Map<String, Object> map);

	Long findAllCount();

	public byte[] findAllByCompress() throws Exception;
}
