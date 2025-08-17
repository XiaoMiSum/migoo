/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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
 *
 */

package io.github.xiaomisum.ryze.protocol.jdbc;

import io.github.xiaomisum.ryze.core.testelement.sampler.SampleResult;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xiaomi
 * Created at 2025/7/21 19:11
 */
public class RealJDBCRequest extends SampleResult.Real implements JDBCConstantsInterface {

    private final String url;

    private final String username;

    private final String password;

    public RealJDBCRequest(String url, String username, String password, String sql) {
        super(sql.getBytes());
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * 设置 JDBC请求信息，格式如下
     * <p>
     * jdbc:mysql://localhost:3306/dbname?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
     * username
     * password
     * <p>
     * data
     */
    @Override
    public String format() {
        return url + "\n" +
                USERNAME + ": " + username + "\n" +
                PASSWORD + ": " + password +
                (StringUtils.isNotBlank(bytesAsString()) ? "\n\n" + bytesAsString() : "");
    }

}
