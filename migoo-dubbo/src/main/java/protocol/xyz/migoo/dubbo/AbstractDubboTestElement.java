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
import java.util.Map;

/**
 * @author mi.xiao
 * @date 2021/4/10 20:35
 */
public abstract class AbstractDubboTestElement extends AbstractTestElement implements DubboConstantsInterface {

    protected final String name = this.getClass().getSimpleName().toLowerCase();
    protected ReferenceConfig<GenericService> reference;

    public void testStarted() {
        super.convertVariable();
        DubboDefaults other = (DubboDefaults) getVariables().get(DUBBO_DEFAULT);
        reference = other == null ? buildReferenceConfig() : (ReferenceConfig<GenericService>) other.get(DUBBO_REFERENCE);
        if (other != null) {
            setProperty(REGISTRY_CENTER, other.get(REGISTRY_CENTER));
            setProperty(REFERENCE_CONFIG, other.get(REFERENCE_CONFIG));
        }
        reference.setInterface(getPropertyAsString(INTERFACE));
        getVariables().put("migoo_protocol_dubbo_request_agrs", get(ARGS_PARAMETERS));
    }

    protected SampleResult execute(DubboSampleResult result) throws Exception {
        result.setTestClass(this.getClass());
        result.sampleStart();
        try {
            if (get(ATTACHMENT_ARGS) != null && !getPropertyAsJSONObject(ATTACHMENT_ARGS).isEmpty()) {
                Map<String, String> attachments = new HashMap<>(16);
                getPropertyAsJSONObject(ATTACHMENT_ARGS).forEach((key, value) -> attachments.put(key, value.toString()));
                RpcContext.getContext().setAttachments(attachments);
                getVariables().put("migoo_protocol_dubbo_attachment_args", get(ATTACHMENT_ARGS));
            }
            String[] parameterTypes = new String[getPropertyAsJSONArray(ARGS_PARAMETER_TYPES).size()];
            for (int i = 0; i < getPropertyAsJSONArray(ARGS_PARAMETER_TYPES).size(); i++) {
                parameterTypes[i] = getPropertyAsJSONArray(ARGS_PARAMETER_TYPES).getString(i);
            }
            result.setRequestData(getProperty());
            Object[] parameters = getPropertyAsJSONArray(ARGS_PARAMETERS).toArray();
            Object response = reference.get().$invoke(getPropertyAsString(METHOD), parameterTypes, parameters);
            result.setResponseData(JSON.toJSONBytes(response));
        } finally {
            result.sampleEnd();
        }
        return result;
    }

    protected ReferenceConfig<GenericService> buildReferenceConfig() {
        JSONObject registerCenter = getPropertyAsJSONObject(REGISTRY_CENTER);
        JSONObject referenceConfig = getPropertyAsJSONObject(REFERENCE_CONFIG);

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setGeneric("true");
        reference.setApplication(new ApplicationConfig(StringUtils.isEmpty(registerCenter.getString(APP_NAME)) ?
                "migoo-dubbo-consumer" : registerCenter.getString(APP_NAME)));
        reference.setVersion(referenceConfig.getString(VERSION));
        reference.setGroup(referenceConfig.getString(GROUP));
        reference.setRetries(Math.max(referenceConfig.getIntValue(RETRIES), 2));
        reference.setTimeout(Math.max(referenceConfig.getIntValue(TIMEOUT), 5000));
        reference.setAsync(referenceConfig.getBooleanValue(ASYNC));
        reference.setLoadbalance(referenceConfig.get(LOAD_BALANCE) == null ? "random" : referenceConfig.getString(LOAD_BALANCE));
        RegistryConfig registry = new RegistryConfig();
        Protocol protocol = Enum.valueOf(Protocol.class, StringUtils.isEmpty(registerCenter.getString(PROTOCOL)) ? "ZOOKEEPER" :
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
