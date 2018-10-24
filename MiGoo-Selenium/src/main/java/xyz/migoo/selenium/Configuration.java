package xyz.migoo.selenium;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaDriverService;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import xyz.migoo.exception.SeleniumException;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.TypeUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import static org.openqa.selenium.remote.BrowserType.*;

/**
 * @author xiaomi
 */
public class Configuration {

    private WebDriver driver;

    private Configuration(Builder builder) {
        this.driver = builder.webDriver;
    }

    public WebDriver driver() {
        return driver;
    }

    public static class Builder {
        private static final Pattern PATTERN = Pattern.compile(
                "^http[s]*://[\\w\\.\\-]+(:\\d*)*(?:/|(?:/[\\w\\.\\-]+)*)?$", Pattern.CASE_INSENSITIVE);

        private URL url;
        private String browser;
        private String driver;
        private File bin;
        private WebDriver webDriver;
        private BrowserVersion version;
        private Boolean headless = Boolean.FALSE;

        public Builder() {
        }

        public Builder remote(String url) {
            if (urlCheck(url)) {
                try {
                    this.url = new URL(url);
                } catch (MalformedURLException e) {
                    throw new SeleniumException("remote url == null");
                }
            }
            return this;
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

        public Builder opera() {
            this.browser = OPERA_BLINK;
            return this;
        }

        public Builder htmlUnit() {
            this.browser = HTMLUNIT;
            return this;
        }

        public Builder htmlUnit(BrowserVersion version) {
            this.browser = HTMLUNIT;
            this.version = version;
            return this;
        }

        public Builder driver(String driver) {
            this.driver = driver;
            return this;
        }

        public Builder bin(String bin) {
            if (StringUtil.isNotBlank(bin)) {
                this.bin = new File(bin);
            }
            if (!this.bin.exists() || this.bin.isDirectory()) {
                throw new SeleniumException("bin not exists or is directory");
            }
            return this;
        }

        public Builder headless(Object headless) {
            this.headless = TypeUtil.booleanOf(headless);
            if (this.headless == null) {
                this.headless = Boolean.FALSE;
            }
            return this;
        }

        public Configuration build() {
            if (StringUtil.isBlank(this.browser)) {
                throw new SeleniumException("browser == null");
            }
            this.driver();
            return new Configuration(this);
        }

        private void driver() {
            if (url != null) {
                this.remote();
            } else {
                if (driver != null) {
                    this.system();
                }
                this.local();
            }
        }

        private void remote() {
            DesiredCapabilities capabilities;
            switch (browser) {
                case FIREFOX:
                    capabilities = DesiredCapabilities.firefox();
                    break;
                case CHROME:
                    capabilities = DesiredCapabilities.chrome();
                    break;
                case EDGE:
                    capabilities = DesiredCapabilities.edge();
                    break;
                case SAFARI:
                    capabilities = DesiredCapabilities.safari();
                    break;
                case IE:
                    capabilities = DesiredCapabilities.internetExplorer();
                    break;
                case OPERA_BLINK:
                    capabilities = DesiredCapabilities.operaBlink();
                    break;
                case HTMLUNIT:
                    capabilities = DesiredCapabilities.htmlUnit();
                    break;
                default:
                    throw new SeleniumException("driver == null. please check configuration");
            }
            this.webDriver = new RemoteWebDriver(url, capabilities);
        }

        private void local() {
            switch (browser) {
                case FIREFOX:
                    this.firefoxOptions();
                    break;
                case CHROME:
                    this.chromeOptions();
                    break;
                case EDGE:
                    this.edgeOptions();
                    break;
                case SAFARI:
                    this.safariOptions();
                    break;
                case IE:
                    this.ieOptions();
                    break;
                case OPERA_BLINK:
                    this.operaOptions();
                    break;
                case HTMLUNIT:
                    this.htmlUnitOptions();
                    break;
                default:
                    throw new SeleniumException("driver == null. please check configuration");
            }
        }

        private void system() {
            switch (browser) {
                case FIREFOX:
                    System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, driver);
                    break;
                case CHROME:
                    System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, driver);
                    break;
                case EDGE:
                    System.setProperty(EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY, driver);
                    break;
                case SAFARI:
                    break;
                case IE:
                    System.setProperty(InternetExplorerDriverService.IE_DRIVER_EXE_PROPERTY, driver);
                    break;
                case OPERA_BLINK:
                    System.setProperty(OperaDriverService.OPERA_DRIVER_EXE_PROPERTY, driver);
                    break;
                case HTMLUNIT:
                    break;
                default:
                    System.setProperty(GeckoDriverService.GECKO_DRIVER_EXE_PROPERTY, driver);
            }
        }

        private void firefoxOptions() {
            FirefoxOptions options = new FirefoxOptions();
            if (bin != null) {
                FirefoxBinary binary = new FirefoxBinary(bin);
                options.setBinary(binary);
            }
            options.setHeadless(headless.booleanValue());
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            webDriver = new FirefoxDriver(options);
        }

        private void chromeOptions() {
            ChromeOptions options = new ChromeOptions();
            if (bin != null) {
                options.setBinary(bin);
            }
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            options.setHeadless(headless.booleanValue());
            webDriver = new ChromeDriver(options);
        }

        private void edgeOptions() {
            EdgeOptions options = new EdgeOptions();
            options.setPageLoadStrategy("normal");
            webDriver = new EdgeDriver(options);
        }

        private void safariOptions() {
            SafariOptions options = new SafariOptions();
            webDriver = new SafariDriver(options);
        }

        private void ieOptions() {
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            webDriver = new InternetExplorerDriver(options);
        }

        private void operaOptions() {
            OperaOptions options = new OperaOptions();
            if (bin != null) {
                options.setBinary(bin);
            }
            webDriver = new OperaDriver(options);
        }

        private void htmlUnitOptions(){
            if (version != null){
                webDriver = new HtmlUnitDriver(version, true);
            }else {
                webDriver = new HtmlUnitDriver(true);
            }
        }

        private boolean urlCheck(String url) {
            return PATTERN.matcher(url).find();
        }
    }
}
