package xyz.migoo.functions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

/**
 * @author xiaomi
 * @date 2019/11/18 17:22
 */
public class Json extends AbstractFunction {

    @Override
    public JSONObject execute(CompoundVariable parameters) throws ExecuteError {
        if (parameters.isEmpty()){
            throw new ExecuteError("parameters con not be null");
        }
        JSONObject json = new JSONObject();
        parameters.forEach(json::put);
        return json;
    }
}
