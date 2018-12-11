package xyz.migoo.utils;

import com.alibaba.fastjson.JSONArray;
import xyz.migoo.config.Platform;
import xyz.migoo.exception.VariableException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;

import static xyz.migoo.utils.Variable.FUNC_PATTERN;

/**
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
    public static void hook(JSONArray hook) throws VariableException{
        if (hook ==null || hook.isEmpty()){
            return;
        }
        for (int i = 0; i < hook.size(); i++){
            String value = hook.getString(i);
            Matcher matcher = FUNC_PATTERN.matcher(value);
            if (matcher.find()){
                functionLoader();
                invoke(matcher.group(1), matcher.group(2));
            }
        }
    }

    private static void functionLoader() throws VariableException{
        try {
            if (methods == null){
                methods = InvokeUtil.functionLoader(Platform.EXTENDS_HOOK);
            }
        }catch (Exception e){
            throw new VariableException(StringUtil.getStackTrace(e));
        }
    }

    private static void invoke(String name, String params) throws VariableException {
        try {
            InvokeUtil.invoke(methods, name, params);
        }catch (Exception e){
            throw new VariableException(StringUtil.getStackTrace(e));
        }
    }
}