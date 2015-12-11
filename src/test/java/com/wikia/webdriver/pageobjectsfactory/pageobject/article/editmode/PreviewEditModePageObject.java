package com.wikia.webdriver.pageobjectsfactory.pageobject.article.editmode;

import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.logging.PageObjectLogging;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Karol 'kkarolk' Kujawiak
 */
public class PreviewEditModePageObject extends EditMode {

  @FindBy(css = ".modalWrapper.preview")
  private WebElement previewModal;
  @FindBy(css = ".preview .video-thumbnail")
  protected WebElement videoArticle;
  @FindBy(xpath = "//*[contains(@id,'w-content-text')]/object/@id")
  protected WebElement video;

  By closeButton = By.cssSelector(".close.wikia-chiclet-button > img");
  By videoWidthSelector = By.cssSelector(".image > img");
  By videoCaptionSelector = By.cssSelector("figcaption");
  By contentWrapper = By.cssSelector("#mw-content-text");
  By publishButton = By.cssSelector("#publish");
  By tableOfContents = By.cssSelector("#toc");
  By tableOfContentsOrderedList = By.cssSelector("#toc ol");

  String videoPostionSelector = "figure.t%position%";

  public PreviewEditModePageObject(WebDriver driver) {
    super(driver);
    wait.forElementVisible(previewModal);
  }

  public void verifyVideoAlignment(PositionsVideo positions) {
    String position;

    switch (positions) {
      case LEFT:
        position = "left";
        break;
      case RIGHT:
        position = "right";
        break;
      case CENTER:
        position = "none";
        break;
      default:
        position = "position not provided";
        break;
    }
    previewModal.findElement(
        By.cssSelector(
            videoPostionSelector.replace("%position%", position)
        )
    );
    PageObjectLogging.log(
        "verifyVideoAlignment",
        "video alignment is as exepected " + positions.toString(),
        true
    );
  }

  public void verifyVideoWidth(int desiredWidth) {
    int width = Integer.parseInt(previewModal.findElement(
        videoWidthSelector
    ).getAttribute("width"));
    Assertion
        .assertNumber(width, desiredWidth, "width should be " + desiredWidth + " but is " + width);
  }

  public void verifyVideoCaption(String desiredCaption) {
    String caption = previewModal.findElement(videoCaptionSelector).getText();
    Assertion.assertStringContains(caption, desiredCaption);
  }

  public void closePreviewModal() {
    previewModal.findElement(closeButton).click();
    wait.forElementNotPresent(closeButton);
    PageObjectLogging.log("closePreviewModal", "preview modal closed", true);
  }

  public void verifyTextContent(String desiredText) {
    Assertion.assertEquals(previewModal.findElement(contentWrapper).getText(), desiredText);
  }

  public void publish() {
    previewModal.findElement(publishButton).click();
  }

  public void verifyTOCpresentOnPreview() {
    wait.forElementVisible(previewModal.findElement(tableOfContents));
    PageObjectLogging.log("verifyTOCpresentOnPreview", "TOC is present on preview", true);
  }

  public void verifyTOCexpandedOnPreview() {
    wait.forElementVisible(previewModal.findElement(tableOfContentsOrderedList));
    PageObjectLogging.log("verifyTOCexpandedOnPreview", "TOC is expanded on preview", true);
  }

  public void verifyTOCcollapsedOnPreview() {
    waitForElementNotVisibleByElement(previewModal.findElement(tableOfContentsOrderedList));
    PageObjectLogging.log("verifyTOCcollapsedOnPreview", "TOC is collapsed on preview", true);
  }

  public void verifyVideoOnPreview(String videoID) {
//    wait.forElementVisible(video);
    video.getText().equals(videoID);
//    waitForValueToBePresentInElementsAttributeByElement(video, "id", videoID);
  }
}
