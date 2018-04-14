package com.seeu.third.qq.sms.impl;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.qcloudsms.SmsSingleSender;
import com.seeu.third.qcloudsms.SmsSingleSenderResult;
import com.seeu.third.qq.sms.SMSService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class SMSServiceImpl implements SMSService {

    @Value("${qq.sms.appId}")
    private Integer APP_ID;
    @Value("${qq.sms.appKey}")
    private String APP_KEY;
    @Value("${qq.sms.templateId}")
    private Integer templateId;
    @Value("${qq.sms.sign}")
    private String sign;

    @Override
    public void sendCheckCode(String phone, String code) throws SMSSendFailureException {
        SmsSingleSender sender = new SmsSingleSender(APP_ID, APP_KEY);
        ArrayList<String> params = new ArrayList<String>();
        params.add(code);
//        params.add("深圳");
//        params.add("小明");
        SmsSingleSenderResult result = null;
        try {
            result = sender.sendWithParam("86", phone, templateId, params, "", "", "");
        } catch (Exception e) {
            throw new SMSSendFailureException(phone, code);
        }
        System.out.println(result);
    }
}
