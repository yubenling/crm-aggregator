package com.kycrm.member.service.message;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.message.ISensitiveWordDao;
import com.kycrm.util.SensitiveWordUtil;

@Service("sensitiveWordService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class SensitiveWordServiceImpl implements ISensitiveWordService {
	@Autowired
	private ISensitiveWordDao sensitiveWordDao;

	@Override
	public List<String> verifySensitiveWord(String content) {
		List<String> arr = new ArrayList<String>();
		List<String> list = sensitiveWordDao.findSensitives();
		for (String antistop : list) {
			List<String> str = SensitiveWordUtil.contentVerify(content, antistop);
			if(null != str && str.size()>0)
				arr.addAll(str);
		}
		return arr;
	}
}
