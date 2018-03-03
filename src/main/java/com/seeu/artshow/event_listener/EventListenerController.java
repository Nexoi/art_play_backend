package com.seeu.artshow.event_listener;

import com.seeu.artshow.event_listener.task.SignInTodayEvent;
import com.seeu.artshow.utils.DateFormatterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class EventListenerController {
    Logger logger = LoggerFactory.getLogger(EventListenerController.class);

    @Autowired
    private DateFormatterService dateFormatterService;

    @EventListener
    public void signInTodayEvent(SignInTodayEvent event) {
        Long uid = event.getUid();
    }

    private String genOrderID() {
        SimpleDateFormat format = dateFormatterService.getyyyyMMddHHmmssS();
        String dateStr = format.format(new Date());
        return dateStr + (new Random().nextInt(900) + 100);
    }
}
