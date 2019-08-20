package xyz.migoo.report;

import xyz.migoo.framework.config.Platform;
import xyz.migoo.utils.DateUtil;

import java.io.File;

import static xyz.migoo.framework.config.Platform.*;

/**
 * @author xiaomi
 * @date 2018/8/7 16:53
 */
public class EmailUtil {

    private static final Email EMAIL = new Email(MAIL_SEND_FROM, MAIL_SEND_PASS, MAIL_IMAP_HOST);

    public static void sendEmail(){
        if (!Platform.MAIL_SEND){
            return;
        }
        File file = new File(System.getProperty("user.dir"));
        file = new File(file.getPath() + File.separator + "Reports" + File.separator + DateUtil.TODAY_DATE);
        String subject = "api test reports " + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS);
        File zip = ZipUtil.zipFile(file.getPath(), "reports-" + DateUtil.TODAY_DATE);
        EMAIL.send(MAIL_SEND_FROM, MAIL_SEND_TO_LIST, zip, subject,"附件，请查收～");
        zip.delete();
    }

    public static void sendEmail(String message, String path){
        if (!Platform.MAIL_SEND){
            return;
        }
        String subject = "api test reports " + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS);
        File file = new File(path);
        EMAIL.send(MAIL_SEND_FROM, MAIL_SEND_TO_LIST, file, subject, message);
    }
}
