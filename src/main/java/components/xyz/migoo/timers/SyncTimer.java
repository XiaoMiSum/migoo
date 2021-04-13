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

package components.xyz.migoo.timers;

import core.xyz.migoo.processor.PostProcessor;
import core.xyz.migoo.samplers.SampleResult;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.Alias;

/**
 * @author mi.xiao
 * @date 2021/4/7 19:49
 */
@Alias(aliasList = {"SyncTimer", "Timer", "sync_timer", "def_timer", "defTimer"})
public class SyncTimer extends AbstractTestElement implements PostProcessor {

    private long timeout;

    @Override
    public void testStarted() {
        timeout = getPropertyAsLong("timeout") * 1000L;
    }

    @Override
    public void testEnded() {
        timeout = 0;
    }

    @Override
    public SampleResult process() {
        SampleResult result = new SampleResult(this.getClass().toString());
        if (timeout > 0) {
            synchronized (this) {
                try {
                    this.wait(timeout);
                } catch (InterruptedException e) {
                    this.notify();
                }
            }
        }
        result.setSuccessful(true);
        result.setResponseData(String.format("the test wait %s second", timeout));
        return result;
    }
}
