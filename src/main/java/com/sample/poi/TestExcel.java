package com.sample.poi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TestExcel {

    //適当なディレクトリに書き換えてください
    static final String INPUT_DIR = "/Users/kjin/DecisionManager/";
 
    public static void main(String[] args) {
     
        try {
             
            String xlsxFileAddress = INPUT_DIR + "Sample.xlsx";
             
            //共通インターフェースを扱える、WorkbookFactoryで読み込む
            Workbook wb = WorkbookFactory.create(new FileInputStream(xlsxFileAddress));
             
            //全セルを表示する
            int numberOfSheets = wb.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
            	Sheet sheet = (Sheet) wb.getSheetAt(i);
            	
            	Iterator<Row> rowIterator = sheet.iterator();
                while (rowIterator.hasNext()) {
                	Row row = rowIterator.next();
                    for (Cell cell : row) {
                        System.out.print(getCellValue(cell));
                        System.out.print(" , ");
                    }
                    System.out.println();
                }
            }
           
            wb.close();
             
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
             
        }
 
    }
     
     
     
    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
         
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
                 
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
                 
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
 
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
 
            default:
                return null;
 
        }
 
    }
 
}
