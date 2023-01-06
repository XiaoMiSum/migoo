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

import itestx.xyz.migoo.element.Configs
import itestx.xyz.migoo.logic.ITestx

/**
 * @author xiaomi 
 * Created at 2022/8/27 11:47
 */
class DubboConfigs {

    private static final Builder builder = new Builder()

    static Builder builder() {
        return builder
    }

    static class Builder {

        DubboDefault dubboSampler() {
            return new DubboDefault()
        }

    }

    static class DubboDefault extends Configs implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16)

        private DubboDefault() {
            props.put("testclass", "DubboDefault")
        }

        DubboDefault registryCenter(protocol = "zookeeper", String address, String appname,
                                    username = null, password = null, version = null) {
            props.put("registry_center", DubboSamplers.s(protocol, address, appname, username, password, version))
            return this
        }

        DubboDefault referenceConfig(retries = 3, timeout = 5, async = false,
                                     loadBalance = "random", version = null) {
            props.put("reference_config", DubboSamplers.s(retries, timeout, async, loadBalance, version))
            return this
        }

        @Override
        Map<String, Object> get() {
            return props
        }
    }
}
