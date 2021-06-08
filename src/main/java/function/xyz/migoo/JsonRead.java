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

package function.xyz.migoo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import core.xyz.migoo.function.Args;
import core.xyz.migoo.function.Function;

/**
 * @author mi.xiao
 * @date 2021/5/31 23:32
 */
public class JsonRead implements Function {

    /**
     * 通过 json path 读取数据
     * 参数顺序
     * json: json对象
     * path: jsonpath
     *
     * @param args 包含json对象、json path的复合参数
     * @return jsonpath对应数据
     */
    @Override
    public Object execute(Args args) {
        if (args.isEmpty() || args.size() < 2) {
            throw new IllegalArgumentException("args is empty or invalid args.");
        }
        if (args.getString(0).isEmpty() || args.getString(1).isEmpty()) {
            throw new IllegalArgumentException("json or jsonpath con not be null");
        }
        return args.get(0) instanceof String ? JSONPath.read((String) args.get(0), args.getString(1)) :
                JSONPath.read(JSON.toJSONString(args.get(0)), args.getString(1));
    }
}
