package function.xyz.migoo;

import core.xyz.migoo.function.Args;
import core.xyz.migoo.function.Function;
import core.xyz.migoo.function.KwArgs;
import core.xyz.migoo.function.LsArgs;
import core.xyz.migoo.variable.MiGooVariables;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Locale;

public class Faker implements Function {

    public static void main(String[] args) throws Exception {
        var kwArgs = new KwArgs(new MiGooVariables());
        kwArgs.put("locale", "zh-CN");
        kwArgs.put("key", "address.city");
        System.out.println(new Faker().execute(kwArgs));
        var lsArgs = new LsArgs(new MiGooVariables());
        lsArgs.add("name.fullName");
        lsArgs.addLast("zh-CN");
        System.out.println(new Faker().execute(lsArgs));
    }

    /**
     * 通过 Faker库生成假数据，支持2个参数
     * 参数：
     * key: 假数据类型，格式为 Faker库的ClassName.Method
     * local: 语言，可控，默认为 zh-CN
     */
    @Override
    public Object execute(Args args) {
        return args instanceof KwArgs kwArgs ? execute(kwArgs) : execute((LsArgs) args);
    }

    public Object execute(KwArgs args) {
        var locale = args.containsKey("local") ? args.getString("locale") : "zh-CN";
        return execute(locale, args.getString("key"));
    }

    public Object execute(LsArgs args) {
        var locale = args.size() < 2 ? "zh-CN" : args.getLast().toString();
        return execute(locale, args.getString(0));
    }

    public Object execute(String locale, String key) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("key con not be null");
        }
        try {
            var faker = new com.github.javafaker.Faker(Locale.of(locale));
            var keys = key.split("\\.");
            var current = faker.getClass().getDeclaredMethod(StringUtils.uncapitalize(keys[0])).invoke(faker);
            Method method = current.getClass().getDeclaredMethod(StringUtils.uncapitalize(keys[1]));
            return method.invoke(current);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
