package xyz.migoo.utils;

import com.alibaba.fastjson.JSONArray;
import xyz.migoo.config.Platform;
import xyz.migoo.exception.InvokeException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;

import static xyz.migoo.utils.Variable.FUNC_PATTERN;

/**
 * 执行 数据处理
 *
 * @author xiaomi
 * @date 2018/08/02 14:53
 */
public class Hook {

    private static Map<String, Method> methods = null;

    private Hook() {
    }

    /**
     *
     * @param hook 待执行的方法列表
     */
    public static void hook(JSONArray hook) throws InvokeException {
        if (hook == null){
            return;
        }
        for (int i = 0; i < hook.size(); i++){
            Matcher matcher = FUNC_PATTERN.matcher(hook.getString(i));
            if (matcher.find()) {
                functionLoader();
                invoke(matcher.group(1), matcher.group(2));
            }
        }
    }

    /**
     * 从指定 扩展类中 获取扩展函数
     */
    private static void functionLoader() throws InvokeException {
        if (methods == null) {
            methods = InvokeUtil.functionLoader(Platform.EXTENDS_HOOK);
        }
    }

    /**
     * 执行指定扩展函数
     *
     * @param name  方法名
     * @param params  参数
     */
    private static void invoke(String name, String params) throws InvokeException {
        InvokeUtil.invoke(methods, name, params);
    }

}
