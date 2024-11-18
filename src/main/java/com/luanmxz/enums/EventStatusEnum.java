package com.luanmxz.enums;

public enum EventStatusEnum {
  CONSOLIDADO("CONSOLIDADO"),
  DISPONÍVEL("DISPONÍVEL"),
  FALHA("FALHA");

  private final String status;

  EventStatusEnum(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }
}
