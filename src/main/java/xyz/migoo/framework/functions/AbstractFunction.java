package xyz.migoo.framework.functions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExtenderException;

/**
 * @author yacheng.xiao
 * @date 2019/11/18 16:54
 */
public abstract class AbstractFunction implements Function{

    private CompoundVariable parameters = new CompoundVariable();

    public Object execute() throws ExtenderException {
        return execute(parameters);
    }

    /**
     * execute function
     *
     * @param parameters function parameters
     * @return function execute result
     * @throws ExtenderException
     */
    @Override
    public abstract Object execute(CompoundVariable parameters) throws ExtenderException;

    @Override
    public void addParameter(String params, JSONObject variables){
        if (!params.isEmpty()) {
            String[] array = params.split(",");
            for (String str : array) {
                parameters.put(str, variables);
            }
        }
    }

    public String getParametersAsString(){
        return JSONObject.toJSONString(parameters);
    }

    /**
     * @return this function name
     */
    public String getFunctionName(){
        return this.getClass().getName();
    }
}
