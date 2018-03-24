package com.seeu.artshow.event_listener;

import com.seeu.artshow.record.service.RecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    private final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    @Autowired
    private RecordService recordService;

    @Scheduled(cron = "00 50 23 * * ?") //晚上 23:50:00 执行一次
//    @Scheduled(fixedDelay=5000) //每隔 5 秒执行一次
    public void statusCheck() {
        logger.info("开始统计用户数据 START");
        recordService.updateRecordUsers();
        logger.info("用户数据统计完毕 END");
    }

}
