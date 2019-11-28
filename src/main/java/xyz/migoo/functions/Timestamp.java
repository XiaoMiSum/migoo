package xyz.migoo.functions;

import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;
import xyz.migoo.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaomi
 * @date 2019/11/18 20:54
 */
public class Timestamp extends AbstractFunction {

    @Override
    public String execute(CompoundVariable parameters) {
        String pattern = parameters.getAsString("format") == null ?
                                    parameters.getAsString("pattern") : parameters.getAsString("format") ;
        if (!StringUtil.isEmpty(pattern)){
            SimpleDateFormat s = new SimpleDateFormat(pattern);
            return s.format(new Date());
        }
        return System.currentTimeMillis() + "";
    }
}
