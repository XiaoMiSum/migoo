package io.github.xiaomisum.ryze.core.interceptor.report;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.interceptor.Handler;
import io.github.xiaomisum.ryze.core.testelement.sampler.AbstractSampler;
import io.github.xiaomisum.ryze.core.testelement.sampler.Sampler;

public class SamplerAllureReportListener implements AllureReportListener {

    @Override
    public boolean match(ContextWrapper context) {
        return context.getTestElement() instanceof Sampler<?>;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void preHandle(ContextWrapper context, Handler handler) {
        if (handler instanceof AbstractSampler<?, ?, ?> sampler) {
            AllureReportListener.step(() -> sampler.getRuntime().getTitle(), uuid -> handler.doHandle(context), context);
        } else if (handler instanceof Sampler<?> sampler) {
            AllureReportListener.step(() -> "匿名取样器：" + sampler.getClass().getSimpleName(), uuid -> handler.doHandle(context), context);
        }
    }
}
