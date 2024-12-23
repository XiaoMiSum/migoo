package protocol.xyz.migoo.debug.sampler;

import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.sampler.Sampler;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.TestStateListener;
import protocol.xyz.migoo.debug.AbstractDebugTestElement;

@Alias({"debug", "debug_sampler"})
public class DebugSampler extends AbstractDebugTestElement implements Sampler, TestStateListener {

    @Override
    public SampleResult sample() {
        return execute(new SampleResult(getPropertyAsString(TITLE)));
    }

}
