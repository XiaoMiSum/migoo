package xyz.migoo.report;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import xyz.migoo.framework.config.Platform;
import xyz.migoo.utils.DateUtil;

import java.io.File;

import static xyz.migoo.framework.config.Platform.*;

/**
 * @author xiaomi
 * @date 2018/8/7 16:53
 */
class Email {

    private static HtmlEmail email = new HtmlEmail();

     static{
        email.setAuthentication(MAIL_SEND_FROM, MAIL_SEND_PASS);
        email.setHostName(MAIL_IMAP_HOST);
        email.setCharset("UTF-8");
    }

    static void sendEmail(String message){
        if (!Platform.MAIL_SEND){
            return;
        }
        String path = String.format("%s/reports/%s/", System.getProperty("user.dir"), DateUtil.TODAY_DATE);
        String subject = "api test reports " + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS);
        File zip = zipFile(path, "reports-" + DateUtil.TODAY_DATE);
        send(zip, subject, message);
        zip.delete();
    }

    private static File zipFile(String path, String fileName){
        Project project = new Project();
        FileSet fileSet = new FileSet();
        fileSet.setExcludes("*.zip");
        fileSet.setDir(new File(path));
        Zip zip = new Zip();
        zip.setProject(project);
        zip.setDestFile(new File(  path + fileName + ".zip"));
        zip.addFileset(fileSet);
        zip.execute();
        return new File(path + fileName + ".zip");
    }

    private static void send(File path, String subject, String message) {
        try {
            email.setFrom(Platform.MAIL_SEND_FROM);
            for(Object to: Platform.MAIL_SEND_TO_LIST){
                email.addTo(String.valueOf(to));
            }
            email.setSubject(subject);
            email.setMsg(message);
            email.attach(path);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}
