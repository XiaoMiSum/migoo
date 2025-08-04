package http.xyz.migoo.example.yaml;

import org.testng.annotations.Test;
import xyz.migoo.MiGoo;

public class YamlTestCase {


    @Test
    public void test1() {
        var result = MiGoo.start("测试用例/http.yaml");
        // 非 migoo-testng 环境，需要自行断言测试结果，否则 allure 报告最外层的测试会标记成功
        assert result.getStatus().isPassed();
    }

    @testng.xyz.migoo.support.annotation.MiGoo
    @Test
    public void test2() {
        MiGoo.start("取样器/http_sampler.yaml");
    }
}
