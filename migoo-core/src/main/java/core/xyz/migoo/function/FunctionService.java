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

package core.xyz.migoo.function;

import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.variable.MiGooVariables;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author xiaomi
 */
public class FunctionService {

    private static final Map<String, Function> SERVICES = new HashMap<>(16);

    static {
        for (Function service : ServiceLoader.load(Function.class)) {
            addService(service);
        }
    }

    public static void addService(Function service) {
        var alias = service.getClass().getAnnotation(Alias.class);
        if (alias != null) {
            for (var key : alias.value()) {
                SERVICES.put(key.toLowerCase(), service);
            }
        }
        SERVICES.put(service.getClass().getSimpleName().toLowerCase(), service);
    }

    public static Object execute(String fName, String parameter, MiGooVariables variables) {
        return getService(fName).execute(Args.newArgs(parameter, variables));
    }

    public static Object execute(String fName, String parameter) {
        return execute(fName, parameter, new MiGooVariables());
    }

    private static Function getService(String fName) {
        var function = SERVICES.get(fName.toLowerCase());
        if (function == null) {
            throw new RuntimeException("No matcher the function: " + fName);
        }
        return function;
    }
}