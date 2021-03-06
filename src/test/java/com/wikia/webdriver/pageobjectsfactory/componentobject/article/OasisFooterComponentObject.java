package com.wikia.webdriver.pageobjectsfactory.componentobject.article;

import com.wikia.webdriver.pageobjectsfactory.pageobject.mercury.ArticlePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.mercury.BasePageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class OasisFooterComponentObject extends BasePageObject{

  @FindBy(css = ".global-footer a[href='#']")
  private WebElement mobileSiteLink;

  public OasisFooterComponentObject(WebDriver driver) {
    super(driver);
  }

  public ArticlePageObject clickMobileSiteLink() {
    waitAndClick(mobileSiteLink);
    return new ArticlePageObject(driver);
  }
}

