package xyz.migoo.framework.functions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ExtenderException;
import xyz.migoo.utils.StringUtil;

/**
 * @author yacheng.xiao
 * @date 2019/11/18 17:10
 */
public class FunctionFactory extends AbstractFunction{

    private final static FunctionFactory FACTORY = new FunctionFactory();

    public static Object execute(String name, String parameter, JSONObject variables) throws ExtenderException {
        FACTORY.getFunction(name);
        FACTORY.addParameter(parameter, variables);
        return FACTORY.execute(null);
    }

    private AbstractFunction function;

    private void getFunction(String name) throws ExtenderException {
        try {
            function = (AbstractFunction) Class.forName("xyz.migoo.functions." + StringUtil.initialToUpperCase(name))
                    .newInstance();
        } catch (Exception e) {
            throw new ExtenderException("get functions error. ", e);
        }
    }

    @Override
    public Object execute(CompoundVariable parameters) throws ExtenderException {
        return function.execute();
    }

    @Override
    public void addParameter(String parameters, JSONObject variables) {
        function.addParameter(parameters, variables);
    }
}
