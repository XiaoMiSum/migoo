/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package coder.xyz.migoo.suite;

import coder.xyz.migoo.Configurer;
import coder.xyz.migoo.El;
import coder.xyz.migoo.Processor;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Testsuite extends El implements Suite {

    Testsuite(String title, Map<String, Object> variables, Suite[] children, Configurer[] configurers,
              Processor[] preprocessors, Processor[] postprocessors) {
        p("title", title);
        p("variables", variables);
        el("configelements", configurers);
        children(children);
        el("preprocessors", preprocessors);
        el("postprocessors", postprocessors);
    }

    protected void el(String key, El[] els) {
        if (Objects.nonNull(els) && els.length > 0) {
            List<Map<String, Object>> v = Lists.newArrayList();
            for (El el : els) {
                v.add(el.customize());
            }
            p(key, v);
        }
    }

    protected void children(Suite[] els) {
        if (Objects.nonNull(els) && els.length > 0) {
            List<Map<String, Object>> v = Lists.newArrayList();
            for (Suite el : els) {
                v.add(el.customize());
            }
            p("children", v);
        }
    }
}
