package core.xyz.migoo.function;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.variable.MiGooVariables;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LsArgs extends ArrayList<Object> implements Args {

    private final MiGooVariables variables;

    public LsArgs(MiGooVariables variables) {
        this.variables = variables;
    }

    @Override
    public boolean add(Object parameter) {
        return super.add(parameter instanceof String s ? getParameterValue(s.trim()) : parameter);
    }

    public String getString(int index) {
        return size() <= index || super.get(index) == null ? "" : super.get(index).toString().trim();
    }

    public BigDecimal getNumber(int index) {
        return getString(index).isEmpty() ? null : new BigDecimal(getString(index));
    }

    public JSONObject getJSONObject(int index) {
        if (index > size()) {
            return null;
        }
        var value = get(index);
        return switch (value) {
            case null -> null;
            case String string -> StringUtils.isBlank(string) ? null : JSON.parseObject(string);
            case Map<?, ?> entry -> new JSONObject(entry);
            default -> JSON.parseObject(JSON.toJSONString(value));
        };
    }

    public JSONArray getJSONArray(int index) {
        if (index > size()) {
            return null;
        }
        var value = get(index);
        return switch (value) {
            case null -> null;
            case String string -> StringUtils.isBlank(string) ? null : JSON.parseArray(string);
            case List<?> objects -> new JSONArray(objects);
            default -> JSON.parseArray(JSON.toJSONString(value));
        };
    }

    public boolean getBooleanValue(int index) {
        String value = getString(index);
        return StringUtils.equalsAnyIgnoreCase(value, "1", "t", "true");
    }

    @Override
    public MiGooVariables getCurrentVars() {
        return this.variables;
    }
}
