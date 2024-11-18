package com.luanmxz.enums;

public enum Messages {
  SUCCESS_OPERATION("Operação realizada com sucesso"),
  SUCCESS_CONSOLIDATION("Consolidação ocorreu com sucesso");

  private final String message;

  Messages(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}