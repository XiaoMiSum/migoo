package xyz.migoo.framework.functions;

import com.alibaba.fastjson.JSONObject;

/**
 * @author yacheng.xiao
 * @date 2019/11/18 15:36
 */
public interface Function {

    /**
     * execute function
     *
     * @param parameters function parameters
     * @return function execute result
     */
    Object execute(CompoundVariable parameters);

    /**
     * add parameter to function
     *
     * @param params function parameter: paramName=paramValue
     * @param variables test case defined variables
     */
    void addParameter(String params, JSONObject variables);
}
