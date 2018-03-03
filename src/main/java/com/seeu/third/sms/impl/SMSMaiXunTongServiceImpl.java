package com.seeu.third.sms.impl;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.sms.SMSService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class SMSMaiXunTongServiceImpl implements SMSService {

    @Override
    public void send(String phone, String message) throws SMSSendFailureException {
        //... do nothing
    }

    @Override
    public void sendTemplate(String phone, String templatesId, String... parameters) {

    }
}
