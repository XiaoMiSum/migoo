package core.xyz.migoo.config;

import support.xyz.migoo.*;
import support.xyz.migoo.Cloneable;

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
