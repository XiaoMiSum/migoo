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


package components.xyz.migoo.functions;

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.functions.InternalFunction;
import core.xyz.migoo.functions.CompoundVariable;
import core.xyz.migoo.functions.FunctionException;

/**
 * @author xiaomi
 * @date 2019/12/16 21:42
 */
public class Value implements InternalFunction {

    @Override
    public Object execute(CompoundVariable parameters) throws FunctionException {
        if (parameters.isEmpty()){
            throw new FunctionException("parameters con not be null");
        }
        JSONObject object = parameters.getJSONObject("object") != null ?
                parameters.getJSONObject("object") : parameters.getJSONObject("json");
        if (object == null){
            throw new FunctionException("object con not be null");
        }
        return object.get(parameters.getString("key"));
    }

    @Override
    public String funcKey() {
        return "VALUE";
    }
}
