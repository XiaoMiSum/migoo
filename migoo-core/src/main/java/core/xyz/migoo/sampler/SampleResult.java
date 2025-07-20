/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.sampler;

import core.xyz.migoo.report.Result;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author xiaomi
 */
public abstract class SampleResult<T extends SampleResult<T>> extends Result<T> {

    public static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

    private String url;

    private byte[] requestData;

    private String requestDataAsString;

    private byte[] responseData;

    private String responseDataAsString;

    private LocalDateTime sampleStartTime;

    private LocalDateTime sampleEndTime;

    public SampleResult(String title) {
        super(title);
    }

    public SampleResult(String id, String title) {
        super(id, title);
    }

    public void sampleStart() {
        if (sampleStartTime == null) {
            setSampleEndTime(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }

    public void sampleEnd() {
        if (sampleEndTime == null) {
            setSampleEndTime(LocalDateTime.now(ZoneId.systemDefault()));
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    public String getRequestDataAsString() {
        if (StringUtils.isBlank(requestDataAsString)) {
            requestDataAsString = new String(requestData, DEFAULT_ENCODING);
        }
        return requestDataAsString;
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public void setResponseData(byte[] responseData) {
        this.responseData = responseData;
    }

    public String getResponseDataAsString() {
        if (StringUtils.isBlank(responseDataAsString)) {
            responseDataAsString = new String(responseData, DEFAULT_ENCODING);
        }
        return responseDataAsString;
    }

    public LocalDateTime getSampleStartTime() {
        return sampleStartTime;
    }

    public void setSampleStartTime(LocalDateTime sampleStartTime) {
        this.sampleStartTime = sampleStartTime;
    }

    public LocalDateTime getSampleEndTime() {
        return sampleEndTime;
    }

    public void setSampleEndTime(LocalDateTime sampleEndTime) {
        this.sampleEndTime = sampleEndTime;
    }

    public String getDuration() {
        var duration = Duration.between(sampleStartTime, sampleEndTime).toMillis() / 1000.00;
        return new BigDecimal(duration).setScale(2, RoundingMode.HALF_UP).doubleValue() + " s";
    }
}