/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
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
 */

package components.xyz.migoo.extractor;

import core.xyz.migoo.extractor.AbstractExtractor;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.Alias;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 */
@Alias(aliasList = {"RegexExtractor", "regex_extractor"})
public class RegexExtractor extends AbstractExtractor {

    private static final long serialVersionUID = -5926230173518436842L;

    @Override
    public SampleResult process(SampleResult result) {
        SampleResult extractorResult = new SampleResult("RegexExtractor");
        Pattern pattern = Pattern.compile(getPropertyAsString(FIELD));
        Matcher matcher = pattern.matcher(result.getResponseDataAsString());
        int matchNum = get(MATCH_NUM) == null || getPropertyAsInt(MATCH_NUM) < 0 ? 0 : getPropertyAsInt(MATCH_NUM);
        Object value = "def_value";
        int state = 0;
        while (state > -1 && matcher.find()) {
            state = matcher.groupCount() > 0 ? matchNum : state;
            value = matcher.group(state > 0 ? 1 : 0);
            state--;
        }
        getVariables().put(getPropertyAsString(VARIABLE_NAME), value);
        getProperty().put("value", value);
        extractorResult.setSamplerData(getProperty().toString());
        return extractorResult;
    }
}