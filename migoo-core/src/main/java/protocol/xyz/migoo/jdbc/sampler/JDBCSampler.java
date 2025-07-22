package protocol.xyz.migoo.jdbc.sampler;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.sampler.AbstractSampler;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import core.xyz.migoo.testelement.sampler.Sampler;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;
import protocol.xyz.migoo.jdbc.RealJDBCRequest;
import protocol.xyz.migoo.jdbc.config.JDBCConfigureItem;

import static protocol.xyz.migoo.jdbc.JDBC.toJSONBytes;

@Alias(value = {"jdbc", "jdbc_sampler"})
public class JDBCSampler extends AbstractSampler<JDBCConfigureItem, JDBCSampler, DefaultSampleResult> implements Sampler<DefaultSampleResult>, JDBCConstantsInterface {

    @JSONField(serialize = false)
    private DruidDataSource datasource;

    private byte[] bytes;

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        result.sampleStart();
        try (var conn = datasource.getConnection(); var statement = conn.createStatement()) {
            var bool = statement.execute(runtime.config.getSql());
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
        datasource = (DruidDataSource) context.getAllVariablesWrapper().get(runtime.config.getDatasource());
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(new RealJDBCRequest(datasource.getUrl(), datasource.getUsername(), datasource.getPassword(), runtime.config.getSql()));
        result.setResponse(SampleResult.DefaultReal.build(bytes));
    }
}
