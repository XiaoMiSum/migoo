package protocol.xyz.migoo.http.sampler;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.sampler.AbstractSampler;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.Sampler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.xyz.migoo.http.HTTPClient;
import protocol.xyz.migoo.http.HTTPConstantsInterface;
import protocol.xyz.migoo.http.RealHTTPRequest;
import protocol.xyz.migoo.http.RealHTTPResponse;
import protocol.xyz.migoo.http.config.HTTPConfigureItem;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

@Alias(value = {"http", "http_sampler"})
public class HTTPSampler extends AbstractSampler<HTTPConfigureItem, HTTPSampler, DefaultSampleResult> implements Sampler<DefaultSampleResult>, HTTPConstantsInterface {

    static Logger logger = LoggerFactory.getLogger(HTTPSampler.class);

    @JSONField(serialize = false)
    private Request request;
    @JSONField(serialize = false)
    private Response response;

    public HTTPSampler() {
        super();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, title);
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
        var otherConfig = (HTTPConfigureItem) context.getLocalVariablesWrapper().get(datasource);
        runtime.config = runtime.config.merge(otherConfig);
        // 2. 创建http对象
        request = HTTPClient.build(runtime.config);
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(new RealHTTPRequest(request));
        result.setResponse(new RealHTTPResponse(response));
    }
}
