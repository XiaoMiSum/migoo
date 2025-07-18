package protocol.xyz.migoo.debug.processer;

import core.xyz.migoo.processor.Preprocessor;
import core.xyz.migoo.testelement.Alias;
import protocol.xyz.migoo.debug.AbstractDebugTestElement;

@Alias({"debug_preprocessor", "debug_pre_processor"})
public class DebugPreprocessor extends AbstractDebugTestElement implements Preprocessor {
    @Override
    public SampleResult process() {
        return super.execute(new SampleResult(getPropertyAsString(TITLE)));
    }
}
