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

import com.alibaba.fastjson2.JSON;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.processor.AbstractProcessor;
import core.xyz.migoo.processor.Preprocessor;
import core.xyz.migoo.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.Alias;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.xyz.migoo.http.HTTPSampleResult;
import protocol.xyz.migoo.http.config.HttpConfigItem;

/**
 * @author xiaomi
 */
@Alias(value = {"http_preprocessor", "http_pre_processor", "http"})
public class HTTPPreprocessor extends AbstractProcessor<HttpConfigItem, HTTPSampleResult> implements Preprocessor {

    static Logger logger = LoggerFactory.getLogger(HTTPPreprocessor.class);

    @Override
    protected void _process(ContextWrapper context) {
        var result = (DefaultSampleResult) context.getTestResult();
        result.sampleStart();
        result.setUrl(getClass().getName());
        result.setRequestData(JSON.toJSONBytes(config));
        result.setResponseData(JSON.toJSONBytes(config));
        logger.info("Debug Preprocessor");
        result.sampleEnd();
    }

    @Override
    protected HTTPSampleResult getTestResult() {
        return new HTTPSampleResult(runtime.getId(),
                StringUtils.isBlank(runtime.getTitle()) ? "Debug Preprocessor" : runtime.getTitle());
    }
}
