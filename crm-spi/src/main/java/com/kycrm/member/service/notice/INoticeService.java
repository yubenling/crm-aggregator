package com.kycrm.member.service.notice;

import java.util.List;

import com.kycrm.member.domain.entity.notice.Notice;

public interface INoticeService {

	 String findNoticeContent(Long id);

	 List<Notice> findNoticeTitle();
}
