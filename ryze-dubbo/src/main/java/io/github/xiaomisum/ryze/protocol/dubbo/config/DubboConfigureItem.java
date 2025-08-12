/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package io.github.xiaomisum.ryze.protocol.dubbo.config;

import com.alibaba.fastjson2.annotation.JSONField;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;
import io.github.xiaomisum.ryze.protocol.dubbo.DubboConstantsInterface;
import io.github.xiaomisum.ryze.support.Collections;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static io.github.xiaomisum.ryze.support.groovy.Groovy.call;

/**
 * @author xiaomi
 * Created at 2025/7/26 21:03
 */
@SuppressWarnings("unchecked")
public class DubboConfigureItem implements ConfigureItem<DubboConfigureItem>, DubboConstantsInterface {

    @JSONField(name = REGISTRY)
    protected Registry registry;
    @JSONField(name = REFERENCE, ordinal = 1)
    protected Reference reference;
    @JSONField(name = INTERFACE, ordinal = 2)
    protected String interfaceName;
    @JSONField(name = METHOD, ordinal = 3)
    protected String method;
    @JSONField(name = ARGS_PARAMETER_TYPES, ordinal = 4)
    protected List<String> parameterTypes;
    @JSONField(name = ARGS_PARAMETERS, ordinal = 5)
    protected List<Object> parameters;
    @JSONField(name = ATTACHMENT_ARGS, ordinal = 6)
    protected Map<String, String> attachmentArgs;
    @JSONField(name = REF)
    private String ref;

