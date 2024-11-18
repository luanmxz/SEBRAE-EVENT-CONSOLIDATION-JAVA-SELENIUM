package com.luanmxz.context;

import com.luanmxz.adapters.WebDriverHandler;
import com.luanmxz.services.EventFileService;

public class ApplicationContext {
  public static final WebDriverHandler webDriverHandler = new WebDriverHandler();
  public static final EventFileService eventFileHandler = new EventFileService();
}
