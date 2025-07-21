package protocol.xyz.migoo.jdbc.processor;

import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.processor.AbstractProcessor;
import core.xyz.migoo.processor.Postprocessor;
import core.xyz.migoo.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.Alias;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;
import protocol.xyz.migoo.jdbc.config.JDBCConfigureItem;

@Alias(value = {"jdbc_postprocessor", "jdbc_post_processor", "jdbc"})
public class JDBCPostprocessor extends AbstractProcessor<JDBCConfigureItem, JDBCPostprocessor, DefaultSampleResult> implements Postprocessor, JDBCConstantsInterface {

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {

    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, StringUtils.isBlank(title) ? "JDBC 前置处理器" : title);
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        // todo
        super.handleRequest(context, result);
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        // todo
        super.handleResponse(context, result);
    }
}
