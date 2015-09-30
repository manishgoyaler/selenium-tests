package com.wikia.webdriver.pageobjectsfactory.pageobject.actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wikia.webdriver.common.logging.LOG;
import com.wikia.webdriver.pageobjectsfactory.pageobject.WikiBasePageObject;

/**
 * @author: Bogna 'bognix' Knychała
 */
public class DeletePageObject extends WikiBasePageObject {

  @FindBy(css = ".mw-submit input")
  private WebElement submitButton;

  public DeletePageObject(WebDriver driver) {
    super(driver);
  }

  public WikiBasePageObject submitDeletion() {
    wait.forElementClickable(submitButton);
    scrollAndClick(submitButton);
    LOG.success("submitDeletion", "page deleted");
    return new WikiBasePageObject(driver);
  }
}
