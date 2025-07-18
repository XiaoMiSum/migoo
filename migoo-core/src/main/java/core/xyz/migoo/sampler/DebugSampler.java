package core.xyz.migoo.sampler;

import core.xyz.migoo.context.ContextWrapper;

public class DebugSampler extends AbstractSampler<DebugSampler, DefaultSampleResult> {
    @Override
    protected void sample(ContextWrapper contextWrapper, DefaultSampleResult result) {
// todo
    }

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(runtime.title);
    }
}
