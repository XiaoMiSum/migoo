package xyz.migoo.test.extender;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.functions.FunctionException;
import core.xyz.migoo.vars.VarsHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author xiaomi
 * @date 2019/9/1 18:51
 */
public class ExtenderHelperTest {

    @Test
    public void testBind1() throws FunctionException {
        JSONObject source = new JSONObject();
        source.put("user", "${user}");
        source.put("pwd", "${pwd}");
        JSONObject vars = new JSONObject();
        vars.put("user", "MiGoo");
        vars.put("pwd", "abb");
        VarsHelper.convertVariables(source, vars);
        Assertions.assertEquals(source, vars);
    }

    @Test
    public void testBind2() throws FunctionException {
        JSONObject source = new JSONObject();
        source.put("user", "${sign}${user}");
        source.put("pwd", "__RandomString(length=10,string=${sign}${user})");
        JSONObject data = new JSONObject();
        data.put("sign", "${sign}${user}");
        data.put("test", "123");
        data.put("func", "__RandomString(length=10,string=${sign}${user})");
        source.put("data", data);
        JSONObject vars = new JSONObject();
        vars.put("user", "MiGoo");
        vars.put("pwd", "abb");
        vars.put("sign", "better");
        VarsHelper.convertVariables(source, vars);
        Assertions.assertEquals(source.getJSONObject("data").get("sign"), vars.getString("sign") + vars.get("user"));
    }
    @Test
    public void testBind3() throws FunctionException {
        JSONObject source = new JSONObject();
        source.put("user", "${user}");
        source.put("pwd", "${pwd}");
        JSONArray data = new JSONArray();
        data.add("${sign}");
        data.add("123");
        source.put("data", data);
        JSONObject vars = new JSONObject();
        vars.put("user", "MiGoo");
        vars.put("pwd", "abb");
        vars.put("sign", "better");
        VarsHelper.convertVariables(source, vars);
        Assertions.assertEquals(source.getJSONArray("data").get(0), vars.getString("sign"));
    }
    @Test
    public void testBind4() throws FunctionException {
        JSONObject source = new JSONObject();
        source.put("user", "${user}");
        source.put("pwd", "${pwd}");
        JSONArray data = new JSONArray();
        data.add("${sign}");
        data.add("${func}");
        source.put("data", data);
        JSONObject vars = new JSONObject();
        vars.put("user", "MiGoo");
        vars.put("pwd", "abb");
        vars.put("sign", "better");
        vars.put("func", "__random()");
        VarsHelper.convertVariables(source, vars);
        Assertions.assertEquals(source.getJSONArray("data").get(0), vars.getString("sign"));
        Assertions.assertNotEquals("${func}", source.getJSONArray("data").get(1));
    }
}
