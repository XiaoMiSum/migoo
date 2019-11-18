package xyz.migoo.framework.functions;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author yacheng.xiao
 * @date 2019/11/18 15:47
 */
public class CompoundVariable extends HashMap<String, Object> {
    private static final long serialVersionUID = 362498820763181265L;

    private static final Pattern FUNC_PATTERN = Pattern.compile("^__(\\w+)\\((.*)\\)");
    private static final Pattern PARAM_PATTERN = Pattern.compile("(\\$\\{(\\w+)})+");
    private static final Pattern REGEX_INTEGER = Pattern.compile("^[-\\+]?[0-9]+$");
    private static final Pattern REGEX_FLOAT = Pattern.compile("^[-\\+]?[0-9]+\\.[0-9]+$");

    public Object put(String parameter, JSONObject variables) {
        String[] array = parameter.split("=");
        return super.put(array[0], parseParameter(array[1], variables));
    }

    private Object parseParameter(String parameter, JSONObject variables){
        if (REGEX_INTEGER.matcher(parameter).find() || REGEX_FLOAT.matcher(parameter).find()){
            return  new BigDecimal(parameter);
        } else if (Boolean.TRUE.toString().equalsIgnoreCase(parameter) ||
                Boolean.FALSE.toString().equalsIgnoreCase(parameter)){
            return  Boolean.valueOf(parameter);
        } else if (PARAM_PATTERN.matcher(parameter).find()){
            if (variables == null || variables.isEmpty()){
                throw new RuntimeException("variables is null or empty");
            }
            Object object = variables.get(parameter.substring(2, parameter.length() -1));
            if (FUNC_PATTERN.matcher(String.valueOf(object)).find()
                    || PARAM_PATTERN.matcher(String.valueOf(object)).find()){
                throw new RuntimeException(String.format("%s need eval!", parameter));
            }
            return object;
        } else {
            return parameter;
        }
    }

    public String getAsString(String key){
        return super.get(key) == null ? "" : super.get(key).toString();
    }

    public Long getAsLong(String key){
        return super.get(key) == null ? null : Long.valueOf(getAsString(key));
    }

    public Integer getAsInteger(String key){
        return super.get(key) == null ? null : Integer.valueOf(getAsString(key));
    }

    public Double getAsDouble(String key){
        return super.get(key) == null ? null : Double.valueOf(getAsString(key));
    }

    public Float getAsFloat(String key){
        return super.get(key) == null ? null : Float.valueOf(getAsString(key));
    }

    public BigDecimal getAsBigDecimal(String key){
        return super.get(key) == null ? null : new BigDecimal(getAsString(key));
    }
}
