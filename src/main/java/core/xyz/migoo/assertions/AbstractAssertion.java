/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package core.xyz.migoo.assertions;

import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author xiaomi
 */
public abstract class AbstractAssertion extends AbstractTestElement implements Serializable, Assertion  {

    private static final Map<String, Rule> RULES = new HashMap<>(100);

    protected static final String FIELD = "field";

    private static final String RULE = "rule";

    protected static final String EXPECTED = "expected";

    protected static final String ACTUAL = "actual";

    static {
        ServiceLoader<Rule> loaders = ServiceLoader.load(Rule.class);
        for (Rule function : loaders) {
            for (String alias : function.getClass().getAnnotation(Alias.class).aliasList()) {
                RULES.put(alias.toLowerCase(), function);
            }
        }
    }

    protected void assertThat(AssertionResult result) {
        convertVariable();
        String ruleStr = get(RULE) == null ? "==" : getPropertyAsString(RULE);
        Rule rule = RULES.get(ruleStr.toLowerCase(Locale.ROOT));
        if (rule == null) {
            result.setFailureMessage(String.format("assert rule '%s' not found", ruleStr));
        } else {
            try {
                result.setSuccessful(rule.assertThat(get(ACTUAL), get(EXPECTED)));
                result.setContext(result.isSuccessful() ? this.toString() :
                        String.format("expected value '%s', but found '%s', rule: '%s'", get(EXPECTED), get(ACTUAL), ruleStr));
            } catch (Exception e) {
                result.setFailureMessage(e);
            }
       }
    }

    protected void setActual(Object actual) {
        getProperty().put(ACTUAL, actual);
    }

    @Override
    public String toString(){
        return getProperty().toString();
    }
}