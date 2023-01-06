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


package xyz.migoo.loader;

import com.alibaba.fastjson2.JSON;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static xyz.migoo.Constants.CLASSPATH;

/**
 * @author xiaomi
 */
public class Loader {

    /**
     * 将文件内容反序列化为java对象
     *
     * @param clazz 类型
     * @param <T>   java 类型
     * @return java对象
     */
    public static <T> T toJavaObject(String path, Class<T> clazz) {
        boolean isClasspath = path.startsWith(CLASSPATH);
        path = isClasspath ? path.substring(CLASSPATH.length()) : path;
        return toJavaObject(path, isClasspath, clazz);
    }

    /**
     * 将文件内容反序列化为java对象
     *
     * @param clazz       类型
     * @param isClasspath 是否为 class path
     * @param <T>         java 类型
     * @return java对象
     */
    public static <T> T toJavaObject(String path, boolean isClasspath, Class<T> clazz) {
        Object result = toJSON(path, isClasspath);
        return JSON.parseObject(result.toString(), clazz);
    }

    public static Object toJSON(String path) {
        boolean isClasspath = path.startsWith(CLASSPATH);
        path = isClasspath ? path.substring(CLASSPATH.length()) : path;
        return toJSON(path, isClasspath);
    }

    public static Object toJSON(String path, boolean isClasspath) {
        String content = isClasspath ? new ResourceReader(path).read() : new FileReader(path).read();
        return JSON.toJSON(new Yaml().load(content));
    }

    private static class FileReader {

        private final String path;

        private FileReader(String path) {
            this.path = path;
        }

        protected String read() {
            try (FileInputStream fis = new FileInputStream(path); InputStream is = new BufferedInputStream(fis)) {
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class ResourceReader {

        private final String path;

        private ResourceReader(String path) {
            this.path = path;
        }

        protected String read() {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream is = classLoader.getResourceAsStream(path);
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}