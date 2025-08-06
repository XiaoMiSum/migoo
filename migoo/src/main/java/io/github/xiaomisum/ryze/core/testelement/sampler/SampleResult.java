/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package io.github.xiaomisum.ryze.core.testelement.sampler;

import io.github.xiaomisum.ryze.core.Result;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author xiaomi
 */
public abstract class SampleResult extends Result {

    private LocalDateTime sampleStartTime;
    private LocalDateTime sampleEndTime;
    private Real request;
    private Real response;

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

    public Real getRequest() {
        return request;
    }

    public void setRequest(Real request) {
        this.request = request;
    }

    public Real getResponse() {
        return response;
    }

    public void setResponse(Real response) {
        this.response = response;
    }


    public static abstract class Real {

        private final byte[] bytes;
        private String bytesAsString;

        public Real(byte[] bytes) {
            this.bytes = bytes;
        }

        public abstract String format();

        public byte[] bytes() {
            return bytes;
        }

        public String bytesAsString() {
            if (bytesAsString == null) {
                bytesAsString = (bytes == null || bytes.length == 0) ? "" : new String(bytes);
            }
            return bytesAsString;
        }
    }

    public static class DefaultReal extends Real {

        private DefaultReal(byte[] bytes) {
            super(bytes);
        }

        public static DefaultReal build(byte[] bytes) {
            return new DefaultReal(bytes);
        }

        @Override
        public String format() {
            return bytesAsString();
        }
    }
}