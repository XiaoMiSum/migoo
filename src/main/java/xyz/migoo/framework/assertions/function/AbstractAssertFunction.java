package xyz.migoo.framework.assertions.function;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.utils.StringUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:21
 */
public abstract class AbstractAssertFunction implements IFunction {


    private final static ThreadLocal<DecimalFormat> decimalFormatter =
            ThreadLocal.withInitial(AbstractAssertFunction::createDecimalFormat);

    private static DecimalFormat createDecimalFormat() {
        DecimalFormat decimalFormatter = new DecimalFormat("#.#");
        // java.text.DecimalFormat.DOUBLE_FRACTION_DIGITS == 340
        decimalFormatter.setMaximumFractionDigits(340);
        decimalFormatter.setMinimumFractionDigits(1);
        return decimalFormatter;
    }

    String objectToString(Object subj) {
        String str;
        if (subj == null || StringUtil.isEmpty(subj.toString())) {
            str = "null";
        } else if (subj instanceof List) {
            str = ((List) subj).isEmpty()?"null": JSONArray.toJSONString(subj);
        } else if (subj instanceof Map) {
            str = ((Map) subj).isEmpty()?"null": JSONObject.toJSONString(subj);
        } else if (subj instanceof Double || subj instanceof Float) {
            str = decimalFormatter.get().format(subj);
        } else {
            str = subj.toString();
        }
        return str;
    }

}
