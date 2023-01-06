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

package itestx.xyz.migoo.element

import itestx.xyz.migoo.element.Configs
import itestx.xyz.migoo.logic.ITestx

/**
 * @author xiaomi 
 * Created at 2022/8/27 13:11
 */
class ActiveMqConfigs {


    private static final Builder builder = new Builder()

    static Builder builder() {
        return builder
    }

    static class Builder {

        ActiveMqDefault activeMqDefault() {
            return new ActiveMqDefault()
        }

    }

    static class ActiveMqDefault extends Configs implements ITestx {

        private final Map<String, Object> props = new LinkedHashMap<>(16)

        private ActiveMqDefault() {
            props.put("testclass", "ActiveMqDefaults")
        }

        ActiveMqDefault username(String username) {
            props.put("username", username)
            return this
        }

        ActiveMqDefault password(String password) {
            props.put("password", password)
            return this
        }

        ActiveMqDefault brokerUrl(String brokerUrl) {
            props.put("broker.url", brokerUrl)
            return this
        }

        @Override
        Map<String, Object> get() {
            return props
        }
    }
}
