package com.kycrm.member.dao.notice;

import java.util.List;

import com.kycrm.member.domain.entity.notice.Notice;

public interface INoticeDao {

	String findNoticeContent(Long id);

	List<Notice> findNoticeTitle();
}
