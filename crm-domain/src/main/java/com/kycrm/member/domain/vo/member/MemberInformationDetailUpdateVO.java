package com.kycrm.member.domain.vo.member;

import java.io.Serializable;

/**
 * 会员信息 - 客户信息详情 - 修改客户信息详情
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月25日下午4:00:15
 * @Tags
 */
public class MemberInformationDetailUpdateVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月25日下午3:55:37
	 */
	private static final long serialVersionUID = 1L;

	// 分库分表唯一标识
	private Long uid;

	// 会员ID
	private String memberId;

	// 买家邮箱
	private String buyerEmail;

	// 性别
	private String gender;

	// 生日
	private String birthday;

	// 年龄
	private String age;

	// 微信
	private String weChat;

	// QQ
	private String qq;

	// 职业
	private String occupation;

	public MemberInformationDetailUpdateVO() {
		super();

	}

	public MemberInformationDetailUpdateVO(Long uid, String memberId, String buyerEmail, String gender, String birthday,
			String age, String weChat, String qq, String occupation) {
		super();
		this.uid = uid;
		this.memberId = memberId;
		this.buyerEmail = buyerEmail;
		this.gender = gender;
		this.birthday = birthday;
		this.age = age;
		this.weChat = weChat;
		this.qq = qq;
		this.occupation = occupation;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getWeChat() {
		return weChat;
	}

	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	@Override
	public String toString() {
		return "MemberInformationDetailUpdateVO [uid=" + uid + ", memberId=" + memberId + ", buyerEmail=" + buyerEmail
				+ ", gender=" + gender + ", birthday=" + birthday + ", age=" + age + ", weChat=" + weChat + ", qq=" + qq
				+ ", occupation=" + occupation + "]";
	}

}
