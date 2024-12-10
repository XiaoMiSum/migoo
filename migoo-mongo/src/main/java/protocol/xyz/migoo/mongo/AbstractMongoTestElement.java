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

package protocol.xyz.migoo.mongo;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import org.bson.Document;
import protocol.xyz.migoo.mongo.config.MongoSourceElement;
import protocol.xyz.migoo.mongo.uitl.MongoConstantsInterface;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author mi.xiao
 * @date 2024/12/06 20:35
 */
public abstract class AbstractMongoTestElement extends AbstractTestElement implements MongoConstantsInterface {

    private MongoClientSettings settings;

    public void testStarted() {
        MongoSourceElement datasource = (MongoSourceElement) getVariables().get(getPropertyAsString(DATASOURCE));
        if (Objects.nonNull(datasource)) {
            setProperty(URL, datasource.get(URL));
            setProperty(DATABASE, datasource.get(DATABASE));
            setProperty(COLLECTION, datasource.get(COLLECTION));
        }
        settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(getPropertyAsString(URL)))
                .retryWrites(true)
                .build();
    }

    protected SampleResult execute(SampleResult result) {
        result.setTestClass(this.getClass());
        result.setUrl(getPropertyAsString(URL));
        setRequestData(result);
        result.sampleStart();
        try (var client = MongoClients.create(settings)) {
            var database = client.getDatabase(getPropertyAsString(DATABASE));
            var collection = database.getCollection(getPropertyAsString(COLLECTION));
            var response = switch (getPropertyAsString(ACTION)) {
                case INSERT -> insert(collection);
                case UPDATE -> update(collection);
                case DELETE -> delete(collection);
                case null, default -> find(collection);
            };
            result.setResponseData(response);
        } finally {
            result.sampleEnd();
        }
        return result;
    }

    private byte[] find(MongoCollection<Document> collection) {
        var document = getOne(getPropertyAsJSONObject(CONDITION));
        var response = collection.find(document);
        JSONArray responses = new JSONArray();
        response.forEach((Block<? super Document>) responses::add);
        return responses.toJSONBBytes();
    }

    private byte[] insert(MongoCollection<Document> collection) {
        var data = get(DATA);
        List<Document> documents = Objects.isNull(data) ? List.of()
                : data instanceof Map<?, ?> d ? List.of(getOne(new JSONObject(d)))
                : data instanceof List<?> items ? getList(new JSONArray(items)) : List.of();
        collection.insertMany(documents);
        return ("Affected rows: " + documents.size()).getBytes(StandardCharsets.UTF_8);
    }

    private byte[] update(MongoCollection<Document> collection) {
        var data = get(DATA);
        var filter = getOne(getPropertyAsJSONObject(CONDITION));
        List<Document> documents = Objects.isNull(data) ? List.of()
                : data instanceof Map<?, ?> d ? List.of(getOne(new JSONObject(d)))
                : data instanceof List<?> items ? getList(new JSONArray(items)) : List.of();
        var updateResult = collection.updateMany(filter, documents);
        return ("Affected rows: " + updateResult.getModifiedCount()).getBytes(StandardCharsets.UTF_8);
    }

    private byte[] delete(MongoCollection<Document> collection) {
        var filter = getOne(getPropertyAsJSONObject(CONDITION));
        var deleteResult = collection.deleteMany(filter);
        return ("Affected rows: " + deleteResult.getDeletedCount()).getBytes(StandardCharsets.UTF_8);
    }

    public void setRequestData(SampleResult result) {
        var data = new JSONObject();
        data.put(DATABASE, get(DATABASE));
        data.put(COLLECTION, get(COLLECTION));
        data.put(ACTION, get(ACTION));
        data.put(DATA, get(DATA));
        data.put(CONDITION, get(CONDITION));
        result.setSamplerData(data.toString());
    }

    private List<Document> getList(JSONArray sources) {
        var documents = new ArrayList<Document>();
        if (Objects.nonNull(sources)) {
            sources.forEach(item -> documents.add(getOne(new JSONObject((Map<?, ?>) item))));
        }
        return documents;
    }

    private Document getOne(JSONObject one) {
        var document = new Document();
        if (Objects.nonNull(one)) {
            one.forEach(document::append);
        }
        return document;
    }
}
