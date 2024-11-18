package com.luanmxz.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.luanmxz.enums.EventSelectors;

import io.github.cdimascio.dotenv.Dotenv;

public class EventPage {

  private final WebDriver webDriver;
  private final WebDriverWait wait;
  private final Dotenv env = Dotenv.load();
  private final String EVENT_PAGE_URL = env.get("EVENT_PAGE_URL");
  private final String URL_DETAIL_EVENT = env.get("URL_DETAIL_EVENT");

  public EventPage(WebDriver webDriver, WebDriverWait wait) {
    this.webDriver = webDriver;
    this.wait = wait;
  }

  public Boolean execute(String eventCode) {

    webDriver.get(EVENT_PAGE_URL);

    WebDriverWait waitForCookies = new WebDriverWait(webDriver, Duration.ofSeconds(2), Duration.ofMillis(500));

    try {
      WebElement cookiesButton = waitForCookies
          .until(ExpectedConditions.elementToBeClickable(By.xpath(EventSelectors.COOKIES_BTN_XPATH.getSelector())));
      cookiesButton.click();
    } catch (TimeoutException e) {
      // do nothing for now
    }

    WebElement codEventInput = wait
        .until(ExpectedConditions.elementToBeClickable(By.id(EventSelectors.COD_EVENT_INPUT.getSelector())));
    codEventInput.sendKeys(eventCode);

    JavascriptExecutor js = (JavascriptExecutor) webDriver;

    js.executeScript("""
        let buttons = document.getElementsByClassName('btn-default');
        buttons[1].click();
        """);

    WebDriverWait waitForTable = new WebDriverWait(webDriver, Duration.ofMinutes(3), Duration.ofSeconds(3));
    WebElement eventTable = waitForTable
        .until(ExpectedConditions.elementToBeClickable(By.className(EventSelectors.TABLE_CLASSNAME.getSelector())));
    WebElement tdStatus = eventTable.findElements(By.tagName("td")).get(2);

    String eventStatus = tdStatus.getText();

    if (eventStatus.equalsIgnoreCase(EventSelectors.CONSOLIDADO.getSelector())) {
      return true;
    }

    webDriver.get(URL_DETAIL_EVENT.concat(eventCode));
    return false;
  }

}
