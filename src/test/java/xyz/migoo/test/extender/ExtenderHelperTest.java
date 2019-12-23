package xyz.migoo.test.extender;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.migoo.framework.functions.VariableHelper;

/**
 * @author xiaomi
 * @date 2019/9/1 18:51
 */
public class ExtenderHelperTest {

    @Test
    public void testBind1(){
        JSONObject source = new JSONObject();
        source.put("user", "${user}");
        source.put("pwd", "${pwd}");
        JSONObject vars = new JSONObject();
        vars.put("user", "MiGoo");
        vars.put("pwd", "abb");
        VariableHelper.bindAndEval(source, vars);
        Assertions.assertEquals(source, vars);
    }

    @Test
    public void testBind2(){
        JSONObject source = new JSONObject();
        source.put("user", "${user}");
        source.put("pwd", "${pwd}");
        JSONObject data = new JSONObject();
        data.put("sign", "${sign}${user}");
        data.put("test", "123");
        data.put("func", "__RandomString(length=10,string=${sign}${user})");
        source.put("data", data);
        JSONObject vars = new JSONObject();
        vars.put("user", "MiGoo");
        vars.put("pwd", "abb");
        vars.put("sign", "better");
        VariableHelper.bindAndEval(source, vars);
        Assertions.assertEquals(source.getJSONObject("data").get("sign"), vars.getString("sign") + vars.get("user"));
    }
    @Test
    public void testBind3(){
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
        VariableHelper.bindAndEval(source, vars);
        Assertions.assertEquals(source.getJSONArray("data").get(0), vars.getString("sign"));
    }
    @Test
    public void testBind4(){
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
        VariableHelper.bindAndEval(vars, vars);
        VariableHelper.bindAndEval(source, vars);
        Assertions.assertEquals(source.getJSONArray("data").get(0), vars.getString("sign"));
        Assertions.assertFalse("${func}".equals(source.getJSONArray("data").get(1)));
    }
}
