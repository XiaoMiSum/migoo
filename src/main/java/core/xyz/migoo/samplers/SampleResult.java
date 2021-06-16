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
import core.xyz.migoo.report.Result;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SampleResult extends Result {

    public static final Charset DEFAULT_HTTP_ENCODING = StandardCharsets.UTF_8;

    private static final byte[] EMPTY_BA = new byte[0];

    public static final String TEXT = "text";

    private String url;

    private String testClass;

    private String samplerData = "";

    private byte[] responseData = EMPTY_BA;

    private String responseDataAsString;

    private List<AssertionResult> assertionResults;

    public SampleResult(String title) {
        super(title);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    public void setTestClass(Class<?> testClass) {
        this.testClass = testClass.getName();
    }

    public void setSamplerData(SampleResult result) {
        this.samplerData = result.getSamplerData();
        this.responseData = result.getResponseData();
        this.url = result.getUrl();
        this.testClass = result.getTestClass();
        super.setSuccessful(result.isSuccessful());
        this.setStartTime(result.getStartTime());
        this.setEndTime(result.getEndTime());
        this.setThrowable(result.getThrowable());
    }
}