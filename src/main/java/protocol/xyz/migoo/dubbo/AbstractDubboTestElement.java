/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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

package protocol.xyz.migoo.dubbo;

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.service.GenericService;
import protocol.xyz.migoo.dubbo.config.DubboDefaults;
import protocol.xyz.migoo.dubbo.sampler.DubboSampleResult;
import protocol.xyz.migoo.dubbo.util.DubboConstantsInterface;

import java.util.*;

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
            setProperty(CONFIG_CENTER, other.get(CONFIG_CENTER));
            setProperty(REGISTRY_CENTER, other.get(REGISTRY_CENTER));
            setProperty(CONSUMER_PROVIDER, other.get(CONSUMER_PROVIDER));
        }
        JSONObject providerInterface = getPropertyAsJSONObject(PROVIDER_INTERFACE);
        reference.setInterface(providerInterface.getString(INTERFACE));
        getVariables().put("migoo.protocol.dubbo.request.args", getPropertyAsJSONObject(PROVIDER_INTERFACE).get(ARGS_PARAMETERS));
   }

    protected SampleResult execute(DubboSampleResult result) throws Exception {
        result.setTestClass(this.getClass());
        result.sampleStart();
        try {
            JSONObject providerInterface = getPropertyAsJSONObject(PROVIDER_INTERFACE);
            GenericService service = reference.get();
            if (providerInterface.getJSONObject(ATTACHMENT_ARGS) != null && !providerInterface.getJSONObject(ATTACHMENT_ARGS).isEmpty()) {
                Map<String, String> attachments = new HashMap<>();
                providerInterface.getJSONObject(ATTACHMENT_ARGS).forEach((key, value) -> attachments.put(key, value.toString()));
                RpcContext.getContext().setAttachments(attachments);
                getVariables().put("migoo.protocol.dubbo.attachment.args", getPropertyAsJSONObject(PROVIDER_INTERFACE).get(ATTACHMENT_ARGS));

            }
            String[] parameterTypes = new String[providerInterface.getJSONArray(ARGS_PARAMETER_TYPES).size()];
            for (int i = 0; i < providerInterface.getJSONArray(ARGS_PARAMETER_TYPES).size(); i++) {
                parameterTypes[i] = providerInterface.getJSONArray(ARGS_PARAMETER_TYPES).getString(i);
            }
            result.setRequestData(getProperty());
            Object[] parameters = providerInterface.getJSONArray(ARGS_PARAMETERS).toArray();
            Object response = service.$invoke(providerInterface.getString(METHOD), parameterTypes, parameters);
            result.setResponseData(JSONObject.toJSONBytes(response));
        } finally {
            result.sampleEnd();
        }
        return result;
    }

    protected ReferenceConfig<GenericService> buildReferenceConfig() {
        JSONObject configCenter = getPropertyAsJSONObject(CONFIG_CENTER);
        JSONObject registerCenter = getPropertyAsJSONObject(REGISTRY_CENTER);
        JSONObject consumerProvider = getPropertyAsJSONObject(CONSUMER_PROVIDER);

        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setGeneric("true");
        reference.setApplication(new ApplicationConfig(registerCenter.getString(APP_NAME)));
        reference.setVersion(consumerProvider.getString(VERSION));
        reference.setGroup(consumerProvider.getString(GROUP));
        reference.setRetries(consumerProvider.get(RETRIES) != null ? consumerProvider.getIntValue(RETRIES) : 2);
        reference.setTimeout(consumerProvider.get(TIMEOUT) != null ? consumerProvider.getIntValue(TIMEOUT) : 5000);
        reference.setAsync(consumerProvider.getBooleanValue(ASYNC));
        reference.setLoadbalance(consumerProvider.get(LOAD_BALANCE) == null ? "random" : consumerProvider.getString(LOAD_BALANCE));
        RegistryConfig registry = new RegistryConfig();
        Protocol protocol = Enum.valueOf(Protocol.class, registerCenter.getString(PROTOCOL).toUpperCase(Locale.ROOT));
        registry.setAddress(protocol.getProtocol() + registerCenter.getString(ADDRESS));
        registry.setGroup(consumerProvider.getString(GROUP));
        registry.setUsername(registerCenter.getString(USERNAME));
        registry.setPassword(registerCenter.getString(PASSWORD));
        reference.setRegistry(registry);
        return reference;
    }

    private enum Protocol {
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
