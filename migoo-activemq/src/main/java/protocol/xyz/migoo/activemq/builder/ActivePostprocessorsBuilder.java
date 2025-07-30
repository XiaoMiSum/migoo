package protocol.xyz.migoo.activemq.builder;

import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.builder.ExtensiblePostprocessorsBuilder;

public class ActivePostprocessorsBuilder extends ExtensiblePostprocessorsBuilder<ActivePostprocessorsBuilder, DefaultExtractorsBuilder> {

    public static ActivePostprocessorsBuilder builder() {
        return new ActivePostprocessorsBuilder();
    }
}
