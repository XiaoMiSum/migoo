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

package io.github.xiaomisum.ryze.core.config;

import com.alibaba.fastjson2.JSONObject;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;

import java.util.Map;
import java.util.function.Consumer;

/**
 * @author xiaomi
 */
public class RyzeVariables extends JSONObject implements ConfigureItem<RyzeVariables> {

    public RyzeVariables() {
    }

    public RyzeVariables(Map<? extends String, ?> variables) {
        this.putAll(variables);
    }

    public static Builder builder() {
        return new Builder();
    }

    public void putAll(Map<? extends String, ?> variables) {
        if (variables != null) {
            super.putAll(variables);
        }
    }

    public RyzeVariables add(String key, Object value) {
        super.put(key, value);
        return this;
    }

    @Override
    public RyzeVariables merge(RyzeVariables other) {
        if (other != null) {
            var copy = new RyzeVariables(this);
            this.clear();
            this.putAll(other);
            this.putAll(copy);
        }
        return this;
    }

    @Override
    public RyzeVariables copy() {
        return new RyzeVariables(this);
    }

    @Override
    public RyzeVariables evaluate(ContextWrapper context) {
        this.replaceAll((key, value) -> context.evaluate(value));
        return this;
    }

    public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, RyzeVariables> {

        private final RyzeVariables variables = new RyzeVariables();

        public Builder put(Map<? extends String, ?> variables) {
            this.variables.putAll(variables);
            return this;
        }

        public Builder put(RyzeVariables variables) {
            this.variables.merge(variables);
            return this;
        }

        public Builder put(Consumer<RyzeVariables> consumer) {
            consumer.accept(variables);
            return this;
        }

        public Builder put(String name, Object value) {
            this.variables.put(name, value);
            return this;
        }

        @Override
        public RyzeVariables build() {
            return variables;
        }
    }
}