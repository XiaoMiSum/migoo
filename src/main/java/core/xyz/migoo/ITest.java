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

import java.util.Date;

/**
 * @author xiaomi
 * @date 2019-08-10 11:02
 */
public interface ITest extends ITestStatus {

    /**
     * The name of test instance(s).
     *
     * @return name associated with a particular instance of a test.
     */
    String getTestName();

    /**
     * Returns count of the test.
     * can return zero.
     *
     * @return count of this test
     */
    int countTestCases();

    /**
     * running the test
     *
     * @return the result of this test
     */
    IResult run();

    /**
     * set start date for this test.
     */
    void start();

    /**
     * the start date of this test.
     *
     * @return the start date for this test.
     */
    Date getStartTime();

    /**
     * set end date for this test.
     */
    void end();

    /**
     * the end date of this test.
     *
     * @return the end date for this test.
     */
    Date getEndTime();

    /**
     * Returns status of this test.
     *
     * @return the status of this test.
     */
    int getStatus();

    /**
     * set status for this test.
     * @param status status for this test
     */
    void setStatus(int status);

    /**
     * Returns throwable of this test.
     *
     * @return the throwable of this test.
     */
    Throwable getThrowable();

    /**
     * set throwable for this test.
     * @param throwable throwable for this test
     */
    void setThrowable(Throwable throwable);

}
