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

import components.xyz.migoo.reports.Report;

import java.util.Date;

/**
 * @author xiaomi
 * @date 2020/7/26 16:24
 */
public class TestSet extends Test {

    TestSet(TestContext context, TestContext superSuite) {
        super(context);
        super.mergeRequest(superSuite);
        context.getCases().forEach(testCase -> super.addTest(new TestCase(testCase, context)));
    }

    @Override
    public IResult run() {
        Report.log("===================================================================");
        Report.log("Beginning of the test，api：{}", this.getTestName());
        return super.run();
    }

    @Override
    public void setup() throws Exception {
        this.startTime = new Date();
        if (!isSkipped()) {
            this.processVariable();
            super.setup();
        }
    }
}
