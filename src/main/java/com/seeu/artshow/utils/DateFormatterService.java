package com.seeu.artshow.utils;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DateFormatterService {

    private SimpleDateFormat yyyyMMddHHmmssS;
    private SimpleDateFormat yyyyMMddHHmmss;
    private SimpleDateFormat yyyyMMdd_X;
    private SimpleDateFormat yyyyMMdd;

    public SimpleDateFormat getyyyyMMddHHmmssS() {
        if (yyyyMMddHHmmssS == null)
            yyyyMMddHHmmssS = new SimpleDateFormat("yyyyMMddHHmmssS");
        return yyyyMMddHHmmssS;
    }

    public SimpleDateFormat getyyyyMMdd_X() {
        if (yyyyMMdd_X == null)
            yyyyMMdd_X = new SimpleDateFormat("yyyy-MM-dd");
        return yyyyMMdd_X;
    }

    public SimpleDateFormat getyyyyMMddHHmmss() {
        if (yyyyMMddHHmmss == null)
            yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return yyyyMMddHHmmss;
    }


    public SimpleDateFormat getyyyyMMdd() {
        if (yyyyMMdd == null)
            yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        return yyyyMMdd;
    }

    public Long getyyyyMMdd(Date date) {
        return Long.parseLong(getyyyyMMdd().format(date));
    }
}
