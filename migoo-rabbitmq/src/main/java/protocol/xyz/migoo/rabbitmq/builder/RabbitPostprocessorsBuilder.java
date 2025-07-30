package protocol.xyz.migoo.rabbitmq.builder;

import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.builder.ExtensiblePostprocessorsBuilder;

public class RabbitPostprocessorsBuilder extends ExtensiblePostprocessorsBuilder<RabbitPostprocessorsBuilder, DefaultExtractorsBuilder> {

    public static RabbitPostprocessorsBuilder builder() {
        return new RabbitPostprocessorsBuilder();
    }
}
