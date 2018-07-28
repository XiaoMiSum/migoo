package xyz.migoo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author xiaomi
 * @Date 2018/02/06
 */
public class MapUtil {
    /**
     * 将Java Bean 转换为 Map 包含父类属性
     * @param object 请求对象
     * @return
     */
    public static Map<Object,Object> toMap(Object object) {
        try {
            if (object instanceof String){
                return toMap((String) object);
            }
            if (object instanceof JSONObject){
                return toMap((JSONObject)object);
            }
            if (object instanceof JSONArray){
                return toMap((JSONArray)object);
            }
            return toMap(object,true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将Java Bean 转换为 Map
     * @param object 请求对象
     * @param onlySub 是否获取当前子类的属性
     * @return
     */
    public static Map<Object,Object> toMap(Object object,boolean onlySub) throws IllegalAccessException {
        Map<Object,Object> map= new HashMap<>(50);
        Class clazz = object.getClass();
        Field[] fields;
        //通过反射将对象中的属性取出
        if (onlySub) {
            fields = clazz.getDeclaredFields();
        }else {
            fields = clazz.getFields();
        }
        for (Field field : fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(object));
        }
        return map;
    }

    /**
     * 将JSON字符串转化为Map对象
     * @param jsonString JSON字符串
     * @return
     */
    private static Map<Object, Object> toMap(String jsonString) {
        try {
            Object json = JSON.parse(jsonString);
            if (json instanceof JSONObject){
                return toMap((JSONObject)json);
            }
            if (json instanceof JSONArray){
                return toMap((JSONArray)json);
            }
            return null;
        }catch (JSONException e){
            return null;
        }
    }

    private static Map toMap(JSONArray jsonArray){
        Map<Object, Object> map = new HashMap<>(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            Object json =  jsonArray.get(i);
            if (json instanceof JSONObject){
                map.put(i, toMap((JSONObject)json));
            }else if (json instanceof JSONArray){
                map.put(i, toMap((JSONArray)json));
            }else {
                map.put(i, json);
            }
        }
        return map;
    }

    private static Map toMap(JSONObject jsonObject){
        Set<Map.Entry<String, Object>> collection =jsonObject.entrySet();
        Map<Object, Object> map = new HashMap<>(collection.size());
        for (Map.Entry<String, Object> entry : collection){
            map.put(entry.getKey(),entry.getValue());
        }
        return map;
    }

}
