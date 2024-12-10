package protocol.xyz.migoo.mongo.config;

import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.TestStateListener;
import protocol.xyz.migoo.mongo.uitl.MongoConstantsInterface;

@Alias({"mongodatasource", "mongo_datasource", "mongo_data_source", "mongo_source", "mongosource"})
public class MongoSourceElement extends AbstractTestElement implements TestStateListener, MongoConstantsInterface {

    @Override
    public void testStarted() {
        getVariables().put(getPropertyAsString(VARIABLE_NAME_KEY), this);
    }

    @Override
    public void testEnded() {

    }

    public String getUrl() {
        return getPropertyAsString(URL);
    }

    public String getCollection() {
        return getPropertyAsString(COLLECTION);
    }

    public String getDatabase() {
        return getPropertyAsString(DATABASE);
    }
}
