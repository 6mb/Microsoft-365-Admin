package cn.itbat.microsoft.service;

/**
 * @author huahui.wu
 * @date 2020年11月23日 17:28:09
 */
public interface MailService {

    /**
     * 发送邮件
     *
     * @param to       接收人
     * @param userName office用户名
     * @param password office密码
     */
    void sendMail(String to, String userName, String password);
}
