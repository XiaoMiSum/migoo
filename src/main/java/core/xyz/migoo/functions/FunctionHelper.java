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


package core.xyz.migoo.functions;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2019/11/18 17:10
 */
public class FunctionHelper {

    public static final Pattern FUNC_PATTERN = Pattern.compile("^__(\\w+)\\((.*)\\)");

    private final static FunctionHelper FACTORY = new FunctionHelper();

    private static final Map<String, Function> SELF_DEFINED_FUNCTIONS = new HashMap<>(100);

    static {
        ServiceLoader<Function> loaders = ServiceLoader.load(Function.class);
        for (Function function : loaders) {
            SELF_DEFINED_FUNCTIONS.put(function.funcKey(), function);
        }
    }

    public static Object execute(String object, JSONObject variables) throws FunctionException {
        Matcher func = FUNC_PATTERN.matcher(object);
        if (func.find()) {
            FACTORY.initFunc(func.group(1));
            FACTORY.initParameter(func.group(2), variables);
            return FACTORY.execute();
        }
        throw new FunctionException("not matcher function: " + object);
    }

    private Function function;

    private final CompoundVariable parameters = new CompoundVariable();

    private final Map<String, AbstractFunction> defaultFunctions = new HashMap<>(11);

    private void initFunc(String name) throws FunctionException {
        if (defaultFunctions.isEmpty()) {
            ServiceLoader<AbstractFunction> loaders = ServiceLoader.load(AbstractFunction.class);
            for (AbstractFunction function : loaders) {
                defaultFunctions.put(function.getClass().getSimpleName().toUpperCase(), function);
            }
        }
        String upper = name.toUpperCase();
        function = defaultFunctions.get(upper) == null ? SELF_DEFINED_FUNCTIONS.get(upper) : defaultFunctions.get(upper);
        if (function == null) {
            throw new FunctionException("function not found: " + name);
        }
    }

    public void initParameter(String origin, JSONObject variables) throws FunctionException {
        try {
            if (StringUtils.isNotBlank(origin)) {
                String[] array = origin.split(",");
                for (String str : array) {
                    parameters.put(str, variables);
                }
            }
        } catch (Exception e) {
            throw new FunctionException("init parameter exception", e);
        }
    }

    public Object execute() throws FunctionException {
        return function.execute(parameters);
    }
}
