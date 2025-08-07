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

package io.github.xiaomisum.ryze.core;

import io.github.xiaomisum.ryze.core.assertion.Assertion;
import io.github.xiaomisum.ryze.core.assertion.Rule;
import io.github.xiaomisum.ryze.core.context.GlobalContext;
import io.github.xiaomisum.ryze.core.extractor.Extractor;
import io.github.xiaomisum.ryze.core.function.Function;
import io.github.xiaomisum.ryze.core.interceptor.RyzeInterceptor;
import io.github.xiaomisum.ryze.core.interceptor.report.ReporterListener;
import io.github.xiaomisum.ryze.core.testelement.TestElement;
import io.github.xiaomisum.ryze.core.testelement.configure.ConfigureElement;
import io.github.xiaomisum.ryze.core.testelement.processor.Postprocessor;
import io.github.xiaomisum.ryze.core.testelement.processor.Preprocessor;
import io.github.xiaomisum.ryze.support.RyzeServiceLoader;
import io.github.xiaomisum.ryze.support.fastjson.interceptor.JSONInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes"})
public class ApplicationConfig {


    // 关键字字典相关读写锁
    private static final ReadWriteLock TEST_ELEMENT_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock CONFIG_ELEMENT_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock PREPROCESSOR_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock POSTPROCESSOR_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock ASSERTION_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock EXTRACTOR_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock FUNCTIONS_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock RULE_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock JSON_INTERCEPTOR_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock REPORT_LISTENER_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock TEST_INTERCEPTOR_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock globalContextLock = new ReentrantReadWriteLock(false);

    private static Map<String, Class<? extends TestElement>> TEST_ELEMENT_KEY_MAP;
    private static Map<String, Class<? extends ConfigureElement>> CONFIG_ELEMENT_KEY_MAP;
    private static Map<String, Class<? extends Preprocessor>> PREPROCESSOR_KEY_MAP;
    private static Map<String, Class<? extends Postprocessor>> POSTPROCESSOR_KEY_MAP;
    private static Map<String, Class<? extends Assertion>> ASSERTION_KEY_MAP;
    private static Map<String, Class<? extends Extractor>> EXTRACTOR_KEY_MAP;
    private static List<Function> FUNCTIONS;
    private static Map<String, Rule> RULE_KEY_MAP;
    private static List<? extends ReporterListener> REPORT_LISTENERS;
    private static Map<String, Class<? extends RyzeInterceptor>> TEST_INTERCEPTOR_KEY_MAP;
    private static Map<Class<?>, JSONInterceptor> JSON_INTERCEPTOR_KEY_MAP;
    private static GlobalContext globalContext;

