package xyz.migoo.functions;

import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

import java.util.UUID;

/**
 * @author yacheng.xiao
 * @date 2019/11/18 17:22
 */
public class Uuid extends AbstractFunction {

    @Override
    public String execute(CompoundVariable parameters) {
        return UUID.randomUUID().toString();
    }
}
