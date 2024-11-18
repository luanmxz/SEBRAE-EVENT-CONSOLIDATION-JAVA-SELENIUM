package com.luanmxz.enums;

public enum DetailEventSelectors {
  CONFIRM_CONSOLIDATE("/html/body/div[14]/div/div/div[3]/button[2]"),
  CATEGORIA_PUBLICO_CONTAINER("divContainerCategoriaPublico"),
  DIV_EVENTO_QTD_INSCRICOES("divEventoQuantidadeInscricoes"),
  TIPO_TURMA_EVENTO("spnAcao"),
  DIV_OPTS_TIPO_TURMA("divRadiosEtapaEducacao"),
  INPUT_QTD_INSCRICOES("txtQuantidadeInscricoesEvento"),
  FIELD_MAX_PARTICIPANTES("spnMaximoParticipantes"),
  BTN_SALVAR_AGENDA("btnAgendaSalvar"),
  TOAST_MESSAGE("toast-message"),
  BTN_CONSOLIDAR("btnConsolidar"),
  PRE_LOADING_BACKGROUND("preloadingBackground"),
  BOOTBOX_CONFIRM("bootbox-confirm"),
  FUNDAMENTAL("Fundamental"),
  MEDIO("Médio");

  private final String selector;

  DetailEventSelectors(String selector) {
    this.selector = selector;
  }

  public String getSelector() {
    return selector;
  }
}