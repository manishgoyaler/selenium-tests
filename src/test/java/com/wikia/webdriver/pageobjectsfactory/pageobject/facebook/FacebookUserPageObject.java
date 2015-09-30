package com.wikia.webdriver.pageobjectsfactory.pageobject.facebook;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wikia.webdriver.common.contentpatterns.URLsContent;
import com.wikia.webdriver.common.logging.LOG;
import com.wikia.webdriver.pageobjectsfactory.pageobject.WikiBasePageObject;

/**
 * @author Michal 'justnpT' Nowierski
 */
public class FacebookUserPageObject extends WikiBasePageObject {

  @FindBy(css = "a[href$='?ref=logo']")
  private WebElement pageLogo;

  public FacebookUserPageObject(WebDriver driver) {
    super(driver);
  }

  public void verifyPageLogo() {
    wait.forElementVisible(pageLogo);
    LOG.success("verifyPageLogo", "Page logo is present");
  }

  public FacebookSettingsPageObject fbOpenSettings() {
    getUrl(URLsContent.FACEBOOK_SETTINGSPAGE);
    return new FacebookSettingsPageObject(driver);
  }
}
