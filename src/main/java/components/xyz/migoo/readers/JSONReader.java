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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import components.xyz.migoo.reports.Report;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @author xiaomi
 */
public class JSONReader extends AbstractReader implements Reader {

    private JSON json;
    private String path;
    private File file;

    public JSONReader(String path) throws ReaderException {
        super.stream(ReaderFactory.JSON_SUFFIX, path);
    }

    public JSONReader(File file) throws ReaderException {
        super.stream(ReaderFactory.JSON_SUFFIX, file);
    }

    @Override
    public JSON read() throws ReaderException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            try {
                json = JSON.parseArray(stringBuilder.toString());
            }catch (Exception e){
                json = JSONObject.parseObject(stringBuilder.toString(), Feature.OrderedField);
            }
        } catch (Exception e) {
            Report.log(e.getMessage(), e);
            throw new ReaderException(e.getMessage() + ". file path : " + this.path);
        }
        return json;
    }

    @Override
    public String get(String key) throws ReaderException {
        if (json == null){
            read();
        }
        if (json instanceof JSONObject){
           return ((JSONObject) json).getString(key);
        }
        return null;
    }
}
