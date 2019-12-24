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
