package protocol.xyz.migoo.rabbitmq.builder;

import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.builder.ExtensiblePreprocessorsBuilder;

public class RabbitPreprocessorsBuilder extends ExtensiblePreprocessorsBuilder<RabbitPreprocessorsBuilder, DefaultExtractorsBuilder> {

    public static RabbitPreprocessorsBuilder builder() {
        return new RabbitPreprocessorsBuilder();
    }
}
