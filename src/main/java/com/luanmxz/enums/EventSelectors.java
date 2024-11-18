package com.luanmxz.enums;

public enum EventSelectors {
  COD_EVENT_INPUT("txtCodEvento"),
  COOKIES_BTN_XPATH("/html/body/div[2]/div[2]/a[1]"),
  TABLE_CLASSNAME("table-responsive"),
  CONSOLIDADO("CONSOLIDADO");

  private final String selector;

  EventSelectors(String selector) {
    this.selector = selector;
  }

  public String getSelector() {
    return selector;
  }
}
