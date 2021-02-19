/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2021. Lorem XiaoMiSum (mi_xiao@qq.com)
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
 */

package function.xyz.migoo;

import core.xyz.migoo.function.CompoundParameter;
import core.xyz.migoo.function.Function;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author mi.xiao
 */
public class Timestamp implements Function {

    /**
     * 获取当前时间，支持一个参数
     * 参数：
     *      format: 指定日期时间格式，如：yyyy-MM-dd、yyyy-MM-dd HH:mm:ss、yyyyMMdd、yyyyMMddHHmmss
     *
     * 当格式化参数未传递时，则返回当前时间戳
     */
    @Override
    public String execute(CompoundParameter parameters) throws Exception {
        String pattern = parameters.isNullKey("format") ?
                parameters.getString("pattern") : parameters.getString("format");
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.systemDefault());
        if (!StringUtils.isEmpty(pattern)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return localDateTime.format(formatter);
        }
        ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(localDateTime);
        return String.valueOf(localDateTime.toInstant(offset).toEpochMilli());
    }
}