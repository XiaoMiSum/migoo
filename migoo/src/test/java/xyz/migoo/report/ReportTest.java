package xyz.migoo.report;

import org.junit.jupiter.api.Test;
import xyz.migoo.MiGoo;

public class ReportTest {

    @Test
    public void test1() {
        MiGoo.start("classpath:debug/R1.yaml");
    }

    @Test
    public void test2() {
        MiGoo.start("classpath:debug/R2.yaml");
    }

    @Test
    public void test3() {
        MiGoo.start("classpath:debug/R3.yaml");
    }

    @Test
    public void test4() {
        MiGoo.start("classpath:debug/R4.yaml");
    }

    @Test
    public void test5() {
        MiGoo.start("classpath:debug/R5.yaml");
    }
}
