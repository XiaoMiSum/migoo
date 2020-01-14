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


package xyz.migoo.framework.functions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;

/**
 * @author xiaomi
 * @date 2019/11/18 16:54
 */
public abstract class AbstractFunction implements Function{

    private final CompoundVariable parameters = new CompoundVariable();

    public Object execute() throws ExecuteError {
        return execute(parameters);
    }

    /**
     * execute function
     *
     * @param parameters function parameters
     * @return function execute result
     * @throws ExecuteError
     */
    @Override
    public abstract Object execute(CompoundVariable parameters) throws ExecuteError;

    @Override
    public void addParameter(String params, JSONObject variables){
        if (!params.isEmpty()) {
            String[] array = params.split(",");
            for (String str : array) {
                parameters.put(str, variables);
            }
        }
    }

    public String getParametersAsString(){
        return JSONObject.toJSONString(parameters);
    }

    /**
     * @return this function name
     */
    public String getFunctionName(){
        return this.getClass().getName();
    }
}
