package xyz.migoo.runner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Platform;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.utils.InvokeUtil;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;

import static xyz.migoo.parser.BindVariable.FUNC_PATTERN;

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
     * @param object 待执行的方法列表
     */
    public static void hook(Object object, JSONObject variables) throws InvokeException {
        if (object == null){
            return;
        }
        JSONArray hook;
        if (object instanceof String){
            hook = JSON.parseArray(object.toString());
        }else {
            hook = (JSONArray)object;
        }
        for (int i = 0; i < hook.size(); i++){
            Matcher func = FUNC_PATTERN.matcher(hook.getString(i));
            if (func.find()) {
                functionLoader();
                InvokeUtil.invoke(methods, func.group(1), func.group(2));
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
}
