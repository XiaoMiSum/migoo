package xyz.migoo;

import org.junit.jupiter.api.Test;

public class MiGooTest {
    @Test
    public void jdbc() {
        MiGoo.start("jdbc/测试用例/jdbc2.yaml");
    }

    @Test
    public void test() {
        MiGoo.start("debug/debug2.yaml");
    }

    @Test
    public void test1() {
        MiGoo.start("debug/多层级执行逻辑.yaml");
    }

    @Test
    public void test2() {
        MiGoo.start("debug/多层级默认配置.yaml");
    }

    @Test
    public void test3() {
        MiGoo.start("debug/同层级默认配置.yaml");
    }

    @Test
    public void test4() {
        MiGoo.start("debug/最小单元执行逻辑.yaml");
    }

    @Test
    public void test5() {
        MiGoo.start("debug/前置处理器.yaml");
    }

    @Test
    public void test6() {
        MiGoo.start("debug/后置处理器.yaml");
    }

    @Test
    public void test7() {
        MiGoo.start("debug/提取器.yaml");
    }

    @Test
    public void test8() {
        MiGoo.start("debug/验证器.yaml");
    }

    @Test
    public void test9() {
        MiGoo.start("debug/变量合并逻辑.yaml");
    }

    @Test
    public void test10() {
        MiGoo.start("debug/上层执行失败处理.yaml");
    }

}
