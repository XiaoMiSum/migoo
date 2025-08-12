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

package io.github.xiaomisum.ryze.protocol.kafka.builder;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.core.builder.ExtensibleConfigureElementsBuilder;
import io.github.xiaomisum.ryze.protocol.kafka.config.KafkaDefaults;
import io.github.xiaomisum.ryze.support.Customizer;

import static io.github.xiaomisum.ryze.support.groovy.Groovy.call;

/**
 * kafka 自定义配置元件列表构建器，提供 kafka 自定义配置元件列表的构建方法
 *
 * @author xiaomi
 */
public class KafkaConfigureElementsBuilder extends ExtensibleConfigureElementsBuilder<KafkaConfigureElementsBuilder> {

    public static KafkaConfigureElementsBuilder builder() {
        return new KafkaConfigureElementsBuilder();
    }

    public KafkaConfigureElementsBuilder kafka(KafkaDefaults defaults) {
        this.configureElements.add(defaults);
        return self;
    }

    public KafkaConfigureElementsBuilder kafka(KafkaDefaults.Builder builder) {
        this.configureElements.add(builder.build());
        return self;
    }

    public KafkaConfigureElementsBuilder kafka(Customizer<KafkaDefaults.Builder> customizer) {
        var builder = KafkaDefaults.builder();
        customizer.customize(builder);
        this.configureElements.add(builder.build());
        return self;
    }

    public KafkaConfigureElementsBuilder kafka(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = KafkaDefaults.Builder.class) Closure<?> closure) {
        var builder = KafkaDefaults.builder();
        call(closure, builder);
        this.configureElements.add(builder.build());
        return self;
    }
}
