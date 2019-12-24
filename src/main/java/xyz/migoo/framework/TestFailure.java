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


package xyz.migoo.framework;

import xyz.migoo.exception.AssertionFailure;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author xiaomi
 * @date 2019-08-10 14:53
 */
public class TestFailure {

    private AbstractTest fFailedTest;
    private Throwable fThrownException;


    /**
     * Constructs a TestFailure with the given test and exception.
     */
    public TestFailure(AbstractTest failedTest, Throwable thrownException) {
        fFailedTest= failedTest;
        fThrownException= thrownException;
    }
    /**
     * Gets the failed test.
     */
    public AbstractTest failedTest() {
        return fFailedTest;
    }
    /**
     * Gets the thrown exception.
     */
    public Throwable thrownException() {
        return fThrownException;
    }
    /**
     * Returns a short description of the failure.
     */
    @Override
    public String toString() {
        return fFailedTest + ": " + fThrownException.getMessage();
    }

    public String trace() {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        fThrownException.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
    public String exceptionMessage() {
        return fThrownException.getMessage();
    }
    public boolean isFailure() {
        return thrownException() instanceof AssertionFailure;
    }
}
