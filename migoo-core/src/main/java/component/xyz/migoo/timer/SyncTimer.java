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

package component.xyz.migoo.timer;

import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.processor.AbstractProcessor;
import core.xyz.migoo.processor.Postprocessor;
import core.xyz.migoo.testelement.Alias;

/**
 * @author mi.xiao
 * @date 2021/4/7 19:49
 */
@Alias({"SyncTimer", "Timer", "sync_timer", "def_timer", "defTimer"})
public class SyncTimer extends AbstractProcessor implements Postprocessor {

    public static final String TIMEOUT = "timeout";


    @Override
    public void process(ContextWrapper context) {
        context.getConfigGroup().get(TIMEOUT);
        // todo 这里要获取配置
        /*if (timeout > 0) {
            synchronized (this) {
                try {
                    this.wait(timeout);
                } catch (InterruptedException e) {
                    this.notify();
                }
            }
        }
        */
    }

    public static class TimerConfigureItem implements ConfigureItem<TimerConfigureItem> {

        private int timeout;

        @Override
        public TimerConfigureItem merge(TimerConfigureItem other) {
            // 无需合并，返回当前对象的拷贝
            return copy();
        }

        @Override
        public TimerConfigureItem copy() {
            var res = new TimerConfigureItem();
            res.timeout = timeout;
            return res;
        }

        public int getTimeout() {
            return timeout;
        }
    }
}
