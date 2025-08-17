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

package io.github.xiaomisum.ryze.protocol.dubbo;

import io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface;

/**
 * @author xiaomi
 */
public interface DubboConstantsInterface extends TestElementConstantsInterface {

    String DEF_REF_NAME_KEY = "__dubbo_configure_element_default_ref_name__";

    String REGISTRY = "registry";

    String REFERENCE = "reference";

    String REFERENCE_OBJECT = "inner_reference_object";

    /**
     * 移除该配置，合并到 {@link #ADDRESS}
     */
    @Deprecated(since = "6.0.0")
    String PROTOCOL = "protocol";

    String GROUP = "group";

    String NAMESPACE = "namespace";

    String APP_NAME = "app_name";

    String USERNAME = "username";

    String PASSWORD = "password";

    /**
     * 包含协议，格式：protocol://host:port，如：zookeeper://127.0.0.1:2181
     */
    String ADDRESS = "address";

    String TIMEOUT = "timeout";

    String LOAD_BALANCE = "load_balance";

    String RETRIES = "retries";

    String ASYNC = "async";

    String INTERFACE = "interface";

    String VERSION = "version";

    String METHOD = "method";

    String ARGS_PARAMETER_TYPES = "parameter_types";

    String ARGS_PARAMETERS = "parameters";

    String ATTACHMENT_ARGS = "attachment_args";

}