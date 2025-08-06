package io.github.xiaomisum.ryze.support.fastjson.interceptor;

import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.testelement.TestElement;
import io.github.xiaomisum.ryze.core.testelement.processor.Postprocessor;
import io.github.xiaomisum.ryze.core.testelement.processor.Preprocessor;

import java.util.List;
import java.util.Map;

public interface JSONInterceptor {

    List<Class<?>> getSupportedClasses();


    /**
     * 反序列化测试元件的 config 属性，同时将使用当前已过期的配置项转化为最新配置
     * <p>
     * 如果不需要反序列化，则返回 null，调用方应当自行对 null 进行处理
     *
     * @param value 数据
     * @return 标准化数据
     */
    default ConfigureItem<?> deserializeConfigureItem(Object value) {
        return null;
    }

    /**
     * 反序列化测试元件的属性，同时将使用当前已过期的配置项转化为最新配置
     * <p>
     * 如果不需要反序列化，则返回 null，调用方应当自行对 null 进行处理
     *
     * @param clazz 反序列化目标类
     * @param value TestElement JSON 表示
     * @return 标准化数据
     */
    default <T extends TestElement<?>> Map<String, Object> deserializeTestElement(Class<T> clazz, Map<String, Object> value) {
        return null;
    }

    /**
     * 反序列化前置处理器的属性，同时将使用当前已过期的配置项转化为最新配置
     * <p>
     * 如果不需要反序列化，则返回 null，调用方应当自行对 null 进行处理
     *
     * @param clazz 反序列化目标类
     * @param value 数据
     * @return 标准化数据
     */
    default <T extends Preprocessor> Map<String, Object> deserializePreprocessor(Class<T> clazz, Object value) {
        return null;
    }

    /**
     * 反序列化后置处理器的属性，同时将使用当前已过期的配置项转化为最新配置
     * <p>
     * 如果不需要反序列化，则返回 null，调用方应当自行对 null 进行处理
     *
     * @param clazz 反序列化目标类
     * @param value 数据
     * @return 标准化数据
     */
    default <T extends Postprocessor> Map<String, Object> deserializePostprocessor(Class<T> clazz, Object value) {
        return null;
    }
}
