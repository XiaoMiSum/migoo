package xyz.migoo.functions.sub;

import xyz.migoo.exception.ExecuteError;
import xyz.migoo.framework.Vars;
import xyz.migoo.framework.functions.AbstractFunction;
import xyz.migoo.framework.functions.CompoundVariable;

/**
 * 变量扩展 用于生成用例中的变量值
 * @author xiaomi
 * @date 2018/8/6 14:41
 */
public class TestUseVars extends AbstractFunction {

    @Override
    public Object execute(CompoundVariable parameters) throws ExecuteError {
        String key = parameters.getAsString("title");
        System.out.println(String.format("case = %s", parameters.get("title")));
        String result =Vars.get(key).getString("pwd1");
        System.out.println(String.format("get vars, key = pwd1, value = %s", result));
        return result;
    }
}
