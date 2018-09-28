package xyz.migoo.test.extend;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Dict;

/**
 * 验证扩展
 * @author xiaomi
 * @date 2018/8/6 14:41
 */
public class Validator {

    public static void setValue(JSONObject validate, String param){
        validate.put(Dict.VALIDATE_EXPECT, param);
    }

}
