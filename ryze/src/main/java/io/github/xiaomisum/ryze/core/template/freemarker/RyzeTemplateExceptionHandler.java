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

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.Writer;

/**
 * 自定义的 TemplateException 处理器。
 */
public class RyzeTemplateExceptionHandler implements TemplateExceptionHandler {

    @Override
    public void handleTemplateException(TemplateException te, Environment env, Writer out) throws TemplateException {
        StringBuilder builder = new StringBuilder();
        String template = env.getCurrentTemplate().toString();
        if (template.contains(FreeMarkerTemplateEngine.TemplateObjectHandler.NAME)) {
            template = String.format("${%s}", template.substring(FreeMarkerTemplateEngine.TemplateObjectHandler.NAME.length() + 3, template.length() - 2));
        }
        builder.append("\n")
                .append(te.getMessageWithoutStackTop())
                .append("\nryze-freemarker-template：")
                .append(template);
        throw new RuntimeException(builder.toString(), te);
    }

}
