package xyz.migoo.framework.functions;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.MiGooException;
import xyz.migoo.utils.StringUtil;

/**
 * @author yacheng.xiao
 * @date 2019/11/18 17:10
 */
public class FunctionFactory extends AbstractFunction{

    private final static FunctionFactory FACTORY = new FunctionFactory();

    public static Object execute(String name, String parameter, JSONObject variables) throws MiGooException {
        FACTORY.getFunction(name);
        FACTORY.addParameter(parameter, variables);
        return FACTORY.execute(null);
    }

    private AbstractFunction function;

    private void getFunction(String name){
        try {
            function = (AbstractFunction) Class.forName("xyz.migoo.functions." + StringUtil.indexToUpperCase(name))
                    .newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object execute(CompoundVariable parameters) {
        return function.execute();
    }

    @Override
    public void addParameter(String parameters, JSONObject variables) {
        function.addParameter(parameters, variables);
    }
}
