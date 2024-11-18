package com.luanmxz.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.luanmxz.enums.LoginSelectors;

import io.github.cdimascio.dotenv.Dotenv;

public class LoginPage {

  private final Dotenv env = Dotenv.load();

  private final WebDriver webDriver;
  private final WebDriverWait wait;

  public LoginPage(WebDriver webDriver, WebDriverWait wait) {
    this.webDriver = webDriver;
    this.wait = wait;
  }

  public void execute() {
    webDriver.get(env.get("SAS_URL"));

    WebElement usernameInput = wait
        .until(ExpectedConditions.elementToBeClickable(By.xpath(LoginSelectors.USERNAME_INPUT_XPATH.getSelector())));
    usernameInput.sendKeys(env.get("SAS_USERNAME"));

    WebElement passwordInput = wait
        .until(ExpectedConditions.elementToBeClickable(By.xpath(LoginSelectors.PASSWORD_INPUT_XPATH.getSelector())));
    passwordInput.sendKeys(env.get("SAS_PASSWORD"));

    WebElement submitButton = wait
        .until(ExpectedConditions.elementToBeClickable(By.xpath(LoginSelectors.SUBMIT_BUTTON_XPATH.getSelector())));
    submitButton.click();

  }
}
