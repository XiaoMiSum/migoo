package io.github.xiaomisum.ryze.protocol.mongo.config;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.KW;
import io.github.xiaomisum.ryze.core.testelement.TestSuiteResult;
import io.github.xiaomisum.ryze.core.testelement.configure.AbstractConfigureElement;
import io.github.xiaomisum.ryze.protocol.mongo.MongoConstantsInterface;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@KW({"mongo_defaults", "mongo"})
public class MongoDefaults extends AbstractConfigureElement<MongoDefaults, MongoConfigItem, TestSuiteResult> implements MongoConstantsInterface {

    public MongoDefaults(Builder builder) {
        super(builder);
    }

    public MongoDefaults() {
    }


    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected void doProcess(ContextWrapper context) {
        refName = StringUtils.isBlank(refName) ? DEF_REF_NAME_KEY : refName;
        var localConfig = runtime.getConfig();
        var otherRefName = StringUtils.isBlank(localConfig.ref) ? DEF_REF_NAME_KEY : localConfig.ref;
        var config = (MongoConfigItem) context.getSessionRunner().getContext().getLocalVariablesWrapper().get(otherRefName);
        if (Objects.nonNull(config)) {
            runtime.setConfig(localConfig = localConfig.merge(config));
        }
        context.getSessionRunner().getContext().getLocalVariablesWrapper().put(refName, localConfig);
    }

    @Override
    protected TestSuiteResult getTestResult() {
        return new TestSuiteResult("MONGO 默认配置");
    }

    /**
     * HTTP默认配置 测试元件 构建类
     */
    public static class Builder extends AbstractConfigureElement.Builder<MongoDefaults, Builder, MongoConfigItem, MongoConfigItem.Builder, TestSuiteResult> {

        @Override
        public MongoDefaults build() {
            return new MongoDefaults(this);
        }

        @Override
        protected MongoConfigItem.Builder getConfigureItemBuilder() {
            return MongoConfigItem.builder();
        }
    }
}
