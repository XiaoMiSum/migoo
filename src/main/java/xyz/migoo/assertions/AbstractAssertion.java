package xyz.migoo.assertions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.assertions.function.Function;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.TypeUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


import static xyz.migoo.config.Platform.*;

/**
 * @author xiaomi
 * @date 2019-04-13 21:57
 */
public abstract class AbstractAssertion implements Assertion{

    private static Map<String, Method> methods;
    private String methodName;
    private Method method;
    Object actual;

    @Override
    public boolean assertThat(JSONObject data) throws ExecuteError {
        setMethod(data.getString(CaseKeys.VALIDATE_TYPE));
        try {
            Boolean result = TypeUtil.booleanOf(method.invoke(null ,actual, data.get(CaseKeys.VALIDATE_EXPECT)));
            return result == null ? false : result;
        } catch (Exception e) {
            throw new ExecuteError(StringUtil.getStackTrace(e));
        }
    }

    /**
     * setting actual values
     * The parameter is response or Java class
     *
     * See the concrete implementation class for details
     * {@linkplain JSONAssertion JSONAssertion}
     * {@linkplain CustomAssertion CustomAssertion}
     * {@linkplain HTMLAssertion HTMLAssertion}
     * {@linkplain ResponseAssertion ResponseAssertion}
     * {@linkplain ResponseCodeAssertion ResponseCodeAssertion}
     *
     * @param actual response or java class
     */
    public abstract void setActual(Object actual);

    public Object getActual(){
        return actual;
    }

    private void setMethods(){
        if (methods != null){
            return;
        }
        Method[] methods = Function.class.getDeclaredMethods();
        AbstractAssertion.methods = new HashMap<>(methods.length);
        for (Method method : methods){
            AbstractAssertion.methods.put(method.getName(), method);
        }
    }

    private void setMethodName(String searchChar){
        if (FUNCTION_EQUALS.contains(searchChar)) {
            methodName = CaseKeys.VALIDATE_TYPE_EQUALS;
            return;
        }
        if (FUNCTION_NOT_EQUALS.contains(searchChar)) {
            methodName = CaseKeys.VALIDATE_TYPE_NOT_EQUALS;
            return;
        }
        if (FUNCTION_EQUALS_IGNORE_CASE.contains(searchChar)) {
            methodName = CaseKeys.VALIDATE_TYPE_EQUALS_IGNORE_CASE;
            return;
        }
        if (FUNCTION_GREATER_THAN_OR_EQUALS.contains(searchChar)){
            methodName = CaseKeys.VALIDATE_TYPE_GREATER_THAN_OR_EQUALS;
            return;
        }
        if (FUNCTION_LESS_THAN_OR_EQUALS.contains(searchChar)){
            methodName = CaseKeys.VALIDATE_TYPE_LESS_THAN_OR_EQUALS;
            return;
        }
        if (FUNCTION_REATER_THAN.contains(searchChar)){
            methodName = CaseKeys.VALIDATE_TYPE_GREATER_THAN;
            return;
        }
        if (FUNCTION_LESS_THAN.contains(searchChar)){
            methodName = CaseKeys.VALIDATE_TYPE_LESS_THAN;
            return;
        }
        if (FUNCTION_CONTAINS.contains(searchChar)) {
            methodName = CaseKeys.VALIDATE_TYPE_CONTAINS;
            return;
        }
        if (FUNCTION_NOT_CONTAINS.contains(searchChar)) {
            methodName = CaseKeys.VALIDATE_TYPE_NOT_CONTAINS;
            return;
        }
        if (FUNCTION_IS_EMPTY.contains(searchChar)) {
            methodName = CaseKeys.VALIDATE_TYPE_IS_EMPTY;
            return;
        }
        if (FUNCTION_IS_NOT_EMPTY.contains(searchChar)) {
            methodName = CaseKeys.VALIDATE_TYPE_IS_NOT_EMPTY;
            return;
        }
        if (FUNCTION_LIST.contains(searchChar)) {
            methodName = CaseKeys.VALIDATE_TYPE_IS_NOT_EMPTY;
            return;
        }
        if (FUNCTION_REGEX.contains(searchChar)) {
            methodName = CaseKeys.VALIDATE_TYPE_REGEX;
            return;
        }
        throw new ExecuteError(String.format("assert method '%s' not found", searchChar));
    }

    void setMethod(String type){
        setMethodName(type);
        setMethods();
        method = methods.get(methodName);
    }
}
