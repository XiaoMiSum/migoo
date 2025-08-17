package io.github.xiaomisum.ryze.core.interceptor;

import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.TestElement;

import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes"})
public class HandlerExecutionChain<T extends TestElement<?>> {
    private final List<RyzeInterceptor> interceptors;
    private int executedIndex = -1;

    public HandlerExecutionChain(List<RyzeInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    // 前置处理
    public boolean applyPreHandle(ContextWrapper context, T runtime) {
        for (int i = 0; i < interceptors.size(); i++) {
            RyzeInterceptor interceptor = interceptors.get(i);
            if (!interceptor.preHandle(context, runtime)) {
                // 前置处理失败，触发已完成拦截器的afterCompletion
                triggerAfterCompletion(context, i - 1);
                return false;
            }
            executedIndex = i;
        }
        return true;
    }

    // 后置处理
    public void applyPostHandle(ContextWrapper context, T runtime) {
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            interceptors.get(i).postHandle(context, runtime);
        }
    }

    // 最终处理（无论成功失败都会执行）
    public void triggerAfterCompletion(ContextWrapper context) {
        for (int i = executedIndex; i >= 0; i--) {
            interceptors.get(i).afterCompletion(context);
        }
    }

    // 失败时的处理
    private void triggerAfterCompletion(ContextWrapper context, int untilIndex) {
        for (int i = untilIndex; i >= 0; i--) {
            interceptors.get(i).afterCompletion(context);
        }
    }
}