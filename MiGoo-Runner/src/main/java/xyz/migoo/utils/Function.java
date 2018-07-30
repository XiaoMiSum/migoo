package xyz.migoo.utils;

import java.lang.reflect.Method;

import static xyz.migoo.config.Platform.FUNCTION_CONTAINS;
import static xyz.migoo.config.Platform.FUNCTION_EQUALS;

/**
 * @author xiaomi
 * @date 2018/7/28 10:50
 */
public class Function {

    private static String type;

    public static void functionLoader(String types) {
        if ( FUNCTION_EQUALS.contains(types)) {
            type = "equals";
        }
        if (FUNCTION_CONTAINS.contains(types)) {
            type = "contains";
        }
    }

    public static boolean validation(String actual, String expect) throws Exception {
        try {
            Method[] methods = Function.class.getDeclaredMethods();
            for (Method method : methods){
                if (type.equals(method.getName())) {
                    type = null;
                    return (boolean) method.invoke(null, actual, expect);
                }
            }
        }catch (Exception e){
            throw new Exception(StringUtil.getStackTrace(e));
        }
        return false;
    }

    private static boolean equals(Object actual, Object expect) {
        return actual.equals(expect);
    }

    private static boolean contains(Object actual, Object expect) {
        return StringUtil.contains((String)actual, (String)expect);
    }
}
