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

package protocol.xyz.migoo.http.processor;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.processor.AbstractProcessor;
import core.xyz.migoo.processor.Postprocessor;
import core.xyz.migoo.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.Alias;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.http.HTTPConstantsInterface;
import protocol.xyz.migoo.http.HttpClient;
import protocol.xyz.migoo.http.RealHTTPRequest;
import protocol.xyz.migoo.http.RealHTTPResponse;
import protocol.xyz.migoo.http.config.HttpConfigureItem;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

/**
 * @author xiaomi
 */
@Alias(value = {"http_postprocessor", "http_post_processor", "http"})
public class HTTPPostprocessor extends AbstractProcessor<HttpConfigureItem, HTTPPostprocessor, DefaultSampleResult> implements Postprocessor, HTTPConstantsInterface {

    @JSONField(serialize = false)
    private Request request;
    @JSONField(serialize = false)
    private Response response;

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, StringUtils.isBlank(title) ? "HTTP 后置处理器" : title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        try {
            result.sampleStart();
            response = request.execute();
        } catch (Exception e) {
            result.setThrowable(e);
        } finally {
            result.sampleEnd();
        }
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        // 1. 合并配置项
        var datasource = StringUtils.isBlank(config.getDatasource()) ? DEF_REF_NAME_KEY : config.getDatasource();
        var otherConfig = (HttpConfigureItem) context.getLocalVariablesWrapper().get(datasource);
        runtime.setConfig(runtime.getConfig().merge(otherConfig));
        // 2. 创建http对象
        request = HttpClient.build(runtime.getConfig());
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(new RealHTTPRequest(request));
        result.setResponse(new RealHTTPResponse(response));
    }
}
