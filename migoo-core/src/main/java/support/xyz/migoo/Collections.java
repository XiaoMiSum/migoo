package support.xyz.migoo;

import java.util.*;

public class Collections {

    public static <K, V> Map<K, V> putAllIfNonNull(Map<K, V> target, Map<K, V> data) {
        if (Objects.isNull(target)) {
            return data;
        }
        if (Objects.isNull(data)) {
            return target;
        }
        target = new HashMap<>(target);     // 防止 target 为不可变 List
        target.putAll(data);
        return data;
    }

    public static <T> List<T> addAllIfNonNull(List<T> target, List<T> data) {
        if (Objects.isNull(target)) {
            return data;
        }
        if (Objects.isNull(data)) {
            return target;
        }
        target = new ArrayList<>(target);     // 防止 target 为不可变 List
        target.addAll(data);
        return data;
    }

    public static <T> Iterator<T> emptyIterator() {
        return java.util.Collections.emptyIterator();
    }

    public static <K, V> Map<K, V> unmodifiableMap(Map<K, V> m) {
        return java.util.Collections.unmodifiableMap(m);
    }

    public static <T> List<T> unmodifiableList(List<T> list) {
        return java.util.Collections.unmodifiableList(list);
    }

    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        java.util.Collections.sort(list);
    }

}
