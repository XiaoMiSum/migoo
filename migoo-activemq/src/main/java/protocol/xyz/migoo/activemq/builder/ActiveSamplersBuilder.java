package protocol.xyz.migoo.activemq.builder;

import core.xyz.migoo.builder.ExtensibleChildrenBuilder;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import protocol.xyz.migoo.activemq.sampler.ActiveMqSampler;

import static support.xyz.migoo.groovy.Groovy.call;

public class ActiveSamplersBuilder extends ExtensibleChildrenBuilder<ActiveSamplersBuilder> {

    public static ActiveSamplersBuilder builder() {
        return new ActiveSamplersBuilder();
    }

    public ActiveSamplersBuilder active(ActiveMqSampler child) {
        this.children.add(child);
        return self;
    }

    public ActiveSamplersBuilder active(ActiveMqSampler.Builder child) {
        this.children.add(child.build());
        return self;
    }

    public ActiveSamplersBuilder active(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = ActiveMqSampler.Builder.class) Closure<?> closure) {
        var builder = ActiveMqSampler.builder();
        call(closure, builder);
        this.children.add(builder.build());
        return self;
    }
}
