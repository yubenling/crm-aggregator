package com.kycrm.util;

public class SmsCalculateUtil {

	/**
	 * 获取具体的短信扣费条数
	 * 
	 * @author: wy
	 * @time: 2017年9月4日 下午1:55:24
	 * @param content
	 *            短信的内容
	 * @return 短信条数
	 */
	public static Integer getActualDeduction(String content) {
		if (ValidateUtil.isEmpty(content)) {
			return 0;
		}
		int messageCount = content.length();
		if (content.contains("买家昵称")) {
			messageCount = messageCount + 2;
		}
		if (content.contains("买家姓名")) {
			messageCount = messageCount - 2;
		}
		if (messageCount <= 70) {
			messageCount = 1;
		} else {
			messageCount = (messageCount + 66) / 67;
		}
		return messageCount;
	}
}
