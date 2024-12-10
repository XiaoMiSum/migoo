package protocol.xyz.migoo.mongo.sampler;

import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.sampler.Sampler;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.TestStateListener;
import protocol.xyz.migoo.mongo.AbstractMongoTestElement;

@Alias({"mongo", "mongo_sampler"})
public class MongoSampler extends AbstractMongoTestElement implements Sampler, TestStateListener {

    @Override
    public SampleResult sample() {
        var result = new SampleResult(getPropertyAsString(TITLE));
        try {
            super.execute(result);
        } catch (Exception e) {
            result.setThrowable(e);
        }
        return result;
    }

    @Override
    public void testEnded() {

    }
}
