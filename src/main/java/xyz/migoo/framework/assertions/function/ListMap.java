package xyz.migoo.framework.assertions.function;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 * @date 2019-08-13 22:17
 */
public class ListMap extends AbstractFunction {

    @Override
    public boolean assertTrue(Map<String, Object> data) {
        List actual = (List)data.get("actual");
        Object expect = data.get("expect");
        try {
            for (Object object : actual){
                JSONObject js = (JSONObject) object;
                if (!(expect instanceof List) && !(expect instanceof Map)){
                    if (js.containsKey(expect) || js.containsValue(expect)){
                        return true;
                    }
                }
                if (expect instanceof Map){
                    Map map =  (Map) expect;
                    for (Object key : map.keySet()){
                        if (js.containsKey(key) || js.containsValue(map.get(key))){
                            return true;
                        }
                    }
                }
                if (expect instanceof List){
                    List list = (List) expect;
                    for (Object obj: list){
                        if (js.containsKey(obj) || js.containsValue(obj)){
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e){
            return false;
        }
    }
}
