package protocol.xyz.migoo.debug.processer;

import core.xyz.migoo.processor.PostProcessor;
import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.Alias;
import protocol.xyz.migoo.debug.AbstractDebugTestElement;

@Alias({"debug_postprocessor", "debug_post_processor"})
public class DebugPostprocessor extends AbstractDebugTestElement implements PostProcessor {
    @Override
    public SampleResult process() {
        return super.execute(new SampleResult(getPropertyAsString(TITLE)));
    }
}
