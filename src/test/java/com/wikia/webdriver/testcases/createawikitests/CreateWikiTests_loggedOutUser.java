package com.wikia.webdriver.testcases.createawikitests;

import com.wikia.webdriver.common.contentpatterns.CreateWikiMessages;
import com.wikia.webdriver.common.core.annotations.RelatedIssue;
import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.properties.Credentials;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.pageobject.WikiBasePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.article.ArticlePageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.createnewwiki.CreateNewWikiLogInSignUpPageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.createnewwiki.CreateNewWikiPageObjectStep1;
import com.wikia.webdriver.pageobjectsfactory.pageobject.createnewwiki.CreateNewWikiPageObjectStep2;
import com.wikia.webdriver.pageobjectsfactory.pageobject.createnewwiki.CreateNewWikiPageObjectStep3;

import org.testng.annotations.Test;

@Test(groups = {"CNW_Anon"})
public class CreateWikiTests_loggedOutUser extends NewTestTemplate {

  Credentials credentials = Configuration.getCredentials();

  @Test(groups = {"CNW", "CreateNewWikiLoggedOut_001"})
  @RelatedIssue(issueID = "CE-3160", comment = "Test manually: Test may fail until the ticket is fixed as" +
          " notification is obscuring the feature being tested")
 public void CreateNewWiki_001_loggedOutUser() {
    WikiBasePageObject base = new WikiBasePageObject(driver);
    CreateNewWikiPageObjectStep1 cnw1 = base.openSpecialCreateNewWikiPage(wikiCorporateURL);
    String wikiName = cnw1.getWikiName();
    cnw1.typeInWikiName(wikiName);
    cnw1.verifySuccessIcon();
    CreateNewWikiLogInSignUpPageObject cnwLogin = cnw1.submitToLogInSignUp();
    cnwLogin.typeInUserName(credentials.userName13);
    cnwLogin.typeInPassword(credentials.password13);
    CreateNewWikiPageObjectStep2 cnw2 = cnwLogin.submitLogin();
    cnw2.selectCategory(CreateWikiMessages.WIKI_CATEGORY);
    CreateNewWikiPageObjectStep3 cnw3 = cnw2.submit();
    cnw3.selectThemeByName(CreateWikiMessages.WIKI_THEME);
    ArticlePageObject article = cnw3.submit();
    article.verifyWikiTitleOnCongratualtionsLightBox(wikiName);
    article.closeNewWikiCongratulationsLightBox();
    article.verifyWikiTitleHeader(wikiName);
    article.verifyUserLoggedIn(credentials.userName13);
  }

  @Test(groups = {"CNW", "CreateNewWikiLoggedOut_002"})
 public void CreateNewWiki_002_wrongPassword() {
    WikiBasePageObject base = new WikiBasePageObject(driver);
    CreateNewWikiPageObjectStep1 cnw1 = base.openSpecialCreateNewWikiPage(wikiCorporateURL);
    cnw1.typeInWikiName(cnw1.getWikiName());
    cnw1.verifySuccessIcon();
    CreateNewWikiLogInSignUpPageObject cnwLogin = cnw1.submitToLogInSignUp();
    cnwLogin.typeInUserName(credentials.userName);
    cnwLogin.typeInPassword(credentials.password + "1");
    cnwLogin.submitLogin();
    cnwLogin.verifyInvalidPasswordValidation();
  }

  @Test(groups = {"CNW", "CreateNewWikiLoggedOut_003"})
  public void CreateNewWiki_003_blankPassword() {
    WikiBasePageObject base = new WikiBasePageObject(driver);
    CreateNewWikiPageObjectStep1 cnw1 = base.openSpecialCreateNewWikiPage(wikiCorporateURL);
    cnw1.typeInWikiName(cnw1.getWikiName());
    cnw1.verifySuccessIcon();
    CreateNewWikiLogInSignUpPageObject cnwLogin = cnw1.submitToLogInSignUp();
    cnwLogin.typeInUserName(credentials.userName);
    cnwLogin.submitLogin();
    cnwLogin.verifyBlankPasswordValidation();
  }

  @Test(groups = {"CNW", "CreateNewWikiLoggedOut_004"})
  public void CreateNewWiki_004_blankUserName() {
    WikiBasePageObject base = new WikiBasePageObject(driver);
    CreateNewWikiPageObjectStep1 cnw1 = base.openSpecialCreateNewWikiPage(wikiCorporateURL);
    cnw1.typeInWikiName(cnw1.getWikiName());
    cnw1.verifySuccessIcon();
    CreateNewWikiLogInSignUpPageObject cnwLogin = cnw1.submitToLogInSignUp();
    cnwLogin.submitLogin();
    cnwLogin.verifyEmptyUserNameValidation();
  }

  @Test(groups = {"CNW", "CreateNewWikiLoggedOut_005"})
 public void CreateNewWiki_005_invalidUserName() {
    WikiBasePageObject base = new WikiBasePageObject(driver);
    CreateNewWikiPageObjectStep1 cnw1 = base.openSpecialCreateNewWikiPage(wikiCorporateURL);
    cnw1.typeInWikiName(cnw1.getWikiName());
    cnw1.verifySuccessIcon();
    CreateNewWikiLogInSignUpPageObject cnwLogin = cnw1.submitToLogInSignUp();
    cnwLogin.typeInUserName(credentials.userName + "1");
    cnwLogin.submitLogin();
    cnwLogin.verifyInvalidUserNameValidation();
  }
}
