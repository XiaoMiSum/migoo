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

import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

import java.util.List;

/**
 * @author xiaomi
 * @date 2019-08-10 14:51
 */
public class TestResult extends Result implements ITestResult {

    @Override
    public void init(ITest test) {
        TestCase t = (TestCase) test;
        super.init(t);
        setCheckers(t.checkers());
        setRequest(t.request());
        setResponse(t.response());
    }

    private List<TestChecker> checkers;

    private Request request;

    private Response response;

    @Override
    public List<TestChecker> getCheckers() {
        return this.checkers;
    }

    @Override
    public void setCheckers(List<TestChecker> checkers) {
        this.checkers = checkers;
    }

    @Override
    public Request getRequest() {
        return this.request;
    }

    @Override
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public Response getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(Response response) {
        this.response = response;
    }
}
