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

package protocol.xyz.migoo.dubbo;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericService;
import protocol.xyz.migoo.dubbo.config.DubboDefaults;
import protocol.xyz.migoo.dubbo.sampler.DubboSampleResult;
import protocol.xyz.migoo.dubbo.util.DubboConstantsInterface;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * @author mi.xiao
 * @date 2021/4/10 20:35
 */
public abstract class AbstractDubboTestElement extends AbstractTestElement implements DubboConstantsInterface {

    protected final String name = this.getClass().getSimpleName().toLowerCase();
    protected ReferenceConfig<GenericService> reference;

    public void testStarted() {
        var other = (DubboDefaults) getVariables().get(DUBBO_DEFAULT);
        if (Objects.nonNull(other)) {
            setProperty(REGISTRY, other.get(REGISTRY));
            setProperty(REFERENCE, other.get(REFERENCE));
        }
        reference = buildReferenceConfig();
        getVariables().put("migoo_protocol_dubbo_request_agrs", get(ARGS_PARAMETERS));
    }

    protected SampleResult execute(DubboSampleResult result) {
        result.setTestClass(this.getClass());
        result.sampleStart();
        try {
            if (get(ATTACHMENT_ARGS) != null && !getPropertyAsJSONObject(ATTACHMENT_ARGS).isEmpty()) {
                var attachments = new HashMap<String, String>(16);
                getPropertyAsJSONObject(ATTACHMENT_ARGS).forEach((key, value) -> attachments.put(key, value.toString()));
                RpcContext.getClientAttachment().setAttachments(attachments);
                getVariables().put("migoo_protocol_dubbo_attachment_args", get(ATTACHMENT_ARGS));
            }
            var parameterTypes = new String[getPropertyAsJSONArray(ARGS_PARAMETER_TYPES).size()];
            for (int i = 0; i < getPropertyAsJSONArray(ARGS_PARAMETER_TYPES).size(); i++) {
                parameterTypes[i] = getPropertyAsJSONArray(ARGS_PARAMETER_TYPES).getString(i);
            }
            result.setRequestData(getProperty());
            var parameters = getPropertyAsJSONArray(ARGS_PARAMETERS).toArray();
            var response = reference.get().$invoke(getPropertyAsString(METHOD), parameterTypes, parameters);
            result.setResponseData(JSON.toJSONBytes(response));
        } catch (Exception e) {
            result.setThrowable(e);
        } finally {
            result.sampleEnd();
        }
        return result;
    }

    protected ReferenceConfig<GenericService> buildReferenceConfig() {
        var registerCenter = Optional.ofNullable((JSONObject) removeProperty(REGISTRY_CENTER)).orElse(getPropertyAsJSONObject(REGISTRY));
        var referenceConfig = Optional.ofNullable((JSONObject) removeProperty(REFERENCE_CONFIG)).orElse(getPropertyAsJSONObject(REFERENCE));
        setProperty(REGISTRY, registerCenter);
        setProperty(REFERENCE, referenceConfig);

        var reference = new ReferenceConfig<GenericService>();
        reference.setGeneric("true");
        reference.getApplicationModel().getApplicationConfigManager().setApplication(new ApplicationConfig("migoo-dubbo-consumer"));
        reference.setVersion(referenceConfig.getString(VERSION));
        reference.setGroup(referenceConfig.getString(GROUP));
        reference.setRetries(Math.max(referenceConfig.getIntValue(RETRIES), 2));
        reference.setTimeout(Math.max(referenceConfig.getIntValue(TIMEOUT), 5000));
        reference.setAsync(referenceConfig.getBooleanValue(ASYNC));
        reference.setLoadbalance(referenceConfig.get(LOAD_BALANCE) == null ? "random" : referenceConfig.getString(LOAD_BALANCE));
        reference.setInterface(getPropertyAsString(INTERFACE));
        RegistryConfig registry = new RegistryConfig();
        Protocol protocol = Protocol.valueOf(StringUtils.isEmpty(registerCenter.getString(PROTOCOL)) ? "ZOOKEEPER" :
                registerCenter.getString(PROTOCOL).toUpperCase(Locale.ROOT));
        registry.setAddress(protocol.getProtocol() + registerCenter.getString(ADDRESS));
        registry.setGroup(registerCenter.getString(GROUP));
        registry.setUsername(registerCenter.getString(USERNAME));
        registry.setPassword(registerCenter.getString(PASSWORD));
        registry.setVersion(registerCenter.getString(VERSION));
        registry.setTimeout(Math.max(registerCenter.getIntValue(TIMEOUT), 5000));
        reference.setRegistry(registry);
        return reference;
    }

    private enum Protocol {
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
}
