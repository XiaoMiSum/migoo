package io.github.xiaomisum.ryze.protocol.mongo;

import com.alibaba.fastjson2.JSON;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Mongo {

    public static byte[] find(MongoCollection<Document> collection, Map<String, Object> condition) {
        var response = collection.find(toDocument(condition));
        return JSON.toJSONBytes(response);
    }

    public static byte[] insert(MongoCollection<Document> collection, Object data) {
        List<Document> documents = Objects.isNull(data) ? List.of()
                : data instanceof Map<?, ?> map ? List.of(toDocument((Map<String, Object>) map))
                : data instanceof List<?> items ? toDocuments(items) : List.of();
        collection.insertMany(documents);
        return ("Affected rows: " + documents.size()).getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] update(MongoCollection<Document> collection, Map<String, Object> condition, Object data) {
        List<Document> documents = Objects.isNull(data) ? List.of()
                : data instanceof Map<?, ?> map ? List.of(toDocument((Map<String, Object>) map))
                : data instanceof List<?> items ? toDocuments(items) : List.of();
        var updateResult = collection.updateMany(toDocument(condition), documents);
        return ("Affected rows: " + updateResult.getModifiedCount()).getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] delete(MongoCollection<Document> collection, Map<String, Object> condition) {
        var deleteResult = collection.deleteMany(toDocument(condition));
        return ("Affected rows: " + deleteResult.getDeletedCount()).getBytes(StandardCharsets.UTF_8);
    }

    public static List<Document> toDocuments(List<?> sources) {
        var documents = new ArrayList<Document>();
        if (Objects.nonNull(sources)) {
            sources.forEach(item -> documents.add(toDocument((Map<String, Object>) item)));
        }
        return documents;
    }

    public static Document toDocument(Map<String, Object> one) {
        var document = new Document();
        if (Objects.nonNull(one)) {
            one.forEach(document::append);
        }
        return document;
    }
}
