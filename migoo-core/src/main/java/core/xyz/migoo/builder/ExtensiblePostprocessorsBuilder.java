package core.xyz.migoo.builder;

import core.xyz.migoo.testelement.AbstractTestElement;

public abstract class ExtensiblePostprocessorsBuilder<SELF extends ExtensiblePostprocessorsBuilder<SELF, EXTRACTORS_BUILDER>,
        EXTRACTORS_BUILDER extends ExtensibleExtractorsBuilder<EXTRACTORS_BUILDER>>
        extends AbstractTestElement.PostprocessorsBuilder<SELF, EXTRACTORS_BUILDER> {
}
