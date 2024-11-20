package com.luanmxz.services;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.luanmxz.enums.EventStatusEnum;

import io.github.cdimascio.dotenv.Dotenv;

public class EventFileService {
  private static final int EVENT_CODE_COLUMN = 0;
  private static final int STATUS_COLUMN = 1;
  private final Dotenv env = Dotenv.load();

  private Map<Integer, String> readEventsFromExcel(String filePath) throws IOException {

    Map<Integer, String> events = new HashMap<Integer, String>();
    FileInputStream fileInputStream = new FileInputStream(filePath);
    Workbook workbook = new XSSFWorkbook(fileInputStream);
    Sheet sheet = workbook.getSheetAt(0);
    DataFormatter formatter = new DataFormatter();

    for (Row row : sheet) {

      if (row.getRowNum() < 1)
        continue;

      Cell cell = row.getCell(EVENT_CODE_COLUMN);
      String event = formatter.formatCellValue(cell);

      Cell statusCell = row.getCell(STATUS_COLUMN);
      String eventStatus = formatter.formatCellValue(statusCell);

      Integer rowNumber = row.getRowNum();

      if (!eventStatus.equals(EventStatusEnum.CONSOLIDADO.getStatus())) {
        events.put(rowNumber, event);
      }
    }

    workbook.close();
    fileInputStream.close();
    return events;
  }

  public Map<Integer, Map<Integer, String>> getEventsListPaginated(Integer pageSize) {
    Map<Integer, String> events = new HashMap<Integer, String>();
    try {
      events = readEventsFromExcel(env.get("FILE_PATH"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Map<Integer, Map<Integer, String>> eventsMap = new HashMap<>();

    int numberOfThreads = pageSize;
    int totalSize = events.size();
    int batchSize = (int) Math.ceil((double) totalSize / numberOfThreads);

    for (int i = 0; i < numberOfThreads; i++) {
      int start = i * batchSize;
      int end = Math.min(start + batchSize, totalSize);

      if (start < totalSize) {
        Map<Integer, String> batchMap = new HashMap<>();
        for (int j = start + 1; j <= end; j++) {
          if (events.containsKey(j)) {
            batchMap.put(j, events.get(j));
          }
        }
        eventsMap.put(i, batchMap);
      }
    }

    return eventsMap;
  }

  public synchronized void updateEventStatus(String filePath, String eventCode, Integer rowNum, String status) {
    try (FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis)) {

      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          if (fis != null) {
            fis.reset();
            fis.close();
          }

          if (workbook != null) {
            workbook.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }));

      Sheet sheet = workbook.getSheetAt(0);

      Row row = sheet.getRow(rowNum);

      Cell eventCell = row.getCell(EVENT_CODE_COLUMN);
      String currentEvent = new DataFormatter().formatCellValue(eventCell);

      if (currentEvent.equals(eventCode)) {
        Cell statusCell = row.getCell(STATUS_COLUMN);
        if (statusCell == null) {
          statusCell = row.createCell(STATUS_COLUMN);
        }

        statusCell.setCellValue(status);
      }

      try (FileOutputStream fos = new FileOutputStream(filePath)) {
        workbook.write(fos);
      }

    } catch (IOException e) {
      System.err.println("Error updating event status: " + e.getMessage());
    }
  }
}
