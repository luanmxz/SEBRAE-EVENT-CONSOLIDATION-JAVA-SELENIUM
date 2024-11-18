package com.luanmxz.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.luanmxz.enums.DetailEventSelectors;
import com.luanmxz.enums.Messages;

public class DetailEventPage {

  private final WebDriver webDriver;
  private final WebDriverWait wait;

  public DetailEventPage(WebDriver webDriver, WebDriverWait wait) {
    this.webDriver = webDriver;
    this.wait = wait;
  }

  public Boolean execute() {

    wait.until(
        ExpectedConditions.elementToBeClickable(By.id(DetailEventSelectors.CATEGORIA_PUBLICO_CONTAINER.getSelector())))
        .findElements(By.tagName("input")).get(0);

    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      System.out.println(e.getMessage());
    }

    WebElement divInputInscricoes = wait
        .until(ExpectedConditions
            .elementToBeClickable(By.id(DetailEventSelectors.DIV_EVENTO_QTD_INSCRICOES.getSelector())));
    String classnames = divInputInscricoes.getAttribute("class");

    if (classnames.contains("escondido")) {
      WebElement tipoTurmaEvento = wait
          .until(ExpectedConditions.elementToBeClickable(By.id(DetailEventSelectors.TIPO_TURMA_EVENTO.getSelector())));
      String tipoTurma = tipoTurmaEvento.getText();

      WebElement divTiposTurma = wait.until(
          ExpectedConditions.elementToBeClickable(By.id(DetailEventSelectors.DIV_OPTS_TIPO_TURMA.getSelector())));

      if (tipoTurma.contains(DetailEventSelectors.FUNDAMENTAL.getSelector())) {

        WebElement radioFundamental = divTiposTurma.findElements(By.tagName("input")).get(0);
        radioFundamental.click();
      }

      if (tipoTurma.contains(DetailEventSelectors.MEDIO.getSelector())) {

        WebElement radioMedio = divTiposTurma.findElements(By.tagName("input")).get(1);
        radioMedio.click();
      }
    }

    WebElement inputQtdInscricoes = wait
        .until(ExpectedConditions.elementToBeClickable(By.id(DetailEventSelectors.INPUT_QTD_INSCRICOES.getSelector())));

    JavascriptExecutor jse = (JavascriptExecutor) webDriver;

    String qtdInscritos = (String) jse.executeScript("""
          let element = document.getElementById('txtQuantidadeInscricoesEvento');
          return element.value;
        """);

    if (qtdInscritos.isEmpty()) {
      WebElement maxParticipantes = wait
          .until(ExpectedConditions
              .elementToBeClickable(By.id(DetailEventSelectors.FIELD_MAX_PARTICIPANTES.getSelector())));
      String qtdMaxParticipantes = maxParticipantes.getText();

      inputQtdInscricoes.clear();
      inputQtdInscricoes.sendKeys(qtdMaxParticipantes);

      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }

      WebElement saveButton = wait
          .until(ExpectedConditions.elementToBeClickable(By.id(DetailEventSelectors.BTN_SALVAR_AGENDA.getSelector())));
      saveButton.click();

      WebElement toast = wait.until(ExpectedConditions
          .visibilityOfElementLocated(By.className(DetailEventSelectors.TOAST_MESSAGE.getSelector())));

      if (!toast.getText().equals(Messages.SUCCESS_OPERATION.getMessage())) {
        return false;
      }
    }

    try {
      WebElement consolidateButton = wait
          .until(ExpectedConditions.elementToBeClickable(By.id(DetailEventSelectors.BTN_CONSOLIDAR.getSelector())));
      consolidateButton.click();
    } catch (ElementClickInterceptedException e) {

      wait.until(ExpectedConditions
          .invisibilityOfElementLocated(By.className(DetailEventSelectors.PRE_LOADING_BACKGROUND.getSelector())));

      WebElement consolidateButton = wait
          .until(ExpectedConditions.elementToBeClickable(By.id(DetailEventSelectors.BTN_CONSOLIDAR.getSelector())));
      consolidateButton.click();
    }

    try {
      WebElement confirmButton = wait.until(
          ExpectedConditions.elementToBeClickable(By.xpath(DetailEventSelectors.CONFIRM_CONSOLIDATE.getSelector())));
      confirmButton.click();
    } catch (ElementClickInterceptedException ex) {

      wait.until(ExpectedConditions
          .invisibilityOfElementLocated(By.className(DetailEventSelectors.BOOTBOX_CONFIRM.getSelector())));

    }

    WebElement toast = wait.until(
        ExpectedConditions.visibilityOfElementLocated(By.className(DetailEventSelectors.TOAST_MESSAGE.getSelector())));
    if (toast.getText().equals(Messages.SUCCESS_CONSOLIDATION.getMessage())) {
      System.out.println(Messages.SUCCESS_CONSOLIDATION.getMessage());
      return true;
    }

    return false;
  }

}