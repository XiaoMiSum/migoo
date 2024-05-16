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

package component.xyz.migoo.extractor;

import core.xyz.migoo.extractor.AbstractExtractor;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.Alias;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
@Alias({"RegexExtractor", "regex_extractor"})
public class RegexExtractor extends AbstractExtractor {

    @Override
    public SampleResult process(SampleResult result) {
        Pattern pattern = Pattern.compile(getPropertyAsString(FIELD));
        Matcher matcher = pattern.matcher(result.getResponseDataAsString());
        int matchNum = get(MATCH_NUM) == null || getPropertyAsInt(MATCH_NUM) < 0 ? 0 : getPropertyAsInt(MATCH_NUM);
        Object value = "def_value";
        int state = 0;
        while (state > -1 && matcher.find()) {
            state = matcher.groupCount() > 0 ? matchNum : state;
            value = matcher.group(1);
            state--;
        }
        getVariables().put(getPropertyAsString(VARIABLE_NAME), value);
        getProperty().put(VALUE, value);
        return getResult(new SampleResult("RegexExtractor"));
    }
}