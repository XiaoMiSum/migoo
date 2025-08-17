/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package io.github.xiaomisum.ryze.protocol.mongo;

import com.alibaba.fastjson2.JSON;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import io.github.xiaomisum.ryze.protocol.mongo.config.MongoConfigItem;
import io.github.xiaomisum.ryze.support.Collections;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.xiaomisum.ryze.protocol.mongo.MongoConstantsInterface.*;

/**
 * @author xiaomi
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Mongo {

    public static byte[] execute(MongoClientSettings settings, MongoConfigItem config, DefaultSampleResult result) {

        try (var client = MongoClients.create(settings)) {
            var database = client.getDatabase(config.getDatabase());
            var collection = database.getCollection(config.getCollection());
            var data = config.getData();
            result.sampleStart();
            return switch (config.getAction()) {
                case INSERT -> insert(collection, data);
                case UPDATE -> update(collection, config.getCondition(), data);
                case DELETE -> delete(collection, config.getCondition());
                case null, default -> find(collection, config.getCondition());
            };
        } finally {
            result.sampleEnd();
        }
    }

    private static byte[] find(MongoCollection<Document> collection, Map<String, Object> condition) {
        var result = collection.find(toDocument(condition));
        List<Object> response = new ArrayList<>();
        result.forEach(response::add);
        return JSON.toJSONBytes(response.isEmpty() ? Collections.newHashMap() : response.size() == 1 ? response.getFirst() : response);
    }

    private static byte[] insert(MongoCollection<Document> collection, Object data) {
        List<Document> documents = Objects.isNull(data) ? List.of()
                : data instanceof Map<?, ?> map ? List.of(toDocument((Map<String, Object>) map))
                : data instanceof List<?> items ? toDocuments(items) : List.of();
        collection.insertMany(documents);
        return ("Affected rows: " + documents.size()).getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] update(MongoCollection<Document> collection, Map<String, Object> condition, Object data) {
        List<Document> documents = Objects.isNull(data) ? List.of()
                : data instanceof Map<?, ?> map ? List.of(toDocument((Map<String, Object>) map))
                : data instanceof List<?> items ? toDocuments(items) : List.of();
        var updateResult = collection.updateMany(toDocument(condition), documents);
        return ("Affected rows: " + updateResult.getModifiedCount()).getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] delete(MongoCollection<Document> collection, Map<String, Object> condition) {
        var deleteResult = collection.deleteMany(toDocument(condition));
        return ("Affected rows: " + deleteResult.getDeletedCount()).getBytes(StandardCharsets.UTF_8);
    }

    private static List<Document> toDocuments(List<?> sources) {
        var documents = new ArrayList<Document>();
        if (Objects.nonNull(sources)) {
            sources.forEach(item -> documents.add(toDocument((Map<String, Object>) item)));
        }
        return documents;
    }

    private static Document toDocument(Map<String, Object> one) {
        var document = new Document();
        if (Objects.nonNull(one)) {
            one.forEach(document::append);
        }
        return document;
    }
}
