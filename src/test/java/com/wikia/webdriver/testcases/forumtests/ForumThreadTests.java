package com.wikia.webdriver.testcases.forumtests;

import com.wikia.webdriver.common.contentpatterns.PageContent;
import com.wikia.webdriver.common.core.configuration.Configuration;
import com.wikia.webdriver.common.properties.Credentials;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.pageobjectsfactory.pageobject.forumpageobject.ForumBoardPageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.forumpageobject.ForumHistoryPageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.forumpageobject.ForumPageObject;
import com.wikia.webdriver.pageobjectsfactory.pageobject.forumpageobject.ForumThreadPageObject;

import org.testng.annotations.Test;

import java.util.List;

public class ForumThreadTests extends NewTestTemplate {

  private String title;
  private String message;
  Credentials credentials = Configuration.getCredentials();

  @Test(groups = {"ForumThreadTests_001", "ForumThreadTests", "Forum", "Smoke3"})
  public void ForumThreadTests_001_replyToThread() {
    ForumPageObject forumMainPage = new ForumPageObject(driver);
    forumMainPage.loginAs(credentials.userNameStaff, credentials.passwordStaff, wikiURL);
    title = PageContent.FORUM_TITLE_PREFIX + forumMainPage.getTimeStamp();
    message = PageContent.FORUM_MESSAGE + forumMainPage.getTimeStamp();
    forumMainPage.openForumMainPage(wikiURL);
    ForumBoardPageObject forumBoard = forumMainPage.openForumBoard();
    ForumThreadPageObject forumThread = forumBoard.startDiscussion(title, message, false);
    forumThread.verifyDiscussionTitleAndMessage(title, message);
    forumThread.reply(message);
    forumThread.verifyReplyMessage(1, message);
  }

  @Test(groups = {"ForumThreadTests_002", "ForumThreadTests", "Forum"})
  public void ForumThreadTests_002_removeThreadAndUndo() {
    ForumPageObject forumMainPage = new ForumPageObject(driver);
    forumMainPage.loginAs(credentials.userNameStaff, credentials.passwordStaff, wikiURL);
    title = PageContent.FORUM_TITLE_PREFIX + forumMainPage.getTimeStamp();
    message = PageContent.FORUM_MESSAGE + forumMainPage.getTimeStamp();
    forumMainPage.openForumMainPage(wikiURL);
    ForumBoardPageObject forumBoard = forumMainPage.openForumBoard();
    ForumThreadPageObject forumThread = forumBoard.startDiscussion(title, message, false);
    forumThread.verifyDiscussionTitleAndMessage(title, message);
    forumThread.removeThread("QA reason");
    forumThread.verifyThreadRemoved();
    forumThread.undoRemove();
    forumThread.verifyDiscussionTitleAndMessage(title, message);
  }

  @Test(groups = {"ForumThreadTests_003", "ForumThreadTests", "Forum"})
  public void ForumThreadTests_003_moveThreadToOtherBoard() {
    ForumPageObject forumMainPage = new ForumPageObject(driver);
    forumMainPage.loginAs(credentials.userNameStaff, credentials.passwordStaff, wikiURL);
    title = PageContent.FORUM_TITLE_PREFIX + forumMainPage.getTimeStamp();
    message = PageContent.FORUM_MESSAGE + forumMainPage.getTimeStamp();
    forumMainPage.openForumMainPage(wikiURL);
    List<String> forumNames = forumMainPage.getForumNamesList();
    ForumBoardPageObject forumBoard = forumMainPage.openForumBoard();
    ForumThreadPageObject forumThread = forumBoard.startDiscussion(title, message, false);
    forumThread.verifyDiscussionTitleAndMessage(title, message);
    forumThread.moveThread(forumNames.get(1));
    forumThread.verifyParentBoard(forumNames.get(1));
  }

  @Test(groups = {"ForumThreadTests_004", "ForumThreadTests", "Forum"})
  public void ForumThreadTests_004_threadHistory() {
    ForumPageObject forumMainPage = new ForumPageObject(driver);
    forumMainPage.loginAs(credentials.userNameStaff, credentials.passwordStaff, wikiURL);
    title = PageContent.FORUM_TITLE_PREFIX + forumMainPage.getTimeStamp();
    message = PageContent.FORUM_MESSAGE + forumMainPage.getTimeStamp();
    forumMainPage.openForumMainPage(wikiURL);
    ForumBoardPageObject forumBoard = forumMainPage.openForumBoard();
    ForumThreadPageObject forumThread = forumBoard.startDiscussion(title, message, false);
    forumThread.verifyDiscussionTitleAndMessage(title, message);
    ForumHistoryPageObject forumHistory = forumThread.openHistory();
    forumHistory.verifyImportandPageElements();
  }

  @Test(groups = {"ForumThreadTests_005", "ForumThreadTests", "Forum"})
  public void ForumThreadTests_005_closeThreadAndReopen() {
    ForumPageObject forumMainPage = new ForumPageObject(driver);
    forumMainPage.loginAs(credentials.userNameStaff, credentials.passwordStaff, wikiURL);
    title = PageContent.FORUM_TITLE_PREFIX + forumMainPage.getTimeStamp();
    message = PageContent.FORUM_MESSAGE + forumMainPage.getTimeStamp();
    forumMainPage.openForumMainPage(wikiURL);
    ForumBoardPageObject forumBoard = forumMainPage.openForumBoard();
    ForumThreadPageObject forumThread = forumBoard.startDiscussion(title, message, false);
    forumThread.verifyDiscussionTitleAndMessage(title, message);
    forumThread.closeThread(PageContent.CLOSE_REASON);
    forumThread.verifyThreadClosed();
    forumThread.reopenThread();
    forumThread.verifyThreadReopened();
  }
}
