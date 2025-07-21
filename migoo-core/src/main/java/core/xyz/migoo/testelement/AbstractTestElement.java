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
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.filter.TestFilter;
import core.xyz.migoo.report.Result;
import support.xyz.migoo.KryoUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试组件抽象类，提供公共属性和方法
 *
 * @author xiaomi
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractTestElement<CONFIG extends ConfigureItem, SELF extends AbstractTestElement<CONFIG, SELF, T>, T extends Result>
        implements TestElement<T>, TestElementConstantsInterface {

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

    protected abstract T getTestResult();


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

}
