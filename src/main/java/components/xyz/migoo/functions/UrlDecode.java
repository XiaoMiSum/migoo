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

import core.xyz.migoo.functions.InternalFunction;
import core.xyz.migoo.functions.CompoundVariable;
import core.xyz.migoo.functions.FunctionException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author xiaomi
 * @date 2019/11/18 17:22
 */
public class UrlDecode implements InternalFunction {

    @Override
    public String execute(CompoundVariable parameters) throws FunctionException {
        if (parameters.isEmpty()){
            throw new FunctionException("parameters con not be null");
        }
        try {
            String content = parameters.isNullKey("content") ?
                    parameters.getString("string") : parameters.getString("content");
            return URLDecoder.decode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new FunctionException("url decode exception", e);
        }
    }

    @Override
    public String funcKey() {
        return "URLDECODE";
    }
}
