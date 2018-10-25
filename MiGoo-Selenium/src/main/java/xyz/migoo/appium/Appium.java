package xyz.migoo.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import xyz.migoo.config.Config;
import xyz.migoo.selenium.Selenium;
import xyz.migoo.utils.DateUtil;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static xyz.migoo.config.Config.SPLITTER;

/**
 * @author xiaomi
 * @date 2018/10/25 00:05
 */
public class Appium {
    private static Log log = new Log(Selenium.class);
    private AppiumDriver driver;
    private String using;
    private String by = "";

    private Appium(Builder builder) {
        this.driver = builder.driver;
    }

    public void click(String using) {
        WebElement element = findElementBy(using);
        if (element != null) {
            element.click();
            log.info("click object: " + element.getText());
        }
    }

    /**
     * 获取元素的 Text
     *
     * @param using 元素定位方式
     * @return 元素的 Text
     */
    public String getText(String using) {
        WebElement element = findElementBy(using);
        if (element != null) {
            String text = element.getText();
            log.info("get text: " + text);
            return text;
        }
        return null;
    }

    /**
     * 获取元素属性
     *
     * @param using     元素定位方式
     * @param attribute 指定元素
     * @return 属性值
     */
    public String getAttribute(String using, String attribute) {
        WebElement element = findElementBy(using);
        if (element != null) {
            String text = element.getAttribute(attribute);
            log.info("get " + attribute + " value: " + text);
            return text;
        }
        return null;
    }

