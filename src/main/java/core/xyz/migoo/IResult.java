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
 * @date 2020/7/27 21:41
 */
public interface IResult extends ITestStatus {

    /**
     * The name of this TestResult, typically identical to the name of the method.
     *
     * @return The name of this TestResult, typically identical to the name of the method.
     */
    String getTestName();

    /**
     * set name for this TestResult, typically identical to the name of the method.
     *
     * @param name The name of this TestResult
     */
    void setTestName(String name);

    Object getTestId();

    void setTestId(Object id);

    /**
     * The status of this result, using one of the constants above.
     *
     * @return The status of this result, using one of the constants above.
     */
    int getStatus();

    /**
     * set status for this result
     *
     * @param status The status of this result
     */
    void setStatus(int status);


    /**
     * the start date of this test.
     *
     * @return the start date for this test, in milliseconds.
     */
    Date getStartTime();

    /**
     * set start date for this test.
     *
     * @param startTime the start date for this test.
     */
    void setStartTime(Date startTime);

    /**
     * the end date of this test.
     *
     * @return the end date for this test.
     */
    Date getEndTime();

    /**
     * set end date for this test.
     *
     * @param endTime the end date for this test.
     */
    void setEndTime(Date endTime);

    /**
     * set throwable for this test.
     *
     * @param throwable the throwable for this test
     */
    void setThrowable(Throwable throwable);

    /**
     * the throwable of this test.
     *
     * @return the throwable for this test.
     */
    Throwable getThrowable();

    /**
     * the throwable as string of this test.
     *
     * @return the throwable as string for this test.
     */
    String getThrowableAsString();

    /**
     * the status is passed of this test.
     *
     * @return the status is successful of this test.
     */
    boolean isPassed();

    /**
     * the status is skipped of this test.
     *
     * @return the status is skipped of this test.
     */
    boolean isSkipped();

    /**
     * the status is error of this test.
     *
     * @return the status is error of this test.
     */
    boolean isError();

    /**
     * the status is failed of this test.
     *
     * @return the status is failed of this test.
     */
    boolean isNotPassed();
}
