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

package itestx.migoo.xyz.element

import itestx.xyz.migoo.element.Extractors
import itestx.xyz.migoo.element.Processors
import itestx.xyz.migoo.element.Samplers
import itestx.xyz.migoo.element.Validators
import itestx.xyz.migoo.logic.ITestx

/**
 * @author xiaomi
 * Created at 2022/8/27 10:30
 */
class DubboSamplers {


    protected static Map<String, Object> s(retries, timeout, async, loadBalance, version) {
        Map<String, Object> referenceConfig = new LinkedHashMap<>(16)
        referenceConfig.put("retries", retries)
        referenceConfig.put("timeout", timeout * 1000)
        referenceConfig.put("async", async)
        referenceConfig.put("load_balance", loadBalance)
        referenceConfig.put("version", version)
        return referenceConfig
    }

    protected static Map<String, Object> s(protocol, address, appname, username, password, version) {
        Map<String, Object> registryCenter = new LinkedHashMap<>(16)
        registryCenter.put("protocol", protocol)
        registryCenter.put("address", address)
        registryCenter.put("appname", appname)
        registryCenter.put("username", username)
        registryCenter.put("password", password)
        registryCenter.put("version", version)
        return registryCenter
    }


    private static final Builder builder = new Builder()

    static Builder builder() {
        return builder
    }

    static class Builder {

        DubboSampler dubboSampler(String title) {
            return new DubboSampler(title)
        }

    }

    static class DubboSampler extends Samplers implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16)

        private DubboSampler(String title) {
            props.put("title", title)
            props.put("testclass", "DubboSampler")
            props.put("config", new LinkedHashMap<>(16))
            props.put("variables", new LinkedHashMap<>(16))
            props.put("PreProcessors", new Vector<>())
            props.put("extractors", new Vector<>())
            props.put("validators", new Vector<>())
            props.put("PostProcessors", new Vector<>())
        }

        DubboSampler registryCenter(String protocol = "zookeeper", String address, String appname,
                                    String username = null, String password = null, String version = null) {
            Map<String, Object> config = (Map<String, Object>) props.get("config")
            config.put("registry_center", s(protocol, address, appname, username, password, version))
            return this
        }

        DubboSampler referenceConfig(int retries = 3, int timeout = 5, boolean async = false,
                                     String loadBalance = "random", String version = null) {
            Map<String, Object> config = (Map<String, Object>) props.get("config")
            config.put("reference_config", s(retries, timeout, async, loadBalance, version))
            return this
        }

        DubboSampler interfaceName(String interfaceName) {
            Map<String, Object> config = (Map<String, Object>) props.get("config")
            config.put("interface", interfaceName)
            return this
        }

        DubboSampler method(String method) {
            Map<String, Object> config = (Map<String, Object>) props.get("config")
            config.put("method", method)
            return this
        }

        DubboSampler parameterTypes(String... typeClass) {
            Map<String, Object> config = (Map<String, Object>) props.get("config")
            config.put("parameter_types", Arrays.asList(typeClass))
            return this
        }

        DubboSampler parameters(Map<String, Object>... parameter) {
            Map<String, Object> config = (Map<String, Object>) props.get("config")
            config.put("parameters", Arrays.asList(parameter))
            return this
        }

        DubboSampler attachmentArgs(Map<String, Object>... args) {
            Map<String, Object> config = (Map<String, Object>) props.get("config")
            config.put("attachment_args", Arrays.asList(args))
            return this
        }

        DubboSampler addVariable(String name, String value) {
            Map<String, Object> variables = (Map<String, Object>) props.get("variables")
            variables.put(name, value)
            return this
        }

        DubboSampler addExtractor(Extractors... extractor) {
            Vector<Extractors> vector = (Vector<Extractors>) props.get("extractors")
            vector.addAll(Arrays.asList(extractor))
            return this
        }

        DubboSampler addPreprocessor(Processors... processor) {
            Vector<Processors> vector = (Vector<Processors>) props.get("PreProcessors")
            vector.addAll(Arrays.asList(processor))
            return this
        }

        DubboSampler addPostprocessor(Processors... processor) {
            Vector<Processors> vector = (Vector<Processors>) props.get("PostProcessors")
            vector.addAll(Arrays.asList(processor))
            return this
        }

        DubboSampler addValidator(Validators... assertion) {
            Vector<Validators> vector = (Vector<Validators>) props.get("validators")
            vector.addAll(Arrays.asList(assertion))
            return this
        }

        @Override
        Map<String, Object> get() {
            return props
        }
    }
}
