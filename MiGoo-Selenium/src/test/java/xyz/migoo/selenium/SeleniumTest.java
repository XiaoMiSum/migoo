package xyz.migoo.selenium;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import xyz.migoo.report.Report;

/**
 * @author xiaomi
 * @date 2018/7/8 21:38
 */
public class SeleniumTest {

    private Selenium selenium;

    @Test
    public void fireFoxInMac(){
        Configuration config = new Configuration.Builder().firefox().bin("/Applications/Firefox.app").build();
        selenium = new Selenium.Builder().driver(config).implicitlyWait(10).pageLoadTimeout(10).maximize().build();
        selenium.get("https://cn.bing.com");
        Assert.assertEquals("https://cn.bing.com/",selenium.url());
        selenium.sendKeys("id=sb_form_q","selenium3");
        selenium.click("id=sb_form_go");
        Report.report("测试搜索Selenium3","是否只需要 selenium3 的结果?",
                selenium.element("xpath=/html/body/div[1]/ol[1]/li[1]/div[2]").getText());
        selenium.exit();
    }

    @Ignore
    @Test
    public void fireFoxInWindows(){
        Configuration config = new Configuration.Builder().firefox().bin("火狐exe路径")
                .profile("火狐配置文件路径").driver("geckodriver.exe").build();
        selenium = new Selenium.Builder().driver(config).implicitlyWait(10).pageLoadTimeout(10).maximize().build();
        selenium.get("https://cn.bing.com");
        Assert.assertEquals("https://cn.bing.com/",selenium.url());
    }
}
