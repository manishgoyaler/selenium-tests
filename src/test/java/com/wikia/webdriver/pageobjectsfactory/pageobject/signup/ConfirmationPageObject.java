package com.wikia.webdriver.pageobjectsfactory.pageobject.signup;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.wikia.webdriver.common.core.MailFunctions;
import com.wikia.webdriver.common.logging.LOG;
import com.wikia.webdriver.pageobjectsfactory.pageobject.BasePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.createnewwiki.CreateNewWikiPageObjectStep1;

public class ConfirmationPageObject extends BasePageObject {

  @FindBy(css = "div.UserConfirm input[name='username']")
  private WebElement userNameField;
  @FindBy(css = "div.UserConfirm input[name='password']")
  private WebElement passwordField;
  @FindBy(css = "div.UserConfirm input[type='submit']")
  private WebElement confirmationButton;

  /**
   * @author Karol Kujawiak
   */
  public ConfirmationPageObject(WebDriver driver) {
    super(driver);
    PageFactory.initElements(driver, this);
  }

  /**
   * @author Karol Kujawiak
   */
  public void typeInUserName(String userName) {
    userNameField.sendKeys(userName);
    LOG.success("typeInUserName ", "user name field populated", true);
  }

  /**
   * @author Karol Kujawiak
   */
  public void typeInPassword(String password) {
    passwordField.sendKeys(password);
    LOG.success("typeInUserPassword ", "password field populated", true);
  }

  /**
   * @author Karol Kujawiak
   */
  public UserProfilePageObject clickSubmitButton(String email, String password) {
    MailFunctions.deleteAllEmails(email, password);
    scrollAndClick(confirmationButton);
    LOG.success("submit button clicked ", "submit button clicked", true);
    return new UserProfilePageObject(driver);
  }

  public CreateNewWikiPageObjectStep1 CNWSubmitButton(String email, String password) {
    MailFunctions.deleteAllEmails(email, password);
    scrollAndClick(confirmationButton);
    LOG.success("submit button clicked ", "submit button clicked", true);
    return new CreateNewWikiPageObjectStep1(driver);
  }
}
