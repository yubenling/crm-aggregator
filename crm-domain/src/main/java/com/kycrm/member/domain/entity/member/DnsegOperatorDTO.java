package com.kycrm.member.domain.entity.member;

import com.kycrm.member.domain.entity.base.BaseEntity;

public class DnsegOperatorDTO extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dnsegP3;
	
    private String operator;


	public String getDnsegP3() {
		return dnsegP3;
	}

	public void setDnsegP3(String dnsegP3) {
		this.dnsegP3 = dnsegP3;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
    
    
}
