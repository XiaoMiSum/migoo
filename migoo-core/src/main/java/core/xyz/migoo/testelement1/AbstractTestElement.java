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

package core.xyz.migoo.testelement1;

import core.xyz.migoo.ContextWrapper;
import core.xyz.migoo.SessionRunner;
import core.xyz.migoo.context.Context;
import core.xyz.migoo.context.TestStepContext;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.TestElementConfigure;
import core.xyz.migoo.testelement.TestElementConstantsInterface;
import core.xyz.migoo.variable.MiGooVariables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试组件抽象类，提供公共属性和方法
 *
 * @author xiaomi
 */
public abstract class AbstractTestElement<SELF extends AbstractTestElement<SELF, T>, T extends Result<T>>
        implements TestElement<T>, TestElementConstantsInterface {

    protected String id;

    protected String title;

    protected boolean disabled = false;

    protected TestElementConfigure config;


    // 元数据，可以挂载一些辅助数据
    protected Map<String, Object> metadata = new HashMap<>();

    protected Map<String, Object> rawData;

    protected SELF runtime;

    protected boolean initialized = false;

    public AbstractTestElement() {
    }

    protected void initialized(SessionRunner session) {
        initialized = true;
        runtime = newInstance();
    }

    /**
     * 当前测试元件的上下文链
     *
     * @param parentContext 父上下文链
     */
    protected List<Context> getContextChain(List<Context> parentContext) {
        List<Context> contextChain = new ArrayList<>(parentContext);
        contextChain.add(createCurrentContext());
        return contextChain;
    }

    /**
     * 创建当前测试元件的上下文对象，当子类有额外的需求时重写该方法
     *
     * @return 当前测试元件的上下文对象
     */
    protected TestStepContext createCurrentContext() {
        TestStepContext context = new TestStepContext();
        context.setConfigGroup(runtime.config);
        return context;
    }

    protected void evalVariableConfigItem(ContextWrapper ctx) {
        MiGooVariables item;
        if (runtime.config != null && (item = runtime.config.getVariables()) != null) {
            ctx.eval(item);
        }
    }


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
        self.title = title;
        self.id = id;
        self.disabled = disabled;
        self.config = config;
        self.metadata = metadata;
        return self;
    }


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

    public TestElementConfigure getConfig() {
        return config;
    }

    public void setConfig(TestElementConfigure config) {
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
