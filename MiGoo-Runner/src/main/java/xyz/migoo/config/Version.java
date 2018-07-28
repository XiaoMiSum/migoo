package xyz.migoo.config;

import xyz.migoo.parser.Properties;

/**
 * @author xiaomi
 * @date 2018/7/25 14:10
 */
public class Version {

    private static final Properties PROPERTIES = new Properties("platform.properties");

    public static final String HTTPCLIENT_VERSION = PROPERTIES.getValue("httpClient.version");

    public static final String JDK_VERSION = System.getProperty("java.version");

    public static final String OS_VERSION = System.getProperty("os.name") + "  " + System.getProperty("os.version");

}
