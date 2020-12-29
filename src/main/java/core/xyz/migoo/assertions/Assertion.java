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

import components.xyz.migoo.assertions.JSONAssertion;
import components.xyz.migoo.assertions.ResponseAssertion;
import core.xyz.migoo.Validator;
import xyz.migoo.simplehttp.Response;

/**
 * @author xiaomi
 * @date 2019-04-13 21:36
 */
public interface Assertion {


    /**
     *
     * {@linkplain JSONAssertion JSONAssertion}
     * {@linkplain ResponseAssertion ResponseAssertion}
     *
     * @param validator Objects that hold expected values
     * @throws Exception Exception
     */
    void assertThat(Validator validator) throws Exception;

    /**
     * setting actual values
     * The parameter is response or Java class
     * <p>
     * See the concrete implementation class for details
     * {@linkplain JSONAssertion JSONAssertion}
     * {@linkplain ResponseAssertion ResponseAssertion}
     *
     * @param actual xyz.migoo.simplehttp.Response
     */
    void setActual(Response actual);

    /**
     * getting actual values
     * <p>
     * See the concrete implementation class for details
     * {@linkplain JSONAssertion JSONAssertion}
     * {@linkplain ResponseAssertion ResponseAssertion}
     * @return actual values
     */
    Object getActual();
}
