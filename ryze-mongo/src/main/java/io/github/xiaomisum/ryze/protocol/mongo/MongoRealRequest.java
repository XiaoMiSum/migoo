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
