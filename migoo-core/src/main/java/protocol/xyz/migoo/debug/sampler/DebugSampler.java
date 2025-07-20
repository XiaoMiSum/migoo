package protocol.xyz.migoo.debug.sampler;

import com.alibaba.fastjson2.JSON;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.sampler.AbstractSampler;
import core.xyz.migoo.sampler.DefaultSampleResult;
import core.xyz.migoo.sampler.Sampler;
import core.xyz.migoo.testelement.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.xyz.migoo.debug.config.DebugConfigItem;
import protocol.xyz.migoo.debug.config.DebugDefaults;

@Alias(value = {"debug", "debug_sampler"}, config = DebugDefaults.class)
public class DebugSampler extends AbstractSampler<DebugConfigItem, DebugSampler, DefaultSampleResult>
        implements Sampler<DefaultSampleResult> {

    static Logger logger = LoggerFactory.getLogger(DebugSampler.class);

    public DebugSampler() {
        super();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, title);
    }

    @Override
    protected void sample(ContextWrapper contextWrapper, DefaultSampleResult result) {
        runtime.getTestResult().sampleStart();
        result.setUrl(getClass().getName());
        result.setRequestData(JSON.toJSONBytes(config));
        result.setResponseData(JSON.toJSONBytes(config));
        logger.info("Debug Sampler");
        result.sampleEnd();
    }
}
