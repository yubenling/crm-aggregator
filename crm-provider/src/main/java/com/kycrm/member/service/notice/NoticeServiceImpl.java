package com.kycrm.member.service.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.notice.INoticeDao;
import com.kycrm.member.domain.entity.notice.Notice;

@Service("noticeService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class NoticeServiceImpl implements INoticeService {
	
	@Autowired
	private INoticeDao noticeDao; 
	
	@Override
	public String findNoticeContent(Long id) {
		return noticeDao.findNoticeContent(id);
	}

	@Override
	public List<Notice> findNoticeTitle() {
		return noticeDao.findNoticeTitle();
	}
	
}
