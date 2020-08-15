/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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
 *
 */


package core.xyz.migoo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import core.xyz.migoo.utils.StringUtil;

/**
 * @author xiaomi
 * @date 2019-08-10 20:36
 */
public class Validator {

    private String assertion;

    private String field;

    private Object expected;

    private Object actual;

    private String rule;

    private String result;

    @JSONField(serialize = false)
    private Throwable throwable;

    public String getAssertion() {
        return StringUtil.isEmpty(assertion) ? "JSONAssertion" : assertion;
    }

    public void setAssertion(String assertion) {
        this.assertion = assertion;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getExpected() {
        return expected;
    }

    public void setExpected(Object expected) {
        this.expected = expected;
    }

    public Object getActual() {
        return actual;
    }

    public void setActual(Object actual) {
        this.actual = actual;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getResult() {
        return result == null ? "skipped" : result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    @JSONField(serialize = false)
    public boolean isSuccess() {
        return "passed".equals(getResult());
    }

    @JSONField(serialize = false)
    public boolean isFailure() {
        return "failed".equals(getResult());
    }

    @JSONField(serialize = false)
    public boolean isSkipped() {
        return "skipped".equals(getResult());
    }

    @JSONField(serialize = false)
    public boolean isError() {
        return "error".equals(getResult());
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
