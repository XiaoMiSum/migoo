/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.framework.entity.MiGooCase;
import xyz.migoo.loader.reader.AbstractReader;
import xyz.migoo.loader.reader.ReaderFactory;
import xyz.migoo.report.MiGooLog;
import xyz.migoo.utils.StringUtil;

import java.io.File;

/**
 * @author xiaomi
 */
public class CaseLoader {

    private CaseLoader() {
    }

    public static MiGooCase loadMiGooCase(String path) throws ReaderException {
        MiGooLog.log("load case sets begin: {}", path);
        AbstractReader reader = (AbstractReader)ReaderFactory.getReader(new File(path));
        MiGooCase cases = JSONObject.parseObject(reader.toString(), MiGooCase.class, Feature.OrderedField);
        MiGooLog.log("load case sets end");
        return cases;
    }

    public static JSONObject loadEnv(String env) throws ReaderException {
        MiGooLog.log("load env begin: {}", env);
        JSONObject json = null;
        if (!StringUtil.isEmpty(env)) {
            try {
                json = JSONObject.parseObject(env);
            } catch (Exception e) {
                File file = new File(env);
                if (!file.isDirectory()) {
                    json = (JSONObject) ReaderFactory.getReader(file).read();
                }
            }
        }
        MiGooLog.log("load env end");
        return json;
    }
}
