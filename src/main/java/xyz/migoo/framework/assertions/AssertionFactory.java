/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package xyz.migoo.framework.assertions;

import com.alibaba.fastjson.JSONObject;
import org.reflections.Reflections;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.assertions.function.AbstractAssertFunction;
import xyz.migoo.framework.assertions.function.Alias;
import xyz.migoo.framework.assertions.function.IFunction;
import xyz.migoo.framework.config.CaseKeys;
import xyz.migoo.framework.entity.Validate;
import xyz.migoo.simplehttp.Response;
import xyz.migoo.utils.TypeUtil;

import java.util.Set;

/**
 * @author xiaomi
 * @date 2019-04-13 22:12
 */
public class AssertionFactory extends AbstractAssertion {

    private static final Reflections FUNC_REFS = new Reflections("xyz.migoo.framework.assertions.function");
    private static final Set<Class<? extends AbstractAssertFunction>> FUNC_SUBS = FUNC_REFS.getSubTypesOf(AbstractAssertFunction.class);


    private static final Reflections ASSERT_REFS = new Reflections("xyz.migoo.framework.assertions");
    private static final Set<Class<? extends AssertionFactory>> ASSERT_SUBS = ASSERT_REFS.getSubTypesOf(AssertionFactory.class);

    private static final AssertionFactory FACTORY = new AssertionFactory();

    public static boolean assertThat(Validate validate, Response response) throws ExecuteError {
        try {
            FACTORY.setInstance(validate.getCheck());
            FACTORY.setActual(response);
            return FACTORY.assertThat(validate);
        } catch (Exception e) {
            throw new ExecuteError("assert error. " + e.getMessage(), e);
        }
    }

    private AbstractAssertion assertion;

    @Override
    public void setActual(Object actual) {
        assertion.setActual(actual);
    }

    private boolean assertThat(Validate validate) throws ExecuteError {
        boolean result = assertion.assertThat(JSONObject.parseObject(validate.toString()));
        validate.setActual(assertion.getActual());
        this.clear();
        return result;
    }

    @Override
    public boolean assertThat(JSONObject data) throws ExecuteError {
        try {
            IFunction function = getFunction(data.getString(CaseKeys.FUNC));
            data.put("actual", actual);
            return TypeUtil.booleanOf(function.assertTrue(data));
        } catch (Exception e) {
            throw new ExecuteError("assert error. " + e.getMessage(), e);
        }
    }

    private IFunction getFunction(String searchChar) throws ExecuteError, Exception {
        for (Class<? extends AbstractAssertFunction> sub : FUNC_SUBS) {
            Alias alias = sub.getAnnotation(Alias.class);
            if (Alias.Check.contains(searchChar, alias.aliasList())) {
                return sub.newInstance();
            }
        }
        throw new ExecuteError(String.format("assert method '%s' not found", searchChar));
    }

    private void setInstance(String check) throws Exception {
        for (Class<? extends AssertionFactory> sub : ASSERT_SUBS) {
            String[] aliasList = sub.getAnnotation(Alias.class).aliasList();
            boolean isJSONAssertion = (sub == JSONAssertion.class && Alias.Check.isJson(check, aliasList));
            assertion = isJSONAssertion || Alias.Check.contains(check, aliasList) ? sub.newInstance() : null;
            if (assertion instanceof JSONAssertion) {
                ((JSONAssertion) assertion).setJsonPath(check);
            }
            if (assertion != null) {return;}
        }
        assertion = (AbstractAssertion) Class.forName(check).newInstance();
    }

    private void clear() {
        assertion = null;
    }

    public static void main(String[] args) {
        System.out.println(null instanceof JSONAssertion);
    }
}
