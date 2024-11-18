package com.luanmxz.adapters;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverHandler {

  public WebDriverHandler() {
  }

  public WebDriver createWebDriver() {
    return WebDriverManager.firefoxdriver().create();

  }

  public WebDriverWait createWait(WebDriver driver) {
    return new WebDriverWait(driver, Duration.ofSeconds(30), Duration.ofSeconds(2));
  }
}
