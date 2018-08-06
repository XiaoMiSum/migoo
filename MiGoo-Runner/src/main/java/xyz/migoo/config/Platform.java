package xyz.migoo.config;

import xyz.migoo.parser.Properties;
import xyz.migoo.utils.StringUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaomi
 * @date 2018/7/25 14:10
 */
public class Platform {

    private Platform(){}

    private static final Properties PROPERTIES = new Properties("application.properties");

    public static final String HTTPCLIENT_VERSION = PROPERTIES.get("httpClient.version");

    public static final String JDK_VERSION = System.getProperty("java.version");

    public static final String OS_VERSION = System.getProperty("os.name") + "  " + System.getProperty("os.version");

    public static final List FUNCTION_EQUALS = Arrays.asList(
            StringUtil.trim(PROPERTIES.get("function.equals")).split(","));

    public static final List FUNCTION_CONTAINS = Arrays.asList(
            StringUtil.trim(PROPERTIES.get("function.contains")).split(","));

    public static final List CHECK_BODY = Arrays.asList(
            StringUtil.trim(PROPERTIES.get("check.body")).split(","));

    public static final List CHECK_CODE = Arrays.asList(
            StringUtil.trim(PROPERTIES.get("check.code")).split(","));

    public static final List VARIABLE_OPERATE_REQUEST = Arrays.asList(
            StringUtil.trim(PROPERTIES.get("variable.operate.request")).split(","));

}
