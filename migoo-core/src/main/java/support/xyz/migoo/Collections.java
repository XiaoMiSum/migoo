/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package support.xyz.migoo;

import java.util.*;

/**
 * 集合工具类，封装了常用集合操作
 */
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

    public static <T> List<T> newArrayList(T element) {
        List<T> list = new ArrayList<>();
        list.add(element);
        return list;
    }

    @SafeVarargs
    public static <T> List<T> newArrayList(T... elements) {
        if (elements != null) {
            return java.util.Arrays.asList(elements);
        }
        return java.util.Collections.emptyList();
    }

    public static <T> List<T> emptyList() {
        return java.util.Collections.emptyList();
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
