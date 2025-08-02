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

package testng.xyz.migoo.support;

import core.xyz.migoo.SessionRunner;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import testng.xyz.migoo.support.annotation.AnnotationUtils;

public abstract class BasicMiGooTestNgTestcase {

    /**
     * 在测试方法执行前执行，用于创建 migoo 运行环境
     *
     * @param testResult testng
     */
    @BeforeMethod(alwaysRun = true)
    public void createSessionAndDestroySession(ITestResult testResult) {
        if (!AnnotationUtils.isMiGooTest(testResult.getMethod().getConstructorOrMethod().getMethod())) {
            return;
        }
        // 创建一个 在测试框架中运行时使用的 session
        SessionRunner.newTestFrameworkSession();
    }

    /**
     * 在测试方法执行前执行，用于创建 migoo 运行环境
     *
     * @param testResult testng
     */
    @AfterMethod(alwaysRun = true)
    public void stopSessionAndRemove(ITestResult testResult) {
        SessionRunner.removeSession();
    }

}
