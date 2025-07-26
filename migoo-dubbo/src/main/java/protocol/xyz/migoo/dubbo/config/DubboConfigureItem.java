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

package protocol.xyz.migoo.dubbo.config;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.dubbo.DubboConstantsInterface;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * @author xiaomi
 * Created at 2025/7/26 21:03
 */
@SuppressWarnings("unchecked")
public class DubboConfigureItem implements ConfigureItem<DubboConfigureItem>, DubboConstantsInterface {

    @JSONField(name = REF)
    private String ref;

    @JSONField(name = REGISTRY)
    private Registry registry;

    @JSONField(name = REFERENCE, ordinal = 1)
    private Reference reference;

    @JSONField(name = INTERFACE, ordinal = 2)
    private String interfaceName;

    @JSONField(name = METHOD, ordinal = 3)
    private String method;

    @JSONField(name = ARGS_PARAMETER_TYPES, ordinal = 4)
    private List<String> parameterTypes;

    @JSONField(name = ARGS_PARAMETERS, ordinal = 5)
    private List<Object> parameters;

    @JSONField(name = ATTACHMENT_ARGS, ordinal = 6)
    private Map<String, String> attachmentArgs;

    public DubboConfigureItem() {
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
    public DubboConfigureItem calc(ContextWrapper context) {
        registry = (Registry) context.eval(registry);
        reference = (Reference) context.eval(reference);
        interfaceName = (String) context.eval(interfaceName);
        method = (String) context.eval(method);
        parameterTypes = (List<String>) context.eval(parameterTypes);
        parameters = (List<Object>) context.eval(parameters);
        attachmentArgs = (Map<String, String>) context.eval(attachmentArgs);
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


    public enum Protocol {
        /**
         * Dubbo 注册中心协议
         */
        ZOOKEEPER("zookeeper://"),
        NACOS("nacos://");

        private final String protocol;

        Protocol(String protocol) {
            this.protocol = protocol;
        }

        public String getProtocol() {
            return this.protocol;
        }

    }

    public static class Registry implements ConfigureItem<Registry> {

        @JSONField(name = PROTOCOL)
        private String protocol;

        @JSONField(name = ADDRESS, ordinal = 1)
        private String address;

        @JSONField(name = USERNAME, ordinal = 2)
        private String username;

        @JSONField(name = PASSWORD, ordinal = 3)
        private String password;

        @JSONField(name = GROUP, ordinal = 4)
        private String group;

        @JSONField(name = VERSION, ordinal = 5)
        private String version;

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
        public Registry calc(ContextWrapper context) {
            protocol = (String) context.eval(protocol);
            address = (String) context.eval(address);
            username = (String) context.eval(username);
            password = (String) context.eval(password);
            group = (String) context.eval(group);
            version = (String) context.eval(version);
            return this;
        }

        public String getProtocol() {
            return StringUtils.isBlank(protocol) ? "ZOOKEEPER" : protocol.toUpperCase(Locale.ROOT);
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
    }

    public static class Reference implements ConfigureItem<Reference> {

        @JSONField(name = VERSION)
        private String version;
        @JSONField(name = GROUP, ordinal = 1)
        private String group;
        @JSONField(name = RETRIES, ordinal = 2)
        private Integer retries;
        @JSONField(name = TIMEOUT, ordinal = 3)
        private Integer timeout;
        @JSONField(name = ASYNC, ordinal = 4)
        private Boolean async;
        @JSONField(name = LOAD_BALANCE, ordinal = 5)
        private String loadBalance;

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
        public Reference calc(ContextWrapper context) {
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
    }
}
