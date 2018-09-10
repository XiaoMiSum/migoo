package xyz.migoo.utils;

import xyz.migoo.config.Platform;
import xyz.migoo.mail.SendEmail;

import java.io.File;
import java.io.IOException;

import static xyz.migoo.config.Platform.*;

/**
 * @author xiaomi
 * @data 2018/8/7 16:53
 */
public class EmailUtil {

    public static void sendEmail(String message, File file){
        String subject = "自动化测试报告" + file.getName();
        SendEmail.send(MAIL_SEND_FROM, MAIL_SEND_PASS, MAIL_SEND_FROM, MAIL_SEND_TO_LIST,
                file, MAIL_IMAP_HOST, subject, message);
    }

    public static void sendEmail(boolean isMain){
        if(!Platform.MAIL_SEND){
            return;
        }
        File path = new File(System.getProperty("user.dir"));
        if (isMain){
            path = new File(path.getPath() + File.separator + "Reports" + File.separator + DateUtil.TODAY);
        }else {
            path = new File(path.getParent() + File.separator + "Reports" + File.separator + DateUtil.TODAY);
        }
        String subject = "自动化测试报告";
        try {
            path = ZipUtil.zipFile(path.getPath(), "reports");
            SendEmail.send(MAIL_SEND_FROM, MAIL_SEND_PASS, MAIL_SEND_FROM, MAIL_SEND_TO_LIST,
                    path, MAIL_IMAP_HOST, subject,"附件，请查收～");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}