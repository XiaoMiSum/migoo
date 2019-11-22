package xyz.migoo.functions;

import xyz.migoo.exception.ExtenderException;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

/**
 * HOOK 扩展
 * @author xiaomi
 * @date 2018/8/6 14:41
 */
public class Hook2 extends AbstractFunction {

    @Override
    public Object execute(CompoundVariable parameters) throws ExtenderException {
        if (parameters.size() > 2){
            throw new ExtenderException("after exception");
        }
        System.out.println(parameters.getAsString("A") + ": " + parameters.getAsString("B"));
        return null;
    }
}
