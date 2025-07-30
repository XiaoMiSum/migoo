package protocol.xyz.migoo.dubbo.builder;

import core.xyz.migoo.builder.DefaultExtractorsBuilder;
import core.xyz.migoo.builder.ExtensiblePostprocessorsBuilder;

public class DubboPostprocessorsBuilder extends ExtensiblePostprocessorsBuilder<DubboPostprocessorsBuilder, DefaultExtractorsBuilder> {

    public static DubboPostprocessorsBuilder builder() {
        return new DubboPostprocessorsBuilder();
    }
}
