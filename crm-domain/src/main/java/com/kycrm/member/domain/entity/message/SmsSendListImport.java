package com.kycrm.member.domain.entity.message;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * @Table(name = "CRM_SMS_SENDLIST_IMPORT")
 * @MetaData(value = "发送名单导入")
 */
public class SmsSendListImport extends BaseEntity{
	private static final long serialVersionUID = -6865915097570967647L;
	
    /**
	 * @MetaData(value="上传的文件名称")
	 * @Column(name="file_name")
	 */
	 private String fileName;
	 
    /**   
	 * @MetaData(value="上传的总电话条数")
	 * @Column(name="send_number")
	 */
	 private Integer sendNumber;
	 
	/**   
	 * @MetaData(value="导入成功的电话条数")
	 * @Column(name="success_number")
	 */
	 private Integer successNumber;
	 
	/**   
	 * @MetaData(value="导入失败的电话条数")
	 * @Column(name="error_number")
	 */
	 private Integer errorNumber;
	
	 /**   
	 * @MetaData(value="导入重复的电话条数")
	 * @Column(name="repetition_number")
	 */
	 private Integer repetitionNumber;
	 
	/**   
	 * @MetaData(value="导入的电话号码")
	 * @Column(name="import_phone")
	 */
	 private String importPhone;
	 
	/**
	 * @MetaData(value="导入的电话号码")
	 * @Column(name="import_phone_byte")
	 */
	 private byte[] importPhoneByte;

	 /**
	 * @MetaData(value="上传的状态 0--导入完成   1--导入失败  2--导入中")
	 * @Column(name="state")
	 */
	 private Integer state;

	public byte[] getImportPhoneByte() {
		return importPhoneByte;
	}

	public void setImportPhoneByte(byte[] importPhoneByte) {
		this.importPhoneByte = importPhoneByte;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getSendNumber() {
		return sendNumber;
	}

	public void setSendNumber(Integer sendNumber) {
		this.sendNumber = sendNumber;
	}

	public Integer getSuccessNumber() {
		return successNumber;
	}

	public void setSuccessNumber(Integer successNumber) {
		this.successNumber = successNumber;
	}

	public Integer getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(Integer errorNumber) {
		this.errorNumber = errorNumber;
	}

	public Integer getRepetitionNumber() {
		return repetitionNumber;
	}

	public void setRepetitionNumber(Integer repetitionNumber) {
		this.repetitionNumber = repetitionNumber;
	}

	public String getImportPhone() {
		return importPhone;
	}

	public void setImportPhone(String importPhone) {
		this.importPhone = importPhone;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}