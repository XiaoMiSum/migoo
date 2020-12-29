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

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.Validator;
import core.xyz.migoo.assertions.rules.Alias;
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

    private static final Map<String, AbstractAssertion> DEFAULT_ASSERTION = new HashMap<>(2);

    static {
        for (AbstractAssertion assertion : ServiceLoader.load(AbstractAssertion.class)) {
            String[] aliasList = assertion.getClass().getAnnotation(Alias.class).aliasList();
            for (String alias : aliasList) {
                DEFAULT_ASSERTION.put(alias.toUpperCase(), assertion);
            }
        }
        for (Assertion assertion : ServiceLoader.load(Assertion.class)) {
            Alias alias = assertion.getClass().getAnnotation(Alias.class);
            if (alias != null) {
                for (String aliasStr : alias.aliasList()) {
                    SELF_DEFINED_ASSERTION.put(aliasStr.toUpperCase(), assertion);
                }
            }
            SELF_DEFINED_ASSERTION.put(assertion.getClass().getSimpleName().toUpperCase(), assertion);
        }
    }

    private static final AssertionFactory FACTORY = new AssertionFactory();

    public static void put(Class<? extends Assertion> clz) throws Exception {
        SELF_DEFINED_ASSERTION.put(clz.getSimpleName().toUpperCase(), clz.newInstance());
    }

    public static Validator assertThat(JSONObject data, Response response) {
        Validator validator = data.toJavaObject(Validator.class);
        try {
            FACTORY.setInstance(validator);
            FACTORY.setActual(response);
            FACTORY.assertThat(validator);
        } catch (Exception e) {
            validator.setThrowable(e);
        } finally {
            FACTORY.clear();
        }
        return validator;
    }

    private Assertion assertion;

    public void setActual(Response actual) {
        assertion.setActual(actual);
    }

    private void assertThat(Validator validator) throws Exception {
        validator.setActual(assertion.getActual());
        assertion.assertThat(validator);
    }

    private void setInstance(Validator validator) throws Exception {
        String type = validator.getAssertion() == null ? "JSON" : validator.getAssertion().toUpperCase();
        assertion = DEFAULT_ASSERTION.get(type) == null ? SELF_DEFINED_ASSERTION.get(type) : DEFAULT_ASSERTION.get(type);
        if (assertion == null && validator.getPackName() != null) {
            assertion = (Assertion) Class.forName(validator.getPackName() + "." + validator.getAssertion()).newInstance();
            SELF_DEFINED_ASSERTION.put(type, assertion);
        } else if (assertion == null) {
            throw new Exception("assertion not found: " + validator.getAssertion());
        } else if (assertion instanceof AbstractAssertion) {
            ((AbstractAssertion) assertion).setField(validator.getField());
        }
        validator.setAssertion(assertion.getClass().getSimpleName());
    }

    private void clear() {
        assertion = null;
    }
}
