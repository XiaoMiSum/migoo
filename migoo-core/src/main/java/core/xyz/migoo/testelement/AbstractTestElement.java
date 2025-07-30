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
import component.xyz.migoo.assertion.JSONAssertion;
import component.xyz.migoo.assertion.ResultAssertion;
import component.xyz.migoo.extractor.JSONExtractor;
import component.xyz.migoo.extractor.RegexExtractor;
import component.xyz.migoo.extractor.ResultExtractor;
import core.xyz.migoo.SessionRunner;
import core.xyz.migoo.assertion.AbstractAssertion;
import core.xyz.migoo.assertion.Assertion;
import core.xyz.migoo.builder.ExtensibleExtractorsBuilder;
import core.xyz.migoo.builder.IBuilder;
import core.xyz.migoo.builder.LazyBuilder;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.extractor.AbstractExtractor;
import core.xyz.migoo.extractor.Extractor;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.configure.AbstractConfigureElement;
import core.xyz.migoo.testelement.configure.ConfigureElement;
import core.xyz.migoo.testelement.processor.AbstractProcessor;
import core.xyz.migoo.testelement.processor.Postprocessor;
import core.xyz.migoo.testelement.processor.Preprocessor;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import protocol.xyz.migoo.debug.config.DebugDefaults;
import protocol.xyz.migoo.debug.processer.DebugPostprocessor;
import protocol.xyz.migoo.debug.processer.DebugPreprocessor;
import protocol.xyz.migoo.debug.sampler.DebugSampler;
import protocol.xyz.migoo.http.config.HTTPDefaults;
import protocol.xyz.migoo.http.extractor.HTTPHeaderExtractor;
import protocol.xyz.migoo.http.processor.HTTPPostprocessor;
import protocol.xyz.migoo.http.processor.HTTPPreprocessor;
import protocol.xyz.migoo.http.sampler.HTTPSampler;
import protocol.xyz.migoo.jdbc.config.JDBCDatasource;
import protocol.xyz.migoo.jdbc.processor.JDBCPostprocessor;
import protocol.xyz.migoo.jdbc.processor.JDBCPreprocessor;
import protocol.xyz.migoo.jdbc.sampler.JDBCSampler;
import protocol.xyz.migoo.redis.config.RedisDatasource;
import protocol.xyz.migoo.redis.processor.RedisPostprocessor;
import protocol.xyz.migoo.redis.processor.RedisPreprocessor;
import protocol.xyz.migoo.redis.sampler.RedisSampler;
import support.xyz.migoo.KryoUtil;
import support.xyz.migoo.groovy.Groovy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static support.xyz.migoo.groovy.Groovy.call;

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
     * @param <ELE>               测试元件类型
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

        public Builder() {
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

    /**
     * 配置元件构建
     * <p>
     * 包含 {@link protocol.xyz.migoo.debug.config.DebugDefaults}、
     * {@link protocol.xyz.migoo.http.config.HTTPDefaults}、
     * {@link protocol.xyz.migoo.jdbc.config.JDBCDatasource}、
     * {@link protocol.xyz.migoo.redis.config.RedisDatasource}
     * <p>
     * * 如有协议独有后置处理器，请自行扩展 {@link core.xyz.migoo.builder.ExtensibleConfigureElementsBuilder}
     */
    public static abstract class ConfigureElementsBuilder<SELF extends ConfigureElementsBuilder<SELF>> implements IBuilder<List<ConfigureElement>> {
        protected final List<ConfigureElement> configureElements = new LazyBuilder<>();

        protected SELF self;

        protected ConfigureElementsBuilder() {
            self = (SELF) this;
        }

        /**
         * 自定义前置处理器
         *
         * @param builder 自定义前置处理器构建器
         * @return 当前对象
         */
        public SELF apply(AbstractConfigureElement.Builder builder) {
            configureElements.add((ConfigureElement) builder.build());
            return self;
        }

        public SELF apply(ConfigureElement configureElement) {
            configureElements.add(configureElement);
            return self;
        }

        public <T extends AbstractConfigureElement.Builder> SELF apply(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
            var builder = Groovy.builder(type, closure);
            configureElements.add((ConfigureElement) builder.build());
            return self;
        }

        // -----------------------DebugDefaults----------------------------

        public SELF debug(DebugDefaults defaults) {
            configureElements.add(defaults);
            return self;
        }

        public SELF debug(Supplier<DebugDefaults.Builder> supplier) {
            configureElements.add(supplier.get().build());
            return self;
        }

        public SELF debug(DebugDefaults.Builder builder) {
            configureElements.add(builder.build());
            return self;
        }

        public SELF debug(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = DebugDefaults.Builder.class) Closure<?> closure) {
            var builder = DebugDefaults.builder();
            call(closure, builder);
            configureElements.add(builder.build());
            return self;
        }

        // -----------------------HTTPDefaults----------------------------

        public SELF http(HTTPDefaults defaults) {
            configureElements.add(defaults);
            return self;
        }

        public SELF http(Supplier<HTTPDefaults.Builder> supplier) {
            configureElements.add(supplier.get().build());
            return self;
        }

        public SELF http(HTTPDefaults.Builder builder) {
            configureElements.add(builder.build());
            return self;
        }

        public SELF http(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = HTTPDefaults.Builder.class) Closure<?> closure) {
            var builder = HTTPDefaults.builder();
            call(closure, builder);
            configureElements.add(builder.build());
            return self;
        }

        // -----------------------JDBCDatasource----------------------------

        public SELF jdbc(JDBCDatasource datasource) {
            configureElements.add(datasource);
            return self;
        }

        public SELF jdbc(Supplier<JDBCDatasource.Builder> supplier) {
            configureElements.add(supplier.get().build());
            return self;
        }

        public SELF jdbc(JDBCDatasource.Builder builder) {
            configureElements.add(builder.build());
            return self;
        }

        public SELF jdbc(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = JDBCDatasource.Builder.class) Closure<?> closure) {
            var builder = JDBCDatasource.builder();
            call(closure, builder);
            configureElements.add(builder.build());
            return self;
        }

        // -----------------------RedisDatasource----------------------------

        public SELF redis(RedisDatasource datasource) {
            configureElements.add(datasource);
            return self;
        }

        public SELF redis(Supplier<RedisDatasource.Builder> supplier) {
            configureElements.add(supplier.get().build());
            return self;
        }

        public SELF redis(RedisDatasource.Builder builder) {
            configureElements.add(builder.build());
            return self;
        }

        public SELF redis(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = RedisDatasource.Builder.class) Closure<?> closure) {
            var builder = RedisDatasource.builder();
            call(closure, builder);
            configureElements.add(builder.build());
            return self;
        }


        public List<ConfigureElement> build() {
            return configureElements;
        }
    }

    /**
     * 前置处理器构建
     * <p>
     * 包含 {@link protocol.xyz.migoo.debug.processer.DebugPreprocessor}、
     * {@link protocol.xyz.migoo.http.processor.HTTPPreprocessor}、
     * {@link protocol.xyz.migoo.jdbc.processor.JDBCPreprocessor}、
     * {@link protocol.xyz.migoo.redis.processor.RedisPreprocessor}
     * <p>
     * * 如有协议独有后置处理器，请自行扩展 {@link core.xyz.migoo.builder.ExtensiblePreprocessorsBuilder}
     */
    public static abstract class PreprocessorsBuilder<SELF extends PreprocessorsBuilder<SELF, EXTRACTORS_BUILDER>,
            EXTRACTORS_BUILDER extends ExtensibleExtractorsBuilder>
            implements IBuilder<List<Preprocessor>> {

        protected final List<Preprocessor> preprocessors = new LazyBuilder<>();

        protected SELF self;

        protected PreprocessorsBuilder() {
            self = (SELF) this;
        }

        /**
         * 自定义前置处理器
         *
         * @param builder 自定义前置处理器构建器
         * @return 当前对象
         */
        public SELF apply(AbstractProcessor.PreprocessorBuilder builder) {
            preprocessors.add((Preprocessor) builder.build());
            return self;
        }

        public SELF apply(Preprocessor preprocessor) {
            preprocessors.add(preprocessor);
            return self;
        }

        public <T extends AbstractProcessor.PreprocessorBuilder> SELF apply(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
            var builder = Groovy.builder(type, closure);
            preprocessors.add((Preprocessor) builder.build());
            return self;
        }

        // -----------------------DebugPreprocessor----------------------------

        public SELF debug(DebugPreprocessor preprocessor) {
            preprocessors.add(preprocessor);
            return self;
        }

        public SELF debug(Supplier<DebugPreprocessor.Builder> supplier) {
            preprocessors.add(supplier.get().build());
            return self;
        }

        public SELF debug(DebugPreprocessor.Builder builder) {
            preprocessors.add(builder.build());
            return self;
        }

        public SELF debug(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = DebugPreprocessor.Builder.class) Closure<?> closure) {
            var builder = DebugPreprocessor.builder();
            call(closure, builder);
            preprocessors.add(builder.build());
            return self;
        }

        // -----------------------HTTPPreprocessor----------------------------

        public SELF http(HTTPPreprocessor preprocessor) {
            preprocessors.add(preprocessor);
            return self;
        }

        public SELF http(Supplier<HTTPPreprocessor.Builder> supplier) {
            preprocessors.add(supplier.get().build());
            return self;
        }

        public SELF http(HTTPPreprocessor.Builder builder) {
            preprocessors.add(builder.build());
            return self;
        }

        public SELF http(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = HTTPPreprocessor.Builder.class) Closure<?> closure) {
            var builder = HTTPPreprocessor.builder();
            call(closure, builder);
            preprocessors.add(builder.build());
            return self;
        }

        // -----------------------JDBCPreprocessor----------------------------

        public SELF jdbc(JDBCPreprocessor preprocessor) {
            preprocessors.add(preprocessor);
            return self;
        }

        public SELF jdbc(Supplier<JDBCPreprocessor.Builder> supplier) {
            preprocessors.add(supplier.get().build());
            return self;
        }

        public SELF jdbc(JDBCPreprocessor.Builder builder) {
            preprocessors.add(builder.build());
            return self;
        }

        public SELF jdbc(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = JDBCPreprocessor.Builder.class) Closure<?> closure) {
            var builder = JDBCPreprocessor.builder();
            call(closure, builder);
            preprocessors.add(builder.build());
            return self;
        }

        // -----------------------RedisPreprocessor----------------------------

        public SELF redis(RedisPreprocessor preprocessor) {
            preprocessors.add(preprocessor);
            return self;
        }

        public SELF redis(Supplier<RedisPreprocessor.Builder> supplier) {
            preprocessors.add(supplier.get().build());
            return self;
        }

        public SELF redis(RedisPreprocessor.Builder builder) {
            preprocessors.add(builder.build());
            return self;
        }

        public SELF redis(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = RedisPreprocessor.Builder.class) Closure<?> closure) {
            var builder = RedisPreprocessor.builder();
            call(closure, builder);
            preprocessors.add(builder.build());
            return self;
        }

        public List<Preprocessor> build() {
            return preprocessors;
        }
    }


    /**
     * 后置处理器构建
     * <p>
     * 包含 {@link protocol.xyz.migoo.debug.processer.DebugPostprocessor}、
     * {@link protocol.xyz.migoo.http.processor.HTTPPostprocessor}、
     * {@link protocol.xyz.migoo.jdbc.processor.JDBCPostprocessor}、
     * {@link protocol.xyz.migoo.redis.processor.RedisPostprocessor}
     * <p>
     * * 如有协议独有后置处理器，请自行扩展 {@link core.xyz.migoo.builder.ExtensiblePostprocessorsBuilder}
     */
    public static abstract class PostprocessorsBuilder<SELF extends PostprocessorsBuilder<SELF, EXTRACTORS_BUILDER>,
            EXTRACTORS_BUILDER extends ExtensibleExtractorsBuilder>
            implements IBuilder<List<Postprocessor>> {

        protected final List<Postprocessor> postprocessors = new LazyBuilder<>();

        protected SELF self;

        protected PostprocessorsBuilder() {
            self = (SELF) this;
        }


        /**
         * 自定义后置处理器
         *
         * @param builder 自定义后置处理器构建器
         * @return 当前对象
         */
        public SELF apply(AbstractProcessor.PostprocessorBuilder builder) {
            postprocessors.add((Postprocessor) builder.build());
            return self;
        }

        public SELF apply(Postprocessor postprocessor) {
            postprocessors.add(postprocessor);
            return self;
        }

        public <T extends AbstractProcessor.PostprocessorBuilder> SELF apply(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
            var builder = Groovy.builder(type, closure);
            postprocessors.add((Postprocessor) builder.build());
            return self;
        }

        // -----------------------DebugPostprocessor----------------------------

        public SELF debug(DebugPostprocessor postprocessor) {
            postprocessors.add(postprocessor);
            return self;
        }

        public SELF debug(Supplier<DebugPostprocessor.Builder> supplier) {
            postprocessors.add(supplier.get().build());
            return self;
        }

        public SELF debug(DebugPostprocessor.Builder builder) {
            postprocessors.add(builder.build());
            return self;
        }

        public SELF debug(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = DebugPostprocessor.Builder.class) Closure<?> closure) {
            var builder = DebugPostprocessor.builder();
            call(closure, builder);
            postprocessors.add(builder.build());
            return self;
        }

        // -----------------------HTTPPostprocessor----------------------------

        public SELF http(HTTPPostprocessor postprocessor) {
            postprocessors.add(postprocessor);
            return self;
        }

        public SELF http(Supplier<HTTPPostprocessor.Builder> supplier) {
            postprocessors.add(supplier.get().build());
            return self;
        }

        public SELF http(HTTPPostprocessor.Builder builder) {
            postprocessors.add(builder.build());
            return self;
        }

        public SELF http(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = HTTPPostprocessor.Builder.class) Closure<?> closure) {
            var builder = HTTPPostprocessor.builder();
            call(closure, builder);
            postprocessors.add(builder.build());
            return self;
        }

        // -----------------------JDBCPostprocessor----------------------------

        public SELF jdbc(JDBCPostprocessor postprocessor) {
            postprocessors.add(postprocessor);
            return self;
        }

        public SELF jdbc(Supplier<JDBCPostprocessor.Builder> supplier) {
            postprocessors.add(supplier.get().build());
            return self;
        }

        public SELF jdbc(JDBCPostprocessor.Builder builder) {
            postprocessors.add(builder.build());
            return self;
        }

        public SELF jdbc(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = JDBCPostprocessor.Builder.class) Closure<?> closure) {
            var builder = JDBCPostprocessor.builder();
            call(closure, builder);
            postprocessors.add(builder.build());
            return self;
        }

        // -----------------------RedisPostprocessor----------------------------

        public SELF redis(RedisPostprocessor postprocessor) {
            postprocessors.add(postprocessor);
            return self;
        }

        public SELF redis(Supplier<RedisPostprocessor.Builder> supplier) {
            postprocessors.add(supplier.get().build());
            return self;
        }

        public SELF redis(RedisPostprocessor.Builder builder) {
            postprocessors.add(builder.build());
            return self;
        }

        public SELF redis(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = RedisPostprocessor.Builder.class) Closure<?> closure) {
            var builder = RedisPostprocessor.builder();
            call(closure, builder);
            postprocessors.add(builder.build());
            return self;
        }

        public List<Postprocessor> build() {
            return postprocessors;
        }
    }

    /**
     * 验证器构建器
     * <p>
     * 包含 {@link component.xyz.migoo.assertion.JSONAssertion}、
     * {@link component.xyz.migoo.assertion.ResultAssertion}、
     * {@link protocol.xyz.migoo.http.assertion.HTTPResponseAssertion }
     * <p>
     * 如有协议独有验证器，请自行扩展 {@link core.xyz.migoo.builder.ExtensibleAssertionsBuilder}
     */
    public static abstract class AssertionsBuilder<SELF extends AssertionsBuilder<SELF>> implements IBuilder<List<Assertion>> {

        protected List<Assertion> assertions = new LazyBuilder<>();

        protected SELF self;

        public AssertionsBuilder() {
            self = (SELF) this;
        }

        /**
         * 自定义验证器
         *
         * @param builder 自定义验证器构建器
         * @return 当前对象
         */
        public SELF apply(AbstractAssertion.Builder builder) {
            assertions.add(builder.build());
            return self;
        }

        public SELF apply(Assertion extractor) {
            assertions.add(extractor);
            return self;
        }

        public <T extends AbstractAssertion.Builder> SELF apply(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
            var builder = Groovy.builder(type, closure);
            assertions.add(builder.build());
            return self;
        }

        // -----------------------JSONAssertion----------------------------

        public SELF json(Supplier<JSONAssertion.Builder> supplier) {
            assertions.add(supplier.get().build());
            return self;
        }

        public SELF json(JSONAssertion.Builder builder) {
            assertions.add(builder.build());
            return self;
        }

        public SELF json(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = JSONAssertion.Builder.class) Closure<?> closure) {
            var builder = JSONAssertion.builder();
            call(closure, builder);
            assertions.add(builder.build());
            return self;
        }

        public SELF json(String field, Object expected) {
            assertions.add(JSONAssertion.builder().field(field).expected(expected).build());
            return self;
        }

        public SELF json(String rule, String field, Object expected) {
            assertions.add(JSONAssertion.builder().rule(rule).field(field).expected(expected).build());
            return self;
        }

        // -----------------------ResultAssertion----------------------------

        public SELF result(Supplier<ResultAssertion.Builder> supplier) {
            assertions.add(supplier.get().build());
            return self;
        }

        public SELF result(ResultAssertion.Builder builder) {
            assertions.add(builder.build());
            return self;
        }

        public SELF result(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ResultAssertion.Builder.class) Closure<?> closure) {
            var builder = ResultAssertion.builder();
            call(closure, builder);
            assertions.add(builder.build());
            return self;
        }

        public SELF result(String rule, Object expected) {
            assertions.add(ResultAssertion.builder().rule(rule).expected(expected).build());
            return self;
        }


        public List<Assertion> build() {
            return assertions;
        }
    }

    /**
     * 提取器构建
     * <p>
     * 包含 {@link component.xyz.migoo.extractor.JSONExtractor }、
     * {@link component.xyz.migoo.extractor.RegexExtractor }、
     * {@link component.xyz.migoo.extractor.ResultExtractor }、
     * {@link HTTPHeaderExtractor }
     *
     * <p>
     * 如有协议独有提取器，请自行扩展 {@link core.xyz.migoo.builder.ExtensibleExtractorsBuilder}
     */
    public static abstract class ExtractorsBuilder<SELF extends ExtractorsBuilder<SELF>> implements IBuilder<List<Extractor>> {

        protected List<Extractor> extractors = new LazyBuilder<>();

        protected SELF self;

        public ExtractorsBuilder() {
            self = (SELF) this;
        }

        /**
         * 自定义提取器
         *
         * @param builder 自定义提取器构建器
         * @return 当前对象
         */
        public SELF apply(AbstractExtractor.Builder builder) {
            extractors.add(builder.build());
            return self;
        }

        public SELF apply(Extractor extractor) {
            extractors.add(extractor);
            return self;
        }

        public <T extends AbstractExtractor.Builder> SELF apply(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
            var builder = Groovy.builder(type, closure);
            extractors.add(builder.build());
            return self;
        }

        // -----------------------JSONExtractor----------------------------

        public SELF json(Supplier<JSONExtractor.Builder> supplier) {
            extractors.add(supplier.get().build());
            return self;
        }

        public SELF json(JSONExtractor.Builder builder) {
            extractors.add(builder.build());
            return self;
        }

        public SELF json(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = JSONExtractor.Builder.class) Closure<?> closure) {
            var builder = JSONExtractor.builder();
            call(closure, builder);
            extractors.add(builder.build());
            return self;
        }

        public SELF json(String refName, String field) {
            extractors.add(JSONExtractor.builder().refName(refName).field(field).build());
            return self;
        }

        public SELF json(String refName, String field, Object defaultValue) {
            extractors.add(JSONExtractor.builder().refName(refName).field(field).defaultValue(defaultValue).build());
            return self;
        }

        // -----------------------RegexExtractor----------------------------

        public SELF regex(Supplier<RegexExtractor.Builder> supplier) {
            extractors.add(supplier.get().build());
            return self;
        }

        public SELF regex(RegexExtractor.Builder builder) {
            extractors.add(builder.build());
            return self;
        }

        public SELF regex(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = RegexExtractor.Builder.class) Closure<?> closure) {
            var builder = RegexExtractor.builder();
            call(closure, builder);
            extractors.add(builder.build());
            return self;
        }

        public SELF regex(String refName, String field) {
            extractors.add(RegexExtractor.builder().refName(refName).field(field).build());
            return self;
        }

        public SELF regex(String refName, String field, int matchNum) {
            extractors.add(RegexExtractor.builder().refName(refName).field(field).matchNum(matchNum).build());
            return self;
        }

        public SELF regex(String refName, String field, Object defaultValue) {
            extractors.add(RegexExtractor.builder().refName(refName).field(field).defaultValue(defaultValue).build());
            return self;
        }

        public SELF regex(String refName, String field, int matchNum, Object defaultValue) {
            extractors.add(RegexExtractor.builder().refName(refName).field(field).matchNum(matchNum).defaultValue(defaultValue).build());
            return self;
        }

        // -----------------------ResultExtractor----------------------------

        public SELF result(Supplier<ResultExtractor.Builder> supplier) {
            extractors.add(supplier.get().build());
            return self;
        }

        public SELF result(ResultExtractor.Builder builder) {
            extractors.add(builder.build());
            return self;
        }

        public SELF result(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ResultExtractor.Builder.class) Closure<?> closure) {
            var builder = ResultExtractor.builder();
            call(closure, builder);
            extractors.add(builder.build());
            return self;
        }

        public SELF result(String refName) {
            extractors.add(ResultExtractor.builder().refName(refName).build());
            return self;
        }

        // -----------------------HTTPHeaderExtractor----------------------------

        public SELF httpHeader(Supplier<HTTPHeaderExtractor.Builder> supplier) {
            extractors.add(supplier.get().build());
            return self;
        }

        public SELF httpHeader(HTTPHeaderExtractor.Builder builder) {
            extractors.add(builder.build());
            return self;
        }

        public SELF httpHeader(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = HTTPHeaderExtractor.Builder.class) Closure<?> closure) {
            var builder = HTTPHeaderExtractor.builder();
            call(closure, builder);
            extractors.add(builder.build());
            return self;
        }

        public SELF httpHeader(String refName, String field) {
            extractors.add(ResultExtractor.builder().refName(refName).field(field).build());
            return self;
        }

        public SELF httpHeader(String refName, String field, Object defaultValue) {
            extractors.add(ResultExtractor.builder().refName(refName).field(field).defaultValue(defaultValue).build());
            return self;
        }

        public SELF httpHeader(String refName, String field, int matchNum) {
            extractors.add(ResultExtractor.builder().refName(refName).field(field).matchNum(matchNum).build());
            return self;
        }

        public SELF httpHeader(String refName, String field, int matchNum, Object defaultValue) {
            extractors.add(ResultExtractor.builder().refName(refName).field(field).matchNum(matchNum).defaultValue(defaultValue).build());
            return self;
        }

        public List<Extractor> build() {
            return extractors;
        }
    }

    /**
     * 容器或取样器构建
     * <p>
     * 包含 {@link core.xyz.migoo.testelement.TestSuite }、
     * {@link protocol.xyz.migoo.debug.sampler.DebugSampler }、
     * {@link protocol.xyz.migoo.http.sampler.HTTPSampler }、
     * {@link protocol.xyz.migoo.jdbc.sampler.JDBCSampler }、
     * {@link protocol.xyz.migoo.redis.sampler.RedisSampler }
     * <p>
     * 如有协议独有提取器，请自行扩展 {@link core.xyz.migoo.builder.ExtensibleExtractorsBuilder}
     */
    public static abstract class ChildrenBuilder<SELF extends ChildrenBuilder<SELF>> implements IBuilder<List<TestElement<?>>> {

        protected List<TestElement<? extends Result>> children = new LazyBuilder<>();

        protected SELF self;

        public ChildrenBuilder() {
            self = (SELF) this;
        }

        /**
         * 自定义容器或取样器
         *
         * @param builder 定义容器或取样器构建器
         * @return 当前对象
         */
        public SELF apply(AbstractTestElementExecutable.Builder builder) {
            children.add(builder.build());
            return self;
        }

        public SELF apply(AbstractTestElementExecutable child) {
            children.add(child);
            return self;
        }

        public <T extends AbstractTestElementExecutable.Builder> SELF apply(Class<T> type, @DelegatesTo(strategy = Closure.DELEGATE_ONLY, type = "T") Closure<T> closure) {
            var builder = Groovy.builder(type, closure);
            children.add(builder.build());
            return self;
        }

        /**
         * 内置测试集合
         *
         * @return 当前对象
         */

        // -----------------------TestSuite----------------------------
        public SELF suite(TestSuite child) {
            this.children.add(child);
            return self;
        }

        public SELF suite(TestSuite.Builder child) {
            this.children.add(child.build());
            return self;
        }

        public SELF suite(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = TestSuite.Builder.class) Closure<?> closure) {
            var builder = TestSuite.builder();
            call(closure, builder);
            this.children.add(builder.build());
            return self;
        }

        // -----------------------DebugSampler----------------------------
        public SELF debug(DebugSampler child) {
            this.children.add(child);
            return self;
        }

        public SELF debug(DebugSampler.Builder child) {
            this.children.add(child.build());
            return self;
        }

        public SELF debug(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = DebugSampler.Builder.class) Closure<?> closure) {
            var builder = DebugSampler.builder();
            call(closure, builder);
            this.children.add(builder.build());
            return self;
        }

        // -----------------------HTTPSampler----------------------------
        public SELF http(HTTPSampler child) {
            this.children.add(child);
            return self;
        }

        public SELF http(HTTPSampler.Builder child) {
            this.children.add(child.build());
            return self;
        }

        public SELF http(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = HTTPSampler.Builder.class) Closure<?> closure) {
            var builder = HTTPSampler.builder();
            call(closure, builder);
            this.children.add(builder.build());
            return self;
        }

        // -----------------------JDBCSampler----------------------------
        public SELF jdbc(JDBCSampler child) {
            this.children.add(child);
            return self;
        }

        public SELF jdbc(JDBCSampler.Builder child) {
            this.children.add(child.build());
            return self;
        }

        public SELF jdbc(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = JDBCSampler.Builder.class) Closure<?> closure) {
            var builder = JDBCSampler.builder();
            call(closure, builder);
            this.children.add(builder.build());
            return self;
        }

        // -----------------------RedisSampler----------------------------
        public SELF redis(RedisSampler child) {
            this.children.add(child);
            return self;
        }

        public SELF redis(RedisSampler.Builder child) {
            this.children.add(child.build());
            return self;
        }

        public SELF redis(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = RedisSampler.Builder.class) Closure<?> closure) {
            var builder = RedisSampler.builder();
            call(closure, builder);
            this.children.add(builder.build());
            return self;
        }

        @Override
        public List<TestElement<?>> build() {
            return children;
        }
    }
}
