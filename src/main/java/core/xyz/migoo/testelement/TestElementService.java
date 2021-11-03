/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo.testelement;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author xiaomi
 */
public class TestElementService {

    private static final Map<String, Class<? extends TestElement>> SERVICES = new HashMap<>(20);

    static {
        for (TestElement element : ServiceLoader.load(TestElement.class)) {
            addService(element.getClass());
        }
    }

    public static TestElement getService(String key) {
        Class<? extends TestElement> clazz = SERVICES.get(key.toLowerCase());
        if (clazz == null) {
            throw new RuntimeException("No matching test element: " + key.toLowerCase());
        }
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Get test element instance error. ", e);
        }
    }

    public static void addService(Class<? extends TestElement> clazz) {
        Alias annotation = clazz.getAnnotation(Alias.class);
        if (annotation != null) {
            String[] aliasList = annotation.aliasList();
            for (String alias : aliasList) {
                SERVICES.put(alias.toLowerCase(), clazz);
            }
        }
        SERVICES.put(clazz.getSimpleName().toLowerCase(), clazz);
    }

    public static Map<String, Class<? extends TestElement>> getServices() {
        return new HashMap<>(SERVICES);
    }
}