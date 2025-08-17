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

package io.github.xiaomisum.ryze.core.testelement.configure;

import com.alibaba.fastjson2.annotation.JSONField;
import io.github.xiaomisum.ryze.core.Result;
import io.github.xiaomisum.ryze.core.config.ConfigureItem;
import io.github.xiaomisum.ryze.core.context.ContextWrapper;
import io.github.xiaomisum.ryze.core.testelement.AbstractTestElement;

/**
 * @author xiaomi
 * Created at 2025/7/19 15:47
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractConfigureElement<SELF extends AbstractConfigureElement<SELF, CONFIG, R>, CONFIG extends ConfigureItem<CONFIG>, R extends Result>
        extends AbstractTestElement<AbstractConfigureElement<SELF, CONFIG, R>, CONFIG, R>
        implements ConfigureElement<R>, ConfigureElementConstantsInterface {

    @JSONField(name = REF_NAME, ordinal = 1)
    protected String refName;

    public AbstractConfigureElement(Builder<SELF, ?, CONFIG, ?, R> builder) {
        super(builder);
        this.refName = builder.refName;
    }

    public AbstractConfigureElement() {
    }


    public R process(ContextWrapper context) {
        if (!initialized) {
            initialized();
        }
        runtime.config = (CONFIG) context.evaluate(runtime.config);
        var result = getTestResult();
        doProcess(context);
        return result;
    }


    protected abstract void doProcess(ContextWrapper context);

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public static abstract class Builder<ELE extends AbstractConfigureElement<ELE, CONFIG, R>,
            SELF extends Builder<ELE, SELF, CONFIG, CONFIGURE_BUILDER, R>,
            CONFIG extends ConfigureItem<CONFIG>,
            CONFIGURE_BUILDER extends ConfigureBuilder<?, CONFIG>,
            R extends Result>
            extends AbstractTestElement.Builder<AbstractConfigureElement<ELE, CONFIG, R>, SELF, CONFIG, CONFIGURE_BUILDER, R> {

        protected String refName;

        public SELF refName(String refName) {
            this.refName = refName;
            return self;
        }
    }
}
