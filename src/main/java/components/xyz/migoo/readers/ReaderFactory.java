/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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


package components.xyz.migoo.readers;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 */
public class ReaderFactory {

    final static String JSON_SUFFIX = ".json";
    final static String YAML_SUFFIX = ".yml";
    final static String PROS_SUFFIX = ".properties";
    static final String XLS_SUFFIX = ".xls";
    static final String XLSX_SUFFIX = ".xlsx";
    private static final List<String> LIST = new ArrayList<>();

    public static Reader getReader(File path) throws ReaderException {
        switch (suffix(path.getName())){
            case JSON_SUFFIX:
                return new JSONReader(path);
            case YAML_SUFFIX:
                return new YamlReader(path);
            case PROS_SUFFIX:
                return new PropertiesReader(path);
            case XLS_SUFFIX:
            case XLSX_SUFFIX:
                return new ExcelReader(path);
            default:
                throw new ReaderException("file reader error");
        }
    }

    private static String suffix(String file) throws ReaderException {
        String suffix = file.substring(file.lastIndexOf("."));
        if (LIST.contains(suffix)){
            return suffix;
        }
        throw new ReaderException("file reader error");
    }

    static{
        Field[] fields = ReaderFactory.class.getDeclaredFields();
        for (Field field:fields){
            try {
                if ("LIST".equals(field.getName())){
                    continue;
                }
                LIST.add(field.get(ReaderFactory.class).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
