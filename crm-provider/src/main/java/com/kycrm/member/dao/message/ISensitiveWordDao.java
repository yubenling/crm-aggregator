package com.kycrm.member.dao.message;

import java.util.List;


public interface ISensitiveWordDao {

	List<String> findSensitives();
	
}
