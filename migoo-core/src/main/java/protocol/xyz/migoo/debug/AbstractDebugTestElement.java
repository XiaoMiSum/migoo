package protocol.xyz.migoo.debug;

import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;

public abstract class AbstractDebugTestElement extends AbstractTestElement {

    public void testStarted() {
    }

    protected SampleResult execute(SampleResult result) {
        result.setTestClass(this.getClass());
        result.sampleStart();
        result.setSamplerData(this.getProperty().toString());
        result.sampleEnd();
        System.out.println(this.getProperty().toString());
        result.setResponseData(this.getProperty().toString());
        return result;
    }

    public void testEnded() {

    }
}
