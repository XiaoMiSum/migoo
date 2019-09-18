package xyz.migoo.test.extender;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.migoo.extender.ExtenderHelper;

/**
 * @author xiaomi
 * @date 2019/9/1 18:51
 */
public class ExtenderHelperTest {

    @Test
    public void testBind1(){
        JSONObject use = new JSONObject();
        use.put("user", "${user}");
        use.put("pwd", "${pwd}");
        JSONObject vars = new JSONObject();
        vars.put("user", "MiGoo");
        vars.put("pwd", "abb");
        ExtenderHelper.bind(use, vars);
        Assertions.assertEquals(use, vars);
    }
    @Test
    public void testBind2(){
        JSONObject use = new JSONObject();
        use.put("user", "${user}");
        use.put("pwd", "${pwd}");
        JSONObject data = new JSONObject();
        data.put("sign", "${sign}");
        data.put("test", "123");
        use.put("data", data);
        JSONObject vars = new JSONObject();
        vars.put("user", "MiGoo");
        vars.put("pwd", "abb");
        vars.put("sign", "better");
        ExtenderHelper.bind(use, vars);
        Assertions.assertEquals(use.getJSONObject("data").get("sign"), vars.getString("sign"));
    }
    @Test
    public void testBind3(){
        JSONObject use = new JSONObject();
        use.put("user", "${user}");
        use.put("pwd", "${pwd}");
        JSONArray data = new JSONArray();
        data.add("${sign}");
        data.add("123");
        use.put("data", data);
        JSONObject vars = new JSONObject();
        vars.put("user", "MiGoo");
        vars.put("pwd", "abb");
        vars.put("sign", "better");
        ExtenderHelper.bind(use, vars);
        Assertions.assertEquals(use.getJSONArray("data").get(0), vars.getString("sign"));
    }
    @Test
    public void testBind4(){
        JSONObject use = new JSONObject();
        use.put("user", "${user}");
        use.put("pwd", "${pwd}");
        JSONArray data = new JSONArray();
        data.add("${sign}");
        data.add("${func}");
        use.put("data", data);
        JSONObject vars = new JSONObject();
        vars.put("user", "MiGoo");
        vars.put("pwd", "abb");
        vars.put("sign", "better");
        vars.put("func", "__func()");
        ExtenderHelper.bind(use, vars);
        Assertions.assertEquals(use.getJSONArray("data").get(0), vars.getString("sign"));
        Assertions.assertEquals(use.getJSONArray("data").get(1), "${func}");
    }
}
