package com.wikia.webdriver.pageobjectsfactory.componentobject.slider;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wikia.webdriver.common.logging.LOG;
import com.wikia.webdriver.pageobjectsfactory.componentobject.addphoto.AddPhotoComponentObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.BasePageObject;

public class SliderBuilderComponentObject extends BasePageObject {

  @FindBy(css = "#WikiaPhotoGallerySliderType_bottom")
  private WebElement hPosition;
  @FindBy(css = "#WikiaPhotoGallerySliderType_right")
  private WebElement vPosition;
  @FindBy(css = "#WikiaPhotoGallerySliderAddImage")
  private WebElement addPhotoButton;
  @FindBy(css = "#WikiaPhotoGalleryEditorSave")
  private WebElement finishButton;

  public SliderBuilderComponentObject(WebDriver driver) {
    super(driver);
    // TODO Auto-generated constructor stub
  }

  public void selectMenuPosition(MenuPositions pos) {
    wait.forElementVisible(hPosition);
    wait.forElementVisible(vPosition);
    switch (pos) {
      case HORIZONTAL:
        hPosition.click();
        break;
      case VERTICAL:
        vPosition.click();
        break;
      default:
        throw new NoSuchElementException("Non-existing position selected");
    }
    LOG.success("selectMenuPosition", pos.toString() + " position selected",true);
  }

  public AddPhotoComponentObject clickAddPhoto() {
    wait.forElementVisible(addPhotoButton);
    addPhotoButton.click();
    LOG.success("addPhoto", "add photo button clicked");
    return new AddPhotoComponentObject(driver);
  }

  public void clickFinish() {
    wait.forElementVisible(finishButton);
    finishButton.click();
    LOG.success("clickFinish", "finish button clicked");
  }

  public enum MenuPositions {
    HORIZONTAL, VERTICAL
  }

}
