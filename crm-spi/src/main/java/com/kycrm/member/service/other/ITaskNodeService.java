package com.kycrm.member.service.other;

import com.kycrm.member.domain.entity.other.TaskNode;

public interface ITaskNodeService {

	/**
	 * 添加一条记录
	 * @Title: saveTaskNode 
	 * @param @param taskNode 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void saveTaskNode(TaskNode taskNode);
	
	/**
	 * 更新记录
	 * @Title: updateTaskNode 
	 * @param @param taskNode 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void updateTaskNode(TaskNode taskNode);
	
	/**
	 * 根据类型查询一条记录
	 * @Title: queryTaskNodeByType 
	 * @param @param type
	 * @param @return 设定文件 
	 * @return TaskNode 返回类型 
	 * @throws
	 */
	TaskNode queryTaskNodeByType(String type);
}
