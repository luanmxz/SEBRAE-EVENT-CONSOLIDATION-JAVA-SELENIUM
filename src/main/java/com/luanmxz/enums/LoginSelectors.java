package com.luanmxz.enums;

public enum LoginSelectors {

  USERNAME_INPUT_XPATH("/html/body/div[1]/form/div/div/div/div[2]/div/div[3]/div/div/div[2]/input"),
  PASSWORD_INPUT_XPATH("/html/body/div[1]/form/div/div/div/div[2]/div/div[3]/div/div/div[3]/input"),
  SUBMIT_BUTTON_XPATH("/html/body/div[1]/form/div/div/div/div[2]/div/div[3]/div/div/div[5]/input");

  private final String selector;

  LoginSelectors(String selector) {
    this.selector = selector;
  }

  public String getSelector() {

    return selector;
  }
}
