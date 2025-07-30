package core.xyz.migoo.builder;

public class DefaultPreprocessorsBuilder extends ExtensiblePreprocessorsBuilder<DefaultPreprocessorsBuilder, DefaultExtractorsBuilder> {

    public static DefaultPreprocessorsBuilder builder() {
        return new DefaultPreprocessorsBuilder();
    }
}
