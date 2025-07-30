package protocol.xyz.migoo.dubbo.builder;

import core.xyz.migoo.builder.ExtensibleChildrenBuilder;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import protocol.xyz.migoo.dubbo.sampler.DubboSampler;

import static support.xyz.migoo.groovy.Groovy.call;

public class DubboSamplersBuilder extends ExtensibleChildrenBuilder<DubboSamplersBuilder> {

    public static DubboSamplersBuilder builder() {
        return new DubboSamplersBuilder();
    }

    public DubboSamplersBuilder dubbo(DubboSampler child) {
        this.children.add(child);
        return self;
    }

    public DubboSamplersBuilder dubbo(DubboSampler.Builder child) {
        this.children.add(child.build());
        return self;
    }

    public DubboSamplersBuilder dubbo(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = DubboSampler.Builder.class) Closure<?> closure) {
        var builder = DubboSampler.builder();
        call(closure, builder);
        this.children.add(builder.build());
        return self;
    }
}
