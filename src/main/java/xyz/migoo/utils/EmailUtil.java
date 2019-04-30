package xyz.migoo.utils;

import xyz.migoo.config.Platform;
import xyz.migoo.report.SendEmail;

import java.io.File;
import java.io.IOException;

import static xyz.migoo.config.Platform.*;

/**
 * @author xiaomi
 * @data 2018/8/7 16:53
 */
public class EmailUtil {

    public static void sendEmail(boolean isMain){
        if (!Platform.MAIL_SEND){
            return;
        }
        File file = new File(System.getProperty("user.dir"));
        if (isMain){
            file = new File(file.getPath() + File.separator + "Reports" + File.separator + DateUtil.TODAY_DATE);
        }else {
            file = new File(file.getParent() + File.separator + "Reports" + File.separator + DateUtil.TODAY_DATE);
        }
        String subject = "自动化测试报告" + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS);
        try {
            File zip = ZipUtil.zipFile(file.getPath(), "reports");
            SendEmail.send(MAIL_SEND_FROM, MAIL_SEND_PASS, MAIL_SEND_FROM, MAIL_SEND_TO_LIST, zip, MAIL_IMAP_HOST, subject,"附件，请查收～");
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendEmail(String message, String path){
        if (!Platform.MAIL_SEND){
            return;
        }
        String subject = "自动化测试报告" + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS);
        File file = new File(path);
        SendEmail.send(MAIL_SEND_FROM, MAIL_SEND_PASS, MAIL_SEND_FROM, MAIL_SEND_TO_LIST, file, MAIL_IMAP_HOST, subject, message);
        new File(file.getParent()).delete();
    }
}
