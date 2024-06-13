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
 * @date 2021/6/25 00:06
 */
public class Postman2Sampler implements Convert2Sampler {
    @Override
    public void convert(JSONObject postman, String path) {
        try {
            var entries = postman.getJSONArray("item");
            for (int i = 0; i < entries.size(); i++) {
                var item = entries.getJSONObject(i);
                if (item.containsKey("item")) {
                    this.convert(item, path);
                } else {
                    var request = entries.getJSONObject(i).getJSONObject("request");
                    var sampler = new JSONObject();
                    sampler.put("title", entries.getJSONObject(i).get("name"));
                    sampler.put("testclass", "httpsampler");
                    sampler.put("config", new JSONObject());
                    sampler.getJSONObject("config").put("method", request.get("method"));
                    this.headers(sampler.getJSONObject("config"), request.getJSONArray("header"));
                    this.url(sampler.getJSONObject("config"), request.getJSONObject("url"));
                    this.body(sampler.getJSONObject("config"), request.getJSONObject("body"));
                    this.writer(sampler, path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void body(JSONObject config, JSONObject postData) {
        if (postData != null) {
            var body = new JSONObject();
            if (postData.getJSONArray("urlencoded") != null) {
                for (int i = 0; i < postData.getJSONArray("urlencoded").size(); i++) {
                    var o = postData.getJSONArray("urlencoded").getJSONObject(i);
                    body.put(o.getString("key"), o.get("value"));
                }
            } else {
                body.putAll(JSONObject.parseObject(postData.getString("raw")));
            }
            config.put("body", body);
        }
    }

    private void headers(JSONObject config, JSONArray headers) {
        config.put("headers", new JSONObject());
        for (int i = 0; i < headers.size(); i++) {
            var header = headers.getJSONObject(i);
            config.getJSONObject("headers").put(header.getString("key"), header.get("value"));
        }
    }

    private void url(JSONObject config, JSONObject url) {
        config.put("protocol", url.get("protocol"));
        var sb = new StringBuilder();
        for (int i = 0; i < url.getJSONArray("host").size(); i++) {
            if (sb.length() > 0) {
                sb.append(".");
            }
            sb.append(url.getJSONArray("host").getString(i));
        }
        config.put("host", sb.toString());
        sb.delete(0, sb.length());
        for (int i = 0; i < url.getJSONArray("path").size(); i++) {
            sb.append("/").append(url.getJSONArray("path").getString(i));
        }
        config.put("api", sb.toString());
        if (url.getJSONArray("query") != null) {
            var query = new JSONObject();
            for (int i = 0; i < url.getJSONArray("query").size(); i++) {
                var o = url.getJSONArray("query").getJSONObject(i);
                query.put(o.getString("key"), o.get("value"));
            }
            config.put("query", query);
        }
    }
}
