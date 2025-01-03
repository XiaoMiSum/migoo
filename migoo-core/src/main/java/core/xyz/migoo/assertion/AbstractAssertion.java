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

import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.MiGooProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
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
        var loaders = ServiceLoader.load(Rule.class);
        for (var function : loaders) {
            for (String alias : function.getClass().getAnnotation(Alias.class).value()) {
                RULES.put(alias.toLowerCase(), function);
            }
        }
    }

    public VerifyResult getResult(SampleResult samplerResult) {
        var result = new VerifyResult(this.getClass());
        super.convertVariable();
        try {
            setActual(samplerResult);
            assertThat(result);
        } catch (Exception e) {
            result.setFailureMessage(e);
        }
        return result;
    }

    protected void assertThat(VerifyResult result) {
        var ruleStr = get(RULE) == null ? "==" : getPropertyAsString(RULE);
        setProperty("rule", ruleStr);
        var rule = RULES.get(ruleStr.toLowerCase(Locale.ROOT));
        if (Objects.isNull(rule)) {
            result.setFailureMessage(String.format("assert rule '%s' not found", ruleStr));
        } else {
            try {
                result.setSuccessful(rule.assertThat(get(ACTUAL), get(EXPECTED)));
            } catch (Exception e) {
                result.setFailureMessage(e);
            }
        }
        setContent(result);
    }

    protected abstract void setActual(SampleResult result);

    protected void setActual(Object actual) {
        getProperty().put(ACTUAL, actual);
    }

    protected void setContent(VerifyResult result) {
        String content = StringUtils.isNotEmpty(result.getContent()) ? result.getContent() : result.isSuccessful() ? "true" :
                String.format("expected value '%s', but found '%s'%s, rule: '%s'",
                        get(EXPECTED), get(ACTUAL), Objects.isNull(get(FIELD)) ? "" : (", field: " + get(FIELD)), get(RULE));
        setContent(result, content);
    }

    protected void setContent(VerifyResult result, String content) {
        MiGooProperty prop = new MiGooProperty(getProperty());
        prop.remove(VARIABLES);
        prop.put("result", content);
        result.setContent(prop.toString());
    }
}