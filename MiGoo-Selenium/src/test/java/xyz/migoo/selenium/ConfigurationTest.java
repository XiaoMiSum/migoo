package xyz.migoo.selenium;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import xyz.migoo.exception.SeleniumException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationTest {

    Configuration configuration ;
    //Selenium s = new Selenium.Builder().driver(configuration).build();

    @Before
    public void before(){

    }

    @Test(expected = SeleniumException.class)
    public void browserNull(){
        configuration = new Configuration.Builder().build();
    }

    @Test(expected = SeleniumException.class)
    public void driverEmptyAdnIE(){
        configuration = new Configuration.Builder().ie().driver("").build();
    }

    @Test(expected = SeleniumException.class)
    public void driverNullAdnIE(){
        configuration = new Configuration.Builder().ie().driver(null).build();
    }

    @Test(expected = SeleniumException.class)
    public void driverEmptyAdnEdge(){
        configuration = new Configuration.Builder().edge().driver("").build();
    }

    @Test(expected = SeleniumException.class)
    public void driverNullAdnEdge(){
        configuration = new Configuration.Builder().edge().driver(null).build();
    }

    @Test(expected = SeleniumException.class)
    public void driverEmptyAdnFirefox(){
        configuration = new Configuration.Builder().firefox().driver("").build();
    }

    @Test(expected = SeleniumException.class)
    public void driverNullAdnFirefox(){
        configuration = new Configuration.Builder().firefox().driver(null).build();
    }

    @Test(expected = SeleniumException.class)
    public void driverEmptyAdnChrome(){
        configuration = new Configuration.Builder().chrome().driver("").build();
    }

    @Test(expected = SeleniumException.class)
    public void driverNullAdnChrome(){
        configuration = new Configuration.Builder().chrome().driver(null).build();
    }


    @Test(expected = SeleniumException.class)
    public void firefoxBinNull(){
        configuration = new Configuration.Builder().firefox().driver("/usr/usr").bin(null).build();
    }

    @Test(expected = SeleniumException.class)
    public void firefoxBinEmpty(){
        configuration = new Configuration.Builder().firefox().driver("/usr/usr").bin("").build();
    }

    @Test(expected = SeleniumException.class)
    public void profileNullAndFirefox(){
        configuration = new Configuration.Builder().firefox().driver("/usr/usr").profile(null).build();
    }

    @Test(expected = SeleniumException.class)
    public void profileEmptyAndFirefox(){
        configuration = new Configuration.Builder().firefox().driver("/usr/usr").profile("").build();
    }

    @Test(expected = SeleniumException.class)
    public void profileNullAndChrome(){
        configuration = new Configuration.Builder().chrome().driver("/usr/usr").profile(null).build();
    }

    @Test(expected = SeleniumException.class)
    public void profileEmptyAndChrome(){
        configuration = new Configuration.Builder().chrome().driver("/usr/usr").profile("").build();
    }

    @Test(expected = SeleniumException.class)
    public void htmlUnit(){
        configuration = new Configuration.Builder().htmlUnit(true, null).build();
    }

    public void test(){
        configuration = new Configuration.Builder().htmlUnit(true,BrowserVersion.FIREFOX_52).build();
        Assert.assertNotNull(configuration);
        configuration = new Configuration.Builder().chrome().driver("/usr/usr").profile("pp").build();
        Assert.assertNotNull(configuration);
        configuration = new Configuration.Builder().edge().driver("/usr/usr").build();
        Assert.assertNotNull(configuration);
        configuration = new Configuration.Builder().ie().driver("/usr/usr").build();
        Assert.assertNotNull(configuration);
        configuration = new Configuration.Builder().firefox().driver("/usr/usr").profile("pp").build();
        Assert.assertNotNull(configuration);
        configuration = new Configuration.Builder().safari().build();
        Assert.assertNotNull(configuration);
    }
}
