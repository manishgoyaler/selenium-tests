package com.wikia.webdriver.pageobjectsfactory.pageobject.adsbase;

import com.wikia.webdriver.common.contentpatterns.AdsContent;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.CommonExpectedConditions;
import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.pageobjectsfactory.pageobject.WikiBasePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.adsbase.helpers.AdsComparison;
import com.wikia.webdriver.pageobjectsfactory.pageobject.adsbase.helpers.AdsSkinHelper;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdsBaseObject extends WikiBasePageObject {

  protected static final String FLITE_MASK_CSS_SELECTOR = ".flite-mask";
  // Constants
  private static final int MIN_MIDDLE_COLOR_PAGE_WIDTH = 1600;
  private static final int PROVIDER_CHAIN_TIMEOUT_SEC = 30;
  private static final String HOP_AD_TYPE = "AdEngine_adType='collapse';";
  private static final String[] GPT_DATA_ATTRIBUTES = {
      "data-gpt-line-item-id",
      "data-gpt-creative-id",
      "data-gpt-creative-size",
  };
  private static final String[] SPOTLIGHT_SLOTS = {
      "#SPOTLIGHT_FOOTER_1",
      "#SPOTLIGHT_FOOTER_2",
      "#SPOTLIGHT_FOOTER_3",
  };
  private static final String[] PROVIDERS = {
      "DirectGpt",
      "DirectGptMobile",
      "RemnantGpt",
      "RemnantGptMobile",
      "Liftium",
  };
  private static final String LIFTIUM_IFRAME_SELECTOR = "iframe[id*='Liftium']";
  private static final String GPT_DIV_SELECTOR = "[data-gpt-creative-size]";
  private static final String INCONTENT_BOXAD_SELECTOR = "div[id*='INCONTENT_1']";
  private static final String ARTICLE_COMMENTS_CSS_SELECTOR = "#WikiaArticleFooter";
  private static final String MIDDLE_PREFOOTER_CSS_SELECTOR = "#PREFOOTER_MIDDLE_BOXAD";

  protected String presentLeaderboardSelector = "div[id*='TOP_LEADERBOARD']";
  protected String presentHighImpactSlotSelector = "div[id*='INVISIBLE_HIGH_IMPACT']";

  @FindBy(css = "div[id*='TOP_LEADERBOARD']")
  protected WebElement presentLeaderboard;
  @FindBy(css = "div[id*='TOP_RIGHT_BOXAD']")
  private WebElement presentMedrec;
  @FindBy(css = INCONTENT_BOXAD_SELECTOR)
  private WebElement incontentBoxad;
  @FindBy(css = LIFTIUM_IFRAME_SELECTOR)
  private List<WebElement> liftiumIframes;
  @FindBy(css = MIDDLE_PREFOOTER_CSS_SELECTOR)
  private WebElement middlePrefooter;

  public AdsBaseObject(WebDriver driver) {
    super(driver);
  }

  public AdsBaseObject(WebDriver driver, String page) {
    super(driver);
    getUrl(page, true);
  }

  public AdsBaseObject(WebDriver driver, String testedPage, Dimension resolution) {
    super(driver);
    driver.manage().window().setSize(resolution);
    getUrl(testedPage, true);
  }

  public AdsBaseObject(WebDriver driver, Dimension resolution) {
    super(driver);
    driver.manage().window().setSize(resolution);
  }

  public void verifyForcedSuccessScriptInSlots(List<String> slots) {
    for (String slot : slots) {
      WebElement slotElement = driver.findElement(By.id(slot));
      WebElement slotGptIframe = slotElement.findElement(By.cssSelector("div > iframe"));
      driver.switchTo().frame(slotGptIframe);
      WebElement iframeHtml = driver.findElement(By.tagName("html"));
      String adDriverForcedSuccessFormatted = String.format(
          AdsContent.AD_DRIVER_FORCED_STATUS_SUCCESS_SCRIPT, slot
      );
      if (checkScriptPresentInElement(iframeHtml, adDriverForcedSuccessFormatted)) {
        PageObjectLogging.log(
            "AdDriver2ForceStatus script",
            "adDriverForcedSuccess script found in slot " + slot,
            true
        );
      } else {
        throw new NoSuchElementException(
            "AdDriver2ForcedStatus script not found in slot " + slot
        );
      }
      driver.switchTo().defaultContent();
    }
  }

  public void verifyMedrec() {
    verifyAdVisibleInSlot("div[id*='TOP_RIGHT_BOXAD']", presentMedrec);
  }

  public void verifyTopLeaderboard() {
    if (!checkIfSlotExpanded(presentLeaderboard) && isElementOnPage(
        By.cssSelector("#jpsuperheader"))) {
      PageObjectLogging.logWarning("Special ad", "Ad in #jpsuperheader detected");
      return;
    }
    verifyAdVisibleInSlot(presentLeaderboardSelector, presentLeaderboard);
  }

  public void verifyIncontentBoxad() {
    verifyAdVisibleInSlot(INCONTENT_BOXAD_SELECTOR, incontentBoxad);
  }

  public void verifyFliteTag(String cssFliteSelector) {
    jsActions.scrollToElement(wait.forElementVisible(By.cssSelector(cssFliteSelector)));
    WebElement fliteTag = driver.findElement(By.cssSelector(cssFliteSelector));
    verifySlotExpanded(fliteTag);
  }

  public void verifyFliteTagBroken(String error, String cssFliteBrokenSelector) {
    jsActions.scrollToElement(wait.forElementVisible(By.cssSelector(cssFliteBrokenSelector)));
    WebElement fliteTagBroken = driver.findElement(By.cssSelector(cssFliteBrokenSelector));
    Assertion.assertEquals(fliteTagBroken.getText(), error);
  }

  public void verifyHubTopLeaderboard() throws Exception {
    String hubLBName = AdsContent.HUB_LB;
    WebElement
        hubLB =
        driver.findElement(By.cssSelector(AdsContent.getSlotSelector(hubLBName)));
    verifyScriptPresentInSlotScripts(hubLBName, hubLB);
    PageObjectLogging.log("HUB_TOP_LEADERBOARD found", "HUB_TOP_LEADERBOARD found", true);

    WebElement
        hubGPTLB =
        hubLB.findElement(By.cssSelector(AdsContent.getSlotSelector(AdsContent.HUB_LB_GPT)));
    PageObjectLogging
        .log("HUB_TOP_LEADERBOARD_gpt found", "HUB_TOP_LEADERBOARD_gpt found", true);

    if (hubGPTLB.findElements(By.cssSelector("iframe")).size() > 1) {
      PageObjectLogging
          .log("IFrames found", "2 IFrames found in HUB_TOP_LEADERBOAD_gpt div", true);
    } else {
      PageObjectLogging.log(
          "IFrames not found",
          "2 IFrames expected to be found in HUB_TOP_LEADERBOAD_gpt div, found less",
          false, driver
      );
      throw new NoSuchElementException("IFrames inside GPT div not found!");
    }
  }

  public void verifyNoLiftiumAdsOnPageExceptWikiaBar() {
    scrollToSelector(AdsContent.getSlotSelector(AdsContent.ADS_IN_CONTENT_CONTAINER));
    scrollToSelector(AdsContent.getSlotSelector(AdsContent.PREFOOTERS_CONTAINER));
    if (isElementOnPage(By.cssSelector(LIFTIUM_IFRAME_SELECTOR))) {
      String iframeSrc = liftiumIframes.get(0).getAttribute("src");
      if (liftiumIframes.size() == 1 && iframeSrc.contains("WIKIA_BAR_BOXAD_1")) {
        PageObjectLogging
            .log("LiftiumAdsNotFound", "Liftium ads not found except WikiaBar", true);
      } else {
        throw new WebDriverException("Liftium ads found!");
      }
    } else {
      PageObjectLogging.log("LiftiumAdsNotFound", "Liftium ads not found", true);
    }
  }

  public void verifyNoAdsOnPage() {
    scrollToSelector(AdsContent.getSlotSelector(AdsContent.ADS_IN_CONTENT_CONTAINER));
    scrollToSelector(AdsContent.getSlotSelector(AdsContent.PREFOOTERS_CONTAINER));
    verifyNoAds();
    PageObjectLogging.log(
        "verifyNoAdsOnPage",
        "No ads detected",
        true,
        driver
    );
  }

  public void verifyNoAdsOnMobilePage() {
    scrollToSelector(AdsContent.getSlotSelector(AdsContent.MOBILE_AD_IN_CONTENT));
    scrollToSelector(AdsContent.getSlotSelector(AdsContent.MOBILE_PREFOOTER));
    verifyNoAds();
    PageObjectLogging.log(
        "verifyNoAdsOnMobilePage",
        "No ads detected",
        true,
        driver
    );
  }

  public void verifyNoLiftiumAdsInSlots(List<String> slots) {
    for (String slot : slots) {
      WebElement slotElement = driver.findElement(By.id(slot));
      if (isElementInContext(LIFTIUM_IFRAME_SELECTOR, slotElement)) {
        throw new NoSuchElementException("Liftium found in slot " + slot);
      } else {
        PageObjectLogging.log("LiftiumNotFound", "Liftium not found in slot " + slot, true);
      }
    }
  }

  /**
   * Test whether the correct GPT ad parameters are passed
   *
   * @param slotName   Slotname
   * @param pageParams List of gpt page-level params to test
   * @param slotParams List of gpt slot-level params to test
   */
  public void verifyGptParams(String slotName, List<String> pageParams,
                              List<String> slotParams) {
    String dataGptPageParams = getGptParams(slotName, "data-gpt-page-params");
    String dataGptSlotParams = getGptParams(slotName, "data-gpt-slot-params");

    for (String param : pageParams) {
      Assertion.assertStringContains(dataGptPageParams, param);
    }

    for (String param : slotParams) {
      Assertion.assertStringContains(dataGptSlotParams, param);
    }

    PageObjectLogging.log(
        "verifyGptParams",
        "All page-level and slot-level params present as expected " + dataGptPageParams + ", "
        + dataGptSlotParams,
        true,
        driver
    );
  }

  /**
   * Test whether the correct GPT ad parameters are passed
   *
   * @param slotName   Slotname
   * @param lineItemId expected line item id
   * @param creativeId expected creative id
   */
  public void verifyGptAdInSlot(String slotName, String lineItemId, String creativeId) {

    Assertion.assertEquals(getGptParams(slotName, "data-gpt-line-item-id"), lineItemId);

    if (creativeId.length() > 0) {
      Assertion.assertEquals(getGptParams(slotName, "data-gpt-creative-id"), creativeId);
    }

    PageObjectLogging.log(
        "verifyGptAdInSlot",
        "Line item id loaded: " + lineItemId + ", creativeId:" + creativeId,
        true,
        driver
    );
  }

  public void verifySpotlights() {
    // Removing comments section as it expands content downwards
    hideElementIfPresent(ARTICLE_COMMENTS_CSS_SELECTOR);

    AdsComparison adsComparison = new AdsComparison();

    jsActions.scrollToElement(wait.forElementVisible(By.cssSelector("#SPOTLIGHT_FOOTER")));

    for (String spotlightSelector : SPOTLIGHT_SLOTS) {
      WebElement slot = wait.forElementVisible(By.cssSelector(spotlightSelector + " img"));
      verifySlotExpanded(slot);

      Assertion.assertTrue(adsComparison.isAdVisible(slot, spotlightSelector, driver));
    }
  }

  public AdsBaseObject verifySize(String slotName, String src, int slotWidth, int slotHeight) {
    waitForElementToHaveSize(slotWidth, slotHeight, getIframe(slotName, src));

    PageObjectLogging.log("verifySize",
                          slotName + " has width: " + slotWidth + ";height: " + slotHeight,
                          true,
                          driver);
    return this;
  }

  public AdsBaseObject verifyLineItemId(String slotName, int lineItemId) {
    String lineItemParam = getGptParams(slotName, GPT_DATA_ATTRIBUTES[0]);
    Assertion.assertStringContains(lineItemParam, String.valueOf(lineItemId));
    PageObjectLogging
        .log("verifyLineItemId", slotName + " has following line item: " + lineItemParam, true);
    return this;
  }

  public AdsBaseObject verifyAdImage(String slotName, String src, String imageUrl) {
    WebElement element = getIframe(slotName, src);
    Assertion.assertTrue(
        new AdsComparison().compareImageWithScreenshot(imageUrl, element, driver),
        "The image is different than the sample"
    );
    PageObjectLogging.log("verifyAdImage", "Ad looks good", true, driver);

    return this;
  }

  public AdsBaseObject verifyProvidersChain(String slotName, String providers) {
    PageObjectLogging.log("SlotName", slotName, true);
    waitForProvidersChain(slotName, providers, PROVIDER_CHAIN_TIMEOUT_SEC);
    return this;
  }

  /**
   * Test whether the correct GPT ad unit is called
   *
   * @param adUnit the ad unit passed to GPT, like wka.wikia/_wikiaglobal//home
   */
  public void verifyGptIframe(String adUnit, String slotName, String src) {
    String iframeId = "google_ads_iframe_/5441/" + adUnit + "/" + src + "/" + slotName + "_0";
    By cssSelector = By.cssSelector("iframe[id^='" + iframeId + "']");

    wait.forElementPresent(cssSelector);

    String msg = "GPT iframe #" + iframeId + " found in slot " + slotName;
    PageObjectLogging.log("verifyGptIframe", msg, true, driver);

    waitForIframeLoaded(driver.findElement(cssSelector));

    msg = "Received \"load\" event from GPT iframe #" + iframeId + "  in slot " + slotName;
    PageObjectLogging.log("verifyGptIframe", msg, true, driver);
  }

  public AdsBaseObject refresh(int times) {
    for (int i = 0; i < times; i++) {
      refreshPage();
    }
    return this;
  }

  public void waitForSlotCollapsed(WebElement slot) {
    waitForElementToHaveSize(0, 0, slot);
  }

  public void waitForSlotExpanded(final WebElement slot) {
    waitFor.until(new ExpectedCondition<Object>() {
      @Override
      public Object apply(WebDriver webDriver) {
        return checkIfSlotExpanded(slot);
      }
    });
  }

  public String getGptParams(String slotName, String attr) {
    WebElement
        adsDiv =
        driver.findElement(
            By.cssSelector("div[id*='wikia_gpt'][id*='" + slotName + "'][" + attr + "]"));
    return adsDiv.getAttribute(attr);
  }

  public String getGptPageParams(String slotName) {
    return getGptParams(slotName, "data-gpt-page-params");
  }

  public void verifyMonocolorAd(String slotName) {
    String slotSelector = AdsContent.getSlotSelector(slotName);
    WebElement slot = driver.findElement(By.cssSelector(slotSelector));
    waitForSlotExpanded(slot);
    Assertion.assertFalse(new AdsComparison().isAdVisible(slot, slotSelector, driver));
  }

  public AdsBaseObject waitForPageLoaded() {
    waitForJavaScriptTruthy("document.readyState === 'complete'");
    return this;
  }

  /**
   * Mercury is a single page application (SPA) and if you want to test navigating between different
   * pages in the application you might want to use this method after clicking anything which is not
   * on the first page.
   *
   * First page in Mercury loads just as a regular web page but next articles in Mercury just change
   * part of loaded DOM. We tried few things but waiting for the page title to change was so far the
   * best way to make sure we can move on with our tests.
   */
  public void waitTitleChangesTo(String desiredArticleTitle) {
    driver.manage().timeouts().implicitlyWait(250, TimeUnit.MILLISECONDS);
    try {
      waitFor.until(
          ExpectedConditions.titleContains(desiredArticleTitle)
      );
    } finally {
      restoreDeaultImplicitWait();
    }
  }

  public String getCountry() {
    waitForJavaScriptTruthy("Wikia.geo");
    return (String) ((JavascriptExecutor) driver).executeScript(
        "return Wikia.geo.getCountryCode();"
    );
  }

  public AdsBaseObject addToUrl(String param) {
    appendToUrl(param);
    waitForPageLoaded();
    return this;
  }

  public void verifySkin(String adSkinLeftPath,
                         String adSkinRightPath,
                         String backgroundColor,
                         String middleColor) {
    AdsSkinHelper skinHelper = new AdsSkinHelper(adSkinLeftPath, adSkinRightPath, driver);
    Assertion.assertTrue(skinHelper.skinPresent());
    PageObjectLogging.log("SKIN", "SKIN presents on the page", true);

    if (!Strings.isNullOrEmpty(backgroundColor)) {
      Assertion.assertEquals(skinHelper.getBackgroundColor(), backgroundColor);
      PageObjectLogging.log("SKIN", "SKIN has correct background color", true);
    }

    if (!Strings.isNullOrEmpty(middleColor) &&
        getWindowSize().getWidth() > MIN_MIDDLE_COLOR_PAGE_WIDTH) {
      Assertion.assertEquals(skinHelper.getMiddleColor(), middleColor);
      PageObjectLogging.log("SKIN", "SKIN has correct middle color", true);
    }
  }

  /**
   * Check if AdEngine loaded the ad web elements inside slot.
   */
  public boolean checkSlotOnPageLoaded(String slotName) {
    changeImplicitWait(250, TimeUnit.MILLISECONDS);
    try {
      String slotSelector = AdsContent.getSlotSelector(slotName);
      WebElement slot = driver.findElement(By.cssSelector(slotSelector));
      if (slotName.equals(AdsContent.FLOATING_MEDREC)) {
        tryTriggerFloatingMedrec(slot);
      }
      List<WebElement> adWebElements = slot.findElements(By.cssSelector("div"));
      return adWebElements.size() > 1;
    } finally {
      restoreDeaultImplicitWait();
    }
  }

  private Optional<WebElement> getLastGptDiv(String slotSelector) {
    WebElement slot = driver.findElement(By.cssSelector(slotSelector));

    if (isElementInContext(GPT_DIV_SELECTOR, slot)) {
      List<WebElement> gptDivs = slot.findElements(By.cssSelector(GPT_DIV_SELECTOR));
      return Optional.of(gptDivs.get(gptDivs.size() - 1));
    }

    return Optional.absent();
  }

  protected String getSlotImageAd(WebElement slot) {
    WebElement iframeWithAd = slot.findElement(
        By.cssSelector("div > iframe:not([id*='hidden'])")
    );
    driver.switchTo().frame(iframeWithAd);
    String imageAd = driver.findElement(
        By.cssSelector("img")
    ).getAttribute("src");
    driver.switchTo().defaultContent();
    return imageAd;
  }

  protected boolean checkIfSlotExpanded(WebElement slot) {
    return slot.getSize().getHeight() > 1 && slot.getSize().getWidth() > 1;
  }

  private void waitForElementToHaveSize(int width, int height, WebElement element) {
    changeImplicitWait(250, TimeUnit.MILLISECONDS);
    try {
      waitFor.until(CommonExpectedConditions.elementToHaveSize(element, width, height));
    } finally {
      restoreDeaultImplicitWait();
    }
  }

  private void waitForIframeLoaded(WebElement iframe) {
    PageObjectLogging.log("waitForIframeLoaded", "Switching to adslot iframe", true);
    driver.switchTo().frame(iframe);
    waitForPageLoaded();
    PageObjectLogging.log("waitForIframeLoaded", "Switching back to the page", true);
    driver.switchTo().defaultContent();
  }

  private void waitForProvidersChain(final String slotName,
                                     final String expectedProviders,
                                     int timeoutSec) {
    new WebDriverWait(driver, timeoutSec).until(
        new ExpectedCondition<Boolean>() {
          @Override
          public Boolean apply(WebDriver webDriver) {
            return expectedProviders.equals(Joiner.on("; ").join(getProvidersChain(slotName)));
          }

          @Override
          public String toString() {
            extractLiftiumTagId(AdsContent.getSlotSelector(slotName));
            extractGptInfo(AdsContent.getSlotSelector(slotName));
            return String.format("Expected: [%s], Actual: [%s]", expectedProviders,
                                 Joiner.on("; ").join(getProvidersChain(slotName)));
          }
        }
    );
  }

  private List<String> getProvidersChain(String slotName) {
    List<String> providersChain = new ArrayList<>();
    String slotSelector = AdsContent.getSlotSelector(slotName);
    for (WebElement providerSlot : driver
        .findElements(By.cssSelector(slotSelector + " > div"))) {
      String providerSlotName = providerSlot.getAttribute("id").split("_")[0];
      String provider = "";
      for (String providerName : PROVIDERS) {
        String
            providerSearch =
            "Liftium".equals(providerName) ? providerName : "/" + providerName + "/";
        if (providerSlotName.contains(providerSearch)) {
          provider = providerName;
          break;
        }
      }
      providersChain.add(provider.isEmpty() ? providerSlotName : provider);
    }
    return providersChain;
  }

  private WebElement getIframe(String slotName, String src) {
    return driver.findElement(By.cssSelector("iframe[id*='" + src + "/" + slotName + "']"));
  }

  public void waitForJavaScriptTruthy(final String script) {
    driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
    try {
      waitFor.until(new ExpectedCondition<Boolean>() {
        public Boolean apply(WebDriver driver) {
          try {
            return (boolean) ((JavascriptExecutor) driver)
                .executeScript("return !!(" + script + ");");
          } catch (WebDriverException e) {
            PageObjectLogging.logError("waitForJavaScriptTruthy", e);
            return false;
          }
        }
      });
    } finally {
      restoreDeaultImplicitWait();
    }
  }

  private void tryTriggerFloatingMedrec(final WebElement slot) {
    try {
      new WebDriverWait(driver, 5).until(new ExpectedCondition<Object>() {
        @Override
        public Object apply(WebDriver webDriver) {
          jsActions.execute(
              " (function(){ window.scroll(0, 5000); setTimeout(function () {window.scroll(0, 5001) }, 100); })(); ");
          return slot.getAttribute("style").contains("visibility: visible;");
        }
      });
    } catch (org.openqa.selenium.TimeoutException e) {
      PageObjectLogging.logError("Floating Medrec", e);
    }
  }

  protected void verifyAdVisibleInSlot(String slotSelector, WebElement slot) {

    if (!checkIfSlotExpanded(slot)) {

      Optional<WebElement> lastGptDiv = getLastGptDiv(slotSelector);
      if (lastGptDiv.isPresent() &&
          checkIfGptSlotHasCreativeContent(lastGptDiv.get(), HOP_AD_TYPE)) {
        PageObjectLogging.log("verifyAdVisibleInSlot", "Slot has " + HOP_AD_TYPE, true);
        return;
      }

      throw new WebDriverException(slot.getAttribute("id") + " is collapsed");
    }

    boolean adVisible = new AdsComparison().isAdVisible(slot, slotSelector, driver);

    extractLiftiumTagId(slotSelector);
    extractGptInfo(slotSelector);

    if (!adVisible) {
      throw new WebDriverException("Ad is not present in " + slotSelector);
    }

    PageObjectLogging.log("ScreenshotsComparison", "Ad is present in " + slotSelector, true);
  }

  private boolean checkIfGptSlotHasCreativeContent(WebElement element, String hopAdType) {
    String slotName = element.getAttribute("id").replace("wikia_gpt", "google_ads_iframe_");
    String iframeSelector = "iframe[id*='" + slotName + "_0']";
    String adTypeScriptXpath = String.format("//script[contains(text(), \"%s\")]", hopAdType);
    WebElement iframe = element.findElement(By.cssSelector(iframeSelector));
    driver.switchTo().frame(iframe);
    List<WebElement> adTypeScripts = driver.findElements(By.xpath(adTypeScriptXpath));
    driver.switchTo().defaultContent();
    return !adTypeScripts.isEmpty();
  }

  private void verifySlotExpanded(WebElement element) {
    if (!checkIfSlotExpanded(element)) {
      throw new WebDriverException(element.getAttribute("id") + " is collapsed");
    }
  }

  private void verifyScriptPresentInSlotScripts(String slotName, WebElement slotElement) {
    String scriptExpectedResult = AdsContent.ADS_PUSHSLOT_SCRIPT.replace(
        "%slot%", slotName
    );
    if (checkScriptPresentInElement(slotElement, scriptExpectedResult)) {
      PageObjectLogging.log(
          "PushSlotsScriptFound",
          "Script " + scriptExpectedResult + " found",
          true
      );
    } else {
      PageObjectLogging.log(
          "PushSlotsScriptNotFound",
          "Script " + scriptExpectedResult + " not found",
          false,
          driver
      );
      throw new NoSuchElementException("Script for pushing ads not found in element");
    }
  }

  private void verifyNoAds() {
    Collection<String> slotsSelectors = AdsContent.SLOTS_SELECTORS.values();
    for (String selector : slotsSelectors) {
      if (isElementOnPage(By.cssSelector(selector))) {
        WebElement element = driver.findElement(By.cssSelector(selector));
        if (
            element.isDisplayed()
            && element.getSize().getHeight() > 1
            && element.getSize().getWidth() > 1
            ) {
          throw new WebDriverException("Ads found on page in selector: " + selector);
        } else {
          PageObjectLogging.log(
              "AdsFoundButNotVisible",
              "Ads found on page with selector: "
              + selector
              + " but is smaller then 1x1 or hidden",
              true
          );
        }
      } else {
        PageObjectLogging.log(
            "AdNotFound",
            "Ad with selector: "
            + selector
            + " not found on page",
            true
        );
      }
    }
  }


  private void extractLiftiumTagId(String slotSelector) {
    String liftiumTagId = null;
    WebElement slot = driver.findElement(By.cssSelector(slotSelector));
    if (isElementInContext(LIFTIUM_IFRAME_SELECTOR, slot)) {
      JavascriptExecutor js = (JavascriptExecutor) driver;
      WebElement currentLiftiumIframe = (WebElement) js.executeScript(
          "return $(arguments[0] + ' iframe[id*=\\'Liftium\\']:visible')[0];",
          slotSelector
      );
      String liftiumAdSrc = currentLiftiumIframe.getAttribute("src");
      Pattern pattern = Pattern.compile("tag_id=\\d*");
      Matcher matcher = pattern.matcher(liftiumAdSrc);
      if (matcher.find()) {
        liftiumTagId = matcher.group().replaceAll("[^\\d]", "");
      }
    }

    if (liftiumTagId != null) {
      PageObjectLogging.log(
          "LiftiumTagId",
          "Present liftium tag id is: "
          + liftiumTagId + "; in slot: " + slotSelector,
          true
      );
    } else {
      PageObjectLogging.log(
          "LiftiumTagId",
          "Liftium not present in slot: " + slotSelector,
          true
      );
    }
  }

  protected void extractGptInfo(String slotSelector) {
    Optional<WebElement> lastGptDiv = getLastGptDiv(slotSelector);

    String log = "GPT ad not found in slot: " + slotSelector;

    if (lastGptDiv.isPresent()) {
      log = "GPT ad found in slot: " + lastGptDiv.get().getAttribute("id");
      for (String attribute : GPT_DATA_ATTRIBUTES) {
        log += "; " + attribute + " = " + lastGptDiv.get().getAttribute(attribute);
      }
    }

    PageObjectLogging.log("extractGptInfo", log, true, driver);
  }

  protected boolean checkScriptPresentInElement(WebElement element, String scriptText) {
    String formattedScriptText = scriptText.replaceAll("\\s", "");

    for (WebElement scriptNode : element.findElements(By.tagName("script"))) {
      String result = scriptNode.getAttribute("innerHTML");
      if (result.replaceAll("\\s", "").contains(formattedScriptText)) {
        return true;
      }
    }
    return false;
  }

  public void clickOnArticleLink(String linkName) {
    hideElementIfPresent(FLITE_MASK_CSS_SELECTOR);

    WebElement link = driver.findElement(
        By.cssSelector("a[title='" + linkName + "']"));
    link.click();

    waitTitleChangesTo(linkName);
  }

  protected void hideElementIfPresent(String cssSelector) {
    if (isElementOnPage(By.cssSelector(cssSelector))) {
      PageObjectLogging.log("Hiding element", cssSelector, true);
      WebElement element = driver.findElement(By.cssSelector(cssSelector));
      JavascriptExecutor js = (JavascriptExecutor) driver;
      js.executeScript("$(arguments[0]).css('display', 'none')", element);
      waitForElementNotVisibleByElement(element);
    }
  }

  public boolean isMiddlePrefooterOnPage() {
    return isElementOnPage(middlePrefooter);
  }

  public void verifyMiddlePrefooterAdPresent() {
    verifyAdVisibleInSlot(MIDDLE_PREFOOTER_CSS_SELECTOR, middlePrefooter);
  }
}
