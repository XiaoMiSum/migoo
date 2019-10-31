package xyz.migoo.framework.assertions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.framework.assertions.function.*;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.TypeUtil;

import static xyz.migoo.framework.config.Platform.*;

/**
 * @author xiaomi
 * @date 2019-04-13 21:57
 */
public abstract class AbstractAssertion implements Assertion{

    protected Object actual;

    @Override
    public boolean assertThat(JSONObject data) throws ExecuteError {
        try {
            IFunction function = getFunction(data.getString(CaseKeys.VALIDATE_TYPE));
            data.put("actual", actual);
            return TypeUtil.booleanOf(function.assertTrue(data));
        } catch (Exception e) {
            throw new ExecuteError("assert error.", e);
        }
    }

    /**
     * setting actual values
     * The parameter is response or Java class
     *
     * See the concrete implementation class for details
     * {@linkplain JSONAssertion JSONAssertion}
     * {@linkplain ResponseAssertion ResponseAssertion}
     * {@linkplain ResponseCodeAssertion ResponseCodeAssertion}
     *
     * @param actual response or java class
     */
    public abstract void setActual(Object actual);

    public Object getActual(){
        return actual;
    }

    protected IFunction getFunction(String searchChar) throws Exception {
        if (FUNCTION_EQUALS.contains(searchChar)) {
            return new Equals();
        }
        if (FUNCTION_NOT_EQUALS.contains(searchChar)) {
            return new DoseNotEquals();
        }
        if (FUNCTION_EQUALS_IGNORE_CASE.contains(searchChar)) {
            return new EqualsIgnoreCase();
        }
        if (FUNCTION_GREATER_THAN_OR_EQUALS.contains(searchChar)){
            return new GreaterThanOrEquals();
        }
        if (FUNCTION_LESS_THAN_OR_EQUALS.contains(searchChar)){
            return new LessThanOrEquals();
        }
        if (FUNCTION_GREATER_THAN.contains(searchChar)){
            return new GreaterThan();
        }
        if (FUNCTION_LESS_THAN.contains(searchChar)){
            return new LessThan();
        }
        if (FUNCTION_CONTAINS.contains(searchChar)) {
            return new Contains();
        }
        if (FUNCTION_NOT_CONTAINS.contains(searchChar)) {
            return new DoseNotContains();
        }
        if (FUNCTION_IS_EMPTY.contains(searchChar)) {
            return new IsEmpty();
        }
        if (FUNCTION_IS_NOT_EMPTY.contains(searchChar)) {
            return new IsNotEmpty();
        }
        if (FUNCTION_LIST.contains(searchChar)) {
            return new ListMap();
        }
        if (FUNCTION_REGEX.contains(searchChar)) {
            return new Regex();
        }
        if (!StringUtil.isEmpty(searchChar)){
            return (IFunction) Class.forName(searchChar).newInstance();
        }
        throw new ExecuteError(String.format("assert method '%s' not found", searchChar));
    }
}
