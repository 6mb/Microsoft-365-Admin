package cn.itbat.microsoft.service.impl;

import cn.itbat.microsoft.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author huahui.wu
 * @date 2020年11月23日 17:28:17
 */
@Service
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Async
    @Override
    public void sendMail(String to, String userName, String password) {
        //简单邮件
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("Office 365帐户信息");
        simpleMailMessage.setText("已创建或修改用户帐户\n" +
                " \n" +
                "用户名: " + userName + "\n" +
                "临时密码: " + password + "\n" +
                " \n" +
                " \n" +
                "要执行的后续步骤如下: \n" +
                "登陆地址：https://www.office.com/\n");
        mailSender.send(simpleMailMessage);
    }

}
