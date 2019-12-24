/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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


package xyz.migoo.functions;

import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * @author xiaomi
 * @date 2019/11/18 20:54
 */
public class TimeShift extends AbstractFunction {

    private ZoneId systemDefaultZoneId = ZoneId.systemDefault();

    @Override
    public String execute(CompoundVariable parameters) throws ExecuteError {
        if (parameters.isEmpty()){
            throw new ExecuteError("parameters con not be null");
        }
        String format = parameters.getString("format").trim();
        LocalDateTime localDateTimeToShift = LocalDateTime.now(systemDefaultZoneId);
        DateTimeFormatter dateTimeFormatter = null;
        if (!format.isEmpty()){
            dateTimeFormatter = createFormatter(format, Locale.getDefault());
        }
        String amount = parameters.getString("amount").trim();
        if (!amount.isEmpty()){
            Duration duration = Duration.parse(amount);
            localDateTimeToShift = localDateTimeToShift.plus(duration);
        }
        if (dateTimeFormatter != null){
            return localDateTimeToShift.format(dateTimeFormatter);
        }
        ZoneOffset offset = ZoneOffset.systemDefault().getRules().getOffset(localDateTimeToShift);
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
