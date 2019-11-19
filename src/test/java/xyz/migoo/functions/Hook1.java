package xyz.migoo.functions;

import xyz.migoo.exception.ExtenderException;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

/**
 * HOOK 扩展
 * @author xiaomi
 * @date 2018/8/6 14:41
 */
public class Hook1 extends AbstractFunction {

    @Override
    public Object execute(CompoundVariable parameters) throws ExtenderException {
        if (parameters.size() > 1){
            throw new ExtenderException("执行异常");
        }
        System.out.println("hook1: " + parameters.getAsString("A"));
        return null;
    }
}
