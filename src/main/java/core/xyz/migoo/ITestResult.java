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

import core.xyz.migoo.step.TestStep;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

import java.util.List;

/**
 * @author xiaomi
 * @date 2020/7/27 22:30
 */
public interface ITestResult {

    /**
     * the validators of this result.
     *
     * @return the validators of this result.
     */
    List<Validator> getValidators();

    /**
     * set the validators for this result.
     *
     * @param validators the validators for this result.
     */
    void setValidators(List<Validator> validators);

    List<TestStep> getSteps();

    void setSteps(List<TestStep> steps);

    /**
     * the request of this result.
     *
     * @return the request of this result.
     */
    Request getRequest();

    /**
     * set the request for this result.
     *
     * @param request the request for this result.
     */
    void setRequest(Request request);

    /**
     * the response of this result.
     *
     * @return the response of this result.
     */
    Response getResponse();

    /**
     * set response request for this result.
     *
     * @param response the response for this result.
     */
    void setResponse(Response response);

}
