package core.xyz.migoo.config;

import support.xyz.migoo.Cloneable;
import support.xyz.migoo.KryoUtil;
import support.xyz.migoo.Mergeable;
import support.xyz.migoo.Validatable;

/**
 * 配置项接口，最基本的配置单元。
 *
 * @param <T>
 * @author xiaomi
 */
public interface ConfigureItem<T extends ConfigureItem<T>> extends Validatable, Mergeable<T>, Cloneable<T> {

    T merge(T other);

    @SuppressWarnings({"unchecked"})
    default T copy() {
        return (T) KryoUtil.copy(this);
    }
}
