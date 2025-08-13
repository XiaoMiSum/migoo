/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.xiaomisum.ryze.protocol.redis;

import io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface;

/**
 * @author xiaomi
 */
public interface RedisConstantsInterface extends TestElementConstantsInterface {

    String DATASOURCE = "datasource";

    String REDIS_URL_TEMPLATE = "redis://";
    /**
     * redis://[username:password@]host[:port][/db]
     */
    String URL = "url";
    /**
     * 废弃属性，请使用 host @see {@link #URL}
     */
    @Deprecated(since = "6.0.0")
    String HOST = "host";
    /**
     * 废弃属性，请使用 host @see {@link #URL}
     */
    @Deprecated(since = "6.0.0")
    String PORT = "port";
    /**
     * 废弃属性，请使用 host @see {@link #URL}
     */
    @Deprecated(since = "6.0.0")
    String DATABASE = "database";
    /**
     * 废弃属性，请使用 host @see {@link #URL}
     */
    @Deprecated(since = "6.0.0")
    String USERNAME = "username";
    /**
     * 废弃属性，请使用 host @see {@link #URL}
     */
    @Deprecated(since = "6.0.0")
    String PASSWORD = "password";
    /**
     * 废弃属性，请使用 timeout @see {@link #TIMEOUT}
     */
    @Deprecated(since = "6.0.0", forRemoval = true)
    String TIME_OUT = "time_out";

    String TIMEOUT = "timeout";

    String MAX_TOTAL = "max_total";

    String MAX_IDLE = "max_idle";

    String MIN_IDLE = "min_idle";

    String COMMAND = "command";

    String ARGS = "args";

    /**
     * 废弃属性，请使用 args @see {@link #ARGS}
     */
    @Deprecated(since = "6.0.0", forRemoval = true)
    String SEND = "send";
}
