/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package io.github.xiaomisum.ryze.core.config;

import com.alibaba.fastjson2.JSON;
import io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface;
import io.github.xiaomisum.ryze.support.Cloneable;
import io.github.xiaomisum.ryze.support.Mergeable;
import io.github.xiaomisum.ryze.support.Validatable;
import io.github.xiaomisum.ryze.support.ValidateResult;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

/**
 * 配置组
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface ConfigureGroup extends Validatable, Map<String, ConfigureItem>, Mergeable<ConfigureGroup>, Cloneable<ConfigureGroup>, TestElementConstantsInterface {

    /**
     * 获取指定配置项
     *
     * <p>{@link Map#get} 语法糖，方法内部会自动强制类型转换。
     * HttpConfig config = configGroup.get("config");
     *
     * @param key 配置项 Key，如 http
     * @param <T> 配置项对象类型，如 HttpConfig
     * @return 配置项对象
     */
    <T extends ConfigureItem<T>> T get(String key);

    /**
     * 合并配置组，参数配置组的配置项覆盖当前对象配置组的配置项
     *
     * @param other 配置组
     * @return 返回合并后的新对象
     */
    default ConfigureGroup merge(ConfigureGroup other) {
        var data = this.copy();
        if (Objects.isNull(other)) {
            return data;
        }
        var keys = new HashSet<>(other.keySet());
        // 过滤当前配置组中已存在的key（重复配置项以当前为准）
        keys.stream().filter(key -> !this.containsKey(key) && other.get(key) != null)
                .forEach(key -> data.put(key, other.get(key).copy()));
        return data;
    }

    default <T extends ConfigureItem<T>> T get(String key, Class<T> clazz) {
        var v = get(key);
        if (Objects.isNull(v)) {
            return null;
        }
        if (clazz.isAssignableFrom(v.getClass())) {
            return (T) v;
        }
        T t = JSON.parseObject(JSON.toJSONBytes(v), clazz);
        put(key, t);
        return t;
    }

    @Override
    default ValidateResult validate() {
        ValidateResult result = new ValidateResult();
        this.values().forEach(result::append);
        return result;
    }

    default RyzeVariables getVariables() {
        return get(VARIABLES, RyzeVariables.class);
    }
}
