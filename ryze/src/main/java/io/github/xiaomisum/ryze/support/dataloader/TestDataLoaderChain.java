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

import io.github.xiaomisum.ryze.support.RyzeServiceLoader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TestDataLoaderChain {

    private static final TestDataLoaderChain chain = new TestDataLoaderChain();
    private final DataLoaderHandler firstHandler;

    public TestDataLoaderChain() {
        // 构建处理链：本地文件 → Classpath资源 → Other DataLoader
        var firstHandler = new LocalFileLoader();
        var secondHandler = new ClasspathLoader();
        // 设置链的顺序
        firstHandler.setNextHandler(secondHandler);
        List<DataLoaderHandler> loaders = RyzeServiceLoader.loadAsInstanceListBySPI(DataLoaderHandler.class);
        if (!loaders.isEmpty()) {
            for (int i = 0; i < loaders.size() - 1; i++) {
                loaders.get(i).setNextHandler(loaders.get(i + 1));
            }
            secondHandler.setNextHandler(loaders.getFirst());
        }
        this.firstHandler = firstHandler;
    }

    public static <T> T loadTestData(String source, Type type) {
        return chain.loadData(source, type);
    }

    Class<?> getTypeClass(Type type) {
        try {
            if (type instanceof ParameterizedType parameterizedType) {
                return Class.forName(parameterizedType.getRawType().getTypeName());
            }
            return Class.forName(type.getTypeName());
        } catch (Exception e) {
            throw new RuntimeException("Class Not Found", e);
        }
    }

    public <T> T loadData(String source, Type type) {
        try {
            var targetType = getTypeClass(type);
            var result = firstHandler.loadData(source, type, targetType);
            return (T) targetType.cast(result);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}