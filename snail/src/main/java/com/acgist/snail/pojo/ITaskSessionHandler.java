package com.acgist.snail.pojo;

import com.acgist.snail.pojo.ITaskSession.Status;

/**
 * <p>任务 - 实体操作接口</p>
 * 
 * @author acgist
 */
public interface ITaskSessionHandler {

	/**
	 * <p>更新</p>
	 */
	void update();
	
	/**
	 * <p>删除</p>
	 */
	void delete();
	
	/**
	 * <p>更新状态</p>
	 * 
	 * @param status 任务状态
	 */
	void updateStatus(Status status);
	
}