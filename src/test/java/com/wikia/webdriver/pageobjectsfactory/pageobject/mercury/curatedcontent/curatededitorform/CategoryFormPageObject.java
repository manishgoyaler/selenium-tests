package com.wikia.webdriver.pageobjectsfactory.pageobject.mercury.curatedcontent.curatededitorform;

import com.wikia.webdriver.pageobjectsfactory.pageobject.mercury.curatedcontent.CuratedEditorFormPageObject;

import org.openqa.selenium.WebDriver;

/**
 * This class represents category editor Form
 * That object differs from Section Form:
 * Done button may redirect you to main editor home or section items form
 * It has two text fields to fill instead of one
 * That object differs from Item Form:
 * Done button may redirect you to main editor home or section items form
 * It allows only category namespace
 */
public class CategoryFormPageObject extends CuratedEditorFormPageObject {

  public CategoryFormPageObject(WebDriver driver) {
    super(driver);
  }

  public SectionItemListPageObject clickDone() {
    waitAndClick(doneButton);
    return new SectionItemListPageObject(driver);
  }
}
