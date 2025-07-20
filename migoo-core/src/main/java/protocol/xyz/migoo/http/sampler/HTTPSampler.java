package protocol.xyz.migoo.http.sampler;

import com.alibaba.fastjson2.JSON;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.sampler.AbstractSampler;
import core.xyz.migoo.sampler.Sampler;
import core.xyz.migoo.testelement.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.xyz.migoo.http.HTTPSampleResult;
import protocol.xyz.migoo.http.config.HttpConfigItem;

@Alias(value = {"http", "http_sampler"})
public class HTTPSampler extends AbstractSampler<HttpConfigItem, HTTPSampler, HTTPSampleResult> implements Sampler<HTTPSampleResult> {

    static Logger logger = LoggerFactory.getLogger(HTTPSampler.class);

    public HTTPSampler() {
        super();
    }

    @Override
    protected HTTPSampleResult getTestResult() {
        return new HTTPSampleResult(id, title);
    }

    @Override
    protected void sample(ContextWrapper contextWrapper, HTTPSampleResult result) {
        runtime.getTestResult().sampleStart();
        result.setUrl(getClass().getName());
        result.setRequestData(JSON.toJSONBytes(config));
        result.setResponseData(JSON.toJSONBytes(config));
        logger.info("Debug Sampler");
        result.sampleEnd();
    }

    @Override
    protected void handleRequest(ContextWrapper contextWrapper, HTTPSampleResult result) {
        // todo 这里实现请求前的的数据处理
        super.handleRequest(contextWrapper, result);
    }

    @Override
    protected void handleResponse(ContextWrapper contextWrapper, HTTPSampleResult result) {
        // todo 这里实现请求后的数据处理
        super.handleResponse(contextWrapper, result);
    }
}
