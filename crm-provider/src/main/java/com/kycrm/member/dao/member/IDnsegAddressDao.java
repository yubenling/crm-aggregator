package com.kycrm.member.dao.member;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.member.DnsegAddressDTO;

public interface IDnsegAddressDao {

	List<DnsegAddressDTO> findAllbyPage(Map<String, Object> map);

	Long findAllCount();

	List<DnsegAddressDTO> findAll();

}