    public static Map<String, Class<? extends TestElement>> getTestElementKeyMap() {
        return getDataMap(TEST_ELEMENT_KEY_MAP_LOCK,
                () -> ApplicationConfig.TEST_ELEMENT_KEY_MAP,
                () -> {
                    ApplicationConfig.TEST_ELEMENT_KEY_MAP = RyzeServiceLoader.loadAsMapBySPI(TestElement.class);
                    return ApplicationConfig.TEST_ELEMENT_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends ConfigureElement>> getConfigureElementKeyMap() {
        return getDataMap(CONFIG_ELEMENT_KEY_MAP_LOCK,
                () -> ApplicationConfig.CONFIG_ELEMENT_KEY_MAP,
                () -> {
                    ApplicationConfig.CONFIG_ELEMENT_KEY_MAP = RyzeServiceLoader.loadAsMapBySPI(ConfigureElement.class);
                    return ApplicationConfig.CONFIG_ELEMENT_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends Preprocessor>> getPreprocessorKeyMap() {
        return getDataMap(PREPROCESSOR_KEY_MAP_LOCK,
                () -> ApplicationConfig.PREPROCESSOR_KEY_MAP,
                () -> {
                    ApplicationConfig.PREPROCESSOR_KEY_MAP = RyzeServiceLoader.loadAsMapBySPI(Preprocessor.class);
                    return ApplicationConfig.PREPROCESSOR_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends Postprocessor>> getPostprocessorKeyMap() {
        return getDataMap(POSTPROCESSOR_KEY_MAP_LOCK,
                () -> ApplicationConfig.POSTPROCESSOR_KEY_MAP,
                () -> {
                    ApplicationConfig.POSTPROCESSOR_KEY_MAP = RyzeServiceLoader.loadAsMapBySPI(Postprocessor.class);
                    return ApplicationConfig.POSTPROCESSOR_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends Assertion>> getAssertionKeyMap() {
        return getDataMap(ASSERTION_KEY_MAP_LOCK,
                () -> ApplicationConfig.ASSERTION_KEY_MAP,
                () -> {
                    ApplicationConfig.ASSERTION_KEY_MAP = RyzeServiceLoader.loadAsMapBySPI(Assertion.class);
                    return ApplicationConfig.ASSERTION_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends Extractor>> getExtractorKeyMap() {
        return getDataMap(EXTRACTOR_KEY_MAP_LOCK,
                () -> ApplicationConfig.EXTRACTOR_KEY_MAP,
                () -> {
                    ApplicationConfig.EXTRACTOR_KEY_MAP = RyzeServiceLoader.loadAsMapBySPI(Extractor.class);
                    return ApplicationConfig.EXTRACTOR_KEY_MAP;
                }
        );
    }

    public static List<Function> getFunctions() {
        return getDataMap(FUNCTIONS_LOCK,
                () -> ApplicationConfig.FUNCTIONS,
                () -> {
                    ApplicationConfig.FUNCTIONS = RyzeServiceLoader.loadAsInstanceListBySPI(Function.class);
                    return ApplicationConfig.FUNCTIONS;
                }
        );
    }

    public static Map<String, Rule> getRuleKeyMap() {
        return getDataMap(RULE_KEY_MAP_LOCK,
                () -> ApplicationConfig.RULE_KEY_MAP,
                () -> {
                    ApplicationConfig.RULE_KEY_MAP = RyzeServiceLoader.loadAsInstanceMapBySPI(Rule.class);
                    return ApplicationConfig.RULE_KEY_MAP;
                }
        );
    }

    public static Map<Class<?>, JSONInterceptor> getJsonInterceptorKeyMap() {
        return getDataMap(JSON_INTERCEPTOR_KEY_MAP_LOCK,
                () -> ApplicationConfig.JSON_INTERCEPTOR_KEY_MAP,
                () -> {
                    var map = new HashMap<Class<?>, JSONInterceptor>();
                    RyzeServiceLoader.loadAsInstanceListBySPI(JSONInterceptor.class).forEach(item -> item.getSupportedClasses().forEach(clazz -> map.put(clazz, item)));
                    ApplicationConfig.JSON_INTERCEPTOR_KEY_MAP = map;
                    return ApplicationConfig.JSON_INTERCEPTOR_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends RyzeInterceptor>> getTestInterceptorKeyMap() {
        return getDataMap(TEST_INTERCEPTOR_KEY_MAP_LOCK,
                () -> ApplicationConfig.TEST_INTERCEPTOR_KEY_MAP,
                () -> {
                    ApplicationConfig.TEST_INTERCEPTOR_KEY_MAP = RyzeServiceLoader.loadAsMapBySPI(RyzeInterceptor.class);
                    return ApplicationConfig.TEST_INTERCEPTOR_KEY_MAP;
                }
        );
    }

    public static List<? extends ReporterListener> getReporterListeners() {
        return getDataMap(REPORT_LISTENER_LOCK,
                () -> ApplicationConfig.REPORT_LISTENERS,
                () -> {
                    ApplicationConfig.REPORT_LISTENERS = RyzeServiceLoader.loadAsInstanceListBySPI(ReporterListener.class);
                    return ApplicationConfig.REPORT_LISTENERS;
                }
        );
    }

    private static <T> T getDataMap(ReadWriteLock readWriteLock, Supplier<T> readWorker, Supplier<T> writeWorker) {
        var readLock = readWriteLock.readLock();
        var writeLock = readWriteLock.writeLock();
        readLock.lock();
        try {
            var clzMap = readWorker.get();
            if (Objects.nonNull(clzMap)) {
                return clzMap;
            }
        } finally {
            readLock.unlock();
        }
        writeLock.lock();
        try {
            var clzMap = readWorker.get();
            if (Objects.isNull(clzMap)) {
                clzMap = writeWorker.get();
            }
            return clzMap;
        } finally {
            writeLock.unlock();
        }
    }
}
