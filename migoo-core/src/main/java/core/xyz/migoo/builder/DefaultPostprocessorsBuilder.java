package core.xyz.migoo.builder;

public class DefaultPostprocessorsBuilder extends ExtensiblePostprocessorsBuilder<DefaultPostprocessorsBuilder, DefaultExtractorsBuilder> {

    public static DefaultPostprocessorsBuilder builder() {
        return new DefaultPostprocessorsBuilder();
    }

}
