/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.function;

import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.variable.MiGooVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 */
public class KwArgs extends JSONObject implements Args {

    private final MiGooVariables variables;

    public KwArgs(MiGooVariables variables) {
        this.variables = variables;
    }

    public Object put(String parameter) {
        List<String> array = new ArrayList<>(List.of(parameter.trim().split("=")));
        if (array.size() == 1) {
            array.add("");
        }
        return this.put(array.getFirst().trim(), getParameterValue(array.getLast().trim()));
    }

    @Override
    public MiGooVariables getCurrentVars() {
        return this.variables;
    }
}
