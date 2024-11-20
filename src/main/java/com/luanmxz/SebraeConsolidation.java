package com.luanmxz;

import com.luanmxz.context.ApplicationContext;
import com.luanmxz.services.EventProcessor;

public class SebraeConsolidation {

  public static void main(String[] args) {

    EventProcessor eventProcessor = new EventProcessor(ApplicationContext.webDriverHandler,
        ApplicationContext.eventFileService);

    eventProcessor.execute();
  }
}