package com.heima.schedule.service;

import com.heima.model.schedule.dto.Task;

/**
 * @author: 周海
 * @Create : 2024/3/29
 **/
public interface TaskService {
    /**
     * 添加延迟任务
     *
     * @param task
     * @return
     */
    public Long addTask(Task task);

    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    public boolean cannelTask(Long id);

    /**
     * 拉取任务-->按照类型和优先级拉取任务
     *
     * @return
     */
    public Task poll(int type, int priority);

}
