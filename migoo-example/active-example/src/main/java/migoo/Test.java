package migoo;

import xyz.migoo.MiGoo;

public class Test {
    public static void main(String[] args) {
        MiGoo.start("classpath:取样器/activemq_sampler_queue0.yaml");
        MiGoo.start("classpath:取样器/activemq_sampler_queue1.yaml");
        MiGoo.start("classpath:取样器/activemq_sampler_queue2.yaml");
        MiGoo.start("classpath:取样器/activemq_sampler_queue3.yaml");
        MiGoo.start("classpath:取样器/activemq_sampler_topic0.yaml");
        MiGoo.start("classpath:测试用例/activemq.yaml");
    }
}
