package core.xyz.migoo.builder;

import core.xyz.migoo.testelement.AbstractTestElement;

public abstract class ExtensiblePreprocessorsBuilder<SELF extends ExtensiblePreprocessorsBuilder<SELF, EXTRACTORS_BUILDER>,
        EXTRACTORS_BUILDER extends ExtensibleExtractorsBuilder<EXTRACTORS_BUILDER>>
        extends AbstractTestElement.PreprocessorsBuilder<SELF, EXTRACTORS_BUILDER> {
}
