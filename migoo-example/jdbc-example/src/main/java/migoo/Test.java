package migoo;

import xyz.migoo.MiGoo;

public class Test {

    public static void main(String[] args) {
        MiGoo.start("classpath:取样器/jdbc_sampler0.yaml");
        MiGoo.start("classpath:取样器/jdbc_sampler1.yaml");
        MiGoo.start("classpath:取样器/jdbc_sampler2.yaml");
        MiGoo.start("classpath:取样器/jdbc_sampler3.yaml");
        MiGoo.start("classpath:取样器/jdbc_sampler4.yaml");
        MiGoo.start("classpath:取样器/jdbc_sampler5.yaml");
        MiGoo.start("classpath:测试用例/jdbc.yaml");
    }
}
