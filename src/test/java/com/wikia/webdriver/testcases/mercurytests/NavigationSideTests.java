package com.wikia.webdriver.testcases.mercurytests;

import com.wikia.webdriver.common.contentpatterns.MercurySubpages;
import com.wikia.webdriver.common.contentpatterns.MercuryWikis;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.annotations.InBrowser;
import com.wikia.webdriver.common.core.annotations.RelatedIssue;
import com.wikia.webdriver.common.core.helpers.Browser;
import com.wikia.webdriver.common.core.helpers.Emulator;
import com.wikia.webdriver.common.logging.PageObjectLogging;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.componentobject.mercury.NavigationSideComponentObject;

import org.testng.annotations.Test;

@Execute(onWikia = MercuryWikis.MERCURY_AUTOMATION_TESTING)
@InBrowser(
    browser = Browser.CHROME,
    emulator = Emulator.GOOGLE_NEXUS_5
)
public class NavigationSideTests extends NewTestTemplate {

  private final static String SEARCH_PASS = "Gallery";
  private final static String SEARCH_FAIL = "te";

  @Test(groups = "MercuryNavigationSideTest_001")
  public void MercuryNavigationSideTest_001_Open_Navigate_Close() {
    NavigationSideComponentObject nav = new NavigationSideComponentObject(driver);
    nav.openArticleOnWikiByNameWithCbAndNoAds(wikiURL, MercurySubpages.MAIN_PAGE);

    Assertion.assertFalse(
        nav.isNavMenuVisible(),
        "Navigation menu is visible"
    );

    PageObjectLogging.log(
        "Navigation menu",
        "is hidden",
        true
    );

    nav.clickSearchButton();

    Assertion.assertTrue(
        nav.isNavMenuVisible(),
        "Navigation menu isn't visible"
    );

    PageObjectLogging.log(
        "Navigation menu",
        "is visible",
        true
    );

    boolean result = nav.isNavListElementEllipsized(0);
    PageObjectLogging.log(
        "Link without chevron",
        "is ellipsized",
        "is not ellipsized",
        result
    );

    result = nav.isNavListElementEllipsized(1);
    PageObjectLogging.log(
        "Link with chevron",
        "is ellipsized",
        "is not ellipsized",
        result
    );

    Assertion.assertFalse(
        nav.isBackLinkDisplayed(),
        "Back link is displayed"
    );

    PageObjectLogging.log(
        "Back link",
        "is hidden",
        true
    );

    result = nav.isRandomPageButtonDisplayed();
    PageObjectLogging.log(
        "Random page button",
        "is displayed",
        "is hidden",
        result
    );

    nav.clickNavListElement(2);

    Assertion.assertTrue(
        nav.isBackLinkDisplayed(),
        "Back link isn't displayed"
    );

    PageObjectLogging.log(
        "Back link",
        "is displayed",
        true
    );

    result = !nav.isRandomPageButtonDisplayed();
    PageObjectLogging.log(
        "Random page button",
        "is hidden",
        "is displayed",
        result
    );

    nav.clickBackChevron();

    Assertion.assertFalse(
        nav.isBackLinkDisplayed(),
        "Back link doesn't work"
    );

    PageObjectLogging.log(
        "Back link",
        "works",
        true
    );

    nav.tapOverlay();

    result = !nav.isNavMenuVisible();
    PageObjectLogging.log(
        "Navigation menu",
        "is hidden",
        "is visible",
        result
    );
  }

  @Test(groups = "MercuryNavigationSideTest_002")
  public void MercuryNavigationSideTest_002_SearchInvalidSuggestion_Cancel() {
    NavigationSideComponentObject searchObject = new NavigationSideComponentObject(driver);
    searchObject.openArticleOnWikiByNameWithCbAndNoAds(wikiURL, MercurySubpages.MAIN_PAGE);

    searchObject.clickSearchButton();
    searchObject.clickSearchField();

    Assertion.assertFalse(
        searchObject.isMenuFieldVisible(),
        "Menu field is visible"
    );

    PageObjectLogging.log(
        "Menu field",
        "is hidden",
        true
    );

    Assertion.assertTrue(
        searchObject.isResultFieldVisible(),
        "Result field is hidden"
    );

    PageObjectLogging.log(
        "Result field",
        "is visible",
        true
    );

    searchObject.typeInSearchField(SEARCH_FAIL);

    boolean result = !searchObject.isSuggestionListDisplayed();
    PageObjectLogging.log(
        "Search suggestions",
        "are hidden",
        "are displayed",
        result
    );

    searchObject.typeInSearchField(SEARCH_FAIL);

    result = !searchObject.isSuggestionListDisplayed();
    PageObjectLogging.log(
        "Search suggestions",
        "are hidden",
        "are loaded",
        result
    );

    searchObject.clickCancelButton();

    result = searchObject.isMenuFieldVisible();
    PageObjectLogging.log(
        "Menu field",
        "is visible",
        "is hidden",
        result
    );

    result = !searchObject.isResultFieldVisible();
    PageObjectLogging.log(
        "Result field",
        "is hidden",
        "is visible",
        result
    );
  }

  @Test(groups = "MercuryNavigationSideTest_003")
  public void MercuryNavigationSideTest_003_ValidSuggestionRedirect() {
    NavigationSideComponentObject searchObject = new NavigationSideComponentObject(driver);
    searchObject.openArticleOnWikiByNameWithCbAndNoAds(wikiURL, MercurySubpages.MAIN_PAGE);

    searchObject.clickSearchButton();
    searchObject.clickSearchField();
    searchObject.typeInSearchField(SEARCH_PASS);

    Assertion.assertTrue(
        searchObject.isSuggestionListDisplayed(),
        "Search suggestions are hidden"
    );

    PageObjectLogging.log(
        "Search suggestions",
        "are displayed",
        true
    );

    String oldUrl = driver.getCurrentUrl();
    searchObject.clickSuggestion(0);
    searchObject.waitForLoadingOverlayToDisappear();

    boolean result = !oldUrl.equals(driver.getCurrentUrl());
    PageObjectLogging.log(
        "Redirection",
        "works",
        "does not work",
        result
    );
  }

  // TODO: check article titles instead of url, check that receive at least 3 different articles in 10 attempts
  @RelatedIssue(issueID = "HG-686")
  @Test(groups = "MercuryNavigationSideTest_004", enabled = false)
  public void MercuryNavigationSideTest_004_RandomPageRedirect() {
    NavigationSideComponentObject nav = new NavigationSideComponentObject(driver);
    nav.openArticleOnWikiByNameWithCbAndNoAds(wikiURL, MercurySubpages.MAIN_PAGE);

    nav.clickSearchButton();
    String oldUrl = driver.getCurrentUrl();
    nav.clickRandomPageButton();
    nav.waitForLoadingOverlayToDisappear();

    boolean result = !oldUrl.equals(driver.getCurrentUrl());
    PageObjectLogging.log(
        "Redirection",
        "works",
        "does not work",
        result
    );
  }
}
