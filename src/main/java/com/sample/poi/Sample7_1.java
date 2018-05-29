package com.sample.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class Sample7_1{
  public static void main(String[] args){
    FileInputStream in = null;
    Workbook wb = null;

    try{
      in = new FileInputStream("sample.xls");
      wb = WorkbookFactory.create(in);
    }catch(IOException e){
      System.out.println(e.toString());
    }catch(InvalidFormatException e){
      System.out.println(e.toString());
    }finally{
      try{
        in.close();
      }catch (IOException e){
        System.out.println(e.toString());
      }
    }

    Sheet sheet = wb.getSheetAt(0);

    for (int i = 1 ; i < 8 ; i++){
      Row row = sheet.getRow(i);
      if (row == null){
        row = sheet.createRow(i);
      }

      Cell cell1 = row.getCell(1);
      if (cell1 == null){
        cell1 = row.createCell(1);
      }

      switch(cell1.getCellType()) {
      case Cell.CELL_TYPE_STRING:
        System.out.println("String:" + cell1.getStringCellValue());
        break;
      case Cell.CELL_TYPE_NUMERIC:
        if(DateUtil.isCellDateFormatted(cell1)) {
          System.out.println("Date:" + cell1.getDateCellValue());
        } else {
          System.out.println("Numeric:" + cell1.getNumericCellValue());
        }
        break;
      case Cell.CELL_TYPE_BOOLEAN:
        System.out.println("Boolean:" + cell1.getBooleanCellValue());
        break;
      case Cell.CELL_TYPE_FORMULA:
        System.out.println("Formula:" + cell1.getCellFormula());
        break;
      case Cell.CELL_TYPE_ERROR :
        System.out.println("Error:" + cell1.getErrorCellValue());
        break;
      case Cell.CELL_TYPE_BLANK  :
        System.out.println("Blank:");
        break;
      }
    }
  }
}
