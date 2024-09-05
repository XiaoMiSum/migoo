package core.xyz.migoo.engine;

import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementService;
import core.xyz.migoo.variable.MiGooVariables;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

import static core.xyz.migoo.testelement.AbstractTestElement.*;

public class MiGooContext {

    private final Vector<TestElement> configurations = new Vector<>(10);
    private final Vector<TestElement> preprocessors = new Vector<>(10);
    private final Vector<MiGooContext> children = new Vector<>(10);
    private final Vector<TestElement> validators = new Vector<>(10);
    private final Vector<TestElement> extractors = new Vector<>(10);
    private final Vector<TestElement> postprocessors = new Vector<>(10);
    private final MiGooVariables variables;
    private final String id;
    private final String title;
    private TestElement sampler;

    public MiGooContext(Testplan plan) {
        this.variables = plan.getVariables();
        this.id = plan.getString(ID);
        this.title = plan.getString(TITLE);
        if (plan.isSampler()) {
            sampler = TestElementService.getService(plan.getString(TEST_CLASS));
            // 这里的 plan 与 sampler 为同一节点，直接设置变量
            TestElementService.prepare(sampler, plan.getJSONObject(CONFIG), variables);
        }
    }

    public static MiGooContext create(Testplan plan) {
        var context = new MiGooContext(plan);
        context.addTestElements(CONFIG_ELEMENTS, plan, context.getConfigurations());
        context.addTestElements(PREPROCESSORS, plan, context.getPreprocessors());
        context.addTestElements(POSTPROCESSORS, plan, context.getPostprocessors());
        context.addChildren(plan.getList(CHILDREN, plan.getClass()));
        context.addTestElements(VALIDATORS, plan, context.getValidators());
        context.addTestElements(EXTRACTORS, plan, context.getExtractors());
        return context;
    }

    public void addChildren(List<? extends Testplan> children) {
        if (Objects.nonNull(children)) {
            children.forEach(item -> this.children.add(create(item)));
        }
    }

    private void addTestElements(String key, Testplan plan, Vector<TestElement> elements) {
        var components = plan.getJSONArray(key);
        if (Objects.nonNull(components)) {
            components.forEach(item -> {
                // 配置元件、处理器、提取器、验证器 等测试组件不可单独设置变量，设置这些组件的变量为测试集合或取样器的变量
                TestElement el = TestElementService.getService(((Testplan) item).getString(TEST_CLASS));
                TestElementService.prepare(el, ((Testplan) item), variables);
                elements.add(el);
            });
        }
    }

    public Vector<TestElement> getConfigurations() {
        return configurations;
    }

    public Vector<TestElement> getPreprocessors() {
        return preprocessors;
    }

    public Vector<MiGooContext> getChildren() {
        return children;
    }

    public Vector<TestElement> getValidators() {
        return validators;
    }

    public Vector<TestElement> getExtractors() {
        return extractors;
    }

    public Vector<TestElement> getPostprocessors() {
        return postprocessors;
    }

    public MiGooVariables getVariables() {
        return variables;
    }

    public TestElement getSampler() {
        return sampler;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }
}
