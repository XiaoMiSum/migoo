package xyz.migoo.test.extend;

/**
 * HOOK 扩展
 * @author xiaomi
 * @date 2018/8/6 14:41
 */
public class Hook {

    public static void hook(String param){
        System.out.println("hook: " + param);
    }

    public static void hook2(String param){
        String[] params = param.split(",");
        for (String s : params) {
            System.out.println("hook2: " + s);
        }
    }
}
