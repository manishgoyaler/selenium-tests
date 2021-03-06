package com.wikia.webdriver.testcases.adstests;

import com.wikia.webdriver.common.dataprovider.ads.AdsDataProvider;
import com.wikia.webdriver.common.templates.TemplateNoFirstLoad;
import com.wikia.webdriver.pageobjectsfactory.pageobject.adsbase.AdsBaseObject;

import org.testng.annotations.Test;

import java.util.List;

/**
 * https://www.google.com/dfp/5441#delivery/LineItemDetail/lineItemId=115974612
 */
public class TestDfpParamsPresent extends TemplateNoFirstLoad {

  private static final String LINE_ITEM_ID = "115974612";
  private static final String CREATIVE_ID = "37674198492";

  @Test(
      dataProviderClass = AdsDataProvider.class,
      dataProvider = "dfpParamsSyntheticOasis",
      groups = {"DfpParamsPresentSyntheticOasis", "Ads"}
  )
  public void dfpParamsPresentSyntheticOasis(String wikiName,
                                             String article,
                                             String adUnit,
                                             String slot,
                                             List<String> pageParams,
                                             List<String> slotParams) {
    String testedPage = urlBuilder.getUrlForPath(wikiName, article);
    AdsBaseObject ads = new AdsBaseObject(driver, testedPage);
    ads.verifyGptIframe(adUnit, slot, "gpt");
    ads.verifyGptParams(slot, pageParams, slotParams);
    ads.verifyGptAdInSlot(slot, LINE_ITEM_ID, CREATIVE_ID);

  }

  @Test(
      dataProviderClass = AdsDataProvider.class,
      dataProvider = "dfpParamsOasis",
      groups = {"DfpParamsPresentOasis", "Ads"}
  )
  public void dfpParamsPresentOasis(String wikiName,
                                    String article,
                                    String adUnit,
                                    String slot,
                                    List<String> pageParams,
                                    List<String> slotParams) {
    String testedPage = urlBuilder.getUrlForPath(wikiName, article);
    AdsBaseObject ads = new AdsBaseObject(driver, testedPage);
    ads.verifyGptIframe(adUnit, slot, "gpt");
    ads.verifyGptParams(slot, pageParams, slotParams);
  }
}
