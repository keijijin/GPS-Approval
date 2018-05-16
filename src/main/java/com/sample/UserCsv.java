package com.sample;

import com.github.mygreen.supercsv.annotation.CsvBean;
import com.github.mygreen.supercsv.annotation.CsvColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//レコード用のPOJOクラスの定義
@Data
@AllArgsConstructor
@NoArgsConstructor
@CsvBean(header=true)
public class UserCsv {

	 @CsvColumn(number=1, label="ID")
	 private String id;
	
	 @CsvColumn(number=2, label="名前")
	 private String name;

	 @CsvColumn(number=3, label="数量")
	 private long volume;

	 @CsvColumn(number=4, label="フラグ")
	 private Boolean flag;

}