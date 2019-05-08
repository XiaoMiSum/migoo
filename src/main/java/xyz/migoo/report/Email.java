package xyz.migoo.report;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.File;

/**
 * @author xiaomi
 * @date 2018/8/7 16:53
 */
public class Email {


    private static final String UTF8 = "UTF-8";
    private HtmlEmail email = new HtmlEmail();

    public Email(String user, String password, String host){
        email.setAuthentication(user,password);
        email.setHostName(host);
        email.setCharset(UTF8);
    }

    public void send(String from, Object[] toList, String subject, String message) {
        send(from, toList,null, subject, message);
    }

    public void send(String from, Object[] toList, File path, String subject, String message) {
        try {
            email.setFrom(from);
            for(Object to: toList){
                email.addTo(String.valueOf(to));
            }
            email.setSubject(subject);
            email.setMsg(message);
            if (path != null){
                email.attach(path);
            }
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}