package protocol.xyz.migoo.http.sampler;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.builder.*;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.sampler.AbstractSampler;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.Sampler;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.http.HTTPClient;
import protocol.xyz.migoo.http.HTTPConstantsInterface;
import protocol.xyz.migoo.http.RealHTTPRealRequest;
import protocol.xyz.migoo.http.RealHTTPRealResponse;
import protocol.xyz.migoo.http.config.HTTPConfigureItem;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

import java.util.Objects;

@Alias(value = {"http", "http_sampler", "https"})
public class HTTPSampler extends AbstractSampler<HTTPSampler, HTTPConfigureItem, DefaultSampleResult> implements Sampler<DefaultSampleResult>, HTTPConstantsInterface {

    @JSONField(serialize = false)
    private Request request;
    @JSONField(serialize = false)
    private Response response;

    public HTTPSampler() {
        super();
    }

    public HTTPSampler(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
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
            result.setTrack(e);
        } finally {
            result.sampleEnd();
        }
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        // 1. 合并配置项
        var localConfig = Objects.isNull(runtime.getConfig()) ? new HTTPConfigureItem() : runtime.getConfig();
        var datasource = StringUtils.isBlank(localConfig.getRef()) ?
                DEF_REF_NAME_KEY : localConfig.getRef();
        var otherConfig = (HTTPConfigureItem) context.getLocalVariablesWrapper().get(datasource);
        runtime.setConfig(localConfig.merge(otherConfig));
        // 2. 创建http对象
        request = HTTPClient.build(runtime.getConfig());
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(new RealHTTPRealRequest(request));
        result.setResponse(new RealHTTPRealResponse(response));
    }

    /**
     * HTTP 取样器构建器
     */
    public static class Builder extends AbstractSampler.Builder<HTTPSampler, Builder, HTTPConfigureItem,
            HTTPConfigureItem.Builder, DefaultConfigureElementsBuilder, DefaultPreprocessorsBuilder, DefaultPostprocessorsBuilder,
            DefaultAssertionsBuilder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public HTTPSampler build() {
            return new HTTPSampler(this);
        }

        @Override
        protected DefaultConfigureElementsBuilder getConfiguresBuilder() {
            return DefaultConfigureElementsBuilder.builder();
        }

        @Override
        protected DefaultAssertionsBuilder getAssertionsBuilder() {
            return DefaultAssertionsBuilder.builder();
        }

        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }

        @Override
        protected DefaultPreprocessorsBuilder getPreprocessorsBuilder() {
            return DefaultPreprocessorsBuilder.builder();
        }

        @Override
        protected DefaultPostprocessorsBuilder getPostprocessorsBuilder() {
            return DefaultPostprocessorsBuilder.builder();
        }

        @Override
        protected HTTPConfigureItem.Builder getConfigureItemBuilder() {
            return HTTPConfigureItem.builder();
        }
    }
}
