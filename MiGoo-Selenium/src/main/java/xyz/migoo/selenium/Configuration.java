package xyz.migoo.selenium;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import xyz.migoo.exception.SeleniumException;
import xyz.migoo.utils.StringUtil;

import static org.openqa.selenium.remote.BrowserType.*;
import static xyz.migoo.config.Config.WINDOWS;

/**
 * @author xiaomi
 */
public class Configuration {

    private String browser;
    private String driver;
    private String profile;
    private String bin;
    private boolean enableJavascript;
    private BrowserVersion version;

    public Configuration(Builder builder) {
        this.browser = builder.browser;
        this.driver = builder.driver;
        this.profile = builder.profile;
        this.bin = builder.bin;
        this.enableJavascript = builder.enableJavascript;
        this.version = builder.version;
    }

    public String browser(){
        return browser;
    }

    public String driver(){
        return driver;
    }

    public String profile(){
        return profile;
    }

    public String bin(){
        return bin;
    }

    public boolean isEnableJavascript() {
        return enableJavascript;
    }

    public BrowserVersion version() {
        return version;
    }


    public static class Builder {
        private String browser;
        private String driver;
        private String profile;
        private String bin;
        private boolean enableJavascript;
        private BrowserVersion version;

        public Builder() {
        }

        public Builder chrome() {
            this.browser = CHROME;
            return this;
        }

        public Builder firefox() {
            this.browser = FIREFOX;
            return this;
        }

        public Builder safari() {
            this.browser = SAFARI;
            return this;
        }

        public Builder ie() {
            this.browser = IE;
            return this;
        }

        public Builder edge() {
            this.browser = EDGE;
            return this;
        }

        public Builder htmlUnit(boolean enableJavascript, BrowserVersion version) {
            this.browser = HTMLUNIT;
            this.enableJavascript = enableJavascript;
            this.version = version;
            return this;
        }

        public Builder driver(String driver) {
            this.driver = driver;
            return this;
        }

        public Builder bin(String bin) {
            this.bin = bin;
            return this;
        }

        public Builder profile(String profile) {
            this.profile = profile;
            return this;
        }

        public Configuration build() {
            if (StringUtil.isBlank(this.browser)) {
                throw new SeleniumException("browser == null");
            }
            if (this.permitsDriver(this.browser) && StringUtil.isBlank(this.driver)
                    && WINDOWS) {
                throw new SeleniumException("driver == null");
            }
            if (this.isFirefox(this.browser) && StringUtil.isBlank(this.bin)) {
                throw new SeleniumException("fireFox bin == null");
            }
            if (this.permitsProfile(this.browser) && StringUtil.isBlank(this.profile)
                    && WINDOWS){
                throw new SeleniumException("profile path == null");
            }
            if (this.isHtmlUnit(this.browser) && this.version == null){
                throw new SeleniumException("version == null");
            }
            return new Configuration(this);
        }

        private boolean isFirefox(String browser) {
            return browser.equals(FIREFOX);
        }

        private boolean isHtmlUnit(String browser) {
            return browser.equals(HTMLUNIT);
        }

        private boolean permitsProfile(String browser) {
            return browser.equals(CHROME);
        }

        private boolean permitsDriver(String browser) {
            return browser.equals(FIREFOX) || browser.equals(CHROME)
                    || browser.equals(IE) || browser.equals(EDGE);
        }
    }
}
