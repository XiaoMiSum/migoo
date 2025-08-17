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

package io.github.xiaomisum.ryze.protocol.http;

import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import org.apache.hc.core5.http.Header;

import java.util.List;

/**
 * @author xiaomi
 */
public abstract class HTTPRealResult extends SampleResult.Real {


    protected String version;

    protected List<Header> headers;

    public HTTPRealResult(byte[] bytes) {
        super(bytes);
    }

    public List<Header> headers() {
        return headers;
    }

    protected void header(StringBuilder buf) {
        if (headers == null || headers.isEmpty()) {
            return;
        }
        buf.append("\n");
        headers.forEach(header -> buf.append(header.getName()).append(": ").append(header.getValue()).append("\n"));
    }
}
