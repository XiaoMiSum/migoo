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

package io.github.xiaomisum.ryze.protocol.mongo;

import com.alibaba.fastjson2.JSON;
import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import io.github.xiaomisum.ryze.protocol.mongo.config.MongoConfigItem;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

public class MongoRealRequest extends SampleResult.Real {


    private String url;
    private String database;
    private String collection;
    private String action;
    private Map<String, Object> condition;

    public MongoRealRequest(byte[] bytes) {
        super(bytes);
    }

    public static MongoRealRequest build(MongoConfigItem configure) {
        var result = new MongoRealRequest(Objects.isNull(configure.getData()) ? new byte[0] :
                JSON.toJSONBytes(configure.getData()));
        result.url = configure.getUrl();
        result.database = configure.getDatabase();
        result.collection = configure.getCollection();
        result.action = configure.getAction();
        result.condition = configure.getCondition();
        return result;
    }

    @Override
    public String format() {

        var buf = new StringBuilder();
        buf.append(url).append("\n")
                .append("database: ").append(database).append("\n")
                .append("collection: ").append(collection).append("\n")
                .append("action: ").append(action).append("\n");
        if (Objects.nonNull(condition)) {
            buf.append("Condition as JSON: ").append(JSON.toJSONString(condition)).append("\n");
        }
        if (StringUtils.isNotBlank(bytesAsString())) {
            buf.append("\n").append("Data: ").append(bytesAsString());
        }
        return buf.toString();
    }
}
