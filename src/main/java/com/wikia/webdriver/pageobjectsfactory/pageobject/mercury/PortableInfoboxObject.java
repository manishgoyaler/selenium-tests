package com.wikia.webdriver.pageobjectsfactory.pageobject.mercury;

import com.wikia.webdriver.common.contentpatterns.MercuryMessages;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.pageobjectsfactory.componentobject.mercury.LightboxComponentObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * @ownshership: Content West-Wing
 */
public class PortableInfoboxObject extends BasePageObject {

  @FindBy(css = "body")
  private WebElement bodyElement;
  @FindBy(css = ".portable-infobox")
  private WebElement infoboxWrapper;
  @FindBy(css = ".pi-hero .article-image")
  private WebElement mainImage;
  @FindBy(css = ".portable-infobox .pi-hero-title")
  private WebElement title;
  @FindBy(css = ".portable-infobox .pi-title")
  private WebElement titleSmallImage;
  @FindBy(css = ".portable-infobox .pi-expand-button")
  private WebElement expandButton;
  @FindBy(css = ".article-content .collapsed")
  private WebElement infoboxIsCollapsed;
  @FindBy(css = ".tabber .article-image")
  private WebElement imageInTabber;
  @FindBy(css = ".tabber figcaption")
  private WebElement captionInTabber;
  @FindBy(css = ".portable-infobox .article-video")
  private WebElement video;
  @FindBy(css = ".portable-infobox .article-video figcaption")
  private WebElement videoCaption;
  @FindBy(css = ".pi-title img")
  private WebElement imageInTitle;
  @FindBy(css = ".portable-infobox .external")
  private List<WebElement> externalLinks;
  @FindBy(css = ".pi-item .pi-data-label")
  private List<WebElement> dataLabels;
  @FindBy(css = ".pi-item .pi-data-value")
  private List<WebElement> dataValues;
  @FindBy(css = ".portable-infobox .reference")
  private List<WebElement> references;
  @FindBy(css = ".portable-infobox ul li")
  private List<WebElement> unorderedLists;
  @FindBy(css = ".portable-infobox ol li")
  private List<WebElement> orderedLists;
  @FindBy(css = ".pi-header")
  private List<WebElement> headers;
  @FindBy(css = ".portable-infobox .pi-image img")
  private List<WebElement> images;

  public PortableInfoboxObject(WebDriver driver) {
    super(driver);
  }

  public String getExternalLinkName(int index) {
    Assertion.assertFalse(externalLinks.isEmpty());
    wait.forElementVisible(externalLinks.get(index));

    return externalLinks.get(index).getText();
  }

  public String getUrlFromExternalLinkaAfterPageIsLoaded() {
    wait.forElementVisible(bodyElement);

    return driver.getCurrentUrl();
  }

  // TODO: This is not real tap, replace with PerformTouchActions class methods
  public PortableInfoboxObject tapInfoboxContent() {
    Assertion.assertFalse(dataLabels.isEmpty());
    dataLabels.get(0).click();

    return this;
  }

  public PortableInfoboxObject clickExpandButton() {
    wait.forElementVisible(expandButton);
    expandButton.click();

    return this;
  }

  public PortableInfoboxObject clickExternalLink(int index) {
    Assertion.assertFalse(externalLinks.isEmpty());
    wait.forElementVisible(externalLinks.get(index));

    externalLinks.get(index).click();
    return this;
  }

  public PortableInfoboxObject closeLightbox() {
    new LightboxComponentObject(driver).clickCloseButton();

    return this;
  }

  public PortableInfoboxObject clickMainImage() {
    wait.forElementVisible(mainImage);
    mainImage.click();

    return this;
  }

  public PortableInfoboxObject clickVideo() {
    wait.forElementVisible(video);
    video.click();

    return this;
  }

