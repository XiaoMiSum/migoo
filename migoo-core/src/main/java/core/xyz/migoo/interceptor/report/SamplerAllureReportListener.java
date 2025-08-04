package core.xyz.migoo.interceptor.report;

import core.xyz.migoo.context.ContextWrapper;
import core.xyz.migoo.interceptor.Handler;
import core.xyz.migoo.testelement.sampler.AbstractSampler;
import core.xyz.migoo.testelement.sampler.Sampler;

public class SamplerAllureReportListener implements AllureReportListener {

    @Override
    public boolean match(ContextWrapper context) {
        return context.getTestElement() instanceof Sampler<?>;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE - 1;
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
