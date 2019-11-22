package xyz.migoo.test.example;

import org.junit.jupiter.api.Test;
import xyz.migoo.TestRunner;

/**
 * @author xiaomi
 * @date 2018/7/25 15:10
 */
public class MiGooTest {
    public TestRunner runner = new TestRunner("MiGoo");

    @Test
    public void testApiCaseSet(){
        runner.run("./case/case.yml", "./case/vars.yml");
    }
}
