package xyz.migoo.functions;

import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Locale;

/**
 * @author xiaomi
 * @date 2019/11/18 20:54
 */
public class TimeShift extends AbstractFunction {

    private ZoneId systemDefaultZoneID = ZoneId.systemDefault();

    @Override
    public String execute(CompoundVariable parameters) {
        String format = parameters.getAsString("format").trim();
        LocalDateTime localDateTimeToShift = LocalDateTime.now(systemDefaultZoneID);
        DateTimeFormatter dateTimeFormatter = null;
        if (!format.isEmpty()){
            dateTimeFormatter = createFormatter(format, Locale.getDefault());
        }
        String amount = parameters.getAsString("amount").trim();
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
