package xyz.migoo.framework.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.loader.reader.YamlReader;

import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2018/7/25 14:10
 */
public class Platform {

    private Platform() {
    }

    private static final JSONObject PROPERTIES = new JSONObject();

    private static final String STRING = "extends.class";

    private static final String IGNORE = "ignore.directory";

    static {
        String[] properties = new String[]{"application.yml", "migoo.yml"};
        for (String file : properties) {
            try {
                YamlReader reader = new YamlReader("classpath://" + file);
                if (reader.isNull()) {
                    continue;
                }
                ((JSONObject)reader.read()).forEach((key, value) -> {
                    if (STRING.equals(key) || IGNORE.equals(key)) {
                        if (PROPERTIES.getJSONArray(key) != null) {
                            ((JSONArray)value).addAll(PROPERTIES.getJSONArray(key));
                        }
                    }
                    PROPERTIES.put(key, value);
                });
            } catch (ReaderException e) {
                e.printStackTrace();
            }
        }
    }

    public static final JSONArray FUNCTION_EQUALS = PROPERTIES.getJSONArray("function.equals");

    public static final JSONArray FUNCTION_NOT_EQUALS = PROPERTIES.getJSONArray("function.notEquals");

    public static final JSONArray FUNCTION_EQUALS_IGNORE_CASE = PROPERTIES.getJSONArray("function.equalsIgnoreCase");

    public static final JSONArray FUNCTION_GREATER_THAN_OR_EQUALS = PROPERTIES.getJSONArray("function.greaterThanOrEquals");

    public static final JSONArray FUNCTION_LESS_THAN_OR_EQUALS = PROPERTIES.getJSONArray("function.lessThanOrEquals");

    public static final JSONArray FUNCTION_GREATER_THAN = PROPERTIES.getJSONArray("function.greaterThan");

    public static final JSONArray FUNCTION_LESS_THAN = PROPERTIES.getJSONArray("function.lessThan");

    public static final JSONArray FUNCTION_CONTAINS = PROPERTIES.getJSONArray("function.contains");

    public static final JSONArray FUNCTION_NOT_CONTAINS = PROPERTIES.getJSONArray("function.doesNotContains");

    public static final JSONArray FUNCTION_IS_EMPTY = PROPERTIES.getJSONArray("function.isEmpty");

    public static final JSONArray FUNCTION_IS_NOT_EMPTY = PROPERTIES.getJSONArray("function.isNotEmpty");

    public static final JSONArray FUNCTION_LIST = PROPERTIES.getJSONArray("function.list");

    public static final JSONArray FUNCTION_REGEX = PROPERTIES.getJSONArray("function.regex");

    private static final JSONArray CHECK_JSON = PROPERTIES.getJSONArray("check.json");

    public static boolean isJson(String str) {
        for (int i = 0; i < CHECK_JSON.size(); i++) {
            if (Pattern.compile(CHECK_JSON.getString(i)).matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    public static final JSONArray CHECK_BODY = PROPERTIES.getJSONArray("check.body");

    public static final JSONArray CHECK_CODE = PROPERTIES.getJSONArray("check.code");

    public static final boolean MAIL_SEND = Boolean.parseBoolean(PROPERTIES.getString("mail.send").trim());

    public static final String MAIL_IMAP_HOST = PROPERTIES.getString("mail.imap.host").trim();

    public static final String MAIL_SEND_FROM = PROPERTIES.getString("mail.send.from").trim();

    public static final String MAIL_SEND_PASS = PROPERTIES.getString("mail.send.password").trim();

    public static final JSONArray IGNORE_DIRECTORY = PROPERTIES.getJSONArray("ignore.directory");

    public static final Object[] MAIL_SEND_TO_LIST = PROPERTIES.getJSONArray("mail.send.toList").toArray();

    public static final Object[] EXTENDS_CLASS = PROPERTIES.getJSONArray("extends.class").toArray();



}
