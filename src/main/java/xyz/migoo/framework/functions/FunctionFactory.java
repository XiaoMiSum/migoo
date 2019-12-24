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
import org.reflections.Reflections;
import xyz.migoo.exception.ExecuteError;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import static xyz.migoo.framework.functions.CompoundVariable.FUNC_PATTERN;

/**
 * @author xiaomi
 * @date 2019/11/18 17:10
 */
public class FunctionFactory extends AbstractFunction{

    private final static FunctionFactory FACTORY = new FunctionFactory();
    private static final Reflections REFLECTIONS = new Reflections("xyz.migoo.functions");
    private static final Set<Class<? extends AbstractFunction>> SUB_TYPES = REFLECTIONS.getSubTypesOf(AbstractFunction.class);
    private static final Map<String, Class<? extends AbstractFunction>> FUNCTION_HASH_MAP = new HashMap<>(SUB_TYPES.size());

    static {
        for (Class<? extends AbstractFunction> sub : SUB_TYPES){
            FUNCTION_HASH_MAP.put(sub.getSimpleName().toUpperCase(), sub);
        }
    }

    public static Object execute(String object, JSONObject variables) throws ExecuteError {
        Matcher func = FUNC_PATTERN.matcher(object);
        if (func.find()) {
            FACTORY.getFunction(func.group(1));
            FACTORY.addParameter(func.group(2), variables);
            return FACTORY.execute(null);
        }
        throw new ExecuteError("not matcher function: " + object);
    }

    private AbstractFunction function;

    private void getFunction(String name) throws ExecuteError {
        try {
            function = FUNCTION_HASH_MAP.get(name.toUpperCase()).newInstance();
        } catch (Exception e) {
            throw new ExecuteError("get functions error. ", e);
        }
    }

    @Override
    public Object execute(CompoundVariable parameters) throws ExecuteError {
        return function.execute();
    }

    @Override
    public void addParameter(String parameters, JSONObject variables) {
        function.addParameter(parameters, variables);
    }
}
