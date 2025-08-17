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

package io.github.xiaomisum.ryze.protocol.jdbc.sampler;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.builder.*;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.sampler.AbstractSampler;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.core.testelement.sampler.Sampler;
import io.github.xiaomisum.ryze.protocol.jdbc.JDBC;
import io.github.xiaomisum.ryze.protocol.jdbc.JDBCConstantsInterface;
import io.github.xiaomisum.ryze.protocol.jdbc.RealJDBCRequest;
import io.github.xiaomisum.ryze.protocol.jdbc.config.JDBCConfigureItem;

/**
 * @author xiaomi
 */
@KW(value = {"jdbc", "jdbc_sampler"})
public class JDBCSampler extends AbstractSampler<JDBCSampler, JDBCConfigureItem, DefaultSampleResult> implements Sampler<DefaultSampleResult>, JDBCConstantsInterface {

    @JSONField(serialize = false)
    private DruidDataSource datasource;

    private byte[] bytes;

    public JDBCSampler(Builder builder) {
        super(builder);
    }

    public JDBCSampler() {
        super();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(runtime.id, runtime.title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        bytes = JDBC.execute(datasource, runtime.getConfig().getSql(), result);
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        datasource = (DruidDataSource) context.getAllVariablesWrapper().get(runtime.config.getDatasource());
        result.setRequest(new RealJDBCRequest(datasource.getUrl(), datasource.getUsername(), datasource.getPassword(), runtime.config.getSql()));
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setResponse(SampleResult.DefaultReal.build(bytes));
    }

    /**
     * JDBC 取样器构建器
     */
    public static class Builder extends AbstractSampler.Builder<JDBCSampler, Builder, JDBCConfigureItem,
            JDBCConfigureItem.Builder, DefaultConfigureElementsBuilder, DefaultPreprocessorsBuilder, DefaultPostprocessorsBuilder,
            DefaultAssertionsBuilder, DefaultExtractorsBuilder, DefaultSampleResult> {
        @Override
        public JDBCSampler build() {
            return new JDBCSampler(this);
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
        protected JDBCConfigureItem.Builder getConfigureItemBuilder() {
            return JDBCConfigureItem.builder();
        }
    }
}
