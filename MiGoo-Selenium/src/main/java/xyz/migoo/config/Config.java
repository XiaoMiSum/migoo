package xyz.migoo.config;

import java.util.Locale;

/**
* @author xiaomi
 */
public class Config {
	private Config(){}

	public static final int SHORT_TIME =1;
	public static final int MEDIUM_TIME =3;
	public static final int LONG_TIME =10;

    public static final String XPATH_1 = ".//";
    public static final String XPATH_2 = "//";
    public static final String XPATH_3 = "xpath=";
    public static final String ID = "id=";
    public static final String PARTIAL_LINK = "partiallink=";
    public static final String LINK = "link=";
    public static final String CSS = "css=";
    public static final String CLASS = "class=";
    public static final String TAG_NAME = "tagname=";
    public static final String NAME = "name=";
    public static final String SPLIT = "=";

    public static final boolean WINDOWS = System.getProperty("os.name").toLowerCase(Locale.US).contains("windows");
}
