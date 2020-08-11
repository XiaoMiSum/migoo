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


package core.xyz.migoo.assertions;

import components.migoo.xyz.assertions.JSONAssertion;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.assertions.function.Alias;
import core.xyz.migoo.functions.FunctionException;
import xyz.migoo.simplehttp.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author xiaomi
 * @date 2019-04-13 22:12
 */
public class AssertionFactory {

    private static final Map<String, Assertion> SELF_DEFINED_ASSERTION = new HashMap<>(100);

    static {
        ServiceLoader<Assertion> loaders = ServiceLoader.load(Assertion.class);
        for (Assertion assertion : loaders) {
            SELF_DEFINED_ASSERTION.put(assertion.getClass().getSimpleName().toUpperCase(), assertion);
        }
    }

    private static final AssertionFactory FACTORY = new AssertionFactory();

    public static boolean assertThat(JSONObject checker, Response response) throws FunctionException {
        FACTORY.setInstance(checker.getString("checker"));
        FACTORY.setActual(response);
        return FACTORY.assertThat(checker);
    }

    private Assertion assertion;

    private final Map<String, AbstractAssertion> defaultAssertion = new HashMap<>(3);

    public void setActual(Object actual) {
        assertion.setActual(actual);
    }

    private boolean assertThat(JSONObject checker) throws FunctionException {
        checker.put("actual", assertion.getActual());
        boolean result = assertion.assertThat(checker);
        this.clear();
        return result;
    }

    private void setInstance(String check) throws FunctionException {
        this.loadDefaultAssertion();
        String type =  Alias.Check.isJson(check, JSONAssertion.class.getAnnotation(Alias.class).aliasList()) ?
                JSONAssertion.class.getSimpleName().toUpperCase() : check.toUpperCase();
        assertion = defaultAssertion.get(type) == null ? SELF_DEFINED_ASSERTION.get(type) : defaultAssertion.get(type);
        if (assertion instanceof JSONAssertion) {
            ((JSONAssertion)assertion).setJsonPath(check);
        }
        if (assertion == null) {
            throw new FunctionException("assertion not found: " + check);
        }
    }

    private void clear() {
        assertion = null;
    }

    private void loadDefaultAssertion() {
        if (defaultAssertion.isEmpty()) {
            ServiceLoader<AbstractAssertion> loaders = ServiceLoader.load(AbstractAssertion.class);
            for (AbstractAssertion assertion : loaders) {
                if (assertion instanceof JSONAssertion) {
                    defaultAssertion.put(assertion.getClass().getSimpleName().toUpperCase(), assertion);
                } else {
                    String[] aliasList = assertion.getClass().getAnnotation(Alias.class).aliasList();
                    for (String alias : aliasList) {
                        defaultAssertion.put(alias.toUpperCase(), assertion);
                    }
                }
            }
        }
    }
}
