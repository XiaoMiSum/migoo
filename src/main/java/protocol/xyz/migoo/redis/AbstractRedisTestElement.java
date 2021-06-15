/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package protocol.xyz.migoo.redis;

import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.Collection;

public abstract class AbstractRedisTestElement extends AbstractTestElement {

    private static final String HASH = "h";
    private static final String LIST = "l";
    private static final String LIST_B = "b";
    private static final String LIST_R = "r";
    private static final String DELETE = "delete";
    private static final String DEL = "del";
    private static final String SET = "set";
    private static final String GET = "get";
    private static final String MGET = "mget";
    private static final String EXISTS = "exists";
    private static final String HDELETE = "hdelete";
    private static final String HDEL = "hdel";
    private static final String HEXISTS = "hexists";
    private static final String HGET = "hget";
    private static final String HGETALL = "hgetall";
    private static final String HKEYS = "hkeys";
    private static final String HLEN = "hlen";
    private static final String HMGET = "hmget";
    private static final String HSET = "hset";
    private static final String BLPOP = "blpop";
    private static final String BRPOP = "brpop";
    private static final String LINDEX = "lindex";
    private static final String LLEN = "llen";
    private static final String LPOP = "lpop";
    private static final String LPUSH = "lpush";
    private static final String LRANGE = "lrange";
    private static final String LREM = "lrem";
    private static final String LSET = "lset";
    private static final String RPOP = "rpop";
    private static final String RPUSH = "rpush";
    private static final String KEY = "key";
    private static final String VALUE = "value";
    private static final String FIELD = "field";
    private static final String INDEX = "index";
    private static final String INDEX_START = "index_start";
    private static final String INDEX_END = "index_end";
    private static final String COUNT = "count";

    protected SampleResult execute(Jedis conn) throws UnsupportedOperationException {
        return execute(conn, new SampleResult(getPropertyAsString(TITLE)));
    }

    protected SampleResult execute(Jedis conn, SampleResult sample) throws UnsupportedOperationException {
        sample.setTestClass(this.getClass());
        Object result = null;
        try {
            String queryType = getPropertyAsString("query_type");
            if (StringUtils.isBlank(queryType)) {
                throw new UnsupportedOperationException("Unexpected query type");
            }
            queryType = queryType.trim();
            sample.sampleStart();
            if (queryType.startsWith(HASH)) {
                result = hashQuery(conn, queryType);
            } else if (queryType.startsWith(LIST) || queryType.startsWith(LIST_R) || queryType.startsWith(LIST_B)) {
                result = listQuery(conn, queryType);
            } else {
                result = defQuery(conn, queryType);
            }
        } finally {
            sample.sampleEnd();
            sample.setResponseData(result == null ? "" : result instanceof Collection ? listToString((Collection) result) : result.toString());
        }
        return sample;
    }

    private Object defQuery(Jedis conn, String queryType) {
        Object result;
        if (SET.equalsIgnoreCase(queryType)) {
            result = conn.set(getPropertyAsString(KEY), getPropertyAsString(VALUE));
        } else if (DEL.equalsIgnoreCase(queryType) || DELETE.equalsIgnoreCase(queryType)) {
            result = conn.del(getPropertyAsString(KEY));
        } else if (EXISTS.equalsIgnoreCase(queryType)) {
            result = conn.exists(getPropertyAsString(KEY));
        } else if (GET.equalsIgnoreCase(queryType)) {
            result = conn.get(getPropertyAsString(KEY));
        } else if (MGET.equalsIgnoreCase(queryType)) {
            result = conn.mget(getPropertyAsString(KEY).split(","));
        } else {
            throw new UnsupportedOperationException("Unexpected query type");
        }
        return result;
    }

    private Object hashQuery(Jedis conn, String queryType) {
        Object result;
        if (HDEL.equalsIgnoreCase(queryType) || HDELETE.equalsIgnoreCase(queryType)) {
            result = conn.hdel(getPropertyAsString(KEY), getPropertyAsString(FIELD));
        } else if (HEXISTS.equalsIgnoreCase(queryType)) {
            result = conn.hexists(getPropertyAsString(KEY), getPropertyAsString(FIELD));
        } else if (HGET.equalsIgnoreCase(queryType)) {
            result = conn.hget(getPropertyAsString(KEY), getPropertyAsString(FIELD));
        } else if (HMGET.equalsIgnoreCase(queryType)) {
            result = conn.hmget(getPropertyAsString(KEY), getPropertyAsString(FIELD).split(","));
        } else if (HGETALL.equalsIgnoreCase(queryType)) {
            result = conn.hgetAll(getPropertyAsString(KEY));
        } else if (HKEYS.equalsIgnoreCase(queryType)) {
            result = conn.hkeys(getPropertyAsString(KEY));
        } else if (HLEN.equalsIgnoreCase(queryType)) {
            result = conn.hlen(getPropertyAsString(KEY));
        } else if (HSET.equalsIgnoreCase(queryType)) {
            result = conn.hset(getPropertyAsString(KEY), getPropertyAsString(FIELD), getPropertyAsString(VALUE));
        } else {
            throw new UnsupportedOperationException("Unexpected query type");
        }
        return result;
    }

    private Object listQuery(Jedis conn, String queryType) {
        Object result;
        if (BLPOP.equalsIgnoreCase(queryType)) {
            result = conn.blpop(getPropertyAsString(KEY));
        } else if (BRPOP.equalsIgnoreCase(queryType)) {
            result = conn.brpop(getPropertyAsString(KEY));
        } else if (LINDEX.equalsIgnoreCase(queryType)) {
            result = conn.lindex(getPropertyAsString(KEY), getPropertyAsInt(INDEX));
        } else if (LLEN.equalsIgnoreCase(queryType)) {
            result = conn.llen(getPropertyAsString(KEY));
        } else if (LPOP.equalsIgnoreCase(queryType)) {
            result = conn.lpop(getPropertyAsString(KEY));
        } else if (LPUSH.equalsIgnoreCase(queryType)) {
            result = conn.lpush(getPropertyAsString(KEY), getPropertyAsString(VALUE).split(","));
        } else if (LRANGE.equalsIgnoreCase(queryType)) {
            result = conn.lrange(getPropertyAsString(KEY), getPropertyAsInt(INDEX_START), getPropertyAsInt(INDEX_END));
        } else if (LREM.equalsIgnoreCase(queryType)) {
            result = conn.lrem(getPropertyAsString(KEY), getPropertyAsInt(COUNT), getPropertyAsString(VALUE));
        } else if (LSET.equalsIgnoreCase(queryType)) {
            result = conn.lset(getPropertyAsString(KEY), getPropertyAsInt(INDEX), getPropertyAsString(VALUE));
        } else if (RPOP.equalsIgnoreCase(queryType)) {
            result = conn.rpop(getPropertyAsString(KEY));
        } else if (RPUSH.equalsIgnoreCase(queryType)) {
            result = conn.rpush(getPropertyAsString(KEY), getPropertyAsString(VALUE).split(","));
        } else {
            throw new UnsupportedOperationException("Unexpected query type");
        }
        return result;
    }

    public static String listToString(Collection<?> list) {
        StringBuilder sb = new StringBuilder("[");
        list.forEach(item -> {
            if (sb.length() > 1) {
                sb.append(",");
            }
            if (item != null) {
                sb.append("\"").append(item).append("\"");
            }
        });
        return sb.append("]").toString();
    }
}