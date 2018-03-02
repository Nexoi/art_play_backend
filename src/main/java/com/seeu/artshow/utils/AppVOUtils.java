package com.seeu.artshow.utils;

import com.seeu.artshow.userlogin.model.User;

import java.util.Date;
import java.util.List;

public interface AppVOUtils {

    public List<Long> parseBytesToLongList(Object object);

    public Integer parseInt(Object object);

    public Long parseLong(Object object);

    public String parseString(Object object);

    public Double parseDouble(Object object);

    public Date parseDate(Object object);

    public User.GENDER parseGENDER(Object object);
}
