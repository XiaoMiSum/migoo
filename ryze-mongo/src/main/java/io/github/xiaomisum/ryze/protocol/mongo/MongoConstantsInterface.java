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

package io.github.xiaomisum.ryze.protocol.mongo;

import io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface;

/**
 * @author xiaomi
 */
public interface MongoConstantsInterface extends TestElementConstantsInterface {

    String DEF_REF_NAME_KEY = "__mongo_configure_element_default_ref_name__";

    /**
     * 连接地址：mongodb://root:123456@127.0.0.1:27017/admin
     */
    String URL = "url";

    /**
     * 数据库
     */
    String DATABASE = "database";

    /**
     * 集合 相当于 mysql的表
     */
    String COLLECTION = "collection";

    /**
     * 操作：find \ delete \ insert \ update
     */
    String ACTION = "action";

    /**
     * insert, update 操作数据
     */
    String DATA = "data";

    /**
     * 条件
     */
    String CONDITION = "condition";

    String FIND = "find";

    String DELETE = "delete";

    String INSERT = "insert";

    String UPDATE = "update";
}
