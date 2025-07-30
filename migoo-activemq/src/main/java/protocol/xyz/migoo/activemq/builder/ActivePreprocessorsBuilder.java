package protocol.xyz.migoo.activemq.builder;

import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.builder.ExtensiblePreprocessorsBuilder;

public class ActivePreprocessorsBuilder extends ExtensiblePreprocessorsBuilder<ActivePreprocessorsBuilder, DefaultExtractorsBuilder> {

    public static ActivePreprocessorsBuilder builder() {
        return new ActivePreprocessorsBuilder();
    }
}
