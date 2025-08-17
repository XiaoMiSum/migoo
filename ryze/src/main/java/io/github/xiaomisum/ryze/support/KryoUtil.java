package io.github.xiaomisum.ryze.support;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;

public class KryoUtil {

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        return kryo;
    });

    public static Kryo getKryo() {
        return kryoThreadLocal.get();
    }

    /**
     * 返回一个对象的深拷贝
     *
     * @param object 原对象
     * @param <T>    原对象类型
     * @return 原对象的深拷贝
     */
    public static <T> T copy(T object) {
        return kryoThreadLocal.get().copy(object);
    }

}