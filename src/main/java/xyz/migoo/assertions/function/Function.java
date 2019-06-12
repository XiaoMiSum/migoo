package xyz.migoo.assertions.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.utils.StringUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2018/7/28 10:50
 */
public class Function {

    private static ThreadLocal<DecimalFormat> decimalFormatter =
            ThreadLocal.withInitial(Function::createDecimalFormat);

    private static DecimalFormat createDecimalFormat() {
        DecimalFormat decimalFormatter = new DecimalFormat("#.#");
        // java.text.DecimalFormat.DOUBLE_FRACTION_DIGITS == 340
        decimalFormatter.setMaximumFractionDigits(340);
        decimalFormatter.setMinimumFractionDigits(1);
        return decimalFormatter;
    }
    private static String objectToString(Object subj) {
        String str;
        if (subj == null || StringUtil.isBlank(subj.toString())) {
            str = "null";
        } else if (subj instanceof List){
            if (((List) subj).isEmpty()){
                str = "null";
            }else {
                str = JSONArray.toJSONString(subj);
            }
        } else if (subj instanceof Map) {
            //noinspection unchecked
            if (((Map) subj).isEmpty()){
                str = "null";
            }else {
                str = JSONArray.toJSONString(subj);
            }
        } else if (subj instanceof Double || subj instanceof Float) {
            str = decimalFormatter.get().format(subj);
        } else {
            str = subj.toString();
        }
        return str;
    }

    /**
     * 验证  actual == expect
     *
     * @param actual  实际值
     * @param expect  期望值
     * @return  验证结果 true / false
     */
    public static boolean equals(Object actual, Object expect) {
        String str1 = objectToString(actual);
        String str2 = objectToString(expect);
        return str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(Object actual, Object expect) {
        String str1 = objectToString(actual);
        String str2 = objectToString(expect);
        return str1.equalsIgnoreCase(str2);
    }

    public static boolean isNotEquals(Object actual, Object expect){
        return !equals(actual, expect);
    }

    /**
     * Compare sizes,
     * returning true if the actual value is greater than or equals expected, or false otherwise
     * @param actual actual value
     * @param expect expect value
     * @return true or false
     */
    public static boolean greaterThanOrEquals(Object actual, Object expect){
        try {
            BigDecimal b1 = new BigDecimal(String.valueOf(actual));
            BigDecimal b2 = new BigDecimal(String.valueOf(expect));
            return b1.compareTo(b2) >= 0;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean greaterThan(Object actual, Object expect){
        try {
            BigDecimal b1 = new BigDecimal(String.valueOf(actual));
            BigDecimal b2 = new BigDecimal(String.valueOf(expect));
            return b1.compareTo(b2) > 0;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Compare sizes,
     * returning true if the actual value is less than or equals expected, or false otherwise
     * @param actual actual value
     * @param expect expect value
     * @return true or false
     */
    public static boolean lessThanOrEquals(Object actual, Object expect){
        try {
            BigDecimal b1 = new BigDecimal(String.valueOf(actual));
            BigDecimal b2 = new BigDecimal(String.valueOf(expect));
            return b1.compareTo(b2) <= 0;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean lessThan(Object actual, Object expect){
        try {
            BigDecimal b1 = new BigDecimal(String.valueOf(actual));
            BigDecimal b2 = new BigDecimal(String.valueOf(expect));
            return b1.compareTo(b2) < 0;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 验证 actual.contains(expect)
     * 只支持 string\map\list
     *
     * @param actual  实际值
     * @param expect  期望值
     * @return  验证结果 true / false
     */
    public static boolean contains(Object actual, Object expect) {
        if (actual instanceof String) {
            return StringUtil.contains((String) actual, (String) expect);
        }
        if (actual instanceof Map) {
            Map json = (Map) actual;
            return json.containsValue(expect) || json.containsKey(expect);
        }
        if (actual instanceof List) {
            return ((List) actual).contains(expect);
        }
        return false;
    }

    public static boolean doesNotContains(Object actual, Object expect){
        return !contains(actual, expect);
    }

    /**
     * 验证 actual 是 空对象 或 空字符串
     *
     * @param actual  实际值
     * @param expect  期望值（未使用，可为任意对象）
     * @return  验证结果 true / false
     */
    public static boolean isEmpty(Object actual, Object expect) {
        if (actual instanceof JSONObject) {
            return ((JSONObject) actual).isEmpty();
        }
        if (actual instanceof JSONArray) {
            return ((JSONArray) actual).isEmpty();
        }
        if (actual instanceof String) {
            return StringUtil.isBlank((String) actual);
        }
        return actual == null;
    }

    /**
     * 验证 actual 不是 空对象 或 空字符串
     *
     * @param actual  实际值
     * @param expect  期望值（未使用，可为任意对象）
     * @return  验证结果 true / false
     */
    public static boolean isNotEmpty(Object actual, Object expect) {
        return !isEmpty(actual, expect);
    }

    /**
     * 正则表达式 验证器
     * @param actual   待验证的实际值
     * @param expect   正则表达式
     * @return 验证结果 true / false
     */
    public static boolean regex(Object actual, Object expect) {
        String str = "";
        if (actual instanceof JSON){
            str = ((JSON) actual).toJSONString();
        }
        if (actual instanceof Number){
            str = String.valueOf(actual);
        }
        if (actual instanceof String){
            str = actual.toString();
        }
        Pattern pattern = Pattern.compile(expect.toString());
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static boolean custom(String clazz, Object expect){
        boolean result = Boolean.FALSE;
        try {
            IFunction func = (IFunction)Class.forName(clazz).newInstance();
            Map<String, Object> data = new HashMap<>(2);
            data.put("expect", expect);
            result = func.assertThat(data);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return result;
    }
}
