package protocol.xyz.migoo.jdbc.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.configureelement.AbstractConfigureElement;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.Closeable;
import core.xyz.migoo.testelement.TestSuiteResult;
import core.xyz.migoo.testelement.ValidateResult;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;

@Alias({"jdbc", "jdbc_datasource", "jdbc_data_source"})
public class JDBCDatasource extends AbstractConfigureElement<JDBCConfigureItem, JDBCDatasource, TestSuiteResult>
        implements Closeable, JDBCConstantsInterface {

    @JSONField(serialize = false)
    private final DruidDataSource dataSource = new DruidDataSource();

    @Override
    public ValidateResult validate() {
        var result = super.validate();
        if (StringUtils.isBlank(runtime.getRefName())) {
            result.append("\n数据源引用名称 %s 字段值缺失或为空，当前值：%s", REF_NAME, toString());
        }
        if (StringUtils.isBlank(runtime.getConfig().getUrl())) {
            result.append("\n数据源连接 %s 字段值缺失或为空，当前值：%s", URL, toString());
        }
        if (StringUtils.isBlank(runtime.getConfig().getUsername())) {
            result.append("\n数据源用户名 %s 字段值缺失或为空，当前值：%s", USERNAME, toString());
        }
        if (StringUtils.isBlank(runtime.getConfig().getPassword())) {
            result.append("\n数据源密码 %s 字段值缺失或为空，当前值：%s", PASSWORD, toString());
        }
        return result;
    }

    @Override
    public TestSuiteResult process(ContextWrapper context) {
        var result = getTestResult();
        try {
            // 兼容 没有使用 SPI 的 JDBC驱动
            if (StringUtils.isNotBlank(runtime.getConfig().getDriver())) {
                Class.forName(runtime.getConfig().getDriver());
            }
        } catch (Exception ignored) {
        }
        dataSource.setUrl(runtime.getConfig().getUrl());
        dataSource.setUsername(runtime.getConfig().getUsername());
        dataSource.setPassword(runtime.getConfig().getPassword());
        dataSource.setMaxActive(runtime.getConfig().getMaxActive());
        dataSource.setMaxWait(runtime.getConfig().getMaxWait());
        context.getSessionRunner().getContextWrapper().getLocalVariablesWrapper().put(refName, datasource);
        return result;
    }

    @Override
    protected TestSuiteResult getTestResult() {
        return new TestSuiteResult("JDBC数据源配置：" + refName);
    }

    @Override
    public void close() {
        dataSource.close();
    }
}
