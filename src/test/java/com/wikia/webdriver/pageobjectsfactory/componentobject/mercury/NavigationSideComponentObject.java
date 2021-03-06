package com.wikia.webdriver.pageobjectsfactory.componentobject.mercury;

import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.pageobjectsfactory.pageobject.mercury.BasePageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class NavigationSideComponentObject extends BasePageObject {

  @FindBy(css = ".ember-text-field")
  private WebElement searchInput;

  @FindBy(css = ".cancel")
  private WebElement cancelSearchCaption;

  @FindBy(css = ".local-wikia-search a")
  private WebElement searchSuggestion;

  @FindBy(css = "ul.local-nav-menu li > *")
  private List<WebElement> navList;

  @FindBy(xpath = "//ul[contains(@class, \"local-nav-menu\")]/li/*[text()[contains(., \"Back\")]]")
  private WebElement backChevron;

  @FindBy(xpath =
      "//ul[contains(@class, \"local-nav-menu\")]/li/*[text()[contains(., \"Random Page\")]]")
  private WebElement randomPageButton;

  @FindBy(css = ".overlay")
  private WebElement overlay;

  @FindBy(css = ".local-wikia-search")
  private WebElement resultField;

  @FindBy(css = ".local-nav-menu")
  private WebElement menuField;

  @FindBy(css = ".nav")
  private WebElement searchButton;

  @FindBy(css = "nav.side-nav")
  private WebElement menuView;

  public NavigationSideComponentObject(WebDriver driver) {
    super(driver);
  }

  public void clickSearchField() {
    waitAndClick(searchInput);
  }

  public void clickSearchButton() {
    waitAndClick(searchButton);
    }

  public void clickBackChevron() {
    waitAndClick(backChevron);
  }

  public void clickCancelButton() {
    waitAndClick(cancelSearchCaption);
  }

  public void clickSuggestion(int index) {
    waitAndClick(searchSuggestion);
  }

  public void clickNavListElement(int index) {
    wait.forElementClickable(navList.get(index));
    navList.get(index).click();
  }

  public void tapOverlay() {
    wait.forElementVisible(overlay);
    tapOnElement(overlay);
  }

  public void clickRandomPageButton() {
    waitAndClick(randomPageButton);
  }

  public boolean isSuggestionListDisplayed() {
    try {
      wait.forElementVisible(searchSuggestion, 5, 1000);
    } catch (TimeoutException | NoSuchElementException e) {
      PageObjectLogging.log("Suggestion list not displayed", e, true);
      return false;
    }
    return true;
  }

  public boolean isNavMenuVisible() throws WebDriverException {
    if (menuView.getAttribute("class") == null) {
      throw new WebDriverException("Expected String but got null");
    }
    return !menuView.getAttribute("class").contains("collapsed");
  }

  public boolean isBackLinkDisplayed() {
    try {
      wait.forElementVisible(backChevron, 5, 1000);
    } catch (TimeoutException | NoSuchElementException e) {
      PageObjectLogging.log("Black link not displayed", e, true);
      return false;
    }
    return true;
  }

  public boolean isRandomPageButtonDisplayed() {
    try {
      wait.forElementVisible(randomPageButton, 5, 1000);
    } catch (TimeoutException | NoSuchElementException e) {
      PageObjectLogging.log("Random page button not displayed", e, true);
      return false;
    }
    return true;
  }

  public boolean isNavListElementEllipsized(int index) {
    wait.forElementVisible(navList.get(index));
    return "ellipsis".equals(navList.get(index).getCssValue("text-overflow"));
  }

  public boolean isMenuFieldVisible() {
    return "visible".equals(menuField.getCssValue("visibility"));
  }

  public boolean isResultFieldVisible() {
    return "visible".equals(resultField.getCssValue("visibility"));
  }

  public boolean isUserLoggedIn(String username) {
    By avatar = By.cssSelector("figure.avatar img[alt='" + username + "']");
    wait.forElementVisible(avatar, 10, 500);
    return driver.findElements(avatar).size() > 0;
  }

  public void typeInSearchField(String content) {
    wait.forElementVisible(searchInput);
    searchInput.sendKeys(content);
  }

  public NavigationSideComponentObject navigateToArticle(String articleName) {
    clickSearchButton();
    clickSearchField();
    typeInSearchField(articleName);
    clickSuggestion(0);
    waitForLoadingOverlayToDisappear();
    return this;
  }
}
