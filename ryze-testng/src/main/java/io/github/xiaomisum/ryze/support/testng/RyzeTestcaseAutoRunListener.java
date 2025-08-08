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

package io.github.xiaomisum.ryze.support.testng;

import io.github.xiaomisum.ryze.core.SessionRunner;
import io.github.xiaomisum.ryze.core.testelement.TestElement;
import io.github.xiaomisum.ryze.support.testng.annotation.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

import java.util.Objects;

/**
 * 自动运行 RyzeTestNGTestcase，
 * <p>
 * 凡继承此类的测试类，若测试用例的参数为 TestElement，则自动运行
 *
 * @author xiaomi
 */
public class RyzeTestcaseAutoRunListener implements IHookable {

    static Logger logger = LoggerFactory.getLogger(RyzeTestcaseAutoRunListener.class);

    @Override
    public void run(IHookCallBack iHookCallBack, ITestResult iTestResult) {
        logger.debug("IHookable run test: {}", iTestResult.getMethod().getMethodName());
        if (!AnnotationUtils.isRyzeTest(iTestResult.getMethod().getConstructorOrMethod().getMethod())) {
            logger.debug("Method 不是Ryze注解测试，执行原始测试");
            iHookCallBack.runTestMethod(iTestResult);
            return;
        }
        var parameters = iHookCallBack.getParameters();
        if (Objects.isNull(parameters) || parameters.length < 1 || !TestElement.class.isAssignableFrom(parameters[0].getClass())) {
            logger.debug("parameters 不符合自动运行条件，执行原始测试");
            iHookCallBack.runTestMethod(iTestResult);
            return;
        }
        logger.debug("自动执行 Ryze TestElement");
        var result = SessionRunner.getSession().runTest((TestElement<?>) parameters[0]);
        iTestResult.setStatus(result.getStatus().isFailed() ? ITestResult.FAILURE : result.getStatus().isSkipped() ? ITestResult.SKIP :
                result.getStatus().isPassed() ? ITestResult.SUCCESS : ITestResult.CREATED
        );
    }
}
