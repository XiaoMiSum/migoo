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

package io.github.xiaomisum.ryze.support;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import io.github.xiaomisum.ryze.support.yaml.IncludeConstructor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * @author xiaomi
 */
public class TestDataLoader {

    /**
     * 将文件内容反序列化为java对象
     *
     * @param clazz 类型
     * @param <T>   java 类型
     * @return java对象
     */
    public static <T> T toJavaObject(String path, Class<T> clazz) {
        var result = readFile(path);
        if (result instanceof String string) {
            return JSON.parseObject(string, clazz);
        }
        return JSON.parseObject(JSON.toJSONString(result), clazz);
    }

    /**
     * 将文件内容反序列化为java对象，支持泛型类型
     *
     * @param path 文件路径
     * @param type 类型
     * @param <T>  java 类型
     * @return java对象
     */
    public static <T> T toJavaObject(String path, Type type) {
        var isList = false;
        try {
            isList = type instanceof ParameterizedType parameterizedType ?
                    List.class.isAssignableFrom(Class.forName(parameterizedType.getRawType().getTypeName()))
                    : List.class.isAssignableFrom(Class.forName(type.getTypeName()));
        } catch (Exception e) {
            // type 为泛型的情况下，Class.forName(type.getTypeName()) 会抛出异常
            // 所以这里判断 type.getTypeName() 是否为 java.util.List 、 java.util.ArrayList
            // 此时无法判断 type 是否为 List的其他实现类
            isList = type.getTypeName().startsWith("java.util.List") || type.getTypeName().startsWith("java.util.ArrayList");
        }

        var result = readFile(path);
        if (result instanceof String string) {
            string = !isList ? string : JSON.isValidArray(string) ? string : "[" + string + "]";
            return JSON.parseObject(string, type);
        }
        result = !isList ? result : result instanceof List<?> ? result : JSONArray.of(result);
        return JSON.parseObject(JSON.toJSONString(result), type);
    }

    public static Object readFile(String path) {
        var file = new File(path);
        try (var stream = getFileInputStream(file)) {
            if (stream != null) {
                if (path.endsWith(".yml") || path.endsWith(".yaml")) {
                    return new Yaml(new IncludeConstructor(file.exists() ? file : null)).load(stream);
                }
                var bytes = new byte[stream.available()];
                stream.read(bytes);
                return new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
        throw new RuntimeException("读取文件失败: " + path);
    }

    private static InputStream getFileInputStream(File file) throws IOException {
        if (file.exists() && file.isFile()) {
            return Files.newInputStream(file.toPath());
        }
        if (file.isDirectory()) {
            return null;
        }
        var path = file.getPath();
        path = path.startsWith("/") ? path.substring(1) : path;
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }
}