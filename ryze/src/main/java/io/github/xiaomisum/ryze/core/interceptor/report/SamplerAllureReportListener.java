package io.github.xiaomisum.ryze.core.interceptor.report;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.sampler.AbstractSampler;
import io.github.xiaomisum.ryze.core.testelement.sampler.Sampler;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SamplerAllureReportListener implements AllureReportListener {

    @Override
    public boolean supports(ContextWrapper context) {
        return context.getTestElement() instanceof Sampler<?>;
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE + 1;
    }

    @Override
    public void afterCompletion(ContextWrapper context) {
        var handler = context.getTestElement();
        if (handler instanceof AbstractSampler<?, ?, ?> sampler) {
            AllureReportListener.startStep(sampler.getRuntime().getTitle(), context);
        } else if (handler instanceof Sampler<?> sampler) {
            AllureReportListener.startStep("匿名取样器：" + sampler.getClass().getSimpleName(), context);
        }
        AllureReportListener.stopStep(context);
    }
}
