package com.sample;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import com.sample.facts.Power;

public class ProduceTestCase {
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		// String filepath = "src/main/resources/dtables/NaritaDomesticRule.xls";
		// String filepath = "src/main/resources/dtables/Sample.xls";
		String filepath = "src/main/resources/dtables/電力料金.xls";
		
		Logger logger = Logger.getLogger(ProduceTestCase.class.getName());
		
		Map<String, RuleInfo> conditionColumns = DTable.getRuleInfo(new File(filepath));
		Map<String, List<List<String>>> res = new HashMap<String, List<List<String>>>();
		
		for (RuleInfo ruleInfo : conditionColumns.values()) {
			Map<String, List<String>> map = ruleInfo.getMap();
			//logger.log(Level.INFO, map);
						
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
		
		Map<String, Power> map = new HashMap<String, Power>();
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-dtables");

            List<List<String>> fact = new ArrayList<List<String>>();
            fact = res.get("RuleTable 電力量料金表");
            for (int n = 0; n < fact.size(); n++) {
            	List<String> ele = fact.get(n);
            	if(ele.isEmpty()) continue;
	            Power power = new Power();
	            power.set氏名(Integer.toString(n));
	            if (ele.size() >= 2) {
	            	power.set契約電流(ele.get(0));
	            	power.set電力量(Double.parseDouble(ele.get(1)));
	            } else {
	            	if (ele.get(0) != null ) {
	            		power.set契約電流(ele.get(0));
	            	} else {
	            		power.set契約電流("");
	            	}
	            	power.set電力量(0.0);
	            }
	            kSession.insert(power);
	            kSession.fireAllRules();
	            
	            System.out.println("電力量料金 : " + power.get電力量料金());
	            System.out.println(power);
	            
	            if (power.getRule() != null && map.get(power.getRule()) == null)
	            	map.put(power.getRule(), power);
	            
	            FactHandle handle = kSession.getFactHandle(power);
	            kSession.delete(handle);
            }
            
            File file = new File("TestData.csv");
            PrintWriter writer = new PrintWriter(file);
            writer.println("Name, Contract, Power amount");
            map.entrySet().stream()
            	.sorted(java.util.Map.Entry.comparingByKey())
            	.forEach(s -> writer.println(s.getValue().toCsv()));
            writer.flush();
            
        } catch (Throwable t) {
            t.printStackTrace();
        }
	}
}
