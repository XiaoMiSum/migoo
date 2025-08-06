package io.github.xiaomisum.ryze.core.interceptor.report;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.interceptor.Handler;
import io.github.xiaomisum.ryze.core.testelement.processor.AbstractProcessor;
import io.github.xiaomisum.ryze.core.testelement.processor.Processor;
import org.apache.commons.lang3.StringUtils;

public class ProcessorAllureReportListener implements AllureReportListener {

    @Override
    public boolean match(ContextWrapper context) {
        return context.getTestElement() instanceof Processor;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }

    @Override
    public void preHandle(ContextWrapper context, Handler handler) {
        if (handler instanceof AbstractProcessor<?, ?, ?> processor) {
            AllureReportListener.step(() -> StringUtils.isNotBlank(processor.getRuntime().getTitle()) ? processor.getRuntime().getTitle()
                    : "匿名处理器：" + processor.getClass().getSimpleName(), uuid -> handler.doHandle(context), context);
        } else if (handler instanceof Processor processor) {
            AllureReportListener.step(() -> "匿名处理器：" + processor.getClass().getSimpleName(), uuid -> handler.doHandle(context), context);
        }
    }
}
