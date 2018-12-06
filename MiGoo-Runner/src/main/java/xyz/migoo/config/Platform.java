package xyz.migoo.config;

import com.alibaba.fastjson.JSONObject;
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

    //private static final PropertiesReader PROPERTIES = new PropertiesReader("classpath://migoo.properties");
    
    private static final JSONObject PROPERTIES = new JSONObject();
    static {
        String[] properties = new String[]{"migoo.properties", "application.properties"};
        for (String file : properties) {
            PROPERTIES.putAll(new PropertiesReader("classpath://" + file).read());
        }
    }

    public static final String HTTP_CLIENT_VERSION = PROPERTIES.getString("http.client.version");

    public static final String JDK_VERSION = System.getProperty("java.version");

    public static final String OS_VERSION = System.getProperty("os.name") + "  " + System.getProperty("os.version");

    public static final List<String> FUNCTION_EQUALS = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.getString("function.equals")).split(","));

    public static final List<String> FUNCTION_CONTAINS = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.getString("function.contains")).split(","));

    public static final List<String> FUNCTION_IS_EMPTY = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.getString("function.isEmpty")).split(","));

    public static final List<String> FUNCTION_IS_NOT_EMPTY = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.getString("function.isNotEmpty")).split(","));

    private static final List<String> CHECK_JSON = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.getString("check.json")).split(","));

    private static final List<String> CHECK_HTML = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.getString("check.html")).split(","));

    public static boolean isJson(String str){
        for (String key : CHECK_JSON){
            if (Pattern.compile(key).matcher(str).find()){
                return true;
            }
        }
        return false;
    }

    public static boolean isHtml(String str){
        for (String key : CHECK_HTML){
            if (Pattern.compile(key).matcher(str).find()){
                return true;
            }
        }
        return false;
    }

    public static final List<String> CHECK_BODY = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.getString("check.body")).split(","));

    public static final List<String> CHECK_REGEX = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.getString("check.regex")).split(","));

    public static final List<String> CHECK_CODE = Arrays.asList(
            StringUtil.trimAny(PROPERTIES.getString("check.status")).split(","));

    public static final boolean MAIL_SEND = Boolean.valueOf(PROPERTIES.getString("mail.send").trim());

    public static final String MAIL_IMAP_HOST = PROPERTIES.getString("mail.imap.host").trim();

    public static final String MAIL_SEND_FROM = PROPERTIES.getString("mail.send.from").trim();

    public static final String MAIL_SEND_PASS = PROPERTIES.getString("mail.send.pass").trim();

    public static final String[] MAIL_SEND_TO_LIST = StringUtil.trimAny(PROPERTIES.getString("mail.send.toList")).split(",");

    public static final String[] EXTENDS_VARIABLE = StringUtil.trimAny(PROPERTIES.getString("extends.variable")).split(",");

    public static final String[] EXTENDS_HOOK = StringUtil.trimAny(PROPERTIES.getString("extends.hook")).split(",");

    public static final String[] EXTENDS_VALIDATOR = StringUtil.trimAny(PROPERTIES.getString("extends.validator")).split(",");
}