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
import core.xyz.migoo.assertions.rules.Alias;
import core.xyz.migoo.assertions.rules.IRule;
import core.xyz.migoo.utils.TypeUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author xiaomi
 * @date 2019-04-13 21:57
 */
public abstract class AbstractAssertion implements Assertion {

    private static final Map<String, IRule> RULES = new HashMap<>(100);

    static {
        ServiceLoader<IRule> loaders = ServiceLoader.load(IRule.class);
        for (IRule function : loaders) {
            for (String alias : function.getClass().getAnnotation(Alias.class).aliasList()) {
                RULES.put(alias, function);
            }
        }
    }

    protected Object actual;

    protected String field;

    @Override
    public Object getActual() {
        return actual;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public boolean assertThat(JSONObject data) throws Exception, AssertionError {
        IRule rule = RULES.get(data.getString("rule"));
        if (rule == null) {
            throw new Exception(String.format("assert rule '%s' not found", data.get("rule")));
        }
        data.put("actual", actual);
        return TypeUtil.booleanOf(rule.assertTrue(data));
    }
}
