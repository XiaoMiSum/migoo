package xyz.migoo.functions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

/**
 * @author xiaomi
 * @date 2019/12/16 21:42
 */
public class Value extends AbstractFunction {

    @Override
    public Object execute(CompoundVariable parameters) throws ExecuteError {
        if (parameters.isEmpty()){
            throw new ExecuteError("parameters con not be null");
        }
        JSONObject object = parameters.getJSONObject("object") != null ?
                parameters.getJSONObject("object") : parameters.getJSONObject("json");
        if (object == null){
            throw new ExecuteError("object con not be null");
        }
        return object.get(parameters.getString("key"));
    }
}
