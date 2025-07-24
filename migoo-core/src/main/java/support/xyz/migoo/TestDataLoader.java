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


package support.xyz.migoo;

import com.alibaba.fastjson2.JSON;
import org.yaml.snakeyaml.Yaml;
import support.xyz.migoo.yaml.IncludeConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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
    public static <T> T toJavaObject(String path, Class<T> clazz) throws IOException {
        var result = readFile(path);
        return JSON.parseObject(JSON.toJSONString(result), clazz);
    }

    public static Object readFile(String path) throws IOException {
        InputStream stream = null;
        try {
            var file = new File(path);
            if (file.exists()) {
                if (file.isDirectory()) {
                    throw new RuntimeException("传入文件路径不能是目录: " + path);
                }
                stream = Files.newInputStream(file.toPath());
            } else {
                path = path.startsWith("/") ? path.substring(1) : path;
                stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
            }
            if (stream != null) {
                if (path.endsWith(".yml") || path.endsWith(".yaml")) {
                    return new Yaml(new IncludeConstructor(file.exists() ? file : null)).load(stream);
                }
                var bytes = new byte[stream.available()];
                stream.read(bytes);
                return new String(bytes, StandardCharsets.UTF_8);
            }
            throw new RuntimeException("读取文件失败: " + path);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }


}
