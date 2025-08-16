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

package io.github.xiaomisum.ryze.support.dataloader;

import io.github.xiaomisum.ryze.support.dataloader.parser.JsonParser;
import io.github.xiaomisum.ryze.support.dataloader.parser.YamlParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class LocalFileLoader extends AbstractDataLoaderHandler {

    @Override
    public Object loadData(String source, Type type, Class<?> targetType) throws Exception {
        var filePath = source;
        if (source.startsWith("file:")) {
            filePath = source.substring(5);
        }
        var file = new File(filePath);
        if (file.exists() && file.isFile()) {
            try (var stream = new FileInputStream(file)) {
                return parseFile(stream, file, type, targetType);
            }
        }
        if (next != null) {
            return next.loadData(source, type, targetType);
        }
        throw new IllegalArgumentException("Unsupported data source: " + source);
    }

    private Object parseFile(InputStream stream, File file, Type type, Class<?> targetType) throws Exception {
        var fileName = file.getName().toLowerCase();
        var content = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
            return YamlParser.parse(content, type, targetType, file);
        } else if (fileName.endsWith(".json")) {
            return JsonParser.parse(content, type, targetType);
        }
        throw new UnsupportedOperationException("Unsupported file format: " + file.getPath());
    }

}