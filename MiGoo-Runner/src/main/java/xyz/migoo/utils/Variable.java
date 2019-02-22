package xyz.migoo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Platform;
import xyz.migoo.exception.InvokeException;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 绑定变量
 *
 * @author xiaomi
 * @date 2018/08/02 14:53
 */
public class Variable {

    private static Log log = new Log(Variable.class);

    static final Pattern FUNC_PATTERN = Pattern.compile("^__(\\w+)\\((.*)\\)");

    private static final Pattern PARAM_PATTERN = Pattern.compile("\\$\\{(\\w+)}");

    private static final Pattern JSON_PATTERN = Pattern.compile("\\{(.+)}");

    private static Map<String, Method> methods = null;

    private Variable() {
    }

    /**
     * 循环5次绑定变量，防止出现未被绑定的
     *
     * 1. 使用 variables 替换 source 中使用变量的属性
     * 2. 计算 variables中的方法变量
     * 3. 再次使用计算后的 variables 替换 source 中使用变量的属性
     * @param variables 包含变量的 json 对象
     * @param source    使用变量的 json 对象
     */
    public static void loopBindVariables(JSONObject variables, JSONObject source) throws InvokeException {
        int defaultLoop = 5;
        for (int i = 0; i < defaultLoop; i++) {
            bindVariable(variables, source);
            evalVariable(variables);
            bindVariable(variables, source);
        }
    }

    /**
     * 绑定变量
     * 1. 遍历变量列表, 取出该变量的值
     * 2. 遍历可能使用变量的地方，并替换为变量的 value
     *
     * @param variables 包含变量的 json 对象，用例的 config.variables 或 case.setUp
     * @param source    使用变量的 json 对象，json.config 或 json.case
     */
    public static void bindVariable(JSONObject variables, JSONObject source) {
        if (variables == null || source == null || variables.isEmpty() || source.isEmpty()) {
            return;
        }
        for (String variable : variables.keySet()) {
            String value = variables.getString(variable);
            Matcher matcher = FUNC_PATTERN.matcher(value);
            if (matcher.find()) {
                continue;
            }
            Matcher jsonMatcher = JSON_PATTERN.matcher(value);
            if (jsonMatcher.find()){
                // 将 json字符中的 , " { } 替换，防止 fastjson 解析报错
                log.info("the variable's value is a JSON object, do replace....");
                value = value.replaceAll("\\\"", "'").replaceAll("「","{").replaceAll("」", "}").replaceAll(",", "=");
            }
            String regex = "${" + variable + "}";
            for (String key : source.keySet()) {
                Object object = source.get(key);
                if (!(object instanceof JSON)){
                    String stringObj = String.valueOf(object);
                    if (StringUtil.contains(stringObj, regex)){
                        log.info(String.format("bindVariable：[key: %s] => [%s] -> [%s]", key, regex, value));
                        source.put(key, stringObj.replace(regex, value));
                    }
                    continue;
                }
                if (object instanceof JSONObject){
                    JSONObject jsonObj = (JSONObject)object;
                    for (String k : jsonObj.keySet()){
                        String s = jsonObj.getString(k);
                        if (StringUtil.contains(s, regex)){
                            String result = s.replace(regex, value);
                            try {
                                jsonObj.put(k, JSON.parse(result));
                            }catch (Exception e){
                                jsonObj.put(k, result);
                            }
                            log.info(String.format("bindVariable：[key: %s.%s] => [%s] -> [%s]", key, k, regex, result));
                        }
                    }
                    source.put(key, jsonObj);
                    continue;
                }
                if (object instanceof JSONArray) {
                    JSONArray arrayObj = (JSONArray)object;
                    for (int i = 0; i < arrayObj.size(); i++) {
                        String s = arrayObj.getString(i);
                        if (StringUtil.contains(s, regex)) {
                            arrayObj.remove(i);
                            String result = s.replace(regex, value);
                            try {
                                arrayObj.add(i, JSON.parse(result));
                            }catch (Exception e){
                                arrayObj.add(i, result);
                            }
                            log.info(String.format("bindVariable：[key: %s[%s]] => [%s] -> [%s]", key, i, regex, result));
                        }
                    }
                    source.put(key, arrayObj);
                }
            }
        }
    }

    @Deprecated
    public static void bindVariable(JSONObject variables, String key) {
        if (variables == null || StringUtil.isBlank(key) || variables.isEmpty()) {
            return;
        }
        String source = variables.getString(key);
        if (StringUtil.isBlank(source)) {
            return;
        }
        for (String variable : variables.keySet()) {
            String value = variables.getString(variable);
            if (FUNC_PATTERN.matcher(value).find()) {
                continue;
            }
            String regex = "${" + variable + "}";
            if (StringUtil.contains(source, regex)) {
                log.info(String.format("bindVariable：[key: %s] => [%s] -> [%s]", key, regex, value));
                variables.put(key, source.replace(regex, value));
            }
        }
    }

    /**
     * 计算出方法变量的值，并更新对应 key 的值
     *
     * @param variables 包含方法变量的 json 对象
     */
    public static JSONObject evalVariable(JSONObject variables) throws InvokeException {
        if (variables == null || variables.isEmpty()) {
            return variables;
        }
        for (String key : variables.keySet()) {
            Object value = variables.get(key);
            if (!(value instanceof String)) {
                continue;
            }
            Matcher matcher = FUNC_PATTERN.matcher((String) value);
            if (matcher.find()) {
                if (PARAM_PATTERN.matcher(matcher.group(2)).find()) {
                    continue;
                }
                functionLoader();
                Object result = invoke(matcher.group(1), matcher.group(2));
                if (result != null) {
                    variables.put(key, result);
                }
            }
        }
        return variables;
    }

    /**
     * 从指定扩展类中 获取扩展函数
     */
    private static void functionLoader() throws InvokeException {
        if (methods == null) {
            methods = InvokeUtil.functionLoader(Platform.EXTENDS_VARIABLE);
        }
    }

    /**
     * 执行指定扩展函数
     *
     * @param name   待执行的函数名
     * @param params 参数
     * @return 扩展函数执行返回值
     */
    private static Object invoke(String name, String params) throws InvokeException {
        return InvokeUtil.invoke(methods, name, params);
    }
}
