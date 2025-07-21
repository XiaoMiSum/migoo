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

package core.xyz.migoo.testelement.configure;

import com.alibaba.fastjson2.annotation.JSONField;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.report.Result;
import core.xyz.migoo.testelement.AbstractTestElement;

/**
 * @author xiaomi
 * Created at 2025/7/19 15:47
 */
public abstract class AbstractConfigureElement<CONFIG extends ConfigureItem<CONFIG>, SELF extends AbstractConfigureElement<CONFIG, SELF, T>, T extends Result>
        extends AbstractTestElement<CONFIG, AbstractConfigureElement<CONFIG, SELF, T>, T>
        implements ConfigureElement<T>, ConfigureElementConstantsInterface {

    @JSONField(name = REF_NAME, ordinal = 1)
    protected String refName;

    @JSONField(name = DATASOURCE, ordinal = 2)
    protected String datasource;

    public AbstractConfigureElement() {
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }
}
