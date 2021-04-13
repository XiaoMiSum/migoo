/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
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
 */

package core.xyz.migoo.assertions;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AssertionResult {

    public static final String RESPONSE_WAS_NULL = "Response was null";

    private final String name;

    private boolean failure;

    private boolean error;

    private String failureMessage;

    private String context;

    public AssertionResult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isSuccessful() {
        return !failure && !error;
    }

    public boolean isFailure() {
        return failure;
    }

    public boolean isError() {
        return error;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setError(boolean e) {
        error = e;
    }

    public void setFailure(boolean f) {
        failure = f;
    }

    public void setFailureMessage(String message) {
        failureMessage = message;
    }

    public void setFailureMessage(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        throwable.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        failureMessage = buffer.toString();
    }

    public AssertionResult setResultForFailure(String message) {
        error = false;
        failure = true;
        failureMessage = message;
        return this;
    }

    public AssertionResult setResultForNull() {
        error = false;
        failure = true;
        failureMessage = RESPONSE_WAS_NULL;
        return this;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}