package protocol.xyz.migoo.jdbc.sampler;

import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.sampler.AbstractSampler;
import core.xyz.migoo.sampler.DefaultSampleResult;
import core.xyz.migoo.sampler.Sampler;
import core.xyz.migoo.testelement.Alias;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;
import protocol.xyz.migoo.jdbc.config.JDBCConfigureItem;

@Alias(value = {"jdbc", "jdbc_sampler"})
public class JDBCSampler extends AbstractSampler<JDBCConfigureItem, JDBCSampler, DefaultSampleResult> implements Sampler<DefaultSampleResult>, JDBCConstantsInterface {

    @Override
    protected void sample(ContextWrapper contextWrapper, DefaultSampleResult result) {

    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return null;
    }

    @Override
    protected void handleResponse(ContextWrapper contextWrapper, DefaultSampleResult result) {
        super.handleResponse(contextWrapper, result);
    }

    @Override
    protected void handleRequest(ContextWrapper contextWrapper, DefaultSampleResult result) {
        super.handleRequest(contextWrapper, result);
    }
}
