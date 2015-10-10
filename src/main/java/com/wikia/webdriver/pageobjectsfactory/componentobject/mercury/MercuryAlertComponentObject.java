package com.wikia.webdriver.pageobjectsfactory.componentobject.mercury;

import com.wikia.webdriver.pageobjectsfactory.pageobject.mercury.BasePageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @ownership Content X-Wing Wikia
 */
public class MercuryAlertComponentObject extends BasePageObject {

  @FindBy(css = ".alert-notifications .alert-box")
  protected WebElement alertBox;

  private String alertMessage;

  public static enum AlertMessage {
    NOT_EXISTING_REDIRECT(
        "The link you followed is a redirect, but the page it directs to does not exist."),
    NOT_EXISTING_CATEGORY("Category not found"),
    NOT_EXISTING_SECTION("Section not found");

    private String message;

    AlertMessage(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }

  public MercuryAlertComponentObject(WebDriver driver, AlertMessage message) {
    super(driver);
    alertMessage = message.getMessage();
  }

  public boolean isAlertMessageVisible() {
    return wait.forTextInElement(alertBox, alertMessage);
  }
}
