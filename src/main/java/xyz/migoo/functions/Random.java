package xyz.migoo.functions;

import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

/**
 * @author yacheng.xiao
 * @date 2019/11/18 17:22
 */
public class Random extends AbstractFunction {

    @Override
    public Integer execute(CompoundVariable parameters) {
        Integer bound = parameters.getAsInteger("bound");
        java.util.Random random = new java.util.Random();
        return bound != null && bound > 0 ? random.nextInt(bound) : random.nextInt();
    }
}
