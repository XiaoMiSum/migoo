package xyz.migoo.selenium;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import xyz.migoo.config.DataStore;
import xyz.migoo.exception.SeleniumException;
import xyz.migoo.utils.DateUtil;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.Os;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.Select;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.remote.BrowserType.*;

/**
 * 封装Selenium的常用api简化测试脚本开发
 *
 * @author xiaomi
 */
public class Selenium {

    private static Log log = new Log(Selenium.class);
    private WebDriver driver;

    private Selenium(Builder builder) {
        this.driver = builder.driver;
    }

    /**
     * 操作提示信息窗口，只针对原生控件
     *
     * @param option 确定 true ，取消 false
     */
    public void alert(boolean option) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        try {
            Alert alert = driver.switchTo().alert();
            log.info("get object " + "alert");
            if (option) {
                alert.accept();
                log.info("click confirm");
            } else {
                alert.dismiss();
                log.info("click cancel");
            }
        } catch (Exception e) {
            log.error("operation alert" + " exception.", e);
        }
    }

    /**
     * 添加cookie
     */
    public void addCookie(Set<Cookie> cookies) {
        try {
            for (Cookie cookie : cookies) {
                driver.manage().addCookie(cookie);
            }
            log.info("add cookies success.");
        } catch (WebDriverException e) {
            log.error("add cookies" + " exception.", e);
        }
    }

    /**
     * 执行点击操作
     *
     * @param id
     */
    public void click(String id) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        try {
            driver.findElement(this.parseObject(id)).click();
            log.info("get and click object " + id);
        } catch (Exception e) {
            log.error("get object exception.",e);
        }
    }

    /**
     * 关闭浏览器
     */
    public void exit() {
        if (driver instanceof ChromeDriver) {
            driver.close();
        }
        driver.quit();
        log.info("test over, close driver");
    }

    /**
     * 返回一个页面元素
     *
     * @param id
     * @return
     */
    public WebElement element(String id) {
        WebElement webElement = null;
        try {
            webElement = driver.findElement(this.parseObject(id));
            log.info("get object " + id);
        } catch (Exception e) {
            log.error("get object exception.",e);
        }
        return webElement;
    }

    /**
     * 执行JS脚本
     *
     * @param script
     */
    public void executeJavaScript(String script) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        try {
            ((JavascriptExecutor) driver).executeScript(script);
            log.info("Javascript execute success.");
        } catch (Exception e) {
            log.error("Javascript execute exception.",e);
        }
    }

    /**
     * 访问url
     */
    public void get(String url) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        try {
            driver.get(url);
            log.info("input url: " + url);
        } catch (WebDriverException e) {
            log.error("input url exception.",e);
        }
    }

    /**
     * 获取元素文字
     *
     * @param id
     * @return
     */
    public String getText(String id) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        String text = null;
        try {
            text = driver.findElement(this.parseObject(id)).getText();
            log.info("get object " + id);
            log.info("get object value" + text);
        } catch (NoSuchElementException e) {
            log.error("get object exception.",e);
        }
        return text;
    }

    /**
     * 获取页面Title
     */
    public String getTitle() {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        String title = null;
        try {
            title = driver.getTitle();
            log.info("get title: " + title);
        } catch (NoSuchElementException e) {
            log.error("get title exception.",e);
        }
        return title;
    }

    /**
     * 获取cookie
     */
    public Set<Cookie> getCookie() {
        Set<Cookie> cookies = null;
        try {
            cookies = driver.manage().getCookies();
            log.info("get cookie" + " success.");
        } catch (WebDriverException e) {
            log.error("get cookie" + " exception.", e);
        }
        return cookies;
    }

    /**
     * 获取元素的某个属性值
     *
     * @param id
     * @param key
     * @return
     */
    public String getAttribute(String id, String key) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        String text = null;
        try {
            text = driver.findElement(this.parseObject(id)).getAttribute(key);
            log.info("get object " + id);
            log.info("get object attribute: " + text);
        } catch (NoSuchElementException e) {
            log.error("get object exception.",e);
        }
        return text;
    }

    /**
     * 判断元素是否显示
     *
     * @param id
     * @return
     */
    public boolean isElementPresent(String id) {
        try {
            driver.findElement(this.parseObject(id));
            log.info("get object " + id);
            return true;
        } catch (NoSuchElementException e) {
            log.error("get object exception.",e);
            return false;
        }
    }

    /**
     * 在N个元素中，对符合条件的元素进行点击操作
     *
     * @param id   元素定位方式
     * @param text 元素的文字
     */
    public void list(String id, String text) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        try {
            List<WebElement> list = driver.findElements(this.parseObject(id));
            log.info("get object " + id);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getText().equals(text)) {
                    list.get(i).click();
                    log.info("click object: " + text);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("get object exception.",e);
        }
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
    }

    /**
     * 移动鼠标到元素上
     *
     * @param id
     */
    public void moveToElement(String id) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(driver.findElement(this.parseObject(id))).perform();
            log.info("get object " + id);
            log.info("mouse move to " + id + " success.");
        } catch (Exception e) {
            log.error("get object exception.",e);
        }
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
    }

    /**
     * 使用JDK的Robot类进行键盘操作，多用于上传文件。
     * 使用此方法需要保持浏览器在最前端
     *
     * @param text  输入的字符或需要操作的按键
     * @param times 按压次数，操作键盘上的特殊按键时需传递此参数，且必须大于等于1
     */
    public void robot(String text, int times) {
        DateUtil.sleep(DataStore.D_WAIT_MEDIUM_TIME);
        try {
            RobotKeyboard robot = RobotKeyboard.getInstance();
            if (times < 1) {
                robot.type(text);
                log.info("enter \"" + text + "\" in the object");
            } else {
                robot.type(text, times);
                log.info("press the key \"" + text + "\"" + times + "times");
            }
        } catch (AWTException e) {
            log.error("robot class operation keyboard exception.", e);
        }
    }

    /**
     * 按下拉选项文字选择下拉项，只针对原生控件
     *
     * @param id
     * @param text
     */
    public void select(String id, String text) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        try {
            Select select = new Select(driver.findElement(this.parseObject(id)));
            log.info("get object " + id);
            select.selectByVisibleText(text);
            log.info("click object " + text);
        } catch (Exception e) {
            log.error("get object exception.",e);
        }
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
    }

    /**
     * 执行输入操作
     *
     * @param id
     * @param text
     */
    public void sendKeys(String id, String text) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        try {
            WebElement webElement = driver.findElement(this.parseObject(id));
            log.info("get object " + id);
            webElement.clear();
            log.info("clear object content");
            webElement.sendKeys(text);
            log.info("enter \"" + text + "\" in the object");
        } catch (Exception e) {
            log.error("get object exception.",e);
        }
    }

    /**
     * 截图，并保存为指定文件
     *
     * @param file 文件完整路径，可忽略文件后缀，建议在使用时，文件名以测试的页面命名
     */
    public void screenshot(String file) {
        File screen = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File screenFile = new File(file + System.currentTimeMillis() + ".jpg");
        try {
            FileUtils.copyFile(screen, screenFile);
            log.info("screen shot" + " success." + "the file is \"" + screenFile + "\"");
        } catch (IOException e) {
            log.error("screen shot" + " exception.", e);
        }
    }

    /**
     * 切换浏览器窗口
     *
     * @param windowName
     */
    public void swithchToWindow(String windowName) {
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
        try {
            for (String s : driver.getWindowHandles()) {
                if (driver.getTitle().equals(windowName)) {
                    driver.switchTo().window(s);
                    log.info("window switch to: \"" + windowName + "\"");
                    break;
                }
            }
        } catch (Exception e) {
            log.error("switching window exception.",e);
        }
        DateUtil.sleep(DataStore.D_WAIT_SHORT_TIME);
    }

    /**
     * 获取 当前页面 url
     *
     * @return
     */
    public String url() {
        String url = "";
        try {
            url = driver.getCurrentUrl();
            log.info("get url: " + url);
        } catch (Exception e) {
            log.error("verify url" + " exception.", e);
        }
        return url;
    }

    private By parseObject(String object) {
        if (object.startsWith(DataStore.SELENIUM_XPATH_1) || object.startsWith(DataStore.SELENIUM_XPATH_2)) {
            return By.xpath(object);
        } else if (object.startsWith(DataStore.SELENIUM_LINK + DataStore.SELENIUM_BASE)) {
            object = object.substring(object.indexOf(DataStore.SELENIUM_BASE) + 1);
            return By.linkText(object);
        } else if (object.startsWith(DataStore.SELENIUM_XPATH_3 + DataStore.SELENIUM_BASE)) {
            object = object.substring(object.indexOf(DataStore.SELENIUM_BASE) + 1);
            return By.xpath(object);
        } else if (object.startsWith(DataStore.SELENIUM_ID + DataStore.SELENIUM_BASE)) {
            object = object.substring(object.indexOf(DataStore.SELENIUM_BASE) + 1);
            return By.id(object);
        } else if (object.startsWith(DataStore.SELENIUM_PARTIAL_LINK + DataStore.SELENIUM_BASE)) {
            object = object.substring(object.indexOf(DataStore.SELENIUM_BASE) + 1);
            return By.partialLinkText(object);
        } else if (object.startsWith(DataStore.SELENIUM_CSS + DataStore.SELENIUM_BASE)) {
            object = object.substring(object.indexOf(DataStore.SELENIUM_BASE) + 1);
            return By.cssSelector(object);
        } else if (object.startsWith(DataStore.SELENIUM_CLASS + DataStore.SELENIUM_BASE)) {
            object = object.substring(object.indexOf(DataStore.SELENIUM_BASE) + 1);
            return By.className(object);
        } else if (object.startsWith(DataStore.SELENIUM_TAG_NAME + DataStore.SELENIUM_BASE)) {
            object = object.substring(object.indexOf(DataStore.SELENIUM_BASE) + 1);
            return By.tagName(object);
        } else if (object.startsWith(DataStore.SELENIUM_NAME + DataStore.SELENIUM_BASE)) {
            object = object.substring(object.indexOf(DataStore.SELENIUM_BASE) + 1);
            return By.name(object);
        } else {
            return null;
        }
    }

    public static class Builder {

        private WebDriver driver;

        public Builder() {
        }

        /**
         * 初始化浏览器
         *
         * @param configuration 配置信息
         */
        public Builder driver(Configuration configuration) {
            log.info("init driver info");
            log.info("browser type is ' " + configuration.browser() + " '");
            log.info("driver exe is ' " + configuration.driver() + " '");
            log.info("properties is ' " + configuration.profile() + " '");
            switch (configuration.browser()) {
                case FIREFOX:
                    //设置火狐浏览器安装路径，driver路径，加载当前系统用户配置文件
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (Os.OS_WINDOWS){
                        System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, configuration.driver());
                        FirefoxProfile profile = new FirefoxProfile(new File(configuration.profile()));
                        firefoxOptions.setProfile(profile);
                        firefoxOptions.setBinary(configuration.bin());
                    }
                    firefoxOptions.setLegacy(false);
                    this.driver = new FirefoxDriver(firefoxOptions);
                    break;
                case CHROME:
                    System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, configuration.driver());
                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("user-data-dir=" + configuration.profile());
                    this.driver = new ChromeDriver(options);
                    break;
                case EDGE:
                    System.setProperty(EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY, configuration.driver());
                    this.driver = new EdgeDriver();
                    break;
                case IE:
                    System.setProperty(InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY, configuration.driver());
                    this.driver = new InternetExplorerDriver();
                    break;
                case SAFARI:
                    this.driver = new SafariDriver();
                    break;
                case HTMLUNIT:
                    log.info("html unit driver version is \"" + configuration.version() + "\"");
                    log.info("html unit driver enableJavascript is \"" + configuration.isEnableJavascript() + "\"");
                    this.driver = new HtmlUnitDriver(configuration.version(), configuration.isEnableJavascript());
                    break;
                default:
                    log.info("driver == null. please check configuration");
                    throw new SeleniumException("driver == null. please check configuration");
            }
            log.info( "init driver success.");
            return this;
        }

        /**
         * 隐式等待，单位：秒
         *
         * @param time
         * @return
         */
        public Builder implicitlyWait(int time) {
            if (this.driver != null) {
                this.driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
                log.info("set implicitlyWait time " + time + "seconds");
            }
            return this;
        }

        /**
         * 页面加载时间，单位：秒
         *
         * @param time
         * @return
         */
        public Builder pageLoadTimeout(int time) {
            if (this.driver != null) {
                this.driver.manage().timeouts().pageLoadTimeout(time, TimeUnit.SECONDS);
                log.info("set pageLoadTimeout time " + time + "seconds");
            }
            return this;
        }

        /**
         * 窗口最大化
         *
         * @return
         */
        public Builder maximize() {
            if (this.driver != null) {
                this.driver.manage().window().maximize();
                log.info("set browser window maximize");
            }
            return this;
        }

        public Selenium build() {
            if (this.driver == null) {
                throw new SeleniumException("driver == null. please check configuration");
            }
            return new Selenium(this);
        }
    }

}



