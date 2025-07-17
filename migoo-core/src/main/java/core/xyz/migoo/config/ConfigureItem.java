package core.xyz.migoo.config;

import core.xyz.migoo.Copyable;
import core.xyz.migoo.Mergeable;
import core.xyz.migoo.Validatable;

/**
 * 配置项接口，最基本的配置单元。
 *
 * @param <T>
 * @author xiaomi
 */
public interface ConfigureItem<T extends ConfigureItem<T>> extends Validatable, Mergeable<T>, Copyable<T> {

    T merge(T other);

    T copy();
}
