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

import assertions.migoo.xyz.assertions.JSONAssertion;
import assertions.migoo.xyz.assertions.ResponseAssertion;
import assertions.migoo.xyz.assertions.ResponseCodeAssertion;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.functions.FunctionsException;

/**
 * @author xiaomi
 * @date 2019-04-13 21:36
 */
public interface Assertion {


    /**
     * get expected values from JSONObject (data.get("expect"))
     * and invoke method to assert expected values
     * <p>
     * support type list:
     * {@linkplain JSONAssertion JSONAssertion}
     * {@linkplain ResponseAssertion ResponseAssertion}
     * {@linkplain ResponseCodeAssertion ResponseCodeAssertion}
     *
     * @param data Objects that hold expected values
     * @return assert result
     * @throws FunctionsException
     */
    boolean assertThat(JSONObject data) throws FunctionsException;

    /**
     * setting actual values
     * The parameter is response or Java class
     * <p>
     * See the concrete implementation class for details
     * {@linkplain JSONAssertion JSONAssertion}
     * {@linkplain ResponseAssertion ResponseAssertion}
     * {@linkplain ResponseCodeAssertion ResponseCodeAssertion}
     *
     * @param actual response or java class
     */
    void setActual(Object actual);

    /**
     * getting actual values
     * <p>
     * See the concrete implementation class for details
     * {@linkplain JSONAssertion JSONAssertion}
     * {@linkplain ResponseAssertion ResponseAssertion}
     * {@linkplain ResponseCodeAssertion ResponseCodeAssertion}
     *
     */
    Object getActual();
}
