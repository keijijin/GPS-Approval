package com.sample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.drools.compiler.compiler.io.Resource;
import org.drools.core.io.impl.FileSystemResource;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.drools.decisiontable.parser.DefaultRuleSheetListener;
import org.drools.decisiontable.parser.xls.ExcelParser;
import org.drools.template.parser.DataListener;
import org.drools.template.parser.TemplateDataListener;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

public class DTable {
	static final String RULETABLE = "RuleTable";
	static final String CONDITION = "CONDITION";

	public static Map<String, RuleInfo> getRuleInfo(File file) {
		Map<String, RuleInfo> conditionColumns = new HashMap<String, RuleInfo>();

		Logger logger = Logger.getLogger(DTable.class.getName());

		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(file);
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);
		
		String ruleTableName = "";
		
		int rowNum = sheet.getLastRowNum();
		for ( int i = 0; i < rowNum; i++ ) {
			Row row = sheet.getRow(i);
			if ( row == null ) {
				if ( ruleTableName != "") {
					RuleInfo ri = conditionColumns.get(ruleTableName);
					ri.setLastRow(i - 1);
				}
				continue;
			}
			int cellNum = row.getLastCellNum();
			for (int n = 0; n < cellNum; n++) {
				Cell cell = row.getCell(n);
				if (cell == null) continue;
				String cellValue = cell.getStringCellValue();
				if ( cellValue.startsWith(RULETABLE)) {
					ruleTableName = cellValue;
					conditionColumns.put(cellValue, new RuleInfo(cellValue));
				} else if (cellValue.equals(CONDITION)) {
					RuleInfo ruleInfo = conditionColumns.get(ruleTableName);
					ruleInfo.getConditionColumns().add(n);
					conditionColumns.put(ruleTableName, ruleInfo);
					ruleInfo.setStartRow(i + 4);
					ruleInfo.setLastRow(sheet.getLastRowNum()-1);
				}
			}
		}
					
		for(RuleInfo rule : conditionColumns.values()) {
			for (int i = rule.getStartRow(); i <= rule.getLastRow(); i++) {
				Row row = sheet.getRow(i);
				if ( row == null ) continue;
				for (Integer n : rule.getConditionColumns()) {
					Cell cell = row.getCell(n);
					String value = cell.getStringCellValue();
					if (value == null || value.equals("")) continue;
					Map<String, List<String>> map = rule.getMap();
					if ( !map.containsKey(Integer.toString(n))) {
						map.put(Integer.toString(n), new ArrayList<String>(Arrays.asList("")));
					}
					List<String> list = map.get(Integer.toString(n));
					if ( !list.contains(value) )
						list.add(value);
					map.put(Integer.toString(n), list);
				}
			}
		}
		logger.log(Level.INFO, conditionColumns);
		return conditionColumns;
	}
	
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(DTable.class.getName());

		String filepath = "src/main/resources/dtables/DealerClasificationRule.xlsx";
		// String filepath = "src/main/resources/dtables/Sample.xls";
		// String filepath = "src/main/resources/dtables/IncentiveDetailRule.xlsx";

		Map<String, RuleInfo> conditionColumns = getRuleInfo(new File(filepath));		
			
		for (RuleInfo rule : conditionColumns.values()) {
			logger.log(Level.INFO, rule);
			Map<String, List<String>> map = rule.getMap();
				
			List<Supplier<Stream<String>>> list = new ArrayList<Supplier<Stream<String>>>();
			//map.keySet().stream().forEach(System.out::println);
			map.values().stream().forEach(l -> {
				list.add(()->l.stream());
			});
			Cartesian.go((a, b) -> a + "," + b, list)
							.forEach(System.out::println);
		}
	}
}
