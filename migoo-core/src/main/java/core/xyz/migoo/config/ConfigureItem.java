package core.xyz.migoo.config;

import core.xyz.migoo.testelement.Cloneable;
import core.xyz.migoo.testelement.Mergeable;
import core.xyz.migoo.testelement.Validatable;

/**
 * 配置项接口，最基本的配置单元。
 *
 * @param <T>
 * @author xiaomi
 */
//@JSONType(deserializer = ConfigureItemObjectReader.class)
public interface ConfigureItem<T extends ConfigureItem<T>> extends Validatable, Mergeable<T>, Cloneable<T> {

    T merge(T other);

    T copy();
}
