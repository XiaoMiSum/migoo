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

package core.xyz.migoo.testelement;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.SessionRunner;
import core.xyz.migoo.builder.IBuilder;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.report.Result;
import support.xyz.migoo.KryoUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 测试组件抽象类，提供公共属性和方法
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractTestElement<SELF extends AbstractTestElement<SELF, CONFIG, R>, CONFIG extends ConfigureItem<CONFIG>, R extends Result>
        implements TestElement<R>, TestElementConstantsInterface {

    @JSONField(name = ID)
    protected String id;

    @JSONField(name = TITLE, ordinal = 1)
    protected String title;

    @JSONField(name = DISABLED, ordinal = 3)
    protected boolean disabled = false;

    @JSONField(name = CONFIG, ordinal = 4)
    protected CONFIG config;

    @JSONField(name = FILTERS, ordinal = 8)
    protected List<TestFilter> filters;

    // 元数据，可以挂载一些辅助数据
    @JSONField(name = METADATA, ordinal = 2)
    protected Map<String, Object> metadata = new HashMap<>();

    protected Map<String, Object> rawData;
    protected SELF runtime;
    protected boolean initialized = false;

    public AbstractTestElement(Builder<SELF, ?, CONFIG, ?, R> builder) {
        id = builder.id;
        title = builder.title;
        disabled = builder.disabled;
        config = builder.config;
        filters = builder.filters;
        metadata.putAll(builder.metadata);
    }

    public AbstractTestElement() {
    }

    protected void initialized(SessionRunner session) {
        initialized = true;
        runtime = copy();
        // 获取所有符合条件的 TestFilter
        handleFilters(session.getContextWrapper());
    }

    protected void handleFilters(ContextWrapper contextWrapper) {

    }

    protected abstract R getTestResult();


    protected SELF newInstance() {
        try {
            //noinspection unchecked
            return (SELF) this.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(String.format("实例化 %s 失败", this.getClass().getName()), e);
        }
    }

    @Override
    public SELF copy() {
        SELF self = newInstance();
        self.title = KryoUtil.copy(title);
        self.id = KryoUtil.copy(id);
        self.disabled = KryoUtil.copy(disabled);
        self.metadata = KryoUtil.copy(metadata);
        self.config = KryoUtil.copy(config);
        self.filters = KryoUtil.copy(filters);
        return self;
    }

    //  getter and setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public CONFIG getConfig() {
        return config;
    }

    public void setConfig(CONFIG config) {
        this.config = config;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getRawData() {
        return rawData;
    }

    public void setRawData(Map<String, Object> rawData) {
        this.rawData = rawData;
    }


    /**
     * 测试元件基础构建实现类
     *
     * @param <ELE>测试元件类型
     * @param <SELF>              构建类自身
     * @param <CONFIGURE_BUILDER> 配置构建类
     */
    public static abstract class Builder<ELE extends AbstractTestElement<ELE, CONFIG, R>,
            SELF extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            R extends Result>
            implements TestElementBuilder<ELE> {

        protected String id;

        protected String title;

        protected boolean disabled;

        protected CONFIG config;

        protected List<TestFilter> filters;

        protected Map<String, Object> metadata = new HashMap<>();

        protected SELF self;

        protected Builder() {
            self = (SELF) this;
        }

        public SELF id(String id) {
            this.id = id;
            return self;
        }

        public SELF title(String title) {
            this.title = title;
            return self;
        }

        public SELF disabled(boolean disabled) {
            this.disabled = disabled;
            return self;
        }

        public SELF config(Supplier<CONFIGURE_BUILDER> supplier) {
            this.config = supplier.get().build();
            return self;
        }

        public SELF config(CONFIGURE_BUILDER builder) {
            this.config = builder.build();
            return self;
        }

        public SELF config(CONFIG config) {
            this.config = config;
            return self;
        }

        public SELF metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return self;
        }

        public SELF metadata(String name, Object value) {
            this.metadata.put(name, value);
            return self;
        }

        public SELF filters(List<TestFilter> filters) {
            this.filters = filters;
            return self;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public CONFIG getConfig() {
            return config;
        }

        public Map<String, Object> getMetadata() {
            return metadata;
        }

        public List<TestFilter> getFilters() {
            return filters;
        }

    }

    /**
     * 测试配置基础构建实现类
     *
     * @param <SELF> 构建类自身
     */
    public static abstract class ConfigureBuilder<SELF extends ConfigureBuilder<SELF, CONFIG>, CONFIG extends ConfigureItem<CONFIG>>
            implements IBuilder<CONFIG> {
        protected SELF self;

        protected ConfigureBuilder() {
            self = (SELF) this;
        }
    }
}
