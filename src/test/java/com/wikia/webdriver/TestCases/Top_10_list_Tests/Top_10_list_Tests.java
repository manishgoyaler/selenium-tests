package com.wikia.webdriver.TestCases.Top_10_list_Tests;

import org.testng.annotations.Test;

import com.wikia.webdriver.Common.Core.CommonFunctions;
import com.wikia.webdriver.Common.Core.Global;
import com.wikia.webdriver.Common.Logging.PageObjectLogging;
import com.wikia.webdriver.Common.Properties.Properties;
import com.wikia.webdriver.Common.Templates.TestTemplate;
import com.wikia.webdriver.PageObjects.PageObject.WikiBasePageObject;
import com.wikia.webdriver.PageObjects.PageObject.WikiPage.SpecialCreateBlogPageObject;
import com.wikia.webdriver.PageObjects.PageObject.WikiPage.SpecialCreateTopListPageObject;
import com.wikia.webdriver.PageObjects.PageObject.WikiPage.Top_10_list;
import com.wikia.webdriver.PageObjects.PageObject.WikiPage.WikiArticlePageObject;

public class Top_10_list_Tests extends TestTemplate {
	
	@Test(groups = { "Top_10_list_Tests_001", "Top_10_list_Tests" })
	public void Top_10_list_Tests_001_createTop10list() {
		WikiBasePageObject wiki = new WikiBasePageObject(driver, Global.DOMAIN);
		String top_10_list_Name = "Top10list" + wiki.getTimeStamp();
		String Description = "DescriptionForList";
		wiki.openWikiPage();
		WikiArticlePageObject article = new WikiArticlePageObject(driver,
				Global.DOMAIN, "random");
		CommonFunctions.logInCookie(Properties.userName, Properties.password, driver);	
		SpecialCreateTopListPageObject top10listCreation = article.createNewTop_10_list(top_10_list_Name);
		top10listCreation.verifyListName(top_10_list_Name);
		top10listCreation.addDescription(Description);
		Top_10_list top10list = top10listCreation.clickCreateList();
		top10list.verifyTop10listPageTitle(top_10_list_Name);
		top10list.verifyArticleText(Description);
	}
	
	@Test(groups = { "Top_10_list_Tests_002", "Top_10_list_Tests" })
	public void Top_10_list_Tests_002_createTop10listWithItems() {
		
		if (Global.BROWSER.equals("CHROME") || Global.BROWSER.equals("IE")) {
			PageObjectLogging.log("ACTIVE BUG 35690", "on CHROME and IE: verifyItemPresent steps are likely to fail. NOTE: the defect SOMETIMES does not occur", false, driver);
		}
		
		WikiBasePageObject wiki = new WikiBasePageObject(driver, Global.DOMAIN);
		String top_10_list_Name = "Top10list" + wiki.getTimeStamp();
		wiki.openWikiPage();
		WikiArticlePageObject article = new WikiArticlePageObject(driver,
				Global.DOMAIN, "random");
		CommonFunctions.logInCookie(Properties.userName, Properties.password, driver);	
		SpecialCreateTopListPageObject top10listCreation = article.createNewTop_10_list(top_10_list_Name);
		top10listCreation.verifyListName(top_10_list_Name);
		top10listCreation.addItem(1, "Item1");
		top10listCreation.addItem(2, "Item2");
		top10listCreation.addItem(3, "Item3");
		Top_10_list top10list = top10listCreation.clickCreateList();
		top10list.verifyTop10listPageTitle(top_10_list_Name);
		top10list.verifyItemPresent(1, "Item1");
		top10list.verifyItemPresent(2, "Item2");
		top10list.verifyItemPresent(3, "Item3");
	}
	
	@Test(groups = { "Top_10_list_Tests_003", "Top_10_list_Tests" })
	public void Top_10_list_Tests_003_anonymousEditingPermissions() {
		WikiBasePageObject wiki = new WikiBasePageObject(driver, Global.DOMAIN);
		String top_10_list_Name = "Top10list" + wiki.getTimeStamp();
		wiki.openWikiPage();
		WikiArticlePageObject article = new WikiArticlePageObject(driver,
				Global.DOMAIN, "random");
		CommonFunctions.logInCookie(Properties.userName, Properties.password, driver);	
		SpecialCreateTopListPageObject top10listCreation = article.createNewTop_10_list(top_10_list_Name);
		top10listCreation.verifyListName(top_10_list_Name);
		Top_10_list top10list = top10listCreation.clickCreateList();
		top10list.verifyTop10listPageTitle(top_10_list_Name);
		CommonFunctions.logOut(driver);
		top10list.navigateBack();
		top10list.refreshPage();
		top10list.clickEditAsAnon(top_10_list_Name);
		top10list.verifyModalLogin();

	}
}