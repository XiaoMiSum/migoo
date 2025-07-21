package protocol.xyz.migoo.debug.sampler;

import com.alibaba.fastjson2.JSON;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.sampler.AbstractSampler;
import core.xyz.migoo.sampler.DefaultSampleResult;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.sampler.Sampler;
import core.xyz.migoo.testelement.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.xyz.migoo.debug.config.DebugConfigureItem;

@Alias(value = {"debug", "debug_sampler"})
public class DebugSampler extends AbstractSampler<DebugConfigureItem, DebugSampler, DefaultSampleResult>
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
        try {
            result.sampleStart();
            byte[] bytes = JSON.toJSONBytes(config);
            result.setRequest(SampleResult.DefaultReal.build(bytes));
            result.setResponse(SampleResult.DefaultReal.build(bytes));
            logger.info("Debug Sampler");
            result.sampleEnd();
        } catch (Exception e) {
            result.setThrowable(e);
        }
    }
}
