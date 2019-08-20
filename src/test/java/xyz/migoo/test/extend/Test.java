package xyz.migoo.test.extend;

import xyz.migoo.framework.assertions.function.IFunction;

import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-04-14 02:22
 */
public class Test implements IFunction{
    @Override
    public Boolean assertTrue(Map<String, Object> data) {
        String s1 = String.valueOf(data.get("expect"));
        return s1.equalsIgnoreCase("1");
    }
}
