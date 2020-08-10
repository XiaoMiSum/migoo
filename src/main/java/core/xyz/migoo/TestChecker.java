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
 * @date 2019-08-10 20:36
 */
public class TestChecker {

    private String checker;

    private Object expect;

    private Object actual;

    private String func;

    private String result;

    private Throwable throwable;

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public Object getExpect() {
        return expect;
    }

    public void setExpect(Object expect) {
        this.expect = expect;
    }

    public Object getActual() {
        return actual;
    }

    public void setActual(Object actual) {
        this.actual = actual;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public String getResult() {
        return result == null ? "skipped" : result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean isSuccess() {
        return "success".equals(getResult());
    }
    public boolean isFailure() {
        return "failure".equals(getResult());
    }
    public boolean isSkipped() {
        return "skipped".equals(getResult());
    }
    public boolean isError() {
        return "error".equals(getResult());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"checker\": \"").append(checker).append("\"").append(",");
        if (expect instanceof String) {
            sb.append("\"expect\": \"").append(expect).append("\"").append(",");
        } else {
            sb.append("\"expect\": ").append(expect).append(",");
        }
        return sb.append("\"actual\": \"").append(actual).append("\"").append(",")
                .append("\"func\": \"").append(func).append("\"").append(",")
                .append("\"result\": \"").append(getResult()).append("\"").append("}")
                .toString();
    }
}
