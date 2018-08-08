package xyz.migoo.mail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.File;

/**
 * @author xiaomi
 * @data 2018/8/7 16:53
 */
public class SendEmail {


    private static final String UTF8 = "UTF-8";
    private static HtmlEmail email = new HtmlEmail();


    public static void send(String user, String password, String from, String[] toList, String host, String subject, String message) {
        send(user, password, from, toList,null, host, subject, message);
    }

    public static void send(String user, String password,String from, String[] toList, File path, String host, String subject, String message) {
        try {
            email.setAuthentication(user,password);
            email.setHostName(host);
            email.setCharset(UTF8);
            email.setFrom(from);
            for(String to: toList){
                email.addTo(to);
            }
            email.setSubject(subject);
            email.setMsg(message);
            if (path !=null){
                email.attach(path);
            }
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
}