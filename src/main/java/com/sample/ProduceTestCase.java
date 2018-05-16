package com.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

public class ProduceTestCase {
	
	public static void main(String[] args) {
		
		Logger logger = Logger.getLogger(ProduceTestCase.class.getName());
		
		try {
			Map<String, List<String>> map = CsvReader.CsvToMap("sample.csv");
			logger.log(Level.INFO, map);
						
			List<Supplier<Stream<String>>> list = new ArrayList<Supplier<Stream<String>>>();
			
			map.keySet().stream().forEach(System.out::println);
			map.values().stream().forEach(l -> {
				list.add(()->l.stream());
			});
			Cartesian.go((a, b) -> a + "," + b, list)
							.forEach(System.out::println);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
