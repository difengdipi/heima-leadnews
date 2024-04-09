package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSON;
import com.heima.apis.schedule.IScheduleClient;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.TaskTypeEnum;
import com.heima.model.schedule.dto.Task;
import com.heima.model.wemedia.pojos.WmNews;
import com.heima.utils.common.JdkSerializeUtil;
import com.heima.wemedia.service.WeNewsAutoScanService;
import com.heima.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: 周海
 * @Create : 2024/3/30
 **/
@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {

    @Autowired
    private IScheduleClient scheduleClient;

    /**
     * 添加任务到延迟队列中
     *
     * @param id          文章的id
     * @param publishTime 发布的时间
     */
    @Async
    public void addnews(Integer id, Date publishTime) {
        log.info("添加任务到延迟服务中-------begin");
        Task task = new Task();
        task.setExecuteTime(publishTime.getTime());
        task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
        task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        WmNews wmNews = new WmNews();
        wmNews.setId(id);
        task.setParameters(JdkSerializeUtil.serialize(wmNews));

        scheduleClient.addTask(task);
        log.info("添加任务到延迟服务中-------end");
    }

    @Autowired
    private WeNewsAutoScanService WeNewsAutoScanService;

    /**
     * 消费任务审核文章
     */
    @Override
    @Scheduled(fixedRate = 1000)//固定频率拉取任务
    public void scanNewsByTask() {
        //消费任务 审核文章
        log.info("消费任务----审核文章");
        ResponseResult responseResult = scheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        if (responseResult.getCode().equals(200) && responseResult.getData() != null) {
            Task task = JSON.parseObject(JSON.toJSONString(responseResult.getData()), Task.class);
            //序列化工具
            WmNews wmNews = JdkSerializeUtil.deserialize(task.getParameters(), WmNews.class);
            WeNewsAutoScanService.autoWmnews(wmNews.getId());
        }
    }
}
