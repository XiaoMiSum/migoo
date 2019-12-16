package xyz.migoo.functions;

import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

/**
 * 变量扩展 用于生成用例中的变量值
 * @author xiaomi
 * @date 2018/8/6 14:41
 */
public class GeneratePassword extends AbstractFunction {

    @Override
    public Object execute(CompoundVariable parameters) throws ExecuteError {
        return parameters.getString("password");
    }
}
