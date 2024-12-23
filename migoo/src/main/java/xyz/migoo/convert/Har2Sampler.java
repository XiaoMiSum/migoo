/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package xyz.migoo.convert;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

/**
 * @author mi.xiao
 * @date 2021/6/24 23:35
 */
public class Har2Sampler implements Convert2Sampler {

    @Override
    public void convert(JSONObject har, String path) {
        try {
            var entries = har.getJSONObject("log").getJSONArray("entries");
            for (var i = 0; i < entries.size(); i++) {
                if (!"xhr".equals(entries.getJSONObject(i).getString("_resourceType"))) {
                    continue;
                }
                var request = entries.getJSONObject(i).getJSONObject("request");
                var sampler = new JSONObject();
                sampler.put("title", request.get("url") + " " + request.get("method") + " " + request.get("httpVersion"));
                sampler.put("testclass", "httpsampler");
                this.headers(sampler, request);
                this.query(sampler, request.getJSONArray("queryString"));
                this.body(sampler, request.getJSONObject("postData"));
                this.writer(sampler, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void headers(JSONObject sampler, JSONObject request) {
        sampler.put("config", new JSONObject());
        sampler.getJSONObject("config").put("method", request.get("method"));
        sampler.getJSONObject("config").put("base_path", request.get("url"));
        sampler.getJSONObject("config").put("headers", new JSONObject());
        var headers = request.getJSONArray("headers");
        for (var i = 0; i < headers.size(); i++) {
            var header = headers.getJSONObject(i);
            if (!"content-Length".equalsIgnoreCase(header.getString("name"))) {
                sampler.getJSONObject("config").getJSONObject("headers").put(header.getString("name"), header.get("value"));
            }
        }
    }

    private void query(JSONObject sampler, JSONArray queryString) {
        if (queryString != null && !queryString.isEmpty()) {
            var query = new JSONObject();
            for (var i = 0; i < queryString.size(); i++) {
                var obj = queryString.getJSONObject(i);
                query.put(obj.getString("name"), obj.get("value"));
            }
            sampler.getJSONObject("config").put("query", query);
        }
    }

    private void body(JSONObject sampler, JSONObject postData) {
        if (postData != null) {
            var mimeType = postData.getString("mimeType");
            var text = postData.getString("text");
            if (text.startsWith("{") && text.endsWith("}")) {
                var body = new JSONObject();
                sampler.put(mimeType.contains("application/json") ? "body" : "data", body);
                body.putAll(JSONObject.parseObject(text));
            } else if (text.startsWith("[") && text.endsWith("]")) {
                sampler.put("body", JSONArray.parseArray(text));
            }
        }
    }
}
