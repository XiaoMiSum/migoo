package xyz.migoo.appium;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
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
        private String appPackage;
        private String appActivity;
        private AppiumDriver driver;

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
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("deviceName", this.devices);

            // 设置app的主包名和主类名
            capabilities.setCapability("appPackage", this.appPackage);
            capabilities.setCapability("appActivity", this.appActivity);

            // 支持中文
            capabilities.setCapability("unicodeKeyboard", "true");
            capabilities.setCapability("resetKeyboard", "true");

            driver = new AndroidDriver<>(this.url, capabilities);
        }

        private void ios(){

        }

        private boolean urlCheck(String url) {
            return PATTERN.matcher(url).find();
        }

        private void androidCheck() {
            if (StringUtil.isNotBlank(devices)){
                throw new AppiumException("devices == null");
            }
            if (StringUtil.isNotBlank(appPackage)){
                throw new AppiumException("devices == null");
            }
            if (StringUtil.isNotBlank(appActivity)){
                throw new AppiumException("devices == null");
            }
        }

        private void iosCheck() {
        }
    }


}
