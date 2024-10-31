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

package protocol.xyz.migoo.http.config;

import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;
import core.xyz.migoo.testelement.TestStateListener;
import protocol.xyz.migoo.http.util.HTTPConstantsInterface;

import java.util.Objects;

/**
 * @author xiaomi
 */
@Alias({"http_default", "HttpDefault", "Http_Defaults"})
public class HttpDefaults extends AbstractTestElement implements TestStateListener, HTTPConstantsInterface {

    @Override
    public void testStarted() {
        var httpDefaults = (HttpDefaults) getVariables().get(HTTP_DEFAULT);
        if (Objects.nonNull(httpDefaults)) {
            // 合并已存在的, 重复Key 以当前对象为准
            setProperties(httpDefaults.getProperty());
        }
        getVariables().put(HTTP_DEFAULT, this);
    }

    @Override
    public void testEnded() {
        // nothing to do
    }

}