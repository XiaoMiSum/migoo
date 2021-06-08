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

package example.xyz.migoo;

import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.samplers.SampleResult;
import xyz.migoo.MiGoo;
import xyz.migoo.readers.ReaderException;
import xyz.migoo.readers.ReaderFactory;

/**
 * @author mi.xiao
 * @date 2021/2/28 13:37
 */
public class Example {

    public static void main(String[] args) throws ReaderException {
         JSONObject yaml =  (JSONObject) ReaderFactory.getReader("./doc/supply_decision_service.yaml").read();
        // JSONObject yaml = (JSONObject) ReaderFactory.getReader("./example/standardsampler_dubbo.yaml").read();
        // JSONObject yaml = (JSONObject) ReaderFactory.getReader("./example/standardsampler_http.yaml").read();
        // JSONObject yaml = (JSONObject) ReaderFactory.getReader("./example/standardpackage.yaml").read();
        // JSONObject yaml = (JSONObject) ReaderFactory.getReader("./example/standardtestcase.yaml").read();
        // JSONObject yaml = (JSONObject) ReaderFactory.getReader("./example/standardproject.yaml").read();
         SampleResult result = new MiGoo(yaml).run();
    }

}
