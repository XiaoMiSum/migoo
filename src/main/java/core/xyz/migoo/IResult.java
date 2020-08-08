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

/**
 * @author xiaomi
 * @date 2020/7/27 21:41
 */
public interface IResult {

    /**
     * @param test
     */
    default void init(AbstractTest test){
        setTestName(test.getTestName());
        setStatus(test.getStatus());
        setThrowable(test.getThrowable());
        setStartTime(test.getStartTime());
        setEndTime(test.getEndTime());
    }

    /**
     * The name of this TestResult, typically identical to the name of the method.
     * @return The name of this TestResult, typically identical to the name of the method.
     */
    String getTestName();

    /**
     * set name of this TestResult, typically identical to the name of the method.
     * @param name The name of this TestResult
     */
    void setTestName(String name);

    /**
     * The status of this result, using one of the constants above.
     * @return The status of this result, using one of the constants above.
     */
    String getStatus();

    /**
     * set status of this result
     * @param status The status of this result
     */
    void setStatus(String status);


    /**
     * the start date for this test, in milliseconds.
     * @return the start date for this test, in milliseconds.
     */
    long getStartTime();

    /**
     * set start date for this test, in milliseconds.
     * @param startTime the start date for this test, in milliseconds.
     */
    void setStartTime(long startTime);

    /**
     * the end date for this test, in milliseconds.
     * @return the end date for this test, in milliseconds.
     */
    long getEndTime();

    /**
     * set end date for this test, in milliseconds.
     * @param endTime the end date for this test, in milliseconds.
     */
    void setEndTime(long endTime);

    void setThrowable(Throwable throwable);

    Throwable getThrowable();

    String getThrowableAsString();
}
