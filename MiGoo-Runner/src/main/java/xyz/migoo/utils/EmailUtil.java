package xyz.migoo.utils;

import xyz.migoo.mail.SendEmail;

import java.io.File;

import static xyz.migoo.config.Platform.*;

/**
 * @author xiaomi
 * @data 2018/8/7 16:53
 */
public class EmailUtil {

    public static void sendEmail(String message, File file){
        String subject = "自动化测试报告" + file.getName();
        SendEmail.send(MAIL_SEND_FROM,MAIL_SEND_PASS, MAIL_SEND_FROM, MAIL_SEND_TO_LIST,
                file,MAIL_IMAP_HOST,subject,message);
    }
}