package com.jonas.tensquare.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.jonas.tensquare.sms.utils.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @RabbitHandler
    public void executeSms(Map<String,String> map){
        String mobile = map.get("mobile");
        String template_code = "SMS_164970151";
        String sign_name = "十次方";
        try {
            smsUtil.sendSms(mobile,template_code,sign_name,"{\"code\":\""+ map.get("checkCode") +"\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
