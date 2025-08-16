/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *
 */

package io.github.xiaomisum.ryze.support.dataloader.parser;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import io.github.xiaomisum.ryze.support.yaml.IncludeConstructor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class YamlParser {

    public static Object parse(String yaml, Type type, Class<?> targetType, File file) {
        return parseYaml(yaml, type, targetType, file);
    }

    private static Object parseYaml(String yaml, Type type, Class<?> targetType, File file) {
        var result = new Yaml(new IncludeConstructor(Objects.isNull(file) || file.isDirectory() || !file.exists() ? null : file)).load(yaml);
        if (List.class.isAssignableFrom(targetType)) {
            return result instanceof List<?> ? JSON.parseArray(JSON.toJSONString(result), type) :
                    JSON.parseObject(JSONArray.of(result).toJSONString(), type);
        }
        return targetType.equals(Object.class) ? result : JSON.parseObject(JSON.toJSONString(result), type);
    }
}