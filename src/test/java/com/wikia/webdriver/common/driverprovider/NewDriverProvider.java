package com.wikia.webdriver.common.driverprovider;


import com.wikia.webdriver.common.core.ExtHelper;
import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.core.helpers.Browser;
import com.wikia.webdriver.common.logging.PageObjectLogging;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class NewDriverProvider {

  private static EventFiringWebDriver driver;
  private static String browserName;
  private static DesiredCapabilities caps = new DesiredCapabilities();
  private static FirefoxProfile firefoxProfile;
  private static ChromeOptions chromeOptions = new ChromeOptions();
  private static UserAgentsRegistry userAgentRegistry = new UserAgentsRegistry();
  private static boolean unstablePageLoadStrategy = false;
  private static AndroidDriver mobileDriver;

  private NewDriverProvider() {

  }

  public static EventFiringWebDriver getDriverInstanceForBrowser(String browser) {
    browserName = browser;

    // If browser equals IE set driver property as IEWebDriver instance
    if (Browser.INTERNET_EXPLORER.equals(browserName)) {
      driver = getIEInstance();

      // If browser contains FF set driver property as FFWebDriver instance
    } else if (Browser.FIREFOX.equals(browserName)) {
      driver = getFFInstance();

      // If browser equals CHROME set driver property as ChromeWebDriver instance
    } else if (browserName.contains(Browser.CHROME)) {
      driver = getChromeInstance();

      // If browser equals SAFARI set driver property as SafariWebDriver instance
    } else if (Browser.SAFARI.equals(browserName)) {
      driver = getSafariInstance();

    } else if (Browser.HTML_UNIT.equals(browserName)) {
      driver = new EventFiringWebDriver(new HtmlUnitDriver());
    } else if (Browser.GHOST.equals(browserName)) {
      driver = getPhantomJSInstance();
    } else if (Browser.CHROME_ANDROID.equals(browserName)) {
      driver = getAndroidInstance();
    } else {
      throw new WebDriverException("Provided driver is not supported.");
    }

    driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    return driver;
  }

  public static void setBrowserUserAgent(String browser, String userAgent) {
    switch (browser.toUpperCase()) {
      case Browser.FIREFOX:
        setFFUserAgent(userAgent);
        break;
      case Browser.CHROME:
        setChromeUserAgent(userAgent);
        break;
      default:
        throw new WebDriverException("Wrong browser provided. Browser " + browser + " not known");
    }
  }

  public static WebDriver getWebDriver() {
    return driver;
  }

  private static EventFiringWebDriver getIEInstance() {
    File file =
        new File("." + File.separator + "src" + File.separator + "test" + File.separator
                 + "resources" + File.separator + "IEDriver" + File.separator
                 + "IEDriverServer.exe");
    System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
    return new EventFiringWebDriver(new InternetExplorerDriver(caps));
  }

  private static EventFiringWebDriver getAndroidInstance() {
    DesiredCapabilities destCaps = new DesiredCapabilities();
    destCaps.setCapability("deviceName", Configuration.getDeviceName());
    URL url = null;
    try {
      url = new URL("http://" + Configuration.getAppiumIp() + "/wd/hub");
    } catch (MalformedURLException e) {
      PageObjectLogging.log("getAndroindInstance", e, false);
    }
    mobileDriver = new AndroidDriver(url, destCaps);

    return new EventFiringWebDriver(mobileDriver);
  }

  private static EventFiringWebDriver getFFInstance() {
    // Windows 8 requires to set webdriver.firefox.bin system variable
    // to path where executive file of FF is placed
    if ("WINDOWS 8".equalsIgnoreCase(System.getProperty("os.name"))) {
      System.setProperty("webdriver.firefox.bin", "c:" + File.separator + "Program Files (x86)"
                                                  + File.separator + "Mozilla Firefox"
                                                  + File.separator + "Firefox.exe");
    }

    // Check if user who is running tests have write access in ~/.mozilla dir and home dir
    if ("LINUX".equals(System.getProperty("os.name").toUpperCase())) {
      File homePath = new File(System.getenv("HOME") + File.separator);
      File mozillaPath = new File(homePath + File.separator + ".mozilla");
      File tmpFile;
      if (mozillaPath.exists()) {
        try {
          tmpFile = File.createTempFile("webdriver", null, mozillaPath);
        } catch (IOException ex) {
          PageObjectLogging.log("Can't create file", ex, false);
          throw new WebDriverException("Can't create file in path: %s".replace("%s",
                                                                               mozillaPath
                                                                                   .getAbsolutePath()));
        }
      } else {
        try {
          tmpFile = File.createTempFile("webdriver", null, homePath);
        } catch (IOException ex) {
          PageObjectLogging.log("Can't create file", ex, false);
          throw new WebDriverException("Can't create file in path: %s".replace("%s",
                                                                               homePath
                                                                                   .getAbsolutePath()));
        }
      }
      tmpFile.delete();
    }

    firefoxProfile =
        new FirefoxProfile(
            new File(ClassLoader.getSystemResource("FirefoxProfiles/Default").getPath()));

    if (unstablePageLoadStrategy) {
      firefoxProfile.setPreference("webdriver.load.strategy", "unstable");
    }

    if ("true".equals(Configuration.getDisableFlash())) {
      firefoxProfile.setPreference("plugin.state.flash", 0);
    }

    caps.setCapability(FirefoxDriver.PROFILE, firefoxProfile);

    // Adding console logging for FF browser
    setBrowserLogging(Level.SEVERE);

    ExtHelper.addExtensions(Configuration.getExtensions());

    return new EventFiringWebDriver(new FirefoxDriver(caps));
  }

  private static void setFFUserAgent(String userAgent) {
    firefoxProfile.setPreference("general.useragent.override",
                                 userAgentRegistry.getUserAgent(userAgent));
  }

  private static EventFiringWebDriver getChromeInstance() {
    String chromeBinaryPath = "";
    String osName = System.getProperty("os.name").toUpperCase();
    String emulator = Configuration.getEmulator();

    if (osName.contains("WINDOWS")) {
      chromeBinaryPath = "/chromedriver_win32/chromedriver.exe";
    } else if (osName.contains("MAC")) {
      chromeBinaryPath = "/chromedriver_mac32/chromedriver";

      File chromedriver =
          new File(ClassLoader.getSystemResource("ChromeDriver" + chromeBinaryPath).getPath());

      // set application user permissions to 455
      chromedriver.setExecutable(true);
    } else if (osName.contains("LINUX")) {
      chromeBinaryPath = "/chromedriver_linux64/chromedriver";

      File chromedriver =
          new File(ClassLoader.getSystemResource("ChromeDriver" + chromeBinaryPath).getPath());

      // set application user permissions to 455
      chromedriver.setExecutable(true);
    }

    System.setProperty("webdriver.chrome.driver",
                       new File(ClassLoader.getSystemResource("ChromeDriver" + chromeBinaryPath)
                                    .getPath())
                           .getPath());

    // TODO change mobile tests to use @UserAgent annotation
    if (Browser.CHROME_MOBILE_MERCURY.equals(browserName)) {
      chromeOptions
          .addArguments("--user-agent=" + userAgentRegistry.getUserAgent("iPhone"));
    }

//    chromeOptions.addArguments("user-data-dir="
//        + ClassLoader.getSystemResource("ChromeProfiles/default/").getPath().substring(1));

    if ("true".equals(Configuration.getDisableFlash())) {
      chromeOptions.addArguments("disable-bundled-ppapi-flash");
      chromeOptions.addArguments("process-per-site");
      chromeOptions.addArguments("start-maximized");
      chromeOptions.addArguments("disable-notifications");
    }

    if (!"null".equals(emulator)) {
      Map<String, String> mobileEmulation = new HashMap<>();
      mobileEmulation.put("deviceName", emulator);
      chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
    }

    caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

    setBrowserLogging(Level.SEVERE);

    ExtHelper.addExtensions(Configuration.getExtensions());

    return new EventFiringWebDriver(new ChromeDriver(caps));
  }

  private static void setChromeUserAgent(String userAgent) {
    chromeOptions.addArguments("--user-agent=" + userAgentRegistry.getUserAgent(userAgent));
    caps.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
  }

  private static EventFiringWebDriver getPhantomJSInstance() {
    String phantomJSBinaryName;
    String OSName = System.getProperty("os.name").toUpperCase();

    if (OSName.contains("WINDOWS")) {
      phantomJSBinaryName = "phantomjs.exe";

      File phantomJSBinary =
          new File("." + File.separator + "src" + File.separator + "test" + File.separator
                   + "resources" + File.separator + "PhantomJS" + File.separator
                   + phantomJSBinaryName);

      caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                         phantomJSBinary.getAbsolutePath());
    }

    return new EventFiringWebDriver(new PhantomJSDriver(caps));
  }

  private static EventFiringWebDriver getSafariInstance() {
    /*
     * clone following repository https://github.com/senthilnayagam/safari-webdriver.git
     * webdriver.safari.driver property should be set to path to the SafariDriver.safariextz file
     */
    System.setProperty("webdriver.safari.driver", "");
    return new EventFiringWebDriver(new SafariDriver());
  }

  public static void setDriverCapabilities(DesiredCapabilities newCaps) {
    caps = newCaps;
  }

  private static void setBrowserLogging(Level logLevel) {
    LoggingPreferences loggingprefs = new LoggingPreferences();
    loggingprefs.enable(LogType.BROWSER, logLevel);
    caps.setCapability(CapabilityType.LOGGING_PREFS, loggingprefs);
  }

  public static void setUnstablePageLoadStrategy(boolean value) {
    unstablePageLoadStrategy = value;
  }

  public static AndroidDriver getMobileDriver() {
    return mobileDriver;
  }

  public static FirefoxProfile getFirefoxProfile() {
    return firefoxProfile;
  }

  public static ChromeOptions getChromeOptions() {
    return chromeOptions;
  }
}