    public DubboConfigureItem() {
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public DubboConfigureItem merge(DubboConfigureItem other) {
        if (other == null) {
            return copy();
        }
        var localOther = other.copy();
        var self = copy();
        self.registry = Objects.isNull(self.registry) ? localOther.registry : self.registry;
        self.reference = Objects.isNull(self.reference) ? localOther.reference : self.reference;
        self.interfaceName = StringUtils.isBlank(self.interfaceName) ? localOther.interfaceName : self.interfaceName;
        self.method = StringUtils.isBlank(self.method) ? localOther.method : self.method;
        self.parameterTypes = Objects.isNull(self.parameterTypes) ? localOther.parameterTypes : self.parameterTypes;
        self.parameters = Objects.isNull(self.parameters) ? localOther.parameters : self.parameters;
        self.attachmentArgs = Objects.isNull(self.attachmentArgs) ? localOther.attachmentArgs : self.attachmentArgs;
        return self;
    }

    @Override
    public DubboConfigureItem evaluate(ContextWrapper context) {
        registry = (Registry) context.evaluate(registry);
        reference = (Reference) context.evaluate(reference);
        interfaceName = (String) context.evaluate(interfaceName);
        method = (String) context.evaluate(method);
        parameterTypes = (List<String>) context.evaluate(parameterTypes);
        parameters = (List<Object>) context.evaluate(parameters);
        attachmentArgs = (Map<String, String>) context.evaluate(attachmentArgs);
        return this;
    }

    public Registry getRegistry() {
        return registry;
    }

    public void setRegistry(Registry registry) {
        this.registry = registry;
    }

    public Reference getReference() {
        return reference;
    }

    public void setReference(Reference reference) {
        this.reference = reference;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<String> getParameterTypes() {
        if (Objects.isNull(parameterTypes) && Objects.nonNull(parameters)) {
            parameterTypes = Collections.newArrayList();
            for (var parameter : parameters) {
                parameterTypes.add(parameter.getClass().getName());
            }
        }
        return parameterTypes;
    }

    public void setParameterTypes(List<String> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getAttachmentArgs() {
        return attachmentArgs;
    }

    public void setAttachmentArgs(Map<String, String> attachmentArgs) {
        this.attachmentArgs = attachmentArgs;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }


    public static class Registry implements ConfigureItem<Registry> {

        /**
         * 过期配置，合并到 {@link #address}
         */
        @JSONField(name = PROTOCOL)
        protected String protocol;

        @JSONField(name = ADDRESS, ordinal = 1)
        protected String address;

        @JSONField(name = USERNAME, ordinal = 2)
        protected String username;

        @JSONField(name = PASSWORD, ordinal = 3)
        protected String password;

        @JSONField(name = GROUP, ordinal = 4)
        protected String group;

        @JSONField(name = VERSION, ordinal = 5)
        protected String version;

        public Registry() {
        }

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public Registry merge(Registry other) {
            if (other == null) {
                return copy();
            }
            var localOther = other.copy();
            var self = copy();
            self.protocol = StringUtils.isBlank(self.protocol) ? localOther.protocol : self.protocol;
            self.address = StringUtils.isBlank(self.address) ? localOther.address : self.address;
            self.username = StringUtils.isBlank(self.username) ? localOther.username : self.username;
            self.password = StringUtils.isBlank(self.password) ? localOther.password : self.password;
            self.group = StringUtils.isBlank(self.group) ? localOther.group : self.group;
            self.version = StringUtils.isBlank(self.version) ? localOther.version : self.version;
            return self;
        }

        @Override
        public Registry evaluate(ContextWrapper context) {
            protocol = (String) context.evaluate(protocol);
            address = (String) context.evaluate(address);
            username = (String) context.evaluate(username);
            password = (String) context.evaluate(password);
            group = (String) context.evaluate(group);
            version = (String) context.evaluate(version);
            return this;
        }

        public String getProtocol() {
            return StringUtils.isNotBlank(protocol) ? protocol.toLowerCase(Locale.ROOT) : "";
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        /**
         * Dubbo 注册中心 配置属性 构建器
         */
        public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, Registry> {

            private Registry registry = new Registry();

            /**
             * 过期配置，合并到 {@link #address(String)}
             *
             * @param protocol
             * @return
             */
            @Deprecated(since = "6.0.0")
            public Builder protocol(String protocol) {
                registry.protocol = protocol;
                return self;
            }

            public Builder address(String address) {
                registry.address = address;
                return self;
            }

            public Builder username(String username) {
                registry.username = username;
                return self;
            }

            public Builder password(String password) {
                registry.password = password;
                return self;
            }

            public Builder group(String group) {
                registry.group = group;
                return self;
            }

            public Builder version(String version) {
                registry.version = version;
                return self;
            }

            public Builder config(Registry registry) {
                this.registry = this.registry.merge(registry);
                return self;
            }

            @Override
            public Registry build() {
                return registry;
            }
        }
    }

    public static class Reference implements ConfigureItem<Reference> {

        @JSONField(name = VERSION)
        protected String version;
        @JSONField(name = GROUP, ordinal = 1)
        protected String group;
        @JSONField(name = RETRIES, ordinal = 2)
        protected Integer retries;
        @JSONField(name = TIMEOUT, ordinal = 3)
        protected Integer timeout;
        @JSONField(name = ASYNC, ordinal = 4)
        protected Boolean async;
        @JSONField(name = LOAD_BALANCE, ordinal = 5)
        protected String loadBalance;

        public Reference() {
        }

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public Reference merge(Reference other) {
            if (other == null) {
                return copy();
            }
            var localOther = other.copy();
            var self = copy();
            self.version = StringUtils.isBlank(self.version) ? localOther.version : self.version;
            self.retries = Objects.isNull(retries) ? localOther.retries : self.retries;
            self.timeout = Objects.isNull(timeout) ? localOther.timeout : self.timeout;
            self.async = Objects.isNull(async) ? localOther.async : self.async;
            self.loadBalance = StringUtils.isBlank(self.loadBalance) ? localOther.loadBalance : self.loadBalance;
            return self;
        }

        @Override
        public Reference evaluate(ContextWrapper context) {
            return this;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public Integer getRetries() {
            return Objects.isNull(retries) ? 1 : retries;
        }

        public void setRetries(Integer retries) {
            this.retries = retries;
        }

        public Integer getTimeout() {
            return Objects.isNull(timeout) ? 5000 : timeout;
        }

        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }

        public Boolean getAsync() {
            return Objects.nonNull(async) && async;
        }

        public void setAsync(Boolean async) {
            this.async = async;
        }

        public String getLoadBalance() {
            return loadBalance;
        }

        public void setLoadBalance(String loadBalance) {
            this.loadBalance = loadBalance;
        }

        /**
         * Dubbo 注册中心 配置属性 构建器
         */
        public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, Reference> {

            private Reference reference = new Reference();

            public Builder version(String version) {
                reference.version = version;
                return self;
            }

            public Builder group(String group) {
                reference.group = group;
                return self;
            }

            public Builder retries(int retries) {
                reference.retries = retries;
                return self;
            }

            public Builder timeout(int timeout) {
                reference.timeout = timeout;
                return self;
            }

            public Builder async(boolean async) {
                reference.async = async;
                return self;
            }

            public Builder loadBalance(String loadBalance) {
                reference.loadBalance = loadBalance;
                return self;
            }

            public Builder config(Reference reference) {
                this.reference = this.reference.merge(reference);
                return self;
            }

            @Override
            public Reference build() {
                return reference;
            }
        }
    }

    /**
     * Dubbo 配置属性 构建器
     */
    public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, DubboConfigureItem> {

        private DubboConfigureItem configure = new DubboConfigureItem();

        public Builder() {
        }

        public Builder registry(Consumer<Registry.Builder> consumer) {
            var builder = Registry.builder();
            consumer.accept(builder);
            configure.registry = builder.build();
            return self;
        }

        public Builder registry(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = Registry.Builder.class) Closure<?> closure) {
            var builder = Registry.builder();
            call(closure, builder);
            configure.registry = builder.build();
            return self;
        }

        public Builder registry(Registry.Builder registry) {
            configure.registry = registry.build();
            return self;
        }

        public Builder registry(Registry registry) {
            configure.registry = registry;
            return self;
        }

        public Builder reference(Consumer<Reference.Builder> consumer) {
            var builder = Reference.builder();
            consumer.accept(builder);
            configure.reference = builder.build();
            return self;
        }

        public Builder reference(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = Reference.Builder.class) Closure<?> closure) {
            var builder = Reference.builder();
            call(closure, builder);
            configure.reference = builder.build();
            return self;
        }

        public Builder reference(Reference.Builder reference) {
            configure.reference = reference.build();
            return self;
        }

        public Builder reference(Reference reference) {
            configure.reference = reference;
            return self;
        }

        public Builder interfaceName(String interfaceName) {
            configure.interfaceName = interfaceName;
            return self;
        }

        public Builder method(String method) {
            configure.method = method;
            return self;
        }

        public Builder parameters(Consumer<List<Object>> consumer) {
            List<Object> parameters = Collections.newArrayList();
            consumer.accept(parameters);
            return parameters(parameters);
        }

        public Builder parameters(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = List.class) Closure<?> closure) {
            List<Object> parameters = Collections.newArrayList();
            call(closure, parameters);
            return parameters(parameters);
        }

        public Builder parameters(List<Object> parameters) {
            configure.parameters = Collections.addAllIfNonNull(configure.parameters, parameters);
            if (Objects.nonNull(parameters) && !parameters.isEmpty()) {
                List<String> parameterTypes = Collections.newArrayList();
                for (Object parameter : parameters) {
                    parameterTypes.add(parameter.getClass().getName());
                }
                configure.parameterTypes = Collections.addAllIfNonNull(configure.parameterTypes, Collections.newArrayList(parameterTypes));
            }
            return self;
        }

        public Builder parameters(Object parameter) {
            return parameters(Collections.newArrayList(parameter));
        }

        public Builder attachmentArgs(Consumer<Map<String, String>> consumer) {
            Map<String, String> attachmentArgs = Collections.newHashMap();
            consumer.accept(attachmentArgs);
            configure.attachmentArgs = Collections.putAllIfNonNull(configure.attachmentArgs, attachmentArgs);
            return self;
        }

        public Builder attachmentArgs(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = List.class) Closure<?> closure) {
            Map<String, String> attachmentArgs = Collections.newHashMap();
            call(closure, attachmentArgs);
            configure.attachmentArgs = Collections.putAllIfNonNull(configure.attachmentArgs, attachmentArgs);
            return self;
        }

        public Builder attachmentArgs(Map<String, String> attachmentArgs) {
            configure.attachmentArgs = Collections.putAllIfNonNull(configure.attachmentArgs, attachmentArgs);
            return self;
        }

        public Builder attachmentArgs(String name, String value) {
            configure.attachmentArgs = Collections.putAllIfNonNull(configure.attachmentArgs, (Map<String, String>) Collections.of(name, value));
            return self;
        }

        public Builder ref(String ref) {
            configure.ref = ref;
            return self;
        }

        public Builder config(Consumer<DubboConfigureItem.Builder> consumer) {
            var builder = DubboConfigureItem.builder();
            consumer.accept(builder);
            configure = configure.merge(builder.build());
            return self;
        }

        public Builder config(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = DubboConfigureItem.Builder.class) Closure<?> closure) {
            var builder = DubboConfigureItem.builder();
            call(closure, builder);
            configure = configure.merge(builder.build());
            return self;
        }

        public Builder config(DubboConfigureItem.Builder builder) {
            configure = configure.merge(builder.build());
            return self;
        }

        public Builder config(DubboConfigureItem config) {
            configure = configure.merge(config);
            return self;
        }

        @Override
        public DubboConfigureItem build() {
            return configure;
        }
    }
}
