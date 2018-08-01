package xyz.migoo.utils;

import com.alibaba.fastjson.JSONObject;

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
    public static Map<Object, Object> toMap(String jsonString) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            Set<Map.Entry<String, Object>> collection =jsonObject.entrySet();
            Map<Object, Object> map = new HashMap<>(collection.size());
            for (Map.Entry<String, Object> entry : collection){
                map.put(entry.getKey(),entry.getValue());
            }
            return map;
        }catch (Exception e){
            return null;
        }
    }

}
