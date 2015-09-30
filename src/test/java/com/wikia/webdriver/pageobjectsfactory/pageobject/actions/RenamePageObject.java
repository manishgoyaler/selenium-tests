package com.wikia.webdriver.pageobjectsfactory.pageobject.actions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wikia.webdriver.common.logging.LOG;
import com.wikia.webdriver.pageobjectsfactory.pageobject.article.ArticlePageObject;

/**
 * @author: Bogna 'bognix' Knychała
 */
public class RenamePageObject extends ArticlePageObject {

  @FindBy(css = "#wpNewTitleMain")
  private WebElement newNameInput;
  @FindBy(css = ".mw-submit [name='wpMove']")
  private WebElement submitRename;
  @FindBy(css = "input#wpLeaveRedirect")
  private WebElement leaveRedirectCheckbox;

  public RenamePageObject(WebDriver driver) {
    super(driver);
  }

  public ArticlePageObject rename(String newName) {
    return rename(newName, false);
  }

  public ArticlePageObject rename(String newName, boolean leaveRedirect) {
    newNameInput.clear();
    newNameInput.sendKeys(newName);
    if (leaveRedirect) {
      leaveRedirectCheckbox.click();
    }
    scrollAndClick(submitRename);
    LOG.success("ArticleRenamed", "Article renamed");
    return new ArticlePageObject(driver);
  }
}
