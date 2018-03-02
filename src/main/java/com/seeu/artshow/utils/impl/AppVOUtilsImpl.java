package com.seeu.artshow.utils.impl;

import com.seeu.artshow.userlogin.model.User;
import com.seeu.artshow.utils.AppVOUtils;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class AppVOUtilsImpl implements AppVOUtils {

    public List<Long> parseBytesToLongList(Object object) {
        if (object == null) return new ArrayList<>();
        if (object instanceof byte[]) {
            byte[] bytes = (byte[]) object;
            if (bytes.length == 0) return new ArrayList<>();
            List<Long> longs = new ArrayList<>();
            String temp = "";
            for (int i = 0; i < bytes.length; i++) {
                byte id = bytes[i];
                if (id == 44) {
                    // 这是逗号
                    longs.add(parseLong(temp)); // 转成对应的 int 值
                    temp = "";
                } else if (i + 1 == bytes.length) {
                    // 这是句末
                    temp += Byte.toUnsignedLong(id) - 48;
                    longs.add(parseLong(temp));
                    temp = "";
                } else {
                    temp += Byte.toUnsignedLong(id) - 48;
                }
            }
        }
        if (object instanceof String) {
            String longStr = object.toString();
            List<Long> longs = new ArrayList<>();
            String[] ids = longStr.split(",");
            for (String id : ids) {
                longs.add(parseLong(id));
            }
            return longs;
        }
        return new ArrayList<>();
    }

    public Integer parseInt(Object object) {
        if (object == null) return 0;
        return Integer.parseInt(object.toString());
    }


    public Long parseLong(Object object) {
        if (object == null) return 0l;
        return Long.parseLong(object.toString());
    }

    public String parseString(Object object) {
        if (object == null) return null;
        return object.toString();
    }

    public Double parseDouble(Object object) {
        if (object == null) return null;
        return Double.parseDouble(object.toString());
    }

    private static DateFormatter formatter = new DateFormatter("yyyy-MM-dd HH:mm");

    public Date parseDate(Object object) {
        if (object == null) return null;
        // TODO
        String dateStr = object.toString();
        try {
            return formatter.parse(dateStr, Locale.CHINESE);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public User.GENDER parseGENDER(Object object) {
        if (object == null) return null;
        int gender = parseInt(object);
        return 0 == gender ? User.GENDER.male : User.GENDER.female;
    }
}
