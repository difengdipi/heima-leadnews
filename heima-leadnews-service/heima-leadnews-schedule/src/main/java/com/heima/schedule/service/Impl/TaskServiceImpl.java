package com.heima.schedule.service.Impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.common.constants.ScheduleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.schedule.dto.Task;
import com.heima.model.schedule.pojos.Taskinfo;
import com.heima.model.schedule.pojos.TaskinfoLogs;
import com.heima.schedule.mapper.TaskinfoLogsMapper;
import com.heima.schedule.mapper.TaskinfoMapper;
import com.heima.schedule.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author: 周海
 * @Create : 2024/3/29
 **/

@Service
@Transactional
@Slf4j
public class TaskServiceImpl implements TaskService {
    /**
     * 添加延迟任务
     *
     * @param task
     * @return
     */
    @Override
    public Long addTask(Task task) {
        //1.添加任务到数据库中
        boolean success = AddTaskToDb(task);
        if (success) {
            //2.添加任务到redis中
            addTaskToCache(task);
        }

        //2.1如果执行时间小于等于当前时间
        //2.2如果执行时间大于当前时间 && 小于等于预设时间（未来5分钟） 存入zset
        return null;
    }

    /**
     * 取消任务
     *
     * @param taskId
     * @return
     */
    public boolean cannelTask(Long taskId) {
        boolean flag = false;
        //删除任务，更新任务日志
        Task task = updateDb(taskId, ScheduleConstants.CANCELLED);
        if (task != null) {
            // 删除redis的数据
            removeTaskFromCache(task);
            flag = true;
        }
        return flag;
    }


    /**
     * 拉取任务-->按照类型和优先级拉取任务
     *
     * @return
     */
    public Task poll(int type, int priority) {
        Task task = null;
        try {
            String key = type + "_" + priority;

            String take_json = cacheService.lRightPop(ScheduleConstants.TOPIC + key);
            if (StringUtils.isNotBlank(take_json)) {
                task = JSON.parseObject(take_json, Task.class);
                //修改数据库的信息
                updateDb(task.getTaskId(), ScheduleConstants.EXECUTED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("poll task exception");
        }
        return task;
    }

    /**
     * 删除redis中的缓存数据
     *
     * @param task
     */
    private void removeTaskFromCache(Task task) {
        //
        String key = task.getTaskType() + "_" + task.getPriority();
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            //小于当前的时间->从list的中删除数据
            cacheService.lRemove(ScheduleConstants.TOPIC + key, 0, JSON.toJSONString(task));
        } else {
            //大于当前的时间->从zset中删除
            cacheService.zRemove(ScheduleConstants.FUTURE + key, JSON.toJSONString(task));
        }
    }

    /**
     * 删除任务，更新日志
     *
     * @param taskId
     * @param status
     * @return
     */
    private Task updateDb(Long taskId, int status) {
        Task task = null;
        try {
            taskinfoMapper.deleteById(taskId);
            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
            taskinfoLogs.setStatus(status);
            taskinfoLogsMapper.updateById(taskinfoLogs);
            task = new Task();
            BeanUtils.copyProperties(taskinfoLogs, task);
            task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
            return task;
        } catch (Exception e) {
            log.info("task cancel exception");
            e.printStackTrace();
        }
        return task;
    }

    @Autowired
    private CacheService cacheService;

    /**
     * 添加任务到redis中
     *
     * @param task
     */
    private void addTaskToCache(Task task) {
        //2.1如果执行时间小于等于当前时间
        String key = task.getTaskType() + "_" + task.getPriority();
        //获取5分钟之后的时间 毫秒值
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long timeInMillis = calendar.getTimeInMillis();
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, JSON.toJSONString(task));
        } else if (task.getExecuteTime() <= timeInMillis) {
            //2.2如果执行时间大于当前时间 && 小于等于预设时间（未来5分钟） 存入zset
            cacheService.zAdd(ScheduleConstants.FUTURE + key, JSON.toJSONString(task), task.getExecuteTime());
        }

    }

    @Autowired
    private TaskinfoMapper taskinfoMapper;
    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;

    /**
     * 添加任务到数据库中
     *
     * @param task
     * @return
     */
    private boolean AddTaskToDb(Task task) {
        boolean flag = false;
        try {
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task, taskinfo);
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            taskinfoMapper.insert(taskinfo);
            //设置taskid
            task.setTaskId(taskinfo.getTaskId());
            //保存日志数据到数据库中
            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(taskinfo, taskinfoLogs);
            taskinfoLogs.setVersion(ScheduleConstants.EXECUTED);
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 定时任务-->未来数据每分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh() {
        //通过加锁的方式将redis异步
        String token = cacheService.tryLock("FUTRUE_TASK_SYNC", 1000 * 30);
        if (StringUtils.isNotBlank(token)) {
            log.info("未来数据定时刷新");
            //获取未来数据的集合key
            Set<String> keys = cacheService.scan(ScheduleConstants.FUTURE + "*");
            //按照key和分支进行查询
            for (String key : keys) {
                String topicKey = ScheduleConstants.TOPIC + key.split(ScheduleConstants.FUTURE)[1];
                Set<String> tasks = cacheService.zRangeByScore(key, 0, System.currentTimeMillis());
                //同步数据
                if (tasks.isEmpty()) {
                    cacheService.refreshWithPipeline(key, topicKey, tasks);
                    log.info("定时任务成功将" + key + "刷新到了" + topicKey);

                }
            }
        }
    }

    /**
     * 数据库任务定时每五分钟同步到redis
     */
    @PostConstruct//初始话方法，微服务每次启动都会执行这个方法
    @Scheduled(cron = "0 */5 * * * ?")
    public void reloadDate() {
        //清理缓存中的数据
        clearCache();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        List<Taskinfo> taskinfos = taskinfoMapper
                .selectList(Wrappers.<Taskinfo>lambdaQuery()
                        .lt(Taskinfo::getExecuteTime, calendar.getTime()));
        //将任务添加入redis
        if (taskinfos != null && taskinfos.size() > 0) {
            for (Taskinfo taskinfo : taskinfos) {
                Task task = new Task();
                BeanUtils.copyProperties(taskinfo, task);
                task.setExecuteTime(taskinfo.getExecuteTime().getTime());
                addTask(task);
            }
        }
        log.info("数据库任务同步到redis中");
    }

    public void clearCache() {
        Set<String> topicKeys = cacheService.scan(ScheduleConstants.TOPIC + "*");
        Set<String> futurekey = cacheService.scan(ScheduleConstants.FUTURE + "*");
        cacheService.delete(topicKeys);
        cacheService.delete(futurekey);

    }
}
