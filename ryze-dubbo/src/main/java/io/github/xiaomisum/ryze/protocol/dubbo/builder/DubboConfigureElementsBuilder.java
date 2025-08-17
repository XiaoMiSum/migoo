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

package io.github.xiaomisum.ryze.protocol.dubbo.builder;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.core.builder.ExtensibleConfigureElementsBuilder;
import io.github.xiaomisum.ryze.protocol.dubbo.config.DubboDefaults;
import io.github.xiaomisum.ryze.support.Customizer;

import static io.github.xiaomisum.ryze.support.groovy.Groovy.call;

/**
 * dubbo 自定义配置元件列表构建器，提供 dubbo 自定义配置元件列表的构建方法
 *
 * @author xiaomi
 */
public class DubboConfigureElementsBuilder extends ExtensibleConfigureElementsBuilder<DubboConfigureElementsBuilder> {

    public static DubboConfigureElementsBuilder builder() {
        return new DubboConfigureElementsBuilder();
    }

    public DubboConfigureElementsBuilder dubbo(DubboDefaults defaults) {
        configureElements.add(defaults);
        return self;
    }

    public DubboConfigureElementsBuilder dubbo(Customizer<DubboDefaults.Builder> customizer) {
        var builder = DubboDefaults.builder();
        customizer.customize(builder);
        configureElements.add(builder.build());
        return self;
    }

    public DubboConfigureElementsBuilder dubbo(DubboDefaults.Builder builder) {
        configureElements.add(builder.build());
        return self;
    }

    public DubboConfigureElementsBuilder dubbo(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = DubboDefaults.Builder.class) Closure<?> closure) {
        var builder = DubboDefaults.builder();
        call(closure, builder);
        configureElements.add(builder.build());
        return self;
    }
}
