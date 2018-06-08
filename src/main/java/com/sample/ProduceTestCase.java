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

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

public class ProduceTestCase {
	
	public static void main(String[] args) throws IOException {
		String filepath = "src/main/resources/dtables/NaritaDomesticRule.xls";
		// String filepath = "src/main/resources/dtables/Sample.xls";
		Logger logger = Logger.getLogger(ProduceTestCase.class.getName());
		
		Map<String, RuleInfo> conditionColumns = DTable.getRuleInfo(new File(filepath));
		Map<String, List<List<String>>> res = new HashMap<String, List<List<String>>>();
		
		for (RuleInfo ruleInfo : conditionColumns.values()) {
			Map<String, List<String>> map = ruleInfo.getMap();
			logger.log(Level.INFO, map);
						
			List<Supplier<Stream<String>>> list = new ArrayList<Supplier<Stream<String>>>();
			
			//map.keySet().stream().forEach(System.out::println);
			map.values().stream().forEach(l -> {
				list.add(()->l.stream());
			});
			res.put(ruleInfo.getRuleName(), new ArrayList<List<String>>());
			List<List<String>> result = res.get(ruleInfo.getRuleName());
			Cartesian.go((a, b) -> a + "," + b, list).forEach(str -> {result.add(Arrays.asList(str.split(",")));});
		}
		logger.log(Level.INFO, res);
	}
}
