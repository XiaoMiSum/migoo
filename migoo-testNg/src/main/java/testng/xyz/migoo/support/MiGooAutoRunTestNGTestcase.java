package testng.xyz.migoo.support;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

public class MiGooAutoRunTestNGTestcase extends BasicMiGooTestNgTestcase implements IHookable {


    @Override
    public void run(IHookCallBack iHookCallBack, ITestResult iTestResult) {
        // todo 通过注解判断是否需要自动运行
        //  需要自动运行，则调用 migoo 执行测试元件，设置 itestresult 的返回结果
        var d = iTestResult.getMethod().getConstructorOrMethod().getMethod().getParameters()[0];
        iHookCallBack.runTestMethod(iTestResult);
    }
}
