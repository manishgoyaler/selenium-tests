package com.wikia.webdriver.testcases.specialpagestests;

import com.wikia.webdriver.common.contentpatterns.CssEditorContent;
import com.wikia.webdriver.common.core.annotations.RelatedIssue;
import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.properties.Credentials;
import com.wikia.webdriver.common.templates.NewTestTemplate_TwoDrivers;
import com.wikia.webdriver.pageobjectsfactory.pageobject.WikiBasePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.special.SpecialCssPageObject;

import org.testng.annotations.Test;

@Test(enabled = false)
public class CssChromeTests_TwoDrivers extends NewTestTemplate_TwoDrivers {

  Credentials credentials = Configuration.getCredentials();
  @RelatedIssue(issueID = "QAART-461",
      comment = "Under investigation. Best way to test is in debug mode on intelliJ")
  @Test(groups = {"cssChromeTwoDrivers_001", "CssChrome"}, enabled = false)
  public void cssChromeTwoDrivers_001_verifyThatConflictAppearsWithTheLatestRevision() {
    //first user opens the special:CSS
    switchToWindow(driverOne);
    WikiBasePageObject base1 = new WikiBasePageObject(driverOne);
    base1.loginAs(credentials.userNameStaff, credentials.passwordStaff, wikiURL);
    SpecialCssPageObject specialCss1 = base1.openSpecialCss(wikiURL);
    specialCss1.verifyAceEditorPresence();
    specialCss1.sendCssText(CssEditorContent.VALID_CSS);
    //second user opens the special:CSS

    switchToWindow(driverTwo);
    WikiBasePageObject base2 = new WikiBasePageObject(driverTwo);
    base2.loginAs(credentials.userNameStaff2, credentials.passwordStaff2, wikiURL);
    SpecialCssPageObject specialCss2 = base2.openSpecialCss(wikiURL);
    specialCss2.verifyAceEditorPresence();
    specialCss2.sendCssText(CssEditorContent.VALID_CSS2);

    //first publishes his changes
    switchToWindow(driverOne);
    specialCss1.clickPublishButton();
    specialCss1.verifyAceEditorPresence(); //make sure page reloaded

    //second user publishes his changes
    switchToWindow(driverTwo);
    specialCss2.clickPublishButton();
    specialCss2.verifyConflictArea();
    specialCss2.verifyLatestRevision();
  }
}
