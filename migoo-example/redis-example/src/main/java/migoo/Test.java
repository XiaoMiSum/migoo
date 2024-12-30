package migoo;

import xyz.migoo.MiGoo;

public class Test {

    public static void main(String[] args) {
        MiGoo.start("classpath:取样器/redis_sampler0.yaml");
        MiGoo.start("classpath:取样器/redis_sampler1.yaml");
        MiGoo.start("classpath:取样器/redis_sampler2.yaml");
        MiGoo.start("classpath:取样器/redis_sampler3.yaml");
        MiGoo.start("classpath:取样器/redis_sampler4.yaml");
        MiGoo.start("classpath:取样器/redis_sampler5.yaml");
        MiGoo.start("classpath:测试用例/redis.yaml");
    }
}
