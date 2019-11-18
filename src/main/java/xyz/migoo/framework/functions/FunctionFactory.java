package xyz.migoo.framework.functions;

import xyz.migoo.exception.MiGooException;
import xyz.migoo.utils.StringUtil;

/**
 * @author yacheng.xiao
 * @date 2019/11/18 17:10
 */
public class FunctionFactory {

    public static AbstractFunction getFunction(String name){
        try {
            return (AbstractFunction) Class.forName("xyz.migoo.functions." + StringUtil.indexToUpperCase(name))
                    .newInstance();
        } catch (Exception e) {
            throw new MiGooException("init function error: " + name, e);
        }
    }
}
