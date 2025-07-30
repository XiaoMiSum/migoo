package protocol.xyz.migoo.rabbitmq.builder;

import core.xyz.migoo.builder.ExtensibleChildrenBuilder;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import protocol.xyz.migoo.rabbitmq.sampler.RabbitMqSampler;

import static support.xyz.migoo.groovy.Groovy.call;

public class RabbitSamplersBuilder extends ExtensibleChildrenBuilder<RabbitSamplersBuilder> {

    public static RabbitSamplersBuilder builder() {
        return new RabbitSamplersBuilder();
    }

    public RabbitSamplersBuilder rabbit(RabbitMqSampler child) {
        this.children.add(child);
        return self;
    }

    public RabbitSamplersBuilder rabbit(RabbitMqSampler.Builder child) {
        this.children.add(child.build());
        return self;
    }

    public RabbitSamplersBuilder rabbit(@DelegatesTo(strategy = Closure.DELEGATE_ONLY, value = RabbitMqSampler.Builder.class) Closure<?> closure) {
        var builder = RabbitMqSampler.builder();
        call(closure, builder);
        this.children.add(builder.build());
        return self;
    }
}
