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


package component.xyz.migoo.assertion.rule;

import core.xyz.migoo.assertion.Rule;
import core.xyz.migoo.testelement.Alias;

import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
@Alias({"contains", "contain", "ct", "⊆"})
public class Contains extends BaseRule implements Rule {

    @Override
    public boolean assertThat(Object actual, Object expected) {
        return switch (actual) {
            case String obj -> obj.contains(String.valueOf(expected));
            case Map<?, ?> obj -> obj.containsValue(expected) || obj.containsKey(expected);
            case List<?> obj -> obj.contains(expected);
            default -> false;
        };
    }
}
