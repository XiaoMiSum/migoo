package testng.xyz.migoo.support;

import core.xyz.migoo.SessionRunner;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import testng.xyz.migoo.support.annotation.MiGooTest;

import java.util.Objects;

public abstract class BasicMiGooTestNgTestcase {

    /**
     * 在测试方法执行前执行，用于创建 migoo 运行环境
     *
     * @param testResult testng
     */
    @BeforeMethod(alwaysRun = true)
    public void createSessionAndDestroySession(ITestResult testResult) {
        MiGooTest migoo = testResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(MiGooTest.class);
        if (Objects.isNull(migoo)) {
            return;
        }
        // 创建一个 在测试框架中运行时使用的 session
        SessionRunner.newTestFrameworkSession();
    }

    /**
     * 在测试方法执行前执行，用于创建 migoo 运行环境
     *
     * @param testResult testng
     */
    @AfterMethod(alwaysRun = true)
    public void stopSessionAndRemove(ITestResult testResult) {
        SessionRunner.removeSession();
    }

}
