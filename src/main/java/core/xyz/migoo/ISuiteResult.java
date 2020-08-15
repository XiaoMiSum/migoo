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

import java.util.List;

/**
 * @author xiaomi
 * @date 2019-08-10 14:51
 */
public interface ISuiteResult {

    /**
     * The test results of this result.
     *
     * @return test results of this result.
     */
    List<IResult> getTestResults();

    /**
     * set test results for this result.
     * @param results test results for this result
     */
    void setTestResults(List<IResult> results);

    /**
     * add test result for this result.
     * @param result test result for this result
     */
    void addTestResult(IResult result);

    /**
     * The size of this result.
     *
     * @return size of this result.
     */
    int size();

    /**
     * add success for this result.
     */
    void addSuccess();

    /**
     * return count success of this result.
     *
     * @return count success of this result.
     */
    int getSuccessCount();

    /**
     * add error for this result.
     */
    void addError();

    /**
     * return count error of this result.
     *
     * @return count error of this result.
     */
    int getErrorCount();

    /**
     * add failure for this result.
     */
    void addFailure();

    /**
     * return count failure of this result.
     *
     * @return count failure of this result.
     */
    int getFailureCount();

    /**
     * add skip for this result.
     */
    void addSkip();

    /**
     * return count skip of this result.
     *
     * @return count skip of this result.
     */
    int getSkipCount();
}
