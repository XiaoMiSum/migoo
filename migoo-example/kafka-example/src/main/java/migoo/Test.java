package migoo;

import xyz.migoo.MiGoo;

public class Test {

    public static void main(String[] args) {
        MiGoo.start("classpath:取样器/kafka_sampler0.yaml");
        MiGoo.start("classpath:取样器/kafka_sampler1.yaml");
        MiGoo.start("classpath:取样器/kafka_sampler2.yaml");
        MiGoo.start("classpath:取样器/kafka_sampler3.yaml");
        MiGoo.start("classpath:取样器/kafka_sampler4.yaml");
        MiGoo.start("classpath:测试用例/kafka.yaml");
    }
}