    /**
     * 以 from 为起点，滑动屏幕到 to
     *
     * @param from 起点
     * @param to   终点
     */
    public void moveTo(String from, String to) {
        AndroidElement f = (AndroidElement) findElementBy(from);
        AndroidElement t = (AndroidElement) findElementBy(to);
        if (f != null && t != null) {
            Point fPoint = f.getLocation();
            Point tPoint = t.getLocation();
            new TouchAction<>(driver)
                    .press(PointOption.point(fPoint.x, fPoint.y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1L)))
                    .moveTo(PointOption.point(tPoint.x, tPoint.y))
                    .perform().release();
            log.info("moveTo : " + f.getText() + " -> " + t.getText());
        }
    }

    /**
     * 在指定元素中输入字符
     *
     * @param using 元素定位方式
     * @param text  输入的字符
     */
    public void sendKeys(String using, String text) {
        WebElement element = findElementBy(using);
        if (element != null) {
            element.clear();
            DateUtil.sleep(Config.SHORT_TIME);
            element.sendKeys(text);
            log.info("send keys: " + text);
        }
    }

    /**
     * 长按元素，直到元素事件被触发
     *
     * @param using 元素定位方式
     * @param seconds 时间
     */
    public void touch(String using, long seconds) {
        AndroidElement element = (AndroidElement) findElementBy(using);
        if (element != null) {
            Point point = element.getLocation();
            LongPressOptions options = LongPressOptions.longPressOptions();
            options.withDuration(Duration.ofSeconds(seconds));
            new TouchAction<>(driver)
                    .longPress(PointOption.point(point.x, point.y))
                    .longPress(options)
                    .perform().release();
            log.info("longPress object: " + element.getText());
        }
    }

    /**
     * 按压元素
     *
     * @param using 元素定位方式
     * @param tapsCount 按压次数
     */
    public void tap(String using, int tapsCount) {
        AndroidElement element = (AndroidElement) findElementBy(using);
        if (element != null) {
            Point point = element.getLocation();
            TapOptions options = TapOptions.tapOptions();
            options.withTapsCount(tapsCount);
            new TouchAction<>(driver)
                    .tap(PointOption.point(point.x, point.y))
                    .tap(options)
                    .perform().release();
            log.info("tap " + element.getText() + ": " + tapsCount + " 次");
        }
    }

    /**
     * 以元素左上角坐标 x + 100 为起点，向左滑动 100像素
     *
     * @param using 元素定位方式
     */
    public void left(String using) {
        AndroidElement element = (AndroidElement) findElementBy(using);
        if (element != null) {
            Point point = element.getLocation();
            new TouchAction<>(driver)
                    .press(PointOption.point(point.x + 100, point.y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1L)))
                    .moveTo(PointOption.point(point.x, point.y))
                    .perform().release();
            log.info("Swipe left on: " + using);
        }
    }

    /**
     * 以元素左上角坐标 x为起点，向右滑动 100像素
     *
     * @param using 元素定位方式
     */
    public void right(String using) {
        AndroidElement element = (AndroidElement) findElementBy(using);
        if (element != null) {
            Point point = element.getLocation();
            new TouchAction<>(driver)
                    .press(PointOption.point(point.x, point.y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1L)))
                    .moveTo(PointOption.point(point.x + 100, point.y))
                    .perform().release();
            log.info("Swipe right on: " + using);
        }
    }

    /**
     * 以元素左上角坐标 x为起点，向上滑动 100像素
     *
     * @param using 元素定位方式
     */
    public void up(String using) {
        AndroidElement element = (AndroidElement) findElementBy(using);
        if (element != null) {
            Point point = element.getLocation();
            new TouchAction<>(driver)
                    .press(PointOption.point(point.x, point.y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1L)))
                    .moveTo(PointOption.point(point.x - 100, point.y))
                    .perform().release();
            log.info("Swipe up on: " + using);
        }
    }

    /**
     * 以元素左上角坐标 x为起点，向下滑动 100像素
     *
     * @param using 元素定位方式
     */
    public void down(String using) {
        AndroidElement element = (AndroidElement) findElementBy(using);
        if (element != null) {
            Point point = element.getLocation();
            new TouchAction<>(driver)
                    .press(PointOption.point(point.x, point.y))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1L)))
                    .moveTo(PointOption.point(point.x + 100, point.y))
                    .perform().release();
            log.info("Swipe down on: " + using);
        }
    }

    /**
     * 获取屏幕分辨率
     */
    public int[] size() {
        int width = driver.manage().window().getSize().getWidth();
        int height = driver.manage().window().getSize().getHeight();
        return new int[]{width, height};
    }

    /**
     * 解析定位方式
     * @param object 定位方式
     */
    private void parseObject(String object) {
        if (object.startsWith(Config.XPATH_1) || object.startsWith(Config.XPATH_2)) {
            by = "xpath";
            using = object;
        } else {
            using = object.substring(object.indexOf(SPLITTER) + 1);
            if (object.startsWith(Config.XPATH_3)) {
                by = "xpath";
            } else if (object.startsWith(Config.LINK)) {
                by = "link text";
            } else if (object.startsWith(Config.ID)) {
                by = "id";
            } else if (object.startsWith(Config.PARTIAL_LINK)) {
                by = "partial link text";
            } else if (object.startsWith(Config.CSS)) {
                by = "css selector";
            } else if (object.startsWith(Config.CLASS)) {
                by = "class name";
            } else if (object.startsWith(Config.TAG_NAME)) {
                by = "tag name";
            } else if (object.startsWith(Config.NAME)) {
                by = "name";
            }
        }
    }

    /**
     * 获取元素
     * @param obj 定位方式
     * @return WebElement
     */
    private WebElement findElementBy(String obj) {
        parseObject(obj);
        try {
            DateUtil.sleep(Config.SHORT_TIME);
            log.info("get object: " + using);
            if (StringUtil.isNotBlank(by)) {
                return driver.findElement(by, using);
            }
            if (obj.startsWith(Config.ACCESSIBILITY_ID)) {
                return driver.findElementByAccessibilityId(using);
            }
            if (obj.startsWith(Config.UIAUTOMATOR)) {
                return ((AndroidDriver) driver).findElementByAndroidUIAutomator(using);
            }
            if (obj.startsWith(Config.UIAUTOMATION)) {
                return ((IOSDriver) driver).findElementByIosUIAutomation(using);
            }
            if (obj.startsWith(Config.IOS_CLASS)) {
                return ((IOSDriver) driver).findElementByIosClassChain(using);
            }
            if (obj.startsWith(Config.PREDICATE)) {
                return ((IOSDriver) driver).findElementByIosNsPredicate(using);
            }
        } catch (Exception e) {
            log.error("get object exception.", e);
        }
        return null;
    }

    public static class Builder {
        private AppiumDriver driver;

        public Builder(){

        }

        public Builder driver(AppiumDriver driver) {
            this.driver = driver;
            return this;
        }

        /**
         * 隐式等待，单位：秒
         *
         * @param time 等待时间
         * @return this
         */
        public Builder implicitlyWait(long time) {
            if (this.driver != null) {
                this.driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
                log.info("set implicitlyWait time " + time + "seconds");
            }
            return this;
        }

        /**
         * 页面加载时间，单位：秒
         *
         * @param time 等待时间
         * @return this
         */
        public Builder pageLoadTimeout(long time) {
            if (this.driver != null) {
                this.driver.manage().timeouts().pageLoadTimeout(time, TimeUnit.SECONDS);
                log.info("set pageLoadTimeout time " + time + "seconds");
            }
            return this;
        }

        public Appium build() {

            return new Appium(this);
        }
    }
}
