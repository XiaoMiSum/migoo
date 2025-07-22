package protocol.xyz.migoo.jdbc.processor;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.processor.AbstractProcessor;
import core.xyz.migoo.testelement.processor.Preprocessor;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;
import protocol.xyz.migoo.jdbc.RealJDBCRequest;
import protocol.xyz.migoo.jdbc.config.JDBCConfigureItem;

import static protocol.xyz.migoo.jdbc.JDBC.toJSONBytes;

@Alias(value = {"jdbc_preprocessor", "jdbc_pre_processor", "jdbc"})
public class JDBCPreprocessor extends AbstractProcessor<JDBCConfigureItem, JDBCPreprocessor, DefaultSampleResult> implements Preprocessor, JDBCConstantsInterface {

    @JSONField(serialize = false)
    private DruidDataSource datasource;

    @JSONField(serialize = false)
    private byte[] bytes;

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, StringUtils.isBlank(title) ? "JDBC 前置处理器" : title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        result.sampleStart();
        try (var conn = datasource.getConnection(); var statement = conn.createStatement()) {
            var bool = statement.execute(runtime.getConfig().getSql());
            bytes = toJSONBytes(bool, statement);
        } catch (Exception e) {
            result.setTrack(e);
        } finally {
            result.sampleEnd();
        }
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        datasource = (DruidDataSource) context.getLocalVariablesWrapper().get(runtime.getConfig().getDatasource());
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(new RealJDBCRequest(datasource.getUrl(), datasource.getUsername(), datasource.getPassword(), runtime.getConfig().getSql()));
        result.setResponse(SampleResult.DefaultReal.build(bytes));
    }
}
