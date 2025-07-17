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

public abstract class AbstractTestElement<SELF extends AbstractTestElement<SELF, T>, T extends Result>
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

    private void evalVariableConfigItem(ContextWrapper ctx) {
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
