package protocol.xyz.migoo.redis.processor;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.processor.AbstractProcessor;
import core.xyz.migoo.testelement.processor.Postprocessor;
import core.xyz.migoo.testelement.sampler.DefaultSampleResult;
import core.xyz.migoo.testelement.sampler.SampleResult;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.jdbc.JDBCConstantsInterface;
import protocol.xyz.migoo.redis.RealRedisRequest;
import protocol.xyz.migoo.redis.Redis;
import protocol.xyz.migoo.redis.config.RedisConfigureItem;
import protocol.xyz.migoo.redis.config.RedisDatasource;
import redis.clients.jedis.Protocol;

import java.util.Locale;

@Alias(value = {"redis_postprocessor", "redis_post_processor", "redis"})
public class RedisPostprocessor extends AbstractProcessor<RedisConfigureItem, RedisPostprocessor, DefaultSampleResult> implements Postprocessor, JDBCConstantsInterface {

    @JSONField(serialize = false)
    private RedisDatasource datasource;

    private byte[] bytes;

    @Override
    protected DefaultSampleResult getTestResult() {
        return new DefaultSampleResult(id, StringUtils.isBlank(title) ? "Redis 前置处理器" : title);
    }

    @Override
    protected void sample(ContextWrapper context, DefaultSampleResult result) {
        result.sampleStart();
        try (var jedis = datasource.getConnection()) {
            var command = runtime.getConfig().getCommand();
            var result2 = jedis.sendCommand(Protocol.Command.valueOf(command.toUpperCase(Locale.ROOT)), runtime.getConfig().getSend().split(","));
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
        datasource = (RedisDatasource) context.getLocalVariablesWrapper().get(runtime.getConfig().getDatasource());
    }

    @Override
    protected void handleResponse(ContextWrapper context, DefaultSampleResult result) {
        super.handleResponse(context, result);
        result.setRequest(new RealRedisRequest(datasource.getUrl(), runtime.getConfig().getCommand(), runtime.getConfig().getSend()));
        result.setResponse(SampleResult.DefaultReal.build(bytes));
    }
}
