package com.sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;

public class CountRule {

	public static void main(String[] args) {
		String file = "countrule.csv";
		try {
			PrintWriter writer = new PrintWriter(new File(file));
	        KieServices ks = KieServices.Factory.get();
		    KieContainer kContainer = ks.getKieClasspathContainer();
		    
		    //RuleCount(kContainer, writer);
		    ruleCount(kContainer, writer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void RuleCount(KieContainer kContainer, PrintWriter writer) {
		Collection<String> kbases = kContainer.getKieBaseNames();
		Iterator<String> ikbases = kbases.iterator();
		writer.println("KieBase, Package, Rule#");
		while(ikbases.hasNext()) {
			String kbasename = ikbases.next();
			KieBase kBase = kContainer.getKieBase(kbasename);
		
		    Collection<KiePackage> kPkgs = kBase.getKiePackages();
		    Iterator<KiePackage> iter = kPkgs.iterator();
		    while(iter.hasNext()) {
		    	KiePackage kPkg = iter.next();
		    	Collection<Rule> rules = kPkg.getRules();
		    	writer.println(kbasename + "," + kPkg.getName() + "," + rules.size());
		    	System.out.println(kbasename + "," + kPkg.getName() + "," + rules.size());
		    }
		}
		writer.flush();
	}
	
	public static void ruleCount(KieContainer kContainer, PrintWriter writer) {
		writer.println("KieBase, Package, Rule#");
		kContainer.getKieBaseNames().stream().forEach(name -> {
			KieBase kBase = kContainer.getKieBase(name);
			kBase.getKiePackages().stream().forEach(kPkg -> {
				if (kPkg.getRules().size() > 0) {
					writer.println(name + "," + kPkg.getName() + "," + kPkg.getRules().size());
					System.out.println(name + "," + kPkg.getName() + "," + kPkg.getRules().size());
				}
			});
		});
		writer.close();
	}
}
