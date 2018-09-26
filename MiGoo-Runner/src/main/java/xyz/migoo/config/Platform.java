package xyz.migoo.config;

import xyz.migoo.reader.PropertiesReader;
import xyz.migoo.utils.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2018/7/25 14:10
 */
public class Platform {

    private Platform(){}

    private static final PropertiesReader PROPERTIES = new PropertiesReader("application.properties");

    public static final String HTTPCLIENT_VERSION = PROPERTIES.get("httpClient.version");

    public static final String JDK_VERSION = System.getProperty("java.version");

    public static final String OS_VERSION = System.getProperty("os.name") + "  " + System.getProperty("os.version");

    public static final List<String> FUNCTION_EQUALS = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.get("function.equals")).split(","));

    public static final List<String> FUNCTION_CONTAINS = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.get("function.contains")).split(","));

    public static final List<String> FUNCTION_IS_EMPTY = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.get("function.isEmpty")).split(","));

    public static final List<String> FUNCTION_IS_NOT_EMPTY = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.get("function.isNotEmpty")).split(","));

    private static final List<String> CHECK_JSON = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.get("check.json")).split(","));

    private static final List<String> CHECK_HTML = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.get("check.html")).split(","));

    public static boolean isJson(String str){
        for (String key : CHECK_JSON){
            if (Pattern.compile(key).matcher(str).find()){
                return true;
            }
        }
        return false;
    }

    public static boolean isHtml(String str){
        for (String key : CHECK_JSON){
            if (Pattern.compile(key).matcher(str).find()){
                return true;
            }
        }
        return false;
    }

    public static final List<String> CHECK_BODY = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.get("check.body")).split(","));

    public static final List<String> CHECK_CODE = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.get("check.status")).split(","));

    public static final boolean MAIL_SEND = Boolean.valueOf(PROPERTIES.get("mail.send").trim());

    public static final String MAIL_IMAP_HOST = PROPERTIES.get("mail.imap.host").trim();

    public static final String MAIL_SEND_FROM = PROPERTIES.get("mail.send.from").trim();

    public static final String MAIL_SEND_PASS = PROPERTIES.get("mail.send.pass").trim();

    public static final String[] MAIL_SEND_TO_LIST = PROPERTIES.get("mail.send.toList").split(",");

    public static final String EXTENDS_VARIABLE = PROPERTIES.get("extends.variable").trim();

    public static final String EXTENDS_HOOK = PROPERTIES.get("extends.hook").trim();

    public static final String EXTENDS_VALIDATOR = PROPERTIES.get("extends.validator").trim();

}