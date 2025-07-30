package protocol.xyz.migoo.kafka.builder;

import core.xyz.migoo.builder.ExtensibleConfigureElementsBuilder;

public class KafkaConfigureElementBuilder extends ExtensibleConfigureElementsBuilder<KafkaConfigureElementBuilder> {

    public static KafkaConfigureElementBuilder builder() {
        return new KafkaConfigureElementBuilder();
    }
}
