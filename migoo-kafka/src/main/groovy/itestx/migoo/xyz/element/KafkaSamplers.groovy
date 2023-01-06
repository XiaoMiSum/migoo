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


import itestx.xyz.migoo.element.Samplers
import itestx.xyz.migoo.logic.ITestx

/**
 * @author xiaomi 
 * Created at 2022/8/27 12:55
 */
class KafkaSamplers {


    private static final Builder builder = new Builder()

    static Builder builder() {
        return builder
    }

    static class Builder {

        KafkaSampler KafkaSampler() {
            return new KafkaSampler()
        }

    }

    static class KafkaSampler extends Samplers implements ITestx {
        private final Map<String, Object> props = new LinkedHashMap<>(16)

        private KafkaSampler() {
            props.put("testclass", "KafkaSampler")
            props.put("acks", "1")
            props.put("retries", 5)
            props.put("linger.ms", 2000)
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        }

        KafkaSampler bootstrap(String servers) {
            props.put("bootstrap.servers", servers)
            return this
        }

        KafkaSampler acks(String acks) {
            props.put("acks", acks)
            return this
        }

        KafkaSampler retries(int retries) {
            props.put("retries", retries)
            return this
        }

        KafkaSampler linger(int seconds) {
            props.put("linger.ms", seconds)
            return this
        }

        KafkaSampler keySerializer(String serializer) {
            props.put("key.serializer", serializer)
            return this
        }

        KafkaSampler valueSerializer(String serializer) {
            props.put("value.serializer", serializer)
            return this
        }

        KafkaSampler topic(String topic) {
            props.put("topic", topic)
            return this
        }

        KafkaSampler key(String key) {
            props.put("key", key)
            return this
        }

        KafkaSampler message(Object message) {
            props.put("message", message)
            return this
        }

        @Override
        Map<String, Object> get() {
            return props
        }
    }
}
