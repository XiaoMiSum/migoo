package io.github.xiaomisum.ryze.core.config;

import io.github.xiaomisum.ryze.support.*;
import io.github.xiaomisum.ryze.support.Cloneable;

/**
 * 配置项接口，最基本的配置单元。
 *
 * @param <T>
 * @author xiaomi
 */
public interface ConfigureItem<T extends ConfigureItem<T>> extends Validatable, Mergeable<T>, Cloneable<T>, Computable<T> {

    T merge(T other);

    @SuppressWarnings({"unchecked"})
    default T copy() {
        return (T) KryoUtil.copy(this);
    }
}
