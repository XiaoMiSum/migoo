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

package core.xyz.migoo;

import core.xyz.migoo.assertion.Assertion;
import core.xyz.migoo.assertion.Rule;
import core.xyz.migoo.config.ConfigureElement;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.function.Function;
import core.xyz.migoo.processor.Postprocessor;
import core.xyz.migoo.processor.Preprocessor;
import core.xyz.migoo.testelement.TestElement;
import support.xyz.migoo.MiGooServiceLoader;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ApplicationConfig {


    // 关键字字典相关读写锁
    private static final ReadWriteLock TEST_ELEMENT_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock CONFIG_ELEMENT_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock PREPROCESSOR_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock POSTPROCESSOR_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock ASSERTION_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock EXTRACTOR_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock FUNCTION_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);
    private static final ReadWriteLock RULE_KEY_MAP_LOCK = new ReentrantReadWriteLock(false);

    private static Map<String, Class<? extends TestElement>> TEST_ELEMENT_KEY_MAP;
    private static Map<String, Class<? extends ConfigureElement>> CONFIG_ELEMENT_KEY_MAP;
    private static Map<String, Class<? extends Preprocessor>> PREPROCESSOR_KEY_MAP;
    private static Map<String, Class<? extends Postprocessor>> POSTPROCESSOR_KEY_MAP;
    private static Map<String, Class<? extends Assertion>> ASSERTION_KEY_MAP;
    private static Map<String, Class<? extends Extractor>> EXTRACTOR_KEY_MAP;
    private static Map<String, Class<? extends Function>> FUNCTION_KEY_MAP;
    private static Map<String, Rule> RULE_KEY_MAP;

    public static Map<String, Class<? extends TestElement>> getTestElementKeyMap() {
        return getClassMap(TEST_ELEMENT_KEY_MAP_LOCK,
                () -> ApplicationConfig.TEST_ELEMENT_KEY_MAP,
                () -> {
                    ApplicationConfig.TEST_ELEMENT_KEY_MAP = MiGooServiceLoader.loadAsMapBySPI(TestElement.class);
                    return ApplicationConfig.TEST_ELEMENT_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends ConfigureElement>> getConfigElementKeyMap() {
        return getClassMap(CONFIG_ELEMENT_KEY_MAP_LOCK,
                () -> ApplicationConfig.CONFIG_ELEMENT_KEY_MAP,
                () -> {
                    ApplicationConfig.CONFIG_ELEMENT_KEY_MAP = MiGooServiceLoader.loadAsMapBySPI(ConfigureElement.class);
                    return ApplicationConfig.CONFIG_ELEMENT_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends Preprocessor>> getPreprocessorKeyMap() {
        return getClassMap(PREPROCESSOR_KEY_MAP_LOCK,
                () -> ApplicationConfig.PREPROCESSOR_KEY_MAP,
                () -> {
                    ApplicationConfig.PREPROCESSOR_KEY_MAP = MiGooServiceLoader.loadAsMapBySPI(Preprocessor.class);
                    return ApplicationConfig.PREPROCESSOR_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends Postprocessor>> getPostprocessorKeyMap() {
        return getClassMap(POSTPROCESSOR_KEY_MAP_LOCK,
                () -> ApplicationConfig.POSTPROCESSOR_KEY_MAP,
                () -> {
                    ApplicationConfig.POSTPROCESSOR_KEY_MAP = MiGooServiceLoader.loadAsMapBySPI(Postprocessor.class);
                    return ApplicationConfig.POSTPROCESSOR_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends Assertion>> getAssertionKeyMap() {
        return getClassMap(ASSERTION_KEY_MAP_LOCK,
                () -> ApplicationConfig.ASSERTION_KEY_MAP,
                () -> {
                    ApplicationConfig.ASSERTION_KEY_MAP = MiGooServiceLoader.loadAsMapBySPI(Assertion.class);
                    return ApplicationConfig.ASSERTION_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends Extractor>> getExtractorKeyMap() {
        return getClassMap(EXTRACTOR_KEY_MAP_LOCK,
                () -> ApplicationConfig.EXTRACTOR_KEY_MAP,
                () -> {
                    ApplicationConfig.EXTRACTOR_KEY_MAP = MiGooServiceLoader.loadAsMapBySPI(Extractor.class);
                    return ApplicationConfig.EXTRACTOR_KEY_MAP;
                }
        );
    }

    public static Map<String, Class<? extends Function>> getFunctionKeyMap() {
        return getClassMap(FUNCTION_KEY_MAP_LOCK,
                () -> ApplicationConfig.FUNCTION_KEY_MAP,
                () -> {
                    ApplicationConfig.FUNCTION_KEY_MAP = MiGooServiceLoader.loadAsMapBySPI(Function.class);
                    return ApplicationConfig.FUNCTION_KEY_MAP;
                }
        );
    }

    public static Map<String, Rule> getRuleKeyMap() {
        return getClassMap(RULE_KEY_MAP_LOCK,
                () -> ApplicationConfig.RULE_KEY_MAP,
                () -> {
                    ApplicationConfig.RULE_KEY_MAP = (Map<String, Rule>) MiGooServiceLoader.loadAsInstanceMapBySPI(Rule.class);
                    return ApplicationConfig.RULE_KEY_MAP;
                }
        );
    }

    private static <T> T getClassMap(ReadWriteLock readWriteLock, Supplier<T> readWorker, Supplier<T> writeWorker) {
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
