/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.assertion;

import com.alibaba.fastjson2.JSON;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.MiGooProperty;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @author xiaomi
 */
public abstract class AbstractAssertion extends AbstractTestElement implements Serializable, Assertion {

    protected static final String FIELD = "field";
    protected static final String EXPECTED = "expected";
    protected static final String ACTUAL = "actual";
    private static final Map<String, Rule> RULES = new HashMap<>(100);
    private static final String RULE = "rule";

    static {
        ServiceLoader<Rule> loaders = ServiceLoader.load(Rule.class);
        for (Rule function : loaders) {
            for (String alias : function.getClass().getAnnotation(Alias.class).value()) {
                RULES.put(alias.toLowerCase(), function);
            }
        }
    }

    protected void assertThat(VerifyResult result) {
        convertVariable();
        String ruleStr = get(RULE) == null ? "==" : getPropertyAsString(RULE);
        Rule rule = RULES.get(ruleStr.toLowerCase(Locale.ROOT));
        if (rule == null) {
            result.setFailureMessage(String.format("assert rule '%s' not found", ruleStr));
        } else {
            try {
                prepare();
                List<Object> objects = new ArrayList<>();
                if (Objects.nonNull(get(EXPECTED)) && get(EXPECTED).getClass().isArray()) {
                    verifyArray(objects, result, rule);
                } else {
                    if (Objects.nonNull(get(EXPECTED))) {
                        objects.add(get(EXPECTED));
                    }
                    result.setSuccessful(rule.assertThat(get(ACTUAL), get(EXPECTED)));
                }
                getProperty().put(EXPECTED, objects.isEmpty() ? null : objects);
                result.setContext(result.isSuccessful() ? this.propToString() :
                        String.format("expected value '%s', but found '%s', rule: '%s'", objects, get(ACTUAL), ruleStr));
            } catch (Exception e) {
                result.setFailureMessage(e);
            }
        }
    }

    private void prepare() {
        if (!(get(EXPECTED) instanceof String expected)) {
            return;
        }
        if (JSON.isValidArray(expected) || JSON.isValidObject(expected) || !expected.contains(",")) {
            return;
        }
        String[] ss = ((String) get(EXPECTED)).split(",");
        for (int i = 0; i < ss.length; i++) {
            ss[i] = ss[i].trim();
        }
        getProperty().put(EXPECTED, ss);
    }

    private void verifyArray(List<Object> objects, VerifyResult result, Rule rule) {
        for (int i = 0; i < Array.getLength(get(EXPECTED)); i++) {
            Object obj = Array.get(get(EXPECTED), i);
            objects.add(obj);
            if (!result.isSuccessful()) {
                result.setSuccessful(rule.assertThat(get(ACTUAL), obj));
            }
        }
    }

    protected void setActual(Object actual) {
        getProperty().put(ACTUAL, actual);
    }

    public String propToString() {
        MiGooProperty prop = new MiGooProperty(getProperty());
        prop.remove(VARIABLES);
        return prop.toString();
    }
}