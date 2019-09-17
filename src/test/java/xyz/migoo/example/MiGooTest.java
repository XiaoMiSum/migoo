package xyz.migoo.example;

import org.junit.jupiter.api.Test;

/**
 * @author xiaomi
 * @date 2018/7/25 15:10
 */
public class MiGooTest extends Base{

    @Test
    public void testApiCaseSet(){
        runner.run("./case/case.yml", "./case/vars.yml");
    }
}
