/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package function.xyz.migoo;

import core.xyz.migoo.function.Args;
import core.xyz.migoo.function.Function;
import core.xyz.migoo.function.KwArgs;
import core.xyz.migoo.function.LsArgs;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * @author mi.xiao
 */
public class TimeShift implements Function {

    private final ZoneId systemDefaultZoneId = ZoneId.systemDefault();

    /**
     * 对时间进行操作，支持两个参数，且两个参数都允许为空
     * 参数：
     * format: 指定日期时间格式，如果该参数未传递，则返回时间戳
     * amount: 表示要从日期参数的值中添加或减去多少天，几小时或几分钟。如果该值未被传递，则不会将任何值减去或添加到日期参数的值中。
     * PT20.345S: 解析为 20.345秒
     * PT15M: 解析为 15分钟
     * PT10H: 解析为 10小时
     * P2D: 解析为 2天
     * P2DT3H4M: 解析为 2天3小时4分钟
     * P-6H3M: 解析为 -6小时+3分钟
     * -P6H3M: 解析为 -6小时-3分钟
     * -P-6H+3M: 解析为 +6小时-3分钟
     * <p>
     * 当两个参数都未传递时，则返回当前时间戳
     */
    @Override
    public String execute(Args args) {
        return args instanceof KwArgs kwArgs ? execute(kwArgs) : execute((LsArgs) args);
    }

    private String execute(KwArgs args) {
        return execute(args.getString("format"), args.getString("amount"));
    }

    private String execute(LsArgs args) {
        return execute(args.getString(0), args.getString(1));
    }

    private String execute(String format, String amount) {
        var localDateTimeToShift = LocalDateTime.now(systemDefaultZoneId);
        DateTimeFormatter dateTimeFormatter = null;
        if (!StringUtils.isBlank(format)) {
            dateTimeFormatter = createFormatter(format, Locale.getDefault());
        }
        if (!StringUtils.isBlank(amount)) {
            localDateTimeToShift = localDateTimeToShift.plus(Duration.parse(amount));
        }
        if (dateTimeFormatter != null) {
            return localDateTimeToShift.format(dateTimeFormatter);
        }
        var offset = ZoneOffset.systemDefault().getRules().getOffset(localDateTimeToShift);
        return String.valueOf(localDateTimeToShift.toInstant(offset).toEpochMilli());
    }

    private DateTimeFormatter createFormatter(String format, Locale locale) {
        return new DateTimeFormatterBuilder().appendPattern(format)
                .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
                .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.YEAR_OF_ERA, Year.now().getValue())
                .toFormatter(locale);

    }
}