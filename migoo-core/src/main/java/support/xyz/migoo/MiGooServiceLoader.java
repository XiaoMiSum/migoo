package support.xyz.migoo;

import core.xyz.migoo.testelement.Alias;

import java.util.*;

public class MiGooServiceLoader {


    /**
     * 通过 SPI 查找并返回指定接口或抽象类的关键字字典
     *
     * @param clazz 目标类型
     * @param <T>   目标类型
     * @return 指定接口或抽象类的关键字字典
     */
    @SuppressWarnings("unchecked")
    public static <T> Map<String, Class<? extends T>> loadAsMapBySPI(Class<T> clazz) {
        // SPI 查找并注册
        var keyMap = new HashMap<String, Class<? extends T>>();
        var serviceLoader = ServiceLoader.load(clazz);
        serviceLoader.iterator().forEachRemaining(t -> {
            var implClazz = (Class<? extends T>) t.getClass();

            var keys = getKeyWord(implClazz);
            keys.stream().distinct().forEach(key -> keyMap.put(key, implClazz));
        });
        return Collections.unmodifiableMap(keyMap);
    }

    /**
     * 通过 SPI 查找并返回指定接口或抽象类的关键字字典
     *
     * @param clazz 目标类型
     * @return 指定接口或抽象类的关键字字典
     */
    public static Map<String, ?> loadAsInstanceMapBySPI(Class<?> clazz) {
        // SPI 查找并注册
        var keyMap = new HashMap<String, Object>();
        var serviceLoader = ServiceLoader.load(clazz);
        serviceLoader.iterator().forEachRemaining(t -> {
            var keys = getKeyWord(t.getClass());
            keys.stream().distinct().forEach(key -> keyMap.put(key, t));
        });
        return Collections.unmodifiableMap(keyMap);
    }

    // 获取目标类的 @KeyWord 注解并返回它的值
    private static List<String> getKeyWord(Class<?> clazz) {
        var annotation = clazz.getAnnotation(Alias.class);
        var keys = new ArrayList<String>();
        keys.add(clazz.getSimpleName().toLowerCase());
        if (annotation != null) {
            var aliasList = annotation.value();
            for (String key : aliasList) {
                keys.add(key.toLowerCase());
            }
        }
        return keys;
    }
}
