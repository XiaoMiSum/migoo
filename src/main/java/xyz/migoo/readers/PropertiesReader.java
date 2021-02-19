/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2021. Lorem XiaoMiSum (mi_xiao@qq.com)
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

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author xiaomi
 */
public class PropertiesReader extends AbstractReader implements Reader {

    private JSONObject json;

    public PropertiesReader(String path) throws ReaderException {
        super.stream(path);
    }

    public PropertiesReader(File file) throws ReaderException {
        super.stream(file);
    }

    @Override
    public JSONObject read() throws ReaderException {
        try {
            Properties props = new Properties();
            props.load(inputStream);
            json = (JSONObject)JSONObject.toJSON(props);
            return json;
        } catch (IOException e) {
            throw new ReaderException("file read exception: " + e.getMessage());
        }  finally {
            super.close();
        }
    }

    @Override
    public String get(String key) throws ReaderException {
        if (json == null){
            read();
        }
        return json.getString(key);
    }
}
