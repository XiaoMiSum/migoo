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

package io.github.xiaomisum.ryze.protocol.mongo;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import io.github.xiaomisum.ryze.MagicBox;
import io.github.xiaomisum.ryze.core.Result;
import io.github.xiaomisum.ryze.protocol.mongo.sampler.MongoSampler;
import io.github.xiaomisum.ryze.support.Customizer;
import io.github.xiaomisum.ryze.support.groovy.Groovy;

/**
 * 魔法盒子 提供函数式取样器执行入口
 *
 * @author xiaomi
 */
public class MongoMagicBox extends MagicBox {

    public static Result mongo(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = MongoSampler.Builder.class) Closure<?> closure) {
        return mongo("", closure);
    }

    public static Result mongo(String title,
                               @DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = MongoSampler.Builder.class) Closure<?> closure) {
        var builder = MongoSampler.builder();
        Groovy.call(closure, builder);
        return MagicBox.runTest(title, builder.build());
    }

    public static Result mongo(Customizer<MongoSampler.Builder> customizer) {
        return mongo("", customizer);
    }

    public static Result mongo(String title, Customizer<MongoSampler.Builder> customizer) {
        var builder = MongoSampler.builder();
        customizer.customize(builder);
        return MagicBox.runTest(title, builder.build());
    }
}
