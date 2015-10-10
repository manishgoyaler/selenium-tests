package com.wikia.webdriver.pageobjectsfactory.pageobject.widget;

import com.wikia.webdriver.common.contentpatterns.MercuryMessages;
import com.wikia.webdriver.common.core.api.ArticleContent;
import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.pageobjectsfactory.pageobject.mercury.BasePageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * @ownership Content X-Wing Wikia
 */
public abstract class WidgetPageObject extends BasePageObject {

  @FindBy(css = "strong.error")
  protected WebElement error;

  protected WidgetPageObject(WebDriver driver) {
    super(driver);
  }

  protected abstract String getTagName();

  public abstract String getTag();

  /**
   * Get all tags defined in the widget page object
   */
  protected abstract String[] getTags();

  protected abstract String getIncorrectTag();

  protected abstract String getErrorMessage();

  protected abstract List<WebElement> getWidgetWrapperList();

  protected abstract List<WebElement> getWidgetIFrameList();

  protected abstract WebElement getWidgetBody();

  public WidgetPageObject create(String articleName) {
    ArticleContent articleContent = new ArticleContent();
    articleContent.push(getTag(), articleName);
    return this;
  }

  /**
   * Create all tags as defined in widget page object tags field on the tested article
   */
  public WidgetPageObject createMultiple(String articleName) {
    String text = "";
    ArticleContent articleContent = new ArticleContent();

    articleContent.clear(articleName);

    for (String tag : getTags()) {
      text += tag + "\n";
    }

    articleContent.push(text, articleName);

    return this;
  }

  /**
   * Create incorrect widget tag on the tested article
   */
  public WidgetPageObject createIncorrect(String articleName) {
    ArticleContent articleContent = new ArticleContent();
    articleContent.clear(articleName);
    articleContent.push(getIncorrectTag(), articleName);
    return this;
  }

  /**
   * Make sure that there is a widget of its type loaded on the page.
   */
  public boolean isLoaded() {
    boolean result = isWidgetVisible(0);
    logVisibility(result);
    return result;
  }

  public boolean areAllValidSwappedForIFrames() {
    boolean result = getWidgetWrapperList().size() == getWidgetIFrameList().size();
    PageObjectLogging.log(getTagName(), MercuryMessages.ALL_VALID_WIDGETS_ARE_SWAPPED_MSG, result);
    return result;
  }

  /**
   * Make sure that all tags defined in the widget tags field are loaded.
   */
  public boolean areLoaded() {
    boolean result = true;
    String[] tags = getTags();
    for (int i = 0; i < tags.length; i++) {
      if (!isWidgetVisible(i)) {
        result = false;
        PageObjectLogging.log(getTagName() + " #" + i, MercuryMessages.INVISIBLE_MSG, result);
        return result;
      }
      PageObjectLogging.log(getTagName() + " #" + i, MercuryMessages.VISIBLE_MSG, result);
    }
    PageObjectLogging.log("all " + getTagName() + " widgets", MercuryMessages.VISIBLE_MSG, result);
    return result;
  }

  public boolean isErrorPresent() {
    boolean result = isElementVisible(error) && error.getText().equals(getErrorMessage());
    logVisibility(result);
    return result;
  }

  protected void logVisibility(boolean result) {
    PageObjectLogging
        .log(getTagName(), result ? MercuryMessages.VISIBLE_MSG : MercuryMessages.INVISIBLE_MSG,
             result);
  }

  /**
   * Verify a widget presence, based of its index position among other widgets of its type. The
   * method assumes there may be more than one widgets of certain type on the article.
   */
  protected boolean isWidgetVisible(int widgetIndex) {
    boolean result;
    List<WebElement> widgetIFrameList = getWidgetIFrameList();
    if (widgetIFrameList.isEmpty()) {
      result = false;
    } else {
      WebElement widgetIFrame = widgetIFrameList.get(widgetIndex);
      if (!isElementVisible(widgetIFrame)) {
        result = false;
      } else {
        driver.switchTo().frame(widgetIFrame);
        result = isElementVisible(getWidgetBody());
        driver.switchTo().parentFrame();
      }
    }
    logVisibility(result);
    return result;
  }
}
