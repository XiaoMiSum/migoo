package support.xyz.migoo;

import java.util.*;

public class Collections {

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

    public static <K, V> Map<K, V> unmodifiableMap(Map<? extends K, ? extends V> m) {
        return java.util.Collections.unmodifiableMap(m);
    }

    public static <T> List<T> unmodifiableList(List<? extends T> list) {
        return java.util.Collections.unmodifiableList(list);
    }

}
