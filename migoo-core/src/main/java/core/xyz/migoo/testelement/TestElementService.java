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

package core.xyz.migoo.testelement;

import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.assertion.Assertion;
import core.xyz.migoo.assertion.VerifyResult;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.processor.Processor;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.sampler.Sampler;
import core.xyz.migoo.variable.MiGooVariables;

import java.util.*;

import static core.xyz.migoo.testelement.AbstractTestElement.CONFIG;

/**
 * @author xiaomi
 */
public class TestElementService {

    private static final Map<String, Class<? extends TestElement>> SERVICES = new HashMap<>(20);

    static {
        for (var element : ServiceLoader.load(TestElement.class)) {
            addService(element.getClass());
        }
    }

    public static TestElement getService(String key) {
        Class<? extends TestElement> clazz = getServiceClass(key);
        if (Objects.isNull(clazz)) {
            throw new RuntimeException("No matching test element: " + key.toLowerCase());
        }
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Get test element instance error. ", e);
        }
    }

    public static Class<? extends TestElement> getServiceClass(String key) {
        return SERVICES.get(Optional.ofNullable(key).orElse("").toLowerCase());
    }

    public static void addService(Class<? extends TestElement> clazz) {
        Alias annotation = clazz.getAnnotation(Alias.class);
        if (annotation != null) {
            String[] aliasList = annotation.value();
            for (var alias : aliasList) {
                SERVICES.put(alias.toLowerCase(), clazz);
            }
        }
        SERVICES.put(clazz.getSimpleName().toLowerCase(), clazz);
    }

    public static Map<String, Class<? extends TestElement>> getServices() {
        return new HashMap<>(SERVICES);
    }

    public static SampleResult runTest(TestElement el, SampleResult... sampleResults) {
        return switch (el) {
            case Sampler s -> s.sample();
            case Processor p -> p.process();
            case Extractor e -> e.process(sampleResults[0]);
            case Assertion a -> {
                VerifyResult aResult = a.getResult(sampleResults[0]);
                sampleResults[0].setSuccessful(!sampleResults[0].isSuccessful() ? sampleResults[0].isSuccessful() : aResult.isSuccessful());
                sampleResults[0].getAssertionResults().add(aResult);
                yield sampleResults[0];
            }
            default -> throw new RuntimeException("不支持的测试元件：" + el.getClass().getName());
        };
    }

    public static void prepare(TestElement el, JSONObject properties, MiGooVariables variables) {
        el.setProperties(properties);
        if (Objects.nonNull(variables)) {
            el.setVariables(variables);
        }
    }

    public static void testStarted(TestElement el) {
        // 框架层处理变量替换
        el.convertVariable();
        // 支持协议（工具）相关取样器\处理器 配置信息存放在 config中
        if (el instanceof Processor || el instanceof Sampler) {
            var property = el.getPropertyAsMiGooProperty(CONFIG);
            el.setProperties(property);
        }
        if (el instanceof TestStateListener tsl) {
            tsl.testStarted();
        }
    }

    public static void testEnded(TestElement el) {
        if (el instanceof TestStateListener tsl) {
            tsl.testEnded();
        }
    }

}