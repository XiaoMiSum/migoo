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

package function.xyz.migoo;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONPath;
import core.xyz.migoo.function.Args;
import core.xyz.migoo.function.Function;
import core.xyz.migoo.function.KwArgs;
import core.xyz.migoo.function.LsArgs;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

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
            throw new IllegalArgumentException("args is empty or invalid args. require args 'json','path'");
        }
        return args instanceof KwArgs kwArgs ? execute(kwArgs) : execute((LsArgs) args);
    }

    private Object execute(KwArgs args) {
        return execute(args.get("json"), args.getString("path"));
    }

    private Object execute(LsArgs args) {
        return execute(args.getFirst(), args.getString(1));
    }

    private Object execute(Object json, String path) {
        if (Objects.isNull(json) || StringUtils.isBlank(path)) {
            throw new IllegalArgumentException("json or jsonpath con not be null");
        }
        if (json instanceof String str) {
            if (str.isBlank()) {
                throw new IllegalArgumentException("json or jsonpath con not be null");
            }
            return JSONPath.extract(str, path);
        }
        return JSONPath.extract(JSON.toJSONString(json), path);
    }
}
