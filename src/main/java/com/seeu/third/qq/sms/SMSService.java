package com.seeu.third.qq.sms;

import com.seeu.third.exception.SMSSendFailureException;
import com.seeu.third.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;

import java.io.IOException;

public interface SMSService {

    // 十分钟有效？ 验证码发送
    void sendCheckCode(String phone, String code) throws SMSSendFailureException;
}
