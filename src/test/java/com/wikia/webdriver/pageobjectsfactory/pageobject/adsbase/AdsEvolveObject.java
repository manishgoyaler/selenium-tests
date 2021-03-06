package com.wikia.webdriver.pageobjectsfactory.pageobject.adsbase;

import com.wikia.webdriver.common.contentpatterns.AdsContent;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.logging.PageObjectLogging;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

public class AdsEvolveObject extends AdsBaseObject {

  private static final String EVOLVE_SELECTOR = " script[src*=\"4403ad\"]";

  public AdsEvolveObject(WebDriver driver, String page) {
    // INVISIBLE_SKIN works only with big resolution.
    super(driver, page, new Dimension(1366, 768));
  }

  public void verifyEvolveInSlot(String slotName) {
    String slotSelector = AdsContent.getSlotSelector(slotName);
    wait.forElementPresent(By.cssSelector(slotSelector + EVOLVE_SELECTOR));
    PageObjectLogging.log("Evolve", slotSelector + " slot has Evolve.", true, driver);
  }

  public void verifyNoEvolveInSlot(String slotName) {
    String slotSelector = AdsContent.getSlotSelector(slotName);
    wait.forElementNotPresent(By.cssSelector(slotSelector + EVOLVE_SELECTOR));
    PageObjectLogging.log("Evolve", slotSelector + " slot doesn't have Evolve.", true, driver);
  }

  public void verifyEvolveCall() {
    verifyEvolveInSlot(AdsContent.TOP_LB);
    verifyEvolveInSlot(AdsContent.MEDREC);
    verifyEvolveInSlot(AdsContent.LEFT_SKYSCRAPPER_2);
    verifyEvolveInSlot(AdsContent.INVISIBLE_SKIN);
    verifyNoEvolveInSlot(AdsContent.FLOATING_MEDREC);
    verifyNoEvolveInSlot(AdsContent.PREFOOTER_LEFT);
    verifyNoEvolveInSlot(AdsContent.PREFOOTER_RIGHT);
  }

  public void verifyEvolveHoppedInSlot(String slotName, String nextProviderSrc) {
    String nextProviderSlotCss = String.format("div[id*=\'%s/%s\']", nextProviderSrc, slotName);
    Assertion.assertTrue(isElementOnPage(By.cssSelector(nextProviderSlotCss)));
    PageObjectLogging.log("Evolve", "Evolve hopped to " + nextProviderSrc, true);
  }
}
