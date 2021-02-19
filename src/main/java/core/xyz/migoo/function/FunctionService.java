/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2021. Lorem XiaoMiSum (mi_xiao@qq.com)
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
 */

package core.xyz.migoo.function;

import core.xyz.migoo.variables.MiGooVariables;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class FunctionService {

    private static final Map<String, Function> SERVICES = new HashMap<>(16);

    static {
        for (Function service : ServiceLoader.load(Function.class)) {
            SERVICES.put(service.getClass().getSimpleName().toLowerCase(), service);
        }
    }

    public static void addService(Class<? extends Function> clz) throws Exception {
        SERVICES.put(clz.getSimpleName().toLowerCase(), clz.newInstance());
    }

    public static Object execute(String fName, String parameter, MiGooVariables variables) throws Exception {
        Function function = getService(fName);
        CompoundParameter parameters = getParameters(parameter, variables);
        return function.execute(parameters);
    }

    private static Function getService(String fName) throws Exception {
        fName = fName.toLowerCase();
        Function function = SERVICES.get(fName);
        if (function == null) {
            throw new Exception("No matcher the function: " + fName);
        }
        return function;
    }

    private static CompoundParameter getParameters(String origin, MiGooVariables variables) throws Exception {
        final CompoundParameter parameters = new CompoundParameter(variables);
        if (StringUtils.isNotBlank(origin)) {
            String[] array = origin.split(",");
            for (String str : array) {
                parameters.put(str);
            }
        }
        return parameters;
    }
}