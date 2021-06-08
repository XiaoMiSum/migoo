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

package core.xyz.migoo.samplers;

import core.xyz.migoo.assertions.AssertionResult;
import core.xyz.migoo.variables.MiGooVariables;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class SampleResult implements Serializable {

    public static final Charset DEFAULT_HTTP_ENCODING = StandardCharsets.UTF_8;

    private static final byte[] EMPTY_BA = new byte[0];

    public static final String TEXT = "text";

    private final String title;

    private final int type;

    private String url;

    private String testClass;

    private MiGooVariables variables;

    private String samplerData = "";

    private byte[] responseData = EMPTY_BA;

    private String responseDataAsString;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<AssertionResult> assertionResults;

    private List<SampleResult> subResults;

    private Throwable throwable;

    private boolean success = true;

    public static SampleResult Failed(String title) {
        SampleResult result = new SampleResult(title);
        result.sampleStart();
        result.sampleEnd();
        return result;
    }

    public SampleResult(String title) {
        this.title = title;
        this.type = 0;
    }

    public SampleResult(String title, int type) {
        this.title = title;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setSuccessful(boolean success) {
        this.success = success;
    }

    public boolean isSuccessful() {
        return success;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public void setResponseData(byte[] responseData) {
        responseDataAsString = null;
        this.responseData = responseData == null ? EMPTY_BA : responseData;
    }

    public void setResponseData(String responseData) {
        responseDataAsString = null;
        this.responseData = responseData == null ? EMPTY_BA : responseData.getBytes(DEFAULT_HTTP_ENCODING);
    }

    public String getResponseDataAsString() {
        if (responseDataAsString == null) {
            responseDataAsString = new String(responseData, DEFAULT_HTTP_ENCODING);
        }
        return responseDataAsString;
    }

    public void sampleStart() {
        if (startTime == null) {
            setStartTime(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }

    public void sampleEnd() {
        if (endTime == null) {
            setEndTime(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }

    public String getSamplerData() {
        return samplerData;
    }

    public void setSamplerData(String samplerData) {
        this.samplerData = samplerData;
    }

    public List<AssertionResult> getAssertionResults() {
        return assertionResults;
    }

    public void setAssertionResults(List<AssertionResult> assertionResults) {
        this.assertionResults = assertionResults;
    }

    public List<SampleResult> getSubResults() {
        return subResults;
    }

    public void setSubResults(List<SampleResult> subResults) {
        this.subResults = subResults;
    }

    public MiGooVariables getVariables() {
        return variables;
    }

    public void setVariables(MiGooVariables variables) {
        this.variables = variables;
    }

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    public void setTestClass(Class<?> testClass) {
        this.testClass = testClass.getName();
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public boolean isException() {
        return throwable != null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.success = false;
        this.throwable = throwable;
    }
}