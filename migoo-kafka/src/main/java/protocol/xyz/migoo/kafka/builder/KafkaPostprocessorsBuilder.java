package protocol.xyz.migoo.kafka.builder;

import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.builder.ExtensiblePostprocessorsBuilder;

public class KafkaPostprocessorsBuilder extends ExtensiblePostprocessorsBuilder<KafkaPostprocessorsBuilder, DefaultExtractorsBuilder> {

    public static KafkaPostprocessorsBuilder builder() {
        return new KafkaPostprocessorsBuilder();
    }
}
