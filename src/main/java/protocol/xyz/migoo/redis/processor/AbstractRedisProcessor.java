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

package protocol.xyz.migoo.redis.processor;

import core.xyz.migoo.samplers.SampleResult;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.redis.AbstractRedisTestElement;
import protocol.xyz.migoo.redis.config.RedisSourceElement;
import redis.clients.jedis.Jedis;

public abstract class AbstractRedisProcessor extends AbstractRedisTestElement {

    protected SampleResult result;

    public SampleResult process() {
        // 1. 获取当前处理器设置的数据源变量名称
        String dataSourceName = getPropertyAsString("datasource");
        if (StringUtils.isBlank(dataSourceName)) {
            throw new IllegalArgumentException("DataSource Name must not be null in datasource");
        }
        // 2. 从变量中取出保存的连接池
        RedisSourceElement dataSource = (RedisSourceElement) getVariables().get(dataSourceName);
        try (Jedis conn = dataSource.getConnection()) {
            SampleResult result = execute(conn);
            result.setUrl(dataSource.getUrl());
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}