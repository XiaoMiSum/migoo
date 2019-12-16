package xyz.migoo.functions;

import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

/**
 * @author xiaomi
 * @date 2019/11/18 17:22
 */
public class Random extends AbstractFunction {

    @Override
    public Integer execute(CompoundVariable parameters) throws ExecuteError {
        Integer bound = parameters.getInteger("bound");
        java.util.Random random = new java.util.Random();
        return bound != null && bound > 0 ? random.nextInt(bound) : random.nextInt();
    }
}
