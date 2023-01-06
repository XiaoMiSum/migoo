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

package protocol.xyz.migoo.dubbo.sampler;

import core.xyz.migoo.sampler.SampleResult;
import core.xyz.migoo.sampler.Sampler;
import core.xyz.migoo.testelement.MiGooProperty;
import core.xyz.migoo.testelement.TestStateListener;
import protocol.xyz.migoo.dubbo.AbstractDubboTestElement;

/**
 * @author mi.xiao
 * @date 2021/4/10 21:10
 */
public class DubboSampler extends AbstractDubboTestElement implements Sampler, TestStateListener {

    @Override
    public void testStarted() {
        MiGooProperty property = getPropertyAsMiGooProperty(CONFIG);
        this.setProperties(property);
        super.testStarted();
    }

    @Override
    public SampleResult sample() {
        DubboSampleResult result = new DubboSampleResult(getPropertyAsString(TITLE));
        try {
            super.execute(result);
        } catch (Exception e) {
            result.setThrowable(e);
        }
        return result;
    }

    @Override
    public void testEnded() {

    }
}
