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

package io.github.xiaomisum.ryze.protocol.mongo.config;


import com.alibaba.fastjson2.annotation.JSONField;
import groovy.lang.Closure;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;
import io.github.xiaomisum.ryze.protocol.mongo.MongoConstantsInterface;
import io.github.xiaomisum.ryze.support.Collections;
import io.github.xiaomisum.ryze.support.Customizer;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * Created at 2025/7/19 20:17
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class MongoConfigItem implements ConfigureItem<MongoConfigItem>, MongoConstantsInterface {

    @JSONField(name = REF)
    protected String ref;
    @JSONField(name = URL, ordinal = 1)
    protected String url;
    @JSONField(name = DATABASE, ordinal = 2)
    protected String database;
    @JSONField(name = COLLECTION, ordinal = 3)
    protected String collection;
    @JSONField(name = ACTION, ordinal = 4)
    protected String action;
    @JSONField(name = DATA, ordinal = 5)
    protected Object data;
    @JSONField(name = CONDITION, ordinal = 6)
    protected Map<String, Object> condition;

    public MongoConfigItem() {
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public MongoConfigItem merge(MongoConfigItem other) {
        if (other == null) {
            return copy();
        }
        var localOther = other.copy();
        var self = copy();
        self.url = StringUtils.isBlank(self.url) ? localOther.url : self.url;
        self.database = StringUtils.isBlank(self.database) ? localOther.database : self.database;
        self.collection = StringUtils.isBlank(self.collection) ? localOther.collection : self.collection;
        self.action = StringUtils.isBlank(self.action) ? localOther.action : self.action;
        self.data = self.data == null ? localOther.data : self.data;
        self.condition = self.condition == null ? localOther.condition : self.condition;
        return self;
    }

    @Override
    public MongoConfigItem evaluate(ContextWrapper context) {
        url = (String) context.evaluate(url);
        database = (String) context.evaluate(database);
        collection = (String) context.evaluate(collection);
        action = (String) context.evaluate(action);
        condition = (Map<String, Object>) context.evaluate(condition);
        data = context.evaluate(data);
        return this;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getAction() {
        return StringUtils.isBlank(action) ? FIND : action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    /**
     * HTTP协议配置项构建类
     */
    public static class Builder extends AbstractTestElement.ConfigureBuilder<Builder, MongoConfigItem> {

        private final MongoConfigItem configure = new MongoConfigItem();

        public Builder() {
        }

        public Builder ref(String ref) {
            configure.ref = ref;
            return self;
        }

        public Builder url(String url) {
            configure.url = url;
            return self;
        }

        public Builder database(String database) {
            configure.database = database;
            return self;
        }

        public Builder collection(String collection) {
            configure.collection = collection;
            return self;
        }

        public Builder action(String action) {
            configure.action = action;
            return self;
        }

        public Builder find() {
            return action(FIND);
        }

        public Builder find(Map<String, Object> condition) {
            return action(FIND).condition(condition);
        }

        public Builder find(Customizer<Map<String, Object>> customizer) {
            return action(FIND).condition(customizer);
        }

        /**
         * 在 Groovy中使用 select 代替 find，因为在 groovy中有个默认方法名称 find，会与 find 方法冲突
         * <p>
         * {@link DefaultGroovyMethods#find(Object, Closure)}
         *
         * @param customizer 函数式表达式
         * @return self
         */
        public Builder select(Customizer<Map<String, Object>> customizer) {
            return action(FIND).condition(customizer);
        }


        public Builder insert(Map<String, Object> data) {
            return action(INSERT).data(data);
        }

        public Builder insertData(Customizer<Map<String, Object>> customizer) {
            return action(INSERT).dataMap(customizer);
        }

        public Builder insert(List<Object> data) {
            return action(INSERT).data(data);
        }

        public Builder insertList(Customizer<List<Object>> customizer) {
            return action(INSERT).dataList(customizer);
        }

        public Builder update(Map<String, Object> data, Map<String, Object> condition) {
            return action(UPDATE).data(data).condition(condition);
        }

        public Builder update(Customizer<Map<String, Object>> data, Customizer<Map<String, Object>> condition) {
            return action(UPDATE).dataMap(data).condition(condition);
        }

        public Builder delete() {
            return action(DELETE);
        }

        public Builder delete(Map<String, Object> condition) {
            return action(DELETE).condition(condition);
        }

        public Builder delete(Customizer<Map<String, Object>> customizer) {
            return action(DELETE).condition(customizer);
        }

        public Builder condition(Map<String, Object> condition) {
            configure.condition = Collections.putAllIfNonNull(configure.condition, condition);
            return self;
        }

        public Builder condition(Customizer<Map<String, Object>> customizer) {
            Map<String, Object> condition = new HashMap<>();
            customizer.customize(condition);
            configure.condition = Collections.putAllIfNonNull(configure.condition, condition);
            return self;
        }

        public Builder data(Map<String, Object> data) {
            if (configure.data != null && !(configure.data instanceof Map)) {
                return self;
            }
            configure.data = Collections.putAllIfNonNull((Map<String, Object>) configure.data, data);
            return self;
        }

        public Builder dataMap(Customizer<Map<String, Object>> customizer) {
            if (configure.data != null && !(configure.data instanceof Map)) {
                return self;
            }
            Map<String, Object> data = new HashMap<>();
            customizer.customize(data);
            configure.data = Collections.putAllIfNonNull((Map<String, Object>) configure.data, data);
            return self;
        }

        public Builder data(List<Object> data) {
            if (configure.data != null && !(configure.data instanceof Map)) {
                return self;
            }
            configure.data = Collections.addAllIfNonNull((List<Object>) configure.data, data);
            return self;
        }

        public Builder dataList(Customizer<List<Object>> customizer) {
            if (configure.data != null && !(configure.data instanceof Map)) {
                return self;
            }
            List<Object> data = Collections.newArrayList();
            customizer.customize(data);
            configure.data = Collections.addAllIfNonNull((List<Object>) configure.data, data);
            return self;
        }


        @Override
        public MongoConfigItem build() {
            return configure;
        }
    }
}
