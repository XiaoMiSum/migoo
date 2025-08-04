/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package protocol.xyz.migoo.jdbc.processor;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.processor.AbstractProcessor;
import core.xyz.migoo.testelement.processor.Postprocessor;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;
import protocol.xyz.migoo.jdbc.RealJDBCRequest;
import protocol.xyz.migoo.jdbc.config.JDBCConfigureItem;

import static protocol.xyz.migoo.jdbc.Utils.toJSONBytes;

/**
 * @author xiaomi
 */
@Alias(value = {"jdbc_postprocessor", "jdbc_post_processor", "jdbc"})
@SuppressWarnings({"unchecked", "rawtypes"})
public class JDBCPostprocessor extends AbstractProcessor<JDBCPostprocessor, JDBCConfigureItem, DefaultSampleResult> implements Postprocessor, JDBCConstantsInterface {

    @JSONField(serialize = false)
    private DruidDataSource datasource;

    @JSONField(serialize = false)
    private byte[] bytes;

    public JDBCPostprocessor(Builder builder) {
        super(builder);
    }

    public JDBCPostprocessor() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

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
            throw new RuntimeException(e);
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

    /**
     * JDBC 后置处理器构建器
     */
    public static class Builder extends AbstractProcessor.PostprocessorBuilder<JDBCPostprocessor, Builder, JDBCConfigureItem,
            JDBCConfigureItem.Builder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public JDBCPostprocessor build() {
            return new JDBCPostprocessor(this);
        }

        @Override
        protected DefaultExtractorsBuilder getExtractorsBuilder() {
            return DefaultExtractorsBuilder.builder();
        }

        @Override
        protected JDBCConfigureItem.Builder getConfigureItemBuilder() {
            return JDBCConfigureItem.builder();
        }
    }
}
