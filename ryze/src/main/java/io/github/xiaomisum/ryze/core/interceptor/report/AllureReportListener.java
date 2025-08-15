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

package io.github.xiaomisum.ryze.core.interceptor.report;

import io.github.xiaomisum.ryze.core.TestStatus;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.util.Objects;
import java.util.function.Supplier;

import static io.qameta.allure.Allure.getLifecycle;
import static io.qameta.allure.util.ResultsUtils.getStatus;
import static io.qameta.allure.util.ResultsUtils.getStatusDetails;

/**
 * Allure 测试报告监听器
 *
 * @author xiaomi
 * Created at 2025/7/20 14:15
 */
public interface AllureReportListener<T extends AbstractTestElement<?, ?, ?>> extends ReporterListener<T> {

    /**
     * 功能上相当于 <code>Allure.step(...)</code>
     *
     * @param name 步骤名称
     */
    static void startStep(Supplier<String> name, ContextWrapper context) {
        String uuid = context.getUuid();
        getLifecycle().startStep(uuid, new StepResult().setName(name.get()));
        if (Objects.isNull(context.getTestResult().getThrowable())) {
            getLifecycle().updateStep(uuid, step -> step
                    .setName(name.get())
                    .setStatus(context.getTestResult().getStatus().getAllureStatus()));
        } else {
            getLifecycle().updateStep(uuid, step -> step
                    .setStatus(getStatus(context.getTestResult().getThrowable()).orElse(Status.BROKEN))
                    .setStatusDetails(getStatusDetails(context.getTestResult().getThrowable()).orElse(null)));
            context.getTestResult().setStatus(TestStatus.failed);
        }
    }

    static void stopStep(ContextWrapper context) {
        getLifecycle().stopStep(context.getUuid());
    }
}
