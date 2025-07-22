package protocol.xyz.migoo.redis.sampler;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.sampler.AbstractSampler;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import core.xyz.migoo.testelement.sampler.Sampler;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;
import protocol.xyz.migoo.redis.RealRedisRequest;
import protocol.xyz.migoo.redis.Redis;
import protocol.xyz.migoo.redis.config.RedisConfigureItem;
import protocol.xyz.migoo.redis.config.RedisDatasource;
import redis.clients.jedis.Protocol;

import java.util.Locale;

@Alias(value = {"redis", "redis_sampler"})
public class RedisSampler extends AbstractSampler<RedisConfigureItem, RedisSampler, DefaultSampleResult> implements Sampler<DefaultSampleResult>, JDBCConstantsInterface {

    @JSONField(serialize = false)
    private RedisDatasource datasource;

    private byte[] bytes;

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        result.sampleStart();
        try (var jedis = datasource.getConnection()) {
            var command = runtime.config.getCommand();
            var result2 = jedis.sendCommand(Protocol.Command.valueOf(command.toUpperCase(Locale.ROOT)), runtime.config.getSend().split(","));
            bytes = Redis.toBytes(result2);
        } catch (Exception e) {
            result.setTrack(e);
        } finally {
            result.sampleEnd();
        }
    }

    @Override
    protected void handleRequest(ContextWrapper context, DefaultSampleResult result) {
        super.handleRequest(context, result);
        datasource = (RedisDatasource) context.getLocalVariablesWrapper().get(runtime.config.getDatasource());
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(new RealRedisRequest(datasource.getUrl(), runtime.config.getCommand(), runtime.config.getSend()));
        result.setResponse(SampleResult.DefaultReal.build(bytes));
    }
}
