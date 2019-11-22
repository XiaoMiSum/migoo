package xyz.migoo.framework.assertions;

import com.alibaba.fastjson.JSONObject;
import org.reflections.Reflections;
import xyz.migoo.framework.assertions.function.*;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.utils.TypeUtil;

import java.util.Arrays;
import java.util.Set;

/**
 * @author xiaomi
 * @date 2019-04-13 21:57
 */
public abstract class AbstractAssertion implements Assertion{
    private static final Reflections REFLECTIONS = new Reflections("xyz.migoo.framework.assertions.function");
    private static final Set<Class<? extends AbstractAssertFunction>> SUB_TYPES = REFLECTIONS.getSubTypesOf(AbstractAssertFunction.class);

    protected Object actual;

    @Override
    public boolean assertThat(JSONObject data) throws ExecuteError {
        try {
            IFunction function = getFunction(data.getString(CaseKeys.FUNC));
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

    protected IFunction getFunction(String searchChar) throws ExecuteError, Exception {
        for (Class<? extends AbstractAssertFunction> sub : SUB_TYPES){
            Alias alias = sub.getAnnotation(Alias.class);
            if (Arrays.asList(alias.aliasList()).contains(searchChar)) {
                return sub.newInstance();
            }
        }
        throw new ExecuteError(String.format("assert method '%s' not found", searchChar));
    }
}
