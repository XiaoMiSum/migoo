package io.github.xiaomisum;

import xyz.migoo.MiGoo;

public class Test {
    public static void main(String[] args) {
        MiGoo.start("取样器/rabbit_sampler_queue0.yaml");
        MiGoo.start("取样器/rabbit_sampler_queue1.yaml");
        MiGoo.start("取样器/rabbit_sampler_queue2.yaml");
        MiGoo.start("取样器/rabbit_sampler_queue3.yaml");
        MiGoo.start("测试用例/rabbit.yaml");
    }
}
