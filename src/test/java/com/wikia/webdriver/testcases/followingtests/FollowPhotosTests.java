/**
 *
 */
package com.wikia.webdriver.testcases.followingtests;

import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.annotations.User;
import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.properties.Credentials;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.pageobject.WikiBasePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.SpecialFollowPageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.SpecialNewFilesPageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.filepage.FilePagePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.watch.WatchPageObject;

import org.testng.annotations.Test;

import javax.jws.soap.SOAPBinding;

public class FollowPhotosTests extends NewTestTemplate {

  String imageName;

  @Test(groups = "FollowPhoto")
  @Execute(asUser = User.USER)
  public void FollowPhoto_001_setup() {
    WikiBasePageObject base = new WikiBasePageObject(driver);
    SpecialNewFilesPageObject special = base.openSpecialNewFiles(wikiURL);
    imageName = special.getRandomImageName();
    WatchPageObject watch = special.unfollowImage(wikiURL, imageName);
    watch.confirmWatchUnwatch();
    special.verifyPageUnfollowed();
  }

  @Test(groups = "FollowPhoto", dependsOnMethods = {"FollowPhoto_001_setup"})
  @Execute(asUser = User.USER)
  public void FollowPhoto_002_follow() {
    WikiBasePageObject base = new WikiBasePageObject(driver);
    FilePagePageObject file = base.openFilePage(wikiURL, imageName);
    file.follow();
  }

  @Test(groups = {"FollowPhoto", "Follow"}, dependsOnMethods = {"FollowPhoto_002_follow"})
  @Execute(asUser = User.USER)
  public void FollowPhoto_003_verify() {
    WikiBasePageObject base = new WikiBasePageObject(driver);
    SpecialFollowPageObject follow = new SpecialFollowPageObject(driver, wikiURL);
    follow.verifyFollowedImageVideo(imageName);
  }
}
