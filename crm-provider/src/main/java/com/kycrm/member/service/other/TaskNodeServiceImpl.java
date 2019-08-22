package com.kycrm.member.service.other;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.other.ITaskNodeDao;
import com.kycrm.member.domain.entity.other.TaskNode;
@Service("taskNodeService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class TaskNodeServiceImpl implements ITaskNodeService {
	
	@Autowired
	private ITaskNodeDao taskNodeDao;

	/**
	 * 添加一条记录
	 */
	@Override
	public void saveTaskNode(TaskNode taskNode) {
		if(taskNode == null){
			return;
		}
		taskNodeDao.saveTaskNode(taskNode);
	}

	/**
	 * 更新记录
	 */
	@Override
	public void updateTaskNode(TaskNode taskNode) {
		if(taskNode == null){
			return;
		}
		taskNodeDao.updateTaskNode(taskNode);
	}

	/**
	 * 根据类型查询一条记录
	 */
	@Override
	public TaskNode queryTaskNodeByType(String type) {
		if(type != null && !"".equals(type)){
			TaskNode taskNode = taskNodeDao.queryTaskNodeByType(type);
			return taskNode;
		}
		return null;
	}

}
