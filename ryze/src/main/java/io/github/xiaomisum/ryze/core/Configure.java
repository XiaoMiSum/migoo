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

package io.github.xiaomisum.ryze.core;

import io.github.xiaomisum.ryze.core.config.GlobalConfigure;
import io.github.xiaomisum.ryze.core.config.InterceptorConfigureItem;
import io.github.xiaomisum.ryze.core.context.GlobalContext;
import io.github.xiaomisum.ryze.core.interceptor.RyzeInterceptor;
import io.github.xiaomisum.ryze.core.interceptor.report.AllureReportListener;
import io.github.xiaomisum.ryze.core.template.TemplateEngine;
import io.github.xiaomisum.ryze.core.template.freemarker.FreeMarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

import static io.github.xiaomisum.ryze.core.testelement.TestElementConstantsInterface.INTERCEPTORS;

@SuppressWarnings({"rawtypes"})
public class Configure {

    private static final String RYZE_ENABLE_ALLURE_REPORT = "ryze.report.allure";

    private final boolean enableAllureReport;

    /**
     * 模板引擎，可指定使用自定义模板引擎
     */
    private TemplateEngine templateEngine;

    /**
     * 内置拦截器，主要是测试报告和执行日志拦截器
     */
    private InterceptorConfigureItem<RyzeInterceptor> builtinRyzeInterceptors;

    private GlobalContext globalContext;

    public Configure() {
        // 从系统参数（命令行参数-Dryze.report.allure）中获取配置，默认开启Allure报告
        this(Boolean.parseBoolean(System.getProperty(RYZE_ENABLE_ALLURE_REPORT, "true")));
    }

    public Configure(boolean enableAllureReport) {
        this.enableAllureReport = enableAllureReport;
    }

    public static Configure defaultConfigure() {
        var configure = new Configure();
        configure.setTemplateEngine(new FreeMarkerTemplateEngine());
        var interceptors = new ArrayList<>(ApplicationConfig.getReporterListeners());
        if (!configure.isEnableAllureReport()) {
            interceptors.removeIf(AllureReportListener.class::isInstance);
        }
        InterceptorConfigureItem<RyzeInterceptor> items = new InterceptorConfigureItem<>();
        items.addAll(interceptors);
        configure.setBuiltinInterceptors(items);
        var globalContext = new GlobalContext(new GlobalConfigure());
        // todo 全局配置可以考虑固定位置存放全局变量
        globalContext.getConfigGroup().put(INTERCEPTORS, items);
        configure.setGlobalContext(globalContext);
        return configure;
    }

    public boolean isEnableAllureReport() {
        return enableAllureReport;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public List<RyzeInterceptor> getBuiltinInterceptors() {
        return builtinRyzeInterceptors;
    }

    public void setBuiltinInterceptors(InterceptorConfigureItem<RyzeInterceptor> builtinRyzeInterceptors) {
        this.builtinRyzeInterceptors = builtinRyzeInterceptors;
    }

    public GlobalContext getGlobalContext() {
        return globalContext;
    }

    public void setGlobalContext(GlobalContext globalContext) {
        this.globalContext = globalContext;
    }
}
