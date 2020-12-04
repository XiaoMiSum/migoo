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
import org.yaml.snakeyaml.Yaml;

import java.io.File;

/**
 * @author xiaomi
 * @date 2018/09/28 14:25:22
 */
public class YamlReader extends AbstractReader implements Reader{

    private JSON json;

    public YamlReader(String path) throws ReaderException {
        stream(path);
    }

    public YamlReader(File file) throws ReaderException {
        stream(file);
    }

    @Override
    public JSON read(){
        try {
            Yaml yaml = new Yaml();
            Object object = yaml.load(inputStream);
            json = (JSON) JSON.toJSON(object);
            return json;
        } finally {
            super.close();
        }
    }

    @Override
    public String get(String key) {
        if (json == null){
            read();
        }
        if (json instanceof JSONObject){
            return ((JSONObject) json).getString(key);
        }
        return null;
    }

    @Override
    public String toString(){
        if (json == null){
            read();
        }
        return json.toString();
    }
}
