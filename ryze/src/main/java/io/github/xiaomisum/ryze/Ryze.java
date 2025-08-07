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

package io.github.xiaomisum.ryze;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.github.xiaomisum.ryze.core.ApplicationConfig;
import io.github.xiaomisum.ryze.core.Result;
import io.github.xiaomisum.ryze.core.SessionRunner;
import io.github.xiaomisum.ryze.support.TestDataLoader;

import java.util.Map;

import static io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface.TEST_CLASS;

/**
 * @author xiaomi
 */
@SuppressWarnings({"unchecked"})
public class Ryze {


    private final JsonTree testcase;

    public Ryze(JsonTree testcase) {
        this.testcase = testcase;
    }

    public static Result start(String filePath) {
        var testcase = TestDataLoader.toJavaObject(filePath, JSONObject.class);
        return start(testcase);
    }

    public static Result start(Map<String, Object> testcase) {
        return start(new JsonTree(testcase));
    }

    public static Result start(JsonTree testcase) {
        try {
            return new Ryze(testcase).runTest();
        } finally {
            SessionRunner.removeSession();
        }
    }

    private Result runTest() {
        var clazz = ApplicationConfig.getTestElementKeyMap().get(testcase.getString(TEST_CLASS));
        return SessionRunner.getSessionIfNoneCreateNew().runTest(JSON.parseObject(testcase.toJSONString(), clazz));
    }
}