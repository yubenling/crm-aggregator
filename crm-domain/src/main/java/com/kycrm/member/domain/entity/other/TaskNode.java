package com.kycrm.member.domain.entity.other;

import java.io.Serializable;
import java.util.Date;


public class TaskNode implements Serializable{

	private static final long serialVersionUID = 1L;

	//订单同步全局任务节点
	private Integer taskNode;
	
	//订单同步时间任务节点
	private Date taskEndTime;
	
	//订单同步类型
	private String type;

	public Integer getTaskNode() {
		return taskNode;
	}

	public void setTaskNode(Integer taskNode) {
		this.taskNode = taskNode;
	}

	public Date getTaskEndTime() {
		return taskEndTime;
	}

	public void setTaskEndTime(Date taskEndTime) {
		this.taskEndTime = taskEndTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
