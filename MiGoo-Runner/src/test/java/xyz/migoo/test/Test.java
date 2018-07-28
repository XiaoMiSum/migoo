package xyz.migoo.test;

import xyz.migoo.runner.Runner;
import xyz.migoo.runner.TestResult;

/**
 * @author xiaomi
 * @date 2018/7/25 15:10
 */
public class Test {

    @org.junit.Test
    public void testApi(){
        TestResult result = new Runner().run("/Users/xiaomi/PycharmProjects/water-block/mg/TestCase/test_case.json"
        ,"","");


    }
}
