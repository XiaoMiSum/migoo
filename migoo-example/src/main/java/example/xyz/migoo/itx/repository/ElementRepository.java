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

package example.xyz.migoo.itx.repository;

import com.alibaba.fastjson2.JSONObject;
import itestx.xyz.migoo.element.Configs;
import itestx.xyz.migoo.element.Extractors;
import itestx.xyz.migoo.element.Processors;

/**
 * @author xiaomi
 * Created at 2022/8/25 11:59
 */
public class ElementRepository {

    public final static Configs HTTP_DEFAULT_HOST = Configs.builder().httpDefault().host("migoo.xyz");
    public final static Configs HTTP_DEFAULT_HEADER = Configs.builder().httpDefault().addHeader("token", "${token}");
    public final static Processors PREPROCESS_GET_TOKEN = Processors.pre.httpProcessor().method("post").api("/api/login")
            .body(JSONObject.of("userName", "${username}", "password", "${password}", "sign", "__digest(,${username}${password},,)"))
            .addExtractor(Extractors.builder().json("token").jsonPath("$.data.token").matchNo(0));


}
