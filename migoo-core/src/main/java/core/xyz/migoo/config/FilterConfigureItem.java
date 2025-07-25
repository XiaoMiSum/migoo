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

package core.xyz.migoo.config;

import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.filter.TestFilter;

import java.util.ArrayList;

/**
 * @author xiaomi
 * Created at 2025/7/20 14:51
 */
public class FilterConfigureItem<T extends TestFilter> extends ArrayList<T> implements ConfigureItem<FilterConfigureItem<T>> {

    @Override
    public FilterConfigureItem<T> merge(FilterConfigureItem<T> other) {
        FilterConfigureItem<T> filterConfigItem = new FilterConfigureItem<>();
        filterConfigItem.addAll(this);
        if (other != null)
            filterConfigItem.addAll(other);
        return filterConfigItem;
    }

    @Override
    public FilterConfigureItem<T> copy() {
        FilterConfigureItem<T> filterConfigItem = new FilterConfigureItem<>();
        filterConfigItem.addAll(this);
        return filterConfigItem;
    }

    @Override
    public FilterConfigureItem<T> calc(ContextWrapper context) {
        return this;
    }
}
