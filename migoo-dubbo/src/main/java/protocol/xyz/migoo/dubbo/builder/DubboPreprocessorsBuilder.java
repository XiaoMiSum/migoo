package protocol.xyz.migoo.dubbo.builder;

import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.builder.ExtensiblePreprocessorsBuilder;

public class DubboPreprocessorsBuilder extends ExtensiblePreprocessorsBuilder<DubboPreprocessorsBuilder, DefaultExtractorsBuilder> {

    public static DubboPreprocessorsBuilder builder() {
        return new DubboPreprocessorsBuilder();
    }
}