  public PortableInfoboxObject isMainImageVisible() {
    wait.forElementVisible(mainImage);
    Assertion.assertEquals(isElementOnPage(mainImage), true);
    PageObjectLogging.log("Main image", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject isLightboxOpened() {
    Assertion.assertTrue(new LightboxComponentObject(driver).isLightboxOpened());

    return this;
  }

  public PortableInfoboxObject isTitleOverImageVisible() {
    wait.forElementVisible(title);
    Assertion.assertEquals(isElementOnPage(title), true);
    PageObjectLogging.log("Title over image", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject isTitleAboveImageVisible() {
    wait.forElementVisible(titleSmallImage);
    Assertion.assertEquals(isElementOnPage(titleSmallImage), true);
    PageObjectLogging.log("Title above image", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject isImageInTitleNotVisible() {
    Assertion.assertEquals(isElementVisible(imageInTitle), false);
    PageObjectLogging.log("Main image title", MercuryMessages.INVISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject isInfoboxCollapsed() {
    Assertion.assertEquals(isElementOnPage(infoboxIsCollapsed), true);
    PageObjectLogging.log("Infobox", MercuryMessages.COLLAPSED_MSG, true);

    return this;
  }

  public PortableInfoboxObject isInfoboxExpanded() {
    Assertion.assertEquals(infoboxWrapper.getAttribute("style"), "height: auto;");
    PageObjectLogging.log("Infobox", MercuryMessages.EXPANDED_MSG, true);

    return this;
  }

  public PortableInfoboxObject isImageInTabberVisible() {
    Assertion.assertEquals(isElementVisible(imageInTabber), true);
    PageObjectLogging.log("Image in tabber", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject isImageCaptionInTabberVisible() {
    Assertion.assertEquals(isElementVisible(captionInTabber), true);
    PageObjectLogging.log("Image caption in tabber", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject isVideoVisible() {
    Assertion.assertEquals(isElementVisible(video), true);
    PageObjectLogging.log("Video", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject isVideoCaptionVisible() {
    Assertion.assertEquals(isElementVisible(videoCaption), true);
    PageObjectLogging.log("Video caption", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject isHeroImageCentered() {
    Assertion.assertEquals(images.get(0).getCssValue("text-align"), "center");
    PageObjectLogging.log("Hero image", "is centered", true);

    return this;
  }

  public PortableInfoboxObject isExternalLinkLabelInURL(String name, String url) {
    Assertion.assertStringContains(url, name);
    PageObjectLogging.log("External links", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject areUnorderedListsVisible() {
    Assertion.assertFalse(unorderedLists.isEmpty());
    PageObjectLogging.log("Unordered list", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject areOrderedListsVisible() {
    Assertion.assertFalse(orderedLists.isEmpty());
    PageObjectLogging.log("Ordered list", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject areHeadersVisible() {
    Assertion.assertFalse(headers.isEmpty());
    PageObjectLogging.log("Headers", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject areLinksVisible() {
    Assertion.assertFalse(externalLinks.isEmpty());
    PageObjectLogging.log("Links", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject areReferencesVisible() {
    Assertion.assertFalse(references.isEmpty());
    PageObjectLogging.log("References", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject areDataLabelsVisible() {
    Assertion.assertFalse(dataLabels.isEmpty());
    PageObjectLogging.log("Data labels", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject areDataValuesVisible() {
    Assertion.assertFalse(dataValues.isEmpty());
    PageObjectLogging.log("Data values", MercuryMessages.VISIBLE_MSG, true);

    return this;
  }

  public PortableInfoboxObject areUnorderedListAndDataValuesMarginEqual() {
    Assertion.assertEquals(
        unorderedLists.get(0).getCssValue("margin"),
        dataValues.get(0).getCssValue("margin")
    );
    PageObjectLogging.log("Unordered list labes and value", "have the same margin", true);

    return this;
  }

  public PortableInfoboxObject areOrderedListAndDataValuesMarginEqual() {
    Assertion.assertEquals(
        orderedLists.get(0).getCssValue("margin"),
        dataValues.get(0).getCssValue("margin")
    );
    PageObjectLogging.log("Ordered list labes and value", "have the same margin", true);

    return this;
  }
}
