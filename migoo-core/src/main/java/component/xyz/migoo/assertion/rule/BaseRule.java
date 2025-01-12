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

import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 */
public abstract class BaseRule {

    private final static ThreadLocal<DecimalFormat> FORMAT_THREAD_LOCAL =
            ThreadLocal.withInitial(BaseRule::createDecimalFormat);

    private static DecimalFormat createDecimalFormat() {
        var decimalFormatter = new DecimalFormat("#.#");
        // java.text.DecimalFormat.DOUBLE_FRACTION_DIGITS == 340
        decimalFormatter.setMaximumFractionDigits(340);
        decimalFormatter.setMinimumFractionDigits(1);
        return decimalFormatter;
    }

    public String objectToString(Object subj) {
        return objectToString(subj, "null");
    }

    public String objectToString(Object subj, String defaultString) {
        defaultString = StringUtils.isBlank(defaultString) ? "null" : defaultString;
        return switch (subj) {
            case String string -> StringUtils.isBlank(string) ? defaultString : string;
            case List<?> objects -> objects.isEmpty() ? defaultString : JSON.toJSONString(objects);
            case Map<?, ?> entry -> entry.isEmpty() ? defaultString : JSON.toJSONString(entry);
            case Double ignored -> FORMAT_THREAD_LOCAL.get().format(subj);
            case Float ignored -> FORMAT_THREAD_LOCAL.get().format(subj);
            case null -> defaultString;
            default -> subj.toString();
        };
    }

}
