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

package io.github.xiaomisum.ryze.protocol.jdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.TestSuiteResult;
import io.github.xiaomisum.ryze.core.testelement.configure.AbstractConfigureElement;
import io.github.xiaomisum.ryze.protocol.jdbc.JDBCConstantsInterface;
import io.github.xiaomisum.ryze.support.Closeable;
import io.github.xiaomisum.ryze.support.ValidateResult;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaomi
 */
@KW({"jdbc", "jdbc_datasource", "jdbc_data_source"})
public class JDBCDatasource extends AbstractConfigureElement<JDBCDatasource, JDBCConfigureItem, TestSuiteResult>
        implements Closeable, JDBCConstantsInterface {
    @JSONField(serialize = false)
    private final DruidDataSource dataSource = new DruidDataSource();

    public JDBCDatasource() {
    }

    public JDBCDatasource(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public ValidateResult validate() {
        var result = super.validate();
        if (StringUtils.isBlank(refName)) {
            result.append("\n数据源引用名称 %s 字段值缺失或为空，当前值：%s", REF_NAME, toString());
        }
        if (StringUtils.isBlank(config.url)) {
            result.append("\n数据源连接 %s 字段值缺失或为空，当前值：%s", URL, toString());
        }
        if (StringUtils.isBlank(config.username)) {
            result.append("\n数据源用户名 %s 字段值缺失或为空，当前值：%s", USERNAME, toString());
        }
        if (StringUtils.isBlank(config.password)) {
            result.append("\n数据源密码 %s 字段值缺失或为空，当前值：%s", PASSWORD, toString());
        }
        return result;
    }

    @Override
    protected void doProcess(ContextWrapper context) {
        try {
            // 兼容 没有使用 SPI 的 JDBC驱动
            if (StringUtils.isNotBlank(runtime.getConfig().driver)) {
                Class.forName(runtime.getConfig().driver);
            }
        } catch (Exception ignored) {
        }
        dataSource.setUrl(runtime.getConfig().url);
        dataSource.setUsername(runtime.getConfig().username);
        dataSource.setPassword(runtime.getConfig().password);
        // 使用getter 获取数值，因为在这里面做了数值判断
        dataSource.setMaxActive(runtime.getConfig().getMaxActive());
        dataSource.setMaxWait(runtime.getConfig().getMaxWait());
        context.getSessionRunner().getContext().getLocalVariablesWrapper().put(refName, dataSource);
    }

    @Override
    protected TestSuiteResult getTestResult() {
        return new TestSuiteResult("JDBC数据源配置：" + refName);
    }

    @Override
    public void close() {
        dataSource.close();
    }

    /**
     * JDBC数据源 测试元件 构建类
     */
    public static class Builder extends AbstractConfigureElement.Builder<JDBCDatasource, Builder, JDBCConfigureItem, JDBCConfigureItem.Builder, TestSuiteResult> {

        @Override
        public JDBCDatasource build() {
            return new JDBCDatasource(this);
        }

        @Override
        protected JDBCConfigureItem.Builder getConfigureItemBuilder() {
            return JDBCConfigureItem.builder();
        }
    }
}
