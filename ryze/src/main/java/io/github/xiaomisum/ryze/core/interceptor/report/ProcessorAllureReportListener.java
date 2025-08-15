package io.github.xiaomisum.ryze.core.interceptor.report;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.processor.AbstractProcessor;
import io.github.xiaomisum.ryze.core.testelement.processor.Processor;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ProcessorAllureReportListener implements AllureReportListener {

    @Override
    public boolean supports(ContextWrapper context) {
        return context.getTestElement() instanceof Processor;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 1;
    }

    @Override
    public void afterCompletion(ContextWrapper context) {
        var handler = context.getTestElement();
        if (handler instanceof AbstractProcessor<?, ?, ?> processor) {
            AllureReportListener.startStep(StringUtils.isNotBlank(processor.getRuntime().getTitle()) ? processor.getRuntime().getTitle()
                    : "匿名处理器：" + processor.getClass().getSimpleName(), context);
        } else if (handler instanceof Processor processor) {
            AllureReportListener.startStep("匿名处理器：" + processor.getClass().getSimpleName(), context);
        }
        AllureReportListener.stopStep(context);
    }
}
