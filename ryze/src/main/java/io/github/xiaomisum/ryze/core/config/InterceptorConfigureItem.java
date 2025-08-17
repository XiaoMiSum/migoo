/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package io.github.xiaomisum.ryze.core.config;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.interceptor.RyzeInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 * Created at 2025/7/20 14:51
 */
public class InterceptorConfigureItem<T extends RyzeInterceptor> extends ArrayList<T> implements ConfigureItem<InterceptorConfigureItem<T>> {

    public InterceptorConfigureItem() {

    }

    public InterceptorConfigureItem(List<T> interceptors) {
        super(interceptors);
    }

    @Override
    public InterceptorConfigureItem<T> merge(InterceptorConfigureItem<T> other) {
        InterceptorConfigureItem<T> filterConfigItem = new InterceptorConfigureItem<>();
        filterConfigItem.addAll(this);
        if (other != null) {
            filterConfigItem.addAll(other);
        }

        return filterConfigItem;
    }

    @Override
    public InterceptorConfigureItem<T> copy() {
        InterceptorConfigureItem<T> filterConfigItem = new InterceptorConfigureItem<>();
        filterConfigItem.addAll(this);
        return filterConfigItem;
    }

    @Override
    public InterceptorConfigureItem<T> evaluate(ContextWrapper context) {
        return this;
    }
}
