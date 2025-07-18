package protocol.xyz.migoo.debug.sampler;

import core.xyz.migoo.testelement.Alias;
import protocol.xyz.migoo.debug.AbstractDebugTestElement;

@Alias({"debug", "debug_sampler"})
public class DebugSampler extends AbstractDebugTestElement implements Sampler, TestStateListener {

    @Override
    public SampleResult sample() {
        return execute(new SampleResult(getPropertyAsString(TITLE)));
    }

}
