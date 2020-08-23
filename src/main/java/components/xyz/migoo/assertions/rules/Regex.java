/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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


package components.xyz.migoo.assertions.rules;

import com.alibaba.fastjson.JSON;
import core.xyz.migoo.assertions.rules.Alias;
import core.xyz.migoo.assertions.rules.IRule;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
@Alias(aliasList = {"regex"})
public class Regex extends BaseRule implements IRule {

    @Override
    public boolean assertTrue(Map<String, Object> data) {
        Object actual = data.get("actual");
        Object expect = data.get("expected");
        String str = "";
        if (actual instanceof JSON) {
            str = ((JSON) actual).toJSONString();
        }
        if (actual instanceof Number) {
            str = String.valueOf(actual);
        }
        if (actual instanceof String) {
            str = actual.toString();
        }
        Pattern pattern = Pattern.compile(expect.toString());
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
