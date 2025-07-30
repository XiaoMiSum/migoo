package protocol.xyz.migoo.rabbitmq.builder;

import core.xyz.migoo.builder.ExtensibleConfigureElementsBuilder;

public class RabbitConfigureElementBuilder extends ExtensibleConfigureElementsBuilder<RabbitConfigureElementBuilder> {

    public static RabbitConfigureElementBuilder builder() {
        return new RabbitConfigureElementBuilder();
    }
}
