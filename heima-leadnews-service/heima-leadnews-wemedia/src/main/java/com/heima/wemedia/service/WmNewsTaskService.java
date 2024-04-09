package com.heima.wemedia.service;

import java.util.Date;

/**
 * @author: 周海
 * @Create : 2024/3/30
 **/
public interface WmNewsTaskService {
    /**
     * 添加任务到延迟队列中
     *
     * @param id          文章的id
     * @param publishTime 发布的时间
     */
    public void addnews(Integer id, Date publishTime);

    /**
     * 消费任务审核文章
     */
    public void scanNewsByTask();
}
