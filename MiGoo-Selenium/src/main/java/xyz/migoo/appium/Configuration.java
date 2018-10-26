package xyz.migoo.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import xyz.migoo.config.Config;
import xyz.migoo.exception.AppiumException;
import xyz.migoo.utils.StringUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;


/**
 * @author xiaomi
 */
public class Configuration {

    private AppiumDriver driver;

    private Configuration(Builder builder) {
        this.driver = builder.driver;
    }

    public AppiumDriver driver() {
        return driver;
    }

    public static class Builder {
        private static final Pattern PATTERN = Pattern.compile(
                "^http[s]*://[\\w\\.\\-]+(:\\d*)*(?:/|(?:/[\\w\\.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);

        private URL url;
        private String devices;
        private String platformName;
        private String platformVersion;
        private String app;
        private String bundleId;
        private String appPackage;
        private String appActivity;
        private AppiumDriver driver;
        private DesiredCapabilities capabilities = new DesiredCapabilities();

        public Builder() {
        }

        public Builder remote(String url) {
            if (!urlCheck(url)) {
                throw new AppiumException("check remote url exception");
            }
            try {
                this.url = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new AppiumException("remote url == null");
            }
            return this;
        }

        public Builder devices(String devices){
            this.devices = devices;
            return this;
        }

        public Builder platformName(String platformName){
            this.platformName = platformName;
            return this;
        }

        public Builder platformVersion(String platformVersion){
            this.platformVersion = platformVersion;
            return this;
        }

        public Builder udId(String udId){
            if (StringUtil.isBlank(udId)){
                throw new AppiumException("udid == null");
            }
            capabilities.setCapability("udid", udId);
            return this;
        }

        public Builder app(String app){
            this.app = app;
            return this;
        }

        public Builder bundleId(String bundleId){
            this.bundleId = bundleId;
            return this;
        }

        public Builder appPackage(String appPackage){
            this.appPackage = appPackage;
            return this;
        }

        public Builder appActivity(String appActivity){
            this.appActivity = appActivity;
            return this;
        }

        public Configuration build() {
            this.driver();
            if (driver == null){
                throw new AppiumException("driver == null");
            }
            return new Configuration(this);
        }

        private void driver() {
            this.platformCheck();
            capabilities.setCapability("autoWebview", true);
            if (StringUtil.equalsIgnoreCase(platformName, Config.IOS)){
                this.iosCheck();
                this.ios();
            }
            if (StringUtil.equalsIgnoreCase(platformName, Config.ANDROID)){
                this.androidCheck();
                this.android();
            }
        }

        private void android(){
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion", platformVersion);
            capabilities.setCapability("deviceName", this.devices);
            // 设置app的主包名和主类名
            capabilities.setCapability("appPackage", this.appPackage);
            capabilities.setCapability("appActivity", this.appActivity);
            // 支持中文
            capabilities.setCapability("unicodeKeyboard", true);
            capabilities.setCapability("resetKeyboard", false);
            // 跳过应用debug签名
            capabilities.setCapability("noSign", true);
            driver = new AndroidDriver<>(this.url, capabilities);
        }

        private void ios(){
            capabilities.setCapability("platformName", "iOS");
            capabilities.setCapability("platformVersion", platformVersion);
            capabilities.setCapability("deviceName", this.devices);
            if (StringUtil.isNotBlank(app)) {
                capabilities.setCapability("app", app);
            }else {
                capabilities.setCapability("bundleId", bundleId);
            }
            // 自动授权 位置\联系人\图片等
            capabilities.setCapability("autoAcceptAlerts", true);
            driver = new IOSDriver(this.url, capabilities);
        }

        private boolean urlCheck(String url) {
            return PATTERN.matcher(url).find();
        }

        private void platformCheck(){
            if (StringUtil.isBlank(platformName)){
                throw new AppiumException("platformName == null");
            }
            if (StringUtil.equalsIgnoreCase(platformName, Config.ANDROID)
                    || StringUtil.equalsIgnoreCase(platformName, Config.IOS)){
                throw new AppiumException("platformName error: " + platformName);
            }
        }

        private void androidCheck() {
            if (StringUtil.isBlank(devices)){
                throw new AppiumException("devices == null");
            }
            if (StringUtil.isBlank(appPackage)){
                throw new AppiumException("appPackage == null");
            }
            if (StringUtil.isBlank(appActivity)){
                throw new AppiumException("appActivity == null");
            }
        }

        private void iosCheck(){
            if (StringUtil.isBlank(devices)){
                throw new AppiumException("devices == null");
            }
            if (StringUtil.isBlank(app) && StringUtil.isBlank(bundleId)){
                throw new AppiumException("app == null");
            }
        }
    }
}
