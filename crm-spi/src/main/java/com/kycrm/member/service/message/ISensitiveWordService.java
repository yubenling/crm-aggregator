package com.kycrm.member.service.message;

import java.util.List;


/**
 * 关键词屏蔽服务
 */
public interface ISensitiveWordService {

	List<String> verifySensitiveWord(String content);
	
}
