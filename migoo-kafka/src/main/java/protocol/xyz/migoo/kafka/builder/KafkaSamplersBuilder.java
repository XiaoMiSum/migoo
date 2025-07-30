package protocol.xyz.migoo.kafka.builder;

import core.xyz.migoo.builder.ExtensibleChildrenBuilder;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import protocol.xyz.migoo.kafka.sampler.KafkaSampler;

import static support.xyz.migoo.groovy.Groovy.call;

public class KafkaSamplersBuilder extends ExtensibleChildrenBuilder<KafkaSamplersBuilder> {

    public static KafkaSamplersBuilder builder() {
        return new KafkaSamplersBuilder();
    }

    public KafkaSamplersBuilder kafka(KafkaSampler child) {
        this.children.add(child);
        return self;
    }

    public KafkaSamplersBuilder kafka(KafkaSampler.Builder child) {
        this.children.add(child.build());
        return self;
    }

    public KafkaSamplersBuilder kafka(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = KafkaSampler.Builder.class) Closure<?> closure) {
        var builder = KafkaSampler.builder();
        call(closure, builder);
        this.children.add(builder.build());
        return self;
    }
}
