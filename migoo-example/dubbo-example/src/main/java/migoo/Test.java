package migoo;

import xyz.migoo.MiGoo;

public class Test {

    public static void main(String[] args) {
        MiGoo.start("classpath:取样器/dubbo_sampler.yaml");
        MiGoo.start("classpath:测试用例/dubbo.yaml");
    }
}
