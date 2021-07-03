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

package xyz.migoo.convert;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author mi.xiao
 * @date 2021/6/24 23:34
 */
public interface Convert2Sampler {

    default void writer(JSONObject sampler, String path) throws IOException {
        if (this instanceof Har2Sampler || this instanceof  Postman2Sampler) {
            sampler.put("validators", new JSONArray());
            JSONObject validator = new JSONObject();
            validator.put("testclass", "httpassertion");
            validator.put("field", "status");
            validator.put("expected", 200);
            validator.put("rule", "==");
            sampler.getJSONArray("validators").add(validator);
        }
        path = StringUtils.isBlank(path) ? System.getProperty("user.dir") : path;
        File file = new File(path + "/" + this.getClass().getSimpleName() + "_"+ System.currentTimeMillis() + ".yaml");
        try (FileWriter writer = new FileWriter(file)) {
            new Yaml().dump(sampler, writer);
        }
        System.out.println("转换完成: " + file.getPath());
    }

    void convert(JSONObject content, String path);
}
