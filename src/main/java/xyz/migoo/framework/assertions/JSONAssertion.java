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


package xyz.migoo.framework.assertions;

import com.alibaba.fastjson.JSONPath;
import xyz.migoo.framework.assertions.function.Alias;
import xyz.migoo.simplehttp.Response;

/**
 * @author xiaomi
 * @date 2019-04-13 21:37
 */
@Alias(aliasList = {"^\\$.[\\w]+(.\\w+)*", "^json.[\\w]+(.\\w+)*", "^body.[\\w]+(.\\w+)*"})
public class JSONAssertion extends AssertionFactory {

    private static final String BODY_ = "body";
    private static final String JSON_ = "json";

    private String jsonPath;

    public void setJsonPath(String jsonPath){
        if (jsonPath.startsWith(JSON_) || jsonPath.startsWith(BODY_)){
            jsonPath = "$" + jsonPath.substring(BODY_.length());
        }
        this.jsonPath = jsonPath;
    }

    @Override
    public void setActual(Object actual) {
        Response response = (Response) actual;
        this.actual = JSONPath.read(response.text(), jsonPath);
    }
}
