package com.luanmxz.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.luanmxz.adapters.WebDriverHandler;
import com.luanmxz.enums.EventStatusEnum;
import com.luanmxz.pages.DetailEventPage;
import com.luanmxz.pages.EventPage;
import com.luanmxz.pages.LoginPage;

import io.github.cdimascio.dotenv.Dotenv;

public class EventProcessor {
  private final Map<Integer, Map<Integer, String>> eventsMap;
  private final WebDriverHandler webDriverHandler;
  private final EventFileService eventFileService;
  private final CountDownLatch latch;
  private final AtomicInteger processedEvents;

  public final Dotenv env = Dotenv.load();
  private final String FILE_PATH = env.get("FILE_PATH");

  public EventProcessor(WebDriverHandler webDriverHandler, EventFileService eventFileService) {
    this.webDriverHandler = webDriverHandler;
    this.eventFileService = eventFileService;
    this.eventsMap = this.eventFileService.getEventsListPaginated(5);
    this.latch = new CountDownLatch(eventsMap.size());
    this.processedEvents = new AtomicInteger(0);
  }

  public void execute() {
    System.out.println("Starting processing with " + eventsMap.size() + " threads");

    List<CompletableFuture<Void>> futures = new ArrayList<>();

    for (Map.Entry<Integer, Map<Integer, String>> entry : eventsMap.entrySet()) {
      CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
        String threadName = Thread.currentThread().getName();
        System.out.println("Thread " + threadName + " started - Processing batch " + entry.getKey());

        WebDriver driver = webDriverHandler.createWebDriver();
        WebDriverWait wait = webDriverHandler.createWait(driver);

        try {
          LoginPage loginPage = new LoginPage(driver, wait);
          loginPage.execute();

          for (Map.Entry<Integer, String> event : entry.getValue().entrySet()) {
            try {
              EventPage eventPage = new EventPage(driver, wait);
              Boolean isConsolidated = eventPage.execute(event.getValue());

              if (!isConsolidated) {
                DetailEventPage detailPage = new DetailEventPage(driver, wait);
                isConsolidated = detailPage.execute();
              }

              String statusConsolidation = isConsolidated ? EventStatusEnum.CONSOLIDADO.getStatus()
                  : EventStatusEnum.FALHA.getStatus();

              eventFileService.updateEventStatus(FILE_PATH, event.getValue(), event.getKey(),
                  statusConsolidation);

              int completed = processedEvents.incrementAndGet();
              System.out.printf("Thread %s: Event %s processed. Total: %d%n",
                  threadName, event, completed);

            } catch (Exception e) {
              System.err.printf("Error in thread %s processing event %s: %s%n",
                  threadName, event, e.getMessage());

              eventFileService.updateEventStatus(FILE_PATH, event.getValue(), event.getKey(),
                  EventStatusEnum.FALHA.getStatus());
            }
          }
        } finally {
          driver.quit();
          latch.countDown();
          System.out.println("Thread " + threadName + " finalized");
        }
      });

      futures.add(future);
    }

    try {
      CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
      latch.await();
      System.out.println("All threads finished. Total processed: " + processedEvents.get());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.err.println("Interruption while waiting for threads: " + e.getMessage());
    }
  }
}