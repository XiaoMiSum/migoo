/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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

package core.xyz.migoo;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @author xiaomi
 * @date 2021/1/20 19:08
 */
public class TestSuiteTest {
    TestContext context;

    @BeforeEach
    public void init(){
        JSONObject request = new JSONObject();
        JSONObject headers = new JSONObject();
        context = new TestContext();
        context.setName("测试项目");
        context.setRequest(request);
        request.put("protocol", "http");
        request.put("host", "www.baidu.com");
        request.put("port", "8080");
        request.put("method", "");
        request.put("headers", headers);
        headers.put("token", "1234567890");
        headers.put("guid", "uuid");

    }

    @Test
    public void test(){
        TestContext set = new TestContext();
        JSONObject request = new JSONObject();
        JSONObject headers = new JSONObject();
        set.setName("测试接口");
        set.setRequest(request);
        request.put("protocol", "");
        request.put("host", "www.qq.com");
        request.put("port", "");
        request.put("api", "/api");
        request.put("method", "get");
        request.put("headers", headers);
        headers.put("token2", 1111);
        headers.put("guid", "uuid2");
        set.setCases(new ArrayList<>());
        TestSet testSet = new TestSet(set, context);
        Assertions.assertEquals(set.getRequestProtocol(), context.getRequestProtocol());
        Assertions.assertEquals(set.getRequestApi(), request.getString("api"));
        Assertions.assertEquals(set.getRequestMethod(), request.getString("method"));
        Assertions.assertEquals(set.getRequestHeaders().size(), 3);
        Assertions.assertEquals(set.getRequestHeaders().get("guid"), headers.getString("guid"));
        Assertions.assertEquals(set.getRequestHost(), request.getString("host"));
        Assertions.assertEquals(set.getRequestPort(), context.getRequestPort());
    }
}
