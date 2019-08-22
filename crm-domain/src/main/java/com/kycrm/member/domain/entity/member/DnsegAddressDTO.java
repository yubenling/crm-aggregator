package com.kycrm.member.domain.entity.member;

import com.kycrm.member.domain.entity.base.BaseEntity;

public class DnsegAddressDTO extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dnsegSeven;
	
	private String province;
	
	private String city;

	public String getDnsegSeven() {
		return dnsegSeven;
	}

	public void setDnsegSeven(String dnsegSeven) {
		this.dnsegSeven = dnsegSeven;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
	

}
