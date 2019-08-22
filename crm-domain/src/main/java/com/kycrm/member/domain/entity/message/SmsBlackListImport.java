package com.kycrm.member.domain.entity.message;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * @Table(name = "CRM_BLACKLIST_IMPORT")
 * @MetaData(value = "黑名单导入记录")
 */
public class SmsBlackListImport extends BaseEntity{

	private static final long serialVersionUID = -6865915097570967647L;
       
    /**
	 * @MetaData(value="上传的文件名称")
	 * @Column(name="file_name")
	 */
	 private String fileName;
	 
	 /**   
	 * @MetaData(value="上传的总电话条数")
	 * @Column(name="sum_num")
	 */
	 private Integer sumNum;
	 
	/**   
	 * @MetaData(value="导入成功的电话条数")
	 * @Column(name="success_num")
	 */
	 private Integer successNum;
	   
	/**
	 * @MetaData(value="上传的状态 0--导入完成   1--导入失败  2--导入中")
	 * @Column(name="status")
	 */
	 private Integer status;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getSumNum() {
		return sumNum;
	}

	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}

	public Integer getSuccessNum() {
		return successNum;
	}

	public void setSuccessNum(Integer successNum) {
		this.successNum = successNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
