/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package io.github.xiaomisum.ryze.core.template.freemarker;

import freemarker.template.*;

import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * ObjectWrapper 对象包装为 TemplateModel 类型。
 */
@SuppressWarnings("rawtypes")
public class RyzeObjectWrapper extends DefaultObjectWrapper {

    public RyzeObjectWrapper(Version incompatibleImprovements) {
        super(incompatibleImprovements);
    }

    public TemplateModel wrap(Object obj) throws TemplateModelException {
        return switch (obj) {
            case null -> super.wrap(null);
            case TemplateModel templateModel -> templateModel;
            case String s -> new SimpleScalar(s);
            case Number number -> new SimpleNumber(number);
            case java.sql.Date date -> new SimpleDate(date);
            case java.sql.Time time -> new SimpleDate(time);
            case java.sql.Timestamp timestamp -> new SimpleDate(timestamp);
            case Date date -> new SimpleDate(date, getDefaultDateType());
            case Map map -> DefaultMapAdapter.adapt(map, this);
            case Boolean b -> b ? TemplateBooleanModel.TRUE : TemplateBooleanModel.FALSE;
            case Iterator iterator -> DefaultIteratorAdapter.adapt(iterator, this);
            case Enumeration enumeration -> DefaultEnumerationAdapter.adapt(enumeration, this);
            default -> {
                Class<?> objClass = obj.getClass();
                yield objClass.isArray() ? DefaultArrayAdapter.adapt(obj, this) : handleUnknownType(obj);
            }
        };
    }

}
