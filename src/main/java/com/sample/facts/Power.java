package com.sample.facts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Power {
	private String 氏名;
	private String 契約電流;
	private double 電力量;
	private double 基本料金;
	private double 電力量料金;
	private String rule;
	
	public String toCsv() {
		return get氏名() + "," + get契約電流() + "," + get電力量() + "," + getRule();
	}
}
