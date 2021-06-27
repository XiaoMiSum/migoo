/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
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


package xyz.migoo.readers;

import java.io.File;

/**
 * @author xiaomi
 */
public class ReaderFactory {

    public static Reader getReader(File file) throws ReaderException {
        FileType type = suffix(file.getName());
        switch (type) {
            case HAR:
            case JSON:
                return new JSONReader(file);
            case YML:
            case YAML:
                return new YamlReader(file);
            case PROPERTIES:
                return new PropertiesReader(file);
            default:
                throw new ReaderException("file reader error");
        }
    }

    public static Reader getReader(String file) throws ReaderException {
        FileType type = suffix(file);
        switch (type) {
            case HAR:
            case JSON:
                return new JSONReader(file);
            case YML:
            case YAML:
                return new YamlReader(file);
            case PROPERTIES:
                return new PropertiesReader(file);
            default:
                throw new ReaderException("file reader error");
        }
    }

    private static FileType suffix(String file) throws ReaderException {
        String suffix = file.substring(file.lastIndexOf(".") + 1);
        try {
            return FileType.valueOf(suffix.toUpperCase());
        } catch (Exception e) {
            throw new ReaderException("unsupported file type: " + file);
        }
    }
}
