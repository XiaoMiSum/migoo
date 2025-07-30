package protocol.xyz.migoo.kafka.builder;

import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.builder.ExtensiblePreprocessorsBuilder;

public class KafkaPreprocessorsBuilder extends ExtensiblePreprocessorsBuilder<KafkaPreprocessorsBuilder, DefaultExtractorsBuilder> {

    public static KafkaPreprocessorsBuilder builder() {
        return new KafkaPreprocessorsBuilder();
    }
}
