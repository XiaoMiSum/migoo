package migoo;

import xyz.migoo.MiGoo;

public class Test {

    public static void main(String[] args) {
        MiGoo.start("取样器/dubbo_sampler.yaml");
        MiGoo.start("测试用例/dubbo.yaml");
    }
}
