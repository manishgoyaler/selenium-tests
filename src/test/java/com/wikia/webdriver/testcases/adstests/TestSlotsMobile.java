package com.wikia.webdriver.testcases.adstests;

import com.wikia.webdriver.common.dataprovider.mobile.MobileAdsDataProvider;
import com.wikia.webdriver.common.templates.mobile.MobileTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.pageobject.adsbase.mobile.MobileAdsBaseObject;

import org.testng.annotations.Test;

/**
 * URL to DFP: https://www.google.com/dfp/5441#delivery/LineItemDetail/lineItemId=111889452
 * Test all slots on mobile skin with different combinations of slots
 */
public class TestSlotsMobile extends MobileTestTemplate {

  private static final String CREATIVE_IMAGE_URL = "googlesyndication.com/pagead/imgad?id=CICAgKCNj62dEhCsAhj6ASgBMgjBw3U0lR5Thg";
  private static final String MOBILE_TOP_LEADERBOARD = "MOBILE_TOP_LEADERBOARD";
  private static final String MOBILE_IN_CONTENT = "MOBILE_IN_CONTENT";
  private static final String MOBILE_PREFOOTER = "MOBILE_PREFOOTER";
  private static final String SRC = "mobile";

  @Test(
      groups = {"MobileAds", "TestAdSlotsMobile_001", "TestAdSlotsMobile"},
      dataProviderClass = MobileAdsDataProvider.class,
      dataProvider = "allSlots"
  )
  public void TestAllSlotsOnPage(String wikiName,
                                 String article,
                                 String adUnit) {

    String testedPage = urlBuilder.getUrlForPath(wikiName, article);
    MobileAdsBaseObject ads = new MobileAdsBaseObject(driver, testedPage);
    ads.verifyGptIframe(adUnit, MOBILE_TOP_LEADERBOARD, SRC);
    ads.verifyGptIframe(adUnit, MOBILE_IN_CONTENT, SRC);
    ads.verifyGptIframe(adUnit, MOBILE_PREFOOTER, SRC);
    ads.verifyImgAdLoadedInSlot(MOBILE_TOP_LEADERBOARD, CREATIVE_IMAGE_URL);
    ads.verifyImgAdLoadedInSlot(MOBILE_IN_CONTENT, CREATIVE_IMAGE_URL);
    ads.verifyImgAdLoadedInSlot(MOBILE_PREFOOTER, CREATIVE_IMAGE_URL);
  }

  @Test(
      groups = {"MobileAds", "TestAdSlotsMobile_002", "TestAdSlotsMobile"},
      dataProviderClass = MobileAdsDataProvider.class,
      dataProvider = "leaderboardAndPrefooterSlots"
  )
  public void TestLeaderboardAndPrefooterOnPage(String wikiName,
                                                String article,
                                                String adUnit) {

    String testedPage = urlBuilder.getUrlForPath(wikiName, article);
    MobileAdsBaseObject ads = new MobileAdsBaseObject(driver, testedPage);
    ads.verifyGptIframe(adUnit, MOBILE_TOP_LEADERBOARD, SRC);
    ads.verifyGptIframe(adUnit, MOBILE_PREFOOTER, SRC);
    ads.verifyImgAdLoadedInSlot(MOBILE_TOP_LEADERBOARD, CREATIVE_IMAGE_URL);
    ads.verifyImgAdLoadedInSlot(MOBILE_PREFOOTER, CREATIVE_IMAGE_URL);
    ads.verifyNoSlotPresent(MOBILE_IN_CONTENT);
  }

  @Test(
      groups = {"MobileAds", "TestAdSlotsMobile_003", "TestAdSlotsMobile"},
      dataProviderClass = MobileAdsDataProvider.class,
      dataProvider = "leaderboardAndInContentSlots"
  )
  public void TestLeaderboardAndInContentOnPage(String wikiName,
                                                String article,
                                                String adUnit) {

    String testedPage = urlBuilder.getUrlForPath(wikiName, article);
    MobileAdsBaseObject ads = new MobileAdsBaseObject(driver, testedPage);
    ads.verifyGptIframe(adUnit, MOBILE_TOP_LEADERBOARD, SRC);
    ads.verifyGptIframe(adUnit, MOBILE_IN_CONTENT, SRC);
    ads.verifyImgAdLoadedInSlot(MOBILE_TOP_LEADERBOARD, CREATIVE_IMAGE_URL);
    ads.verifyImgAdLoadedInSlot(MOBILE_IN_CONTENT, CREATIVE_IMAGE_URL);
    ads.verifyNoSlotPresent(MOBILE_PREFOOTER);
  }

  @Test(
      groups = {"MobileAds", "MercuryAds", "TestAdSlotsMobile_004", "TestAdSlotsMobile"},
      dataProviderClass = MobileAdsDataProvider.class,
      dataProvider = "mercuryConsecutivePageViews"
  )
  public void TestLeaderboardAndPrefooterOnConsecutivePageViews(String wikiName,
                                                                String firstArticle,
                                                                String secondArticle,
                                                                String thirdArticle,
                                                                String adUnit) {

    String testedPage = urlBuilder.getUrlForPath(wikiName, firstArticle);
    MobileAdsBaseObject ads = new MobileAdsBaseObject(driver, testedPage);
    ads.verifyGptIframe(adUnit, MOBILE_TOP_LEADERBOARD, SRC);
    ads.verifyGptIframe(adUnit, MOBILE_PREFOOTER, SRC);

    ads.mercuryNavigateToAnArticle(secondArticle);
    ads.waitTitleChangesTo(secondArticle);
    ads.verifyGptIframe(adUnit, MOBILE_TOP_LEADERBOARD, SRC);
    ads.verifyGptIframe(adUnit, MOBILE_PREFOOTER, SRC);

    ads.mercuryNavigateToAnArticle(thirdArticle);
    ads.waitTitleChangesTo(thirdArticle);
    ads.verifyGptIframe(adUnit, MOBILE_TOP_LEADERBOARD, SRC);
    ads.verifyGptIframe(adUnit, MOBILE_PREFOOTER, SRC);
  }
}
